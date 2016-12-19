package com.bameng.ui.business;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.model.CustomerModel;
import com.bameng.model.PostModel;
import com.bameng.model.RefreshOrderEvent;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.PhoteActivity;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.bameng.utils.Util;

import com.bameng.widgets.PhotoSelectView;
import com.bumptech.glide.Glide;
import com.huotu.android.library.libedittext.EditText;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;


import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
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


/***
 * 新增订单 界面
 */
@RuntimePermissions
public class NewOrderActivity extends PhoteActivity
        implements PhotoSelectView.OnPhotoSelectBackListener{

    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
    @BindView(R2.id.tvname)
    EditText etName;
    @BindView(R2.id.tvphone)
    EditText etPhone;
    @BindView(R2.id.tvaddress)
    EditText etAddress;
    @BindView(R2.id.tvremarks)
    EditText etRemarks;
    @BindView(R2.id.tvcash)
    EditText etCash;

    @BindView(R2.id.ivImage)
    ImageView ivImage;

    @BindView(R2.id.layAddImage)
    LinearLayout layAddImage;
    @BindView(R2.id.layImage)
    FrameLayout layImage;

    private PhotoSelectView pop;
//    private String imgPath;
    //public Resources resources;
    private Bitmap cropBitmap;
    private Bitmap currentBitmap;
    boolean hasImage=false;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        ButterKnife.bind(this);
        initView();
    }

    protected void initView() {
        titleText.setText("新增订单");
        titleLeftImage.setVisibility(View.VISIBLE);
        //Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        //SystemTools.loadBackground(titleLeftImage, leftDraw);
        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);

        layAddImage.setVisibility(View.VISIBLE);
        layImage.setVisibility(View.GONE);

        CustomerModel customerModel;
        if(getIntent().hasExtra("customer")){
            customerModel = (CustomerModel) getIntent().getSerializableExtra("customer");
            if(customerModel!=null){
                etName.setText( customerModel.getName() );
                etPhone.setText(customerModel.getMobile());
                etAddress.setText(customerModel.getAddr());
            }
        }
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
        ivImage.setDrawingCacheEnabled(false);
    }


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

        if(!hasImage ){
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

        String sign = AuthParamUtils.getSign(map);

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

        ivImage.setDrawingCacheEnabled(true);
        Bitmap tempBitmap = ivImage.getDrawingCache();

        if( tempBitmap !=null) {
            requestBody = RequestBody.create(MediaType.parse("image/*"), Util.bitmap2Bytes(tempBitmap));
            requestBodyMap.put("image\"; filename=\"" + timestamp + "\"", requestBody);
        }

        if(progressDialog==null){
            progressDialog=new ProgressDialog(this);
        }
        progressDialog.setMessage("上传订单中...");
        progressDialog.show();

        String token = BaseApplication.readToken();
        ApiService apiService = ZRetrofitUtil.getApiService();
        Call<PostModel> call = apiService.ordercreate( token , requestBodyMap );
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if(progressDialog !=null) progressDialog.dismiss();

                if(response.code()!=200){
                    ToastUtils.showLongToast( response.message());
                    return;
                }
                if( response.body() !=null && response.body().getStatus() == 200 ){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new RefreshOrderEvent());
                    NewOrderActivity.this.finish();
                }else if( response.body() !=null ){
                    ToastUtils.showLongToast(response.body().getStatusText());
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                if(progressDialog!=null) progressDialog.dismiss();
                ToastUtils.showLongToast(t.getMessage()==null? "请求失败":t.getMessage());
            }
        });
    }

    @OnClick(R.id.btncreate)
    void create(){
        StartApi();
    }


    @OnClick(R.id.layAddImage)
    public void onUploadFile(){
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
                //getPhotoByCamera();
                selectPhotoByCamera();
                break;
            case File:
                //getPhotoByFile();
                selectPhotoByFile();
                break;
            default:
                break;
        }
    }

    void selectPhotoByCamera(){
        //File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
        File file = new File( this.getExternalCacheDir(), "/temp/"+ System.currentTimeMillis() + ".jpg" );

        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);

        selectByCamera(imageUri);
    }

    void selectPhotoByFile(){
        File file = new File( this.getExternalCacheDir(), "/temp/"+ System.currentTimeMillis() + ".jpg" );

        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        selectByFile(imageUri);
    }


    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        hasImage=true;
        showImg(result.getImages());
    }


    private void showImg( ArrayList<TImage> images) {

        layAddImage.setVisibility(View.GONE);
        layImage.setVisibility(View.VISIBLE);


        Glide.with(this).load(new File(images.get(0).getPath())).into(ivImage);

//            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llImages);
//            for (int i = 0, j = images.size(); i < j - 1; i += 2) {
//                View view = LayoutInflater.from(this).inflate(R.layout.image_show, null);
//                ImageView imageView1 = (ImageView) view.findViewById(R.id.imgShow1);
//                ImageView imageView2 = (ImageView) view.findViewById(R.id.imgShow2);
//                Glide.with(this).load(new File(images.get(i).getPath())).into(imageView1);
//                Glide.with(this).load(new File(images.get(i + 1).getPath())).into(imageView2);
//                linearLayout.addView(view);
//            }
//            if (images.size() % 2 == 1) {
//                View view = LayoutInflater.from(this).inflate(R.layout.image_show, null);
//                ImageView imageView1 = (ImageView) view.findViewById(R.id.imgShow1);
//                Glide.with(this).load(new File(images.get(images.size() - 1).getPath())).into(imageView1);
//                linearLayout.addView(view);
//            }

    }

//    public void getPhotoByCamera(){
//        String sdStatus = Environment.getExternalStorageState();
//        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
//            Log.v("TestFile","SD card is not avaiable/writeable right now.");
//            return;
//        }
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss", Locale.CHINA);
//        String imageName = "bm" + sdf.format(date) + ".jpg";
//        imgPath = Environment.getExternalStorageDirectory()+ "/"+ imageName;
//        File out = new File(imgPath);
//        Uri uri = Uri.fromFile(out);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        intent.putExtra("fileName", imageName);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, 0);
//    }
//
//    public void getPhotoByFile(){
//        Intent intent2 = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent2, 1);
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        NewOrderActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != Activity.RESULT_OK) return;
//
//        if (requestCode == 0) {// camera back
////            cropBitmap = Util.readBitmapByPath(imgPath);
////            cropperImage( cropBitmap );
//
//            cropBitmap = Util.readBitmapByPath(imgPath);
//            if (cropBitmap == null) {
//                ToastUtils.showLongToast( "未获取到图片!");
//                return;
//            }
//            if (null == cropperView)  cropperView = new CropperView(this, this);
//            cropperView.cropper(cropBitmap);
//        } else if (requestCode == 1) {// file back
//            if (data != null) {
//                Bitmap bitmap = null;
//                Uri uri = data.getData();
//                // url是content开头的格式
//                if (uri.toString().startsWith("content://")) {
//                    String path = null;
//                    String[] pojo = { MediaStore.Images.Media.DATA };
//                    Cursor cursor = this.getContentResolver().query(uri, pojo, null, null, null);
//                    // managedQuery(uri, pojo, null, null, null);
//
//                    if (cursor != null) {
//                        // ContentResolver cr = this.getContentResolver();
//                        int colunm_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                        cursor.moveToFirst();
//                        path = cursor.getString(colunm_index);
//
//                        cropBitmap = Util.readBitmapByPath(path);
//                        cursor.close();
//                    }
//
//                    if (cropBitmap == null) {
//                        ToastUtils.showLongToast("未获取到图片!");
//                        return;
//                    }
//
//                } else if (uri.toString().startsWith("file:///")) {
//                    String path = uri.toString().substring(8, uri.toString().length());
//                    cropBitmap = Util.readBitmapByPath(path);
//                    if (cropBitmap == null) {
//                        ToastUtils.showLongToast("未获取到图片!");
//                        return;
//                    }
//
//                }
//                if (null == cropperView)
//                    cropperView = new CropperView(this, this);
//                cropperView.cropper(cropBitmap);
//            }
//
//        }else if( requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//
//        }
//
//    }

//    @Override
//    public void OnCropperBack(Bitmap bitmap) {
//        if(null == bitmap) {
//            if(cropBitmap !=null){
//                cropBitmap.recycle();
//                cropBitmap=null;
//            }
//            return;
//        }
//        currentBitmap = bitmap;
//
//        layAddImage.setVisibility(View.GONE);
//        layImage.setVisibility(View.VISIBLE);
//        setWidthHeight(ivImage,bitmap);
//        ivImage.setImageBitmap(bitmap);
//
//        if(cropBitmap !=null){
//            cropBitmap.recycle();
//            cropBitmap=null;
//        }
//    }

//    void setWidthHeight( ImageView iv , Bitmap bitmap){
//        int width = bitmap.getWidth();
//        int height =bitmap.getHeight();
//        int sw = DensityUtils.getScreenW(this);
//        int sh = DensityUtils.getScreenH(this);
//        int tw;
//        int th;
//        if( width <= sw && height <= sh  ){
//            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width,height);
//            layoutParams.gravity = Gravity.CENTER;
//            iv.setLayoutParams(layoutParams);
//        }else  {
//            tw = sw * height / sh;
//            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
//            layoutParams.gravity = Gravity.CENTER;
//            iv.setLayoutParams(layoutParams);
//        }
//    }

    @OnClick(R.id.tvClose)
    void closeImage() {
        hasImage = false;


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
