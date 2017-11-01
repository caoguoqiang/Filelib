package com.bidostar.videoedit.operator;

import com.bidostar.videoedit.BuildConfig;
import com.bidostar.videoedit.model.ImageItem;
import com.bidostar.videoedit.utils.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by small.cao on 2017/10/25.
 */

public class PictureOperator extends FileOperators {


    public List<ImageItem> getImages(int category){
        if(BuildConfig.Model.equals("BD05")){
            if(category == 1){
                return getImageList(Constant.VIDEO_BD05_NORMAL_PATH+Constant.VIDEO_BDO5_CATEGORY_PHOTO1,category,4);
            } else {
                return getImageList(Constant.VIDEO_BD05_NORMAL_PATH+Constant.VIDEO_BDO5_CATEGORY_PHOTO2,category,4);
            }
        } else if(BuildConfig.Model.equals("BD06")){
            return getImageList(Constant.VIDEO_BD06_CAPTURE_PATH,category,4);
        }
        return null;
    }

    public List<ImageItem> getImageList(String path, int category,int type){
        File file = new File(path);
        if(!file.exists()){
            return null;
        }
        List<ImageItem> list = new ArrayList<>();
        File[] files = file.listFiles();
        for(File videoFile : files){
            if (videoFile.exists()) {
                boolean condition = (getCategoryIdByName(videoFile.getName()) == 2 && category == 2) ||
                        (getCategoryIdByName(videoFile.getName()) == 1 && category == 1);
                if(condition){
                    ImageItem imageItem = getPhInfo(videoFile,type);
                    if(imageItem == null ){
                        continue;
                    }
                    list.add(imageItem);
                }
            }
        }
        VideoCompare videoCompare = new VideoCompare();
        videoCompare.setCompare(false);//升序
        Collections.sort(list,videoCompare);

        return list;
    }

    private ImageItem getPhInfo(File file,int type){
        ImageItem imageItem = new ImageItem();
        imageItem.name = file.getName();
        imageItem.path = file.getPath();
        imageItem.createTime = getStartTime(file,type);
        if(imageItem.createTime == null){
            return null;
        }
//        videoItem.length = getLengh(file);
//        videoItem.endTime = DateFormatUtils.format(new Date(DateFormatUtils.parse(videoItem.startTime,DateFormatUtils.PATTERN_FULL).getTime()+ videoItem.length),DateFormatUtils.PATTERN_FULL);
        imageItem.type = 4;
        imageItem.categoryId = getCategoryIdByName(file.getName());
        return imageItem;
    }
}
