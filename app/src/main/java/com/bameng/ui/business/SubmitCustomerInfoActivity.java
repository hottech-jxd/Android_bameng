package com.bameng.ui.business;

import android.content.Intent;
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
import com.bameng.R2;
import com.bameng.config.Constants;
import com.bameng.model.ArticleListOutput;
import com.bameng.model.CloseEvent;
import com.bameng.model.CustomerModel;
import com.bameng.model.GetRewardOutput;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.PostModel;
import com.bameng.model.RefreshCustomerEvent;
import com.bameng.model.UserData;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.ui.news.ChooseObjectActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.huotu.android.library.libedittext.EditText;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bameng.R.id.CustomReward;
import static com.bameng.R.id.laySend;
import static com.bameng.R.id.orderReword;
import static com.bameng.R.id.shopReword;
import static com.bameng.R.id.tvDes1;
import static com.bameng.R.id.tvDes3;

/***
 * 新增客户界面
 */
public class SubmitCustomerInfoActivity extends BaseActivity {
    final  static int REQUEST_CODE_CHOOSE= 100;
    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
    public Resources resources;
    @BindView(R2.id.customName)
    EditText customName;
    @BindView(R2.id.customMoblie)
    EditText customMoblie;
    @BindView(R2.id.customAddress)
    EditText customAddress;
    @BindView(R2.id.remark)
    EditText remark;
    @BindView(R2.id.layDesc)
    LinearLayout layDesc;
    @BindView(R2.id.tvDes1)
    TextView tvDes1;
    @BindView(R2.id.tvDes2)
    TextView tvDes2;
    @BindView(R2.id.tvDes3)
    TextView tvDes3;
    @BindView(R.id.tvDes4)
    TextView tvDes4;

    @BindView(R.id.laySendShop)
    LinearLayout laySendShop;
    @BindView(R.id.ivSelect)
    ImageView ivSelect;
    @BindView(R.id.tvPersons)
    TextView tvPersons;
    @BindView(R.id.laySend)
    LinearLayout laySend;

    boolean sendShop= true;
    List<UserData> objects;

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
        //Drawable leftDraw = ContextCompat.getDrawable(this, R.mipmap.ic_back);
        //SystemTools.loadBackground(titleLeftImage, leftDraw);
        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);

        if( BaseApplication.UserData().getUserIdentity() == Constants.MENG_ZHU){
            layDesc.setVisibility(View.GONE);
            laySend.setVisibility(View.VISIBLE);
        }else{
            layDesc.setVisibility(View.VISIBLE);
            laySend.setVisibility(View.GONE);
            StartApi();
        }
    }

    @OnClick(R.id.btn_submit)
    void submit(){

        String name = customName.getText().toString().trim();
        String mobile = customMoblie.getText().toString().trim();
        String address = customAddress.getText().toString().trim();
        boolean isok = true;
        if(address.isEmpty()){
            customAddress.setError("请填写客户地址");
            isok=false;
        }
        if( mobile.isEmpty()){
            customMoblie.setError("请填写客户电话");
            isok=false;
        }
        if( name.isEmpty()){
            customName.setError("请填写客户姓名");
            isok=false;
        }

        if(tvPersons.getText().toString().isEmpty() && !sendShop ){
            ToastUtils.showLongToast("请选择分享盟友或者发送门店");
            isok=false;
        }
        String ids = "";
        if( objects !=null) {
            for (UserData item : objects) {
                if (!ids.isEmpty()) {
                    ids += "|";
                }
                ids += item.getUserId();
            }
        }

        if(!isok){
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("username",customName.getText().toString());
        map.put("mobile",customMoblie.getText().toString());
        map.put("address",customAddress.getText().toString());
        map.put("remark",remark.getText().toString());
        map.put("issave", sendShop?"1":"0");
        map.put("ids", ids);
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<PostModel> call = apiService.addInfo(token , map); // apiService.create(token,map);
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
                        EventBus.getDefault().post(new RefreshCustomerEvent(null,""));
                        SubmitCustomerInfoActivity.this.setResult(RESULT_OK);
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
                    if( null != response.body().getData().getExtraReward() && !response.body().getData().getExtraReward().isEmpty() ){
                        tvDes4.setText("4.额外奖励:"+ response.body().getData().getExtraReward() );
                    }
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

    @OnClick(R.id.laySendShop)
    void onSendShop(View v){
        sendShop=  !sendShop;
        ivSelect.setBackgroundResource( sendShop ? R.mipmap.ic_choose : R.mipmap.ic_nochoose );
    }
    @OnClick(R.id.laySendObject)
    void onSendPersons(View v){
        Bundle bd = new Bundle();
        bd.putSerializable("customer", (Serializable) objects);
        ActivityUtils.getInstance().showActivityForResult(this, REQUEST_CODE_CHOOSE , ChooseObjectActivity.class , bd );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode != RESULT_OK )return;

        if( requestCode == REQUEST_CODE_CHOOSE){
            tvPersons.setText("");
            objects = (List<UserData>)data.getExtras().getSerializable("customer");
            if(objects==null) return;
            String txt = "";
            for(UserData item : objects){
                if( !txt.isEmpty()){
                    txt+=",";
                }
                txt += item.getRealName();
            }
            tvPersons.setText(txt);
        }

    }

}
