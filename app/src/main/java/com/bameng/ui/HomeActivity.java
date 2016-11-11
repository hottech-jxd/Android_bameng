package com.bameng.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
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
import com.bameng.model.PostModel;
import com.bameng.model.SlideListOutputModel;
import com.bameng.receiver.MyBroadcastReceiver;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.ui.news.AddnewsActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.ProgressPopupWindow;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends BaseActivity {


    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;

    @Bind(R.id.titleRightImage)
    ImageView titleRightImage;

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

    public Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        application = (BaseApplication) this.getApplication();
        application.loadAddress();
        mHandler = new Handler(this);
        application.mFragManager = FragManager.getIns(this, R.id.fragment_container);
        resources = this.getResources();
        initView();
        StartApi();

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
                SystemTools.killAppDestory(HomeActivity.this);
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
}
