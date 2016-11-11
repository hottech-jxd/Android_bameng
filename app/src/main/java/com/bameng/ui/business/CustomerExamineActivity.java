package com.bameng.ui.business;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.model.CustomerModel;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.SystemTools;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CustomerExamineActivity extends BaseActivity {

    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.name) TextView name;
    @Bind(R.id.moblie)
    TextView moblie;
    @Bind(R.id.address) TextView address;
    @Bind(R.id.status) TextView status;
    @Bind(R.id.remark) TextView remark;
    @Bind(R.id.layBtn) LinearLayout laybtn;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_examine);
        ButterKnife.bind(this);
        initView();
        StartApi();
    }

    @Override
    protected void initView() {
        titleText.setText("客户意向审核");
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);
        CustomerModel customerModel=new CustomerModel();
        bundle = this.getIntent().getExtras();
        customerModel= (CustomerModel) bundle.getSerializable("customerinfo");
        name.setText(customerModel.getName());
        moblie.setText(customerModel.getMobile());
        address.setText(customerModel.getAddr());
        if (customerModel.getStatus()==0){
            laybtn.setVisibility(View.VISIBLE);
            status.setText("审核中");
        }else if (customerModel.getStatus()==1){
            laybtn.setVisibility(View.GONE);
            status.setText("已同意");
        }else {
            laybtn.setVisibility(View.GONE);
            status.setText("已拒绝");
        }
        remark.setText(TextUtils.isEmpty(customerModel.getRemark())? "无":customerModel.getRemark() );

    }

    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
