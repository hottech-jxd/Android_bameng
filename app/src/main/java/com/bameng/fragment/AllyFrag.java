package com.bameng.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.bameng.R;
import com.bameng.adapter.ArticleAdapter;
import com.bameng.model.ArticleListOutput;
import com.bameng.model.ListModel;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.TopArticleIdModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseFragment;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.ToastUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 47483 on 2016.11.01.
 */

public class AllyFrag extends BaseFragment {
    int pageIndex=1;
    public OperateTypeEnum operateType= OperateTypeEnum.REFRESH;
    public List<ListModel> Articles;
    public List<TopArticleIdModel> TopArticles;
    public ArticleAdapter adapter;

    @Bind(R.id.homePullRefresh)
    PullToRefreshListView homePullRefresh;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        ButterKnife.bind(this, view);
    }
    public void initView() {
        loadData();
        initlist();
    }
    public void initlist(){
        Articles = new ArrayList<ListModel>();
        TopArticles = new ArrayList<TopArticleIdModel>();
        adapter = new ArticleAdapter(Articles,TopArticles, getActivity(), getActivity());
        homePullRefresh.setAdapter(adapter);
        homePullRefresh.setMode(PullToRefreshBase.Mode.BOTH);
        homePullRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                operateType = OperateTypeEnum.REFRESH;
                pageIndex=1;
                Articles.clear();
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                operateType = OperateTypeEnum.LOADMORE;
                pageIndex= pageIndex+1;
                loadData();

            }
        });
    }

    private void loadData() {
        homePullRefresh.onRefreshComplete();
        homePullRefresh.setMode(PullToRefreshBase.Mode.BOTH);
        Map<String, String> map = new HashMap<>();
        map.put("version", application.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("identity","3");
        map.put("pageIndex",String.valueOf(pageIndex));
        map.put("pageSize","10");
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        String token = application.readToken();
        Call<ArticleListOutput> call = apiService.list(token,map);
        call.enqueue(new Callback<ArticleListOutput>() {
            @Override
            public void onResponse(Call<ArticleListOutput> call, Response<ArticleListOutput> response) {
                if (response.body() != null) {

                    ArticleListOutput articleListOutput = new ArticleListOutput();
                    articleListOutput.setData(response.body().getData());
                    if (response.body().getStatus() == 200&&response.body()!=null) {
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
                    } else {
                        ToastUtils.showLongToast(response.body().getStatusText());
                    }

                }

                return;



            }


            @Override
            public void onFailure(Call<ArticleListOutput> call, Throwable t) {
                ToastUtils.showLongToast("失败");
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
        return R.layout.frag_ally;
    }
}
