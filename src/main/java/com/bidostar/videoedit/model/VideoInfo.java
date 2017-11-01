package com.bidostar.videoedit.model;

import java.util.List;

/**
 * Created by smallc on 2017/4/5 13:57.
 */

public class VideoInfo {

    public String fileName;//文件名称
    public String startTime;//视频开始时间
    public String endTime;//视频结束时间
    public String format;//格式
    public int categoryId;//摄像头类别  1：前摄像头 2 后摄像头
    public long whenLong;//录制时长
    public int width;//视频宽
    public int height;//视频高
    public int type;//视频锁定状态 1 循环视频  2  震动视频
    public String path;//文件路径

    public long cutStartTime; //截取开始时间
    public long cutEndTime;//截取结束时间

    public List<ExtractFrame> extractFrameLists;//抽帧时间列表

    public long compareTime;//排序时间

    public String sT;
    public String eT;
    public boolean isInsert = true;
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof VideoInfo && this.fileName.equals(((VideoInfo) obj).fileName)){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "fileName='" + fileName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", format='" + format + '\'' +
                ", categoryId=" + categoryId +
                ", whenLong=" + whenLong +
                ", width=" + width +
                ", height=" + height +
                ", type=" + type +
                ", path='" + path + '\'' +
                ", cutStartTime=" + cutStartTime +
                ", cutEndTime=" + cutEndTime +
                ", extractFrameLists=" + extractFrameLists +
                ", compareTime=" + compareTime +
                ", sT='" + sT + '\'' +
                ", eT='" + eT + '\'' +
                ", isInsert=" + isInsert +
                '}';
    }
}
