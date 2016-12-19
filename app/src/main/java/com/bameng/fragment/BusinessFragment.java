package com.bameng.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.config.Constants;
import com.bameng.model.BadgeBusinessEvent;
import com.bameng.model.CloseEvent;
import com.bameng.model.MyBusinessOutputModel;
import com.bameng.model.PostModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseFragment;
import com.bameng.ui.business.CustomerInfoActivity;
import com.bameng.ui.business.ExchangeExamineActivity;
import com.bameng.ui.business.MyAlliesActivity;
import com.bameng.ui.business.MyCashCardActivity;
import com.bameng.ui.business.OrderListActivity;
import com.bameng.ui.business.RwordActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 我的业务
 */
public class BusinessFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R2.id.txt_order)
    TextView txtOrder;
    @BindView(R2.id.txt_Customer)
    TextView txtCustomer;
    @BindView(R2.id.txt_exchange)
    TextView txtExchange;
    @BindView(R2.id.txt_cash)
    TextView txtCash;
    @BindView(R.id.txt_ally)
    TextView txtAlly;
    @BindView(R.id.circle_customer)
    View circle_customer;
    @BindView(R.id.circle_exchange)
    View circle_exchange;
    @BindView(R.id.circle_ally)
    View circle_ally;

    @BindView(R2.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    Unbinder unbinder;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    public void initView(){
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<MyBusinessOutputModel> call = apiService.MyBusiness(token,map);
        call.enqueue(new Callback<MyBusinessOutputModel>() {
            @Override
            public void onResponse(Call<MyBusinessOutputModel> call, Response<MyBusinessOutputModel> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.code() != 200) {
                    ToastUtils.showLongToast(response.message());
                    return;
                }
                if (response.body() == null) {
                    ToastUtils.showLongToast("服务器发生错误");
                    return;
                }
                if (response.body().getStatus() == Constants.STATUS_70035) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(getActivity(), PhoneLoginActivity.class);
                    return;
                }

                if (response.body().getStatus() == 200 && response.body().getData() != null) {
                    txtOrder.setText(String.valueOf(response.body().getData().getOrderAmount()));
                    txtCustomer.setText(String.valueOf(response.body().getData().getCustomerAmount()));
                    txtCash.setText(String.valueOf(response.body().getData().getCashCouponAmount()));
                    txtExchange.setText(String.valueOf(response.body().getData().getExchangeAmount()));
                    txtAlly.setText( String.valueOf(response.body().getData().getAllyApplyAmount()) );

                    circle_customer.setBackgroundResource( response.body().getData().getCustomerAmount()>0? R.drawable.circle_red:R.drawable.circle_white );
                    circle_exchange.setBackgroundResource(response.body().getData().getExchangeAmount()>0?R.drawable.circle_red :R.drawable.circle_white);
                    circle_ally.setBackgroundResource(response.body().getData().getAllyApplyAmount()>0?R.drawable.circle_red:R.drawable.circle_white);

                    boolean showCircle= response.body().getData().getCustomerAmount()>0
                            ||response.body().getData().getExchangeAmount()>0
                            || response.body().getData().getAllyApplyAmount()>0;

                    EventBus.getDefault().post(new BadgeBusinessEvent( showCircle ));

                } else {
                    ToastUtils.showLongToast(response.body().getStatusText());
                }
            }

            @Override
            public void onFailure(Call<MyBusinessOutputModel> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                //ToastUtils.showLongToast(t.getMessage()==null?"请求失败":t.getMessage());
                ToastUtils.showLongToast(Constants.SERVER_ERROR);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void loadData() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    @Override
    public void onReshow() {

    }

    @Override
    public void onFragPasue() {

    }

    @OnClick({R.id.layOrder,R.id.layCash,R.id.layCustomer,R.id.layExchange,R.id.layReward,R.id.layMylm})void onclick(View view){
        switch(view.getId()){
            case R.id.layOrder:
                ActivityUtils.getInstance().showActivity(getActivity(), OrderListActivity.class);
                break;
            case R.id.layCustomer:
                ActivityUtils.getInstance().showActivity(getActivity(), CustomerInfoActivity.class);
                break;
            case R.id.layExchange:
                ActivityUtils.getInstance().showActivity(getActivity(), ExchangeExamineActivity.class);
                break;
            case R.id.layReward:
                ActivityUtils.getInstance().showActivity(getActivity(), RwordActivity.class);
                break;
            case R.id.layMylm:
                ActivityUtils.getInstance().showActivity(getActivity(), MyAlliesActivity.class);
                break;
            case R.id.layCash:
                ActivityUtils.getInstance().showActivity(getActivity(), MyCashCardActivity.class);
                break;
        }

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_business;
    }

    @Override
    public void onRefresh() {
        initView();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);



    }
}
