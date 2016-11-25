package com.bameng.ui;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.config.Constants;
import com.bameng.model.InitOutputsModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.baidu.location.h.j.V;
import static com.baidu.location.h.j.t;

public class SplashActivity extends BaseActivity {

    @Bind(R.id.splashL)
    RelativeLayout splashL;
    @Bind(R.id.tvVersion)
    TextView tvVersion;
    @Bind(R.id.layRefresh)
    RelativeLayout layRefresh;
    @Bind(R.id.tvClick)
    TextView tvClick;

    private boolean isConnection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        application = (BaseApplication) SplashActivity.this.getApplication();
        setImmerseLayout(splashL);
        initView();
    }

    @Override
    protected void initView() {
        String app = this.getString(R.string.app_name);
        String version = BaseApplication.getAppVersion();
        tvVersion.setText(app + version);

        BaseApplication.single.loadAddress();

        //加载背景图片
//        Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.splash_bg);
//        SystemTools.loadBackground(splashL, drawable);

        AlphaAnimation anima = new AlphaAnimation(0.0f, 1.0f);
        anima.setDuration(1000);// 设置动画显示时间
        splashL.setAnimation(anima);
        anima.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //检测网络
                isConnection = BaseApplication.checkNet(SplashActivity.this);
                if (!isConnection) {
                    //无网络日志
                    ToastUtils.showLongToast("无网络，请打开网络。");
                } else {
                    StartApi();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @OnClick(R.id.tvClick)
    void onRefresh(View v){
        StartApi();
    }

    @Override
    protected void StartApi() {
        tvClick.setVisibility(View.GONE);
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<InitOutputsModel> call = apiService.init(token, map);
        call.enqueue(new Callback<InitOutputsModel>() {
            @Override
            public void onResponse(Call<InitOutputsModel> call, Response<InitOutputsModel> response) {
                if (response.code() != 200) {
                    tvClick.setVisibility(View.VISIBLE);
                    ToastUtils.showLongToast(response.message());
                    return;
                }
                if (response.body() == null) {
                    tvClick.setVisibility(View.VISIBLE);
                    ToastUtils.showLongToast("发送错误");
                    return;
                }


                InitOutputsModel initOutputs = new InitOutputsModel();
                initOutputs.setData(response.body().getData());

                if (response.body().getStatus() == 200 && response.body().getData() != null) {
                    BaseApplication.writeBaseInfo(initOutputs.getData().getBaseData());
                    //加载更新信息
                    application.loadUpdate(initOutputs.getData().getVersionData());

                    if (initOutputs.getData().getBaseData().getUserStatus() == Constants.USER_LOGIN_STATUS_FREEZE) {
                        ToastUtils.showLongToast("账号被冻结，请重新登录");
                        ActivityUtils.getInstance().skipActivity(SplashActivity.this, PhoneLoginActivity.class);
                    } else if (initOutputs.getData().getBaseData().getUserStatus() == Constants.USER_LOGIN_STATUS_NOlOGIN) {
                        ActivityUtils.getInstance().skipActivity(SplashActivity.this, PhoneLoginActivity.class);
                    } else {
                        BaseApplication.writeUserInfo(response.body().getData().getUserData());
                        //加载用户信息
                        if (initOutputs.getData().getUserData().getUserIdentity() == 1) {
                            ActivityUtils.getInstance().skipActivity(SplashActivity.this, HomeActivity.class);
                        } else {
                            ActivityUtils.getInstance().skipActivity(SplashActivity.this, AllyHomeActivity.class);
                        }
                    }
                } else if (response.body().getStatus() == Constants.STATUS_70035) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    ActivityUtils.getInstance().skipActivity(SplashActivity.this, PhoneLoginActivity.class);
                }else {
                    tvClick.setVisibility(View.VISIBLE);
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }
            }


            @Override
            public void onFailure(Call<InitOutputsModel> call, Throwable t) {
                tvClick.setVisibility(View.VISIBLE);
                ToastUtils.showLongToast("失败");
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
