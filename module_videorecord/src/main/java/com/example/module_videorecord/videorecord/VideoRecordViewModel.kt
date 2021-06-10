package com.yxztb.liblivedetection.ui.videorecord

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Point
import android.hardware.Camera
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import android.view.SurfaceHolder
import android.view.WindowManager
import androidx.lifecycle.ViewModel
import com.example.module_videorecord.util.Constant
import com.example.module_videorecord.videorecord.MediaUtils
import com.example.module_videorecord.videorecord.PictureUtils
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.concurrent.thread

/**
 * 最原始的代码，VideoRecordHelper 基于此
 */
class VideoRecordViewModel : ViewModel() {
    var mStartedFlag = false //录像中标志
    var mPlayFlag = false
    private var mRecorder: MediaRecorder? = null
    private lateinit var mCamera: Camera
    private lateinit var mMediaPlayer: MediaPlayer
    private lateinit var dirPath: String //目标文件夹地址
    lateinit var path: String //最终视频路径
    lateinit var imgPath: String //缩略图 或 拍照模式图片位置

    private var startTime: Long = 0L //起始时间毫秒
    private var stopTime: Long = 0L  //结束时间毫秒
    private var cameraReleaseEnable = true  //回收摄像头
    private var recorderReleaseEnable = false  //回收recorder
    private var playerReleaseEnable = false //回收palyer

    var mType = TYPE_VIDEO //默认为视频模式

    /**
     * init MediaPlayer
     */
    fun init() {
        mMediaPlayer = MediaPlayer()
    }

    fun onSurfaceCreate(activity: Activity, holder: SurfaceHolder) {
        val cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT
        mCamera = Camera.open(cameraId)
        mCamera.apply {
            val isFrontCamera = cameraId === Camera.CameraInfo.CAMERA_FACING_FRONT
            val rotation: Int
            rotation = if (isFrontCamera) {
                270
            } else {
                90
            }
            setDisplayOrientation(90)//旋转90度
            setPreviewDisplay(holder)
            val params = mCamera.parameters
            //注意此处需要根据摄像头获取最优像素，//如果不设置会按照系统默认配置最低160x120分辨率
            val size = getPreviewSize(activity)
            params.apply {
                setPictureSize(size.first, size.second)
                jpegQuality = 100
                parameters.focusMode = getAutoFocus()
                parameters.pictureFormat = ImageFormat.JPEG
                parameters.previewFormat = ImageFormat.NV21
            }
            parameters = params
        }
    }

    //从底层拿camera支持的previewsize，完了和屏幕分辨率做差，diff最小的就是最佳预览分辨率
    private fun getPreviewSize(activity: Activity): Pair<Int, Int> {
        var bestPreviewWidth: Int = 1920
        var bestPreviewHeight: Int = 1080
        var mCameraPreviewWidth: Int
        var mCameraPreviewHeight: Int
        var diffs = Integer.MAX_VALUE
        val windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val screenResolution = Point(display.width, display.height)
        val availablePreviewSizes = mCamera.parameters.supportedPreviewSizes
        Log.e(TAG, "屏幕宽度 ${screenResolution.x}  屏幕高度${screenResolution.y}")
        for (previewSize in availablePreviewSizes) {
            Log.v(TAG, " PreviewSizes = $previewSize")
            mCameraPreviewWidth = previewSize.width
            mCameraPreviewHeight = previewSize.height
            val newDiffs = Math.abs(mCameraPreviewWidth - screenResolution.y) + Math.abs(mCameraPreviewHeight - screenResolution.x)
            Log.v(TAG, "newDiffs = $newDiffs")
            if (newDiffs == 0) {
                bestPreviewWidth = mCameraPreviewWidth
                bestPreviewHeight = mCameraPreviewHeight
                break
            }
            if (diffs > newDiffs) {
                bestPreviewWidth = mCameraPreviewWidth
                bestPreviewHeight = mCameraPreviewHeight
                diffs = newDiffs
            }
            Log.e(TAG, "${previewSize.width} ${previewSize.height}  宽度 $bestPreviewWidth 高度 $bestPreviewHeight")
        }
        Log.e(TAG, "最佳宽度 $bestPreviewWidth 最佳高度 $bestPreviewHeight")
        return Pair(bestPreviewWidth, bestPreviewHeight)
    }

    private fun getAutoFocus(): String? {
        val parameters = mCamera.parameters
        val focusModes = parameters.supportedFocusModes
        return if ((Build.MODEL.startsWith("GT-I950") || Build.MODEL.endsWith("SCH-I959") || Build.MODEL.endsWith("MEIZU MX3"))
                && focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO
        } else {
            Camera.Parameters.FOCUS_MODE_FIXED
        }
    }

    fun onSurfaceChanged() {
        mCamera.apply {
            startPreview()
            cancelAutoFocus()
            // 关键代码 该操作必须在开启预览之后进行（最后调用），
            // 否则会黑屏，并提示该操作的下一步出错
            // 只有执行该步骤后才可以使用MediaRecorder进行录制
            // 否则会报 MediaRecorder(13280): start failed: -19
            unlock()
        }
        cameraReleaseEnable = true
    }

    fun startRecord(activity: Activity) {
        recorderReleaseEnable = true
        if (mRecorder == null) {
            mRecorder = MediaRecorder().apply {
                reset()
                setCamera(mCamera)
                // 设置音频源与视频源 这两项需要放在setOutputFormat之前
                setAudioSource(MediaRecorder.AudioSource.CAMCORDER)
                setVideoSource(MediaRecorder.VideoSource.CAMERA)
                //设置输出格式
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                //这两项需要放在setOutputFormat之后 IOS必须使用ACC
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)  //音频编码格式
                //使用MPEG_4_SP格式在华为P20 pro上停止录制时会出现
                //MediaRecorder: stop failed: -1007
                //java.lang.RuntimeException: stop failed.
                // at android.media.MediaRecorder.stop(Native Method)
                setVideoEncoder(MediaRecorder.VideoEncoder.H264)  //视频编码格式
                //设置最终出片分辨率
//                setVideoSize(640, 480)
                // 微信小视频的尺寸是320*240
                setVideoSize(VideoRecordConfig.video_width, VideoRecordConfig.video_height)
//                setVideoFrameRate(30)
                setVideoFrameRate(VideoRecordConfig.video_frame_rate)
//                setVideoEncodingBitRate(3 * 1024 * 1024)
                setVideoEncodingBitRate((VideoRecordConfig.video_encoding_bit_rate) * 1024 * 1024)
                setOrientationHint(270)
                setAudioSamplingRate(44100)//设置音频采样率为44100, 所有安卓系统都支持的才采样频率
                setAudioEncodingBitRate(96000)// 设置音频比特率为64, 音质比较好的频率:96000
                setAudioChannels(2) //设置录制的音频通道数
                //设置记录会话的最大持续时间（毫秒）
                setMaxDuration(30 * 1000)
            }
        }

        path = getVideoPath(activity)
        if (path != null) {
            var dir = File(path)
            if (!dir.exists()) {
                dir.mkdir()
            }
            dirPath = dir.absolutePath
            path = dir.absolutePath + "/" + getVideoArg() + ".mp4"
            Log.d(TAG, "文件路径： $path")

            try {
                mRecorder!!.apply {
                    setOutputFile(path)
                    prepare()
                    start()
                }
            } catch (e: java.lang.RuntimeException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            startTime = System.currentTimeMillis()  //记录开始拍摄时间
        }
    }

    fun pauseRecord() {
        try {
            mRecorder?.apply {
                stop()
                reset()
                release()
            }
            mRecorder = null
//            recorderReleaseEnable = false
            mCamera.apply {
                lock()
                stopPreview()
//                release()
            }
//            cameraReleaseEnable = false
        } catch (e: java.lang.RuntimeException) {
            e.printStackTrace()
        }
    }

    fun stopRecord(getImageForVideo: () -> Unit) {
        stopTime = System.currentTimeMillis()
        try {
            mRecorder?.apply {
                stop()
                reset()
                release()
            }
            recorderReleaseEnable = false
            mCamera.apply {
                lock()
                stopPreview()
                release()
            }
            cameraReleaseEnable = false

            MediaUtils.getImageForVideo(path) {
                //获取到第一帧图片后再显示操作按钮
                Log.d(TAG, "获取到了第一帧")
                imgPath = it.absolutePath
                getImageForVideo.invoke()
            }
        } catch (e: java.lang.RuntimeException) {
            //当catch到RE时，说明是录制时间过短
            Log.e(TAG, "拍摄时间过短" + e.message)
            mRecorder?.apply {
                reset()
                release()
            }
            recorderReleaseEnable = false
        }
    }

    /**
     * @param getImageForVideo : 获取第一帧后回调
     * @param errorCallback: 失败回调
     */
    fun stopRecord(getImageForVideo: () -> Unit, errorCallback: () -> Unit) {
        stopTime = System.currentTimeMillis()
//          方法1 ： 延时确保录制时间大于1s
//            if (stopTime-startTime<1100) {
//                Thread.sleep(1100+startTime-stopTime)
//            }
//            mRecorder.stop()
//            mRecorder.reset()
//            mRecorder.release()
//            recorderReleaseEnable = false
//            mCamera.lock()
//            mCamera.stopPreview()
//            mCamera.release()
//            cameraReleaseEnable = false
//            mBtnPlay.visibility = View.VISIBLE
//            MediaUtils.getImageForVideo(path) {
//                //获取到第一帧图片后再显示操作按钮
//                Log.d(TAG,"获取到了第一帧")
//                imgPath=it.absolutePath
//                mLlRecordOp.visibility = View.VISIBLE
//            }


//          方法2 ： 捕捉异常改为拍照
        try {
            mRecorder?.apply {
                stop()
                reset()
                release()
            }
            recorderReleaseEnable = false
            mCamera.apply {
                lock()
                stopPreview()
                release()
            }
            cameraReleaseEnable = false

            MediaUtils.getImageForVideo(path) {
                //获取到第一帧图片后再显示操作按钮
                Log.d(TAG, "获取到了第一帧")
                imgPath = it.absolutePath
                getImageForVideo.invoke()
            }
        } catch (e: java.lang.RuntimeException) {
            //当catch到RE时，说明是录制时间过短，此时将由录制改变为拍摄
            mType = TYPE_IMAGE
            Log.e(TAG, "拍摄时间过短" + e.message)
            mRecorder?.apply {
                reset()
                release()
            }
            recorderReleaseEnable = false
            mCamera.takePicture(null, null, Camera.PictureCallback { data, camera ->
                data?.let {
                    saveImage(it) { imagepath ->
                        Log.d(TAG, "转为拍照，获取到图片数据 $imagepath")
                        imgPath = imagepath
                        mCamera.apply {
                            lock()
                            stopPreview()
                            release()
                        }
                        cameraReleaseEnable = false
                        errorCallback.invoke()
                    }
                }
            })
        }
    }

    fun onDestroy() {
        if (recorderReleaseEnable) mRecorder?.release()
        if (cameraReleaseEnable) {
            mCamera.stopPreview()
            mCamera.release()
        }
        if (playerReleaseEnable) {
            mMediaPlayer.release()
        }
    }

    fun playRecord(activity: Context, mSurfaceHolder: SurfaceHolder, completionListener: MediaPlayer.OnCompletionListener) {
        //修复录制时home键切出再次切回时无法播放的问题
        if (cameraReleaseEnable) {
            Log.d(TAG, "回收摄像头资源")
            mCamera.apply {
                lock()
                stopPreview()
                release()
            }
            cameraReleaseEnable = false
        }
        playerReleaseEnable = true
        mPlayFlag = true


        mMediaPlayer.reset()

        // 解决 FileNotFoundException: No content provider: MediaPlayer.create ->  setDataSource
//        var uri = Uri.parse(path)
//        mMediaPlayer = MediaPlayer.create(activity, uri)
        mMediaPlayer = MediaPlayer()
        mMediaPlayer.apply {
            setDataSource(path)
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setDisplay(mSurfaceHolder)
            setOnCompletionListener(completionListener)
        }
        try {
            mMediaPlayer.prepare()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mMediaPlayer.start()
    }

    fun stopPlay() {
        if (mMediaPlayer.isPlaying) {
            mMediaPlayer.stop()
        }
    }

    /**
     * 获取系统时间
     * @return
     */
    fun getDate(): String {
        var ca = Calendar.getInstance()
        var year = ca.get(Calendar.YEAR)           // 获取年份
        var month = ca.get(Calendar.MONTH)         // 获取月份
        var day = ca.get(Calendar.DATE)            // 获取日
        var minute = ca.get(Calendar.MINUTE)       // 分
        var hour = ca.get(Calendar.HOUR)           // 小时
        var second = ca.get(Calendar.SECOND)       // 秒
        return "" + year + (month + 1) + day + hour + minute + second
    }

    /**
     * 视频路径
     */
    fun getVideoPath(activity: Activity): String {
        return Constant.dirName(activity)
    }

    /**
     * 命名参数
     */
    fun getVideoArg(): String {
        return "${VideoRecordConfig.video_width}-${VideoRecordConfig.video_height}-" +
                "${VideoRecordConfig.video_frame_rate}-${VideoRecordConfig.video_encoding_bit_rate}-"
    }


    /**
     * @Description
     * @Author Junerver
     * Created at 2019/5/23 15:13
     * @param data   从摄像头拍照回调获取的字节数组
     * @param onDone 保存图片完毕后的回调函数
     * @return
     */
    fun saveImage(data: ByteArray, onDone: (path: String) -> Unit) {
        thread {
            //方式1 ：java nio 保存图片 保存后的图片存在0度旋转角
//            val imgFileName = "IMG_" + getDate() + ".jpg"
//            val imgFile = File(dirPath + File.separator + imgFileName)
//            val outputStream = FileOutputStream(imgFile)
//            val fileChannel = outputStream.channel
//            val buffer = ByteBuffer.allocate(data.size)
//            try {
//                buffer.put(data)
//                buffer.flip()
//                fileChannel.write(buffer)
//            } catch (e: IOException) {
//                Log.e("写图片失败", e.message)
//            } finally {
//                try {
//                    outputStream.close()
//                    fileChannel.close()
//                    buffer.clear()
//                } catch (e: IOException) {
//                    Log.e("关闭图片失败", e.message)
//                }
//            }

            //方式2： bitmap保存 将拍摄结果旋转90度
            val imgFileName = "IMG_" + getDate() + ".jpg"
            val imgFile = File(dirPath + File.separator + imgFileName)
            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            val newBitmap = PictureUtils.rotateBitmap(bitmap, 90)
            imgFile.createNewFile()
            val os = BufferedOutputStream(FileOutputStream(imgFile))
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()
            val degree = PictureUtils.getBitmapDegree(imgFile.absolutePath)
            Log.d(TAG, "图片角度为：$degree")
            onDone(imgFile.absolutePath)
        }
    }

    companion object {
        const val TYPE_VIDEO = 0  //视频模式
        const val TYPE_IMAGE = 1  //拍照模式

        const val TAG = "VideoRecordViewModel"
    }
}