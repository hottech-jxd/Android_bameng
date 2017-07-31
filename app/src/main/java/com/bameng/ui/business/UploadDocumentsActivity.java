package com.bameng.ui.business;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.BuildConfig;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.model.OrderModel;
import com.bameng.ui.base.PhoteActivity;
import com.bameng.utils.DensityUtils;
import com.bameng.utils.SystemTools;
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

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.bitmap;


/***
 * 上传成交凭证 界面
 */
public class UploadDocumentsActivity
        extends PhoteActivity
        implements PhotoSelectView.OnPhotoSelectBackListener , FrescoControllerListener.ImageCallback{

    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
    @BindView(R2.id.txtCustomer)
    EditText etCustomer;
    @BindView(R2.id.txtPhone)
    EditText etPhone;
    @BindView(R2.id.txtPrice)
    EditText etPrice;
    @BindView(R2.id.txtRemark)
    EditText etRemarks;
    @BindView(R2.id.ivPicture)
    SimpleDraweeView ivPicture;
    @BindView(R2.id.ivAddPic)
    ImageView ivAddPic;
    @BindView(R2.id.tvAddPic)
    TextView tvAddPic;
    @BindView(R2.id.llAddPic)
    LinearLayout llAddPic;

    private Bitmap currentBitmap;

    public Resources resources;

    OrderModel orderModel;

    ProgressDialog progressDialog;

    private PhotoSelectView pop;

    private String imgPath;

    //private Bitmap cropBitmap;

    boolean hasImage=false;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_documents);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void imageCallback(int position, int width, int height) {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);

        ivPicture.setLayoutParams(layoutParams);
    }

    protected void initView() {
        titleText.setText("上传成交凭证");
        titleLeftImage.setVisibility(View.VISIBLE);
        //Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        //SystemTools.loadBackground(titleLeftImage, leftDraw);
        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);

        orderModel = (OrderModel) getIntent().getSerializableExtra("order");
        etRemarks.setText( orderModel.getRemark()==null?"": orderModel.getRemark() );
        etCustomer.setText( orderModel.getUserName() );
        etPhone.setText( orderModel.getMobile());
        etPrice.setText( orderModel.getMoney() );
        String bitmapPath = getIntent().getStringExtra("bitmapPath");
        //Bitmap bitmap = getIntent().getParcelableExtra("bitmap");
        if(bitmapPath!=null && !bitmapPath.isEmpty()){
            ivAddPic.setVisibility(View.GONE);
            tvAddPic.setVisibility(View.GONE);
            ivPicture.setVisibility(View.VISIBLE);
            hasImage=true;
            imagePath = bitmapPath;
            int swid= DensityUtils.dip2px(this, 20);
            int wid = DensityUtils.getScreenW(this)-swid;
            FrescoDraweeController.loadImage( ivPicture , wid , "file://"+ bitmapPath , 0 , this );
        }else{
            ivAddPic.setVisibility(View.VISIBLE);
            tvAddPic.setVisibility(View.VISIBLE);
            ivPicture.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.llAddPic)
    void onClick(View v ){
        if(null == pop) pop = new PhotoSelectView(this, this);
        pop.show();
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

    private void showImg(final ArrayList<TImage> images) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                ivPicture.setVisibility(View.VISIBLE);
                ivAddPic.setVisibility(View.GONE);
                tvAddPic.setVisibility(View.GONE);
                //ivPicture.setImageBitmap(currentBitmap);

                imagePath = images.get(0).getCompressPath();//.getPath();

                int wid = DensityUtils.getScreenW(UploadDocumentsActivity.this);
                int swid = DensityUtils.dip2px(UploadDocumentsActivity.this, 20);
                //Glide.with(this).load(new File(images.get(0).getPath())).into(ivPicture);
                FrescoDraweeController.loadImage(ivPicture, wid - swid, "file://" + imagePath, 0, UploadDocumentsActivity.this);

            }

        });
    }

//    public void getPhotoByFile(){
//        Intent intent2 = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent2, 1);
//    }


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

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != Activity.RESULT_OK) return;
//
//        if (requestCode == 0) {// camera back
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
//                    //cropperView.cropper( bitmap );
//
//                }
//                if (null == cropperView)
//                    cropperView = new CropperView(this, this);
//                cropperView.cropper(cropBitmap);
//            }
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
//        ivPicture.setVisibility(View.VISIBLE);
//        ivAddPic.setVisibility(View.GONE);
//        tvAddPic.setVisibility(View.GONE);
//        ivPicture.setImageBitmap(currentBitmap);
//
//        if(cropBitmap !=null){
//            cropBitmap.recycle();
//            cropBitmap=null;
//        }
//    }

    @OnClick(R.id.btnSubmit)
    void onSubmit(View v){
        boolean isok = true;
        if(etCustomer.getText().toString().isEmpty()){
            etCustomer.setError("请输入客户名称");
            isok=false;
        }
        if(etPhone.getText().toString().isEmpty()){
            etPhone.setError("请输入客户电话");
            isok = false;
        }
        if(etPrice.getText().toString().isEmpty()){
            etPrice.setError("请输入价格");
            isok=false;
        }
        String price = etPrice.getText().toString().trim();
        if( !price.isEmpty() && !Util.isMoney( price) ){
            etPrice.setError("请输入最多3位小数的价格");
            return;
        }
        if( !hasImage) {
            ToastUtils.showLongToast("请上传图片");
            isok=false;
            return;
        }
        if(isok==false) return;

        Intent data = new Intent();
        Bundle bd = new Bundle();
        orderModel.setUserName(etCustomer.getText().toString().trim());
        orderModel.setMobile(etPhone.getText().toString().trim());
        orderModel.setMoney(etPrice.getText().toString().trim());
        orderModel.setRemark( etRemarks.getText().toString().trim() );
        //orderModel.setBitmap( currentBitmap );
        bd.putSerializable("order", orderModel);

        data.putExtras(bd);



        //ivPicture.setDrawingCacheEnabled(true);
        //Bitmap bitmap = null;// ivPicture.getDrawingCache();
        //data.putExtra("bitmap", bitmap);

        data.putExtra("bitmapPath", imagePath);

        setResult(RESULT_OK, data);
        finish();
    }
}
