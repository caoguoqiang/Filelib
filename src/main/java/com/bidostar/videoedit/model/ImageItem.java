package com.bidostar.videoedit.model;

/**
 * Created by small.cao on 2017/10/30.
 */

public class ImageItem {
    public String createTime;
    public String path;
    public int categoryId;//摄像头类别  1：前摄像头 2 后摄像头
    public String name;
    public int type;//TODO 1 normal 2 震动锁影 3  事故视频 4  图片
    @Override
    public String toString() {
        return "ImageItem{" +
                "createTime='" + createTime + '\'' +
                ", path='" + path + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }
}
