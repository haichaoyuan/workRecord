package com.yxztb.liblivedetection.ui.videorecord


class VideoRecordConfig {

    companion object {
        // 默认，高清
//        var video_width = 640
//        var video_height = 480
//        var video_frame_rate = 30
//        var video_encoding_bit_rate = 3 * 1024

        // 标清
        var video_width = 320
        var video_height = 240
        var video_frame_rate = 30
        var video_encoding_bit_rate = 1024 //500


        fun string2Int(string: String, defaultInt: Int): Int {
            try {
                return string.toInt()
            } catch (e: Exception) {
                return string.toInt()
            }
        }
    }
}
