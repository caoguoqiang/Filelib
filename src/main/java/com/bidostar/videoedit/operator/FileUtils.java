package com.bidostar.videoedit.operator;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by small.cao on 2017/10/12.
 */

public class FileUtils {

    private static   String storagePath = "";
    private static final String DST_FOLDER_NAME = "obdTest";

    /**初始化保存路径
     * @return
     */
    private static String initPath(){
        if(storagePath.equals("")){
            storagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/" + DST_FOLDER_NAME;
            File f = new File(storagePath);
            if(!f.exists()){
                f.mkdir();
            }
        }
        return storagePath;
    }

    /**保存Bitmap到sdcard
     * @param b
     * @return 返回拍照图片在内存卡中的地址
     */
    public static String saveBitmap(Bitmap b){

        String path = initPath();
        long dataTake = System.currentTimeMillis();
        String jpegName = path + "/" + dataTake +".jpg";
        Log.i("ykz", "saveBitmap:jpegName = " + jpegName);
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            Log.i("ykz", "saveBitmap成功");
            return jpegName;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.i("ykz", "saveBitmap:失败");
//			e.printStackTrace();
            return null;
        }
    }

    /**
     * 移动文件
     * @param srcFileName    源文件完整路径
     * @param destDirName    目的目录完整路径
     * @return 文件移动成功返回true，否则返回false
     */
    public static boolean moveFile(String srcFileName, String destDirName) {

        File srcFile = new File(srcFileName);
        if(!srcFile.exists() || !srcFile.isFile())
            return false;

        File destDir = new File(destDirName);
        if (!destDir.getParentFile().exists())
            destDir.getParentFile().mkdirs();
        return srcFile.renameTo(new File(destDirName));
//        return srcFile.renameTo(new File(destDirName + File.separator + srcFile.getName()));
    }

    /**
     * 移动文件
     * @param srcFileName    源文件完整路径
     * @param destDirName    目的目录完整路径
     * @return 文件移动成功返回true，否则返回false
     */
    public static void moveFile1(String srcFileName, String destDirName) {
        File srcFile = new File(srcFileName);
        if(!srcFile.exists() || !srcFile.isFile()) {
            return;
        }
        File destDir = new File(destDirName);
        if (!destDir.getParentFile().exists()) {
            destDir.getParentFile().mkdirs();
        }
        try {
            copyFile(srcFile,destDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return srcFile.renameTo(new File(destDirName));
//        return srcFile.renameTo(new File(destDirName + File.separator + srcFile.getName()));
    }

    private static void copyFile(File source, File dest)
            throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }
}
