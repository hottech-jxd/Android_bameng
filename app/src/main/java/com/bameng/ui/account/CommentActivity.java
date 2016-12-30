package com.bameng.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.R2;
import com.bameng.adapter.TabPagerAdapter;
import com.bameng.fragment.MsgFrag;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.base.BaseFragment;
import com.bameng.ui.news.AddnewsActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * 我的留言
 */
public class CommentActivity extends BaseActivity {
    final static int REQUEST_CODE_REFRESH=100;

    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
    @BindView(R2.id.titleRightImage)
    ImageView titleRightImage;
    @BindView(R.id.titleRightText)
    TextView titleRightText;
    @BindView(R.id.layTitleRight)
    LinearLayout layTitleRight;

    @BindView(R2.id.customViewPager)
    ViewPager customViewPager;
    TabPagerAdapter tabPagerAdapter;
    List<BaseFragment> mFragmentList = new ArrayList<>();
    String title="我的留言";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void initView() {
        titleText.setText(title);

        titleRightImage.setImageResource(R.mipmap.ic_newadd);
        titleRightText.setText("新增");
        int leftPx = DensityUtils.dip2px(this,5);
        titleRightText.setPadding( leftPx , titleRightText.getPaddingTop() , titleRightText.getPaddingRight(),titleRightText.getPaddingBottom() );
        layTitleRight.setBackgroundResource(R.drawable.item_click_selector);


        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);

        MsgFrag sendFrag =new MsgFrag();
        Bundle bd = new Bundle();
        bd.putInt("type",2);
        bd.putInt("sendtype",1);
        sendFrag.setArguments(bd);
        mFragmentList.add(sendFrag);
        tabPagerAdapter=new TabPagerAdapter(this, getSupportFragmentManager(), mFragmentList);
        customViewPager.setAdapter(tabPagerAdapter);
    }

    @OnClick(R.id.layTitleRight)
    void onClick(View v){
        Intent intent = new Intent();
        intent.setClass(this, AddnewsActivity.class);
        intent.putExtra("title","新增留言");
        intent.putExtra("comment",true);
        intent.putExtra("hintTitle","请输入留言标题");
        intent.putExtra("hintContext","请输入留言内容");

        ActivityUtils.getInstance().showActivityForResult(this, REQUEST_CODE_REFRESH ,intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) return;
        if( requestCode== REQUEST_CODE_REFRESH){
            ((MsgFrag)mFragmentList.get(0)).loadData();
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
