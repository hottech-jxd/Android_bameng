package com.bameng.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
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

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Header;

import static android.R.attr.addPrintersActivity;
import static android.R.attr.type;
import static com.baidu.location.h.j.P;
import static com.baidu.location.h.j.ad;

/**
 * 盟友 主页
 * Created by 47483 on 2016.11.02.
 */
public class AllyHomeFrag extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener{

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycleView)
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

        initView();
    }

    void initView(){
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new AllyCustomerAdapter();
        adapter.openLoadMore(PAGESIZE);
        adapter.setOnLoadMoreListener(this);
        LayoutInflater layoutInflater= LayoutInflater.from(getContext());
        emptyView = layoutInflater.inflate(R.layout.layout_empty,null);
        adapter.setEmptyView(emptyView);
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
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
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
                ToastUtils.showLongToast( t.getMessage()==null?"请求失败":t.getMessage() );
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
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
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
                    adapter.setNewData(response.body().getData().getRows());
                    if ( response.body().getData().getRows() !=null && response.body().getData().getRows().size()>0) {
                        pageIndex++;
                    }
                }else{
                    if(response.body().getData().getRows()==null|| response.body().getData().getRows().size()<1){
                        if (noDataView == null){
                            noDataView = LayoutInflater.from(getContext()).inflate(R.layout.layout_nodata,null);
                        }
                        adapter.removeAllFooterView();
                        adapter.addFooterView(noDataView);
                        adapter.loadComplete();
                        return;
                    }

                    adapter.addData(response.body().getData().getRows());
                    pageIndex ++;
                }

            }

            @Override
            public void onFailure(Call<CustomListOutput> call, Throwable t) {
                ToastUtils.showLongToast("失败");
            }
        });

    }
}
