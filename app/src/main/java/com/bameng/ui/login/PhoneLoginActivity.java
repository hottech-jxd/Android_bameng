package com.bameng.ui.login;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.model.InitOutputsModel;
import com.bameng.model.InitOutputsModel;
import com.bameng.model.PostModel;
import com.bameng.model.UserOutputsModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.AllyHomeActivity;
import com.bameng.ui.HomeActivity;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.EncryptUtil;
import com.bameng.utils.ToastUtils;
import com.huotu.android.library.libedittext.EditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/*
 * 手机注册并登录
 */
public class PhoneLoginActivity extends BaseActivity {

    @Bind(R.id.titleText)
    TextView titleText;

    @Bind(R.id.titleLayoutL)
    RelativeLayout titleLayoutL;

    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;

    @Bind(R.id.edtUserName)
    EditText edtPhone;

    @Bind(R.id.edtPwd)
    EditText edtpwd;

    @Bind(R.id.btn_login)
    Button btnLogin;
    public Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        ButterKnife.bind(this);
        resources = this.getResources();
        initView();
        StartApi();

    }

    @Override
    protected void initView() {
        goback=false;
        titleText.setText("登录");


    }

    @Override
    protected void StartApi() {

    }
    /***
     * 手机登录
     */
    @OnClick(R.id.btn_login)
    protected void onBtnLoginClick(){
        //ActivityUtils.getInstance().skipActivity(PhoneLoginActivity.this, HomeActivity.class);
        String phone = edtPhone.getText().toString().trim();
        String password = edtpwd.getText().toString().trim();
        if(TextUtils.isEmpty( phone )){
            edtPhone.setError("请输入手机号");
            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(edtPhone , 0);
            return;
        }

        if(TextUtils.isEmpty(password)){
            edtpwd.setError("请输入验证码");
            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(edtpwd,0);
            return;
        }

        ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        Map<String, String> map = new HashMap<>();
        map.put("version", application.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("loginName",edtPhone.getText().toString());
        map.put("password", EncryptUtil.getInstance().encryptMd532(edtpwd.getText().toString()));
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        String token = application.readToken();
        Call<UserOutputsModel> call = apiService.Login(token,map);
        call.enqueue(new Callback<UserOutputsModel>() {
            @Override
            public void onResponse(Call<UserOutputsModel> call, Response<UserOutputsModel> response) {
                if (response.body() != null) {
                    if (response.body().getStatus() == 200&&response.body()!=null) {
                        application.writeUserToken(response.body().getData().getToken());
                        application.writeUserInfo(response.body().getData());
                        if (response.body().getData().getUserIdentity()==1) {
                            ActivityUtils.getInstance().skipActivity(PhoneLoginActivity.this, HomeActivity.class);
                        }else {
                            ActivityUtils.getInstance().skipActivity(PhoneLoginActivity.this,AllyHomeActivity.class);
                        }
                    } else {
                        ToastUtils.showLongToast(response.body().getStatusText());
                    }

                }

                return;



            }


            @Override
            public void onFailure(Call<UserOutputsModel> call, Throwable t) {
                ToastUtils.showLongToast("失败");
            }
        });

    }

    @OnClick({R.id.tv_forgetpsd,R.id.img_forgetpsd})
    void onforgetpsd(){
        ActivityUtils.getInstance().showActivity(PhoneLoginActivity.this,ForgetPasswordActivity.class);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
