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
import android.widget.ScrollView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.adapter.ArticleAdapter;
import com.bameng.adapter.HomeBannerPagerAdapter;
import com.bameng.adapter.StoreAdapter;
import com.bameng.config.Constants;
import com.bameng.model.AdBannerConfig;
import com.bameng.model.AdImageBean;
import com.bameng.model.AdlistModel;
import com.bameng.model.ArticleListOutput;
import com.bameng.model.ListModel;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.SlideListModel;
import com.bameng.model.SlideListOutputModel;
import com.bameng.model.TopArticleIdModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.WebViewActivity;
import com.bameng.ui.base.BaseFragment;
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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.baidu.location.h.j.ad;
import static com.bameng.R.id.dot;
import static com.bameng.R.id.homePullRefresh;
import static com.bameng.R.id.homeViewPager;
import static com.bameng.R.id.listL;
import static com.bameng.R.id.recycleView;

/**
 * 集团资讯
 * Created by 47483 on 2016.11.01.
 */
public class GroupFrag extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener , BaseQuickAdapter.RequestLoadMoreListener {
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycleView)
    RecyclerView recyclerView;
    int pageIndex=1;
    public OperateTypeEnum operateType= OperateTypeEnum.REFRESH;
    StoreAdapter adapter;
    final static int PAGESIZE=10;
    List<SlideListModel> adDataList;
    View noDataView;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }
    public void initView() {
        initlist();

        initBanner();

        loadData(pageIndex);
    }


    public void initlist(){
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new StoreAdapter(R.layout.article_item);
        adapter.openLoadMore(PAGESIZE);
        adapter.setOnLoadMoreListener(this);
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        //recyclerView.addItemDecoration( new RecycleItemDivider( this.getContext() , LinearLayoutManager.VERTICAL));


        adDataList = new ArrayList<>();


        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                ListModel model = (ListModel) baseQuickAdapter.getItem(position);
                Bundle bd = new Bundle();
                bd.putString(Constants.INTENT_URL, model.getArticleUrl());
                ActivityUtils.getInstance().showActivity( getActivity() , WebViewActivity.class , bd );
            }
        });

        //View header = LayoutInflater.from(getContext()).inflate(R.layout.layout_group_header,null);
//        AdBannerConfig config = new AdBannerConfig();
//        config.setAutoPlay(true);
//        View header = new  AdBannerWidget(getContext(), config);

//        dot = (LinearLayout) header.findViewById(R.id.dot);
//        homeViewPager = (ViewPager) header.findViewById(R.id.homeViewPager);
//
//        adapter.addHeaderView(header);


        //Articles = new ArrayList<ListModel>();
        //TopArticles = new ArrayList<ListModel>();
        //adapter = new ArticleAdapter(Articles,TopArticles, getActivity(), getActivity());
        //listL.setAdapter(adapter);
        //homePullRefresh.setMode(PullToRefreshBase.Mode.BOTH);
        //homePullRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
        //    @Override
        //    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
        //        operateType = OperateTypeEnum.REFRESH;
        //        pageIndex=1;
        //        Articles.clear();
        //        loadData();
        //        initSwitchImg();
        //    }

//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
//                operateType = OperateTypeEnum.LOADMORE;
//                pageIndex= pageIndex+1;
//                loadData();
//
//            }
//        });
    }

    @Override
    protected void loadData() {
        onRefresh();
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
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        String token = BaseApplication.readToken();
        Call<ArticleListOutput> call = apiService.list(token,map);
        call.enqueue(new Callback<ArticleListOutput>() {
            @Override
            public void onResponse(Call<ArticleListOutput> call, Response<ArticleListOutput> response) {
                swipeRefreshLayout.setRefreshing(false);

                if(response.code() !=200){
                    ToastUtils.showLongToast(response.message());
                    return;
                }

                if (response.body() != null) {
                    //homePullRefresh.onRefreshComplete();
                    ArticleListOutput articleListOutput = new ArticleListOutput();
                    articleListOutput.setData(response.body().getData());
                    if (response.body().getStatus() == 200 &&response.body().getData() !=null) {
                        if (operateType == OperateTypeEnum.REFRESH) {
                            List<ListModel> Articles = new ArrayList<>();
                            List<ListModel> topList = response.body().getData().getTop();
                            if( topList !=null){
                                for(ListModel item : topList){
                                    item.setTop(true);
                                }
                                Articles.addAll(topList);
                            }
                            if(response.body().getData().getList()!=null && response.body().getData().getList().getRows()!=null){
                                Articles.addAll(response.body().getData().getList().getRows());
                            }

                            adapter.setNewData( Articles );

                        } else if (operateType == OperateTypeEnum.LOADMORE) {
                            if (response.body().getData().getList().getRows().size()==0){
                                if(noDataView==null){
                                    noDataView = getActivity().getLayoutInflater().inflate(R.layout.layout_nodata, (ViewGroup) recyclerView.getParent(), false);
                                }
                                adapter.removeAllFooterView();
                                adapter.addFooterView(noDataView);
                                adapter.loadComplete();
                            }else{
                                adapter.addData( response.body().getData().getList().getRows());
                                pageIndex=pageIndex+1;
                            }
                        }
                    } else {
                        ToastUtils.showLongToast(response.body().getStatusText());
                    }
                }
            }


            @Override
            public void onFailure(Call<ArticleListOutput> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showLongToast("失败");
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

                        AdBannerWidget adBannerWidget = new AdBannerWidget(getContext(),config);
                        adapter.removeAllHeaderView();
                        adapter.addHeaderView(adBannerWidget);
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
}
