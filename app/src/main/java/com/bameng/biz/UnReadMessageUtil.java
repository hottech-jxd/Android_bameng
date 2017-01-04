package com.bameng.biz;

import android.util.Log;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.model.BadgeEvent;
import com.bameng.model.BadgeNewEvent;
import com.bameng.model.RemindOutputModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.HomeActivity;
import com.bameng.utils.AuthParamUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/12/16.
 */
public class UnReadMessageUtil {

    public static void getUnReadMessage(){
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<RemindOutputModel> call = apiService.remind(token, map);
        call.enqueue(new Callback<RemindOutputModel>() {
            @Override
            public void onResponse(Call<RemindOutputModel> call, Response<RemindOutputModel> response) {
                if(response.code() ==200 && response.body()!=null && response.body().getStatus()==200){
                    BaseApplication.writeMessageInfo(
                            response.body().getData().getMessageCount() ,
                            response.body().getData().isBusinessRemind() ,
                            response.body().getData().getMessagePushCount(),
                            response.body().getData().getMessagePullCount());
                    boolean hasMessage= (response.body().getData().getMessageCount()+ response.body().getData().getMessagePullCount()+ response.body().getData().getMessagePushCount()) > 0;
                    EventBus.getDefault().post( new BadgeEvent(  hasMessage , response.body().getData().isBusinessRemind() ));
                    EventBus.getDefault().post(new BadgeNewEvent());
                }
            }

            @Override
            public void onFailure(Call<RemindOutputModel> call, Throwable t) {
                Log.e(this.getClass().getName(), t.getMessage()==null?"error":t.getMessage());
            }
        });
    }
}
