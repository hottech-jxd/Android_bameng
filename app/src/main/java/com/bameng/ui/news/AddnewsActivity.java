package com.bameng.ui.news;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.config.Constants;
import com.bameng.model.CloseEvent;
import com.bameng.model.CustomListOutput;
import com.bameng.model.CustomerModel;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.PostModel;
import com.bameng.model.UserData;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.huotu.android.library.libedittext.EditText;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.baidu.location.d.j.G;
import static com.bameng.R.id.edtName;
import static com.bameng.R.id.edtTitle;
import static com.bameng.R.id.start;
import static com.bameng.R.id.swipeRefreshLayout;

public class AddnewsActivity extends BaseActivity {
    public final  static int REQUEST_CODE_CHOOSE= 100;

    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
    @BindView(R2.id.titleText)
    TextView titleText;

    @BindView(R2.id.tvObject)
            TextView tvObject;

    @BindView(R2.id.edtTitle)
    EditText etTitle;
    @BindView(R2.id.edtContent)
    EditText etContent;

    @BindView(R2.id.chooseObjectL)
    LinearLayout llChoose;
    @BindView(R.id.layComment)
    LinearLayout layComment;

    List<UserData> objects;

    ProgressDialog progressDialog;
    //标记是否 留言
    boolean comment=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        ButterKnife.bind(this);
        initView();
    }

    public  int getLayoutRes(){
        return R.layout.activity_addnews;
    }


    @Override
    protected void initView() {

        titleText.setText("新增消息");
        String title = getIntent().getStringExtra("title");
        if(title!=null && !title.isEmpty()){
            titleText.setText(title);
        }
        String hintTitle = getIntent().getStringExtra("hintTitle");
        if( hintTitle!=null && !hintTitle.isEmpty()){
            etTitle.setHint(hintTitle);
        }
        String hintContent = getIntent().getStringExtra("hintContent");
        if( hintContent!=null && !hintContent.isEmpty()){
            etContent.setHint(hintContent);
        }


        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);

        if(BaseApplication.UserData().getUserIdentity() == 1){
            llChoose.setVisibility(View.VISIBLE);
        }else{
            llChoose.setVisibility(View.GONE);
        }

        comment = getIntent().getBooleanExtra("comment",false);
        if(comment){
            llChoose.setVisibility(View.GONE);
            layComment.setVisibility(View.VISIBLE);
        }else{
            layComment.setVisibility(View.GONE);
        }
    }

    @Override
    protected void StartApi() {
        String title= etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        String ids = "";

        if( objects !=null) {
            for (UserData item : objects) {
                if (!ids.isEmpty()) {
                    ids += "|";
                }
                ids += item.getUserId();
            }
        }

        if(comment){//如果是留言，则传递ids参数=-1
            ids="-1";
        }

        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("title",  title );
        map.put("content", content );
        map.put("ids", ids );
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<PostModel> call = apiService.articlecreate( token,map);
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
               if(progressDialog !=null) progressDialog.dismiss();

                if(response.code()!=200){
                    ToastUtils.showLongToast(response.message());
                    return;
                }
                if(response.body()==null){
                    ToastUtils.showLongToast("服务器出问题了");
                    return;
                }
                if (response.body().getStatus() == Constants.STATUS_70035) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(AddnewsActivity.this , PhoneLoginActivity.class);
                    return;
                }

                if (response.body() != null) {
                    if (response.body().getStatus() == 200 ) {
                        ToastUtils.showLongToast(response.body().getStatusText());
                        AddnewsActivity.this.setResult(RESULT_OK);
                        AddnewsActivity.this.finish();
                    } else {
                        ToastUtils.showLongToast(response.body().getStatusText());
                    }
                }

                return;

            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                if(progressDialog!=null) progressDialog.dismiss();
                ToastUtils.showLongToast("失败");
            }
        });

    }

    @OnClick(R.id.chooseObjectL)
    void choose(){
        Bundle bd = new Bundle();
        bd.putSerializable("customer", (Serializable) objects);
        ActivityUtils.getInstance().showActivityForResult(AddnewsActivity.this, REQUEST_CODE_CHOOSE , ChooseObjectActivity.class , bd );
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode != RESULT_OK )return;

        if( requestCode == REQUEST_CODE_CHOOSE){
            tvObject.setText("");
            objects = (List<UserData>)data.getExtras().getSerializable("customer");
            if(objects==null) return;
            String txt = "";
            for(UserData item : objects){
                if( !txt.isEmpty()){
                    txt+=",";
                }
                txt += item.getRealName();
            }
            tvObject.setText(txt);
        }

    }

    @OnClick(R.id.btnSend)
    void onSend(){
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        if(title.isEmpty()){
            etTitle.setError("请输入标题");
            return;
        }
        if(content.isEmpty()){
            etContent.setError("请输入内容");
            return;
        }

        if( !comment && BaseApplication.UserData().getUserIdentity() ==1 ) {
            if (objects == null || objects.size() < 1) {
                tvObject.setError("请选择发送对象");
                return;
            }
        }

        if(progressDialog==null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("发送中...");
        }
        progressDialog.show();

        StartApi();
    }
}
