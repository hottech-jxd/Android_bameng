package com.bameng.fragment;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.model.PostModel;
import com.bameng.model.UserData;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.account.MyAccountActivity;
import com.bameng.ui.account.SettingActivity;
import com.bameng.ui.account.UserInfoActivity;
import com.bameng.ui.base.BaseFragment;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshHorizontalScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/***
 * 盟主 个人中心
 */
public class UserFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener{

    @Bind(R.id.homePullRefresh)
    PullToRefreshScrollView homePullRefresh;
    @Bind(R.id.img_user)
    SimpleDraweeView img_user;
    @Bind(R.id.txt_name)
    TextView txtName;
    @Bind(R.id.txt_Points)
    TextView txtPoints;
    @Bind(R.id.img_setting)
    ImageView imgSetting;
    @Bind(R.id.layaccount)
    LinearLayout layaccount;
    @Bind(R.id.txt_mbean)
    TextView txtMbean;
    @Bind(R.id.txt_nosettlembean)
    TextView txtNosettlembean;
    @Bind(R.id.txt_integral)
    TextView txtIntegral;
    @Bind(R.id.laysign)
    LinearLayout laysign;
    @Bind(R.id.layuserinfo)
    LinearLayout layuserinfo;
    @Bind(R.id.laycode)
    LinearLayout laycode;
    UserData userData;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userData = new UserData();
        initView();
        ButterKnife.bind(this, view);
    }


    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        initData();
    }

    public void initView() {

        userData= application.readUserInfo();

        if(userData!=null) {
            img_user.setImageURI(userData.getUserHeadImg());
            txtName.setText(userData.getNickName());
            txtPoints.setText(userData.getLevelName());
            txtMbean.setText(String.valueOf(userData.getMengBeans()));
            txtNosettlembean.setText(String.valueOf(userData.getMengBeansLocked()));
            txtIntegral.setText(String.valueOf(userData.getScore()));
        }

        homePullRefresh.setOnRefreshListener(this);

        initData();

    }

    public void initData(){
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        String token = BaseApplication.readToken();
        Call<PostModel> call = apiService.myinfo(token, map);
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {

                homePullRefresh.onRefreshComplete();

                if(response.code() !=200){
                    ToastUtils.showLongToast(response.message());
                    return;
                }
                if (response.body() != null) {

                    if (response.body().getStatus() == 200 && response.body().getData() != null&&response.body().getData().size()!=0) {


                    } else if (response.body().getStatus()==70035){

                        ToastUtils.showLongToast(response.body().getStatusText());
                    }

                } else {
                    ToastUtils.showLongToast("连接服务器失败！！！");
                }
                return;


            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                homePullRefresh.onRefreshComplete();

                ToastUtils.showLongToast("失败");
            }
        });
    }
    @Override
    public void onReshow() {

    }

    @Override
    public void onFragPasue() {

    }

    @Override
    public void onClick(View view) {

    }

    @OnClick({R.id.img_setting,R.id.img_user,R.id.layuserinfo,R.id.layaccount})
    void onclick(View view) {
        switch (view.getId()) {
            case R.id.layaccount:
                ActivityUtils.getInstance().showActivity(getActivity(), MyAccountActivity.class);
                break;
            case R.id.img_setting:
                ActivityUtils.getInstance().showActivity(getActivity(), SettingActivity.class);
                break;
            case R.id.layuserinfo:
            case R.id.img_user:
                ActivityUtils.getInstance().showActivity(getActivity(), UserInfoActivity.class);
                break;
            default:
                break;
        }

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_user;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
