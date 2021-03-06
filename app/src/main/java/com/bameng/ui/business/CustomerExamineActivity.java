package com.bameng.ui.business;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.bameng.R2;
import com.bameng.config.Constants;
import com.bameng.model.BaseModel;
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
import com.bameng.utils.DensityUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.TipAlertDialog;
import com.bameng.widgets.UserInfoView;
import com.bameng.widgets.custom.FrescoControllerListener;
import com.bameng.widgets.custom.FrescoDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.baidu.location.d.j.G;
import static com.baidu.location.d.j.V;

/***
 * 订单审核
 */
public class CustomerExamineActivity extends BaseActivity implements UserInfoView.OnUserInfoBackListener  , FrescoControllerListener.ImageCallback{

    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.name) TextView name;
    @BindView(R2.id.moblie)
    TextView moblie;
    @BindView(R2.id.address) TextView address;
    @BindView(R.id.layStatus) LinearLayout layStatus;
    @BindView(R2.id.status) TextView status;
    @BindView(R2.id.remark) TextView remark;
    @BindView(R2.id.layBtn) LinearLayout laybtn;
    @BindView(R2.id.llShopStatus) LinearLayout layShopStatus;
    @BindView(R2.id.inShopStatus) TextView shopStatus;
    @BindView(R2.id.tvAgree) TextView agree;
    @BindView(R2.id.tvReject) TextView reject;
    @BindView(R2.id.btnSubmit) Button btnSubmit;
    @BindView(R2.id.belongone) TextView belongone;
    @BindView(R.id.btnNewOrder) Button btnNewOrder;
    @BindView(R.id.btnCustomerRecord) Button btnCustomerRecord;
    @BindView(R.id.llOrderStatus) LinearLayout llOrderStatus;
    @BindView(R.id.orderStatus) TextView orderStatus;

    @BindView(R.id.layPicture) LinearLayout layPicture;
    @BindView(R.id.ivPicture)  SimpleDraweeView ivPicture;
    @BindView(R.id.laySecond) LinearLayout laySecond;

    UserInfoView popWind;
    Bundle bundle;
    CustomerModel customerModel;
    ProgressDialog progressDialog;
    TipAlertDialog tipAlertDialog;

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
        //Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        //SystemTools.loadBackground(titleLeftImage, leftDraw);
        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);

        bundle = this.getIntent().getExtras();
        customerModel= (CustomerModel) bundle.getSerializable("customerinfo");

        if(customerModel.getIsSave()==1){
            laySecond.setVisibility(View.GONE);
            layPicture.setVisibility(View.VISIBLE);
            //ivPicture.setImageURI(Uri.parse( customerModel.getDataImg() ) );
            int width = DensityUtils.getScreenW(this);
            int sw = DensityUtils.dip2px(this, 100);
            width = width- sw;
            FrescoDraweeController.loadImage(ivPicture , width, customerModel.getDataImg(),0, this );
        }else {
            laySecond.setVisibility(View.VISIBLE);
            layPicture.setVisibility(View.GONE);

            name.setText(customerModel.getName());
            moblie.setText(customerModel.getMobile());
            address.setText(customerModel.getAddr());
        }

        shopStatus.setText( customerModel.getInShop() ==1 ? "已进店":"未进店" );
        belongone.setText( customerModel.getBelongOneName() );

        if (customerModel.getStatus()==0){//未审核
            laybtn.setVisibility(View.VISIBLE);
            layStatus.setVisibility(View.VISIBLE);
            llOrderStatus.setVisibility(View.GONE);
            btnCustomerRecord.setVisibility(View.GONE);
            //layShopStatus.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.GONE);
            btnNewOrder.setVisibility(View.GONE);
            status.setText("未审核");
            orderStatus.setText("");
        }else if (customerModel.getStatus()==1){//已同意
            laybtn.setVisibility(View.GONE);
            //layShopStatus.setVisibility(View.VISIBLE);
            //if(customerModel.getInShop() == 1){
            //    btnSubmit.setVisibility(View.GONE);
             //   btnNewOrder.setVisibility(View.VISIBLE);
             //   layShopStatus.setEnabled(false);
            //}else{
                //layShopStatus.setEnabled(true);
             //   btnSubmit.setVisibility(View.GONE);
            //}

            layStatus.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.GONE);
            btnNewOrder.setVisibility(View.VISIBLE);
            llOrderStatus.setVisibility(View.VISIBLE);
            btnCustomerRecord.setVisibility(View.VISIBLE);

            status.setText("已同意");
            orderStatus.setText("");
        }else if(customerModel.getStatus()==2){//已拒绝
            laybtn.setVisibility(View.GONE);
            layStatus.setVisibility(View.VISIBLE);
            //layShopStatus.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.GONE);
            btnNewOrder.setVisibility(View.GONE);
            llOrderStatus.setVisibility(View.GONE);
            btnCustomerRecord.setVisibility(View.GONE);
            status.setText("已拒绝");
            orderStatus.setText("");
        }else if(customerModel.getStatus() == 3) {//未生成订单
            laybtn.setVisibility(View.GONE);
            layStatus.setVisibility(View.GONE);
            llOrderStatus.setVisibility(View.VISIBLE);
            btnNewOrder.setVisibility(View.VISIBLE);
            btnCustomerRecord.setVisibility(View.VISIBLE);
            status.setText( getString(R.string.noOrder));
            orderStatus.setText( getString(R.string.noOrder ));
        }else if(customerModel.getStatus()==4){//已生成订单
            laybtn.setVisibility(View.GONE);
            layStatus.setVisibility(View.VISIBLE);
            llOrderStatus.setVisibility(View.GONE);
            btnNewOrder.setVisibility(View.GONE);
            btnCustomerRecord.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.GONE);
            status.setText("已生成订单");
            orderStatus.setText("");
        } else if(customerModel.getStatus()==5){//已失效
            laybtn.setVisibility(View.GONE);
            layStatus.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.GONE);
            btnNewOrder.setVisibility(View.GONE);
            llOrderStatus.setVisibility(View.VISIBLE);
            btnCustomerRecord.setVisibility(View.VISIBLE);
            status.setText( getString( R.string.orderInvalid));
            orderStatus.setText( getString(R.string.orderInvalid) );
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

    @OnClick({R.id.llShopStatus, R.id.tvAgree , R.id.tvReject ,R.id.btnSubmit,R.id.btnNewOrder , R.id.llOrderStatus,R.id.btnCustomerRecord})
    public void OnClick(View view){
        if(view.getId()== R.id.llOrderStatus){
            popWind.show(UserInfoView.Type.CustomerOrderStatus , orderStatus.getText().toString());
        }else if(view.getId() == R.id.llShopStatus){
            popWind.show(UserInfoView.Type.ShopStatus , shopStatus.getText().toString() );
        }else if(view.getId() == R.id.tvAgree){
            //audit(customerModel , 1 );
            auditTip("确定要同意申请吗？",customerModel , 1);
        }else if(view.getId() == R.id.tvReject){
            auditTip( "确定要拒绝申请吗？", customerModel , 2 );
        }else if( view.getId() == R.id.btnSubmit){
            //int status = shopStatus.getText().toString().equals("未进店")? 0:1;
            //updateShop(customerModel, status);
            updateStatus(customerModel);
        }else if(view.getId() == R.id.btnNewOrder){
            Intent intent=new Intent(CustomerExamineActivity.this, NewOrderActivity.class);
            intent.putExtra("customer", customerModel );
            ActivityUtils.getInstance().skipActivity(this, intent);
        }else if(view.getId()==R.id.btnCustomerRecord){
            Intent intent = new Intent( CustomerExamineActivity.this , CustomerRecordActivity.class);
            intent.putExtra("customer", customerModel);
            ActivityUtils.getInstance().showActivity(this, intent);
        }
    }

    protected void updateStatus(final CustomerModel customerModel  ){
        if( progressDialog == null){
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage("");
        progressDialog.show();

        int status = orderStatus.getText().toString().equals( getString(R.string.orderInvalid) ) ? 1:2;

        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("cid", String.valueOf( customerModel.getID()  ) );
        map.put("status",  String.valueOf( status ));
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<BaseModel> call = apiService.updateStatus(token, map);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
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
                    CustomerExamineActivity.this.setResult(RESULT_OK);
                    CustomerExamineActivity.this.finish();
                    EventBus.getDefault().post(new RefreshCustomerEvent( customerModel ,"DoneFrag"));
                }else{
                    ToastUtils.showLongToast( response.body() !=null ?  response.body().getStatusText() : "失败");
                }

            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                if(progressDialog !=null )progressDialog.dismiss();
                ToastUtils.showLongToast("请求失败");
            }
        });
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
        String sign = AuthParamUtils.getSign(map);
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
                    CustomerExamineActivity.this.setResult(RESULT_OK);
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

    void auditTip( String msg , final CustomerModel customerModel , final int status){
        if(tipAlertDialog==null) tipAlertDialog = new TipAlertDialog(this,false);
        tipAlertDialog.show("审核提醒", msg , null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( tipAlertDialog!=null) tipAlertDialog.dismiss();
                audit( customerModel , status );
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
        String sign = AuthParamUtils.getSign(map);
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
                    CustomerExamineActivity.this.setResult(RESULT_OK);
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
        if( type == UserInfoView.Type.ShopStatus ) {
            shopStatus.setText(value);
            if (value.equals("未进店")) {
                btnSubmit.setVisibility(View.GONE);
            } else {
                btnSubmit.setVisibility(View.VISIBLE);
            }
        }else if( type== UserInfoView.Type.CustomerOrderStatus){
            if( customerModel !=null && customerModel.getStatusName().equals( value )   ){
                btnSubmit.setVisibility(View.GONE);
            }else{
                btnSubmit.setVisibility(View.VISIBLE);
            }
            orderStatus.setText( value );

            if( value.equals(getString(R.string.orderInvalid)) ){
                btnNewOrder.setVisibility(View.GONE);
            }else{
                btnNewOrder.setVisibility(View.VISIBLE);
            }

        }

    }

    @Override
    public void imageCallback(int position, int width, int height) {
        if( ivPicture==null) return;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
        ivPicture.setLayoutParams(layoutParams);
    }
}
