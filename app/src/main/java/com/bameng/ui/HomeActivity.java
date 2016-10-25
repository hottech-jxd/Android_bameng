package com.bameng.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
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
import com.bameng.model.PostModel;
import com.bameng.receiver.MyBroadcastReceiver;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.ProgressPopupWindow;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends BaseActivity {


    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;

    @Bind(R.id.titleText)
    TextView titleText;

    @Bind(R.id.fragment_container)
    FrameLayout fragmentContainer;

    //bottom
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        application = (BaseApplication) this.getApplication();
        application.mFragManager = FragManager.getIns(this, R.id.fragment_container);
        resources = this.getResources();
        initView();
        StartApi();

    }

    @Override
    protected void initView() {
        goback=false;
        titleText.setText(getString(R.string.app_name));
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_location);
        SystemTools.loadBackground(titleLeftImage, leftDraw);
        application.mFragManager.setCurrentFrag(FragManager.FragType.HOME);
        initTab();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            int data = bundle.getInt("home");
            if (100 == data) {
                application.mFragManager.setCurrentFrag(FragManager.FragType.HOME);
                ((HomeFragment) application.mFragManager.getCurrentFrag()).scrollToTop();
                initTab();

            }else if (200 == data){
                application.mFragManager.setCurrentFrag(FragManager.FragType.HOME);
                ((HomeFragment) application.mFragManager.getCurrentFrag()).scrollToTop();
                initTab();
            }else if (300 == data){
                application.mFragManager.setCurrentFrag(FragManager.FragType.HOME);
                ((HomeFragment) application.mFragManager.getCurrentFrag()).scrollToTop();
                initTab();
            }
        }
    }
    private void initTab() {
        Drawable oneBuyDraw = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
        SystemTools.loadBackground(homeImg, oneBuyDraw);
        homeTxt.setTextColor(resources.getColor(R.color.chocolate));
        //重置其他
        Drawable newestDraw = ContextCompat.getDrawable(this,R.mipmap.ic_launcher);
        SystemTools.loadBackground(newsImg, newestDraw);
        newsTxt.setTextColor(resources.getColor(R.color.text_color_black));
        Drawable listDraw = ContextCompat.getDrawable(this,R.mipmap.ic_launcher);
        SystemTools.loadBackground(businessImg, listDraw);
        businessTxt.setTextColor(resources.getColor(R.color.text_color_black));
        Drawable profileDraw = ContextCompat.getDrawable(this,R.mipmap.ic_launcher);
        SystemTools.loadBackground(profileImg, profileDraw);
        profileTxt.setTextColor(resources.getColor(R.color.text_color_black));
    }
    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.homePage: {
                if(progress!=null) progress.dismissView();

                //设置选中状态
                Drawable oneBuyDraw = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
                SystemTools.loadBackground(homeImg, oneBuyDraw);
                homeTxt.setTextColor(resources.getColor(R.color.chocolate));
                //重置其他
                Drawable newestDraw = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
                SystemTools.loadBackground(newsImg, newestDraw);
                newsTxt.setTextColor(resources.getColor(R.color.text_color_black));
                Drawable listDraw = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
                SystemTools.loadBackground(businessImg, listDraw);
                businessTxt.setTextColor(resources.getColor(R.color.text_color_black));
                Drawable profileDraw = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
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
                if(progress!=null) progress.dismissView();
                //设置选中状态
                Drawable oneBuyDraw = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
                SystemTools.loadBackground(homeImg, oneBuyDraw);
                homeTxt.setTextColor(resources.getColor(R.color.text_color_black));
                //重置其他
                Drawable newestDraw = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
                SystemTools.loadBackground(newsImg, newestDraw);
                newsTxt.setTextColor(resources.getColor(R.color.chocolate));
                Drawable listDraw = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
                SystemTools.loadBackground(businessImg, listDraw);
                businessTxt.setTextColor(resources.getColor(R.color.text_color_black));
                Drawable profileDraw = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
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
                if(progress!=null) progress.dismissView();
                //设置选中状态
                Drawable oneBuyDraw = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
                SystemTools.loadBackground(homeImg, oneBuyDraw);
                //重置其他
                Drawable newestDraw = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
                SystemTools.loadBackground(newsImg, newestDraw);
                newsTxt.setTextColor(resources.getColor(R.color.text_color_black));
                Drawable listDraw = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
                SystemTools.loadBackground(businessImg, listDraw);
                businessTxt.setTextColor(resources.getColor(R.color.chocolate));
                Drawable profileDraw = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
                SystemTools.loadBackground(profileImg, profileDraw);
                profileTxt.setTextColor(resources.getColor(R.color.text_color_black));


                String tag = Constants.TAG_3;
                //加载具体的页面
                Message msg = mHandler.obtainMessage(Constants.SWITCH_UI, tag);
                mHandler.sendMessage(msg);
            }
            break;
            case R.id.profilePage: {
                if(progress!=null) progress.dismissView();
                //设置选中状态
                    Drawable oneBuyDraw = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
                    SystemTools.loadBackground(homeImg, oneBuyDraw);
                    homeTxt.setTextColor(resources.getColor(R.color.text_color_black));
                    //重置其他
                    Drawable newestDraw = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
                    SystemTools.loadBackground(newsImg, newestDraw);
                    newsTxt.setTextColor(resources.getColor(R.color.text_color_black));
                    Drawable listDraw = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
                    SystemTools.loadBackground(businessImg, listDraw);
                    businessTxt.setTextColor(resources.getColor(R.color.text_color_black));
                    Drawable profileDraw = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
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
        Map<String, String> map = new HashMap<>();
        map.put("version", application.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        Call<PostModel> call = apiService.init(map);
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if (response.body() == null) {
                    ToastUtils.showLongToast(response.code() + ":" + response.message());
                    return;
                }

                if (response.body().getStatus() == 200) {
                    ToastUtils.showLongToast("成功");
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
}
