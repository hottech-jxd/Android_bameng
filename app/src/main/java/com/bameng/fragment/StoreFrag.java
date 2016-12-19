package com.bameng.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.adapter.StoreAdapter;
import com.bameng.config.Constants;
import com.bameng.model.ArticleListOutput;
import com.bameng.model.CloseEvent;
import com.bameng.model.ListModel;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.SlideListModel;
import com.bameng.model.SlideListOutputModel;
import com.bameng.model.TopArticleIdModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.WebViewActivity;
import com.bameng.ui.base.BaseFragment;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.MyListView;
import com.bameng.widgets.RecycleItemDivider;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 47483 on 2016.11.01.
 */

public class StoreFrag extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener , BaseQuickAdapter.RequestLoadMoreListener {
    int pageIndex=1;
    OperateTypeEnum operateType= OperateTypeEnum.REFRESH;
    @BindView(R2.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.recycleView)
    RecyclerView recyclerView;
    BaseQuickAdapter baseAdapter;
    final int PAGESIZE=10;
    View noDataView;
    View emptyView;

    protected int type = 1;

    protected  int layoutId = R.layout.article_item;

    OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position ) {

            ListModel model = (ListModel) baseQuickAdapter.getItem(position);

            setBadge(model);

            model.setIsRead(1);
            baseQuickAdapter.notifyItemChanged(position);

            Bundle bd = new Bundle();
            bd.putString(Constants.INTENT_URL, model.getArticleUrl());
            bd.putString(Constants.INTENT_TITLE, model.getArticleTitle());
            ActivityUtils.getInstance().showActivity( getActivity() , WebViewActivity.class , bd );
        }
    };

    public StoreFrag(){
        baseAdapter = new StoreAdapter( layoutId );
    }

    /***
     *  处理未读消息的红点显示
     */
    protected void setBadge(ListModel model){

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        recyclerView.removeOnItemTouchListener(onItemClickListener);
    }

    public void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout.setOnRefreshListener(this);
        baseAdapter.openLoadMore(PAGESIZE);
        baseAdapter.setOnLoadMoreListener(this);
        recyclerView.setAdapter(baseAdapter);
        recyclerView.addOnItemTouchListener( onItemClickListener);

        emptyView = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty ,  (ViewGroup) recyclerView.getParent(),false);
        baseAdapter.setEmptyView(emptyView);

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

    private void loadData(int pidx ) {

        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("identity",String.valueOf(type));
        map.put("pageIndex",String.valueOf(pidx));
        map.put("pageSize",String.valueOf(PAGESIZE));
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<ArticleListOutput> call = apiService.list(token,map);
        call.enqueue(new Callback<ArticleListOutput>() {
            @Override
            public void onResponse(Call<ArticleListOutput> call, Response<ArticleListOutput> response) {
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
                if (response.body().getStatus() != 200) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }

                if (response.body().getStatus() == 200 && response.body() != null) {
                    if (operateType == OperateTypeEnum.REFRESH) {
                        List<ListModel> Articles = new ArrayList<ListModel>();
                        List<ListModel> topList = response.body().getData().getTop();
                        if (topList != null) {
                            for (ListModel item : topList) {
                                item.setTop(true);
                            }
                            Articles.addAll(topList);
                        }
                        if (response.body().getData().getList() != null && response.body().getData().getList().getRows() != null) {
                            Articles.addAll(response.body().getData().getList().getRows());
                        }
                        baseAdapter.setNewData(Articles);

                        updateBadge();

                    } else if (operateType == OperateTypeEnum.LOADMORE) {

                        if (response.body().getData().getList().getRows().size() < 1) {
                            if (noDataView == null) {
                                noDataView = getActivity().getLayoutInflater().inflate(R.layout.layout_nodata, (ViewGroup) recyclerView.getParent(), false);
                            }
                            baseAdapter.removeAllFooterView();
                            baseAdapter.addFooterView(noDataView);

                            baseAdapter.loadComplete();
                            //adapter.notifyDataSetChanged();
                            //pageIndex=pageIndex-1;
                            //ToastUtils.showLongToast("没有更多信息...");
                        } else {
                            //Articles.addAll( response.body().getData().getList().getRows() );
                            baseAdapter.addData(response.body().getData().getList().getRows());
                            pageIndex = pageIndex + 1;

                            updateBadge();
                        }

                    }
                } else {
                    //ToastUtils.showLongToast(response.body().getStatusText());
                    baseAdapter.showLoadMoreFailedView();
                }
            }


            @Override
            public void onFailure(Call<ArticleListOutput> call, Throwable t) {
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
        return R.layout.frag_store;
    }


    @Override
    public void onRefresh() {
        operateType = OperateTypeEnum.REFRESH;
        pageIndex=1;
        baseAdapter.removeAllFooterView();
        loadData(pageIndex);
    }

    @Override
    public void onLoadMoreRequested() {

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                operateType = OperateTypeEnum.LOADMORE;
                loadData(pageIndex+1);
            }
        });
    }

    @Override
    public String getPageTitle() {
        return "总店资讯";
    }

    protected void updateBadge(){

    }

}
