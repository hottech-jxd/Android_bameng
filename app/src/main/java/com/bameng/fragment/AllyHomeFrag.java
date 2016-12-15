package com.bameng.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.adapter.AllyCustomerAdapter;
import com.bameng.adapter.CustomerDetailAdapter;
import com.bameng.config.Constants;
import com.bameng.model.AdImageBean;
import com.bameng.model.AllySummeryOutputModel;
import com.bameng.model.CashCouponOutputModel;
import com.bameng.model.CloseEvent;
import com.bameng.model.CustomListOutput;
import com.bameng.model.CustomerModel;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.PostModel;
import com.bameng.model.RefreshCustomerEvent;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseFragment;
import com.bameng.ui.business.CustomerDetailsActivity;
import com.bameng.ui.business.CustomerExamineActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 盟友 主页
 * Created by 47483 on 2016.11.02.
 */
public class AllyHomeFrag extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener{

    @BindView(R2.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.recycleView)
    RecyclerView recyclerView;
    View header;
    AllyCustomerAdapter adapter;
    int pageIndex=0;
    final  int PAGESIZE=10;
    OperateTypeEnum operateTypeEnum = OperateTypeEnum.REFRESH;
    View emptyView;
    View noDataView;
    TextView tvinfoCount;
    TextView tvinforank;
    TextView tvmycount;
    TextView tvordercount;
    TextView tvtradecount;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
    }

    void initView(){
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new AllyCustomerAdapter();
        adapter.openLoadMore(PAGESIZE);
        adapter.setOnLoadMoreListener(this);
        LayoutInflater layoutInflater= LayoutInflater.from(getContext());
        emptyView = layoutInflater.inflate(R.layout.layout_empty, (ViewGroup) recyclerView.getParent() , false);
        adapter.setEmptyView( true , emptyView);
        recyclerView.setAdapter(adapter);

        header = layoutInflater.inflate(R.layout.layout_allyhome_header,null);
        adapter.addHeaderView( header );

        tvinfoCount = (TextView) header.findViewById(R.id.tvInfoCount);
        tvinforank = (TextView) header.findViewById(R.id.tvInfoRank);
        tvmycount = (TextView) header.findViewById(R.id.tvMengCount);
        tvordercount = (TextView) header.findViewById(R.id.tvOrderCount);
        tvtradecount = (TextView) header.findViewById(R.id.tvTradeCount);

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                CustomerModel customerModel = (CustomerModel) baseQuickAdapter.getItem(i);
                Bundle bundle = new Bundle();
                bundle.putSerializable("customerinfo", customerModel);
                ActivityUtils.getInstance().showActivity( getActivity() , CustomerDetailsActivity.class,bundle);
            }
        });

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

    @Override
    public void onClick(View view) {

    }



    @Override
    public int getLayoutRes() {
        return R.layout.frag_ally_home;
    }

    @Override
    public void onRefresh() {
        operateTypeEnum=OperateTypeEnum.REFRESH;
        pageIndex=0;
        if( noDataView!=null && noDataView.getParent()!=null){
            ((ViewGroup)noDataView.getParent()).removeView(noDataView);
        }
        loadMySummary();
        loadData(pageIndex);
    }

    @Override
    public void onLoadMoreRequested() {
        operateTypeEnum = OperateTypeEnum.LOADMORE;
        loadData(pageIndex+1);
    }

    private void loadMySummary(){
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<AllySummeryOutputModel> call = apiService.AllyHomeSummary(token, map);
        call.enqueue(new Callback<AllySummeryOutputModel>() {
            @Override
            public void onResponse(Call<AllySummeryOutputModel> call, Response<AllySummeryOutputModel> response) {
                if(response.code()!=200){
                    ToastUtils.showLongToast(response.message());
                    return;
                }
                if(response.body()==null){
                    ToastUtils.showLongToast("返回错误数据");
                    return;
                }
                if(response.body().getStatus() == Constants.STATUS_70035){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().showActivity(getActivity(), PhoneLoginActivity.class);
                    return;
                }
                if(response.body().getStatus()!=200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }

                tvtradecount.setText( String.valueOf( response.body().getData().getOrderSuccessAmount() ));
                tvordercount.setText( String.valueOf( response.body().getData().getOrderRank() ) );
                tvmycount.setText( String.valueOf( response.body().getData().getAllyAmount() ) );
                tvinfoCount.setText( String.valueOf( response.body().getData().getCustomerAmount() ) );
                tvinforank.setText( String.valueOf( response.body().getData().getCustomerRank() ));
            }

            @Override
            public void onFailure(Call<AllySummeryOutputModel> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                //ToastUtils.showLongToast( t.getMessage()==null?"请求失败":t.getMessage() );
                ToastUtils.showLongToast( Constants.SERVER_ERROR );
            }
        });

    }

    void loadData( int indx ){
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("type", String.valueOf(0));
        map.put("pageIndex",String.valueOf(indx));
        map.put("pageSize", String.valueOf( PAGESIZE ));
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<CustomListOutput> call = apiService.customlist(token, map);
        call.enqueue(new Callback<CustomListOutput>() {
            @Override
            public void onResponse(Call<CustomListOutput> call, Response<CustomListOutput> response) {
                swipeRefreshLayout.setRefreshing(false);
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
                if(response.body().getStatus()!=200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }

                if(operateTypeEnum ==OperateTypeEnum.REFRESH) {
                    if( response.body().getData().getTotal() <1 ){
                        adapter.setNewData(null);
                    }else {
                        adapter.setNewData(response.body().getData().getRows());
                    }
                    if ( response.body().getData().getRows() !=null && response.body().getData().getRows().size()>0) {
                        pageIndex++;
                    }
                }else{
                    if(response.body().getData().getRows()==null|| response.body().getData().getRows().size()<1){
                        if (noDataView == null){
                            noDataView = LayoutInflater.from(getContext()).inflate(R.layout.layout_nodata,null);
                            adapter.addFooterView( noDataView );
                        }else {
                            if( noDataView.getParent()!=null){
                                ((ViewGroup)noDataView.getParent()).removeView( noDataView);
                            }
                            adapter.addFooterView(noDataView);
                        }

                        adapter.loadComplete();
                        return;
                    }

                    adapter.addData(response.body().getData().getRows());
                    pageIndex ++;
                }

            }

            @Override
            public void onFailure(Call<CustomListOutput> call, Throwable t) {
                //ToastUtils.showLongToast(t.getMessage()==null?"请求失败":t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showLongToast(Constants.SERVER_ERROR);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefreshData(RefreshCustomerEvent event){
        onRefresh();
    }

}
