package com.bameng.ui.business;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.adapter.MengAdapter;
import com.bameng.fragment.MengFragment;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.DensityUtils;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/***
 * 我的联盟 界面
 */
public class MyAlliesActivity extends BaseActivity {

    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
    @BindView(R2.id.tablayout)
    TabLayout tabLayout;
    @BindView(R2.id.viewerPager)
    ViewPager viewPager;
    MengAdapter mengAdapter;
    List<MengFragment> fragmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_allies);
        ButterKnife.bind(this);
        initView();
        StartApi();
    }

    @Override
    protected void initView() {
        titleText.setText("我的联盟");
        //Drawable leftDraw = ContextCompat.getDrawable(this, R.mipmap.ic_back);
        //SystemTools.loadBackground(titleLeftImage, leftDraw);
        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);

        int minW = DensityUtils.getScreenH(this) / 2 - 20;
        //tabLayout.setMinimumWidth(maxWidth);
        //tabLayout.getTabAt(0).getCustomView().setMinimumWidth(minW);
        //tabLayout.getTabAt(1).getCustomView().setMinimumWidth(minW);

        fragmentList = new ArrayList<>();
        Bundle bd = new Bundle();
        bd.putInt("type", 0);//0盟友申请，1盟友列表
        MengFragment fragment = new MengFragment();
        fragment.setArguments(bd);
        fragmentList.add(fragment);
        bd = new Bundle();
        bd.putInt("type", 1);
        fragment = new MengFragment();
        fragment.setArguments(bd);
        fragmentList.add(fragment);

        mengAdapter = new MengAdapter(getSupportFragmentManager(), fragmentList);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.setAdapter(mengAdapter);
        tabLayout.setupWithViewPager(viewPager);

        int count =tabLayout.getTabCount();
        for(int i=0;i<count;i++){
            //tabLayout.getTabAt(i).getCustomView().getp
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
