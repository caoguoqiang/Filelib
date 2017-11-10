package com.bidostar.videoedit.operator;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.bidostar.videoedit.model.CutVideoFragment;
import com.bidostar.videoedit.model.CutVideoInfo;
import com.bidostar.videoedit.model.UploadInfo;
import com.bidostar.videoedit.model.VideoInfo;
import com.bidostar.videoedit.utils.Constant;
import com.bidostar.videoedit.utils.DateFormatUtils;

import org.ffmpeg.android.Clip;
import org.ffmpeg.android.FFmpegController;
import org.ffmpeg.android.ShellUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by small.cao on 2017/9/13.
 */
public class EditVideoOperators {
    private final String TAG = "AReport.EditVideo";
    private Context context;
    private boolean isRun = false;

    private static EditVideoOperators editVideoOperators;

    public static EditVideoOperators getInstance(Context context){
        if(editVideoOperators == null){
            editVideoOperators = new EditVideoOperators(context);
        }
        return editVideoOperators;
    }


    private static Map<Date,List<CutVideoFragment>> maps = new HashMap<>();
    private EditVideoOperators(Context context){
        this.context = context;
    }

    public void addTask(List<CutVideoFragment> list, Date eventTime){
        maps.put(eventTime,list);
    }


    public synchronized void  executeCut(){
        Log.e(TAG,"edit video ing... ");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(true){
            isRun = true;
            if (maps == null || maps.size() <= 0) {
                Log.e(TAG,"edit video no ...");
                isRun = false;
                break;
            }
            for(Date cTime : maps.keySet()){
                List<CutVideoFragment> cvfs = maps.get(cTime);
                maps.remove(cTime);
                cutVideo(cvfs,cTime);
                break;
            }
        }
    }

    public boolean isRun(){
        return isRun;
    }

    /**
     * 执行视频切割
     * @param lists
     * @param eventTime
     * @return
     * @throws RuntimeException
     */
    public List<UploadInfo> cutVideo(List<CutVideoFragment> lists, Date eventTime) throws RuntimeException{
        List<UploadInfo> uploadInfos = new ArrayList<>();
        for(CutVideoFragment cutVideoFragment : lists){
            List<CutVideoInfo> list = new ArrayList<>();
            for(VideoInfo videoInfo : cutVideoFragment.list){
                Log.e(TAG,"切割视频："+videoInfo.fileName+" start = "+videoInfo.startTime+" endtime = "+ videoInfo.endTime);
                //拿到需要切割的视频开始时间和结束时间，执行视频切割，并入库
                if(videoInfo.isInsert){
                    CutVideoInfo cutVideoInfo = cutVideo(videoInfo);
                    if(cutVideoInfo != null){
                        list.add(cutVideoInfo);
                    }
                }
            }
            //合并视频
            CutVideoInfo cutVideoInfo = null;
            if(list.size() > 1){
                cutVideoInfo = mergeVideo(context,list);
            } else if(list. size() == 1) {
                cutVideoInfo = list.get(0);
            }
            if(cutVideoInfo != null){
                Log.e(TAG, "开始入库 : " + cutVideoInfo.path);
                //切割之后直接入库//开始上传任务
//                insertInfo(eventTime,cutVideoInfo.path,cutVideoInfo.startTime,cutVideoInfo.sourceCategory,cutVideoInfo.duration);
                UploadInfo uploadInfo = new UploadInfo();
                uploadInfo.filePath = cutVideoInfo.path;
                uploadInfo.createTime = DateFormatUtils.format(new Date(cutVideoInfo.startTime), DateFormatUtils.PATTERN_FULL);
                uploadInfo.duration = cutVideoInfo.duration;
                uploadInfo.sourceCategory = cutVideoInfo.sourceCategory;

                uploadInfos.add(uploadInfo);
            }
            list.clear();
        }
        return uploadInfos;
    }


    /**
     * 切割视频并合并视频片段 最长不会超过10s
     * @param videoInfo
     * @return
     */
    public CutVideoInfo cutVideo(VideoInfo videoInfo) {
        long startTime = DateFormatUtils.parse(videoInfo.startTime, DateFormatUtils.PATTERN_MILL).getTime();
        String fileName = videoInfo.categoryId + "_" + DateFormatUtils.format(new Date(videoInfo.cutStartTime), DateFormatUtils.PATTERN_FULL_H);
        String formats = ".mp4";

        String second = ((videoInfo.cutStartTime - startTime)/1000)+"";
        //差多少秒，就剪切多少秒
        double duration = (videoInfo.cutEndTime - videoInfo.cutStartTime)/1000;
        if(duration <= 1){
            duration += 1;
        }
        //执行视频切割
        clipVideo(videoInfo.path,second,duration,videoInfo.outputPath,fileName+formats);
        //保存视频的路径
        String filePath = videoInfo.outputPath + File.separator +  fileName;
        Log.e(TAG,"output file path is " + filePath);
        File file = new File(filePath+formats);
        //切割完之后，进行任务入库，并上传
        if(file.exists()){
            CutVideoInfo cutVideoInfo = new CutVideoInfo();
            cutVideoInfo.path = file.getPath();
            cutVideoInfo.startTime = videoInfo.cutStartTime;
            cutVideoInfo.sourceCategory = videoInfo.categoryId;
            cutVideoInfo.duration = duration;
            return cutVideoInfo;
        } else {
            Log.e("cgq","要切割视频文件不存在本地 = " + filePath + formats);
        }
        return null;
    }

    /**
     * 视频剪切 分割
     * @param srcVideoPath      资源视频文件路径
     * @param second            开始切割时间(视频开始时间向后N秒)
     * @param duration          从切割时间向后多少秒
     * @param parentPath        保存文件父级目录
     * @param fileName          切割后保存文件名称
     */
    private void clipVideo(String srcVideoPath,String second,double duration,String parentPath,String fileName) {
        File dstDirFile = new File(parentPath);
        if (!dstDirFile.exists()) {//不存在则创建
            dstDirFile.mkdir();
        }
        try {
            FFmpegController fc = new FFmpegController(context, new File(parentPath));
            Clip in = new Clip();
            in.startTime = second;
            in.path = srcVideoPath;

            Clip out = new Clip();
            out.duration = duration;
            out.audioCodec = "copy";
            out.videoCodec = "copy";
            out.path = parentPath + File.separator +  fileName;//保存视频的路径
            fc.clipVideo(in, out, true,new ShellUtils.ShellCallback() {
                @Override
                public void shellOut(String shellLine) {
                    Log.e(TAG, "shellOut() returned: " + shellLine);
                }

                @Override
                public void processComplete(int exitValue)  {
                    Log.e(TAG, "processComplete() returned: " + exitValue);
                /*if(exitValue == 0){
                    return;
                }*/

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 合并视频
     * @param context
     * @param list
     * @return
     */
    public CutVideoInfo mergeVideo(Context context, List<CutVideoInfo> list){
        if(list == null){
            return null;
        }
        CutVideoInfo cutVideoInfo1 = new CutVideoInfo();
        cutVideoInfo1.startTime = list.get(0).startTime;
        ArrayList<Clip> clips = new ArrayList<>();
        for(CutVideoInfo cutVideoInfo : list){
            cutVideoInfo1.sourceCategory = cutVideoInfo.sourceCategory;
            cutVideoInfo1.duration += cutVideoInfo.duration;

            Clip clip = new Clip();
            clip.path = cutVideoInfo.path;
            clips.add(clip);
        }

        Log.e("cgq","开始合并视频-----");
        String dstDirName = "concact";
        String sdcardPath = Environment.getExternalStorageDirectory().getPath();
        String dstDir = sdcardPath + File.separator + dstDirName;

        String fileName = cutVideoInfo1.sourceCategory + "_" + DateFormatUtils.format(new Date(cutVideoInfo1.startTime), DateFormatUtils.PATTERN_FULL_H)+"."+ Constant.MEDIA_VIDEO_SUFFIX_MP4;
        cutVideoInfo1.path = dstDir + File.separator + fileName;

        merge(context,clips,dstDir, cutVideoInfo1.path);
        for(Clip clip : clips){
            File file = new File(clip.path);
            if(file.exists()){
                file.delete();
            }
        }
        return cutVideoInfo1;
    }

    public void merge(Context context, final ArrayList<Clip> list, String parentPath, String outPath){

        try {
            FFmpegController fc = new FFmpegController(context, new File(parentPath));
            Clip out = new Clip();
            out.path = outPath;

            fc.concatVideo(list, out, false, new ShellUtils.ShellCallback() {
                @Override
                public void shellOut(String shellLine) {
                    Log.e("cgq","合并过程-----"+shellLine);
                }

                @Override
                public void processComplete(int exitValue) {
                    Log.e("cgq","合并结果-----"+exitValue);
                }
            });
            Log.e("cgq","完成合并视频-----");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("cgq","合并视频异常-----");
        }
    }

}
