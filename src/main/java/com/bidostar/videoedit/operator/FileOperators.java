package com.bidostar.videoedit.operator;

import android.text.TextUtils;
import android.util.Log;

import com.bidostar.videoedit.BuildConfig;
import com.bidostar.videoedit.model.VideoInfo;
import com.bidostar.videoedit.utils.Constant;
import com.bidostar.videoedit.utils.DateFormatUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.bidostar.videoedit.utils.DateFormatUtils.format;

/**
 * Created by small.cao on 2017/9/7.
 */

public class FileOperators {
    private final String TAG = "AReport.FileOperators";
    /**
     * 获取视频列表
     * @param type   1 or 2 or 3  normal or locked 锁定视频  or 事故锁定视频
     * @param category  1  前摄像头 2  后摄像头
     * @return
     */
    public List<VideoInfo>  getVideoItems(int type,int category){
        if(type == 1){//TODO 循环视频
            if(BuildConfig.Model.equals("BD05")){
                if(category == 1){
                    return getVideoList(Constant.VIDEO_BD05_NORMAL_PATH+Constant.VIDEO_BDO5_CATEGORY_VIDEO1,type,category);
                } else {
                    return getVideoList(Constant.VIDEO_BD05_NORMAL_PATH+Constant.VIDEO_BDO5_CATEGORY_VIDEO2,type,category);
                }
            } else if(BuildConfig.Model.equals("BD06")){
                return getVideoList(Constant.VIDEO_BD06_NORMAL_PATH,type,category);
            }
        } else if(type == 2 ){//TODO 震动视频
            if(BuildConfig.Model.equals("BD05")){
                if(category == 1){
                    return getVideoList(Constant.VIDEO_BD05_LOCKED_PATH+Constant.VIDEO_BDO5_CATEGORY_VIDEO1,type,category);
                } else {
                    return getVideoList(Constant.VIDEO_BD05_LOCKED_PATH+Constant.VIDEO_BDO5_CATEGORY_VIDEO2,type,category);
                }
            } else if(BuildConfig.Model.equals("BD06")){
                return getVideoList(Constant.VIDEO_BD06_LOCKED_PATH,type,category);
            }
        }
        return null;
    }

    public List<VideoInfo> getVideoList(String path,int type,int category){
        File file = new File(path);
        if(!file.exists()){
            return null;
        }
        List<VideoInfo> list = new ArrayList<>();
        File[] files = file.listFiles();
        for(File videoFile : files){
            if (videoFile.exists()) {
                if((videoFile.getName().substring(0,1).equals("B") && category == 2) ||
                        (videoFile.getName().substring(0,1).equals("F") && category == 1) ||
                        (videoFile.getName().substring(0,2).equals("VD"))){
                    VideoInfo vi = getVideoInfo(videoFile,type);
                    if(vi == null ){
                        continue;
                    }
                    list.add(vi);
                }
            }
        }
        VideoCompare videoCompare = new VideoCompare();
        videoCompare.setCompare(false);//升序
        Collections.sort(list,videoCompare);

        return list;
    }

    public List<VideoInfo> getVideoList(int category, Date startDate, Date endDate){
        int whenlong = 150 * 1000;
        String startTime = format(new Date(startDate.getTime() - whenlong),DateFormatUtils.PATTERN_MILL);
        String endTime = format(new Date(endDate.getTime()),DateFormatUtils.PATTERN_MILL);
        Map<String,Integer> pathMap = getPathByCategory(category);
        List<VideoInfo> list = new ArrayList<>();
        Set<String> paths = pathMap.keySet();
        for(String path : paths){
            int type = pathMap.get(path);
            File file = new File(path);
            if(!file.exists()){
                return null;
            }
            File[] files = file.listFiles();
            for(File videoFile : files){
                if (videoFile.exists()) {
                    if((videoFile.getName().substring(0,1).equals("B") && category == 2) ||
                            (videoFile.getName().substring(0,1).equals("F") && category == 1) ||
                            (videoFile.getName().substring(0,2).equals("VD"))){
                        VideoInfo vi = getVideoInfo(videoFile,type);
                        if(vi == null ){
                            continue;
                        }
                        if(vi.startTime.compareTo(endTime) <0 && vi.startTime.compareTo(startTime) > 0 ){
                            VideoInfoServiceImpl videoInfoService = new VideoInfoServiceImpl(vi.path);
                            String len = videoInfoService.getVideoLength();
                            Date vEndDate = new Date(DateFormatUtils.parse(vi.startTime,DateFormatUtils.PATTERN_MILL).getTime()+Long.valueOf(len));
                            vi.endTime = DateFormatUtils.format(vEndDate,DateFormatUtils.PATTERN_MILL);
                            if(startDate.getTime() < vEndDate.getTime()){
                                Log.e(TAG,"video end time is " + vi.endTime);
                                list.add(vi);
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    public List<File> getFileList(int category){
        Map<String,Integer> pathMap = getPathByCategory(category);
        List<File> fileList = new ArrayList<>();
        Set<String> keys = pathMap.keySet();
        for(String path : keys){
            File file = new File(path);
            if(!file.exists()){
                return null;
            }
            File[] files = file.listFiles();
            for(File videoFile : files){
                if (videoFile.exists()) {
                    if((videoFile.getName().substring(0,1).equals("B") && category == 2) ||
                            (videoFile.getName().substring(0,1).equals("F") && category == 1) ||
                            (videoFile.getName().substring(0,2).equals("VD"))){
                        fileList.add(videoFile);
                    }
                }
            }
        }
        return fileList;
    }

    public Map<String,Integer> getPathByCategory(int category){
        Map<String,Integer> pathMap = new HashMap();
        if(BuildConfig.Model.equals("BD05")){
            if(category == 0 ||category == 1){ //前摄像头
                pathMap.put(Constant.VIDEO_BD05_NORMAL_PATH + Constant.VIDEO_BDO5_CATEGORY_VIDEO1,1);
                pathMap.put(Constant.VIDEO_BD05_LOCKED_PATH + Constant.VIDEO_BDO5_CATEGORY_VIDEO1,2);
            }
            if(category == 0 ||category == 2){//后摄像头
                pathMap.put(Constant.VIDEO_BD05_NORMAL_PATH + Constant.VIDEO_BDO5_CATEGORY_VIDEO2,1);
                pathMap.put(Constant.VIDEO_BD05_LOCKED_PATH + Constant.VIDEO_BDO5_CATEGORY_VIDEO2,2);
            }
        } else if(BuildConfig.Model.equals("BD06")){
            pathMap.put(Constant.VIDEO_BD06_LOCKED_PATH,0);
            pathMap.put(Constant.VIDEO_BD06_NORMAL_PATH,0);
        }
        return pathMap;
    }

    private VideoInfo getVideoInfo(File file, int type){
        VideoInfo videoItem = new VideoInfo();
        videoItem.fileName = file.getName();
        videoItem.path = file.getPath();
        videoItem.startTime = getStartTime(file,type);
        if(videoItem.startTime == null){
            return null;
        }
//        videoItem.length = getLengh(file);
//        videoItem.endTime = DateFormatUtils.format(new Date(DateFormatUtils.parse(videoItem.startTime,DateFormatUtils.PATTERN_FULL).getTime()+ videoItem.length),DateFormatUtils.PATTERN_FULL);
        videoItem.type = type;
        videoItem.categoryId = getCategoryIdByName(file.getName());
        return videoItem;
    }


    /***
     *  获取多媒体文件创建时间
     * @param file
     * @param type  1 normal 2 震动锁影 3  事故视频 4  图片
     * @return
     */
    public String getStartTime(File file,int type){
        String name = file.getName();
        String[] names = name.split("\\.");
        if(names == null || names.length < 2){
            return null;
        }
        if(BuildConfig.Model.equals("BD05")){
            names = names[0].split("-");
            if(names == null || names.length < 3){
                return null;
            }
            return format(names[1]+names[2],DateFormatUtils.PATTERN_FULL_J,DateFormatUtils.PATTERN_MILL);
//            VD2-20170907-201442.mp4
        } else if(BuildConfig.Model.equals("BD06")){
            return getCreateTimeFor06(names,type);
        }
        return "";
    }

    /**
     * 多媒体文件创建时间  BD06
     * @param names 文件名称
     * @param type  文件类型  1  循环录影 2  锁定视频  3 图片
     * @return   开始时间
     */
    private String getCreateTimeFor06(String[] names ,int type){
        String timestr = "";
        if(type == 1){//循环露营
            String endStr = names[0].substring(names[0].length()-1,names[0].length());
            if("S".equals(endStr)){
                timestr = names[0].substring(1,names[0].length()-1);
            } else {
                timestr = names[0].substring(1);
            }
        } else if(type == 2) {//锁定视频
            String endStr = names[0].substring(names[0].length()-1,names[0].length());
            if("S".equals(endStr)){
                timestr = names[0].substring(3,names[0].length()-1);
            } else {
                timestr = names[0].substring(3);
            }
        } else if(type == 4){//图片
            String[] phtimeStrs = names[0].substring(1).split("_");
            StringBuffer sb = new StringBuffer();
            for(String item : phtimeStrs){
                sb.append(item);
            }
            return DateFormatUtils.format(sb.toString(),DateFormatUtils.PATTERN_FULL_J,DateFormatUtils.PATTERN_MILL);
        }
        if(!TextUtils.isEmpty(timestr)){
            return DateFormatUtils.format(timestr,DateFormatUtils.PATTERN_FULL_J,DateFormatUtils.PATTERN_MILL);
        }
        return null;
    }

    /*public int getLengh(File file){
        VideoInfoServiceImpl videoInfoService = new VideoInfoServiceImpl(file.getPath());
        String len = videoInfoService.getVideoLength();
        return TextUtils.isEmpty(len) ? 0 : Integer.valueOf(len);
    }*/

//    VD2-20170907-201442.mp4
    //@param category 通道  1  or  2    前摄 or 后摄
    public int getCategoryIdByName(String name){
        String prefix = name.substring(0,1);
        if("B".equals(prefix)){
            return 2;
        } else if("F".equals(prefix)){
            return 1;
        }
        prefix = name.substring(0,3);
        if("VD1".equals(prefix)){
            return 1;
        } else if("VD2".equals(prefix)){
            return 2;
        } else if("PH1".equals(prefix)){
            return 1;
        } else if("PH2".equals(prefix)){
            return 2;
        }

        return -1;
    }

    public int getTypeFor06(File file){
        String prefix = file.getName().substring(0,3);
        if("BPZ".equals(prefix) || "FPZ".equals(prefix)){
            return 2;
        }
        prefix = file.getName().substring(0,1);
        if("B".equals(prefix) || "F".equals(prefix)){
            return 1;
        }
        return -1;
    }




}
