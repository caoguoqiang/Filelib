package com.bidostar.videoedit.operator;

import android.content.Context;

import com.bidostar.videoedit.model.CutVideoFragment;
import com.bidostar.videoedit.model.CutVideoInfo;
import com.bidostar.videoedit.model.VideoInfo;
import com.bidostar.videoedit.utils.DateFormatUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by small.cao on 2017/11/1.
 */

public class MediaOperator {

    // 创建线程池
    private static final ThreadPoolExecutor mThreadPoolExecutor = (ThreadPoolExecutor) Executors
            .newCachedThreadPool();

    private static MediaOperator mediaOperator;

    public static MediaOperator getInstance(Context context){
        if(mediaOperator == null){
            mediaOperator = new MediaOperator(context);
        }
        return mediaOperator;
    }
    private Context context;

    public MediaOperator(Context context) {
        this.context = context;
    }

    public void CutVideo() {
        EditVideoOperators editVideoOperators = EditVideoOperators.getInstance(context);
        editVideoOperators.addTask(new ArrayList<CutVideoFragment>(), new Date());
        if (!editVideoOperators.isRun()) {
            mThreadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    EditVideoOperators.getInstance(context).executeCut();
                }
            });
        }
    }

    public CutVideoInfo cutVideo(String startTime,String endTime,String path,String ouputPath){
        File file = new File(path);
        FileOperators fileOperators = new FileOperators();
        int type = fileOperators.getTypeFor06(file);
        VideoInfo videoInfo = fileOperators.getVideoInfo(file,type);
        videoInfo.cutStartTime = DateFormatUtils.parse(startTime, DateFormatUtils.PATTERN_MILL).getTime();;
        videoInfo.cutEndTime = DateFormatUtils.parse(endTime, DateFormatUtils.PATTERN_MILL).getTime();;
        videoInfo.path = path;
        videoInfo.outputPath = ouputPath;
        EditVideoOperators editVideoOperators = EditVideoOperators.getInstance(context);
        CutVideoInfo cutVideoInfo = editVideoOperators.cutVideo(videoInfo);
        return cutVideoInfo;
    }

}
