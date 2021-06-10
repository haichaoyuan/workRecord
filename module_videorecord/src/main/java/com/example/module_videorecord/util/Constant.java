package com.example.module_videorecord.util;

import android.content.Context;


public class Constant {

    public static String cacheText = "livenessDemo_text";
    public static String cacheImage = "livenessDemo_image";
    public static String cacheVideo = "livenessDemo_video";
    public static String cacheCampareImage = "livenessDemo_campareimage";

    /**
     * 视频文件存储路径，andorid/data/files/recordVideo
     */
    public static String dirName(Context context){
        return context.getExternalFilesDir("recordVideo").getAbsolutePath();
    }
}