package com.bidostar.videoedit.utils;

import com.bidostar.videoedit.BuildConfig;

/**
 * Created by small.cao on 2017/9/13.
 */

public class Constant {

    public static final String VIDEO_BD05_ROOT_PATH = "/mnt/extsd/landsem/cardvr/";
    public static final String VIDEO_BD05_NORMAL_PATH = VIDEO_BD05_ROOT_PATH + "normal/";
    public static final String VIDEO_BD05_LOCKED_PATH = VIDEO_BD05_ROOT_PATH + "locked/";
    public static final String VIDEO_BDO5_CATEGORY_VIDEO1 = "video1";//前摄像头
    public static final String VIDEO_BDO5_CATEGORY_VIDEO2 = "video2";//后摄像头
    public static final String VIDEO_BDO5_CATEGORY_PHOTO1 = "photo1";//后摄像头
    public static final String VIDEO_BDO5_CATEGORY_PHOTO2 = "photo2";//后摄像头

    public static final String VIDEO_BD06_NORMAL_PATH = "/mnt/sdcard2/DVR";
    public static final String VIDEO_BD06_LOCKED_PATH = "/mnt/sdcard2/DVR/lock";
    public static final String VIDEO_BD06_CAPTURE_PATH = "/mnt/sdcard2/DVR/capture";

    public static final String MEDIA_VIDEO_SUFFIX_MP4 = "mp4";//视频文件格式

    public static final int timeout = 20 * 1000;

    public static final String MD5_SEED = "bidostar";
//    private static final String HTTP_URL_BASE = "http://api.ttt.com";
    private static String HTTP_URL_BASE = "http://api.bidostar.com";
    static {
        if(BuildConfig.ES == 1){
            HTTP_URL_BASE = "http://alpha.api.bidostar.com/";
        } else if(BuildConfig.ES == 2){
            HTTP_URL_BASE = "http://beta.api.bidostar.com/";
        }
    }

    public static final String URL_GET_REGISTER_DEVICE = HTTP_URL_BASE + "/oss/assume_role.json";
    public static final String URL_NOTIFY_MEDIA_UPLOAD_RECORD = HTTP_URL_BASE + "/v1/device/add_device_media.json";//本地文件上传记录通知服务端 作业务处理
    public static final String URL_GET_CONFIG = HTTP_URL_BASE + "/oss/config.json";

}
