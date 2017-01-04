package com.bameng.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.adapter.MsgAdapter;
import com.bameng.adapter.StoreAdapter;
import com.bameng.biz.UnReadMessageUtil;
import com.bameng.config.Constants;
import com.bameng.model.ArticleListOutput;
import com.bameng.model.BadgeNewEvent;
import com.bameng.model.CloseEvent;
import com.bameng.model.ListModel;
import com.bameng.model.MessageOutputModel;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.PostModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.WebViewActivity;
import com.bameng.ui.base.BaseFragment;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bameng.R.layout.layout_msg_item;

/**
 * Created by 47483 on 2016.11.01.
 */
public class MsgFrag extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener , BaseQuickAdapter.RequestLoadMoreListener {
    int pageIndex=1;
    OperateTypeEnum operateType= OperateTypeEnum.REFRESH;
    @BindView(R2.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.recycleView)
    RecyclerView recyclerView;
    BaseQuickAdapter baseAdapter;
    final int PAGESIZE=10;
    View noDataView;
    View emptyView;
    //1:消息，2：反馈
    protected int type = 1;
    //1：发送的消息，0：接受的消息
    protected int sendType = 1;

    protected  int layoutId = R.layout.layout_msg_item;

    String title = "";

    OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position ) {

            ListModel model = (ListModel) baseQuickAdapter.getItem(position);

            setBadge(model);

            model.setIsRead(1);
            baseQuickAdapter.notifyItemChanged(position);

            Bundle bd = new Bundle();
            //String deviceName = Build.MANUFACTURER;
            bd.putString(Constants.INTENT_URL, model.getArticleUrl());
            bd.putString(Constants.INTENT_TITLE, model.getArticleTitle());
            ActivityUtils.getInstance().showActivity( getActivity() , WebViewActivity.class , bd );
        }
    };

    public MsgFrag(){
        layoutId = R.layout.layout_msg_item;
        baseAdapter = new MsgAdapter ();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        type = getArguments().getInt("type");
        sendType = getArguments().getInt("sendtype");

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /***
     *  处理未读消息的红点显示
     */
    protected void setBadge(ListModel model){
//        if(model.getIsRead()==1) return;
//        int count = BaseApplication.readNewsCount();
//        count = count-1;
//
//        EventBus.getDefault().post( new BadgeNewEvent(count>0));
//        if(count<1) count=0;
//        BaseApplication.writeMessageCount(count);

        if(type== 1){
            if(sendType==1){
                int count = BaseApplication.readMessagePushCount();
                count = count-1;
                if(count<1) count=0;
                BaseApplication.writeMessagePushCount(count);
                EventBus.getDefault().post( new BadgeNewEvent( ));
            }else if(sendType==0){
                int count = BaseApplication.readMessagePullCount();
                count = count-1;
                if(count<1) count=0;
                BaseApplication.writeMessagePullCount(count);
                EventBus.getDefault().post( new BadgeNewEvent( ));
            }
        }else if(type==2){
            int count = BaseApplication.readCommentCount();
            count = count-1;
            if(count<1) count=0;
            BaseApplication.writeCommentCount(count);
            EventBus.getDefault().post( new BadgeNewEvent( ));
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        recyclerView.removeOnItemTouchListener(onItemClickListener);
    }

    public void initView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout.setOnRefreshListener(this);
        baseAdapter.openLoadMore(PAGESIZE);
        baseAdapter.setOnLoadMoreListener(this);
        recyclerView.setAdapter(baseAdapter);
        recyclerView.addOnItemTouchListener( onItemClickListener);

        emptyView = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty ,  (ViewGroup) recyclerView.getParent(),false);
        baseAdapter.setEmptyView(emptyView);

    }

    @Override
    public void loadData() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                updateBadge();
                onRefresh();
            }
        });
    }

    private void loadData(int pidx ) {

        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("type",String.valueOf(type));
        map.put("isSend",String.valueOf(sendType));
        map.put("pageIndex",String.valueOf(pidx));
        map.put("pageSize",String.valueOf(PAGESIZE));
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<MessageOutputModel> call = apiService.maillist( token,map);
        call.enqueue(new Callback<MessageOutputModel>() {
            @Override
            public void onResponse(Call<MessageOutputModel> call, Response<MessageOutputModel> response) {
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
                    ActivityUtils.getInstance().skipActivity(getActivity(), PhoneLoginActivity.class);
                    return;
                }
                if (response.body().getStatus() != 200) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }

                if (response.body().getStatus() == 200 && response.body() != null) {
                    if (operateType == OperateTypeEnum.REFRESH) {
                        List<ListModel> Articles = new ArrayList<ListModel>();
                        if (response.body().getData() != null && response.body().getData().getRows() != null) {
                            Articles.addAll(response.body().getData().getRows());
                        }
                        baseAdapter.setNewData(Articles);

                        //updateBadge();

                    } else if (operateType == OperateTypeEnum.LOADMORE) {

                        if (response.body().getData().getRows().size() < 1) {
                            if (noDataView == null) {
                                noDataView = getActivity().getLayoutInflater().inflate(R.layout.layout_nodata, (ViewGroup) recyclerView.getParent(), false);
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
            public void onFailure(Call<MessageOutputModel> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showLongToast(Constants.SERVER_ERROR);
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
        return R.layout.frag_store;
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

    @Override
    public String getPageTitle() {
        type = getArguments().getInt("type");
        sendType = getArguments().getInt("sendtype");
        return sendType==1?"发送消息":"接受消息";
    }

    protected void updateBadge(){
//        List<ListModel> data = baseAdapter.getData();
//        if(data ==null) return;
//        boolean hasUnRead=false;
//        for(int i=0;i<data.size();i++){
//            if( data.get(i).getIsRead()==0){
//                hasUnRead=true;
//                break;
//            }
//        }

        //EventBus.getDefault().post(new BadgeNewEvent( hasUnRead ));

        UnReadMessageUtil.getUnReadMessage();

    }

}
