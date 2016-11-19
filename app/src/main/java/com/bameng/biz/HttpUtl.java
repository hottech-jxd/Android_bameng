package com.bameng.biz;

import com.bameng.BaseApplication;
import com.bameng.model.PostModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.utils.AuthParamUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2016/11/18.
 */

public class HttpUtl<T> {

    public void post(Map<String,String> params ){
        Map<String,String> map = new HashMap<>();
        map.putAll(params);
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);

        //ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        //Call<T> call = apiService.SendSms( BaseApplication.readToken(),map);
    }

}
