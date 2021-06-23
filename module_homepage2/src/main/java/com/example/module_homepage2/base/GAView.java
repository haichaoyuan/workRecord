package com.example.module_homepage2.base;

/**
 * Created by gerong on 6/21/17.
 * <p>
 * 打点相关的view需要实现此方法
 */

public interface GAView {

    /**
     * 设置view的打点名字
     *
     * @param elementId
     */
    void setGAString(String elementId);

    /**
     * 获取view的打点名字
     *
     * @return
     */
    String getGAString();
}
