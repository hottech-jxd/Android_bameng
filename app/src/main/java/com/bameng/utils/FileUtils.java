package com.bameng.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2015/10/28.
 */
public class FileUtils {

    private String SDPATH;

    public String getSDPATH() {
        return SDPATH;
    }

    public FileUtils() {
        //得到当前外部存储设备的目录
        SDPATH = Environment.getExternalStorageDirectory() + File.separator;
    }

    /**
     * 在SD卡上创建文件
     */
    public File creatSDFile(String fileName) throws IOException {

        File file = new File(SDPATH + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName
     */
    public File creatSDDir(String dirName) {
        File dir = new File(SDPATH + dirName);
        dir.mkdir();
        return dir;
    }

    /**
     * 判断SD卡上的文件夹是否存在
     */
    public void isFileExist(String fileName) {

        File file = new File(SDPATH + fileName);
        //判断是文件夹
        if (file.isDirectory()) {
            if (file.exists()) {
                //删除文件夹
                String[] children = file.list();
                //递归删除目录中的子目录下
                for (int i = 0; i < children.length; i++) {
                    new File(file, children[i]).delete();
                }
                // 目录此时为空，可以删除
                file.delete();
            }
        } else {
            if (file.exists()) {
                file.delete();
            }
        }

    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     */
    public File writeToSDFromInput(String path, String fileName, InputStream input) {

        File file = null;
        OutputStream output = null;
        try {
            creatSDDir(path);
            file = creatSDFile(path + File.separator + fileName);
            output = new FileOutputStream(file);
            byte buffer[] = new byte[1024];
            int len = 0;
            //如果下载成功就开往SD卡里些数据
            while ((len = input.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                input.close();
                output.close();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return file;
    }
}
