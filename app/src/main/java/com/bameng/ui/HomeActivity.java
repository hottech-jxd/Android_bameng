package com.bameng.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.config.Constants;
import com.bameng.fragment.FragManager;
import com.bameng.fragment.HomeFragment;
import com.bameng.model.BaiduLocationEvent;
import com.bameng.model.BaseModel;
import com.bameng.model.CloseEvent;
import com.bameng.model.PostModel;
import com.bameng.model.SlideListOutputModel;
import com.bameng.model.SwitchFragmentEvent;
import com.bameng.receiver.MyBroadcastReceiver;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.ui.news.AddnewsActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.PreferenceHelper;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.ProgressPopupWindow;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.Manifest;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.baidu.location.h.j.G;
import static com.baidu.location.h.j.an;
import static com.baidu.location.h.j.ap;
import static com.baidu.location.h.j.t;
import static com.bameng.service.LocationService.Longitude;
import static com.bameng.service.LocationService.address;
import static com.bameng.service.LocationService.city;
import static com.bameng.service.LocationService.latitude;

/***
 * 盟主主页
 */
@RuntimePermissions
public class HomeActivity extends BaseActivity {

    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;

    @Bind(R.id.titleLeftText)
    TextView titleLeftText;

    @Bind(R.id.titleRightImage)
    ImageView titleRightImage;

    @Bind(R.id.titleText)
    TextView titleText;

    @Bind(R.id.fragment_container)
    FrameLayout fragmentContainer;

    @Bind(R.id.homePage)
    RelativeLayout homePage;

    @Bind(R.id.homeImg)
    ImageView homeImg;

    @Bind(R.id.homeTxt)
    TextView homeTxt;

    @Bind(R.id.newsPage)
    RelativeLayout newsPage;

    @Bind(R.id.newsImg)
    ImageView newsImg;

    @Bind(R.id.newsTxt)
    TextView newsTxt;

    @Bind(R.id.businessPage)
    RelativeLayout businessPage;

    @Bind(R.id.businessImg)
    ImageView businessImg;

    @Bind(R.id.businessTxt)
    TextView businessTxt;

    @Bind(R.id.profilePage)
    RelativeLayout profilePage;

    @Bind(R.id.profileImg)
    ImageView profileImg;

    @Bind(R.id.profileTxt)
    TextView profileTxt;

    @Bind(R.id.homeBottom)
    LinearLayout homeBottom;

    public Resources resources;

    public ProgressPopupWindow progress;

    public Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);
        Log.i( HomeActivity.class.getName() , " EventBus.register---" + HomeActivity.class.getName());

        mHandler = new Handler(this);
        application.mFragManager = FragManager.getIns(this, R.id.fragment_container);
        resources = this.getResources();
        initView();
        StartApi();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);

        Log.i( HomeActivity.class.getName() , " EventBus.unregister---" + HomeActivity.class.getName());


        FragManager.clear();
    }

    @Override
    protected void initView() {
        goback=false;
        titleText.setText(getString(R.string.app_name));
        titleLeftImage.setVisibility(View.VISIBLE);
        titleRightImage.setVisibility(View.GONE);
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_location);
        SystemTools.loadBackground(titleLeftImage, leftDraw);
        Drawable rightDraw = ContextCompat.getDrawable(this , R.mipmap.ic_newadd);
        SystemTools.loadBackground(titleRightImage,rightDraw);
        application.mFragManager.setCurrentFrag(FragManager.FragType.HOME);
        initTab();

        //
        requestBaiduLocation();
    }

    void requestBaiduLocation(){
        HomeActivityPermissionsDispatcher.baiduLocationWithCheck(this);
    }



    @NeedsPermission( {Manifest.permission.READ_PHONE_STATE ,
            Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    protected void baiduLocation(){
        application.baiduLocationService.start();
    }
    @OnShowRationale({Manifest.permission.READ_PHONE_STATE ,
            Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    protected void showRationaleForBaiduLocation(PermissionRequest request){
        showRationaleDialog( R.string.permission_baiduLocation_rationale , request );
    }

    @OnPermissionDenied({Manifest.permission.READ_PHONE_STATE ,
            Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    protected void onBaiduLocationDenied(){
        Toast.makeText(this, R.string.permission_baiduLocation_denied , Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.READ_PHONE_STATE ,
            Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    protected void onBaiduLocationNeverAskAgain(){
        Toast.makeText(this, R.string.permission_baiduLocation_never_asked , Toast.LENGTH_SHORT).show();
    }

    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.permission_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.permission_dency, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        HomeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            int data = bundle.getInt("home");
            if (100 == data) {
                application.mFragManager.setCurrentFrag(FragManager.FragType.HOME);
                //((HomeFragment) application.mFragManager.getCurrentFrag()).scrollToTop();
                initTab();

            }else if (200 == data){
                application.mFragManager.setCurrentFrag(FragManager.FragType.HOME);
                //((HomeFragment) application.mFragManager.getCurrentFrag()).scrollToTop();
                initTab();
            }else if (300 == data){
                application.mFragManager.setCurrentFrag(FragManager.FragType.HOME);
                //((HomeFragment) application.mFragManager.getCurrentFrag()).scrollToTop();
                initTab();
            }
        }
    }
    private void initTab() {
        Drawable oneBuyDraw = ContextCompat.getDrawable(this, R.mipmap.ic_on_homepage);
        SystemTools.loadBackground(homeImg, oneBuyDraw);
        homeTxt.setTextColor(resources.getColor(R.color.chocolate));
        //重置其他
        Drawable newestDraw = ContextCompat.getDrawable(this,R.mipmap.ic_zx);
        SystemTools.loadBackground(newsImg, newestDraw);
        newsTxt.setTextColor(resources.getColor(R.color.text_color_black));
        Drawable listDraw = ContextCompat.getDrawable(this,R.mipmap.ic_yw);
        SystemTools.loadBackground(businessImg, listDraw);
        businessTxt.setTextColor(resources.getColor(R.color.text_color_black));
        Drawable profileDraw = ContextCompat.getDrawable(this,R.mipmap.ic_account);
        SystemTools.loadBackground(profileImg, profileDraw);
        profileTxt.setTextColor(resources.getColor(R.color.text_color_black));
    }
    @OnClick(R.id.titleRightImage)
    void onRightClick(){
        ActivityUtils.getInstance().showActivity(HomeActivity.this, AddnewsActivity.class);
    }
    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.homePage: {
                titleText.setText(getString(R.string.app_name));
                titleLeftText.setVisibility(View.VISIBLE);
                titleLeftImage.setVisibility(View.VISIBLE);
                titleRightImage.setVisibility(View.GONE);
                if(progress!=null) progress.dismissView();

                //设置选中状态
                Drawable oneBuyDraw = ContextCompat.getDrawable(this, R.mipmap.ic_on_homepage);
                SystemTools.loadBackground(homeImg, oneBuyDraw);
                homeTxt.setTextColor(resources.getColor(R.color.chocolate));
                //重置其他
                Drawable newestDraw = ContextCompat.getDrawable(this, R.mipmap.ic_zx);
                SystemTools.loadBackground(newsImg, newestDraw);
                newsTxt.setTextColor(resources.getColor(R.color.text_color_black));
                Drawable listDraw = ContextCompat.getDrawable(this, R.mipmap.ic_yw);
                SystemTools.loadBackground(businessImg, listDraw);
                businessTxt.setTextColor(resources.getColor(R.color.text_color_black));
                Drawable profileDraw = ContextCompat.getDrawable(this, R.mipmap.ic_account);
                SystemTools.loadBackground(profileImg, profileDraw);
                profileTxt.setTextColor(resources.getColor(R.color.text_color_black));
                //切换内容
                String tag = Constants.TAG_1;
                //加载具体的页面
                Message msg = mHandler.obtainMessage(Constants.SWITCH_UI, tag);
                mHandler.sendMessage(msg);
                MyBroadcastReceiver.sendBroadcast(this, MyBroadcastReceiver.GO_TO_HOMEFRAG);
            }
            break;
            case R.id.newsPage: {
                titleText.setText("资讯列表");
                titleLeftImage.setVisibility(View.GONE);
                titleLeftText.setVisibility(View.GONE);
                titleRightImage.setVisibility(View.VISIBLE);
                if(progress!=null) progress.dismissView();
                //设置选中状态
                Drawable oneBuyDraw = ContextCompat.getDrawable(this, R.mipmap.ic_homepage);
                SystemTools.loadBackground(homeImg, oneBuyDraw);
                homeTxt.setTextColor(resources.getColor(R.color.text_color_black));
                //重置其他
                Drawable newestDraw = ContextCompat.getDrawable(this, R.mipmap.ic_on_zx);
                SystemTools.loadBackground(newsImg, newestDraw);
                newsTxt.setTextColor(resources.getColor(R.color.chocolate));
                Drawable listDraw = ContextCompat.getDrawable(this, R.mipmap.ic_yw);
                SystemTools.loadBackground(businessImg, listDraw);
                businessTxt.setTextColor(resources.getColor(R.color.text_color_black));
                Drawable profileDraw = ContextCompat.getDrawable(this, R.mipmap.ic_account);
                SystemTools.loadBackground(profileImg, profileDraw);
                profileTxt.setTextColor(resources.getColor(R.color.text_color_black));

                //切换内容
                String tag = Constants.TAG_2;
                //加载具体的页面
                Message msg = mHandler.obtainMessage(Constants.SWITCH_UI, tag);
                mHandler.sendMessage(msg);
            }
            break;
            case R.id.businessPage: {
                titleText.setText("我的业务");
                titleLeftText.setVisibility(View.GONE);
                titleLeftImage.setVisibility(View.GONE);
                titleRightImage.setVisibility(View.GONE);
                if(progress!=null) progress.dismissView();
                //设置选中状态
                Drawable oneBuyDraw = ContextCompat.getDrawable(this, R.mipmap.ic_homepage);
                SystemTools.loadBackground(homeImg, oneBuyDraw);
                homeTxt.setTextColor(resources.getColor(R.color.text_color_black));
                //重置其他
                Drawable newestDraw = ContextCompat.getDrawable(this, R.mipmap.ic_zx);
                SystemTools.loadBackground(newsImg, newestDraw);
                newsTxt.setTextColor(resources.getColor(R.color.text_color_black));
                Drawable listDraw = ContextCompat.getDrawable(this, R.mipmap.ic_on_yw);
                SystemTools.loadBackground(businessImg, listDraw);
                businessTxt.setTextColor(resources.getColor(R.color.chocolate));
                Drawable profileDraw = ContextCompat.getDrawable(this, R.mipmap.ic_account);
                SystemTools.loadBackground(profileImg, profileDraw);
                profileTxt.setTextColor(resources.getColor(R.color.text_color_black));


                String tag = Constants.TAG_3;
                //加载具体的页面
                Message msg = mHandler.obtainMessage(Constants.SWITCH_UI, tag);
                mHandler.sendMessage(msg);
            }
            break;
            case R.id.profilePage: {
                titleText.setText("我的账户");
                titleLeftText.setVisibility(View.GONE);
                titleLeftImage.setVisibility(View.GONE);
                titleRightImage.setVisibility(View.GONE);
                if(progress!=null) progress.dismissView();
                //设置选中状态
                    Drawable oneBuyDraw = ContextCompat.getDrawable(this, R.mipmap.ic_homepage);
                    SystemTools.loadBackground(homeImg, oneBuyDraw);
                    homeTxt.setTextColor(resources.getColor(R.color.text_color_black));
                    //重置其他
                    Drawable newestDraw = ContextCompat.getDrawable(this, R.mipmap.ic_zx);
                    SystemTools.loadBackground(newsImg, newestDraw);
                    newsTxt.setTextColor(resources.getColor(R.color.text_color_black));
                    Drawable listDraw = ContextCompat.getDrawable(this, R.mipmap.ic_yw);
                    SystemTools.loadBackground(businessImg, listDraw);
                    businessTxt.setTextColor(resources.getColor(R.color.text_color_black));
                    Drawable profileDraw = ContextCompat.getDrawable(this, R.mipmap.ic_on_account);
                    SystemTools.loadBackground(profileImg, profileDraw);
                    profileTxt.setTextColor(resources.getColor(R.color.chocolate));
                    //切换内容
                    String tag = Constants.TAG_4;
                    //加载具体的页面
                    Message msg = mHandler.obtainMessage(Constants.SWITCH_UI, tag);
                    mHandler.sendMessage(msg);
            }
            break;
            default:
                break;
        }
    }
    @Override
    protected void StartApi() {


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
                closeSelf(HomeActivity.this);
                //SystemTools.killAppDestory(HomeActivity.this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case Constants.SWITCH_UI:{
                if(progress!=null) progress.dismissView();

                String tag = msg.obj.toString();
                if (tag.equals(Constants.TAG_1)) {
                    application.mFragManager.setCurrentFrag(FragManager.FragType.HOME);
                } else if (tag.equals(Constants.TAG_2)) {
                    application.mFragManager.setCurrentFrag(FragManager.FragType.NEWS);
                } else if (tag.equals(Constants.TAG_3)) {
                    application.mFragManager.setCurrentFrag(FragManager.FragType.BUSINESS);
                } else if (tag.equals(Constants.TAG_4)) {
                    application.mFragManager.setCurrentFrag(FragManager.FragType.PROFILE);
                }
            }
            break;
            default:
                break;
        }
        return false;
    }


    @Subscribe(threadMode = ThreadMode.MAIN )
    public void onEventBaiduLocation(BaiduLocationEvent event) {
        titleLeftText.setText("");
        if (event == null) return;
        titleLeftText.setText(event.getModel() == null || event.getModel().getCity() == null ? "" : event.getModel().getCity());
        //TODO 调用接口 上报位置信息
        String lnglat = String.valueOf( event.getModel().getLongitude() ) +","+ String.valueOf( event.getModel().getLatitude() );

        if (null != event.getModel().getCity() ) {
            PreferenceHelper.writeString(getApplicationContext(),Constants.LOCATION_INFO, Constants.CITY, event.getModel().getCity());
        }
        if (null != event.getModel().getAddress() ) {
            PreferenceHelper.writeString(getApplicationContext(),Constants.LOCATION_INFO, Constants.ADDRESS, event.getModel().getAddress());
        }
        PreferenceHelper.writeString(getApplicationContext(), Constants.LOCATION_INFO, Constants.LATITUDE, String.valueOf(event.getModel().getLatitude()));
        PreferenceHelper.writeString(getApplicationContext(), Constants.LOCATION_INFO, Constants.LONGITUDE, String.valueOf(event.getModel().getLongitude()));


        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        String token = application.readToken();
        Map<String, String> map = new HashMap<>();
        map.put("version", application.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("mylocation", event.getModel().getCity() );
        map.put("lnglat", lnglat );
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        application.baiduLocationService.stop();

        Call<BaseModel> call = apiService.myLocation( token , map);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if( response.body()==null) {
                    application.baiduLocationService.start();
                    return;
                }
                if(response.body().getStatus() == 70035 ){
                    ToastUtils.showLongToast( response.body().getStatusText());
                    ActivityUtils.getInstance().skipActivity(HomeActivity.this, PhoneLoginActivity.class );
                    return;
                }else if( response.body().getStatus() != 200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    application.baiduLocationService.start();
                    return;
                }
                //application.baiduLocationService.stop();
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                Log.e(HomeActivity.class.getName() , t.getMessage()==null? "error" : t.getMessage());
                application.baiduLocationService.start();
            }
        });


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventSwitchFragment(SwitchFragmentEvent event){
        //Message msg = mHandler.obtainMessage(Constants.SWITCH_UI, event.getFragnmentName());
        //mHandler.sendMessage( msg);
        if( event.getFragnmentName().equals( Constants.TAG_1 ) ) {
            onTabClicked(homePage);
        }else if( event.getFragnmentName().equals( Constants.TAG_2 )){
            onTabClicked(newsPage);
        }else if(event.getFragnmentName().equals( Constants.TAG_3 )){
            onTabClicked( businessPage);
        }else if(event.getFragnmentName().equals(Constants.TAG_4)){
            onTabClicked(profilePage);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventClose(CloseEvent event){
        finish();
    }

}
