package com.bidostar.videoedit.model;

/**
 * 视频抽帧
 * Created by smallc on 2017/4/18 10:52.
 */
public class ExtractFrame {
    public LocationInfo locationInfo;
    public long time;//抽帧时间

    @Override
    public String toString() {
        return "ExtractFrame{" +
                "locationInfo=" + locationInfo +
                ", time=" + time +
                '}';
    }
}
