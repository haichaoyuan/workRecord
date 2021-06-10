package com.yxztb.liblivedetection.ui.videorecord

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.module_videorecord.R
import kotlinx.android.synthetic.main.video_view_video_record.view.*
import java.io.File

class VideoRecordFragment : Fragment() {

    private lateinit var rootView: View

    private var timer = 0 //计时器
    private val maxSec = 10 //视频总时长

    private lateinit var mSurfaceHolder: SurfaceHolder
    private val viewModel: VideoRecordViewModel by lazy {
        ViewModelProvider(this).get(VideoRecordViewModel::class.java)
    }

    //用于记录视频录制时长
    var handler = Handler()
    var runnable = object : Runnable {
        override fun run() {
            timer++
//            Log.d("计数器","$timer")
            if (timer < 100) {
                // 之所以这里是100 是为了方便使用进度条
                rootView.mProgress.progress = timer
                //之所以每一百毫秒增加一次计时器是因为：总时长的毫秒数 / 100 即每次间隔延时的毫秒数 为 100
                handler.postDelayed(this, maxSec * 10L)
            } else {
                //停止录制 保存录制的流、显示供操作的ui
                Log.d(VideoRecordViewModel.TAG, "到最大拍摄时间")
                stopRecord()
                System.currentTimeMillis()
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.video_view_video_record, null)
        init()
        return rootView
    }

    private fun init() {
        viewModel.init()

        val holder: SurfaceHolder = rootView.mSurfaceview.holder
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                mSurfaceHolder = holder!!
                viewModel.onSurfaceChanged()
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                handler.removeCallbacks(runnable)
            }

            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    mSurfaceHolder = holder!!
                    //使用后置摄像头
                    viewModel.onSurfaceCreate(requireActivity(), holder)

                } catch (e: RuntimeException) {
                    //Camera.open() 在摄像头服务无法连接时可能会抛出 RuntimeException
                    activity?.finish()
                }

            }
        }
        )
        rootView.mBtnRecord.setOnTouchListener { _, event ->
            Log.d(VideoRecordViewModel.TAG, "点击屏幕 ${event.action}")
            if (event.action == MotionEvent.ACTION_DOWN) {
                startRecord()
            }
            if (event.action == MotionEvent.ACTION_UP) {
                stopRecord()
            }
            true
        }
        rootView.mBtnPlay.setOnClickListener {
            playRecord()
        }
        rootView.mBtnCancle.setOnClickListener {
            //取消，删视频、图片
            if (viewModel.mType == VideoRecordViewModel.TYPE_VIDEO) {
                stopPlay()
                var videoFile = File(viewModel.path)
                if (videoFile.exists() && videoFile.isFile) {
                    videoFile.delete()
                }
            } else {
                //拍照模式
                val imgFile = File(viewModel.imgPath)
                if (imgFile.exists() && imgFile.isFile) {
                    imgFile.delete()
                }
            }
            activity?.setResult(Activity.RESULT_CANCELED)
            activity?.finish()
        }
        rootView.mBtnSubmit.setOnClickListener {
            stopPlay()
            var intent = Intent().apply {
                putExtra("path", viewModel.path)
                putExtra("imagePath", viewModel.imgPath)
                putExtra("type", viewModel.mType)
            }
            if (viewModel.mType == VideoRecordViewModel.TYPE_IMAGE) {
                //删除一开始创建的视频文件
                var videoFile = File(viewModel.path)
                if (videoFile.exists() && videoFile.isFile) {
                    videoFile.delete()
                }
            }
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }
    }


    override fun onStop() {
        super.onStop()
        if (viewModel.mPlayFlag) {
            stopPlay()
        }
        if (viewModel.mStartedFlag) {
            Log.d(VideoRecordViewModel.TAG, "页面stop")
            stopRecord()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()

    }

    //开始录制
    private fun startRecord() {
        timer = 0
        if (!viewModel.mStartedFlag) {
            viewModel.mStartedFlag = true
            rootView.mLlRecordOp.visibility = View.INVISIBLE
            rootView.mBtnPlay.visibility = View.INVISIBLE
            rootView.mLlRecordBtn.visibility = View.VISIBLE
            rootView.mProgress.visibility = View.VISIBLE //进度条可见
            //开始计时
            handler.postDelayed(runnable, maxSec * 10L)

            viewModel.startRecord(requireActivity())
        }
    }

    //结束录制
    private fun stopRecord() {

        if (viewModel.mStartedFlag) {
            viewModel.mStartedFlag = false
            rootView.mBtnRecord.isEnabled = false
            rootView.mBtnRecord.isClickable = false

            rootView.mLlRecordBtn.visibility = View.INVISIBLE
            rootView.mProgress.visibility = View.INVISIBLE

            handler.removeCallbacks(runnable)

            rootView.mBtnPlay.visibility = View.VISIBLE

            viewModel.stopRecord({
                rootView.mLlRecordOp.visibility = View.VISIBLE
            }) {
                activity?.runOnUiThread {
                    rootView.mBtnPlay.visibility = View.INVISIBLE
                    rootView.mLlRecordOp.visibility = View.VISIBLE
                }
            }
        }
    }

    //播放录像
    private fun playRecord() {

        rootView.mBtnPlay.visibility = View.INVISIBLE
        viewModel.playRecord(requireActivity(), mSurfaceHolder) {
            //播放解释后再次显示播放按钮
            rootView.mBtnPlay.visibility = View.VISIBLE
        }

    }

    //停止播放录像
    private fun stopPlay() {
        viewModel.stopPlay()
    }


}
