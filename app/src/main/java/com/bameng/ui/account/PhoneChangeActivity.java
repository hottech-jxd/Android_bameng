package com.bameng.ui.account;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.biz.SendSmsUtil;
import com.bameng.config.Constants;
import com.bameng.model.BaseModel;
import com.bameng.model.CloseEvent;
import com.bameng.model.PostModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.huotu.android.library.libedittext.EditText;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.baidu.location.h.j.v;

/***
 * 绑定手机
 */
public class PhoneChangeActivity extends BaseActivity implements CountdownView.OnCountdownEndListener,
        SendSmsUtil.SendSmsCallbackListener {

    @Bind(R.id.edtPhone)
    EditText edtPhone;
    @Bind(R.id.edtCode)
    EditText edtCode;
    @Bind(R.id.btn_code)
    TextView btnCode;
    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    @Bind(R.id.countdown)
    CountdownView countdownView;

    public Resources resources;
    SendSmsUtil sendSmsUtil;
    String oldPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_reg);
        ButterKnife.bind(this);
        initView();
        StartApi();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //countdownView.restart();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //countdownView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        countdownView.stop();
        countdownView.setOnCountdownEndListener(null);
    }

    @OnClick(R.id.btn_code)
    void onGetCode(){
        String phone = edtPhone.getText().toString().trim();
        if( TextUtils.isEmpty( phone ) ){
            edtPhone.setError("请输入手机号");
            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(edtCode, 0);
            return;
        }
        if( phone.length()<11 ){
            edtPhone.setError("请输入合法的手机号");
            edtPhone.setFocusable(true);
            return;
        }


        sendSmsUtil.sendSms(phone, this);

//        countdownView.setVisibility(View.VISIBLE);
//        btnCode.setVisibility(View.GONE);
//        countdownView.start(60*1000);
    }

    @OnClick(R.id.btn_commit)
    void onBtnCommitClick() {
        final String phone = edtPhone.getText().toString().trim();
        if( TextUtils.isEmpty( phone ) ){
            edtPhone.setError("请输入手机号");
            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(edtPhone, 0);
            return;
        }
        if( phone.length()<11 ){
            edtPhone.setError("请输入合法的手机号");
            edtPhone.setFocusable(true);
            return;
        }

        String code = edtCode.getText().toString().trim();
        if(TextUtils.isEmpty(code)){
            edtCode.setError("请输入验证码");
            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(edtCode, 0);
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("mobile", oldPhone );
        map.put("verifyCode",code);
        map.put("newMobile",phone);
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        Call<PostModel> call = apiService.ChanageMobile( BaseApplication.readToken(),map);

        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if(response.code()!=200){
                    ToastUtils.showLongToast(response.message());
                    return;
                }
                if(response.body()==null) {
                    ToastUtils.showLongToast("error");
                    return;
                }

                if (response.body().getStatus() == Constants.STATUS_70035) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(PhoneChangeActivity.this , PhoneLoginActivity.class);
                    return;
                }

                if(response.body().getStatus()!=200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }

                Intent intent=new Intent();
                intent.putExtra("newPhone",phone);
                PhoneChangeActivity.this.setResult( RESULT_OK , intent);
                PhoneChangeActivity.this.finish();
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                ToastUtils.showLongToast("失败");
            }
        });

    }

    @Override
    protected void initView() {
        oldPhone = getIntent().getStringExtra("oldPhone");

        titleText.setText("手机");
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);

        countdownView.setOnCountdownEndListener(this);

        sendSmsUtil = new SendSmsUtil();

    }

    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void onEnd(CountdownView cv) {
        btnCode.setVisibility(View.VISIBLE);
        countdownView.setVisibility(View.GONE);
    }

    @Override
    public void callback(BaseModel status) {
        if( status.getStatus() !=200){
            ToastUtils.showLongToast(status.getStatusText());
            return;
        }else{
            ToastUtils.showLongToast(status.getStatusText());
        }

        countdownView.setVisibility(View.VISIBLE);
        btnCode.setVisibility(View.GONE);
        countdownView.start(60*1000);
    }
}
