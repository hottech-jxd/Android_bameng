package com.bameng.ui.business;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.config.Constants;
import com.bameng.model.ArticleListOutput;
import com.bameng.model.CloseEvent;
import com.bameng.model.GetRewardOutput;
import com.bameng.model.OperateTypeEnum;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bameng.R.id.CustomReward;
import static com.bameng.R.id.orderReword;
import static com.bameng.R.id.shopReword;
import static com.bameng.R.id.tvDes1;
import static com.bameng.R.id.tvDes3;

/***
 * 新增客户界面
 */
public class SubmitCustomerInfoActivity extends BaseActivity {

    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    public Resources resources;
    @Bind(R.id.customName)
    EditText customName;
    @Bind(R.id.customMoblie)
    EditText customMoblie;
    @Bind(R.id.customAddress)
    EditText customAddress;
    @Bind(R.id.remark)
    EditText remark;
    @Bind(R.id.layDesc)
    LinearLayout layDesc;
    @Bind(R.id.tvDes1)
    TextView tvDes1;
    @Bind(R.id.tvDes2)
    TextView tvDes2;
    @Bind(R.id.tvDes3)
    TextView tvDes3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_customer_info);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void initView() {
        titleText.setText("提交客户信息");
        titleLeftImage.setVisibility(View.VISIBLE);
        Drawable leftDraw = ContextCompat.getDrawable(this, R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);

        if( BaseApplication.UserData().getUserIdentity() == Constants.MENG_ZHU){
            layDesc.setVisibility(View.GONE);
        }else{
            layDesc.setVisibility(View.VISIBLE);
            StartApi();
        }
    }

    @OnClick(R.id.btn_submit)
    void submit(){
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("username",customName.getText().toString());
        map.put("mobile",customMoblie.getText().toString());
        map.put("address",customAddress.getText().toString());
        map.put("remark",remark.getText().toString());
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<PostModel> call = apiService.create(token,map);
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if( response.code() !=200 ){
                    ToastUtils.showLongToast(response.message());
                    return;
                }
                if(response.body()==null){
                    ToastUtils.showLongToast("服务器开小差了");
                    return;
                }

                if (response.body().getStatus() == Constants.STATUS_70035) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(SubmitCustomerInfoActivity.this , PhoneLoginActivity.class);
                    return;
                }

                if (response.body() != null) {
                    if (response.body().getStatus() == 200 ) {
                        finish();
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
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<GetRewardOutput> call = apiService.GetAllyReward(token,map);
        call.enqueue(new Callback<GetRewardOutput>() {
            @Override
            public void onResponse(Call<GetRewardOutput> call, Response<GetRewardOutput> response) {
                if ( response.code() != 200) {
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
                    ActivityUtils.getInstance().skipActivity(SubmitCustomerInfoActivity.this, PhoneLoginActivity.class);
                    return;
                }

                if(response.body().getStatus() !=200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }

                if (response.body().getStatus() == 200 && response.body().getData() != null) {
                    tvDes1.setText( String.format( "1.每条成功提交的信息可获得%d盟豆奖励" , response.body().getData().getCustomerReward()));
                    tvDes2.setText(String.format( "2.每笔成交的订单可获得%d盟豆奖励", response.body().getData().getOrderReward()));
                    tvDes3.setText(String.format( "3.客户上门%d盟豆奖励", response.body().getData().getShopReward()));
                }
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
