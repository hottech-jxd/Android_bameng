package com.bameng.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bameng.R;
import com.bameng.adapter.CustomDetailsAdapter;
import com.bameng.model.CustomListOutput;
import com.bameng.model.CustomerModel;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.PostModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseFragment;
import com.bameng.ui.business.CustomerExamineActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.ToastUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 47483 on 2016.11.09.
 */

public class CustomDoneFrag extends BaseFragment implements AdapterView.OnItemClickListener {


    @Bind(R.id.customDoneList)
    PullToRefreshListView customDoneList;
    public OperateTypeEnum operateType= OperateTypeEnum.REFRESH;
    public List<CustomerModel> Customers;
    public CustomDetailsAdapter adapter;
    int pageIndex= 1;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList();
        loadData();
    }
    private void initList()
    {
        customDoneList.setMode(PullToRefreshBase.Mode.BOTH);
        customDoneList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                operateType = OperateTypeEnum.REFRESH;
                Customers.clear();
                pageIndex=1;
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                operateType = OperateTypeEnum.LOADMORE;
                loadData();

            }
        });
        Customers = new ArrayList<CustomerModel>();
        adapter = new CustomDetailsAdapter(Customers, getActivity(), getActivity());
        customDoneList.setAdapter(adapter);
        customDoneList.setOnItemClickListener(this);
    }


    private void loadData()
    {
        Map<String, String> map = new HashMap<>();
        map.put("version", application.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("type","2");
        map.put("pageIndex",String.valueOf(pageIndex));
        map.put("pageSize","20");
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        String token = application.readToken();
        Call<CustomListOutput> call = apiService.customlist(token, map);
        call.enqueue(new Callback<CustomListOutput>() {
            @Override
            public void onResponse(Call<CustomListOutput> call, Response<CustomListOutput> response) {
                if (response.body() != null) {
                    customDoneList.onRefreshComplete();

                    if (response.body().getStatus() == 200 && response.body().getData() != null) {

                        Customers.addAll(response.body().getData().getRows());
                        adapter.notifyDataSetChanged();
                    } else if (response.body().getStatus()==70035){

                        ToastUtils.showLongToast(response.body().getStatusText());
                    }

                } else {
                    ToastUtils.showLongToast("连接服务器失败！！！");
                }
                return;


            }

            @Override
            public void onFailure(Call<CustomListOutput> call, Throwable t) {
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
        return R.layout.frag_customdone;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         Bundle bundle = new Bundle();
        bundle.putSerializable("customerinfo", Customers.get(position-1));
        ActivityUtils.getInstance().showActivity(getActivity(), CustomerExamineActivity.class,bundle);

    }
}
