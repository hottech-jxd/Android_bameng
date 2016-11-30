package com.bameng.ui.business;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.config.Constants;
import com.bameng.model.CloseEvent;
import com.bameng.model.OrderDetailOutputModel;
import com.bameng.model.OrderModel;
import com.bameng.model.OrderOutputModel;
import com.bameng.model.PostModel;
import com.bameng.model.UserData;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.DateUtils;
import com.bameng.utils.DensityUtils;
import com.bameng.utils.FileUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.bameng.utils.Util;
import com.bameng.widgets.UserInfoView;
import com.bameng.widgets.custom.FrescoControllerListener;
import com.bameng.widgets.custom.FrescoDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.bitmap;
import static android.R.attr.cursorVisible;
import static android.R.attr.mode;
import static android.R.attr.order;
import static android.R.attr.width;
import static com.baidu.location.h.j.B;
import static com.baidu.location.h.j.ap;
import static com.baidu.location.h.j.t;
import static com.bameng.R.id.btnSave;
import static com.bameng.R.id.orderReword;
import static com.bameng.R.id.status;
import static com.bameng.R.id.tvname;
import static com.bameng.service.LocationService.address;

public class OrderDetailsActivity
        extends BaseActivity implements UserInfoView.OnUserInfoBackListener, FrescoControllerListener.ImageCallback {

    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    @Bind(R.id.tvOrderNo)
    TextView tvOrderNo;
    @Bind(R.id.tvOrderTime)
    TextView tvOrderTime;
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.tvPhone)
    TextView tvPhone;
    @Bind(R.id.tvAddress)
    TextView tvAddress;
    @Bind(R.id.tvRemarks)
    TextView tvRemark;
    @Bind(R.id.tvOrderStatus)
    TextView tvOrderStatus;
    @Bind(R.id.ivPicture)
    SimpleDraweeView ivPicture;
    @Bind(R.id.btnSave)
    Button btnSave;
    @Bind(R.id.btnUpload)
    Button btnUpload;
    Resources resources;
    ProgressDialog progressDialog;
    UserInfoView userInfoView;

    String orderId;
    OrderModel orderModel;
    Bitmap bitmap;

    final  int REQUEST_CODE_UPLOAD=100;
    String bitmapPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);
        initView();
        orderId = getIntent().getStringExtra("orderid");
        StartApi();
    }

    @Override
    protected void initView() {
        titleText.setText("订单详情");
        titleLeftImage.setVisibility(View.VISIBLE);
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);
    }

    @Override
    protected void StartApi() {
        if(progressDialog==null){
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage("正在拉取订单详情");
        progressDialog.show();

        String timestamp = String.valueOf(System.currentTimeMillis());
        Map<String,String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", timestamp );
        map.put("os", "android");
        map.put("id", orderId);

        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign",sign);

        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<OrderDetailOutputModel> call = apiService.orderdetails(token , map);
        call.enqueue(new Callback<OrderDetailOutputModel>() {
            @Override
            public void onResponse(Call<OrderDetailOutputModel> call, Response<OrderDetailOutputModel> response) {
                if(progressDialog!=null)progressDialog.dismiss();
                if(response.code() !=200 || response.body()==null ){
                    ToastUtils.showLongToast(response.message()==null?"error":response.message());
                    return;
                }
                if (response.body().getStatus() == Constants.STATUS_70035) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(OrderDetailsActivity.this, PhoneLoginActivity.class);
                    return;
                }

                if(response.body().getStatus()!=200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }
                if( response.body().getData() ==null){
                    ToastUtils.showLongToast("返回的数据为空");
                    return;
                }

                orderModel = response.body().getData();
                tvOrderNo.setText( orderModel.getOrderId() );
                tvOrderTime.setText(DateUtils.formatDate( orderModel.getOrderTime() ));
                tvAddress.setText( orderModel.getAddress());
                tvName.setText(orderModel.getUserName());
                tvPhone.setText(orderModel.getMobile());
                tvRemark.setText(orderModel.getRemark());
                tvOrderStatus.setText( orderModel.getStatus() == Constants.ORDER_DEAL ? getString(R.string.deal) : orderModel.getStatus() ==Constants.ORDER_BACK ? getString(R.string.backorder ) : getString(R.string.noDeal));

                tvOrderStatus.setEnabled( orderModel.getStatus() == Constants.ORDER_NODEAL ? true : false );

                btnSave.setVisibility(View.GONE);
                btnUpload.setVisibility(View.GONE);
                //btnUpload.setVisibility( orderModel.getStatus() == Constants.ORDER_DEAL || orderModel.getStatus() == Constants.ORDER_BACK ? View.GONE:View.VISIBLE );
                int wpx = DensityUtils.getScreenW(OrderDetailsActivity.this);

                if( orderModel.getStatus() == Constants.ORDER_DEAL){
                    FrescoDraweeController.loadImage( ivPicture , wpx , orderModel.getSuccessUrl() , 0 , OrderDetailsActivity.this );
                }else {
                    FrescoDraweeController.loadImage(ivPicture, wpx, orderModel.getPictureUrl(), 0 , OrderDetailsActivity.this);
                }

                //btnSave.setVisibility( orderModel.getStatus()== Constants.ORDER_NODEAL ? View.VISIBLE :View.GONE  );
            }

            @Override
            public void onFailure(Call<OrderDetailOutputModel> call, Throwable t) {
                if(progressDialog!=null)progressDialog.dismiss();
                Snackbar.make(getWindow().getDecorView(), t.getMessage()==null?"error":t.getMessage(),Snackbar.LENGTH_LONG);
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @OnClick({R.id.tvOrderStatus,R.id.btnUpload,R.id.btnSave})
    void onClick(View view ){
        if( view.getId() == R.id.tvOrderStatus){
            setOrderStatus();
        }else if(view.getId() == R.id.btnUpload){
            upload();
        }else if(view.getId() == R.id.btnSave){

            //save();
            String statusstr = tvOrderStatus.getText().toString();
            if( statusstr.equals( getString(R.string.backorder)) ){
                save();
            }else {
                if( bitmapPath==null || bitmapPath.isEmpty() ) {
                    ToastUtils.showLongToast("请上传成交凭证");
                    return;
                }
                uploadData();
            }
        }
    }

    void save(){

        String statusstr = tvOrderStatus.getText().toString();
        int status=0;
        if(statusstr.equals(getString(R.string.deal))){
            status = 1;
        }else if(statusstr.equals(getString(R.string.noDeal))){
            status = 0;
        }else if(statusstr.equals(getString(R.string.backorder))){
            status=2;
        }


        if(progressDialog==null){
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage("正在上传订单数据");
        progressDialog.show();

        String timestamp = String.valueOf(System.currentTimeMillis());
        Map<String,String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", timestamp );
        map.put("os", "android");
        map.put("orderId", orderId);
        map.put("status", String.valueOf(status));

        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign",sign);
        String token = BaseApplication.readToken();
        ApiService apiService = ZRetrofitUtil.getApiService();
        Call<PostModel> call = apiService.orderUpdate( token , map );
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }

                if(response.code() !=200){
                    ToastUtils.showLongToast(response.message());
                    return;
                }
                if(response.body()==null){
                    ToastUtils.showLongToast("返回数据空");
                    return;
                }
                if (response.body().getStatus() == Constants.STATUS_70035) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(OrderDetailsActivity.this, PhoneLoginActivity.class);
                    return;
                }

                ToastUtils.showLongToast(response.body().getStatusText());
                if(response.body().getStatus() == 200) {
                    OrderDetailsActivity.this.finish();
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                ToastUtils.showLongToast("请求失败"+ t.getMessage()==null?"":t.getMessage());
            }
        });

    }

    void uploadData(){

        if(progressDialog==null){
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage("正在上传成交凭证中...");
        progressDialog.show();

        String customer = orderModel.getUserName();
        String phone = orderModel.getMobile();
        String price = orderModel.getMoney();
        String remarks = orderModel.getRemark();

        String timestamp = String.valueOf(System.currentTimeMillis());
        Map<String,String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", timestamp );
        map.put("os", "android");
        map.put("orderId", orderModel.getOrderId());
        map.put("customer",customer);
        map.put("mobile",phone );
        map.put("price",price);
        map.put("memo",remarks);

        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        RequestBody requestBody = RequestBody.create( MediaType.parse("text/plain") , timestamp );
        requestBodyMap.put("timestamp",requestBody);
        requestBody=RequestBody.create(MediaType.parse("text/plain"), BaseApplication.getAppVersion());
        requestBodyMap.put("version",requestBody);
        requestBody=RequestBody.create(MediaType.parse("text/plain"), "android");
        requestBodyMap.put("os",requestBody);

        requestBody=RequestBody.create(MediaType.parse("text/plain"), orderModel.getOrderId() );
        requestBodyMap.put("orderId",requestBody);

        requestBody=RequestBody.create(MediaType.parse("text/plain"), customer );
        requestBodyMap.put("customer",requestBody);

        requestBody=RequestBody.create(MediaType.parse("text/plain"), phone );
        requestBodyMap.put("mobile",requestBody);

        requestBody=RequestBody.create(MediaType.parse("text/plain"), price );
        requestBodyMap.put("price",requestBody);

        requestBody=RequestBody.create(MediaType.parse("text/plain"), remarks );
        requestBodyMap.put("memo",requestBody);

        requestBody=RequestBody.create(MediaType.parse("text/plain"), sign);
        requestBodyMap.put("sign",requestBody);


        byte[] buffer = Util.File2byte( bitmapPath );

        requestBody = RequestBody.create(MediaType.parse("image/*"), buffer );
        requestBodyMap.put("image\"; filename=\"" + timestamp + "\"", requestBody);


        String token = BaseApplication.readToken();
        ApiService apiService = ZRetrofitUtil.getApiService();
        Call<PostModel> call = apiService.UploadSuccessVoucher( token , requestBodyMap );
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if(progressDialog!=null)progressDialog.dismiss();
                if(response.code() !=200){
                    ToastUtils.showLongToast(response.message());
                    return;
                }
                if(response.body()==null){
                    ToastUtils.showLongToast("返回数据空");
                    return;
                }
                if (response.body().getStatus() == Constants.STATUS_70035) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(OrderDetailsActivity.this, PhoneLoginActivity.class);
                    return;
                }

                if(response.body().getStatus()!=200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }
                save();
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                if(progressDialog!=null)progressDialog.dismiss();
                ToastUtils.showLongToast("请求失败"+ t.getMessage()==null?"":t.getMessage());
            }
        });

    }

    void upload(){
        Intent intent = new Intent(this,UploadDocumentsActivity.class);
        Bundle bd = new Bundle();
        bd.putSerializable("order",(Serializable) orderModel);
        intent.putExtras(bd);
        intent.putExtra("bitmapPath",bitmapPath);
        ActivityUtils.getInstance().showActivityForResult(this , REQUEST_CODE_UPLOAD , intent );
    }

    void setOrderStatus(){
        if(userInfoView==null){
            userInfoView = new UserInfoView(this);
            userInfoView.setOnUserInfoBackListener(this);
        }
        userInfoView.show(UserInfoView.Type.OrderStatus , tvOrderStatus.getText().toString().trim() );
    }

    @Override
    public void onUserInfoBack(UserInfoView.Type type, String value) {
        if(type==null) return;

        tvOrderStatus.setText( value );

        btnSave.setVisibility( value.equals( getString(R.string.noDeal) ) ? View.GONE : View.VISIBLE );

        btnUpload.setVisibility( value.equals( getString(R.string.deal) ) ? View.VISIBLE: View.GONE );

    }

    @Override
    public void imageCallback(int position, int width, int height) {

        if( ivPicture==null) return;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
        ivPicture.setLayoutParams(layoutParams);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK)return;

        if(requestCode == REQUEST_CODE_UPLOAD){
            OrderModel temp = (OrderModel) data.getExtras().getSerializable("order");
            orderModel.setUserName(temp.getUserName());
            tvName.setText( temp.getUserName() );
            orderModel.setMobile(temp.getMobile());
            tvPhone.setText( temp.getMobile() );
            orderModel.setRemark(temp.getRemark());
            tvRemark.setText( temp.getRemark() );
            orderModel.setMoney(temp.getMoney());

            //bitmap = data.getParcelableExtra("bitmap");

            bitmapPath = data.getStringExtra("bitmapPath");

            //bitmap = Util.readBitmapByPath(bitmapPath);

//            ivPicture.setImageBitmap(bitmap);
            ivPicture.setImageURI("file://"+ bitmapPath );

        }
    }
}
