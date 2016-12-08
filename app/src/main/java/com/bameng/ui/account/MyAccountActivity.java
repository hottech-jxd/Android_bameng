package com.bameng.ui.account;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.SystemTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bameng.R.id.swipeRefreshLayout;

/***
 * 我的账号
 */
public class MyAccountActivity extends BaseActivity {
    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
    @BindView(R2.id.txt_mbean)
    TextView txtMbean;
    @BindView(R2.id.txt_jifen)
    TextView txtJifen;
    @BindView(R2.id.txt_countbean)
    TextView txtCountbean;
    final static int REQUEST_CODE_CONFIRM = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        ButterKnife.bind(this);

        titleText.setText("我的财富");
        //Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        //SystemTools.loadBackground(titleLeftImage, leftDraw);
        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);

        initView();
        StartApi();

    }

    @OnClick({R.id.btn_checkout,R.id.layMbean,R.id.layjifen,R.id.laydjsmd})
    void onclick(View view) {
        switch (view.getId()){
            case R.id.btn_checkout://兑换
                ActivityUtils.getInstance().showActivityForResult(MyAccountActivity.this, REQUEST_CODE_CONFIRM , ExchangeConfirmActivity.class);
                break;
            case R.id.layMbean://
                ActivityUtils.getInstance().showActivity(MyAccountActivity.this,MyBeanActivity.class);
                break;
            case R.id.layjifen:
                ActivityUtils.getInstance().showActivity(MyAccountActivity.this,ScoreActivity.class);
                break;
            case R.id.laydjsmd:
                ActivityUtils.getInstance().showActivity(MyAccountActivity.this,MDouCountActivity.class);
                break;
            default:
                break;
        }


    }

    @Override
    protected void initView() {


        txtMbean.setText(String.valueOf(BaseApplication.UserData().getMengBeans()));
        txtCountbean.setText(String.valueOf(BaseApplication.UserData().getTempMengBeans()));
        txtJifen.setText(String.valueOf(BaseApplication.UserData().getScore()));

    }

    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode != RESULT_OK)return;
        if( requestCode==REQUEST_CODE_CONFIRM){
            initView();
        }
    }
}
