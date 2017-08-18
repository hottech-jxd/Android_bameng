package com.bameng.ui.business;

import android.os.Message;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.adapter.CustomerRecordAdapter;
import com.bameng.config.Constants;
import com.bameng.model.BaseModel;
import com.bameng.model.CloseEvent;
import com.bameng.model.CustomerModel;
import com.bameng.model.CustomerRecordOutputModel;
import com.bameng.model.OperateTypeEnum;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.InputDialogView;
import com.bameng.widgets.RecycleItemDivider;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.start;
import static android.R.attr.trimPathStart;
import static android.R.attr.type;
import static android.R.id.input;
import static com.baidu.mapapi.BMapManager.getContext;
import static com.bameng.config.Constants.PAGESIZE;
import static com.umeng.analytics.a.p;

public class CustomerRecordActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener ,BaseQuickAdapter.RequestLoadMoreListener , InputDialogView.OnInputBackListener{
    @BindView(R.id.recycleView)
    RecyclerView recyclerView;
    @BindView(R.id.titleText)
    TextView titleText;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.titleLeftImage)
    ImageView titleLeftImage;
    @BindView(R.id.titleRightImage)
    ImageView titleRightImage;
    @BindView(R.id.titleRightText)
    TextView titleRightText;
    CustomerModel customerModel;

    View emptyView;
    CustomerRecordAdapter customerRecordAdapter;
    OperateTypeEnum operateType= OperateTypeEnum.REFRESH;
    int pageIndex= 1;
    View noDataView;
    InputDialogView inputDialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        StartApi();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_customer_record);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        titleText.setText("客户动态");


        if(getIntent().hasExtra("customer")){
            customerModel = (CustomerModel) getIntent().getSerializableExtra("customer");
        }


        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);
        titleRightImage.setBackgroundResource(R.drawable.title_left_back);
        titleRightImage.setImageResource(R.mipmap.ic_newadd);
        titleRightText.setText("新增");
        titleRightText.setBackgroundResource(R.drawable.item_click_selector);

        recyclerView.addItemDecoration(new RecycleItemDivider( getContext() , RecyclerView.VERTICAL , 1, R.color.dividerColor ));
        emptyView =  getLayoutInflater().inflate(R.layout.layout_empty, (ViewGroup)recyclerView.getParent() , false);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        customerRecordAdapter = new CustomerRecordAdapter();
        customerRecordAdapter.openLoadMore(PAGESIZE);
        //customerRecordAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        customerRecordAdapter.setOnLoadMoreListener(this);
        customerRecordAdapter.setEmptyView(emptyView);
        customerRecordAdapter.isUseEmpty(false);
        recyclerView.setAdapter(customerRecordAdapter);
    }

    @OnClick({R.id.titleRightImage,R.id.titleRightText})
   public  void onClick(View view ){
        if( inputDialogView==null){
            inputDialogView=new InputDialogView(this);
            inputDialogView.setOnUserInfoBackListener(this);
        }

        inputDialogView.show();
    }

    @Override
    public void onInputBack(String value) {
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("cid", String.valueOf( customerModel.getID()));
        map.put("content",  value );
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<BaseModel> call = apiService.addAssert(token,map);
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if(response.code()!=200 || response.body()==null ){
                    ToastUtils.showLongToast( response.message()==null? "失败": response.message());
                    return;
                }

                if (response.body().getStatus() == Constants.STATUS_70035) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(CustomerRecordActivity.this , PhoneLoginActivity.class);
                    return;
                }
                if( response.body().getStatus() !=200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }

                ToastUtils.showShortToast(response.body().getStatusText());

                StartApi();
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                ToastUtils.showLongToast(Constants.SERVER_ERROR);
            }
        });
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

        if( noDataView!=null && noDataView.getParent()!=null){
            ((ViewGroup)noDataView.getParent()).removeView(noDataView);
        }
        customerRecordAdapter.isUseEmpty(false);
        loadData( pageIndex  );
    }

    @Override
    public void onLoadMoreRequested() {
        operateType = OperateTypeEnum.LOADMORE;
        loadData( pageIndex+1 );
    }

    void loadData( final long lid ){
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("cid", String.valueOf( customerModel.getID()));
        map.put("pageSize",String.valueOf(Constants.PAGESIZE));
        map.put("pageIndex",String.valueOf(pageIndex));
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<CustomerRecordOutputModel> call = apiService.assertList(token,map);
        call.enqueue(new Callback<CustomerRecordOutputModel>() {
            @Override
            public void onResponse(Call<CustomerRecordOutputModel> call, Response<CustomerRecordOutputModel> response) {
                swipeRefreshLayout.setRefreshing(false);
                customerRecordAdapter.isUseEmpty(true);

                if(response.code()!=200 || response.body()==null ){
                    ToastUtils.showLongToast( response.message()==null? "失败": response.message());
                    return;
                }

                if (response.body().getStatus() == Constants.STATUS_70035) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(CustomerRecordActivity.this , PhoneLoginActivity.class);
                    return;
                }
                if( response.body().getStatus() !=200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }

                if( operateType == OperateTypeEnum.REFRESH){
                    //orderAdapter.setEmptyView(emptyView);
                    customerRecordAdapter.setNewData( response.body().getData().getRows() );
                    if(response.body().getData().getRows()!=null && response.body().getData().getRows().size()>0) {
                        //pageIndex = response.body().getData().getList().get(response.body().getData().getList().size() - 1).getId();
                        pageIndex++;
                    }
                }else {
                    if (response.body().getData().getRows() == null || response.body().getData().getRows().size()<1) {
                        if (noDataView == null) {
                            noDataView = getLayoutInflater().inflate(R.layout.layout_nodata,  (ViewGroup) recyclerView.getParent() , false);
                            customerRecordAdapter.addFooterView( noDataView );
                        }else{
                            if( noDataView.getParent()!=null){
                                ((ViewGroup)noDataView.getParent()).removeView( noDataView);
                            }
                            customerRecordAdapter.addFooterView(noDataView);
                        }
                        customerRecordAdapter.loadComplete();
                        return;
                    }

                    customerRecordAdapter.addData( response.body().getData().getRows());
                    pageIndex++;
                    //lastId = response.body().getData().getList().get( response.body().getData().getList().size()-1 ).getId();
                }

            }

            @Override
            public void onFailure(Call<CustomerRecordOutputModel> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                customerRecordAdapter.isUseEmpty(true);
                ToastUtils.showLongToast(Constants.SERVER_ERROR);
            }
        });

    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
