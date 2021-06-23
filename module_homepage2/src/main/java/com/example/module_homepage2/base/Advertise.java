package com.example.module_homepage2.base;

/**
 * Des:广告图
 * Author: Luke
 * Data: 16/12/12
 */
public class Advertise {
    public static String PLACEVALUE_HOME = "2";//首页banner位置
    public static String PLACEVALUE_SPLASH = "6";//开屏banner位置

    public static String STATUS_NORMAL = "1";//有效，可以显示
    public static String TARGET_TYPE_H5 = "0";
    public static String TARGET_TYPE_NATIVE = "1";

    public static String THIRD_SD = "sd";

    public String code;//数据是否有效 -1:表示服务器异常 0:表示没有可用数据 1:有效，可以显示
    public String version;//开屏页广告版本
    public String advertisePath;//开屏页广告图片下载地址
    public String timer = "3";//开屏页广告显示时长，单位秒 默认3秒
    public String advertiseType;//开屏页广告类型，0:H5; 1:native
    public String advertiseTargetUrl;//H5广告跳转地址
    public String title;//标题
    public String activityType;//活动类别
    public String activityDescription;//活动描述
    public String ids = "";
    public String jumpThird;

    @Override
    public String toString() {
        return "Advertise{" +
                "code='" + code + '\'' +
                ", version='" + version + '\'' +
                ", advertisePath='" + advertisePath + '\'' +
                ", timer='" + timer + '\'' +
                ", advertiseType='" + advertiseType + '\'' +
                ", advertiseTargetUrl='" + advertiseTargetUrl + '\'' +
                '}';
    }
}
