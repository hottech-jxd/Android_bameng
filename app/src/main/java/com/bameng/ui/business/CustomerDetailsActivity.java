package com.bameng.ui.business;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.model.CustomerModel;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.SystemTools;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bameng.R.id.btnSubmit;
import static com.bameng.R.id.layShopStatus;
import static com.bameng.R.id.start;

/***
 * 客户详情界面
 */
public class CustomerDetailsActivity extends BaseActivity {

    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
    public Resources resources;

    @BindView(R2.id.name) TextView name;
    @BindView(R2.id.moblie) TextView moblie;
    @BindView(R2.id.address) TextView address;
    @BindView(R2.id.status) TextView status;
    @BindView(R2.id.remark) TextView remark;
    @BindView(R2.id.inShopStatus) TextView shopStatus;
    @BindView(R.id.belongone) TextView belongone;

    Bundle bundle;
    CustomerModel customerModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        ButterKnife.bind(this);
        initView();
        application = (BaseApplication) this.getApplication();
        resources = this.getResources();
        StartApi();
    }

    @Override
    protected void initView() {
        titleText.setText("客户信息详情");
        titleLeftImage.setVisibility(View.VISIBLE);
        //Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        //SystemTools.loadBackground(titleLeftImage, leftDraw);
        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);

        bundle = this.getIntent().getExtras();
        customerModel= (CustomerModel) bundle.getSerializable("customerinfo");

        name.setText(customerModel.getName());
        moblie.setText(customerModel.getMobile());
        address.setText(customerModel.getAddr());
        shopStatus.setText( customerModel.getInShop() ==1 ? "已进店":"未进店" );
        remark.setText(TextUtils.isEmpty(customerModel.getRemark())? "无":customerModel.getRemark() );
        belongone.setText( customerModel.getBelongOneName() );

        if (customerModel.getStatus()==0) {
            status.setText("未审核");
        }else if(customerModel.getStatus()==1){
            status.setText("已同意");
        }else if(customerModel.getStatus() == 2){
            status.setText("已拒绝");
        }
    }

    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
