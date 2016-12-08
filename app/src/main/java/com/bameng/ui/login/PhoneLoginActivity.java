package com.bameng.ui.login;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
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
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.huotu.android.library.libedittext.EditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/*
 * 手机注册并登录
 */
public class PhoneLoginActivity extends BaseActivity {

    @BindView(R2.id.titleText)
    TextView titleText;

    @BindView(R2.id.titleLayoutL)
    RelativeLayout titleLayoutL;

    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;

    @BindView(R2.id.edtUserName)
    EditText edtPhone;

    @BindView(R2.id.edtPwd)
    EditText edtpwd;

    @BindView(R2.id.btn_login)
    Button btnLogin;
    //public Resources resources;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        ButterKnife.bind(this);
        //resources = this.getResources();
        initView();
        BaseApplication.clearAll();
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

        if(progressDialog==null){
            progressDialog=new ProgressDialog(this);
        }
        progressDialog.setMessage("登录中...");
        progressDialog.show();

        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("loginName",edtPhone.getText().toString());
        map.put("password", EncryptUtil.getInstance().encryptMd532(edtpwd.getText().toString()));
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<UserOutputsModel> call = apiService.Login(token,map);
        call.enqueue(new Callback<UserOutputsModel>() {
            @Override
            public void onResponse(Call<UserOutputsModel> call, Response<UserOutputsModel> response) {
                if(progressDialog!=null)progressDialog.dismiss();
                if(response.code()!=200){
                    ToastUtils.showLongToast(response.message());
                    return;
                }

                if (response.body() != null) {
                    if (response.body().getStatus() == 200&&response.body()!=null) {
                        BaseApplication.writeUserToken(response.body().getData().getToken());
                        BaseApplication.writeUserInfo(response.body().getData());
                        if (response.body().getData().getUserIdentity()==1) {
                            ActivityUtils.getInstance().skipActivity(PhoneLoginActivity.this, HomeActivity.class);
                        }else {
                            ActivityUtils.getInstance().skipActivity(PhoneLoginActivity.this,AllyHomeActivity.class);
                        }
                    } else {
                        ToastUtils.showLongToast(response.body().getStatusText());
                    }

                }

            }


            @Override
            public void onFailure(Call<UserOutputsModel> call, Throwable t) {
                if(progressDialog!=null)progressDialog.dismiss();
                ToastUtils.showLongToast("失败");
            }
        });

    }
    private long exitTime = 0l;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 2秒以内按两次推出程序
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.showLongToast("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                closeSelf(PhoneLoginActivity.this);
                //SystemTools.killAppDestory(PhoneLoginActivity.this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
