package com.bameng.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
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
import com.bameng.model.VersionData;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.DownloadUtil;
import com.bameng.utils.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity implements DownloadUtil.ProgressListener {

    @Bind(R.id.splashL)
    RelativeLayout splashL;
    @Bind(R.id.tvVersion)
    TextView tvVersion;
    @Bind(R.id.tvClick)
    TextView tvClick;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    boolean isConnection = false;

    DownloadUtil downloadUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void initView() {
        String app = this.getString(R.string.app_name);
        String version = BaseApplication.getAppVersion();
        tvVersion.setText(app + version);

        BaseApplication.single.loadAddress();

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
                if(response.body().getStatus() !=200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }

//                InitOutputsModel initOutputs = new InitOutputsModel();
//                initOutputs.setData(response.body().getData());

                callback( response.body() );

//                if (response.body().getStatus() == 200 && response.body().getData() != null) {
//                    BaseApplication.writeBaseInfo(initOutputs.getData().getBaseData());
//                    //加载更新信息
//                    application.loadUpdate(initOutputs.getData().getVersionData());
//
//                    if (initOutputs.getData().getBaseData().getUserStatus() == Constants.USER_LOGIN_STATUS_FREEZE) {
//                        ToastUtils.showLongToast("账号被冻结，请重新登录");
//                        ActivityUtils.getInstance().skipActivity(SplashActivity.this, PhoneLoginActivity.class);
//                    } else if (initOutputs.getData().getBaseData().getUserStatus() == Constants.USER_LOGIN_STATUS_NOlOGIN) {
//                        ActivityUtils.getInstance().skipActivity(SplashActivity.this, PhoneLoginActivity.class);
//                    } else {
//                        BaseApplication.writeUserInfo(response.body().getData().getUserData());
//                        //加载用户信息
//                        if (initOutputs.getData().getUserData().getUserIdentity() == 1) {
//                            ActivityUtils.getInstance().skipActivity(SplashActivity.this, HomeActivity.class);
//                        } else {
//                            ActivityUtils.getInstance().skipActivity(SplashActivity.this, AllyHomeActivity.class);
//                        }
//                    }
//                } else if (response.body().getStatus() == Constants.STATUS_70035) {
//                    ToastUtils.showLongToast(response.body().getStatusText());
//                    ActivityUtils.getInstance().skipActivity(SplashActivity.this, PhoneLoginActivity.class);
//                }else {
//                    tvClick.setVisibility(View.VISIBLE);
//                    ToastUtils.showLongToast(response.body().getStatusText());
//                    return;
//                }
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

    void gotoHome( InitOutputsModel data){
        if (data.getStatus() == 200 && data.getData() != null) {
            BaseApplication.writeBaseInfo( data.getData().getBaseData());
            //加载更新信息
            application.loadUpdate(data.getData().getVersionData());

            if (data.getData().getBaseData().getUserStatus() == Constants.USER_LOGIN_STATUS_FREEZE) {
                ToastUtils.showLongToast("账号被冻结，请重新登录");
                ActivityUtils.getInstance().skipActivity(SplashActivity.this, PhoneLoginActivity.class);
            } else if (data.getData().getBaseData().getUserStatus() == Constants.USER_LOGIN_STATUS_NOlOGIN) {
                ActivityUtils.getInstance().skipActivity(SplashActivity.this, PhoneLoginActivity.class);
            } else {
                BaseApplication.writeUserInfo(data.getData().getUserData());
                //加载用户信息
                if (data.getData().getUserData().getUserIdentity() == 1) {
                    ActivityUtils.getInstance().skipActivity(SplashActivity.this, HomeActivity.class);
                } else {
                    ActivityUtils.getInstance().skipActivity(SplashActivity.this, AllyHomeActivity.class);
                }
            }
        } else if (data.getStatus() == Constants.STATUS_70035) {
            ToastUtils.showLongToast(data.getStatusText());
            ActivityUtils.getInstance().skipActivity(SplashActivity.this, PhoneLoginActivity.class);
        }else {
            tvClick.setVisibility(View.VISIBLE);
            ToastUtils.showLongToast(data.getStatusText());
        }
    }

    void callback(InitOutputsModel data ){

        if( data.getData() !=null && data.getData().getVersionData() !=null && data.getData().getVersionData().getUpdateType() == 1 ){
            checkAppUpdate( data );
        }else{
            gotoHome(data);
        }
    }

    void updataApp(VersionData versionData){
        downloadUtil = new DownloadUtil(versionData);
        downloadUtil.setProgressListener(this);
        downloadUtil.download();
    }


    private void checkAppUpdate( final InitOutputsModel data  ) {
        AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this)
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(data.getData().getVersionData().getUpdateTip())
                .setTitle("温馨提示")
                .setPositiveButton("马上更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updataApp(data.getData().getVersionData());
                    }
                })
                .setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gotoHome(data);
                    }
                })
                .create();

        alertDialog.show();

    }

    @Override
    public void progress(int precent, String msg) {
        if( !progressBar.isShown()) progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress( precent );
    }

    @Override
    public void progresscomplete(int precent, String msg , String apkPath ) {
        progressBar.setVisibility(View.GONE);
        installApk( apkPath );
    }

    @Override
    public void progresserror(String message) {
        //ToastUtils.showLongToast( "新版本更新失败...");
        downloadUtil.deleteTempApkFile();
        finish();
    }

    private void installApk(String path){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path)),	"application/vnd.android.package-archive");
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if( keyCode == KeyEvent.KEYCODE_BACK) {
            checkAppUpdataIsDoing();
            return true;
        }

        return super.onKeyDown(keyCode,event);
    }

    void checkAppUpdataIsDoing(){
        if( downloadUtil !=null){
            askStopAppUpdate();
        }else{
            finish();
        }
    }

    void askStopAppUpdate(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定要取消更新吗？")
                .setPositiveButton("继续更新",null)
                .setNegativeButton("取消更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        downloadUtil.setCancel(true);
                        //downloadUtil.deleteTempApkFile();
                        //finish();
                    }
                })
                .create();
        alertDialog.show();
    }
}
