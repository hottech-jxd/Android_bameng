package com.bameng.ui.account;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.bameng.utils.Util;
import com.bameng.widgets.CropperView;
import com.bameng.widgets.PhotoSelectView;
import com.bameng.widgets.UserInfoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInfoActivity extends BaseActivity implements PhotoSelectView.OnPhotoSelectBackListener,CropperView.OnCropperBackListener,UserInfoView.OnUserInfoBackListener {

    @Bind(R.id.layImg)
    LinearLayout layImg;
    @Bind(R.id.img_user)
    ImageView imgUser;
    @Bind(R.id.layname)
    LinearLayout layname;
    @Bind(R.id.txt_name)
    TextView txtName;
    @Bind(R.id.layphone)
    LinearLayout layphone;
    @Bind(R.id.txt_phone)
    TextView txtPhone;
    @Bind(R.id.layrealname)
    LinearLayout layrealname;
    @Bind(R.id.txt_realname)
    TextView txtRealname;
    @Bind(R.id.laysex)
    LinearLayout laysex;
    @Bind(R.id.txt_sex)
    TextView txtSex;
    @Bind(R.id.laynationality)
    LinearLayout laynationality;
    @Bind(R.id.txt_nationality)
    TextView txtNationality;
    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    public Resources resources;
    private PhotoSelectView pop;
    private CropperView cropperView;
    private UserInfoView userInfoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        resources = this.getResources();
        userInfoView = new UserInfoView(this);
        userInfoView.setOnUserInfoBackListener(this);
        initView();
        StartApi();
    }

    @Override
    protected void initView() {
        titleText.setText("个人信息");
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);
    }

    @OnClick({R.id.layname,R.id.layImg,R.id.laynationality,R.id.layphone,R.id.layrealname,R.id.laysex})
    void click(View view){
        switch (view.getId()){
            case R.id.layImg:
                if(null == pop)
                    pop = new PhotoSelectView(UserInfoActivity.this, this);
                pop.show();
                break;
            case R.id.layname:
                userInfoView.show(UserInfoView.Type.Name, txtName.getText().toString());
                break;
            case R.id.layphone:
                ActivityUtils.getInstance().showActivity(UserInfoActivity.this,PhoneChangeActivity.class);
                break;
            case R.id.layrealname:
                userInfoView.show(UserInfoView.Type.Realname, txtRealname.getText().toString());
                break;
            case R.id.laysex:
                userInfoView.show(UserInfoView.Type.Sex,txtSex.getText().toString());
                break;
            case R.id.laynationality:
                ActivityUtils.getInstance().showActivity(UserInfoActivity.this,AddressActivity.class);
                break;
            default:
                break;
        }
    }
    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == 0) {// camera back
            Bitmap bitmap = Util.readBitmapByPath(imgPath);
            if (bitmap == null) {
                ToastUtils.showLongToast( "未获取到图片!");
                return;
            }
            if (null == cropperView)
                cropperView = new CropperView(this, this);
            cropperView.cropper(bitmap);
        } else if (requestCode == 1) {// file back
            if (data != null) {
                Bitmap bitmap = null;
                Uri uri = data.getData();
                // url是content开头的格式
                if (uri.toString().startsWith("content://")) {
                    String path = null;
                    String[] pojo = { MediaStore.Images.Media.DATA };
                    Cursor cursor = this.getContentResolver().query(uri, pojo, null,
                            null, null);
                    // managedQuery(uri, pojo, null, null, null);

                    if (cursor != null) {
                        // ContentResolver cr = this.getContentResolver();
                        int colunm_index = cursor
                                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        path = cursor.getString(colunm_index);

                        bitmap = Util.readBitmapByPath(path);
                    }

                    if (bitmap == null) {
                        ToastUtils.showLongToast("未获取到图片!");
                        return;
                    }
                } else if (uri.toString().startsWith("file:///")) {
                    String path = uri.toString().substring(8,
                            uri.toString().length());
                    bitmap = Util.readBitmapByPath(path);
                    if (bitmap == null) {
                        ToastUtils.showLongToast("未获取到图片!");
                        return;
                    }

                }
                if (null == cropperView)
                    cropperView = new CropperView(this, this);
                cropperView.cropper(bitmap);
            }

        }

    }


    private Bitmap cropBitmap;
    @Override
    public void OnCropperBack(Bitmap bitmap) {
        if(null == bitmap)
            return;
        cropBitmap = bitmap;
        commitPhoto();

    }
    private void commitPhoto(){

    }


    @Override
    public void onPhotoSelectBack(PhotoSelectView.SelectType type) {
        if(null == type)
            return;
        getPhotoByType(type);
    }
    private void getPhotoByType(PhotoSelectView.SelectType type){
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

    private String imgPath;

    public void getPhotoByCamera(){
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.v("TestFile","SD card is not avaiable/writeable right now.");
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss", Locale.CHINA);
        String imageName = "fm" + sdf.format(date) + ".jpg";
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
    public void onUserInfoBack(UserInfoView.Type type) {
        commit(type);

    }
    private void commit(UserInfoView.Type type){

    }
}
