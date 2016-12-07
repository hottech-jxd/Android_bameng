package com.bameng.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.adapter.ApplyCashAdapter;
import com.bameng.adapter.MengDataAdapter;
import com.bameng.config.Constants;
import com.bameng.model.BaseModel;
import com.bameng.model.CloseEvent;
import com.bameng.model.ConvertFlowModel;
import com.bameng.model.ConvertFlowOutputModel;
import com.bameng.model.MengModel;
import com.bameng.model.MyOutputModel;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.PostModel;
import com.bameng.model.RefreshCashEvent;
import com.bameng.model.RefreshMengYouEvent;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseFragment;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.AddressPopWin;
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
 * A simple {@link Fragment} subclass.
 */
public class CashFragment extends BaseFragment   implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R2.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.recycleView)
    RecyclerView recyclerView;

    ApplyCashAdapter adapter;

    View noDataView;
    View emptyView;
    //0：未处理审核列表，1：已处理审核列表
    int type = 0;

    long lastId =0;
    //int PAGESIZE = 10;
    OperateTypeEnum operateTypeEnum = OperateTypeEnum.REFRESH;

    public CashFragment() {
        // Required empty public constructor
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
        return R.layout.frag_cash;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.type = getArguments().getInt("type");

        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new ApplyCashAdapter();
        adapter.setOnLoadMoreListener(this);

        emptyView = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty ,  (ViewGroup) recyclerView.getParent(),false);
        adapter.setEmptyView(emptyView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }
        });

        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);

                if( view.getId() == R.id.btnAgree){
                    ConvertFlowModel model =(ConvertFlowModel)baseQuickAdapter.getItem(position);
                    model.setDoing(true);
                    baseQuickAdapter.notifyItemChanged(position);
                    audit(model , 1 );
                }else if(view.getId() == R.id.btnReject){
                    ConvertFlowModel model =(ConvertFlowModel)baseQuickAdapter.getItem(position);
                    model.setDoing(true);
                    baseQuickAdapter.notifyItemChanged(position);
                    audit(model,2);
                }else{

                }

            }
        });

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {

        EventBus.getDefault().unregister(this);

        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        operateTypeEnum = OperateTypeEnum.REFRESH;
        lastId = 0;
        loadData(lastId);
    }

    @Override
    public void onLoadMoreRequested() {
        operateTypeEnum = OperateTypeEnum.LOADMORE;

        loadData(lastId);
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

    void loadData(final long id ){
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");

        map.put("lastId",String.valueOf(id));
        map.put("type", String.valueOf(type));

        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<ConvertFlowOutputModel> call = apiService.ConvertAuditList(token , map);

        call.enqueue(new Callback<ConvertFlowOutputModel>() {
            @Override
            public void onResponse(Call<ConvertFlowOutputModel> call, Response<ConvertFlowOutputModel> response) {
                swipeRefreshLayout.setRefreshing(false);

                if(response.code()!=200|| response.body()==null){

                    ToastUtils.showLongToast( response.message()==null?"error": response.message() );
                    return;
                }
                if (response.body().getStatus() == Constants.STATUS_70035) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(getActivity(), PhoneLoginActivity.class);
                    return;
                }

                if( response.body().getStatus() !=200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }

                if(response.body().getData()==null){
                    ToastUtils.showLongToast("返回的数据为空");
                    return;
                }

                if( operateTypeEnum== OperateTypeEnum.REFRESH) {
                    adapter.setNewData( response.body().getData().getList() );
                    lastId = response.body().getData().getList() ==null || response.body().getData().getList().size()<1 ? 0 : response.body().getData().getList().get( response.body().getData().getList().size()-1).getID();
                }else{
                    if(response.body().getData().getList()==null|| response.body().getData().getList().size()<1){
                        if (noDataView == null){
                            noDataView = LayoutInflater.from(getContext()).inflate(R.layout.layout_nodata,null);
                        }
                        adapter.removeAllFooterView();
                        adapter.addFooterView(noDataView);
                        adapter.loadComplete();
                        return;
                    }

                    adapter.addData(response.body().getData().getList());
                    lastId = response.body().getData().getList().get( response.body().getData().getList().size()-1 ).getID();
                }
            }

            @Override
            public void onFailure(Call<ConvertFlowOutputModel> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                //ToastUtils.showLongToast(t.getMessage()==null?"请求失败": t.getMessage());
                ToastUtils.showLongToast(Constants.SERVER_ERROR);
            }
        });

    }

    void audit(final ConvertFlowModel model , final int status){

        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("id", String.valueOf( model.getID()  ) );
        map.put("status",  String.valueOf( status ));
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<PostModel> call = apiService.ConvertAudit(token, map);
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if (response.code() != 200) {
                    model.setDoing(false);
                    adapter.notifyDataSetChanged();
                    ToastUtils.showLongToast(response.message());
                    return;
                }
                if (response.body() != null && response.body().getStatus() == 200) {
//                    if(status==2){
//                        model.setStatus(2);
//                    }else {
                    model.setDoing(false);
                    adapter.getData().remove(model);
                    EventBus.getDefault().post(new RefreshCashEvent(1));
//                    }
                    adapter.notifyDataSetChanged();
                } else {
                    model.setDoing(false);
                    adapter.notifyDataSetChanged();
                    ToastUtils.showLongToast(response.body() != null ? response.body().getStatusText() : "失败");
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                //ToastUtils.showLongToast("请求失败");
                ToastUtils.showLongToast(Constants.SERVER_ERROR);
                model.setDoing(false);
                adapter.notifyDataSetChanged();
            }
        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnRefreshData(RefreshCashEvent event ){
        if( event.getType() == type ){
            operateTypeEnum =   OperateTypeEnum.REFRESH;
            lastId = 0;
            loadData(lastId);
        }
    }

}
