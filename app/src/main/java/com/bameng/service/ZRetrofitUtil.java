package com.bameng.service;


import com.bameng.config.Constants;

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
        if ( retrofitClient==null){
            retrofitClient = new Retrofit.Builder()
                    .baseUrl(  Constants.url  )
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitClient;
    }


}
