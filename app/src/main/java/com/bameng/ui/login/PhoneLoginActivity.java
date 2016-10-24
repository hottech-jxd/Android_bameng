package com.bameng.ui.login;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Button;
import com.bameng.R;
import com.bameng.ui.HomeActivity;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.ActivityUtils;
import com.huotu.android.library.libedittext.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/*
 * 手机注册并登录
 */
public class PhoneLoginActivity extends BaseActivity {
    @Bind(R.id.edtPhone)
    EditText edtPhone;

    @Bind(R.id.edtCode)
    EditText edtCode;

    @Bind(R.id.btnLogin)
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

    }

    @Override
    protected void StartApi() {

    }
    /***
     * 手机登录
     */
    @OnClick(R.id.btnLogin)
    protected void onBtnLoginClick(){
        ActivityUtils.getInstance().skipActivity(PhoneLoginActivity.this, HomeActivity.class);
        String phone = edtPhone.getText().toString().trim();
        String code = edtCode.getText().toString().trim();
//        if(TextUtils.isEmpty( phone )){
//            edtPhone.setError("请输入手机号");
//            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(edtPhone , 0);
//            return;
//        }
//
//        if(TextUtils.isEmpty(code)){
//            edtCode.setError("请输入验证码");
//            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(edtCode,0);
//            return;
//        }
//
//        ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//
//        login( phone , code );
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
