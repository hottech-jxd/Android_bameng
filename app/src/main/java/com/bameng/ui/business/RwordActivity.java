package com.bameng.ui.business;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.model.ArticleListOutput;
import com.bameng.model.GetRewardOutput;
import com.bameng.model.PostModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.huotu.android.library.libedittext.EditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RwordActivity extends BaseActivity {

    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    public Resources resources;
    @Bind(R.id.CustomReward)
    EditText CustomReward;
    @Bind(R.id.orderReword)
    EditText orderReword;
    @Bind(R.id.shopReword)
    EditText shopReword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rword);
        ButterKnife.bind(this);
        initView();
        application = (BaseApplication) this.getApplication();
        resources = this.getResources();
        StartApi();
    }

    @Override
    protected void initView() {
        titleText.setText("盟友奖励设置");
        titleLeftImage.setVisibility(View.VISIBLE);
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);
    }


    @OnClick(R.id.btn_commit)
    void setallyaward(){
        Map<String, String> map = new HashMap<>();
        map.put("version", application.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("creward",CustomReward.getText().toString());
        map.put("orderreward",orderReword.getText().toString());
        map.put("shopreward",shopReword.getText().toString());
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        String token = application.readToken();
        Call<PostModel> call = apiService.setallyRaward(token,map);
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if (response.body() != null) {


                    if (response.body().getStatus() == 200&&response.body()!=null) {

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
    protected void StartApi() {
        Map<String, String> map = new HashMap<>();
        map.put("version", application.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");

        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        String token = application.readToken();
        Call<GetRewardOutput> call = apiService.GetAllyReward(token,map);
        call.enqueue(new Callback<GetRewardOutput>() {
            @Override
            public void onResponse(Call<GetRewardOutput> call, Response<GetRewardOutput> response) {
                if (response.body() != null) {


                    if (response.body().getStatus() == 200&&response.body()!=null) {
                        CustomReward.setText(String.valueOf(response.body().getData().getCustomerReward()));
                        orderReword.setText(String.valueOf(response.body().getData().getOrderReward()));
                        shopReword.setText(String.valueOf(response.body().getData().getShopReward()));
                    } else {
                        ToastUtils.showLongToast(response.body().getStatusText());
                    }

                }

                return;



            }


            @Override
            public void onFailure(Call<GetRewardOutput> call, Throwable t) {
                ToastUtils.showLongToast("失败");
            }
        });

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
