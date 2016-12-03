package com.bameng.ui.account;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.adapter.MBeanFlowAdapter;
import com.bameng.config.Constants;
import com.bameng.model.BeanFlowOutputModel;
import com.bameng.model.CloseEvent;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.PostModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.ui.news.AddnewsActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/***
 * 盟豆 流水 界面
 */
public class MyBeanActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener{

    @BindView(R2.id.txt_income)
    TextView txtIncome;
    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.txt_outbean)
    TextView txtOutbean;
    @BindView(R2.id.recycleView)
    RecyclerView recyclerView;
    @BindView(R2.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    MBeanFlowAdapter adapter;
    final  int PAGESIZE = 10;
    long lastId=0;
    View noDataView;
    View emptyView;
    OperateTypeEnum operateTypeEnum = OperateTypeEnum.REFRESH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bean);
        ButterKnife.bind(this);
        initView();
        //StartApi();
    }

    @Override
    protected void initView() {
        titleText.setText("盟豆");
        //Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        //SystemTools.loadBackground(titleLeftImage, leftDraw);
        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);

        adapter = new MBeanFlowAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter.setOnLoadMoreListener(this);
        adapter.openLoadMore(PAGESIZE);

        emptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty, (ViewGroup)recyclerView.getParent(),false);
        adapter.setEmptyView(emptyView);

        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }


    @Override
    public void onRefresh() {
        operateTypeEnum = OperateTypeEnum.REFRESH;
        lastId=0;
        loadData();
    }

    @Override
    public void onLoadMoreRequested() {
        operateTypeEnum = OperateTypeEnum.LOADMORE;
        loadData();
    }

    protected void loadData(){
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("lastId", String.valueOf( lastId ));
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<BeanFlowOutputModel> call = apiService.BeanFlowList(token,map);
        call.enqueue(new Callback<BeanFlowOutputModel>() {
            @Override
            public void onResponse(Call<BeanFlowOutputModel> call, Response<BeanFlowOutputModel> response) {
                swipeRefreshLayout.setRefreshing(false);
                if(response.code()!=200){
                    ToastUtils.showLongToast(response.message());
                    return;
                }
                if(response.body()==null){
                    ToastUtils.showLongToast("服务端返回空数据");
                    return;
                }
                if(response.body().getStatus() == Constants.STATUS_70035){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(MyBeanActivity.this , PhoneLoginActivity.class);
                    return;
                }
                if(response.body().getStatus()!=200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }
                if( response.body().getData()==null){
                    return;
                }


                if(operateTypeEnum == OperateTypeEnum.REFRESH){

                    txtIncome.setText( String.valueOf( response.body().getData().getIncome() ));
                    txtOutbean.setText( String.valueOf( response.body().getData().getOutcome() ) );

                    adapter.setNewData( response.body().getData().getList() );
                    if( response.body().getData().getList().size()>0){
                        lastId = response.body().getData().getList().get( response.body().getData().getList().size()-1 ).getID();
                    }
                }else{
                    if (response.body().getData().getList() == null || response.body().getData().getList().size()<1) {
                        if (noDataView == null) {
                            noDataView = MyBeanActivity.this.getLayoutInflater().inflate(R.layout.layout_nodata, (ViewGroup) recyclerView.getParent(), false);
                        }
                        adapter.removeAllFooterView();
                        adapter.addFooterView(noDataView);
                        adapter.loadComplete();
                        return;
                    }

                    adapter.addData( response.body().getData().getList());

                    lastId = response.body().getData().getList().get( response.body().getData().getList().size()-1 ).getID();
                }

            }

            @Override
            public void onFailure(Call<BeanFlowOutputModel> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showLongToast(t.getMessage()==null?"请求失败":t.getMessage());
            }
        });

    }
}
