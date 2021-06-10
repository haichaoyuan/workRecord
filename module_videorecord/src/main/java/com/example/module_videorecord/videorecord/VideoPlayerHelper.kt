package com.yxztb.liblivedetection.ui.videorecord

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.SurfaceHolder

class VideoPlayerHelper(val path: String) {
    private var mMediaPlayer: MediaPlayer? = null

    fun init(activity: Context, mSurfaceHolder: SurfaceHolder, onComplete: () -> Unit) {
        if (mMediaPlayer == null) {
            // 解决 FileNotFoundException: No content provider: MediaPlayer.create ->  setDataSource
//        var uri = Uri.parse(path)
//        mMediaPlayer = MediaPlayer.create(activity, uri)
            mMediaPlayer = MediaPlayer()
            mMediaPlayer?.apply {
                setDataSource(path)
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setDisplay(mSurfaceHolder)
                setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
                    override fun onCompletion(p0: MediaPlayer?) {
                        p0?.seekTo(0)
                        onComplete.invoke()
                    }
                })
            }
            try {
                mMediaPlayer?.prepare()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            mMediaPlayer?.seekTo(0)
        }
    }

    fun playRecord() {
        //修复录制时home键切出再次切回时无法播放的问题
        mMediaPlayer?.start()
    }

    fun pausePlay() {
        if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
            mMediaPlayer?.pause()
        }
    }

    fun stopPlay() {
        if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
            mMediaPlayer?.stop()
            mMediaPlayer?.release()
            mMediaPlayer = null
        }
    }

    fun onDestroy() {
        if (mMediaPlayer != null) {
            mMediaPlayer?.release()
            mMediaPlayer = null
        }
    }

    fun getDuration(): Int {
        return mMediaPlayer?.duration ?: 0
    }

    fun getCurrentPosition(): Int {
        return mMediaPlayer?.currentPosition ?: 0
    }
}