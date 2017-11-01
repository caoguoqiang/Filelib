package com.bidostar.videoedit.operator;

import com.bidostar.videoedit.BuildConfig;
import com.bidostar.videoedit.model.VideoInfo;
import com.bidostar.videoedit.utils.Constant;
import com.bidostar.videoedit.utils.DateFormatUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by small.cao on 2017/10/12.
 */
public class LockVideo {

    public static void lockvideoList(Date eventEndDate){
        Date eventStartDate = new Date(eventEndDate.getTime() - 30 * 60 * 1000);
        List<VideoInfo> list = new FileOperators().getVideoItems(1,1);//前摄
        List<VideoInfo> list1 = new FileOperators().getVideoItems(1,2);//后摄
        if(list != null && list.size() > 0){
            dealVideo(eventStartDate,eventEndDate,list);
        }
        if(list1 != null && list1.size() > 0){
            dealVideo(eventStartDate,eventEndDate,list1);
        }
    }

    private static void dealVideo(Date eventStartDate,Date eventEndDate,List<VideoInfo> list){
        for(VideoInfo videoInfo : list){
            Date vstartDate = DateFormatUtils.parse(videoInfo.startTime,DateFormatUtils.PATTERN_MILL);
            Date vendDate = new Date(vstartDate.getTime() + 150 * 1000);
            videoInfo.endTime = DateFormatUtils.format(vendDate,DateFormatUtils.PATTERN_MILL);
            if(vstartDate.getTime() < eventEndDate.getTime() && vendDate.getTime() > eventStartDate.getTime()){
                if(BuildConfig.Model.equals("BD05")){
                    if(videoInfo.categoryId == 1){//前摄
                        String name = "VD1-" + DateFormatUtils.format(videoInfo.startTime,DateFormatUtils.PATTERN_MILL,DateFormatUtils.PATTERN_FULL_J1) + ".mp4";
                        FileUtils.moveFile(videoInfo.path, Constant.VIDEO_BD05_LOCKED_PATH + Constant.VIDEO_BDO5_CATEGORY_VIDEO1 + "/"+ name);
                    } else if(videoInfo.categoryId == 2){//后摄
                        String name = "VD2-" + DateFormatUtils.format(videoInfo.startTime,DateFormatUtils.PATTERN_MILL,DateFormatUtils.PATTERN_FULL_J1) + ".mp4";
                        FileUtils.moveFile(videoInfo.path, Constant.VIDEO_BD05_LOCKED_PATH + Constant.VIDEO_BDO5_CATEGORY_VIDEO2 + "/"+ name);
                    }
                } else if(BuildConfig.Model.equals("BD06")){
                    if(videoInfo.categoryId == 1){//前摄
                        String name = "FPZ" + DateFormatUtils.format(videoInfo.startTime,DateFormatUtils.PATTERN_MILL,DateFormatUtils.PATTERN_FULL_J) + ".ts";
                        FileUtils.moveFile(videoInfo.path, Constant.VIDEO_BD06_LOCKED_PATH +"/"+ name);
                    } else if(videoInfo.categoryId == 2){//后摄
                        String name = "BPZ" + DateFormatUtils.format(videoInfo.startTime,DateFormatUtils.PATTERN_MILL,DateFormatUtils.PATTERN_FULL_J) + ".ts";
                        FileUtils.moveFile(videoInfo.path, Constant.VIDEO_BD06_LOCKED_PATH +"/"+ name);
                    }
                }

            }
        }
    }


}
