package com.yxztb.liblivedetection.ui.videorecord

import android.app.Activity
import android.content.Context
import android.graphics.ImageFormat
import android.graphics.Point
import android.hardware.Camera
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import android.view.SurfaceHolder
import android.view.WindowManager
import com.example.module_videorecord.util.Constant
import com.example.module_videorecord.videorecord.MediaUtils
import java.io.File

/**
 * VideoRecordFragment + VideoRecordViewModel 自带界面
 * 自定义界面可使用 VideoRecordHelper ，封装了视频录制相关操作
 */
class VideoRecordHelper {
    companion object {
        const val STATUS_NONE = 1
        const val STATUS_STARTING = 2
        const val STATUS_STOPPING = 3
        const val STATUS_STOPPED = 4

    }
    private var mRecorder: MediaRecorder? = null
    private var mCamera: Camera? = null

    private var cameraReleaseEnable = true  //回收摄像头
    private var recorderReleaseEnable = false  //回收recorder

    lateinit var dirPath: String //目标文件夹地址
    lateinit var path: String //最终视频路径
    lateinit var imgPath: String //缩略图 或 拍照模式图片位置

    private var startTime: Long = 0L //起始时间毫秒
    private var stopTime: Long = 0L  //结束时间毫秒
    var mStartedFlag = STATUS_NONE //是否开始

    /**
     * 创建 Camera
     */
    fun onSurfaceCreate(activity: Activity, holder: SurfaceHolder) {
        val cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT
        mCamera = Camera.open(cameraId)
        mCamera?.apply {
            val isFrontCamera = cameraId === Camera.CameraInfo.CAMERA_FACING_FRONT
            setDisplayOrientation(90)//旋转90度
            setPreviewDisplay(holder)
            val params = mCamera!!.parameters
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
        val availablePreviewSizes = mCamera!!.parameters.supportedPreviewSizes
        Log.e(VideoRecordViewModel.TAG, "屏幕宽度 ${screenResolution.x}  屏幕高度${screenResolution.y}")
        for (previewSize in availablePreviewSizes) {
            Log.v(VideoRecordViewModel.TAG, " PreviewSizes = $previewSize")
            mCameraPreviewWidth = previewSize.width
            mCameraPreviewHeight = previewSize.height
            val newDiffs = Math.abs(mCameraPreviewWidth - screenResolution.y) + Math.abs(mCameraPreviewHeight - screenResolution.x)
            Log.v(VideoRecordViewModel.TAG, "newDiffs = $newDiffs")
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
            Log.e(VideoRecordViewModel.TAG, "${previewSize.width} ${previewSize.height}  宽度 $bestPreviewWidth 高度 $bestPreviewHeight")
        }
        Log.e(VideoRecordViewModel.TAG, "最佳宽度 $bestPreviewWidth 最佳高度 $bestPreviewHeight")
        return Pair(bestPreviewWidth, bestPreviewHeight)
    }

    /**
     * Camera 开启预览
     */
    fun onSurfaceChanged() {
        mCamera?.apply {
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

    private fun getAutoFocus(): String {
        val parameters = mCamera!!.parameters
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

    // =================================== videoRecord 开始、停止

    fun startRecord(activity: Activity, maxTime:Int = 30, listener:MediaRecorder.OnInfoListener?):Boolean {
        recorderReleaseEnable = true
        if(mRecorder == null && mCamera != null) {
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
                setVideoEncodingBitRate((VideoRecordConfig.video_encoding_bit_rate) * 1024)
                setOrientationHint(270)
                setAudioSamplingRate(44100)//设置音频采样率为44100, 所有安卓系统都支持的才采样频率
                setAudioEncodingBitRate(96000)// 设置音频比特率为64, 音质比较好的频率:96000
                setAudioChannels(2) //设置录制的音频通道数
                //设置记录会话的最大持续时间（毫秒）
                setMaxDuration(maxTime * 1000)
            }
        }

        val pathDir = getVideoPath(activity)
        if (pathDir != null) {
            val dir = File(pathDir)
            if (!dir.exists()) {
                dir.mkdir()
            }
            dirPath = dir.absolutePath
            path = dirPath + "/" + "tmp_"+ getVideoArg() + MediaUtils.getYyyyMMdd_hHmmss() + ".mp4"
            Log.d(VideoRecordViewModel.TAG, "文件路径： $path")

            try {
                mRecorder!!.apply {
                    setOutputFile(path)
                    setOnInfoListener(listener)
                    prepare()
                    start()
                }
            } catch (e: java.lang.RuntimeException) {
                e.printStackTrace()
                try {
                    mRecorder?.release()
                    mRecorder = null
                    return false
                }catch (e:Exception){}
            }
            startTime = System.currentTimeMillis()  //记录开始拍摄时间

        }
        return true
    }

    /**
     * 视频路径
     */
    private fun getVideoPath(activity: Activity): String {
        return Constant.dirName(activity)
    }

    /**
     * 命名参数
     */
    private fun getVideoArg(): String {
        return "${VideoRecordConfig.video_width}-${VideoRecordConfig.video_height}-" +
                "${VideoRecordConfig.video_frame_rate}-${VideoRecordConfig.video_encoding_bit_rate}-"
    }

    /**
     * 方便再次 start
     */
    fun pauseRecord() {
        try {
            mRecorder?.apply {
                stop()
                reset()
                release()
            }
            mRecorder = null
            recorderReleaseEnable = false
            mCamera?.apply {
                lock()
                stopPreview()
//                release()
            }
            onSurfaceChanged()
//            cameraReleaseEnable = false
        } catch (e: java.lang.RuntimeException) {
            e.printStackTrace()
        }
    }

    /**
     * 暂停，
     * 1. 需1s 后调用
     */
    fun stopRecord(getImageForVideo: () -> Unit) {
        stopTime = System.currentTimeMillis()
        try {
            mRecorder?.apply {
                stop()
                reset()
                release()
            }
            mRecorder = null
            recorderReleaseEnable = false
            mCamera?.apply {
                lock()
                stopPreview()
                release()
            }
            mCamera = null
            cameraReleaseEnable = false

            val imgName = dirPath + "/" + "tmp_"+ getVideoArg() + MediaUtils.getYyyyMMdd_hHmmss() + ".jpg"
            MediaUtils.getImageForVideo(path, imgName) {
                //获取到第一帧图片后再显示操作按钮
                imgPath = it.absolutePath
                Log.d(VideoRecordViewModel.TAG, "获取到了第一帧,$imgPath ")
                getImageForVideo.invoke()
            }
        } catch (e: java.lang.RuntimeException) {
            //当catch到RE时，说明是录制时间过短
            Log.e(VideoRecordViewModel.TAG, "拍摄时间过短" + e.message)
            mRecorder?.apply {
                reset()
                release()
            }
            recorderReleaseEnable = false
        }
    }

    fun onDestroy() {
        if (recorderReleaseEnable) {
            mRecorder?.apply {
                stop()
                reset()
                release()
            }
            recorderReleaseEnable = false
        }
        if (cameraReleaseEnable && mCamera != null) {
            mCamera?.apply {
                lock()
                stopPreview()
                release()
            }
            mCamera = null
            cameraReleaseEnable = false
        }
    }
}