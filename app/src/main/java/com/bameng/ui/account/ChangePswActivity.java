package com.bameng.ui.account;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.model.CloseEvent;
import com.bameng.model.GetRewardOutput;
import com.bameng.model.PostModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.HomeActivity;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.EncryptUtil;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.huotu.android.library.libedittext.EditText;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePswActivity extends BaseActivity {

    @Bind(R.id.titleLayoutL)
    RelativeLayout titleLayoutL;
    @Bind(R.id.edt_oldpsw)
    EditText edtOldpsw;
    @Bind(R.id.edt_newpsw1)
    EditText edtNewpsw1;
    @Bind(R.id.edt_newpsw2)
    EditText edtNewpsw2;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    @Bind(R.id.titleText)
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_psw);
        ButterKnife.bind(this);
        initView();

    }

    @OnClick(R.id.btn_commit) void onBtnCommitClick() {
        if (TextUtils.isEmpty(edtOldpsw.getText().toString())){
            edtOldpsw.setError("旧密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(edtNewpsw1.getText().toString())){
            edtNewpsw1.setError("新密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(edtNewpsw2.getText().toString())){
            edtNewpsw2.setError("新密码不能为空");
            return;
        }
        if (!edtNewpsw1.getText().toString().equals(edtNewpsw2.getText().toString())){
            ToastUtils.showLongToast("两次输入的密码不一致");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("version", application.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("oldPassword", EncryptUtil.getInstance().encryptMd532(edtOldpsw.getText().toString()));
        map.put("newPassword",EncryptUtil.getInstance().encryptMd532(edtNewpsw1.getText().toString()));
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        String token = application.readToken();
        Call<PostModel> call = apiService.ChanagePassword(token,map);
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if (response.body() != null) {


                    if (response.body().getStatus() == 200&&response.body()!=null) {
                        application.writeUserToken("");
                        EventBus.getDefault().post(new CloseEvent());
                        ActivityUtils.getInstance().skipActivity(ChangePswActivity.this, PhoneLoginActivity.class);
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
    protected void initView() {
        titleText.setText("修改密码");
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);

    }

    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
