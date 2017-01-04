package com.bameng.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.share.LocationShareURLOption;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.biz.UnReadMessageUtil;
import com.bameng.config.Constants;
import com.bameng.fragment.FragManager;
import com.bameng.model.BadgeEvent;
import com.bameng.model.BadgeNewEvent;
import com.bameng.model.BaiduLocationEvent;
import com.bameng.model.BaseModel;
import com.bameng.model.CloseEvent;
import com.bameng.model.SetRightVisibleEvent;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.base.BaseShareActivity;
import com.bameng.ui.business.CustomerInfoActivity;
import com.bameng.ui.business.SubmitCustomerInfoActivity;
import com.bameng.ui.business.SubmitCustomerPictureActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.ui.news.AddnewsActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.DensityUtils;
import com.bameng.utils.PreferenceHelper;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.BMPopWindow;
import com.bameng.widgets.ProgressPopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
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

import static com.umeng.analytics.a.p;
import static com.umeng.analytics.social.e.t;

/**
 * 盟友主页
 */
@RuntimePermissions
public class AllyHomeActivity extends BaseShareActivity implements  View.OnClickListener{

    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;

    @BindView(R2.id.titleLeftText)
    TextView titleLeftText;

    @BindView(R2.id.titleRightImage)
    ImageView titleRightImage;

    @BindView(R.id.titleRightText)
    TextView titleRightText;

    @BindView(R2.id.titleText)
    TextView titleText;

    @BindView(R2.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R2.id.homePage)
    RelativeLayout homePage;

    @BindView(R2.id.homeImg)
    ImageView homeImg;

    @BindView(R2.id.homeTxt)
    TextView homeTxt;

    @BindView(R2.id.richesPage)
    RelativeLayout richesPage;

    @BindView(R2.id.richesImg)
    ImageView richesImg;

    @BindView(R2.id.richesTxt)
    TextView richesTxt;

    @BindView(R2.id.newsPage)
    RelativeLayout newsPage;

    @BindView(R2.id.newsImg)
    ImageView newsImg;

    @BindView(R2.id.newsTxt)
    TextView newsTxt;

    @BindView(R.id.circle_news)
    View circleNews;

    @BindView(R.id.layTitleRight)
    LinearLayout layTitleRight;

    public Resources resources;
    public ProgressPopupWindow progress;

    FragManager mFragManager;

    String currentTab = Constants.TAG_1;
    long exitTime = 0;
    BMPopWindow bmPopWindow;
    List<String> menus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ally_home);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);

        mFragManager = FragManager.getIns(this, R.id.fragment_container);
        resources = this.getResources();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FragManager.clear();
        EventBus.getDefault().unregister(this);
        BaseApplication.clearAll();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        outState.putString("curfrag", currentTab);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState==null)return;
        currentTab = savedInstanceState.getString("curfrag");
        if(currentTab==null || currentTab.isEmpty())return;

        if( currentTab.equals( Constants.TAG_1 ) ) {
            onTabClicked(homePage);
        }else if( currentTab.equals( Constants.TAG_2 )){
            onTabClicked(newsPage);
        }else if(currentTab.equals(Constants.TAG_4)){
            onTabClicked(richesPage);
        }
    }

    @Override
    protected void initView() {
        super.initView();

        menus= new ArrayList<>();
        menus.add("新增客户资料");
        menus.add("新增客户照片");

        goback = false;
        titleText.setText("业务客户");
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftImage.setImageResource(R.mipmap.ic_location);

        titleLeftText.setBackgroundResource(R.drawable.item_click_selector);

        //titleRightImage.setBackgroundResource(R.drawable.title_left_back);
        titleRightImage.setImageResource(R.mipmap.ic_newadd);
        //int rightPx = DensityUtils.dip2px(this,1);
        //titleRightImage.setPadding( titleRightImage.getPaddingLeft(),titleRightImage.getPaddingTop() , rightPx , titleRightImage.getPaddingBottom() );

        layTitleRight.setBackgroundResource(R.drawable.item_click_selector);

        int leftPx = DensityUtils.dip2px(this,5);
        titleRightText.setPadding( leftPx , titleRightText.getPaddingTop() , titleRightText.getPaddingRight(),titleRightText.getPaddingBottom() );
        titleRightText.setText("新增");

        mFragManager.setCurrentFrag(FragManager.FragType.ALLYHOME);
        initTab();

        requestBaiduLocation();

        periodGetUnReadMsg();
    }

    void periodGetUnReadMsg(){
        mHandler.postDelayed( runnable,1000);
    }

    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            boolean hasNews = BaseApplication.readNewsMessage();
            circleNews.setBackgroundResource( hasNews ?R.drawable.circle_red:R.drawable.circle_white);
            UnReadMessageUtil.getUnReadMessage();
            mHandler.postDelayed(runnable , 30000);
        }
    };

    void requestBaiduLocation() {
        AllyHomeActivityPermissionsDispatcher.baiduLocationWithCheck(this);
    }

    @NeedsPermission({Manifest.permission.READ_PHONE_STATE,Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    protected void baiduLocation() {
        application.baiduLocationService.start();
    }

    @OnShowRationale({Manifest.permission.READ_PHONE_STATE,Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    protected void showRationaleForBaiduLocation(PermissionRequest request) {
        showRationaleDialog(R.string.permission_baiduLocation_rationale, request);
    }

    @OnPermissionDenied({Manifest.permission.READ_PHONE_STATE,Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    protected void onBaiduLocationDenied() {
        Toast.makeText(this, R.string.permission_baiduLocation_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.READ_PHONE_STATE,Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    protected void onBaiduLocationNeverAskAgain() {
        Toast.makeText(this, R.string.permission_baiduLocation_never_asked, Toast.LENGTH_SHORT).show();
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

        AllyHomeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnClick({R.id.titleLeftText})
    void shareLocation(View view){
        Object obj  =view.getTag();
        if(obj==null ) {
            ToastUtils.showLongToast("缺少位置信息，无法分享");
            return;
        }
        LatLng latLng = (LatLng) obj;
        if( latLng==null ){
            ToastUtils.showLongToast("缺少位置信息，无法分享");
            return;
        }

        if(progressDialog==null){
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage("请稍等...");
        progressDialog.show();

        if(shareUrlSearch==null){
            shareUrlSearch = ShareUrlSearch.newInstance();
            shareUrlSearch.setOnGetShareUrlResultListener(this);
        }
        LocationShareURLOption option = new LocationShareURLOption();
        option.name("我的位置");
        String city = ((TextView)view).getText().toString();
        option.snippet(city);
        option.location( latLng);
        shareUrlSearch.requestLocationShareUrl(option);
    }



    private void initTab() {
        //currentTab="业务客户";
        currentTab = Constants.TAG_1;

        Drawable oneBuyDraw = ContextCompat.getDrawable(this, R.mipmap.ic_on_homepage);
        SystemTools.loadBackground(homeImg, oneBuyDraw);
        homeTxt.setTextColor(resources.getColor(R.color.chocolate));
        //重置其他
        Drawable newestDraw = ContextCompat.getDrawable(this, R.mipmap.ic_zx);
        SystemTools.loadBackground(newsImg, newestDraw);
        newsTxt.setTextColor(resources.getColor(R.color.text_color_black));
        Drawable listDraw = ContextCompat.getDrawable(this, R.mipmap.ic_riches);
        SystemTools.loadBackground(richesImg, listDraw);
        richesTxt.setTextColor(resources.getColor(R.color.text_color_black));
    }

    @OnClick(R.id.layTitleRight)
    void onRightClick() {
        if (currentTab.equals(Constants.TAG_1)) {
            //ActivityUtils.getInstance().showActivity(AllyHomeActivity.this, SubmitCustomerInfoActivity.class);
            popWindow();
        }
//        else if (currentTab.equals(Constants.TAG_2)) {
//            ActivityUtils.getInstance().showActivity(AllyHomeActivity.this, AddnewsActivity.class);
//        }
    }

    /***
     * 弹出选择框
     */
    void popWindow(){
        if(bmPopWindow==null){
            bmPopWindow = new BMPopWindow(this, menus ,this);
        }
        bmPopWindow.show();
    }



    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.homePage: {
                currentTab = Constants.TAG_1;
                titleText.setText("业务客户");
                titleLeftImage.setVisibility(View.VISIBLE);
                titleLeftText.setVisibility(View.VISIBLE);
                titleRightImage.setVisibility(View.VISIBLE);
                titleRightText.setVisibility(View.VISIBLE);
                if (progress != null) progress.dismissView();
                //设置选中状态
                Drawable oneBuyDraw = ContextCompat.getDrawable(this, R.mipmap.ic_on_homepage);
                SystemTools.loadBackground(homeImg, oneBuyDraw);
                homeTxt.setTextColor( ContextCompat.getColor( this ,  R.color.bottomSelectedColor));
                //重置其他
                Drawable newestDraw = ContextCompat.getDrawable(this, R.mipmap.ic_zx);
                SystemTools.loadBackground(newsImg, newestDraw);
                newsTxt.setTextColor(ContextCompat.getColor( this ,R.color.text_color_black));
                Drawable profileDraw = ContextCompat.getDrawable(this, R.mipmap.ic_riches);
                SystemTools.loadBackground(richesImg, profileDraw);
                richesTxt.setTextColor(ContextCompat.getColor( this , R.color.text_color_black));
                mFragManager.setCurrentFrag(FragManager.FragType.ALLYHOME);
            }
            break;
            case R.id.newsPage: {
                currentTab = Constants.TAG_2;
                titleText.setText("资讯列表");
                titleLeftImage.setVisibility(View.GONE);
                titleLeftText.setVisibility(View.GONE);
                titleRightImage.setVisibility(View.GONE);
                titleRightText.setVisibility(View.GONE);
                if (progress != null) progress.dismissView();
                //设置选中状态
                Drawable oneBuyDraw = ContextCompat.getDrawable(this, R.mipmap.ic_homepage);
                SystemTools.loadBackground(homeImg, oneBuyDraw);
                homeTxt.setTextColor( ContextCompat.getColor(this , R.color.text_color_black));
                //重置其他
                Drawable newestDraw = ContextCompat.getDrawable(this, R.mipmap.ic_on_zx);
                SystemTools.loadBackground(newsImg, newestDraw);
                newsTxt.setTextColor(ContextCompat.getColor( this , R.color.bottomSelectedColor));
                Drawable listDraw = ContextCompat.getDrawable(this, R.mipmap.ic_riches);
                SystemTools.loadBackground(richesImg, listDraw);
                richesTxt.setTextColor(ContextCompat.getColor(this, R.color.text_color_black));
                mFragManager.setCurrentFrag(FragManager.FragType.NEWS);

            }
            break;
            case R.id.richesPage: {
                //currentTab="财富";
                currentTab = Constants.TAG_4;

                titleText.setText("财富");
                titleLeftImage.setVisibility(View.GONE);
                titleLeftText.setVisibility(View.GONE);
                titleRightImage.setVisibility(View.GONE);
                titleRightText.setVisibility(View.GONE);
                if (progress != null) progress.dismissView();
                //设置选中状态
                Drawable oneBuyDraw = ContextCompat.getDrawable(this, R.mipmap.ic_homepage);
                SystemTools.loadBackground(homeImg, oneBuyDraw);
                homeTxt.setTextColor(ContextCompat.getColor(this, R.color.text_color_black));
                //重置其他
                Drawable newestDraw = ContextCompat.getDrawable(this, R.mipmap.ic_zx);
                SystemTools.loadBackground(newsImg, newestDraw);
                newsTxt.setTextColor(ContextCompat.getColor(this, R.color.text_color_black));
                Drawable listDraw = ContextCompat.getDrawable(this, R.mipmap.ic_on_riches);
                SystemTools.loadBackground(richesImg, listDraw);
                richesTxt.setTextColor(ContextCompat.getColor(this, R.color.bottomSelectedColor ));
                mFragManager.setCurrentFrag(FragManager.FragType.PROFILE );
            }
            break;
        }
    }
    @Override
    protected void StartApi() {
    }

    @Override
    public boolean handleMessage(Message msg) {
        return super.handleMessage(msg);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 2秒以内按两次推出程序
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.showLongToast("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                closeSelf(this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventClose(CloseEvent event){
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRightButtomVisible(SetRightVisibleEvent event){
        if( currentTab.equals( event.getTabName() )) {
            titleRightImage.setVisibility(event.isShow() ? View.VISIBLE : View.GONE);
            titleRightText.setVisibility(event.isShow()?View.VISIBLE:View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN )
    public void onEventBaiduLocation(BaiduLocationEvent event) {
        titleLeftText.setText("");
        if (event == null) return;
        titleLeftText.setText(event.getModel() == null || event.getModel().getCity() == null ? "" : event.getModel().getCity());

        String lnglat = String.valueOf( event.getModel().getLongitude() ) +","+ String.valueOf( event.getModel().getLatitude() );
        LatLng latLng = new LatLng( event.getModel().getLatitude() , event.getModel().getLongitude() );
        titleLeftText.setTag( latLng );


        if (null != event.getModel().getCity() ) {
            PreferenceHelper.writeString(getApplicationContext(),Constants.LOCATION_INFO, Constants.CITY, event.getModel().getCity());
        }
        if (null != event.getModel().getAddress() ) {
            PreferenceHelper.writeString(getApplicationContext(),Constants.LOCATION_INFO, Constants.ADDRESS, event.getModel().getAddress());
        }
        PreferenceHelper.writeString(getApplicationContext(), Constants.LOCATION_INFO, Constants.LATITUDE, String.valueOf(event.getModel().getLatitude()));
        PreferenceHelper.writeString(getApplicationContext(), Constants.LOCATION_INFO, Constants.LONGITUDE, String.valueOf(event.getModel().getLongitude()));


        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        String token = BaseApplication.readToken();
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("mylocation", event.getModel().getCity() );
        map.put("lnglat", lnglat );
        map.put("addr", event.getModel().getAddress() );
        String sign = AuthParamUtils.getSign(map);
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
                    ActivityUtils.getInstance().skipActivity(AllyHomeActivity.this, PhoneLoginActivity.class );
                    return;
                }else if( response.body().getStatus() != 200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    application.baiduLocationService.start();
                    return;
                }

            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                Log.e(HomeActivity.class.getName() , t.getMessage()==null? "error" : t.getMessage());
                application.baiduLocationService.start();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBadge(BadgeEvent event){
        circleNews.setBackgroundResource( event.isShowNew() ?R.drawable.circle_red:R.drawable.circle_white);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBadge(BadgeNewEvent event){
//        int pushCount = BaseApplication.readMessagePushCount();
//        int pullCount = BaseApplication.readMessagePullCount();
//        int commentCount =BaseApplication.readCommentCount();
//        boolean showRed  = (pushCount+pullCount+commentCount)>0;
        boolean showRed = BaseApplication.readNewsMessage();
        circleNews.setBackgroundResource( showRed ?R.drawable.circle_red:R.drawable.circle_white);
        //circleNews.setBackgroundResource( event.isShowNew() ?R.drawable.circle_red:R.drawable.circle_white);
    }

    @Override
    public void onClick(View view) {
        if( ((TextView)view).getText().equals( "新增客户资料") ){
            ActivityUtils.getInstance().showActivity(this,SubmitCustomerInfoActivity.class);
        }else{
            ActivityUtils.getInstance().showActivity(this,SubmitCustomerPictureActivity.class);
        }
    }
}
