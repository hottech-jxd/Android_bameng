package com.bameng.ui.business;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.config.Constants;
import com.bameng.model.BaseModel;
import com.bameng.model.CloseEvent;
import com.bameng.model.MengModel;
import com.bameng.model.MengOutputModel;
import com.bameng.model.PostModel;
import com.bameng.model.RefreshMengYouEvent;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.TipAlertDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static u.aly.av.n;

/***
 * 盟友审核 界面
 */
public class AlliesExamineActivity extends BaseActivity {

    @BindView(R2.id.txt_realname)
    TextView txtRealname;
    @BindView(R2.id.txt_rank)
    TextView txtRank;
    @BindView(R2.id.txt_sex)
    TextView txtSex;
    @BindView(R2.id.txt_phone)
    TextView txtPhone;
    @BindView(R2.id.txt_time)
    TextView txtTime;
    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;

    MengModel model;

    ProgressDialog progressDialog;

    TipAlertDialog tipAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allies_examine);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void initView() {
        titleText.setText("盟友审核详情");
        //Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        //SystemTools.loadBackground(titleLeftImage, leftDraw);
        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);

        model = (MengModel) getIntent().getSerializableExtra("model");
        if(model==null)return;

        txtRealname.setText( model.getNickName() );
        txtSex.setText( model.getUserGender().equals("M")?"男": model.getUserGender().equals("F") ? "女": model.getUserGender() );
        txtPhone.setText( model.getMobile() );
        txtTime.setText( model.getCreateTime());
    }

    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @OnClick({R.id.tvAgress})
    void agress(View v ){
        if(tipAlertDialog==null) tipAlertDialog = new TipAlertDialog(this,false);
        tipAlertDialog.show("审核提醒", "确定要同意申请吗？", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( tipAlertDialog!=null) tipAlertDialog.dismiss();
                audit( model.getID() , 1 );
            }
        });
    }

    @OnClick(R.id.tvReject)
    void reject(View v){
        if(tipAlertDialog==null) tipAlertDialog = new TipAlertDialog(this,false);
        tipAlertDialog.show("审核提醒", "确定要拒绝申请吗？", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tipAlertDialog!=null) tipAlertDialog.dismiss();

                audit( model.getID() , 2 );
            }
        });
    }

    protected void audit( int id , int status ){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(this);
        }
        progressDialog.show();


        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("id", String.valueOf( id  ) );
        map.put("status", String.valueOf( status ));
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<BaseModel> call = apiService.AllyApplyAudit(token, map);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
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
                    ActivityUtils.getInstance().skipActivity( AlliesExamineActivity.this , PhoneLoginActivity.class);
                    return;
                }

                if(response.body().getStatus() != 200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }
                ToastUtils.showLongToast(response.body().getStatusText());
                AlliesExamineActivity.this.finish();

                EventBus.getDefault().post(new RefreshMengYouEvent(0));
                EventBus.getDefault().post(new RefreshMengYouEvent(1));
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                ToastUtils.showLongToast("请求失败");
            }
        });

    }

}
