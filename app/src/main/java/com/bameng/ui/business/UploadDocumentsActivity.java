package com.bameng.ui.business;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.model.OrderModel;
import com.bameng.model.PostModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.DensityUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.bameng.utils.Util;
import com.bameng.widgets.CropperView;
import com.bameng.widgets.PhotoSelectView;
import com.bameng.widgets.custom.FrescoDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.huotu.android.library.libedittext.EditText;

import java.io.File;
import java.math.BigDecimal;
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

import static com.baidu.location.h.j.D;
import static com.baidu.location.h.j.O;
import static com.bameng.R.id.ivImage;
import static com.bameng.R.id.layAddImage;
import static com.bameng.R.id.layImage;
import static com.bameng.R.id.txtCustomer;
import static com.bameng.service.LocationService.address;

/***
 * 上传成交凭证 界面
 */
public class UploadDocumentsActivity extends BaseActivity implements  CropperView.OnCropperBackListener,  PhotoSelectView.OnPhotoSelectBackListener{

    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    @Bind(R.id.txtCustomer)
    EditText etCustomer;
    @Bind(R.id.txtPhone)
    EditText etPhone;
    @Bind(R.id.txtPrice)
    EditText etPrice;
    @Bind(R.id.txtRemark)
    EditText etRemarks;
    @Bind(R.id.ivPicture)
    ImageView ivPicture;
    @Bind(R.id.ivAddPic)
    ImageView ivAddPic;
    @Bind(R.id.tvAddPic)
    TextView tvAddPic;
    @Bind(R.id.llAddPic)
    LinearLayout llAddPic;

    private Bitmap currentBitmap;

    public Resources resources;

    OrderModel orderModel;

    ProgressDialog progressDialog;

    private PhotoSelectView pop;

    private String imgPath;

    private Bitmap cropBitmap;

    private CropperView cropperView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_documents);
        ButterKnife.bind(this);
        initView();
        application = (BaseApplication) this.getApplication();
        resources = this.getResources();
        //StartApi();
    }

    @Override
    protected void initView() {
        titleText.setText("上传成交凭证");
        titleLeftImage.setVisibility(View.VISIBLE);
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);

        orderModel = (OrderModel) getIntent().getSerializableExtra("order");
        etRemarks.setText( orderModel.getRemark()==null?"": orderModel.getRemark() );
        etCustomer.setText( orderModel.getUserName() );
        etPhone.setText( orderModel.getMobile());
        etPrice.setText( orderModel.getMoney() );
        Bitmap bitmap = getIntent().getParcelableExtra("bitmap");

        if( bitmap!=null){
            ivAddPic.setVisibility(View.GONE);
            tvAddPic.setVisibility(View.GONE);
            ivPicture.setVisibility(View.VISIBLE);
            ivPicture.setImageBitmap(bitmap);
        }else{
            ivAddPic.setVisibility(View.VISIBLE);
            tvAddPic.setVisibility(View.VISIBLE);
            ivPicture.setVisibility(View.GONE);
        }
    }

    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
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
                getPhotoByCamera();
                break;
            case File:
                getPhotoByFile();
                break;
            default:
                break;
        }
    }

    public void getPhotoByFile(){
        Intent intent2 = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent2, 1);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == 0) {// camera back
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

                    //cropperView.cropper( bitmap );

                }
                if (null == cropperView)
                    cropperView = new CropperView(this, this);
                cropperView.cropper(cropBitmap);
            }

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

        ivPicture.setVisibility(View.VISIBLE);
        ivAddPic.setVisibility(View.GONE);
        tvAddPic.setVisibility(View.GONE);
        ivPicture.setImageBitmap(currentBitmap);

        if(cropBitmap !=null){
            cropBitmap.recycle();
            cropBitmap=null;
        }
    }

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
        if(currentBitmap==null) {
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
        data.putExtra("bitmap", currentBitmap);
        setResult(RESULT_OK, data);
        finish();
    }
}
