package com.bidostar.videoedit.operator;

import com.bidostar.videoedit.model.VideoInfo;

import java.util.Comparator;

/**
 * Created by smallc on 2017/4/19 17:45.
 */

public class VideoCompare implements Comparator {

    public boolean isDes= false;
    // true or false  升序 or 降序
    public void setCompare(boolean isDesc){
        this.isDes = isDesc;
    }

    @Override
    public int compare(Object o1, Object o2) {
        if(o1 instanceof VideoInfo && o2 instanceof VideoInfo){
            if(((VideoInfo) o1).startTime.compareTo(((VideoInfo) o2).startTime) < 0 ){
                if(isDes){
                    return -1;
                } else {
                    return 1;
                }
            } else if(((VideoInfo) o1).startTime.compareTo(((VideoInfo) o2).startTime) > 0){
                if(isDes){
                    return 1;
                } else {
                    return -1;
                }
            }

        }
        return 0;
    }
}
