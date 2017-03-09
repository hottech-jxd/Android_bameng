package com.bameng.ui.business;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.BuildConfig;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.config.Constants;
import com.bameng.model.CloseEvent;
import com.bameng.model.GetRewardOutput;
import com.bameng.model.PostModel;
import com.bameng.model.RefreshCustomerEvent;
import com.bameng.model.UserData;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.base.PhoteActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.ui.news.AddnewsActivity;
import com.bameng.ui.news.ChooseObjectActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.DensityUtils;
import com.bameng.utils.ToastUtils;
import com.bameng.utils.Util;
import com.bameng.widgets.PhotoSelectView;
import com.bameng.widgets.custom.FrescoControllerListener;
import com.bameng.widgets.custom.FrescoDraweeController;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.libedittext.EditText;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.bitmap;

/***
 * 新增客户图片
 */
public class SubmitCustomerPictureActivity extends PhoteActivity
        implements PhotoSelectView.OnPhotoSelectBackListener,FrescoControllerListener.ImageCallback{
    public final  static int REQUEST_CODE_CHOOSE= 100;

    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;

    @BindView(R2.id.remark)
    EditText remark;

    //@BindView(R.id.layAddImage)
    //LinearLayout layAddImage;

    //@BindView(R.id.layImage)
    //LinearLayout layImage;

    @BindView(R.id.ivPicture)
    SimpleDraweeView ivPicture;
    //@BindView(R.id.ivAddPic)
    //ImageView ivAddPic;

    //@BindView(R.id.laySendObject)
    //LinearLayout laySendObject;
    @BindView(R.id.laySendShop)
    LinearLayout laySendShop;
    @BindView(R.id.ivSelect)
    ImageView ivSelect;

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

    @BindView(R.id.tvPersons)
    TextView tvPersons;
    @BindView(R.id.laySend)
    LinearLayout laySend;

    boolean hasImage=false;
    String imagePath;
    private PhotoSelectView pop;
    boolean sendShop= true;
    List<UserData> objects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_customer_picture);
        ButterKnife.bind(this);
        initView();
    }

    protected void initView() {
        titleText.setText("提交客户信息");
        titleLeftImage.setVisibility(View.VISIBLE);
        //Drawable leftDraw = ContextCompat.getDrawable(this, R.mipmap.ic_back);
        //SystemTools.loadBackground(titleLeftImage, leftDraw);
        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);

        //layAddImage.setVisibility(View.VISIBLE);
        //layImage.setVisibility(View.GONE);

        ivPicture.setImageURI( Uri.parse("res:///"+ R.mipmap.ic_defaultpic));

        if( BaseApplication.UserData().getUserIdentity() == Constants.MENG_ZHU){
            layDesc.setVisibility(View.GONE);
            laySend.setVisibility(View.VISIBLE);
        }else{
            layDesc.setVisibility(View.VISIBLE);
            laySend.setVisibility(View.GONE);
            StartApi();
        }
    }

    @OnClick(R.id.ivAddPic)
    void onClick(View v ){
        if(null == pop) pop = new PhotoSelectView(this, this);
        pop.show();
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

    @OnClick(R.id.btn_submit)
    void submit(){

        boolean isok = true;

        if(!hasImage){
            ToastUtils.showLongToast("请上传图片");
            return;
        }
        if(tvPersons.getText().toString().isEmpty() && !sendShop ){
            ToastUtils.showLongToast("请选择分享盟友或者发送门店");
            return;
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

        String timestamp = String.valueOf(System.currentTimeMillis());
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", timestamp);
        map.put("os", "android");

        map.put("remark",remark.getText().toString().trim());
        map.put("issave", sendShop?"1":"0");
        map.put("ids", ids);
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        RequestBody requestBody = RequestBody.create( MediaType.parse("text/plain") , timestamp );
        requestBodyMap.put("timestamp",requestBody);
        requestBody=RequestBody.create(MediaType.parse("text/plain"), BaseApplication.getAppVersion());
        requestBodyMap.put("version",requestBody);
        requestBody=RequestBody.create(MediaType.parse("text/plain"), "android");
        requestBodyMap.put("os",requestBody);
        requestBody=RequestBody.create(MediaType.parse("text/plain"), remark.getText().toString().trim());
        requestBodyMap.put("remark",requestBody);
        requestBody=RequestBody.create(MediaType.parse("text/plain"), sendShop?"1":"0");
        requestBodyMap.put("issave",requestBody);
        requestBody=RequestBody.create(MediaType.parse("text/plain"), ids);
        requestBodyMap.put("ids",requestBody);
        requestBody=RequestBody.create(MediaType.parse("text/plain"), sign);
        requestBodyMap.put("sign",requestBody);

        requestBody = RequestBody.create(MediaType.parse("image/*"), Util.File2byte(  imagePath ));
        requestBodyMap.put("image\"; filename=\"" + timestamp + "\"", requestBody);

        Call<PostModel> call = apiService.addImgInfo(token,requestBodyMap);
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
                    ActivityUtils.getInstance().skipActivity(SubmitCustomerPictureActivity.this , PhoneLoginActivity.class);
                    return;
                }

                if (response.body() != null) {
                    if (response.body().getStatus() == 200 ) {
                        EventBus.getDefault().post(new RefreshCustomerEvent(null,""));
                        SubmitCustomerPictureActivity.this.setResult(RESULT_OK);
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
                    ActivityUtils.getInstance().skipActivity(SubmitCustomerPictureActivity.this, PhoneLoginActivity.class);
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
                        tvDes4.setText( "4.额外奖励:"+response.body().getData().getExtraReward() );
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
    public void onPhotoSelectBack(PhotoSelectView.SelectType type) {
        if(null == type) return;

        switch (type) {
            case Camera:
                selectPhotoByCamera();
                break;
            case File:
                selectPhotoByFile();
                break;
            default:
                break;
        }
    }

    void selectPhotoByCamera(){
        File file = new File( this.getExternalCacheDir(), "/temp/"+ System.currentTimeMillis() + ".jpg" );
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();

        Uri imageUri;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(this , BuildConfig.APPLICATION_ID + ".fileprovider", file );
        }else {
            imageUri = Uri.fromFile(file);
        }

        selectByCamera(imageUri);
    }

    void selectPhotoByFile(){
        File file = new File( this.getExternalCacheDir(), "/temp/"+ System.currentTimeMillis() + ".jpg" );
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();

        Uri imageUri;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(this , BuildConfig.APPLICATION_ID + ".fileprovider", file );
        }else {
            imageUri = Uri.fromFile(file);
        }

        selectByFile(imageUri);
    }


    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);

        hasImage=true;
        showImg(result.getImages());
    }

    private void showImg( ArrayList<TImage> images) {
        imagePath = images.get(0).getPath();
        //Glide.with(this).load(new File(imagePath)).into(ivPicture);
        int wid = DensityUtils.dip2px(this, 80);
        FrescoDraweeController.loadImage( ivPicture , wid , "file://"+ imagePath , 0 , this );
    }


    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
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

    @Override
    public void imageCallback(int position, int width, int height) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
        ivPicture.setLayoutParams(layoutParams);
    }

//    @Override
//    protected void setCropWidthHeigth() {
//        int wid = DensityUtils.getScreenW(this);
//        int hei = DensityUtils.getScreenH(this);
//        int t = wid>= hei ? hei:wid;
//        this.cropWidth = this.cropHeight =t;
//    }
}
