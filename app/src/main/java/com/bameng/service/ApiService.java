package com.bameng.service;


import com.bameng.model.ArticleListOutput;
import com.bameng.model.BaseModel;
import com.bameng.model.CustomListOutput;
import com.bameng.model.GetRewardOutput;
import com.bameng.model.InitOutputsModel;
import com.bameng.model.MyBusinessOutputModel;
import com.bameng.model.MyOutputModel;
import com.bameng.model.OrderOutputModel;
import com.bameng.model.PostModel;
import com.bameng.model.SlideListOutputModel;
import com.bameng.model.UserData;
import com.bameng.model.UserOutputsModel;


import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * Created by Administrator on 2016/3/15.
 */
public interface ApiService {

    @FormUrlEncoded
    @POST("/user/MyBusiness")
    Call<MyBusinessOutputModel> MyBusiness(@Header("Authorization") String token, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/customer/create")
    Call<PostModel> create(@Header("Authorization") String token, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/customer/audit")
    Call<PostModel> audit(@Header("Authorization") String token , @FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("/customer/UpdateInShop")
    Call<PostModel> UpdateInShop(@Header("Authorization") String token , @FieldMap Map<String ,String> params );

    @FormUrlEncoded
    @POST("/user/setallyRaward")
    Call<PostModel> setallyRaward(@Header("Authorization") String token, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/user/GetAllyReward")
    Call<GetRewardOutput> GetAllyReward(@Header("Authorization") String token, @FieldMap Map<String, String> params);
    @FormUrlEncoded
    @POST("/user/ChanagePassword")
    Call<PostModel> ChanagePassword(@Header("Authorization") String token, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/article/list")
    Call<ArticleListOutput> list(@Header("Authorization") String token, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/article/create")
    Call<PostModel> articlecreate(@Header("Authorization") String token , @FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("/customer/list")
    Call<CustomListOutput> customlist(@Header("Authorization") String token, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/user/myinfo")
    Call<PostModel> myinfo(@Header("Authorization") String token, @FieldMap Map<String, String> params);
    @FormUrlEncoded
    @POST("/sys/init")
    Call<InitOutputsModel> init(@Header("Authorization") String token, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/sys/FocusPic")
    Call<SlideListOutputModel> FocusPic(@Header("Authorization") String token, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/sys/sendsms")
    Call<PostModel> SendSms(@Header("Authorization") String token, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/sys/CheckUpdate")
    Call<InitOutputsModel> CheckUpdate(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/sys/MyLocation")
    Call<BaseModel> myLocation(@Header("Authorization") String token, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("user/login")
    Call<UserOutputsModel> Login(@Header("Authorization") String token, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("user/forgetpwd")
    Call<PostModel> ForgetPwd(@Header("Authorization") String token,@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("user/ChanageMobile")
    Call<PostModel> ChanageMobile(@Header("Authorization") String token , @FieldMap Map<String ,String > params);

    @FormUrlEncoded
    @POST("user/allylist")
    Call<MyOutputModel> allylist(@Header("Authorization") String token , @FieldMap Map<String ,String > params);

    @FormUrlEncoded
    @POST("user/UpdateInfo")
    Call<PostModel> UpdateInfo(@Header("Authorization") String token , @FieldMap Map<String,String> params);


    /***
     * 提交图片
     * @return
     */
    @Multipart
    @POST("user/UpdateInfo")
    Call<PostModel> UpdateFileInfo(@Header("Authorization") String toekn , @PartMap Map<String, RequestBody> params );


    @Multipart
    @POST("order/create")
    Call<PostModel> ordercreate(@Header("Authorization") String toekn , @PartMap Map<String, RequestBody> params);


    @FormUrlEncoded
    @POST("order/myList")
    Call<OrderOutputModel> ordermyList(@Header("Authorization") String token , @FieldMap Map<String,String> params);

}
