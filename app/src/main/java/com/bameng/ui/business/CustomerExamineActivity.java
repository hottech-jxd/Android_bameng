package com.bameng.ui.business;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.adapter.CustomDetailsAdapter;
import com.bameng.config.Constants;
import com.bameng.model.CloseEvent;
import com.bameng.model.CustomerModel;
import com.bameng.model.PostModel;
import com.bameng.model.RefreshCustomerEvent;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.UserInfoView;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.baidu.location.h.j.t;

public class CustomerExamineActivity extends BaseActivity implements UserInfoView.OnUserInfoBackListener  {

    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.name) TextView name;
    @Bind(R.id.moblie)
    TextView moblie;
    @Bind(R.id.address) TextView address;
    @Bind(R.id.status) TextView status;
    @Bind(R.id.remark) TextView remark;
    @Bind(R.id.layBtn) LinearLayout laybtn;
    @Bind(R.id.llShopStatus) LinearLayout layShopStatus;
    @Bind(R.id.inShopStatus) TextView shopStatus;
    @Bind(R.id.tvAgree) TextView agree;
    @Bind(R.id.tvReject) TextView reject;
    @Bind(R.id.btnSubmit) Button btnSubmit;

    UserInfoView popWind;
    Bundle bundle;
    CustomerModel customerModel;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_examine);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void initView() {
        titleText.setText("客户意向审核");
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);
        bundle = this.getIntent().getExtras();
        customerModel= (CustomerModel) bundle.getSerializable("customerinfo");
        name.setText(customerModel.getName());
        moblie.setText(customerModel.getMobile());
        address.setText(customerModel.getAddr());
        shopStatus.setText( customerModel.getInShop() ==1 ? "已进店":"未进店" );

        if (customerModel.getStatus()==0){
            laybtn.setVisibility(View.VISIBLE);
            layShopStatus.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.GONE);
            status.setText("审核中");
        }else if (customerModel.getStatus()==1){
            laybtn.setVisibility(View.GONE);
            layShopStatus.setVisibility(View.VISIBLE);


            if(customerModel.getInShop() == 1){
                btnSubmit.setVisibility(View.GONE);
                layShopStatus.setEnabled(false);
            }else{
                btnSubmit.setVisibility(View.VISIBLE);
            }


            status.setText("已同意");
        }else {
            laybtn.setVisibility(View.GONE);
            layShopStatus.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.GONE);
            status.setText("已拒绝");
        }
        remark.setText(TextUtils.isEmpty(customerModel.getRemark())? "无":customerModel.getRemark() );

        popWind = new UserInfoView(this);
        popWind.setOnUserInfoBackListener(this);
    }

    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @OnClick({R.id.llShopStatus, R.id.tvAgree , R.id.tvReject ,R.id.btnSubmit })
    public void OnClick(View view){
        if(view.getId() == R.id.llShopStatus){
            popWind.show(UserInfoView.Type.ShopStatus , shopStatus.getText().toString() );
        }else if(view.getId() == R.id.tvAgree){
            audit(customerModel , 1 );
        }else if(view.getId() == R.id.tvReject){
            audit(customerModel , 2 );
        }else if( view.getId() == R.id.btnSubmit){
            int status = shopStatus.getText().toString().equals("未进店")? 0:1;
            updateShop(customerModel, status);
        }
    }

    protected void updateShop(final CustomerModel customerModel , int status ){
        if( progressDialog == null){
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage("");
        progressDialog.show();

        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("cid", String.valueOf( customerModel.getID()  ) );
        map.put("status",  String.valueOf( status ));
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<PostModel> call = apiService.UpdateInShop(token, map);
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if(progressDialog !=null) progressDialog.dismiss();
                if( response.code() != 200){
                    ToastUtils.showLongToast( response.message() );
                    return;
                }
                if(response.body()==null){
                    ToastUtils.showLongToast("服务器开小差了");
                    return;
                }
                if (response.body().getStatus() == Constants.STATUS_70035) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(CustomerExamineActivity.this, PhoneLoginActivity.class);
                    return;
                }


                if( response.body() !=null && response.body().getStatus() ==200 ){
                    CustomerExamineActivity.this.finish();
                    EventBus.getDefault().post(new RefreshCustomerEvent( customerModel ,"DoneFrag"));
                }else{
                    ToastUtils.showLongToast( response.body() !=null ?  response.body().getStatusText() : "失败");
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                if(progressDialog !=null )progressDialog.dismiss();
                ToastUtils.showLongToast("请求失败");
            }
        });
    }

    protected void audit(final CustomerModel customerModel , int status ){
        if( progressDialog == null){
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage("");
        progressDialog.show();


        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("cid", String.valueOf( customerModel.getID()  ) );
        map.put("status",  String.valueOf( status ));
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<PostModel> call = apiService.audit(token, map);
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if(progressDialog !=null) progressDialog.dismiss();
                if( response.code() != 200){
                    ToastUtils.showLongToast( response.message() );
                    return;
                }
                if( response.body() !=null && response.body().getStatus() ==200 ){
                    CustomerExamineActivity.this.finish();
                    EventBus.getDefault().post(new RefreshCustomerEvent( customerModel ,"NoDoneFrag"));
                }else{
                    ToastUtils.showLongToast( response.body() !=null ?  response.body().getStatusText() : "失败");
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                if(progressDialog !=null )progressDialog.dismiss();
                ToastUtils.showLongToast("请求失败");
            }
        });
    }


    @Override
    public void onUserInfoBack(UserInfoView.Type type , String value ) {
        shopStatus.setText( value );
    }
}
