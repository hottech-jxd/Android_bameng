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

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.config.Constants;
import com.bameng.model.CloseEvent;
import com.bameng.model.PostModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.HomeActivity;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.business.AlliesDetailsActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.EncryptUtil;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.CountDownTimerButton;
import com.huotu.android.library.libedittext.EditText;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.baidu.location.h.j.t;


/***
 * 忘记密码
 */
public class ForgetPasswordActivity extends BaseActivity implements CountDownTimerButton.CountDownFinishListener {

    @BindView(R2.id.edtPhone)
    EditText edtPhone;
    @BindView(R2.id.edtCode)
    EditText edtCode;
    @BindView(R2.id.edtPsd)
    EditText edtPsd;

    @BindView(R2.id.titleText)
    TextView titleText;

    @BindView(R2.id.titleLayoutL)
    RelativeLayout titleLayoutL;

    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;

    @BindView(R2.id.btn_code)
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
        //Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        //SystemTools.loadBackground(titleLeftImage, leftDraw);
        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);

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
            map.put("version", BaseApplication.getAppVersion());
            map.put("timestamp", String.valueOf(System.currentTimeMillis()));
            map.put("os", "android");
            map.put("type", "1");
            map.put("mobile",edtPhone.getText().toString());
            AuthParamUtils authParamUtils = new AuthParamUtils();
            String sign = authParamUtils.getSign(map);
            map.put("sign", sign);
            ApiService apiService = ZRetrofitUtil.getApiService();
            Call<PostModel> call = apiService.SendSms(BaseApplication.readToken(),map);
            call.enqueue(new Callback<PostModel>() {
                @Override
                public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                    if (response.code() != 200) {
                        ToastUtils.showLongToast(response.message());
                        return;
                    }
                    if (response.body() == null) {
                        ToastUtils.showLongToast("服务器发送错误");
                        return;
                    }

                    if (response.body().getStatus() == Constants.STATUS_70035) {
                        ToastUtils.showLongToast(response.body().getStatusText());
                        EventBus.getDefault().post(new CloseEvent());
                        ActivityUtils.getInstance().skipActivity(ForgetPasswordActivity.this, PhoneLoginActivity.class);
                        return;
                    }

                    if (response.body().getStatus() != 200) {
                        ToastUtils.showLongToast(response.body().getStatusText());
                        return;
                    }


                    if (response.body().getStatus() == 200 && response.body() != null) {

                        if (countDownBtn == null) {
                            countDownBtn = new CountDownTimerButton(btn_code, "%dS", "获取验证码", 60000, ForgetPasswordActivity.this, 60000);
                        }
                        countDownBtn.start();
                        ToastUtils.showLongToast(response.body().getStatusText());
                    } else {
                        ToastUtils.showLongToast(response.body().getStatusText());
                    }
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
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("mobile",edtPhone.getText().toString());
        map.put("verifyCode",edtCode.getText().toString());
        map.put("password",edtPsd.getText().toString());
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        Call<PostModel> call = apiService.ForgetPwd(BaseApplication.readToken(),map);
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if (response.body() != null) {
                    if (response.body().getStatus() == 200&&response.body()!=null) {
                        ToastUtils.showLongToast(response.body().getStatusText());
                        ForgetPasswordActivity.this.finish();
                    } else {
                        ToastUtils.showLongToast(response.body().getStatusText());
                    }
                }
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
