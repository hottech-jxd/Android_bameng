package com.bameng.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;


import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.adapter.HomeBannerPagerAdapter;
import com.bameng.adapter.StoreAdapter;
import com.bameng.config.Constants;
import com.bameng.model.AdBannerConfig;
import com.bameng.model.AdlistModel;
import com.bameng.model.ArticleListOutput;
import com.bameng.model.ArticleModel;
import com.bameng.model.CloseEvent;
import com.bameng.model.ListModel;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.PostModel;
import com.bameng.model.SlideListModel;
import com.bameng.model.SlideListOutputModel;
import com.bameng.model.SwitchFragmentEvent;
import com.bameng.model.TopArticleIdModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.HomeActivity;
import com.bameng.ui.WebViewActivity;
import com.bameng.ui.account.ExchangeRecordActivity;
import com.bameng.ui.account.MyAccountActivity;
import com.bameng.ui.base.BaseFragment;
import com.bameng.ui.business.AlliesDetailsActivity;
import com.bameng.ui.business.CustomerInfoActivity;
import com.bameng.ui.business.ExchangeExamineActivity;
import com.bameng.ui.business.MyAlliesActivity;
import com.bameng.ui.business.NewOrderActivity;
import com.bameng.ui.business.RwordActivity;
import com.bameng.ui.login.PhoneLoginActivity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/***
 * 霸盟 首页
 */
public class HomeFragment extends BaseFragment  implements  SwipeRefreshLayout.OnRefreshListener
 ,BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener{

    @BindView(R.id.homePullRefresh)
    SwipeRefreshLayout homePullRefresh;
    @BindView(R.id.listL)
    RecyclerView recyclerView;

    LinearLayout layBanner;

    AdBannerWidget adBannerWidget;

    int pageIndex=1;
    public OperateTypeEnum operateType= OperateTypeEnum.REFRESH;
    public List<SlideListModel> adDataList;
    StoreAdapter adapter;
    View header;
    View emptyView;
    View noDataView;
    int PAGESIZE=10;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

    }

    @Override
    protected void loadData() {
        homePullRefresh.post(new Runnable() {
            @Override
            public void run() {
                homePullRefresh.setRefreshing(true);
                onRefresh();
            }
        });
    }

    public void initView(){
        homePullRefresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.addItemDecoration(new RecycleItemDivider(getContext(),RecyclerView.VERTICAL, 8 , R.color.dividerColor));

        adapter = new StoreAdapter();
        adapter.setOnLoadMoreListener(this);

        emptyView = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty, (ViewGroup) recyclerView.getParent());
        adapter.setEmptyView(emptyView);

        adapter.openLoadMore(PAGESIZE);


        recyclerView.setAdapter(adapter);

        header = LayoutInflater.from(getContext()).inflate(R.layout.layout_home_header , null );

        adBannerWidget = (AdBannerWidget) header.findViewById(R.id.adbannerWidget);
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

        adapter.addHeaderView(header);

        layBanner = (LinearLayout) header.findViewById(R.id.layBanner);

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                ListModel model = (ListModel) baseQuickAdapter.getItem(i);
                Bundle bd = new Bundle();
                bd.putString(Constants.INTENT_URL, model.getArticleUrl());
                ActivityUtils.getInstance().showActivity(getActivity(), WebViewActivity.class , bd);
            }
        });


        LinearLayout layaccount = (LinearLayout) header.findViewById(R.id.layaccount);
        layaccount.setOnClickListener(this);
        LinearLayout layneworder = (LinearLayout) header.findViewById(R.id.layneworder);
        layneworder.setOnClickListener(this);
        LinearLayout layCustomer = (LinearLayout) header.findViewById(R.id.layCustomer);
        layCustomer.setOnClickListener(this);
        LinearLayout layally = (LinearLayout) header.findViewById(R.id.layally);
        layally.setOnClickListener(this);
        LinearLayout layExchange = (LinearLayout) header.findViewById(R.id.layExchange);
        layExchange.setOnClickListener(this);
        LinearLayout layReward = (LinearLayout) header.findViewById(R.id.layReward);
        layReward.setOnClickListener(this);
        RelativeLayout layMore = (RelativeLayout) header.findViewById(R.id.layMore);
        layMore.setOnClickListener(this);
    }

    //@OnClick({R.id.layaccount,R.id.layneworder,R.id.layCustomer,R.id.layally,R.id.layExchange,R.id.layReward , R.id.layMore})
     void click(View v) {
        switch (v.getId()) {
            case R.id.layaccount:
                ActivityUtils.getInstance().showActivity(getActivity(), MyAccountActivity.class);
                break;
            case R.id.layneworder:
                ActivityUtils.getInstance().showActivity(getActivity(), NewOrderActivity.class);
                break;
            case R.id.layCustomer:
                ActivityUtils.getInstance().showActivity(getActivity(), CustomerInfoActivity.class);
                break;
            case R.id.layally:
                ActivityUtils.getInstance().showActivity(getActivity(), MyAlliesActivity.class);
                break;
            case R.id.layExchange:
                ActivityUtils.getInstance().showActivity(getActivity(), ExchangeExamineActivity.class);
                break;
            case R.id.layReward:
                ActivityUtils.getInstance().showActivity(getActivity(), RwordActivity.class);
                break;
            case R.id.layMore:
                EventBus.getDefault().post(new SwitchFragmentEvent(Constants.TAG_2));
                break;
            default:
                break;

        }
    }

    private void loadData(int idx) {
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("identity","0");
        map.put("pageIndex",String.valueOf(idx));
        map.put("pageSize", String.valueOf(PAGESIZE));
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<ArticleListOutput> call = apiService.list(token,map);
        call.enqueue(new Callback<ArticleListOutput>() {
            @Override
            public void onResponse(Call<ArticleListOutput> call, Response<ArticleListOutput> response) {
                homePullRefresh.setRefreshing(false);
                if (response.code() != 200) {
                    ToastUtils.showLongToast(response.message());
                    return;
                }
                if (response.body() == null) {
                    adapter.showLoadMoreFailedView();
                    return;
                }
                if(response.body().getStatus()==Constants.STATUS_70035){
                    ToastUtils.showLongToast( response.body().getStatusText() );
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(getActivity(), PhoneLoginActivity.class);
                    return;
                }
                if (response.body().getStatus() != 200) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }

                if (response.body().getData() == null) {
                    ToastUtils.showLongToast("返回错误数据");
                    return;
                }

                if (operateType == OperateTypeEnum.REFRESH) {
                    List<ListModel> Articles = new ArrayList<>();
                    List<ListModel> topList = response.body().getData().getTop();
                    if (topList != null && topList.size()>0 ) {
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
                    if (response.body().getData().getList().getRows() == null || response.body().getData().getList().getRows().size() == 0) {
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
            }


            @Override
            public void onFailure(Call<ArticleListOutput> call, Throwable t) {
                homePullRefresh.setRefreshing(false);
                //ToastUtils.showLongToast(t.getMessage()==null?"请求失败":t.getMessage());
                ToastUtils.showLongToast(Constants.SERVER_ERROR);
            }
        });
    }

    private void initSwitchImg() {
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("type","2");
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<SlideListOutputModel> call = apiService.FocusPic(token,map);
        call.enqueue(new Callback<SlideListOutputModel>() {
            @Override
            public void onResponse(Call<SlideListOutputModel> call, Response<SlideListOutputModel> response) {
                if (response.body() != null) {

                    if (response.body().getStatus() == 200&&response.body().getData()!=null) {
                        adDataList= new ArrayList<>();
                        adDataList.addAll(response.body().getData());
                        AdBannerConfig config = new AdBannerConfig();
                        config.setAutoPlay(true);
                        config.setImages(adDataList);

                        //AdBannerWidget adBannerWidget = new AdBannerWidget(getContext(),config);
                        //layBanner.removeAllViews();
                        //layBanner.addView( adBannerWidget );
                        adBannerWidget.setAdBannerConfig( config );

                    } else {
                        ToastUtils.showLongToast(response.body().getStatusText());
                    }

                }

            }


            @Override
            public void onFailure(Call<SlideListOutputModel> call, Throwable t) {
                //ToastUtils.showLongToast(t.getMessage()==null?"请求失败":t.getMessage());
                ToastUtils.showLongToast(Constants.SERVER_ERROR);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //ButterKnife.unbind(this);
    }

    @Override
    public void onReshow() {

    }

    @Override
    public void onFragPasue() {

    }

    @Override
    public void onClick(View view) {
        click(view);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_home;
    }

    @Override
    public void onRefresh() {
        operateType = OperateTypeEnum.REFRESH;
        pageIndex = 1;
        loadData(pageIndex);
        initSwitchImg();
    }

    @Override
    public void onLoadMoreRequested() {
        operateType = OperateTypeEnum.LOADMORE;
        loadData( pageIndex+1);
    }
}
