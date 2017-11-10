package com.bidostar.videoedit.operator;


import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * 视频抽帧
 * Created by smallc on 2017/4/12 15:41.
 */
public class VideoExtractFrameServiceImpl {
    private final String TAG = "VideoExtractFrame";
    private MediaMetadataRetriever mMetadataRetriever;

    public VideoExtractFrameServiceImpl(String path){
        mMetadataRetriever = new MediaMetadataRetriever();
        init(path);
    }

    private void init(String path){
        if (TextUtils.isEmpty(path)) {
            Log.e(TAG,"path must be not null !--"+path);
            return ;
        }
        File file = new File(path);
        if (!file.exists()) {
            Log.e(TAG,"path file no exists ! = "+ path);
            return ;
        }
        mMetadataRetriever.setDataSource(file.getPath());
    }

    public void release(){
        if(mMetadataRetriever != null) {
            mMetadataRetriever.release();
        }
    }
    /***
     * 视频抽帧
     * @param extractTime   抽取时间  毫秒
     */
    public Bitmap extractFrame(long extractTime ) {
        Bitmap bitmap = mMetadataRetriever.getFrameAtTime(extractTime,MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        return bitmap;
    }

    /**
     * 获取视频的典型的一帧图片
     *
     * @return Bitmap
     */
    public Bitmap extractFrame() {
        return mMetadataRetriever.getFrameAtTime();
    }

    /***
     * 获取视频的长度时间
     *
     * @return String 毫秒
     */
    public String getVideoLength() {
        return mMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
    }

}
