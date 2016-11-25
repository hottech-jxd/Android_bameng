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
import com.bameng.adapter.MBeanFlowAdapter;
import com.bameng.model.PostModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.bameng.utils.Util;
import com.huotu.android.library.libedittext.EditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.recognitionService;
import static android.R.attr.type;
import static com.baidu.location.h.j.B;

/***
 * 兑换确认 界面
 */
public class ExchangeConfirmActivity extends BaseActivity {

    @Bind(R.id.tv_record)
    TextView tv_record;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.etBean)
    EditText etBean;
    @Bind(R.id.txt_income)
    TextView tvIn;
    @Bind(R.id.txt_outbean)
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
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);

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
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
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
                if( response.body().getStatus()!=200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }

                BaseApplication.UserData().setMengBeans( BaseApplication.UserData().getMengBeans() - beam );
                ToastUtils.showShortToast(response.body().getStatusText());
                ExchangeConfirmActivity.this.finish();
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                if(progressDialog!=null) progressDialog.dismiss();
                ToastUtils.showLongToast( "请求失败"+ t.getMessage()==null ? "": t.getMessage() );
            }
        });

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
