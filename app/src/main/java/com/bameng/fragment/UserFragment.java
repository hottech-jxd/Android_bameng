package com.bameng.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.config.Constants;
import com.bameng.model.CloseEvent;
import com.bameng.model.RefreshUserDataEvent;
import com.bameng.model.SignOutputModel;
import com.bameng.model.UserData;
import com.bameng.model.UserOutputsModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.WebViewActivity;
import com.bameng.ui.account.MDouCountActivity;
import com.bameng.ui.account.MyAccountActivity;
import com.bameng.ui.account.MyBeanActivity;
import com.bameng.ui.account.ScoreActivity;
import com.bameng.ui.account.SettingActivity;
import com.bameng.ui.account.UserInfoActivity;
import com.bameng.ui.base.BaseFragment;
import com.bameng.ui.business.MyCashCardActivity;
import com.bameng.ui.business.OrderListActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.PreferenceHelper;
import com.bameng.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/***
 * 盟主 个人中心
 */
public class UserFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener , View.OnClickListener {

    @BindView(R2.id.homePullRefresh)
    SwipeRefreshLayout homePullRefresh;
    @BindView(R2.id.img_user)
    SimpleDraweeView img_user;
    @BindView(R2.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_belongone)
    TextView txtBelongOne;
    @BindView(R2.id.txt_Points)
    TextView txtPoints;
    @BindView(R2.id.img_setting)
    ImageView imgSetting;
    @BindView(R2.id.layaccount)
    LinearLayout layaccount;
    @BindView(R2.id.txt_mbean)
    TextView txtMbean;
    @BindView(R2.id.txt_nosettlembean)
    TextView txtNosettlembean;
    @BindView(R2.id.txt_integral)
    TextView txtIntegral;
    LinearLayout laysign;
    LinearLayout layuserinfo;
    LinearLayout laycode;
    LinearLayout laymyorder;
    LinearLayout laymycash;
    @BindView(R2.id.laymenus)
    LinearLayout laymenus;

    UserData userData;
    ProgressDialog progressDialog;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        homePullRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        initData();
    }

    public void initView() {

        userData= BaseApplication.readUserInfo();

        if(userData!=null) {
            img_user.setImageURI(userData.getUserHeadImg());
            txtName.setText(userData.getNickName());
            if( !userData.getBelongOneUserName().isEmpty()) {
                txtBelongOne.setText("盟主:"+userData.getBelongOneUserName());
                txtBelongOne.setVisibility(View.VISIBLE);
            }
            txtPoints.setText(userData.getLevelName());
            txtMbean.setText(String.valueOf(userData.getMengBeans()));
            txtNosettlembean.setText(String.valueOf(userData.getTempMengBeans()));
            txtIntegral.setText(String.valueOf(userData.getScore()));


            if( userData.getUserIdentity() == Constants.MENG_ZHU ){
               initMengZhu();
            }else{
              initAlly();
            }
        }
    }

    void initMengZhu(){
        View view = LayoutInflater.from(getContext()).inflate( R.layout.layout_meng_menu ,null );
        laymenus.removeAllViews();
        laymenus.addView( view );

        laysign = (LinearLayout) laymenus.findViewById(R.id.laysign);
        laysign.setOnClickListener(this);
        layuserinfo = (LinearLayout)laymenus.findViewById(R.id.layuserinfo);
        layuserinfo.setOnClickListener(this);
        laycode = (LinearLayout)laymenus.findViewById(R.id.laycode);
        laycode.setOnClickListener(this);
        if( BaseApplication.BaseData() !=null && BaseApplication.BaseData().getEnableSignIn() == 1 ){
            laysign.setVisibility(View.VISIBLE);
        }else{
            laysign.setVisibility(View.GONE);
        }
    }

    void initAlly(){
        View view = LayoutInflater.from(getContext()).inflate( R.layout.layout_ally_menu ,null );
        laymenus.removeAllViews();
        laymenus.addView( view );

        laymyorder = (LinearLayout) laymenus.findViewById(R.id.laymyorder);
        laymyorder.setOnClickListener(this);
        laymycash = (LinearLayout)laymenus.findViewById(R.id.laymycash);
        laymycash.setOnClickListener(this);
        laysign = (LinearLayout)laymenus.findViewById(R.id.laysign);
        laysign.setOnClickListener(this);
        layuserinfo = (LinearLayout)laymenus.findViewById(R.id.laymyuserinfo);
        layuserinfo.setOnClickListener(this);

        if( BaseApplication.BaseData() !=null && BaseApplication.BaseData().getEnableSignIn() == 1 ){
            laysign.setVisibility(View.VISIBLE);
        }else{
            laysign.setVisibility(View.GONE);
        }
    }

    public void initData(){
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<UserOutputsModel> call = apiService.myinfo(token, map);
        call.enqueue(new Callback<UserOutputsModel>() {
            @Override
            public void onResponse(Call<UserOutputsModel> call, Response<UserOutputsModel> response) {

                homePullRefresh.setRefreshing(false);

                if (response.code() != 200) {
                    ToastUtils.showLongToast(response.message());
                    return;
                }
                if (response.body() == null) {
                    ToastUtils.showLongToast("返回错误数据");
                    return;
                }
                if (response.body().getStatus() == Constants.STATUS_70035) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(getActivity(), PhoneLoginActivity.class);
                    return;
                }
                if (response.body().getStatus() != 200) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }

                if (response.body().getStatus() == 200 && response.body().getData() != null && response.body().getData() != null) {
                    BaseApplication.writeUserInfo(response.body().getData());
                    initView();
                } else if (response.body().getStatus() == 70035) {

                    ToastUtils.showLongToast(response.body().getStatusText());
                }
            }

            @Override
            public void onFailure(Call<UserOutputsModel> call, Throwable t) {
                homePullRefresh.setRefreshing(false);
                //ToastUtils.showLongToast(t.getMessage()==null?"请求发生异常":t.getMessage());
                ToastUtils.showLongToast(Constants.SERVER_ERROR);
            }
        });
    }
    @Override
    public void onReshow() {

    }

    @Override
    public void onFragPasue() {

    }



//    @OnClick({R.id.img_setting,R.id.img_user,R.id.layuserinfo,R.id.layaccount,
//            R.id.laycode,R.id.laysign , R.id.layBean,R.id.laywaitbean,R.id.layscore,R.id.layName})

    @OnClick({R.id.layaccount,R.id.img_setting,R.id.img_user,R.id.layBean,R.id.laywaitbean,R.id.layscore,R.id.layName})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layaccount:
                ActivityUtils.getInstance().showActivity(getActivity(), MyAccountActivity.class);
                break;
            case R.id.img_setting:
                ActivityUtils.getInstance().showActivity(getActivity(), SettingActivity.class);
                break;
            case R.id.laymyuserinfo:
            case R.id.layuserinfo:
                goto_one();
                break;
            case R.id.img_user:
            case R.id.layName:
                ActivityUtils.getInstance().showActivity(getActivity(), UserInfoActivity.class);
                break;
            case R.id.laycode:
                goto_two();
                break;
            case R.id.laysign:
                sign();
                break;
            case R.id.layBean:
                ActivityUtils.getInstance().showActivity(getActivity(),MyBeanActivity.class);
                break;
            case R.id.laywaitbean:
                ActivityUtils.getInstance().showActivity(getActivity(),MDouCountActivity.class);
                break;
            case R.id.layscore:
                ActivityUtils.getInstance().showActivity(getActivity(),ScoreActivity.class);
                break;
            case R.id.laymycash:
                ActivityUtils.getInstance().showActivity(getActivity(), MyCashCardActivity.class);
                break;
            case R.id.laymyorder:
                ActivityUtils.getInstance().showActivity(getActivity(), OrderListActivity.class);
                break;
            default:
                break;
        }

    }

    void goto_one(){
        //if(BaseApplication.UserData().getUserIdentity() == Constants.MENG_ZHU){
            ActivityUtils.getInstance().showActivity(getActivity(), UserInfoActivity.class );
        //}else{
        //    ActivityUtils.getInstance().showActivity(getActivity(), MyCashCardActivity.class);
        //}
    }

    void goto_two(){
        if(BaseApplication.UserData().getUserIdentity() == Constants.MENG_ZHU){
            Bundle bd = new Bundle();
            bd.putString(Constants.INTENT_URL, BaseApplication.UserData().getMyqrcodeUrl());
            ActivityUtils.getInstance().showActivity(getActivity(), WebViewActivity.class, bd );
        }else{
            ActivityUtils.getInstance().showActivity(getActivity(), OrderListActivity.class);
        }
    }

    private void sign(){

        //ToastUtils.showSign( "积分+10");


        if(progressDialog==null){
            progressDialog = new ProgressDialog(getContext());
        }
        progressDialog.setMessage("签到...");
        progressDialog.show();

        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        String address = PreferenceHelper.readString( BaseApplication.single ,Constants.LOCATION_INFO, Constants.ADDRESS,"");
        map.put("addr",  address );

        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<SignOutputModel> call = apiService.signin(token, map);
        call.enqueue(new Callback<SignOutputModel>() {
            @Override
            public void onResponse(Call<SignOutputModel> call, Response<SignOutputModel> response) {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                if( response.code() !=200){
                    ToastUtils.showLongToast( Constants.SERVER_ERROR );
                    return;
                }
                if(response.body()==null){
                    ToastUtils.showLongToast("返回数据空");
                    return;
                }
                if(response.body().getStatus() == Constants.STATUS_70035){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(getActivity() , PhoneLoginActivity.class);
                    return;
                }

                if(response.body().getStatus() != 200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }
                if( response.body().getData()==null){
                    ToastUtils.showLongToast("返回数据错误");
                    return;
                }

                BaseApplication.UserData().setScore( response.body().getData().getScore() +
                BaseApplication.UserData().getScore());

                txtIntegral.setText( String.valueOf( BaseApplication.UserData().getScore() ));

                //String score = "+" + String.valueOf( response.body().getData().getScore());

                ToastUtils.showSign( response.body().getStatusText() );
            }

            @Override
            public void onFailure(Call<SignOutputModel> call, Throwable t) {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                ToastUtils.showLongToast("请求失败");
            }
        });

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_user;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefreshUserInfo(RefreshUserDataEvent event){

        initView();
    }
}
