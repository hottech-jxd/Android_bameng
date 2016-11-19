package com.bameng.service;


import com.bameng.BuildConfig;
import com.bameng.config.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/3/15.
 */
public class ZRetrofitUtil {
    private static Retrofit retrofitClient;
    /**
     * 获得UI配置接口
     * @return
     */
    public static Retrofit getInstance(){
        if(BuildConfig.DEBUG) {
            if (retrofitClient == null) {
                OkHttpClient okHttpClient = new OkHttpClient.Builder().retryOnConnectionFailure(false).connectTimeout(120, TimeUnit.SECONDS).build();
                retrofitClient = new Retrofit.Builder().client(okHttpClient)
                        .baseUrl(Constants.url).addConverterFactory(GsonConverterFactory.create()).build();
            }
        }else {

            if (retrofitClient == null) {
                retrofitClient = new Retrofit.Builder()
                        .baseUrl(Constants.url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
        }
        return retrofitClient;
    }
}
