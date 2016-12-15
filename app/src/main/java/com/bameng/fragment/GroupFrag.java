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
import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.adapter.StoreAdapter;
import com.bameng.config.Constants;
import com.bameng.model.AdBannerConfig;
import com.bameng.model.AdImageBean;
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
import com.bameng.ui.news.AddnewsActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.DensityUtils;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.MyListView;
import com.bameng.widgets.RecycleItemDivider;
import com.bameng.widgets.custom.AdBannerWidget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 集团资讯
 * Created by 47483 on 2016.11.01.
 */
public class GroupFrag extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener , BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R2.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.recycleView)
    RecyclerView recyclerView;
    int pageIndex=1;
    public OperateTypeEnum operateType= OperateTypeEnum.REFRESH;
    StoreAdapter adapter;
    final static int PAGESIZE=10;
    List<SlideListModel> adDataList;
    View noDataView;

    AdBannerWidget adBannerWidget;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    public void initView(){
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new StoreAdapter(R.layout.article_item);
        adapter.openLoadMore(PAGESIZE);
        adapter.setOnLoadMoreListener(this);
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adDataList = new ArrayList<>();

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                ListModel model = (ListModel) baseQuickAdapter.getItem(position);
                Bundle bd = new Bundle();
                bd.putString(Constants.INTENT_URL, model.getArticleUrl());
                bd.putString(Constants.INTENT_TITLE,model.getArticleTitle());
                ActivityUtils.getInstance().showActivity( getActivity() , WebViewActivity.class , bd );
            }
        });

        adBannerWidget = new AdBannerWidget(getContext());
        int width = DensityUtils.getScreenW(getContext());
        int height = width/2;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
        adBannerWidget.setLayoutParams(layoutParams);


        List<SlideListModel> list = new ArrayList<>();
        SlideListModel item = new SlideListModel();
        item.setPicUrl("res://"+ getContext().getPackageName() +"/" + R.mipmap.none);
        list.add(item);
        AdBannerConfig config =  new AdBannerConfig();
        config.setImages(list);
        adBannerWidget.setAdBannerConfig(config);

        adapter.addHeaderView(adBannerWidget);
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

    private void loadData(int idx ) {
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("identity","0");
        map.put("pageIndex",String.valueOf( idx ));
        map.put("pageSize",String.valueOf(PAGESIZE));
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
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
                if(response.body().getStatus() !=200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }


                //ArticleListOutput articleListOutput = new ArticleListOutput();
                //articleListOutput.setData(response.body().getData());
                if (response.body().getStatus() == 200 && response.body().getData() != null) {
                    if (operateType == OperateTypeEnum.REFRESH) {
                        List<ListModel> Articles = new ArrayList<>();
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

                        adapter.setNewData(Articles);

                    } else if (operateType == OperateTypeEnum.LOADMORE) {
                        if (response.body().getData().getList().getRows().size() == 0) {
                            if (noDataView == null) {
                                noDataView = getActivity().getLayoutInflater().inflate(R.layout.layout_nodata, (ViewGroup) recyclerView.getParent(), false);
                            }
                            adapter.removeAllFooterView();
                            adapter.addFooterView(noDataView);
                            adapter.loadComplete();
                        } else {
                            adapter.addData(response.body().getData().getList().getRows());
                            pageIndex = pageIndex + 1;
                        }
                    }
                } else {
                    ToastUtils.showLongToast(response.body().getStatusText());
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

    private void initBanner(){
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("type","0");
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<SlideListOutputModel> call = apiService.FocusPic(token,map);
        call.enqueue(new Callback<SlideListOutputModel>() {
            @Override
            public void onResponse(Call<SlideListOutputModel> call, Response<SlideListOutputModel> response) {
                if(response.code()!=200){
                    ToastUtils.showLongToast(response.message());
                    return;
                }
                if (response.body() != null) {
                    if (response.body().getStatus() == 200&&response.body().getData()!=null) {
                        adDataList.clear();
                        adDataList.addAll(response.body().getData());
                        AdBannerConfig config = new AdBannerConfig();
                        List<AdImageBean> images = new ArrayList<>();
                        config.setAutoPlay(true);
                        config.setImages(adDataList);
                        config.setWidth(DensityUtils.getDialogW(getActivity()));

//                        AdBannerWidget adBannerWidget = new AdBannerWidget(getContext(),config);
//                        adapter.removeAllHeaderView();
//                        adapter.addHeaderView(adBannerWidget);

                        adBannerWidget.setAdBannerConfig( config );

                    } else {
                        ToastUtils.showLongToast(response.body().getStatusText());
                    }

                }
            }

            @Override
            public void onFailure(Call<SlideListOutputModel> call, Throwable t) {
                ToastUtils.showLongToast("获得轮播图失败");
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
        return R.layout.frag_group;
    }

    @Override
    public void onRefresh() {
        operateType = OperateTypeEnum.REFRESH;
        pageIndex = 1;
        initBanner();
        loadData(pageIndex);
    }

    @Override
    public void onLoadMoreRequested() {
        operateType = OperateTypeEnum.LOADMORE;
        loadData(pageIndex+1);
    }

    @Override
    public String getPageTitle() {
        return "集团资讯";
    }
}
