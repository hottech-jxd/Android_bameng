package com.bameng.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.adapter.OrderAdapter;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.OrderModel;
import com.bameng.model.OrderOutputModel;
import com.bameng.model.PostModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseFragment;
import com.bameng.ui.business.OrderDetailsActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.RecycleItemDivider;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 *
 */
public class OrderFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener{
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycleView)
    RecyclerView recyclerView;
    OrderAdapter orderAdapter;
    OperateTypeEnum operateType= OperateTypeEnum.REFRESH;
    final int PAGESIZE =10;
    long lastId=0;
    int type=0;
    View noDataView;
    View emptyView;


    OnItemClickListener onItemClickListener=new OnItemClickListener() {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            OrderModel orderModel =(OrderModel) baseQuickAdapter.getItem(i);
            String orderId =orderModel.getOrderId();
            Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
            intent.putExtra("orderid", orderId );
            ActivityUtils.getInstance().showActivity( getActivity(), intent);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.type = this.getArguments().getInt("type");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    public void initView() {
        emptyView =  getActivity().getLayoutInflater().inflate(R.layout.layout_empty, (ViewGroup) recyclerView.getParent(), false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout.setOnRefreshListener(this);
        orderAdapter = new OrderAdapter();
        orderAdapter.openLoadMore(PAGESIZE);
        orderAdapter.setOnLoadMoreListener(this);
        orderAdapter.setEmptyView(emptyView);
        recyclerView.setAdapter(orderAdapter);

        recyclerView.addOnItemTouchListener(onItemClickListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        recyclerView.removeOnItemTouchListener(onItemClickListener);
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
        return R.layout.frag_order;
    }


    @Override
    public void onRefresh() {
        lastId=0;
        operateType = OperateTypeEnum.REFRESH;
        loadData(type , lastId);
    }

    @Override
    public void onLoadMoreRequested() {
        operateType = OperateTypeEnum.LOADMORE;
        loadData( type , lastId);
    }

    void loadData(int type , final long lid  ){
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("type",String.valueOf(type));
        map.put("lastId",String.valueOf(lid));
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<OrderOutputModel> call = apiService.ordermyList(token,map);
        call.enqueue(new Callback<OrderOutputModel>() {
            @Override
            public void onResponse(Call<OrderOutputModel> call, Response<OrderOutputModel> response) {
                swipeRefreshLayout.setRefreshing(false);
                if(response.code()!=200 || response.body()==null ){
                    ToastUtils.showLongToast( response.message()==null? "失败": response.message());
                    return;
                }

                if( operateType == OperateTypeEnum.REFRESH){
                    orderAdapter.setNewData( response.body().getData().getList() );
                    if(response.body().getData().getList()!=null && response.body().getData().getList().size()>0) {
                        lastId = response.body().getData().getList().get(response.body().getData().getList().size() - 1).getId();
                    }
                }else {
                    if (response.body().getData().getList() == null || response.body().getData().getList().size()<1) {
                        if (noDataView == null) {
                            noDataView = getActivity().getLayoutInflater().inflate(R.layout.layout_nodata, (ViewGroup) recyclerView.getParent(), false);
                        }
                        orderAdapter.removeAllFooterView();
                        orderAdapter.addFooterView(noDataView);
                        orderAdapter.loadComplete();
                        return;
                    }

                    orderAdapter.addData( response.body().getData().getList());

                    lastId = response.body().getData().getList().get( response.body().getData().getList().size()-1 ).getId();
                }

            }

            @Override
            public void onFailure(Call<OrderOutputModel> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showLongToast(t.getMessage()==null?"发送错误":t.getMessage());
            }
        });

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
}
