package com.bameng.ui.account;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.adapter.MBeanFlowAdapter;
import com.bameng.config.Constants;
import com.bameng.model.CloseEvent;
import com.bameng.model.ConvertBeanTotalOutputModel;
import com.bameng.model.PostModel;
import com.bameng.model.RefreshUserDataEvent;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.bameng.utils.Util;
import com.huotu.android.library.libedittext.EditText;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/***
 * 兑换确认 界面
 */
public class ExchangeConfirmActivity extends BaseActivity {

    @BindView(R2.id.tv_record)
    TextView tv_record;
    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.etBean)
    EditText etBean;
    @BindView(R2.id.txt_income)
    TextView tvIn;
    @BindView(R2.id.txt_outbean)
    TextView tvOut;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_confirm);
        ButterKnife.bind(this);
        initView();
        StartApi();
    }

    @Override
    protected void initView() {
        titleText.setText("兑换确认");
        //Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        //SystemTools.loadBackground(titleLeftImage, leftDraw);
        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);

        tvIn.setText( String.valueOf( BaseApplication.UserData().getMengBeans()));
        //tvOut.setText( BaseApplication.UserData().get );


    }
    @OnClick(R.id.tv_record) void ontvrecord()  {
        ActivityUtils.getInstance().showActivity(ExchangeConfirmActivity.this,ExchangeRecordActivity.class);

    }

    @OnClick(R.id.btnCash) void oncash()  {
        String beanStr = etBean.getText().toString();
        if(beanStr.isEmpty()){
            etBean.setError("请输入兑换盟豆数量");
            return;
        }

        if( !Util.isPositiveInt(beanStr)){
            etBean.setError("请输入正确的兑换数量");
            return;
        }

        int bean = Integer.parseInt( beanStr );
        cash(bean);
    }

    @Override
    protected void StartApi( ) {
        if(progressDialog==null){
            progressDialog=new ProgressDialog(this);
        }
        progressDialog.setMessage("正在获取数据...");
        progressDialog.show();

        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<ConvertBeanTotalOutputModel> call = apiService.AlreadyConvertTotal(token,map);
        call.enqueue(new Callback<ConvertBeanTotalOutputModel>() {
            @Override
            public void onResponse(Call<ConvertBeanTotalOutputModel> call, Response<ConvertBeanTotalOutputModel> response) {
                if(progressDialog!=null) progressDialog.dismiss();
                if(response.code()!=200 || response.body()==null){
                    ToastUtils.showLongToast( response.message()==null ? "请求发生错误": response.message());
                    return;
                }

                if(response.body().getStatus() == Constants.STATUS_70035){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(ExchangeConfirmActivity.this , PhoneLoginActivity.class);
                    return;
                }

                if( response.body().getStatus()!=200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }
                if(response.body().getData() == null ){
                    ToastUtils.showLongToast("服务器发送错误");
                    return;
                }

                tvIn.setText( String.valueOf( response.body().getData().getMengBeansCount()));
                tvOut.setText( String.valueOf( response.body().getData().getAlreadyConverCount() ));
            }

            @Override
            public void onFailure(Call<ConvertBeanTotalOutputModel> call, Throwable t) {
                if(progressDialog!=null) progressDialog.dismiss();
                ToastUtils.showLongToast( Constants.SERVER_ERROR );
            }
        });
    }

    protected void cash(final int beam ) {
        if(progressDialog==null){
            progressDialog=new ProgressDialog(this);
        }
        progressDialog.setMessage("兑换中");
        progressDialog.show();

        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("amount", String.valueOf( beam ));
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<PostModel> call = apiService.ConvertToBean(token,map);

        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if(progressDialog!=null) progressDialog.dismiss();
                if(response.code()!=200 || response.body()==null){
                    ToastUtils.showLongToast( response.message()==null ? "请求发生错误": response.message());
                    return;
                }

                if(response.body().getStatus() == Constants.STATUS_70035){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(ExchangeConfirmActivity.this , PhoneLoginActivity.class);
                    return;
                }

                if( response.body().getStatus()!=200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }

                BaseApplication.UserData().setMengBeans( BaseApplication.UserData().getMengBeans() - beam );
                BaseApplication.writeUserInfo( BaseApplication.UserData() );
                //ToastUtils.showShortToast(response.body().getStatusText());
                EventBus.getDefault().post(new RefreshUserDataEvent(BaseApplication.UserData() ));
                ExchangeConfirmActivity.this.setResult(RESULT_OK);
                ExchangeConfirmActivity.this.finish();
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                if(progressDialog!=null) progressDialog.dismiss();
                ToastUtils.showLongToast( Constants.SERVER_ERROR );
            }
        });

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
