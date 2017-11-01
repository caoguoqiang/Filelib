package com.bidostar.videoedit.operator;

import com.bidostar.videoedit.model.CutVideoFragment;
import com.bidostar.videoedit.model.VideoInfo;
import com.bidostar.videoedit.utils.DateFormatUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by small.cao on 2017/9/13.
 */
public class VideoOperator {

    private final String TAG = "AReport.VideoOperator";

    public List<CutVideoFragment> getVideoListByTime(Date startDate,Date endDate,int category,int extractSpace){
        List<CutVideoFragment> cutVideoFragments = new ArrayList<>();
        for(long date = startDate.getTime();date< endDate.getTime(); date +=extractSpace){
            CutVideoFragment cutVideoFragment1 = getCutVideoFragment(new Date(date),new Date(date+extractSpace),category,extractSpace);
            if(cutVideoFragment1!= null){
                cutVideoFragments.add(cutVideoFragment1);
            }
        }
        return cutVideoFragments;
    }

    private CutVideoFragment getCutVideoFragment(Date startDate,Date endDate,int category,int extractSpace){
        List<VideoInfo> videoInfos = new FileOperators().getVideoList(category,startDate,endDate);
        insertVideoInfo(videoInfos);
        gen(videoInfos,startDate,extractSpace);
        if(videoInfos != null && videoInfos.size() > 0){
            CutVideoFragment cutVideoFragment = new CutVideoFragment();
            cutVideoFragment.list = videoInfos;
            cutVideoFragment.sourceCategory = category;
            return cutVideoFragment;
        }
        return null;
    }

    /**
     * 构建最新的需要切割的视频信息
     */
    private void insertVideoInfo(List<VideoInfo> list){
        for(int i = 0;i<list.size();i++){
            VideoInfo videoInfo = list.get(i);
            long st = DateFormatUtils.parse(videoInfo.startTime,DateFormatUtils.PATTERN_MILL).getTime();
            long et = DateFormatUtils.parse(videoInfo.endTime,DateFormatUtils.PATTERN_MILL).getTime();
            videoInfo.sT = videoInfo.startTime;
            videoInfo.eT = videoInfo.endTime;
            for(int j = i+1;j<list.size();j++){
                VideoInfo videoInfo1 = list.get(j);
                if(!videoInfo1.isInsert){
                    break;
                }
                long st1 = DateFormatUtils.parse(videoInfo1.startTime,DateFormatUtils.PATTERN_MILL).getTime();
                long et1 = DateFormatUtils.parse(videoInfo1.endTime,DateFormatUtils.PATTERN_MILL).getTime();
                if(st == st1 && et == et1){
                    videoInfo.isInsert = false;
                    break;
                }
                // v2包含 v1
                if(st > st1 && et < et1 ){
                    videoInfo.isInsert = false;
                    break;
                }
                //左边或右边不相干
                boolean condition1 = (st1 > st && (st1 > et || et -st1 <1000)) || ((st > et1 || et1 -st < 1000) && et > et1);
                if(condition1){
                    videoInfo.isInsert = true;
                    videoInfo.sT = videoInfo.startTime;
                    videoInfo.eT = videoInfo.endTime;
                }
                //v1 包含v2
                if((st < st1 && et > et1)){
                    videoInfo.isInsert = true;
                    videoInfo1.isInsert = false;
                    videoInfo.sT = videoInfo.startTime;
                    videoInfo.eT = videoInfo.endTime;
                }
                //左边有交集
                if(st < st1  && et - st1 > 1000 && et < et1){
                    videoInfo.isInsert = true;
                    videoInfo.sT = videoInfo.startTime;
                    videoInfo.eT = videoInfo1.startTime;
                }
                //右边有交集
                if(et1 - st > 1000 && et > et1 && st > st1){
                    videoInfo.isInsert = true;
                    videoInfo.sT = videoInfo1.endTime;
                    videoInfo.eT = videoInfo.endTime;
                }
                break;
            }
        }
    }

    private void gen(List<VideoInfo> videoInfos,Date startDate,int extractSpace){
        for(VideoInfo videoInfo : videoInfos){
            long startTimeM = DateFormatUtils.parse(videoInfo.sT,DateFormatUtils.PATTERN_MILL).getTime();
            long endTimeM = DateFormatUtils.parse(videoInfo.eT,DateFormatUtils.PATTERN_MILL).getTime();
            long startTimeL = startDate.getTime();
            long endT = startDate.getTime()+extractSpace;
            if(startTimeL >= startTimeM && endT <= endTimeM){
                videoInfo.cutStartTime = startTimeL;
                videoInfo.cutEndTime = endT;
            } else if (startTimeL >= startTimeM && endT >=endTimeM && startTimeL < endTimeM) {
                videoInfo.cutStartTime = startTimeL;
                videoInfo.cutEndTime = endTimeM;
            } else if(startTimeL < startTimeM && endT <= endTimeM && endT > startTimeM){
                videoInfo.cutStartTime = startTimeM;
                videoInfo.cutEndTime = endT;
            } else if(startTimeL < startTimeM && endT > endTimeM ){
                videoInfo.cutStartTime = startTimeM;
                videoInfo.cutEndTime = endTimeM;
            }
        }
    }
}
