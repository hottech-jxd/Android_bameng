package com.bameng.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ListView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.adapter.ArticleAdapter;
import com.bameng.adapter.MsgAdapter;
import com.bameng.config.Constants;
import com.bameng.model.ArticleListOutput;
import com.bameng.model.ListModel;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.RefreshWebViewEvent;
import com.bameng.model.TopArticleIdModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.WebViewActivity;
import com.bameng.ui.base.BaseFragment;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.ToastUtils;
import com.bameng.widgets.RecycleItemDivider;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bameng.R.id.homePullRefresh;

/**
 * 盟主/盟友 资讯列表
 * Created by 47483 on 2016.11.01.
 */
public class AllyFrag extends StoreFrag {

    public AllyFrag(){
        layoutId = R.layout.layout_msg_item;
        if(BaseApplication.UserData().getUserIdentity() == Constants.MENG_ZHU ) {
            type= 4;
        }else {
            type = 3;
        }
        baseAdapter = new MsgAdapter();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //EventBus.getDefault().register(this);
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
    public void onDestroyView() {
        super.onDestroyView();
        //EventBus.getDefault().unregister(this);
    }

    /***
     * 刷新数据
     * @param event
     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventRefreshData(RefreshWebViewEvent event){
//
//    }
}
