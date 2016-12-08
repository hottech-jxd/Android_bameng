package com.bameng.utils;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.format.Formatter;

import com.bameng.BaseApplication;
import com.bameng.model.VersionData;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/11/29.
 */

public class DownloadUtil {
    VersionData versionData;
    private String oldapk_filepath;
    private String newapk_savepath;
    private boolean isCancel;
    private ProgressListener progressListener;
    ClientDownLoadTask clientDownLoadTask;

    public ProgressListener getProgressListener() {
        return progressListener;
    }

    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public interface ProgressListener{
        void progress( int precent , String msg );
        void progresscomplete( int precent , String msg ,String apkPath );
        void progresserror(String message);
    }

    public DownloadUtil(VersionData versionData){
        this.versionData = versionData;
    }

     public void download(){
         String cachePath="";
         if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
             if ( BaseApplication.single.getExternalCacheDir() != null) {
                 cachePath = BaseApplication.single.getExternalCacheDir().getPath(); // most likely your null value
             }
         } else {
             if (BaseApplication.single.getCacheDir() != null) {
                 cachePath = BaseApplication.single.getCacheDir().getPath();
             }
         }

        oldapk_filepath = cachePath + File.separator + BaseApplication.single.getPackageName()+"_old.apk";
        newapk_savepath = cachePath + File.separator + BaseApplication.single.getPackageName() + "_new.apk";
         //oldapk_filepath = BaseApplication.single.getExternalCacheDir().getPath() + File.separator + BaseApplication.single.getPackageName() + "_old.apk";
        //newapk_savepath = BaseApplication.single.getExternalCacheDir().getPath() + File.separator + BaseApplication.single.getPackageName() + "_new.apk";
        //softwarePath = BaseApplication.single.getExternalCacheDir().getPath() + File.separator + BaseApplication.single.getPackageName() + "_patch.apk";

        clientDownLoadTask = new ClientDownLoadTask();
        clientDownLoadTask.execute( versionData.getUpdateUrl() );
    }

    class ClientDownLoadTask extends AsyncTask<String, Integer, Integer> {

        public ClientDownLoadTask() {
        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            HttpURLConnection hc;
            try {
                url = new URL(params[0]);
                hc = (HttpURLConnection) url.openConnection();
                //hc.setConnectTimeout(15000);
                //hc.setUseCaches(false);
                //hc.setDoOutput(true);

                if (hc == null) {
                    return null;
                }

                InputStream update_is = null;
                BufferedInputStream update_bis = null;
                FileOutputStream update_os = null;
                BufferedOutputStream update_bos = null;
                byte[] buffer;
                try {
                    if (hc.getResponseCode() != 200) {
                        return null;
                    }
                    int contentLen = hc.getContentLength();
                    if (contentLen == 0) {
                        return null;
                    }
                    update_is = hc.getInputStream();
                    update_bis = new BufferedInputStream(update_is, 2048);

                    File cityMapFile = new File(newapk_savepath);
                    if (cityMapFile.exists()) {
                        cityMapFile.delete();
                    }
                    cityMapFile.createNewFile();

                    update_os = new FileOutputStream(cityMapFile, false);
                    update_bos = new BufferedOutputStream(update_os, 2048);

                    buffer = new byte[2048];
                    int readed = 0;
                    int step = 0;
                    while ((step = update_bis.read(buffer)) != -1 && !isCancel) {
                        readed += step;
                        update_bos.write(buffer, 0, step);
                        update_bos.flush();
                        publishProgress((int) ((readed / (float) contentLen) * 100), readed, contentLen);
                    }
                    update_os.flush();
                    return contentLen;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (update_bis != null)
                            update_bis.close();
                        if (update_is != null)
                            update_is.close();
                        if (update_bos != null)
                            update_bos.close();
                        if (update_os != null)
                            update_os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            String des = String.format("%s/%s", formatterSize(values[1]), formatterSize(values[2]));
            if(progressListener!=null){
                progressListener.progress(values[0],des);
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            //taskIsComplete = true;
            if(result != null && !isCancel){
                String des = String.format("%s/%s", formatterSize(result), formatterSize(result));
                if(progressListener!=null){
                    progressListener.progresscomplete( 100 , des , newapk_savepath );
                }
                //下载完成
                //downloadClientSuccess();
            }else{
                //下载失败
                //downloadClientFailed();
                if(progressListener!=null){
                    progressListener.progresserror( "下载失败" );
                }
            }
        }
    }

    private String formatterSize(int size){
        return Formatter.formatFileSize( BaseApplication.single , size);
    }

    public void deleteTempApkFile(){
        try {
            File file = new File(newapk_savepath);
            if (file.exists())
                file.delete();
        }catch (Exception ex){}
    }

}
