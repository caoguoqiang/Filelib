package com.bidostar.videoedit.model;

import java.util.List;

/**
 * Created by smallc on 2017/5/11 19:45.
 */

public class CutVideoFragment {
    public LocationInfo locationInfo;
    public String startTime;
    public String endTime;
    public int sourceCategory;
    public List<VideoInfo> list;

    @Override
    public String toString() {
        return "CutVideoFragment{" +
                "locationInfo=" + locationInfo +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", sourceCategory=" + sourceCategory +
                ", list=" + list +
                '}';
    }
}
