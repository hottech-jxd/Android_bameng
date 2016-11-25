package com.bameng.ui;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.config.Constants;
import com.bameng.fragment.FragManager;
import com.bameng.model.CloseEvent;
import com.bameng.model.InitOutputsModel;
import com.bameng.model.PostModel;
import com.bameng.receiver.MyBroadcastReceiver;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.business.SubmitCustomerInfoActivity;
import com.bameng.ui.news.AddnewsActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.ProgressPopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 盟友主页
 */
public class AllyHomeActivity extends BaseActivity {

    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;

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

    @Bind(R.id.richesPage)
    RelativeLayout richesPage;

    @Bind(R.id.richesImg)
    ImageView richesImg;

    @Bind(R.id.richesTxt)
    TextView richesTxt;

    @Bind(R.id.newsPage)
    RelativeLayout newsPage;

    @Bind(R.id.newsImg)
    ImageView newsImg;

    @Bind(R.id.newsTxt)
    TextView newsTxt;
    public Resources resources;
    public ProgressPopupWindow progress;

    String currentTab="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ally_home);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);

        application.mFragManager = FragManager.getIns(this, R.id.fragment_container);
        resources = this.getResources();
        initView();
        StartApi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FragManager.clear();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {
        goback=false;
        titleText.setText("业务客户");
        titleLeftImage.setVisibility(View.VISIBLE);
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_location);
        SystemTools.loadBackground(titleLeftImage, leftDraw);
        Drawable rightDraw = ContextCompat.getDrawable(this , R.mipmap.ic_newadd);
        SystemTools.loadBackground(titleRightImage,rightDraw);
        application.mFragManager.setCurrentFrag(FragManager.FragType.ALLYHOME);
        initTab();
    }

    private void initTab() {
        currentTab="业务客户";
        Drawable oneBuyDraw = ContextCompat.getDrawable(this, R.mipmap.ic_on_homepage);
        SystemTools.loadBackground(homeImg, oneBuyDraw);
        homeTxt.setTextColor(resources.getColor(R.color.chocolate));
        //重置其他
        Drawable newestDraw = ContextCompat.getDrawable(this,R.mipmap.ic_zx);
        SystemTools.loadBackground(newsImg, newestDraw);
        newsTxt.setTextColor(resources.getColor(R.color.text_color_black));
        Drawable listDraw = ContextCompat.getDrawable(this,R.mipmap.ic_riches);
        SystemTools.loadBackground(richesImg, listDraw);
        richesTxt.setTextColor(resources.getColor(R.color.text_color_black));
    }

    @OnClick(R.id.titleRightImage)
    void onRightClick(){
        if( currentTab.equals("业务客户")) {
            ActivityUtils.getInstance().showActivity(AllyHomeActivity.this, SubmitCustomerInfoActivity.class);
        }else if(currentTab.equals("资讯列表")){
            ActivityUtils.getInstance().showActivity(AllyHomeActivity.this, AddnewsActivity.class);
        }
    }
    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.homePage: {
                currentTab="业务客户";
                titleText.setText("业务客户");
                titleLeftImage.setVisibility(View.VISIBLE);
                titleRightImage.setVisibility(View.VISIBLE);
                if (progress != null) progress.dismissView();

                //设置选中状态
                Drawable oneBuyDraw = ContextCompat.getDrawable(this, R.mipmap.ic_on_homepage);
                SystemTools.loadBackground(homeImg, oneBuyDraw);
                homeTxt.setTextColor(resources.getColor(R.color.chocolate));
                //重置其他
                Drawable newestDraw = ContextCompat.getDrawable(this, R.mipmap.ic_zx);
                SystemTools.loadBackground(newsImg, newestDraw);
                newsTxt.setTextColor(resources.getColor(R.color.text_color_black));
                Drawable profileDraw = ContextCompat.getDrawable(this, R.mipmap.ic_riches);
                SystemTools.loadBackground(richesImg, profileDraw);
                richesTxt.setTextColor(resources.getColor(R.color.text_color_black));
                application.mFragManager.setCurrentFrag(FragManager.FragType.ALLYHOME);
            }
            break;
            case R.id.newsPage: {
                currentTab="资讯列表";
                titleText.setText("资讯列表");
                titleLeftImage.setVisibility(View.GONE);
                titleRightImage.setVisibility(View.VISIBLE);
                if (progress != null) progress.dismissView();
                //设置选中状态
                Drawable oneBuyDraw = ContextCompat.getDrawable(this, R.mipmap.ic_homepage);
                SystemTools.loadBackground(homeImg, oneBuyDraw);
                homeTxt.setTextColor(resources.getColor(R.color.text_color_black));
                //重置其他
                Drawable newestDraw = ContextCompat.getDrawable(this, R.mipmap.ic_on_zx);
                SystemTools.loadBackground(newsImg, newestDraw);
                newsTxt.setTextColor(resources.getColor(R.color.chocolate));
                Drawable listDraw = ContextCompat.getDrawable(this, R.mipmap.ic_riches);
                SystemTools.loadBackground(richesImg, listDraw);
                richesTxt.setTextColor(resources.getColor(R.color.text_color_black));
                application.mFragManager.setCurrentFrag(FragManager.FragType.NEWS);


            }
            break;
            case R.id.richesPage: {
                currentTab="财富";
                titleText.setText("财富");
                titleLeftImage.setVisibility(View.GONE);
                titleRightImage.setVisibility(View.GONE);
                if (progress != null) progress.dismissView();
                //设置选中状态
                Drawable oneBuyDraw = ContextCompat.getDrawable(this, R.mipmap.ic_homepage);
                SystemTools.loadBackground(homeImg, oneBuyDraw);
                homeTxt.setTextColor(resources.getColor(R.color.text_color_black));
                //重置其他
                Drawable newestDraw = ContextCompat.getDrawable(this, R.mipmap.ic_zx);
                SystemTools.loadBackground(newsImg, newestDraw);
                newsTxt.setTextColor(resources.getColor(R.color.text_color_black));
                Drawable listDraw = ContextCompat.getDrawable(this, R.mipmap.ic_on_riches);
                SystemTools.loadBackground(richesImg, listDraw);
                richesTxt.setTextColor(resources.getColor(R.color.chocolate));
                application.mFragManager.setCurrentFrag(FragManager.FragType.PROFILE );

            }
            break;
        }
    }
    @Override
    protected void StartApi() {
//        Map<String, String> map = new HashMap<>();
//        map.put("version", application.getAppVersion());
//        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
//        map.put("os", "android");
//        AuthParamUtils authParamUtils = new AuthParamUtils();
//        String sign = authParamUtils.getSign(map);
//        map.put("sign", sign);
//        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
//        Call<InitOutputsModel> call = apiService.init(map);
//        call.enqueue(new Callback<InitOutputsModel>() {
//            @Override
//            public void onResponse(Call<InitOutputsModel> call, Response<InitOutputsModel> response) {
//                if (response.body() == null) {
//                    ToastUtils.showLongToast(response.code() + ":" + response.message());
//                    return;
//                }
//
//                if (response.body().getStatus() == 200) {
//                    ToastUtils.showLongToast("成功");
//                } else {
//                    ToastUtils.showLongToast(response.body().getStatusText());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PostModel> call, Throwable t) {
//                ToastUtils.showLongToast("失败");
//            }
//        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventClose(CloseEvent event){
        finish();
    }

}
