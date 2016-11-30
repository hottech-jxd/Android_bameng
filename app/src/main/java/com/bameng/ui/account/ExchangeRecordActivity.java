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
import com.bameng.adapter.ConvertCashAdapter;
import com.bameng.adapter.ScoreAdapter;
import com.bameng.config.Constants;
import com.bameng.model.CloseEvent;
import com.bameng.model.ConvertFlowOutputModel;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.ScoreOutputModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.baidu.location.h.j.ad;
import static com.baidu.location.h.j.t;
import static com.bameng.R.id.status;

/***
 * 兑换记录
 */
public class ExchangeRecordActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener{


    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycleView)
    RecyclerView recyclerView;
    long lastId=0;
    ConvertCashAdapter adapter;
    OperateTypeEnum operateTypeEnum=OperateTypeEnum.REFRESH;
    View noDataView;
    View emptyView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_record);
        ButterKnife.bind(this);
        initView();
        StartApi();

    }

    @Override
    protected void initView() {
        titleText.setText("兑换记录");
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);

        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new ConvertCashAdapter();
        adapter.setOnLoadMoreListener(this);

        emptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty ,  (ViewGroup) recyclerView.getParent(),false);
        adapter.setEmptyView(emptyView);

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void StartApi() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                loadData(lastId);
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void onRefresh() {
        operateTypeEnum = OperateTypeEnum.REFRESH;
        lastId=0;
        loadData(lastId);
    }

    @Override
    public void onLoadMoreRequested() {
        operateTypeEnum= OperateTypeEnum.LOADMORE;
        loadData(lastId+1);
    }

    private void loadData( long id ){

        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("lastId", String.valueOf( id  ) );
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<ConvertFlowOutputModel> call= apiService.ConvertFlow(token, map);
        call.enqueue(new Callback<ConvertFlowOutputModel>() {
            @Override
            public void onResponse(Call<ConvertFlowOutputModel> call, Response<ConvertFlowOutputModel> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.code() != 200) {
                    ToastUtils.showLongToast(response.message());
                    return;
                }

                if (response.body() == null ){
                    ToastUtils.showLongToast("返回错误数据");
                    return;
                }
                if (response.body().getStatus() == Constants.STATUS_70035) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(ExchangeRecordActivity.this , PhoneLoginActivity.class);
                    return;
                }

                if(response.body().getStatus()!=200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }

                if( operateTypeEnum == OperateTypeEnum.REFRESH){

                    adapter.setNewData( response.body().getData().getList() );
                    if( response.body().getData().getList().size()>0) {
                        lastId = response.body().getData().getList().get(response.body().getData().getList().size() - 1).getID();
                    }
                    return;
                }else{
                    if( response.body().getData().getList() ==null || response.body().getData().getList().size()<1 ) {
                        if (noDataView == null) {
                            noDataView = LayoutInflater.from(ExchangeRecordActivity.this).inflate(R.layout.layout_nodata, null);
                        }
                        adapter.removeAllFooterView();
                        adapter.addFooterView(noDataView);
                        adapter.loadComplete();
                        return;
                    }

                    adapter.addData(response.body().getData().getList());
                    lastId = response.body().getData().getList().get( response.body().getData().getList().size()-1 ).getID();
                }

            }

            @Override
            public void onFailure(Call<ConvertFlowOutputModel> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showLongToast( t.getMessage()==null?"请求失败":t.getMessage() );
            }
        });
    }
}
