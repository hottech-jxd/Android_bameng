package com.bameng.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
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
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 我的业务
 */
public class BusinessFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.txt_order)
    TextView txtOrder;
    @Bind(R.id.txt_Customer)
    TextView txtCustomer;
    @Bind(R.id.txt_exchange)
    TextView txtExchange;
    @Bind(R.id.txt_cash)
    TextView txtCash;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        swipeRefreshLayout.setOnRefreshListener(this);

        initView();
    }

    public void initView(){//******数据为空
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        String token = BaseApplication.readToken();
        Call<MyBusinessOutputModel> call = apiService.MyBusiness(token,map);
        call.enqueue(new Callback<MyBusinessOutputModel>() {
            @Override
            public void onResponse(Call<MyBusinessOutputModel> call, Response<MyBusinessOutputModel> response) {
                swipeRefreshLayout.setRefreshing(false);
                if(response.code() !=200){
                    ToastUtils.showLongToast(response.message());
                    return;
                }
                if (response.body() != null) {
//
                    if (response.body().getStatus() == 200&&response.body().getData()!=null) {
                        //ToastUtils.showLongToast("提交成功");
                        txtOrder.setText( String.valueOf( response.body().getData().getOrderAmount() ));
                        txtCustomer.setText( String.valueOf( response.body().getData().getCustomerAmount() ) );
                        txtCash.setText( String.valueOf( response.body().getData().getCashCouponAmount()) );
                        txtExchange.setText(String.valueOf( response.body().getData().getExchangeAmount() ));
//
                    } else {
                        ToastUtils.showLongToast(response.body().getStatusText());
                    }

                }
            }


            @Override
            public void onFailure(Call<MyBusinessOutputModel> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showLongToast("失败");
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
}
