package com.bameng.service;

import com.bameng.model.PostModel;


import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/3/15.
 */
public interface ApiService {
    @FormUrlEncoded
    @POST("/sys/init")
    Call<PostModel> init(@FieldMap Map<String, String> params);

}
