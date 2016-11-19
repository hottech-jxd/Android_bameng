package com.bameng.ui.login;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.model.PostModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.HomeActivity;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.EncryptUtil;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.CountDownTimerButton;
import com.huotu.android.library.libedittext.EditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class ForgetPasswordActivity extends BaseActivity implements CountDownTimerButton.CountDownFinishListener {

    @Bind(R.id.edtPhone)
    EditText edtPhone;
    @Bind(R.id.edtCode)
    EditText edtCode;
    @Bind(R.id.edtPsd)
    EditText edtPsd;

    @Bind(R.id.titleText)
    TextView titleText;

    @Bind(R.id.titleLayoutL)
    RelativeLayout titleLayoutL;

    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;

    @Bind(R.id.btn_code)
    TextView btn_code;
    CountDownTimerButton countDownBtn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        initView();
        StartApi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    protected void initView() {
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);
        titleText.setText("忘记密码");
    }

    @Override
    protected void StartApi() {

    }


    @OnClick(R.id.btn_code)
    void getcode(){
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
            Map<String, String> map = new HashMap<>();
            map.put("version", application.getAppVersion());
            map.put("timestamp", String.valueOf(System.currentTimeMillis()));
            map.put("os", "android");
            map.put("mobile",edtPhone.getText().toString());
            AuthParamUtils authParamUtils = new AuthParamUtils();
            String sign = authParamUtils.getSign(map);
            map.put("sign", sign);
            ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
            Call<PostModel> call = apiService.SendSms(application.readToken(),map);
            call.enqueue(new Callback<PostModel>() {
                @Override
                public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                    if (response.body() != null) {


                        if (response.body().getStatus() == 200&&response.body()!=null) {

                            if( countDownBtn ==null ) {
                                countDownBtn = new CountDownTimerButton( btn_code, "%dS", "获取验证码", 60000,ForgetPasswordActivity.this , 60000);
                            }
                            countDownBtn.start();
                            ToastUtils.showLongToast("成功");
                        } else {
                            ToastUtils.showLongToast(response.body().getStatusText());
                        }

                    }

                    return;



                }


                @Override
                public void onFailure(Call<PostModel> call, Throwable t) {
                    ToastUtils.showLongToast("失败");
                }
            });

    }
    @OnClick(R.id.btn_commit)
    void commitcode(){
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
        if (edtCode.getText().toString().isEmpty()){
            edtCode.setError("请输入验证码");
            return;
        }
        if (edtPsd.getText().toString().isEmpty()){
            edtPsd.setError("请输入新密码");
            return;

        }
        Map<String, String> map = new HashMap<>();
        map.put("version", application.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("mobile",edtPhone.getText().toString());
        map.put("verifyCode",edtCode.getText().toString());
        map.put("password",edtPsd.getText().toString());
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        Call<PostModel> call = apiService.ForgetPwd(application.readToken(),map);
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if (response.body() != null) {


                    if (response.body().getStatus() == 200&&response.body()!=null) {

//                        if( countDownBtn ==null ) {
//                            countDownBtn = new CountDownTimerButton( btn_code, "%dS", "获取验证码", 60000,ForgetPasswordActivity.this , 60000);
//                        }
//                        countDownBtn.start();
//                        ToastUtils.showLongToast("成功");
                    } else {
                        ToastUtils.showLongToast(response.body().getStatusText());
                    }

                }

                return;



            }


            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                ToastUtils.showLongToast("失败");
            }
        });

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void timeFinish() {
        if( btn_code==null)return;
        if(countDownBtn!=null){
            countDownBtn.Stop();
            countDownBtn=null;
        }
        btn_code.setText("获取验证码");
    }

    @Override
    public void timeProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                return;
            }
        });
    }


}
