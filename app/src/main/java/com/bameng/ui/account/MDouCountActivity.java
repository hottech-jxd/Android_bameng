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
import com.bameng.adapter.ApplyCashAdapter;
import com.bameng.adapter.MBeanFlowAdapter;
import com.bameng.adapter.WaitMBeanFlowAdapter;
import com.bameng.config.Constants;
import com.bameng.model.BeanFlowOutputModel;
import com.bameng.model.CloseEvent;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.UserOutputsModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/***
 * 待结算盟豆
 */
public class MDouCountActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener{

    @BindView(R2.id.mdou_count)
    TextView mdou_count;
    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.recycleView)
    RecyclerView recyclerView;

    WaitMBeanFlowAdapter adapter;
    View noDataView;
    View emptyView;
    OperateTypeEnum operateTypeEnum = OperateTypeEnum.REFRESH;
    long lastid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdou_count);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void initView() {
        titleText.setText("待结算盟豆");
        //Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        //SystemTools.loadBackground(titleLeftImage, leftDraw);
        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);

        mdou_count.setText( String.valueOf( BaseApplication.UserData().getTempMengBeans() ));

        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new WaitMBeanFlowAdapter();
        adapter.setOnLoadMoreListener(this);

        emptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty ,  (ViewGroup) recyclerView.getParent(),false);
        adapter.setEmptyView(emptyView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }
        });

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
        operateTypeEnum= OperateTypeEnum.REFRESH;
        lastid=0;
        loadData();
    }

    void loadData(){
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("lastId",String.valueOf(lastid));
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<BeanFlowOutputModel> call = apiService.tempsettlebeanlist(token, map);
        call.enqueue(new Callback<BeanFlowOutputModel>() {
            @Override
            public void onResponse(Call<BeanFlowOutputModel> call, Response<BeanFlowOutputModel> response) {
                swipeRefreshLayout.setRefreshing(false);

                if(response.code() !=200){
                    ToastUtils.showLongToast(response.message());
                    return;
                }
                if(response.body()==null){
                    ToastUtils.showLongToast("返回空数据");
                    return;
                }
                if(response.body().getStatus() == Constants.STATUS_70035){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(MDouCountActivity.this , PhoneLoginActivity.class);
                    return;
                }
                if(response.body().getStatus()!=200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }
                if(operateTypeEnum == OperateTypeEnum.REFRESH){
                    mdou_count.setText( String.valueOf(response.body().getData().getTempMengBeans() ) );

                    adapter.setNewData( response.body().getData().getList() );
                    if(response.body().getData().getList()!=null && response.body().getData().getList().size()>0){
                        lastid = response.body().getData().getList().get( response.body().getData().getList().size()-1 ).getID();
                    }
                }else{
                    if(response.body().getData().getList()==null|| response.body().getData().getList().size()<1){
                        if (noDataView == null) {
                            noDataView = LayoutInflater.from(MDouCountActivity.this).inflate(R.layout.layout_nodata, null);
                        }
                        adapter.removeAllFooterView();
                        adapter.addFooterView(noDataView);
                        adapter.loadComplete();
                        return;
                    }

                    adapter.addData(response.body().getData().getList());
                    lastid = response.body().getData().getList().get( response.body().getData().getList().size()-1 ).getID();
                }
            }

            @Override
            public void onFailure(Call<BeanFlowOutputModel> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showLongToast( Constants.SERVER_ERROR );
            }
        });

    }

    @Override
    public void onLoadMoreRequested() {
        operateTypeEnum = OperateTypeEnum.LOADMORE;
        loadData();
    }
}
