package com.bameng.biz;

import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import com.bameng.BaseApplication;
import com.bameng.model.BaseModel;
import com.bameng.model.PostModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.login.ForgetPasswordActivity;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.CountDownTimerButton;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.baidu.location.h.j.t;
import static com.bameng.R.id.edtPhone;

/**
 * Created by Administrator on 2016/11/16.
 */

public class SendSmsUtil {
    public interface SendSmsCallbackListener{
        void callback(BaseModel status );
    }

    public void sendSms(String phone , final SendSmsCallbackListener listener){

        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("mobile", phone );
        //map.put("type", "1");
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        Call<PostModel> call = apiService.SendSmsForModifyPhone( BaseApplication.readToken(),map);
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if(response.code()!=200){
                    if( listener!=null){
                        BaseModel status = new BaseModel();
                        status.setStatus(response.code());
                        status.setStatusText(response.message());
                        listener.callback( status );
                    }
                    return;
                }
                if (response.body() != null) {
                    BaseModel status = new BaseModel();
                    status.setStatus(response.body().getStatus());
                    status.setStatusText(response.body().getStatusText());
                    if (listener != null) {
                        listener.callback(status);
                    }

//                    if (response.body().getStatus() == 200&&response.body()!=null) {
//                            if( listener!=null){
//                                listener.callback( new BaseModel() );
//                            }
//                        if( countDownBtn ==null ) {
//                            countDownBtn = new CountDownTimerButton( btn_code, "%dS", "获取验证码", 60000,ForgetPasswordActivity.this , 60000);
//                        }
//                        countDownBtn.start();
//                        ToastUtils.showLongToast("成功");
//                    } else {
//                        ToastUtils.showLongToast(response.body().getStatusText());
//                    }

//                }
//                return;
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                BaseModel status=  new BaseModel();
                status.setStatus(500);
                status.setStatusText("error");
                if (listener != null) {
                    listener.callback(status);
                }
            }
        });
    }
}
