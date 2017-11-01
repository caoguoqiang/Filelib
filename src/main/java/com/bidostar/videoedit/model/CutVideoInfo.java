package com.bidostar.videoedit.model;

/**
 * 切割后的视频信息
 * Created by smallc on 2017/5/2 17:43.
 */
public class CutVideoInfo {

    public String path;
    public long startTime;
    public int sourceCategory;
    public double duration;//时长

    @Override
    public String toString() {
        return "CutVideoInfo{" +
                "path='" + path + '\'' +
                ", startTime='" + startTime + '\'' +
                ", sourceCategory=" + sourceCategory +
                ", duration=" + duration +
                '}';
    }
}
