package com.bameng.ui.business;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.config.Constants;
import com.bameng.model.ArticleListOutput;
import com.bameng.model.CloseEvent;
import com.bameng.model.GetRewardOutput;
import com.bameng.model.PostModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
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

/***
 * 奖励设置
 */
public class RwordActivity extends BaseActivity {

    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
    @BindView(R2.id.CustomReward)
    EditText CustomReward;
    @BindView(R2.id.orderReword)
    EditText orderReword;
    @BindView(R2.id.shopReword)
    EditText shopReword;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rword);
        ButterKnife.bind(this);
        initView();
        StartApi();
    }

    @Override
    protected void initView() {
        titleText.setText("盟友奖励设置");
        titleLeftImage.setVisibility(View.VISIBLE);
        //Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        //SystemTools.loadBackground(titleLeftImage, leftDraw);
        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);
    }


    @OnClick(R.id.btn_commit)
    void setallyaward(){
        boolean isok = true;
        if( CustomReward.getText().toString().isEmpty() ){
            CustomReward.setError("请输入盟豆");
            isok=false;
        }
        if( orderReword.getText().toString().isEmpty() ){
            orderReword.setError("请输入盟豆");
            isok=false;
        }
        if( shopReword.getText().toString().isEmpty() ){
            shopReword.setError("请输入盟豆");
            isok=false;
        }
        if(isok==false){
            return;
        }

        ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("creward",CustomReward.getText().toString());
        map.put("orderreward",orderReword.getText().toString());
        map.put("shopreward",shopReword.getText().toString());
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<PostModel> call = apiService.setallyRaward(token,map);
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if( response.code() != 200 ){
                    ToastUtils.showLongToast(response.message()==null? String.valueOf( response.code()) : response.message());
                    return;
                }
                if(response.body()==null){
                    ToastUtils.showLongToast("服务器开小差了");
                    return;
                }
                if (response.body().getStatus() == Constants.STATUS_70035) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity( RwordActivity.this , PhoneLoginActivity.class);
                    return;
                }


                if (response.body() != null) {
                    if (response.body().getStatus() == 200 ) {
                        ToastUtils.showLongToast(response.body().getStatusText());
                        RwordActivity.this.finish();
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
    protected void StartApi() {
        if(progressDialog==null){
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage("请稍等...");
        progressDialog.show();

        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<GetRewardOutput> call = apiService.GetAllyReward(token,map);
        call.enqueue(new Callback<GetRewardOutput>() {
            @Override
            public void onResponse(Call<GetRewardOutput> call, Response<GetRewardOutput> response) {
                if(progressDialog!=null) progressDialog.dismiss();

                if (response.code() != 200) {
                    ToastUtils.showLongToast(response.message());
                    return;
                }
                if (response.body() == null) {
                    ToastUtils.showLongToast("服务器开小差了");
                    return;
                }

                if (response.body().getStatus() == Constants.STATUS_70035) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(RwordActivity.this, PhoneLoginActivity.class);
                    return;
                }

                if(response.body().getStatus() !=200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }

                if (response.body().getStatus() == 200 && response.body().getData() != null) {
                    CustomReward.setText(String.valueOf(response.body().getData().getCustomerReward()));
                    orderReword.setText(String.valueOf(response.body().getData().getOrderReward()));
                    shopReword.setText(String.valueOf(response.body().getData().getShopReward()));
                }
            }


            @Override
            public void onFailure(Call<GetRewardOutput> call, Throwable t) {
                if(progressDialog!=null) progressDialog.dismiss();

                ToastUtils.showLongToast("失败");
            }
        });

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
