package com.bameng.service;


import com.bameng.model.AllySummeryOutputModel;
import com.bameng.model.ArticleListOutput;
import com.bameng.model.AvatarOutputModel;
import com.bameng.model.BaseModel;
import com.bameng.model.BeanFlowOutputModel;
import com.bameng.model.CashCouponOutputModel;
import com.bameng.model.ConvertBeanTotalOutputModel;
import com.bameng.model.ConvertFlowModel;
import com.bameng.model.ConvertFlowOutputModel;
import com.bameng.model.CustomListOutput;
import com.bameng.model.GetRewardOutput;
import com.bameng.model.InitOutputsModel;
import com.bameng.model.MengModel;
import com.bameng.model.MengOutputModel;
import com.bameng.model.MyBusinessOutputModel;
import com.bameng.model.MyOutputModel;
import com.bameng.model.OrderDetailOutputModel;
import com.bameng.model.OrderOutputModel;
import com.bameng.model.PostModel;
import com.bameng.model.ScoreOutputModel;
import com.bameng.model.SignOutputModel;
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

import static com.baidu.location.h.j.m;

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
    Call<UserOutputsModel> myinfo(@Header("Authorization") String token, @FieldMap Map<String, String> params);
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
    @POST("/sys/SendUserSms")
    Call<PostModel> SendSmsForModifyPhone(@Header("Authorization") String token, @FieldMap Map<String, String> params);


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

    /***
     * 盟友列表
     * @param token
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/allylist")
    Call<MyOutputModel<MengModel>> allylist(@Header("Authorization") String token , @FieldMap Map<String ,String > params);

    @FormUrlEncoded
    @POST("user/UpdateInfo")
    Call<PostModel> UpdateInfo(@Header("Authorization") String token , @FieldMap Map<String,String> params);

    /***
     * 盟友申请列表
     * @param token
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/AllyApplylist")
    Call<MyOutputModel<MengModel>> AllyApplylist(@Header("Authorization") String token , @FieldMap Map<String,String> params);

    /***
     * 提交图片
     * @return
     */
    @Multipart
    @POST("user/UpdateInfo")
    Call<AvatarOutputModel> UpdateFileInfo(@Header("Authorization") String token , @PartMap Map<String, RequestBody> params );

    /***
     * 盟友申请审核
     * @param token
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/AllyApplyAudit")
    Call<BaseModel> AllyApplyAudit(@Header("Authorization") String token , @FieldMap Map<String, String> params );

    /***
     * 盟友详情
     * @param token
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/AllyInfo")
    Call<MengOutputModel> AllyInfo(@Header("Authorization") String token , @FieldMap Map<String, String> params );

    /***
     * 我的现金券列表
     * @param token
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/MyCashCouponList")
    Call<CashCouponOutputModel> MyCashCouponList(@Header("Authorization") String token , @FieldMap Map<String, String> params );

    /***
     * 兑换审核列表
     * @param token
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/ConvertAuditList")
    Call<ConvertFlowOutputModel> ConvertAuditList(@Header("Authorization") String token , @FieldMap Map<String, String> params );


    /***
     * 兑换审核
     * @param token
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/ConvertAudit")
    Call<PostModel> ConvertAudit(@Header("Authorization") String token , @FieldMap Map<String, String> params );

    /***
     * 兑换记录流水
     * @param token
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/ConvertFlow")
    Call<ConvertFlowOutputModel> ConvertFlow(@Header("Authorization") String token , @FieldMap Map<String, String> params );


    /***
     * 兑换盟豆
     * @param token
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/ConvertToBean")
    Call<PostModel> ConvertToBean(@Header("Authorization") String token , @FieldMap Map<String, String> params );


    /***
     * 盟豆流水列表
     * @param token
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/BeanFlowList")
    Call<BeanFlowOutputModel> BeanFlowList(@Header("Authorization") String token , @FieldMap Map<String, String> params );


    /***
     * 待结算盟豆列表
     * @param token
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/tempsettlebeanlist")
    Call<BeanFlowOutputModel> tempsettlebeanlist(@Header("Authorization") String token , @FieldMap Map<String, String> params );

    /***
     * 签到 接口
     * @param token
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/signin")
    Call<SignOutputModel> signin(@Header("Authorization") String token , @FieldMap Map<String, String> params );

    /***
     * 积分列表
     * @param token
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/scoreList")
    Call<ScoreOutputModel> scoreList(@Header("Authorization") String token , @FieldMap Map<String, String> params );


    /***
     * 盟友首页汇总
     * @param token
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/AllyHomeSummary")
    Call<AllySummeryOutputModel> AllyHomeSummary(@Header("Authorization") String token , @FieldMap Map<String, String> params );


    /***
     * 发送优惠券
     * @param token
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/SendCashCoupon")
    Call<PostModel> SendCashCoupon(@Header("Authorization") String token , @FieldMap Map<String, String> params );


    /***
     * 给盟友发送优惠券
     * @param token
     * @param params 盟友ID，多个用|隔开
     * @return
     */
    @FormUrlEncoded
    @POST("user/SendAllyCashCoupon")
    Call<PostModel> SendAllyCashCoupon(@Header("Authorization") String token , @FieldMap Map<String, String> params );

    /***
     * 获得可兑换盟豆数量和已兑换盟豆数量。
     * @param token
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/AlreadyConvertTotal")
    Call<ConvertBeanTotalOutputModel> AlreadyConvertTotal(@Header("Authorization") String token , @FieldMap Map<String, String> params );

    @Multipart
    @POST("order/create")
    Call<PostModel> ordercreate(@Header("Authorization") String toekn , @PartMap Map<String, RequestBody> params);


    @FormUrlEncoded
    @POST("order/myList")
    Call<OrderOutputModel> ordermyList(@Header("Authorization") String token , @FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("order/details")
    Call<OrderDetailOutputModel> orderdetails(@Header("Authorization") String token , @FieldMap Map<String,String> params);

    /***
     * 上传成交凭证
     * @param token
     * @param params
     * @return
     */
    @Multipart
    @POST("order/UploadSuccessVoucher")
    Call<PostModel> UploadSuccessVoucher(@Header("Authorization") String token , @PartMap Map<String, RequestBody> params);

    /***
     * 订单保存
     * @param token
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("order/update")
    Call<PostModel> orderUpdate(@Header("Authorization") String token , @FieldMap Map<String,String> params);

}
