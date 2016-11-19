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
import com.bameng.model.AdlistModel;
import com.bameng.model.ArticleListOutput;
import com.bameng.model.ListModel;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.SlideListModel;
import com.bameng.model.SlideListOutputModel;
import com.bameng.model.TopArticleIdModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseFragment;
import com.bameng.ui.news.AddnewsActivity;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.MyListView;
import com.bameng.widgets.RecycleItemDivider;
import com.chad.library.adapter.base.BaseQuickAdapter;
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

import static com.bameng.R.id.homePullRefresh;
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
//    @Bind(R.id.listL)
//    MyListView listL;
    int pageIndex=1;
    public OperateTypeEnum operateType= OperateTypeEnum.REFRESH;
    //public List<ListModel> Articles;
    //public List<ListModel> TopArticles;
    //public ArticleAdapter adapter;

    StoreAdapter adapter;
    final static int PAGESIZE=10;

//    @Bind(R.id.homePullRefresh)
//    PullToRefreshScrollView homePullRefresh;

    //@Bind(R.id.dot)
    LinearLayout dot;

    //@Bind(R.id.homeViewPager)
    ViewPager homeViewPager;
    HomeBannerPagerAdapter homeBannerPagerAdapter;

    List<SlideListModel> adDataList ;

    View noDataView;

    private Handler mhandler;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
       // ButterKnife.bind(this, view);
    }
    public void initView() {





        initlist();

        initSwitchImg();

        loadData(pageIndex);
    }
    public void initlist(){
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new StoreAdapter(R.layout.article_item);
        adapter.openLoadMore(PAGESIZE);
        adapter.setOnLoadMoreListener(this);
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration( new RecycleItemDivider( this.getContext() , LinearLayoutManager.VERTICAL));


        View header = LayoutInflater.from(getContext()).inflate(R.layout.layout_group_header,null);
        //ButterKnife.bind( this ,header  );
        dot = (LinearLayout) header.findViewById(R.id.dot);
        homeViewPager = (ViewPager) header.findViewById(R.id.homeViewPager);

        adapter.addHeaderView(header);


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

    private void loadData( int idx ) {
        //homePullRefresh.setMode(PullToRefreshBase.Mode.BOTH);
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

                            //Articles.clear();
                            //TopArticles.clear();
                            //Articles.addAll(response.body().getData().getList().getRows());
                            //TopArticles.addAll(response.body().getData().getTop());
                            //adapter.notifyDataSetChanged();
                            //pageIndex++;
                        } else if (operateType == OperateTypeEnum.LOADMORE) {
                            if (response.body().getData().getList().getRows().size()==0){
                                //adapter.notifyDataSetChanged();
                                //pageIndex=pageIndex-1;
                                //ToastUtils.showLongToast("没有更多信息...");
                                if(noDataView==null){
                                    noDataView = getActivity().getLayoutInflater().inflate(R.layout.layout_nodata, (ViewGroup) recyclerView.getParent(), false);
                                }
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

                return;



            }


            @Override
            public void onFailure(Call<ArticleListOutput> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showLongToast("失败");
            }
        });
    }

    private void initSwitchImg() {
        adDataList = new ArrayList<>();

        //通过适配器引入图片
        homeBannerPagerAdapter=new HomeBannerPagerAdapter(adDataList, getContext(),mhandler);
        homeViewPager.setAdapter(homeBannerPagerAdapter);
        homeViewPager.setCurrentItem(0);

        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("type","0");
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        String token = BaseApplication.readToken();
        Call<SlideListOutputModel> call = apiService.FocusPic(token,map);
        call.enqueue(new Callback<SlideListOutputModel>() {
            @Override
            public void onResponse(Call<SlideListOutputModel> call, Response<SlideListOutputModel> response) {
                if (response.body() != null) {
                    if (response.body().getStatus() == 200&&response.body().getData()!=null) {
                        adDataList.addAll(response.body().getData());
                        initDots();
                        //通过适配器引入图片
                        homeViewPager.setAdapter(homeBannerPagerAdapter);
                        homeViewPager.setCurrentItem(0);
                        initListener();
                        //更新文本内容
                        updateTextAndDot();
                        homeBannerPagerAdapter.notifyDataSetChanged();
                        ToastUtils.showLongToast("成功");
                    } else {
                        ToastUtils.showLongToast(response.body().getStatusText());
                    }

                }

                return;



            }


            @Override
            public void onFailure(Call<SlideListOutputModel> call, Throwable t) {
                ToastUtils.showLongToast("失败");
            }
        });
        initListener();
        initDots();
        //更新文本内容
        updateTextAndDot();
    }
    /**
     * 初始化监听器
     */
    @SuppressWarnings("deprecation")
    private void initListener() {
        homeViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                updateTextAndDot();

            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void initDots() {
        for (int i = 0; i < adDataList.size(); i++) {
            View view = new View(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8, 8);
            if (i != 0) {
                params.leftMargin = 5;
            }

            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.selecter_dot);
            dot.addView(view);
        }
    }
    private void updateTextAndDot() {
        int currentPage = homeViewPager.getCurrentItem();

        //改变dot
        for (int i = 0; i < dot.getChildCount(); i++) {
            dot.getChildAt(i).setEnabled(i == currentPage);
        }

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
        initSwitchImg();
        loadData(pageIndex);
    }

    @Override
    public void onLoadMoreRequested() {
        operateType = OperateTypeEnum.LOADMORE;
        loadData(pageIndex+1);
    }
}
