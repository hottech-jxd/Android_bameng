package com.bameng.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.adapter.CustomerDetailAdapter;
import com.bameng.config.Constants;
import com.bameng.model.CloseEvent;
import com.bameng.model.CustomListOutput;
import com.bameng.model.CustomerModel;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.PostModel;
import com.bameng.model.RefreshCustomerEvent;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseFragment;
import com.bameng.ui.business.CustomerExamineActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.AddressPopWin;
import com.bameng.widgets.TipAlertDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 未处理的客户信息
 * Created by 47483 on 2016.11.09.
 */
public class CustomNoDoneFrag extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener{

    @BindView(R2.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R2.id.recycleView)
    RecyclerView recyclerView;

    public OperateTypeEnum operateType= OperateTypeEnum.REFRESH;

    int pageIndex= 1;
    final int PAGESIZE=10;
    View noDataView;
    View emptyView;
    int type = 1;

    CustomerDetailAdapter adapter;
    TipAlertDialog tipAlertDialog;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        type = getArguments().getInt("type");

        initList();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
    }

    private void initList(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CustomerDetailAdapter( R.layout.customnodone_item);
        adapter.setOnLoadMoreListener(this);
        adapter.openLoadMore(PAGESIZE);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setAdapter(adapter);

        emptyView = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty, (ViewGroup) recyclerView.getParent() ,false);

        adapter.setEmptyView(emptyView);

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                if( view.getId() == R.id.btnAgree){
                    CustomerModel model = (CustomerModel) adapter.getItem(position);
                    auditTip(  position , model , 1 ,"确定要同意申请吗？");
                }else if(view.getId() == R.id.btnReject){
                    CustomerModel model = (CustomerModel) adapter.getItem(position);
                    auditTip( position , model , 2 ,"确定要拒绝申请吗？");
                }else {
                    CustomerModel customerModel = (CustomerModel)adapter.getItem(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("customerinfo", customerModel);
                    ActivityUtils.getInstance().showActivity( getActivity() , CustomerExamineActivity.class,bundle);
                }
            }
        });
    }

    void auditTip(final int position , final CustomerModel customerModel , final int status , String msg  ){
        if(tipAlertDialog==null) tipAlertDialog = new TipAlertDialog(getContext() ,false);
        tipAlertDialog.show("审核提醒", msg , null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( tipAlertDialog !=null) tipAlertDialog.dismiss();
                audit( position, customerModel , status );
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

    private void loadData(int indx ){
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("type", String.valueOf(type));
        map.put("pageIndex",String.valueOf(indx));
        map.put("pageSize",String.valueOf(PAGESIZE));
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


                if(operateType==OperateTypeEnum.REFRESH) {
                    adapter.setNewData(response.body().getData().getRows());
                    if ( response.body().getData().getRows() .size()>0) {
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
                swipeRefreshLayout.setRefreshing(false);
                //ToastUtils.showLongToast(t.getMessage()==null?"请求失败":t.getMessage());
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

    @Override
    public void onClick(View view) {

    }

    @Override
    public int getLayoutRes() {
        return R.layout.frag_customdone;
    }

    @Subscribe( threadMode = ThreadMode.MAIN)
    public void onEventRefreshData(RefreshCustomerEvent event){
        if( event.getTabName().equals("NoDoneFrag")) {
//            for (int i = 0; i < Customers.size(); i++) {
//                if (Customers.get(i).getID() == event.getCustomerModel().getID()) {
//                    Customers.remove(Customers.get(i));
//                    break;
//                }
//            }
//            adapter.notifyDataSetChanged();

            onRefresh();
        }
    }

    @Override
    public void onRefresh() {
        operateType = OperateTypeEnum.REFRESH;
        pageIndex=0;
        loadData(pageIndex);
    }

    @Override
    public void onLoadMoreRequested() {
        operateType=OperateTypeEnum.LOADMORE;
        loadData(pageIndex+1);
    }

    protected void audit(final int position , final CustomerModel customerModel , int status ){

        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("cid", String.valueOf( customerModel.getID()  ) );
        map.put("status",  String.valueOf( status ));
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        String token = BaseApplication.readToken();
        Call<PostModel> call = apiService.audit(token, map);
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if( response.code() !=200){
                    customerModel.setDoing(false);
                    adapter.notifyDataSetChanged();
                    ToastUtils.showLongToast( response.message() );
                    return;
                }
                if( response.body() !=null && response.body().getStatus() ==200 ){
                    //Customers.remove( customerModel );

                    adapter.remove(position);

                    adapter.notifyDataSetChanged();
                }else{
                    customerModel.setDoing(false);
                    adapter.notifyDataSetChanged();
                    ToastUtils.showLongToast( response.body() !=null ?  response.body().getStatusText() : "失败");
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                //ToastUtils.showLongToast("请求失败");
                ToastUtils.showLongToast(Constants.SERVER_ERROR);
                customerModel.setDoing(false);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public String getPageTitle() {
        return "未处理信息";
    }
}
