package com.bidostar.videoedit.model;

/**
 * 上传信息
 * Created by smallc on 2017/4/15 10:57.
 */
public class UploadInfo {

    public String fileName ;//文件名称
    public String filePath;//文件路径
    public String eventTime;//事件时间
    public int sourceCategory;//通道类别  1.前摄像头 2.后摄像头
    public String fileCreateTime;//文件创建时间
    public int type;//文件类型
    public double duration = 0;//时长
    public int encType;//编码格式
    public int status;//上传状态
    public int eventType;//事件类型 15.图像抓拍 16 视频录制 18 视频抽帧
    public String createTime;//记录产生时间

    @Override
    public String toString() {
        return "UploadInfo{" +
                "fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", eventTime='" + eventTime + '\'' +
                ", sourceCategory=" + sourceCategory +
                ", fileCreateTime='" + fileCreateTime + '\'' +
                ", type=" + type +
                ", duration=" + duration +
                ", encType='" + encType + '\'' +
                ", status=" + status +
                ", eventType=" + eventType +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
