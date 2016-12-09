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
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.config.Constants;
import com.bameng.listener.PoponDismissListener;
import com.bameng.model.ArticleListOutput;
import com.bameng.model.AvatarOutputModel;
import com.bameng.model.CloseEvent;
import com.bameng.model.PostModel;
import com.bameng.model.RefreshUserDataEvent;
import com.bameng.model.UserData;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.base.PhoteActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.bameng.utils.Util;
import com.bameng.widgets.AddressPopWin;
//import com.bameng.widgets.CropperView;
import com.bameng.widgets.PhotoSelectView;
import com.bameng.widgets.UserInfoView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.memory.BitmapPool;
import com.jph.takephoto.model.TResult;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.bameng.R.id.txt_nationality;

/***
 * 个人信息
 */
public class UserInfoActivity extends PhoteActivity
        implements PhotoSelectView.OnPhotoSelectBackListener,
        UserInfoView.OnUserInfoBackListener ,Handler.Callback {

    @BindView(R.id.txt_loginName)
    TextView txtLoginName;
    @BindView(R2.id.layImg)
    LinearLayout layImg;
    @BindView(R2.id.img_user)
    SimpleDraweeView imgUser;
    @BindView(R2.id.layname)
    LinearLayout layname;
    @BindView(R2.id.txt_name)
    TextView txtName;
    @BindView(R2.id.layphone)
    LinearLayout layphone;
    @BindView(R2.id.txt_phone)
    TextView txtPhone;
    @BindView(R2.id.layrealname)
    LinearLayout layrealname;
    @BindView(R2.id.txt_realname)
    TextView txtRealname;
    @BindView(R2.id.laysex)
    LinearLayout laysex;
    @BindView(R2.id.txt_sex)
    TextView txtSex;
    @BindView(R2.id.laynationality)
    LinearLayout laynationality;
    @BindView(txt_nationality)
    TextView txtNationality;
    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
    private PhotoSelectView pop;
    private UserInfoView userInfoView;
    UserData userData;
    public static final int RESULT_CODE_CHNAGEPHONE=100;

    public AddressPopWin addressPopWin;
    public InputMethodManager inputMethodManager;
    private Bitmap cropBitmap;
    protected Handler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        userInfoView = new UserInfoView(this);
        userInfoView.setOnUserInfoBackListener(this);
        inputMethodManager = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));

        initView();

    }

    protected void initView() {
        mHandler= new Handler(this);
        titleText.setText("个人信息");
        //Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        //SystemTools.loadBackground(titleLeftImage, leftDraw);
        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);

        userData= BaseApplication.readUserInfo();
        imgUser.setImageURI(userData.getUserHeadImg());
        txtName.setText(userData.getNickName());
        txtRealname.setText(userData.getRealName());
        txtPhone.setText(userData.getUserMobile());
        if( userData.getUserGender() !=null && userData.getUserGender().equals("M")){
            txtSex.setText("男");
        }else if( userData.getUserGender() !=null && userData.getUserGender().equals("F")){
            txtSex.setText("女");
        }else{
            txtSex.setText("未知");
        }

        txtNationality.setText(userData.getUserCity());

        txtLoginName.setText(userData.getLoginName());

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
                Intent intent=new Intent(UserInfoActivity.this, PhoneChangeActivity.class);
                intent.putExtra("oldPhone",userData.getUserMobile());
                ActivityUtils.getInstance().showActivityForResult(UserInfoActivity.this, RESULT_CODE_CHNAGEPHONE , intent );
                break;
            case R.id.layrealname:
                userInfoView.show(UserInfoView.Type.Realname, txtRealname.getText().toString());
                break;
            case R.id.laysex:
                userInfoView.show(UserInfoView.Type.Sex,txtSex.getText().toString());
                break;
            case R.id.laynationality:
                //ActivityUtils.getInstance().showActivity(UserInfoActivity.this,AddressActivity.class);
                selectArea();
                break;
            default:
                break;
        }
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.SELECT_ADDRESS: {
                List<String> address = (List<String>) msg.obj;
                String temp = address.get(0) +"-" + address.get(1) +"-" + address.get(2);
                txtNationality.setText( temp );
                updateUserInfo( Constants.User_6 , temp);
            }
            break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;
//        if (requestCode == 0) {// camera back
//            Bitmap bitmap = Util.readBitmapByPath(imgPath);
//            if (bitmap == null) {
//                ToastUtils.showLongToast( "未获取到图片!");
//                return;
//            }
//            if (null == cropperView)
//                cropperView = new CropperView(this, this);
//            cropperView.cropper(bitmap);
//        } else if (requestCode == 1) {// file back
//            if (data != null) {
//                Bitmap bitmap = null;
//                Uri uri = data.getData();
//                // url是content开头的格式
//                if (uri.toString().startsWith("content://")) {
//                    String path = null;
//                    String[] pojo = { MediaStore.Images.Media.DATA };
//                    Cursor cursor = this.getContentResolver().query(uri, pojo, null,
//                            null, null);
//                    // managedQuery(uri, pojo, null, null, null);
//
//                    if (cursor != null) {
//                        // ContentResolver cr = this.getContentResolver();
//                        int colunm_index = cursor
//                                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                        cursor.moveToFirst();
//                        path = cursor.getString(colunm_index);
//
//                        bitmap = Util.readBitmapByPath(path);
//                    }
//
//                    if (bitmap == null) {
//                        ToastUtils.showLongToast("未获取到图片!");
//                        return;
//                    }
//
//                    //cropperView.cropper( bitmap );
//
//                } else if (uri.toString().startsWith("file:///")) {
//                    String path = uri.toString().substring(8,
//                            uri.toString().length());
//                    bitmap = Util.readBitmapByPath(path);
//                    if (bitmap == null) {
//                        ToastUtils.showLongToast("未获取到图片!");
//                        return;
//                    }
//
//                    //cropperView.cropper( bitmap );
//
//                }
//                if (null == cropperView)
//                    cropperView = new CropperView(this, this);
//                cropperView.cropper(bitmap);
//            }

//        }else
            if( requestCode == RESULT_CODE_CHNAGEPHONE){
            String newPhone = data.getStringExtra("newPhone");
            txtPhone.setText( newPhone );
            userData.setUserMobile(newPhone);
            BaseApplication.writeUserInfo( userData );
        }

    }


    private void commitPhoto(){
        updateFile( Constants.User_1 , cropBitmap );
    }


    @Override
    public void onPhotoSelectBack(PhotoSelectView.SelectType type) {
        if(null == type) return;
        getPhotoByType(type);
    }

    private void getPhotoByType(PhotoSelectView.SelectType type){
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
        //hasImage=true;
        //showImg(result.getImages());

        String imagePath = result.getImages().get(0).getPath();
        cropBitmap = Util.readBitmapByPath( imagePath );
        commitPhoto();
    }


    private String imgPath;

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

//    public void getPhotoByFile(){
//        Intent intent2 = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent2, 1);
//    }

    @Override
    public void onUserInfoBack(UserInfoView.Type type , String value ) {
        if(type == UserInfoView.Type.Name){
            userData.setNickName( value);
            BaseApplication.writeUserInfo(userData);
            txtName.setText(  value );
            updateUserInfo(Constants.User_2 , value );
        }else if( type == UserInfoView.Type.Sex){
            userData.setUserGender( value );
            BaseApplication.writeUserInfo(userData);
            txtSex.setText( value.equals("M")?"男": value.equals("F")?"女":"未知");
            updateUserInfo(Constants.User_5 , value);
        }else if( type == UserInfoView.Type.Realname) {
            userData.setRealName(value);
            BaseApplication.writeUserInfo(userData);
            txtRealname.setText( value );
            updateUserInfo(Constants.User_4 , value);
        }
        //TODO 提交服务端

    }

    protected void updateUserInfo(int type , final String value){
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("type",String.valueOf(type));
        map.put("content", value );
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<PostModel> call = apiService.UpdateInfo(token,map);
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if( response.code() !=200) {
                    ToastUtils.showLongToast(response.message());
                    return;
                }
                if(response.body()==null){
                    ToastUtils.showLongToast("返回错误数据");
                    return;
                }
                if(response.body().getStatus() == Constants.STATUS_70035){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(UserInfoActivity.this, PhoneLoginActivity.class);
                    return;
                }

                if(response.body().getStatus() !=200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }
                ToastUtils.showLongToast(response.body().getStatusText());
                BaseApplication.UserData().setUserCity( value );
                BaseApplication.writeUserInfo( BaseApplication.UserData() );
                EventBus.getDefault().post( new RefreshUserDataEvent( BaseApplication.UserData() ));
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                ToastUtils.showLongToast( t.getMessage()==null? "请求失败": t.getMessage() );
            }
        });

    }

    protected void updateFile(int type , Bitmap bitmap ){
        String timestamp = String.valueOf(System.currentTimeMillis());
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", timestamp);
        map.put("os", "android");
        map.put("type",String.valueOf(type));
        //map.put("content", value );
        String sign = AuthParamUtils.getSign(map);

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        RequestBody requestBody = RequestBody.create( MediaType.parse("text/plain") , timestamp );
        requestBodyMap.put("timestamp",requestBody);
        requestBody=RequestBody.create(MediaType.parse("text/plain"), BaseApplication.getAppVersion());
        requestBodyMap.put("version",requestBody);
        requestBody=RequestBody.create(MediaType.parse("text/plain"), "android");
        requestBodyMap.put("os",requestBody);
        requestBody=RequestBody.create(MediaType.parse("text/plain"), String.valueOf(type));
        requestBodyMap.put("type",requestBody);
        requestBody=RequestBody.create(MediaType.parse("text/plain"), sign);
        requestBodyMap.put("sign",requestBody);

        requestBody = RequestBody.create(MediaType.parse("image/*"), Util.bitmap2Bytes( bitmap ));
        requestBodyMap.put("image\"; filename=\"" + timestamp + "\"", requestBody);

        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<AvatarOutputModel> call = apiService.UpdateFileInfo(token,requestBodyMap);
        call.enqueue(new Callback<AvatarOutputModel>() {
            @Override
            public void onResponse(Call<AvatarOutputModel> call, Response<AvatarOutputModel> response) {
                if(response.code() !=200){
                    ToastUtils.showLongToast(response.message());
                    return;
                }
                if(response.body()==null){
                    ToastUtils.showLongToast("服务器发生错误");
                    return;
                }
                if(response.body().getStatus() == Constants.STATUS_70035){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(UserInfoActivity.this, PhoneLoginActivity.class);
                    return;
                }
                if(response.body().getStatus()!=200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }

                if( response.body().getStatus() == 200 && response.body().getData() !=null ) {
                    String url = response.body().getData().getUrl();
                    imgUser.setImageURI( url );
                    BaseApplication.UserData().setUserHeadImg( url );
                    BaseApplication.writeUserInfo( BaseApplication.UserData() );
                    EventBus.getDefault().post(new RefreshUserDataEvent(BaseApplication.UserData()));
                }
            }

            @Override
            public void onFailure(Call<AvatarOutputModel> call, Throwable t) {
                ToastUtils.showLongToast( t.getMessage()==null?"请求失败":t.getMessage() );
            }
        });
    }


    void selectArea() {
        String temp = txtNationality.getText().toString().trim();
        String province="";
        String city="";
        String area="";
        if( !temp.isEmpty()){
            String[] items = temp.split("-");
            if( items !=null){
                if(items.length>0){
                    province = items[0];
                }
                if(items.length>1){
                    city = items[1];
                }
                if(items.length>2){
                    area= items[2];
                }
            }
        }

        //inputMethodManager.hideSoftInputFromWindow(provinceL.getWindowToken(), 0);
        if(addressPopWin==null) {
            addressPopWin = new AddressPopWin(mHandler, application, UserInfoActivity.this, application.localAddress, 0, getWindowManager(), UserInfoActivity.this);
            addressPopWin.initView();
        }
        //addressPopWin.initView();


        //if( !TextUtils.isEmpty(province)) {
        //    String cityname = city.getText().toString();
        //    String areaname = area.getText().toString();
        //    addressPopWin.setCurrentAddress(proviceName, cityname, areaname);
        //}

        if( !province.isEmpty()) {
            addressPopWin.setCurrentAddress(province, city, area);
        }

        addressPopWin.showAtLocation(getWindow().getDecorView() , Gravity.BOTTOM, 0, 0);
        addressPopWin.setOnDismissListener(new PoponDismissListener(UserInfoActivity.this));
    }

}
