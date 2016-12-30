package com.bameng.ui.account;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.adapter.CustomerResourceAdapter;
import com.bameng.adapter.WorkReportAdapter;
import com.bameng.config.Constants;
import com.bameng.model.CloseEvent;
import com.bameng.model.CustomerModel;
import com.bameng.model.CustomerResourceModel;
import com.bameng.model.ListModel;
import com.bameng.model.MyOutputModel;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.WorkReportModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.WebViewActivity;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.business.CustomerDetailsActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.DensityUtils;
import com.bameng.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.baidu.mapapi.BMapManager.getContext;

/***
 * 客户资源 界面
 */
public class CustomerActivity extends BaseActivity
        implements  SwipeRefreshLayout.OnRefreshListener , BaseQuickAdapter.RequestLoadMoreListener{
    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
//    @BindView(R2.id.titleRightImage)
//    ImageView titleRightImage;
//    @BindView(R.id.titleRightText)
//    TextView titleRightText;
    @BindView(R.id.layTitleRight)
    LinearLayout layTitleRight;
    @BindView(R2.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.recycleView)
    RecyclerView recyclerView;

    BaseQuickAdapter baseAdapter;
    OperateTypeEnum operateType= OperateTypeEnum.REFRESH;
    View noDataView;
    View emptyView;
    int pageIndex=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        ButterKnife.bind(this);
        initView();
        StartApi();
    }

    @Override
    protected void initView() {
        String title="客户资源";
        titleText.setText(title);

        layTitleRight.setBackgroundResource(R.drawable.item_click_selector);

        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);

        baseAdapter = new CustomerResourceAdapter();
        emptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty ,  (ViewGroup) recyclerView.getParent(),false);
        baseAdapter.setEmptyView(emptyView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout.setOnRefreshListener(this);
        baseAdapter.openLoadMore(Constants.PAGESIZE);
        baseAdapter.setOnLoadMoreListener(this);
        recyclerView.setAdapter(baseAdapter);
        recyclerView.addOnItemTouchListener(  new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position ) {
                CustomerResourceModel model = (CustomerResourceModel) baseQuickAdapter.getItem(position);

                CustomerModel cModel = new CustomerModel();
                cModel.setMobile(model.getMobile());
                cModel.setName(model.getName());
                cModel.setAddr(model.getAddr());
                cModel.setDataImg(model.getDataImg());
                cModel.setIsSave( model.getType() );
                cModel.setRemark(model.getRemark());
//                cModel.setShowInShop(false);
//                cModel.setShowMengYou(false);
//                cModel.setShowStatus(false);

                Bundle bundle = new Bundle();
                bundle.putBoolean("showInShop",false);
                bundle.putBoolean("showMengYou",false);
                bundle.putBoolean("showStatus",false);
                bundle.putSerializable("customerinfo", cModel);
                ActivityUtils.getInstance().showActivity( CustomerActivity.this , CustomerDetailsActivity.class,bundle);
            }});



    }

    @Override
    protected void StartApi() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
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


    private void loadData(int pidx ) {

        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("pageIndex",String.valueOf(pidx));
        map.put("pageSize",String.valueOf(Constants.PAGESIZE));
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<MyOutputModel<CustomerResourceModel>> call = apiService.reslist( token,map);
        call.enqueue(new Callback<MyOutputModel<CustomerResourceModel>>() {
            @Override
            public void onResponse(Call<MyOutputModel<CustomerResourceModel>> call, Response<MyOutputModel<CustomerResourceModel>> response) {
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
                    ActivityUtils.getInstance().skipActivity(CustomerActivity.this, PhoneLoginActivity.class);
                    return;
                }

                if (response.body().getStatus() != 200) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }
                if( response.body().getData()== null ) {
                    ToastUtils.showLongToast(Constants.SERVER_ERROR);
                    return;
                }

                if (response.body().getStatus() == 200 && response.body() != null) {
                    if (operateType == OperateTypeEnum.REFRESH) {
                        List<CustomerResourceModel> Articles = new ArrayList<>();
                        if (response.body().getData() != null && response.body().getData().getRows() != null) {
                            Articles.addAll(response.body().getData().getRows());
                        }
                        baseAdapter.setNewData(Articles);

                    } else if (operateType == OperateTypeEnum.LOADMORE) {

                        if (response.body().getData().getRows().size() < 1) {
                            if (noDataView == null) {
                                noDataView = CustomerActivity.this.getLayoutInflater().inflate(R.layout.layout_nodata, (ViewGroup) recyclerView.getParent(), false);
                            }
                            baseAdapter.removeAllFooterView();
                            baseAdapter.addFooterView(noDataView);
                            baseAdapter.loadComplete();
                        } else {
                            baseAdapter.addData(response.body().getData().getRows());
                            pageIndex = pageIndex + 1;
                            //updateBadge();
                        }

                    }
                } else {
                    baseAdapter.showLoadMoreFailedView();
                }
            }


            @Override
            public void onFailure(Call<MyOutputModel<CustomerResourceModel>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showLongToast(Constants.SERVER_ERROR);
            }
        });
    }

}
