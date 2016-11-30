package com.bameng.fragment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.adapter.CustomDetailsAdapter;
import com.bameng.adapter.MengDataAdapter;
import com.bameng.config.Constants;
import com.bameng.model.BaseModel;
import com.bameng.model.CloseEvent;
import com.bameng.model.MengModel;
import com.bameng.model.MyOutputModel;
import com.bameng.model.MyPageModel;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.PostModel;
import com.bameng.model.RefreshMengYouEvent;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseFragment;
import com.bameng.ui.business.AlliesDetailsActivity;
import com.bameng.ui.business.AlliesExamineActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.ui.news.AddnewsActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.DensityUtils;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.AddressPopWin;
import com.bameng.widgets.RecycleItemDivider;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 */
public class MengFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener  {
    int type=0;
    MengDataAdapter adapter;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycleView)
    RecyclerView recyclerView;

    @Bind(R.id.llsort)
    LinearLayout llSort;

    @Bind(R.id.sortLevel)
    TextView sortLevel;
    @Bind(R.id.sortOrderCount)
    TextView sortOrderCount;
    @Bind(R.id.sortsubCount)
    TextView sortSubCount;

    int pageIndex=1;
    int PAGESIZE = 10;
    OperateTypeEnum operateTypeEnum = OperateTypeEnum.REFRESH;
    View noDataView;
    View emptyView;

    int sortCode=0;
    int isDesc = 0;

    public MengFragment() {
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
        return com.bameng.R.layout.frag_meng;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        type = getArguments().getInt("type");
        if(type == 0){
            llSort.setVisibility(View.GONE);
        }else{
            llSort.setVisibility(View.VISIBLE);

        }

        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new MengDataAdapter(  type );
        adapter.openLoadMore(PAGESIZE);
        adapter.setOnLoadMoreListener(this);


        emptyView = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty ,  (ViewGroup) recyclerView.getParent(),false);
        adapter.setEmptyView(emptyView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                //ToastUtils.showLongToast("i");
                if(type== 0 ){

                    MengModel model =(MengModel) baseQuickAdapter.getItem(i);

                    if(model.getStatus() == 2) return;//当状态是拒绝时，无法操作

                    Bundle bd = new Bundle();
                    bd.putSerializable("model", model );
                    ActivityUtils.getInstance().showActivity(getActivity(), AlliesExamineActivity.class, bd );


                }else if( type == 1){
                    MengModel model =(MengModel) baseQuickAdapter.getItem(i);
                    int id= model.getUserId();
                    Bundle bd = new Bundle();
                    bd.putInt("userid",id);
                    ActivityUtils.getInstance().showActivity(getActivity(), AlliesDetailsActivity.class, bd );
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //super.onItemChildClick(adapter, view, position);
                if( view.getId() == R.id.btnAgree){
                    MengModel model =(MengModel)baseQuickAdapter.getItem(position);
                    model.setDoing(true);
                    baseQuickAdapter.notifyItemChanged(position);
                    audit(model , 1 );
                }else if(view.getId() == R.id.btnReject){
                    MengModel model =(MengModel)baseQuickAdapter.getItem(position);
                    model.setDoing(true);
                    baseQuickAdapter.notifyItemChanged(position);
                    audit(model,2);
                }else{

                }
            }
        });

        recyclerView.setAdapter(adapter);

        if( type == 0){
            LinearLayout.LayoutParams layoutParams = ( LinearLayout.LayoutParams)recyclerView.getLayoutParams();
            if(layoutParams!=null) {
                int div = DensityUtils.dip2px(getContext(),8);
                layoutParams.setMargins(layoutParams.leftMargin, div , layoutParams.rightMargin, layoutParams.bottomMargin);
            }
            recyclerView.addItemDecoration(new RecycleItemDivider( getContext() , RecyclerView.VERTICAL , 8, R.color.dividerColor ));
        }

    }

    @Override
    public void onRefresh() {
        operateTypeEnum = OperateTypeEnum.REFRESH;
        pageIndex = 1;
        loadData(pageIndex);

    }

    @Override
    public void onLoadMoreRequested() {
        operateTypeEnum = OperateTypeEnum.LOADMORE;
        loadData(pageIndex+1);
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


    void loadData(int idx ){
        type = getArguments().getInt("type");
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("pageIndex",String.valueOf(idx));
        map.put("pageSize", String.valueOf(PAGESIZE));

        if( type==1){
            map.put("orderbyCode", String.valueOf( sortCode));
            map.put("isDesc", String.valueOf( isDesc));
        }

        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<MyOutputModel<MengModel>> call = null;
        if(type==0){
            call =  apiService.AllyApplylist(token , map);
        }else{
            call = apiService.allylist(token,map);
        }
        call.enqueue(new Callback<MyOutputModel<MengModel>>() {
            @Override
            public void onResponse(Call<MyOutputModel<MengModel>> call, Response<MyOutputModel<MengModel>> response) {
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
                    adapter.setNewData( response.body().getData().getRows() );
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
                    pageIndex++;
                }

            }

            @Override
            public void onFailure(Call<MyOutputModel<MengModel>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showLongToast(t.getMessage()==null?"请求失败":t.getMessage());
            }
        });


    }


    void audit(final MengModel model , final int status){


        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("id", String.valueOf( model.getID()  ) );
        map.put("status",  String.valueOf( status ));
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        String token = BaseApplication.readToken();
        Call<BaseModel> call = apiService.AllyApplyAudit(token, map);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if( response.code() !=200){
                    model.setDoing(false);
                    adapter.notifyDataSetChanged();
                    ToastUtils.showLongToast( response.message() );
                    return;
                }
                if( response.body() !=null && response.body().getStatus() ==200 ){
                    if(status==2){
                        model.setStatus(2);
                        model.setStatusName("拒绝");
                    }else {
                        adapter.getData().remove(model);
                        EventBus.getDefault().post(new RefreshMengYouEvent(1));
                    }

                    adapter.notifyDataSetChanged();
                }else{
                    model.setDoing(false);
                    adapter.notifyDataSetChanged();
                    ToastUtils.showLongToast( response.body() !=null ?  response.body().getStatusText() : "失败");
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                ToastUtils.showLongToast("请求失败");
                model.setDoing(false);
                adapter.notifyDataSetChanged();
            }
        });

    }


    @OnClick({R.id.sortLevel,R.id.sortsubCount,R.id.sortOrderCount})
    void onSort(View v){
        if( v.getId() == R.id.sortLevel){
            isDesc = sortCode == 0 ? (isDesc==0? 1: 0) : 0;
            sortCode = 0;
            Drawable drawable =  ContextCompat.getDrawable( getActivity() , isDesc ==1 ? R.mipmap.ic_desc:R.mipmap.ic_asc);
            drawable.setBounds(0,0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());


            sortLevel.setCompoundDrawables(null,null, drawable ,null);

            drawable =  ContextCompat.getDrawable( getActivity() , R.mipmap.ic_nosort);
            drawable.setBounds(0,0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

            sortOrderCount.setCompoundDrawables( null,null, drawable , null );
            sortSubCount.setCompoundDrawables( null, null, drawable,null);

        }else if(R.id.sortsubCount==v.getId()){
            isDesc = sortCode == 1 ? (isDesc==0? 1: 0) : 0;
            sortCode = 1;
            Drawable drawable =  ContextCompat.getDrawable( getActivity() , isDesc ==1 ? R.mipmap.ic_desc:R.mipmap.ic_asc);
            drawable.setBounds(0,0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            sortSubCount.setCompoundDrawables(null,null, drawable ,null);

            drawable =  ContextCompat.getDrawable( getActivity() , R.mipmap.ic_nosort);
            drawable.setBounds(0,0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

            sortOrderCount.setCompoundDrawables( null,null, drawable , null );
            sortLevel.setCompoundDrawables( null, null, drawable,null);

        }else if(R.id.sortOrderCount == v.getId()){
            isDesc = sortCode == 2 ? (isDesc==0? 1: 0) : 0;
            sortCode = 2;
            Drawable drawable =  ContextCompat.getDrawable( getActivity() , isDesc==1 ? R.mipmap.ic_desc:R.mipmap.ic_asc);
            drawable.setBounds(0,0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            sortOrderCount.setCompoundDrawables(null,null, drawable ,null);

            drawable =  ContextCompat.getDrawable( getActivity() , R.mipmap.ic_nosort);
            drawable.setBounds(0,0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

            sortLevel.setCompoundDrawables( null,null, drawable , null );
            sortSubCount.setCompoundDrawables( null, null, drawable,null);
        }

        operateTypeEnum = OperateTypeEnum.REFRESH;
        pageIndex = 1;
        loadData( pageIndex );
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRefreshData(RefreshMengYouEvent event){
        if(event.getType() == type) {
            onRefresh();
        }
    }
}


