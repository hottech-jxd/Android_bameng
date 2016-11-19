package com.bameng.ui.business;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.config.Constants;
import com.bameng.model.PostModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.account.UserInfoActivity;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.bameng.utils.Util;
import com.bameng.widgets.CropperView;
import com.bameng.widgets.PhotoSelectView;
import com.huotu.android.library.libedittext.EditText;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.type;
import static com.baidu.location.h.j.v;
import static com.bameng.ui.account.UserInfoActivity.RESULT_CODE_CHNAGEPHONE;

public class NewOrderActivity extends BaseActivity implements CropperView.OnCropperBackListener, PhotoSelectView.OnPhotoSelectBackListener{

    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    @Bind(R.id.tvname)
    EditText etName;
    @Bind(R.id.tvphone)
    EditText etPhone;
    @Bind(R.id.tvaddress)
    EditText etAddress;
    @Bind(R.id.tvremarks)
    EditText etRemarks;
    @Bind(R.id.tvcash)
    EditText etCash;

    @Bind(R.id.ivImage)
    ImageView ivImage;

    @Bind(R.id.layAddImage)
    LinearLayout layAddImage;
    @Bind(R.id.layImage)
    FrameLayout layImage;
    @Bind(R.id.tvClose)
    TextView tvClose;
    private PhotoSelectView pop;
    private String imgPath;
    public Resources resources;
    private CropperView cropperView;
    private Bitmap cropBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        ButterKnife.bind(this);
        initView();
        application = (BaseApplication) this.getApplication();
        resources = this.getResources();
        StartApi();

    }

    @Override
    protected void initView() {
        titleText.setText("新增订单");
        titleLeftImage.setVisibility(View.VISIBLE);
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);

        layAddImage.setVisibility(View.VISIBLE);
        layImage.setVisibility(View.GONE);

    }

    @Override
    protected void StartApi() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String remarks = etRemarks.getText().toString().trim();
        String cash = etCash.getText().toString().trim();

        boolean isok=true;
        if(name.isEmpty()){
            etName.setError("请输入客户名称");
            isok=false;
        }
        if(phone.isEmpty()){
            etPhone.setError("请输入手机号");
            isok=false;
        }
        if(!isok){
            return;
        }

        String timestamp = String.valueOf(System.currentTimeMillis());
        Map<String,String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", timestamp );
        map.put("os", "android");
        map.put("userName",name);
        map.put("mobile",phone);
        map.put("address",address);
        map.put("memo",remarks);
        map.put("cashNo",cash);

        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        RequestBody requestBody = RequestBody.create( MediaType.parse("text/plain") , timestamp );
        requestBodyMap.put("timestamp",requestBody);
        requestBody=RequestBody.create(MediaType.parse("text/plain"), BaseApplication.getAppVersion());
        requestBodyMap.put("version",requestBody);
        requestBody=RequestBody.create(MediaType.parse("text/plain"), "android");
        requestBodyMap.put("os",requestBody);
        requestBody=RequestBody.create(MediaType.parse("text/plain"), name );
        requestBodyMap.put("userName",requestBody);

        requestBody=RequestBody.create(MediaType.parse("text/plain"), phone );
        requestBodyMap.put("mobile",requestBody);

        requestBody=RequestBody.create(MediaType.parse("text/plain"), address );
        requestBodyMap.put("address",requestBody);

        requestBody=RequestBody.create(MediaType.parse("text/plain"), remarks );
        requestBodyMap.put("memo",requestBody);

        requestBody=RequestBody.create(MediaType.parse("text/plain"), cash );
        requestBodyMap.put("cashNo",requestBody);

        requestBody=RequestBody.create(MediaType.parse("text/plain"), sign);
        requestBodyMap.put("sign",requestBody);

        if( cropBitmap !=null) {
            requestBody = RequestBody.create(MediaType.parse("image/*"), Util.bitmap2Bytes(cropBitmap));
            requestBodyMap.put("image\"; filename=\"" + timestamp + "\"", requestBody);
        }

        String token = BaseApplication.readToken();
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        Call<PostModel> call = apiService.ordercreate( token , requestBodyMap );
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if(response.code()!=200){
                    ToastUtils.showLongToast( response.message());
                    return;
                }
                if( response.body() !=null && response.body().getStatus() == 200 ){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    NewOrderActivity.this.finish();
                }else if( response.body() !=null ){
                    ToastUtils.showLongToast(response.body().getStatusText());
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                ToastUtils.showLongToast("error");
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @OnClick(R.id.btncreate)
    void create(){

        StartApi();
    }

    @OnClick(R.id.layAddImage)
    void uploadFile(){
        if(null == pop) pop = new PhotoSelectView(this, this);
        pop.show();
    }

    @Override
    public void onPhotoSelectBack(PhotoSelectView.SelectType type) {
        if(null == type) return;

        switch (type) {
            case Camera:
                getPhotoByCamera();
                break;
            case File:
                getPhotoByFile();
                break;
            default:
                break;
        }
    }


    public void getPhotoByCamera(){
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.v("TestFile","SD card is not avaiable/writeable right now.");
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss", Locale.CHINA);
        String imageName = "bm" + sdf.format(date) + ".jpg";
        imgPath = Environment.getExternalStorageDirectory()+ "/"+ imageName;
        File out = new File(imgPath);
        Uri uri = Uri.fromFile(out);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("fileName", imageName);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 0);
    }

    public void getPhotoByFile(){
        Intent intent2 = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent2, 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == 0) {// camera back
            Bitmap bitmap = Util.readBitmapByPath(imgPath);
            if (bitmap == null) {
                ToastUtils.showLongToast( "未获取到图片!");
                return;
            }
            if (null == cropperView)  cropperView = new CropperView(this, this);
            cropperView.cropper(bitmap);
        } else if (requestCode == 1) {// file back
            if (data != null) {
                Bitmap bitmap = null;
                Uri uri = data.getData();
                // url是content开头的格式
                if (uri.toString().startsWith("content://")) {
                    String path = null;
                    String[] pojo = { MediaStore.Images.Media.DATA };
                    Cursor cursor = this.getContentResolver().query(uri, pojo, null, null, null);
                    // managedQuery(uri, pojo, null, null, null);

                    if (cursor != null) {
                        // ContentResolver cr = this.getContentResolver();
                        int colunm_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        path = cursor.getString(colunm_index);

                        bitmap = Util.readBitmapByPath(path);
                        cursor.close();
                    }

                    if (bitmap == null) {
                        ToastUtils.showLongToast("未获取到图片!");
                        return;
                    }

                } else if (uri.toString().startsWith("file:///")) {
                    String path = uri.toString().substring(8, uri.toString().length());
                    bitmap = Util.readBitmapByPath(path);
                    if (bitmap == null) {
                        ToastUtils.showLongToast("未获取到图片!");
                        return;
                    }

                    //cropperView.cropper( bitmap );

                }
                if (null == cropperView)
                    cropperView = new CropperView(this, this);
                cropperView.cropper(bitmap);
            }

        }

    }

    @Override
    public void OnCropperBack(Bitmap bitmap) {
        if(null == bitmap)  return;
        cropBitmap = bitmap;

        layAddImage.setVisibility(View.GONE);
        layImage.setVisibility(View.VISIBLE);
        ivImage.setImageBitmap(cropBitmap);
    }

    @OnClick(R.id.tvClose)
    void closeImage(){
        if(cropBitmap!=null){
            cropBitmap.recycle();
            cropBitmap=null;
        }
        ivImage.setImageBitmap(null);

        layAddImage.setVisibility(View.VISIBLE);
        layImage.setVisibility(View.GONE);
    }

}
