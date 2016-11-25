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
import com.bameng.model.BaseModel;
import com.bameng.model.MengModel;
import com.bameng.model.MengOutputModel;
import com.bameng.model.PostModel;
import com.bameng.model.RefreshMengYouEvent;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/***
 * 盟友审核 界面
 */
public class AlliesExamineActivity extends BaseActivity {

    @Bind(R.id.txt_realname)
    TextView txtRealname;
    @Bind(R.id.txt_rank)
    TextView txtRank;
    @Bind(R.id.txt_sex)
    TextView txtSex;
    @Bind(R.id.txt_phone)
    TextView txtPhone;
    @Bind(R.id.txt_time)
    TextView txtTime;
    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;

    MengModel model;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allies_examine);
        ButterKnife.bind(this);
        initView();
        //StartApi();
    }

    @Override
    protected void initView() {
        titleText.setText("盟友审核详情");
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);

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
        audit( model.getID() , 1 );
    }

    @OnClick(R.id.tvReject)
    void reject(View v){
        audit(model.getID() , 2 );
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
