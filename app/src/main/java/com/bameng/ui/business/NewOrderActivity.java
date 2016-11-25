package com.bameng.ui.business;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
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
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.config.Constants;
import com.bameng.model.PostModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.account.UserInfoActivity;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.DensityUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.bameng.utils.Util;
import com.bameng.widgets.CropperView;
import com.bameng.widgets.ImageCropperView;
import com.bameng.widgets.PhotoSelectView;
import com.huotu.android.library.libedittext.EditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

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
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.bitmap;
import static android.R.attr.type;
import static com.baidu.location.h.j.s;
import static com.baidu.location.h.j.v;
import static com.baidu.location.h.j.w;
import static com.bameng.ui.account.UserInfoActivity.RESULT_CODE_CHNAGEPHONE;

@RuntimePermissions
public class NewOrderActivity extends BaseActivity
        implements CropperView.OnCropperBackListener, PhotoSelectView.OnPhotoSelectBackListener{

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
    //private ImageCropperView imageCropperView;

    private Bitmap cropBitmap;
    private Bitmap currentBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        ButterKnife.bind(this);
        initView();
        application = (BaseApplication) this.getApplication();
        resources = this.getResources();
        //StartApi();
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
    protected void onDestroy() {
        super.onDestroy();
        if (cropBitmap != null) {
            cropBitmap.recycle();
            cropBitmap = null;
        }
        if (currentBitmap != null) {
            currentBitmap.recycle();
            currentBitmap = null;
        }
    }

    @Override
    protected void StartApi() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String remarks = etRemarks.getText().toString().trim();
        String cash = etCash.getText().toString().trim();

        boolean isok=true;
        if(address.isEmpty()){
            etAddress.setError("请输入客户地址");
            isok=false;
        }
        if(phone.isEmpty()){
            etPhone.setError("请输入手机号");
            isok=false;
        }
        if(name.isEmpty()){
            etName.setError("请输入客户名称");
            isok=false;
        }

        if(currentBitmap==null){
            ToastUtils.showLongToast("请上传订单图片");
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

        if( currentBitmap !=null) {
            requestBody = RequestBody.create(MediaType.parse("image/*"), Util.bitmap2Bytes(currentBitmap));
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
    public void onUploadFile(){
//        if(null == pop) pop = new PhotoSelectView(this, this);
//        pop.show();
        NewOrderActivityPermissionsDispatcher.uploadFileWithCheck(this);
    }

    @NeedsPermission( {Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void uploadFile(){
        if(null == pop) pop = new PhotoSelectView(this, this);
        pop.show();
    }

    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void showRationaleForWriteStorage(PermissionRequest request){
        showRationaleDialog( R.string.permission_writefile_asked , request );
    }

    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE })
    public void onWriteStorageDenied(){
        Toast.makeText(this, R.string.permission_writefile_denied , Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE })
    public void onWriteStorageNeverAskAgain(){
        Toast.makeText(this, R.string.permission_writefile_never_asked , Toast.LENGTH_SHORT).show();
    }


    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.permission_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.permission_dency, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
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

//    private void cropperImage( Bitmap bitmap  ){
//        //Uri uri = Uri.parse(path);
//        if( null == imageCropperView ) imageCropperView = new ImageCropperView(this, this);
//        imageCropperView.cropper(  bitmap  );
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        NewOrderActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == 0) {// camera back
//            cropBitmap = Util.readBitmapByPath(imgPath);
//            cropperImage( cropBitmap );

            cropBitmap = Util.readBitmapByPath(imgPath);
            if (cropBitmap == null) {
                ToastUtils.showLongToast( "未获取到图片!");
                return;
            }
            if (null == cropperView)  cropperView = new CropperView(this, this);
            cropperView.cropper(cropBitmap);
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

                        cropBitmap = Util.readBitmapByPath(path);
                        cursor.close();
                    }

                    if (cropBitmap == null) {
                        ToastUtils.showLongToast("未获取到图片!");
                        return;
                    }

                } else if (uri.toString().startsWith("file:///")) {
                    String path = uri.toString().substring(8, uri.toString().length());
                    cropBitmap = Util.readBitmapByPath(path);
                    if (cropBitmap == null) {
                        ToastUtils.showLongToast("未获取到图片!");
                        return;
                    }

                }
                if (null == cropperView)
                    cropperView = new CropperView(this, this);
                cropperView.cropper(cropBitmap);
            }

        }else if( requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

        }

    }

    @Override
    public void OnCropperBack(Bitmap bitmap) {
        if(null == bitmap) {
            if(cropBitmap !=null){
                cropBitmap.recycle();
                cropBitmap=null;
            }
            return;
        }
        currentBitmap = bitmap;

        layAddImage.setVisibility(View.GONE);
        layImage.setVisibility(View.VISIBLE);
        setWidthHeight(ivImage,bitmap);
        ivImage.setImageBitmap(bitmap);

        if(cropBitmap !=null){
            cropBitmap.recycle();
            cropBitmap=null;
        }
    }

    void setWidthHeight( ImageView iv , Bitmap bitmap){
        int width = bitmap.getWidth();
        int height =bitmap.getHeight();
        int sw = DensityUtils.getScreenW(this);
        int sh = DensityUtils.getScreenH(this);
        int tw;
        int th;
        if( width <= sw && height <= sh  ){
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width,height);
            layoutParams.gravity = Gravity.CENTER;
            iv.setLayoutParams(layoutParams);
        }else  {
            tw = sw * height / sh;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
            layoutParams.gravity = Gravity.CENTER;
            iv.setLayoutParams(layoutParams);
        }
    }

    @OnClick(R.id.tvClose)
    void closeImage() {

        ivImage.setImageBitmap(null);
        if (cropBitmap != null) {
            cropBitmap.recycle();
            cropBitmap = null;
        }
        if (currentBitmap != null) {
            currentBitmap.recycle();
            currentBitmap = null;
        }

        layAddImage.setVisibility(View.VISIBLE);
        layImage.setVisibility(View.GONE);
    }

}
