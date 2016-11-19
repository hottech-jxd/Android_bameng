package com.bameng.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;


import com.bameng.R;
import com.bameng.adapter.ArticleAdapter;
import com.bameng.adapter.HomeBannerPagerAdapter;
import com.bameng.config.Constants;
import com.bameng.model.AdlistModel;
import com.bameng.model.ArticleListOutput;
import com.bameng.model.ArticleModel;
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
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.MyListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/***
 * 霸盟 首页
 */
public class HomeFragment extends BaseFragment  implements AdapterView.OnItemClickListener {

    //public HomeActivity rootAty;
    @Bind(R.id.homePullRefresh)
    PullToRefreshScrollView homePullRefresh;

    @Bind(R.id.dot)
    LinearLayout dot;

    @Bind(R.id.homeViewPager)
    ViewPager homeViewPager;
    @Bind(R.id.listL)
    MyListView listL;
    int pageIndex=1;
    public OperateTypeEnum operateType= OperateTypeEnum.REFRESH;
    public List<SlideListModel> adDataList ;
    public HomeBannerPagerAdapter homeBannerPagerAdapter;
    public List<ListModel> Articles;
    public List<ListModel> TopArticles;
    public ArticleAdapter adapter;

    private Handler mhandler;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        ButterKnife.bind(this, view);
    }

    public void initView() {

        initList();
        loadData();
        initProduct();
        initSwitchImg();
    }
    public void initProduct(){
        homePullRefresh.setMode(PullToRefreshBase.Mode.BOTH);
        homePullRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                operateType = OperateTypeEnum.REFRESH;
                pageIndex=1;
                Articles.clear();
                loadData();
                initSwitchImg();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                operateType = OperateTypeEnum.LOADMORE;
                pageIndex= pageIndex+1;
                loadData();

            }
        });

    }

    public void initList(){
        Articles = new ArrayList<ListModel>();
        TopArticles = new ArrayList<ListModel>();
        adapter = new ArticleAdapter(Articles,TopArticles, getActivity(), getActivity());
        listL.setAdapter(adapter);

        listL.setOnItemClickListener(this);
    }
    @OnClick({R.id.layaccount,R.id.layneworder,R.id.layCustomer,R.id.layally,R.id.layExchange,R.id.layReward , R.id.layMore})
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


    private void loadData() {
        homePullRefresh.onRefreshComplete();
        homePullRefresh.setMode(PullToRefreshBase.Mode.BOTH);
        Map<String, String> map = new HashMap<>();
        map.put("version", application.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("identity","0");
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
                        if (operateType == OperateTypeEnum.REFRESH) {
                            Articles.clear();
                            TopArticles.clear();
                            Articles.addAll(response.body().getData().getList().getRows());
                            TopArticles.addAll(response.body().getData().getTop());
                            adapter.notifyDataSetChanged();
                        } else if (operateType == OperateTypeEnum.LOADMORE) {
                            if (response.body().getData().getList().getRows().size()==0){
                                adapter.notifyDataSetChanged();
                                pageIndex=pageIndex-1;
                                ToastUtils.showLongToast("没有更多信息...");
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
                ToastUtils.showLongToast("失败");
            }
        });
    }

    public void scrollToTop() {
        homePullRefresh.getRefreshableView().smoothScrollBy(0, 0);
    }

    private void initSwitchImg() {
        Map<String, String> map = new HashMap<>();
        map.put("version", application.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("type","2");
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        String token = application.readToken();
        Call<SlideListOutputModel> call = apiService.FocusPic(token,map);
        call.enqueue(new Callback<SlideListOutputModel>() {
            @Override
            public void onResponse(Call<SlideListOutputModel> call, Response<SlideListOutputModel> response) {
                if (response.body() != null) {
//                    PostModel initOutputs = new PostModel();
//                    initOutputs.setData( response.body().getData());

                    if (response.body().getStatus() == 200&&response.body().getData()!=null) {
                        adDataList= new ArrayList<SlideListModel>();
                        homeBannerPagerAdapter = new HomeBannerPagerAdapter(adDataList, getContext(), mhandler);
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
        //if(adDataList==null || adDataList.size()<1 ) return;



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
    private void updateTextAndDot() {
        int currentPage = homeViewPager.getCurrentItem();

        //改变dot
        for (int i = 0; i < dot.getChildCount(); i++) {
            dot.getChildAt(i).setEnabled(i == currentPage);
        }

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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
        return R.layout.fragment_home;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String url;
        Object obj = adapter.getItem(i);
        if( obj instanceof TopArticleIdModel){
            TopArticleIdModel model = (TopArticleIdModel) obj;
            url = model.getArticleUrl();
        }else{
            ListModel model = (ListModel) obj;
            url = model.getArticleUrl();
        }

        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        Bundle bd = new Bundle();
        bd.putString(Constants.INTENT_URL, url);
        ActivityUtils.getInstance().showActivity(getActivity(), WebViewActivity.class , bd);
    }
}
