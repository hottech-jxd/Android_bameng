package com.bameng.ui.account;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.SystemTools;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * 我的账号
 */
public class MyAccountActivity extends BaseActivity {

    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    @Bind(R.id.txt_mbean)
    TextView txtMbean;
    @Bind(R.id.txt_jifen)
    TextView txtJifen;
    @Bind(R.id.txt_countbean)
    TextView txtCountbean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        ButterKnife.bind(this);
        initView();
        StartApi();

    }

    @OnClick({R.id.btn_checkout,R.id.layMbean,R.id.layjifen,R.id.laydjsmd})
    void onclick(View view) {
        switch (view.getId()){
            case R.id.btn_checkout://兑换
                ActivityUtils.getInstance().showActivity(MyAccountActivity.this,ExchangeBeanActivity.class);
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
        titleText.setText("我的账号");
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);

        txtMbean.setText(String.valueOf(BaseApplication.UserData().getMengBeans()));
        txtCountbean.setText(String.valueOf(BaseApplication.UserData().getMengBeansLocked()));
        txtJifen.setText(String.valueOf(BaseApplication.UserData().getScore()));
    }

    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
