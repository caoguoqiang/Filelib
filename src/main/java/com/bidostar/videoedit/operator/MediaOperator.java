package com.bidostar.videoedit.operator;

import android.content.Context;

import com.bidostar.videoedit.model.CutVideoFragment;

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

}
