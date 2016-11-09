package com.bameng.ui.news;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.adapter.CustomerAdapter;
import com.bameng.model.ArticleListOutput;
import com.bameng.model.CustomListOutput;
import com.bameng.model.CustomerModel;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.PostModel;
import com.bameng.model.TopArticleIdModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseObjectActivity extends BaseActivity {

    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.customListView)
    PullToRefreshListView customListView;
    int pageIndex=1;
    public OperateTypeEnum operateType= OperateTypeEnum.REFRESH;
    public List<CustomerModel> Customers;
    public CustomerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_object);
        ButterKnife.bind(this);
        initView();
        initlist();
        StartApi();
    }
    public void initlist(){
        Customers = new ArrayList<CustomerModel>();
        adapter = new CustomerAdapter(Customers, this,this);
        customListView.setAdapter(adapter);
        customListView.setMode(PullToRefreshBase.Mode.BOTH);
        customListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                operateType = OperateTypeEnum.REFRESH;
                pageIndex=1;
                StartApi();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                operateType = OperateTypeEnum.LOADMORE;
                pageIndex= pageIndex+1;
                StartApi();

            }
        });
    }

    @Override
    protected void initView() {
        titleText.setText("选着对象");
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);
    }

    @Override
    protected void StartApi() {
        Map<String, String> map = new HashMap<>();
        map.put("version", application.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("Type","0");
        map.put("pageIndex",String.valueOf(pageIndex));
        map.put("pageSize","1000");
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        String token = application.readToken();
        Call<CustomListOutput> call = apiService.customlist(token,map);
        call.enqueue(new Callback<CustomListOutput>() {
            @Override
            public void onResponse(Call<CustomListOutput> call, Response<CustomListOutput> response) {
                if (response.body() != null) {
                    customListView.onRefreshComplete();

                    CustomListOutput customListOutput = new CustomListOutput();
                    customListOutput.setData(response.body().getData());
                    if (response.body().getStatus() == 200&&response.body()!=null) {
                        if (operateType == OperateTypeEnum.REFRESH) {
                            Customers.clear();
                            Customers.addAll(response.body().getData().getRows());
                            adapter.notifyDataSetChanged();
                        } else if (operateType == OperateTypeEnum.LOADMORE) {
                            if (response.body().getData().getRows().size()==0){
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
            public void onFailure(Call<CustomListOutput> call, Throwable t) {
                customListView.onRefreshComplete();
                ToastUtils.showLongToast("失败");
            }
        });

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
