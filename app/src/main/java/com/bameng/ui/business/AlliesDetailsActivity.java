package com.bameng.ui.business;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.config.Constants;
import com.bameng.model.CloseEvent;
import com.bameng.model.MengModel;
import com.bameng.model.MengOutputModel;
import com.bameng.model.PostModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.UserInfoView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.baidu.location.h.j.t;
import static com.bameng.R.id.status;
import static com.bameng.R.id.txt_name;

/***
 * 盟友详情 界面
 */
public class AlliesDetailsActivity extends BaseActivity {

    @BindView(R2.id.txt_user)
    TextView txtUser;
    @BindView(R2.id.txt_name)
    TextView txtName;
    @BindView(R2.id.txt_realname)
    TextView txtRealname;
    @BindView(R2.id.txt_rank)
    TextView txtRank;
    @BindView(R2.id.txt_sex)
    TextView txtSex;
    @BindView(R2.id.txt_submitnum)
    TextView txtSubmitnum;
    @BindView(R2.id.txt_doneorder)
    TextView txtDoneorder;
    @BindView(R2.id.txt_phone)
    TextView txtPhone;
    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.txt_time)
    TextView txtTime;
    @BindView(R2.id.image)
    com.facebook.drawee.view.SimpleDraweeView image;

    ProgressDialog progressDialog;

    int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allies_details);
        ButterKnife.bind(this);
        initView();
        StartApi();
    }

    @Override
    protected void initView() {
        titleText.setText("盟友详情");
        //Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        //SystemTools.loadBackground(titleLeftImage, leftDraw);
        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);
    }

    @Override
    protected void StartApi() {

        if(progressDialog==null){
            progressDialog=new ProgressDialog(this);
        }
        progressDialog.show();

        userid = getIntent().getIntExtra("userid",0);

        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("userid", String.valueOf( userid  ) );
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<MengOutputModel> call = apiService.AllyInfo(token, map);
        call.enqueue(new Callback<MengOutputModel>() {
            @Override
            public void onResponse(Call<MengOutputModel> call, Response<MengOutputModel> response) {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                if( response.code() !=200){
                    ToastUtils.showLongToast( response.message() );
                    return;
                }
                if(response.body()==null){
                    ToastUtils.showLongToast("返回数据空");
                    return;
                }
                if (response.body().getStatus() == Constants.STATUS_70035) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(AlliesDetailsActivity.this, PhoneLoginActivity.class);
                    return;
                }

                if(response.body().getStatus() != 200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }
                if(response.body().getData()==null){
                    ToastUtils.showLongToast("返回数据空");
                    return;
                }

                MengModel model = response.body().getData();
                txtUser.setText( model.getLoginName() );
                txtName.setText(model.getNickName());
                txtRealname.setText(model.getRealName());
                txtRank.setText( model.getLevelName());
                txtSex.setText(model.getUserGender().equals("M")?"男": model.getUserGender().equals("F")?"女":model.getUserGender());
                txtSubmitnum.setText( String.valueOf( model.getCustomerAmount() ));
                txtDoneorder.setText( String.valueOf( model.getOrderSuccessAmount() ) );
                txtPhone.setText(model.getUserMobile());
                txtTime.setText( model.getCreateTime() );
                image.setImageURI(model.getUserHeadImg());
            }

            @Override
            public void onFailure(Call<MengOutputModel> call, Throwable t) {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                ToastUtils.showLongToast("请求失败");
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
