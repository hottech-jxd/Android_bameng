package com.bameng.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bameng.model.OperateTypeEnum;
import com.bameng.model.SlideListModel;
import com.bameng.model.SlideListOutputModel;
import com.bameng.model.TopArticleIdModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseFragment;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.MyListView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bameng.R.id.homePullRefresh;

/**
 * 分店资讯
 * Created by 47483 on 2016.11.01.
 */
public class ShopFrag extends StoreFrag {
    //int pageIndex=1;
    //public OperateTypeEnum operateType= OperateTypeEnum.REFRESH;
    //public List<ListModel> Articles;
    //public List<ListModel> TopArticles;
    //public ArticleAdapter adapter;

    //@BindView(R2.id.homePullRefresh)
    //PullToRefreshListView homePullRefresh;
    public ShopFrag(){
        this.type=2;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initView();
        //ButterKnife.bind(this, view);

    }
//    public void initView() {
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        swipeRefreshLayout.setOnRefreshListener(this);
//
//        storeAdapter = new StoreAdapter();
//        //storeAdapter.openLoadAnimation();
//        storeAdapter.openLoadMore(PAGESIZE);
//        storeAdapter.setOnLoadMoreListener(this);
//        recyclerView.setAdapter(storeAdapter);
//        //mCurrentCounter = storeAdapter.getData().size();
//
//        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
//            @Override
//            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Toast.makeText(getActivity() , Integer.toString(position), Toast.LENGTH_LONG).show();
//            }
//        });
//
//
//
//        loadData();
//        initlist();
//    }
//
//
//    public void initlist(){
//        Articles = new ArrayList<ListModel>();
//        TopArticles = new ArrayList<ListModel>();
//        adapter = new ArticleAdapter(Articles,TopArticles, getActivity(), getActivity());
//        homePullRefresh.setAdapter(adapter);
//        homePullRefresh.setMode(PullToRefreshBase.Mode.BOTH);
//        homePullRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
//                operateType = OperateTypeEnum.REFRESH;
//                pageIndex=1;
//                Articles.clear();
//                loadData();
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
//                operateType = OperateTypeEnum.LOADMORE;
//                pageIndex= pageIndex+1;
//                loadData();
//
//            }
//        });
//    }

//    private void loadData() {
//        homePullRefresh.setMode(PullToRefreshBase.Mode.BOTH);
//        Map<String, String> map = new HashMap<>();
//        map.put("version", application.getAppVersion());
//        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
//        map.put("os", "android");
//        map.put("identity","2");
//        map.put("pageIndex",String.valueOf(pageIndex));
//        map.put("pageSize","10");
//        AuthParamUtils authParamUtils = new AuthParamUtils();
//        String sign = authParamUtils.getSign(map);
//        map.put("sign", sign);
//        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
//        String token = application.readToken();
//        Call<ArticleListOutput> call = apiService.list(token,map);
//        call.enqueue(new Callback<ArticleListOutput>() {
//            @Override
//            public void onResponse(Call<ArticleListOutput> call, Response<ArticleListOutput> response) {
//                if (response.body() != null) {
//                    homePullRefresh.onRefreshComplete();
//
//                    ArticleListOutput articleListOutput = new ArticleListOutput();
//                    articleListOutput.setData(response.body().getData());
//                    if (response.body().getStatus() == 200&&response.body()!=null) {
//                        if (operateType == OperateTypeEnum.REFRESH) {
//                            Articles.clear();
//                            TopArticles.clear();
//                            Articles.addAll(response.body().getData().getList().getRows());
//                            TopArticles.addAll(response.body().getData().getTop());
//                            adapter.notifyDataSetChanged();
//                        } else if (operateType == OperateTypeEnum.LOADMORE) {
//                            if (response.body().getData().getList().getRows().size()==0){
//                                adapter.notifyDataSetChanged();
//                                pageIndex=pageIndex-1;
//                                ToastUtils.showLongToast("没有更多信息...");
//                            }
//
//                        }
//                    } else {
//                        ToastUtils.showLongToast(response.body().getStatusText());
//                    }
//
//                }
//
//                return;
//
//
//
//            }
//
//
//            @Override
//            public void onFailure(Call<ArticleListOutput> call, Throwable t) {
//                homePullRefresh.onRefreshComplete();
//
//                ToastUtils.showLongToast("失败");
//            }
//        });
//    }

    @Override
    public void onReshow() {

    }

    @Override
    public void onFragPasue() {

    }

    @Override
    public void onClick(View view) {

    }
//    @Override
//    public int getLayoutRes() {
//        return R.layout.frag_shop;
//    }


    @Override
    public String getPageTitle() {
        return "分店资讯";
    }
}
