package com.bameng.ui.business;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.model.ArticleListOutput;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.PostModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.huotu.android.library.libedittext.EditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmitCustomerInfoActivity extends BaseActivity {

    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    public Resources resources;
    @Bind(R.id.customName)
    EditText customName;
    @Bind(R.id.customMoblie)
    EditText customMoblie;
    @Bind(R.id.customAddress)
    EditText customAddress;
    @Bind(R.id.remark)
    EditText remark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_customer_info);
        ButterKnife.bind(this);
        initView();
        application = (BaseApplication) this.getApplication();
        resources = this.getResources();
        StartApi();
    }

    @Override
    protected void initView() {
        titleText.setText("提交客户信息");
        titleLeftImage.setVisibility(View.VISIBLE);
        Drawable leftDraw = ContextCompat.getDrawable(this, R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);
    }

    @OnClick(R.id.btn_submit)
    void submit(){
        Map<String, String> map = new HashMap<>();
        map.put("version", application.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("username",customName.getText().toString());
        map.put("mobile",customMoblie.getText().toString());
        map.put("address",customAddress.getText().toString());
        map.put("remark",remark.getText().toString());
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        String token = application.readToken();
        Call<PostModel> call = apiService.create(token,map);
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if (response.body() != null) {

//                    ArticleListOutput articleListOutput = new ArticleListOutput();
//                    articleListOutput.setData(response.body().getData());
                    if (response.body().getStatus() == 200&&response.body()!=null) {
                        ToastUtils.showLongToast("提交成功");
                        finish();
//
                    } else {
                        ToastUtils.showLongToast(response.body().getStatusText());
                    }

                }

                return;



            }


            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                ToastUtils.showLongToast("失败");
            }
        });

    }
    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
