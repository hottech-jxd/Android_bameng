package com.bameng.ui.business;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.text.util.LinkifyCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.model.CustomerModel;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.DensityUtils;
import com.bameng.utils.SystemTools;
import com.bameng.widgets.custom.FrescoControllerListener;
import com.bameng.widgets.custom.FrescoDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bameng.R.id.btnSubmit;
import static com.bameng.R.id.layShopStatus;
import static com.bameng.R.id.laysign;
import static com.bameng.R.id.start;

/***
 * 客户详情界面
 */
public class CustomerDetailsActivity extends BaseActivity implements FrescoControllerListener.ImageCallback{

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

    @BindView(R.id.layPicture)
    LinearLayout layPicture;
    @BindView(R.id.ivPicture)
    SimpleDraweeView ivPicture;
    @BindView(R.id.laySecond) LinearLayout laySecond;

    //@BindView(R.id.viewMengYou)
     //       View viewMengYou;
    @BindView(R.id.layMengyou)
            LinearLayout layMengyou;
    @BindView(R.id.viewStatus)
            View viewStatus;
    @BindView(R.id.layStatus)
            LinearLayout layStatus;
    @BindView(R.id.viewInShop)
            View viewInShop;
    @BindView(R.id.layInShop)
            LinearLayout layInShop;

    @BindView(R.id.lbPicture)
            TextView lbPicture;

    Bundle bundle;
    CustomerModel customerModel;

    boolean showStatus=true;
    boolean showInShop=true;
    boolean showMengYou=true;

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

        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);

        bundle = this.getIntent().getExtras();
        customerModel= (CustomerModel) bundle.getSerializable("customerinfo");

        showInShop = bundle.getBoolean("showInShop",true);
        showMengYou = bundle.getBoolean("showMengYou",true);
        showStatus = bundle.getBoolean("showStatus",true);


        if(customerModel.getIsSave()==1){
            laySecond.setVisibility(View.GONE);
            layPicture.setVisibility(View.VISIBLE);
            int width = DensityUtils.getScreenW(this);
            int lwidth = lbPicture.getWidth();
            int sw = DensityUtils.dip2px(this, 80);
            width = width- sw;
            FrescoDraweeController.loadImage(ivPicture , width, customerModel.getDataImg(),0, this );
        }else {
            laySecond.setVisibility(View.VISIBLE);
            layPicture.setVisibility(View.GONE);

            name.setText(customerModel.getName());
            moblie.setText(customerModel.getMobile());
            address.setText(customerModel.getAddr());
        }

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

        if(!showStatus){
            viewStatus.setVisibility(View.GONE);
            layStatus.setVisibility(View.GONE);
        }
        if(!showMengYou){
           // viewMengYou.setVisibility(View.GONE);
            layMengyou.setVisibility(View.GONE);
        }
        if(!showInShop){
            viewInShop.setVisibility(View.GONE);
            layInShop.setVisibility(View.GONE);
        }
    }

    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void imageCallback(int position, int width, int height) {
        if (ivPicture == null) return;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        ivPicture.setLayoutParams(layoutParams);
    }
}
