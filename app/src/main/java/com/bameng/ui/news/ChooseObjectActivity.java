package com.bameng.ui.news;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.adapter.ChooseCustomerAdapter;
import com.bameng.adapter.CustomerAdapter;
import com.bameng.model.ArticleListOutput;
import com.bameng.model.CustomListOutput;
import com.bameng.model.CustomerModel;
import com.bameng.model.MengModel;
import com.bameng.model.MyOutputModel;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.PostModel;
import com.bameng.model.TopArticleIdModel;
import com.bameng.model.UserData;
import com.bameng.model.UserOutputsModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.bameng.R;
import com.chad.library.adapter.base.listener.OnItemClickListener;

public class ChooseObjectActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener , BaseQuickAdapter.RequestLoadMoreListener{

    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.recycleView)
    RecyclerView recyclerView;
    @BindView(R2.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.tvSelectAll)
    TextView tvSelectAll;
    @BindView(R2.id.tvFinish)
    TextView tvFinish;

    ChooseCustomerAdapter adapter;
    boolean currentSelected = false;

    int pageIndex=1;
    public OperateTypeEnum operateType= OperateTypeEnum.REFRESH;
    final int PAGESIZE= 1000;

    View noDataView;

    List<UserData> chooses;
    /***
     * 是否单选
     */
    boolean isRadio = false;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new ChooseCustomerAdapter();
        adapter.openLoadAnimation();
        adapter.openLoadMore(PAGESIZE);
        adapter.setOnLoadMoreListener(this);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if(isRadio){
                    setSignleSelect( adapter.getItem(i) );
                }else {
                    adapter.getItem(i).setSelected(!adapter.getItem(i).isSelected());
                    adapter.notifyItemChanged(i);
                }
            }
        });

        recyclerView.setAdapter(adapter);

    }

    private void setSignleSelect(UserData model ){
        if( adapter.getData()==null|| adapter.getData().size()<1)return;
        for(UserData item : adapter.getData()){
            if(item.getUserId() != model.getUserId()){
                item.setSelected(false);
            }else{
                item.setSelected(true);
            }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initView() {

        isRadio = getIntent().getBooleanExtra("select" , false);
        tvSelectAll.setVisibility( isRadio? View.GONE:View.VISIBLE);

        titleText.setText("选择对象");
        //Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        //SystemTools.loadBackground(titleLeftImage, leftDraw);
        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);

        chooses = (List<UserData>)getIntent().getExtras().getSerializable("customer");

    }

    @Override
    protected void StartApi( ) {
        StartApi(pageIndex);
    }
    //
    protected void StartApi( int idx) {
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        //map.put("Type","0");
        map.put("pageIndex",String.valueOf( idx ));
        map.put("pageSize", String.valueOf(PAGESIZE) );

        map.put("orderbyCode",  "-1");
        map.put("isDesc", "0");

        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<MyOutputModel<MengModel>> call = apiService.allylist(token,map);
        call.enqueue(new Callback<MyOutputModel<MengModel>>() {
            @Override
            public void onResponse(Call<MyOutputModel<MengModel>> call, Response<MyOutputModel<MengModel>> response) {
                swipeRefreshLayout.setRefreshing(false);
                if(response.code()!=200){
                    ToastUtils.showLongToast(response.message());
                    return;
                }
                if (response.body() != null) {
                    if (response.body().getStatus() == 200&&response.body().getData() !=null) {
                        if (operateType == OperateTypeEnum.REFRESH) {
                            List<UserData> data = response.body().getData().getRows();
                            setChoose(data);

                            adapter.setNewData(  data  );

                        } else if (operateType == OperateTypeEnum.LOADMORE) {
                            if (response.body().getData().getRows().size()==0){
                                if(noDataView==null){
                                    noDataView = LayoutInflater.from(ChooseObjectActivity.this).inflate(R.layout.layout_nodata,null);
                                }
                                adapter.removeAllFooterView();
                                adapter.addFooterView(noDataView);
                                adapter.loadComplete();
                                return;
                            }

                            List<UserData> data = response.body().getData().getRows();
                            setChoose(data);

                            adapter.addData( data );
                            pageIndex++;
                        }
                    } else {
                        ToastUtils.showLongToast(response.body().getStatusText());
                    }
                }
            }


            @Override
            public void onFailure(Call<MyOutputModel<MengModel>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showLongToast("失败");
            }
        });

    }

    protected void setChoose(List<UserData> data ){
        if( data ==null || chooses ==null || chooses.size()<1 ) return;
        for( UserData item : data ){
            int id = item.getUserId();
            for(UserData bb : chooses){
                if( bb.getUserId()  == id ){
                    item.setSelected(true);
                }
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @OnClick(R.id.tvSelectAll)
    void onSelectAll(){
        List<UserData> list = adapter.getData();
        if(list==null) return;
        currentSelected = !currentSelected;
        for(UserData item : list){
            item.setSelected( currentSelected);
        }
        adapter.notifyDataSetChanged();
    }
    @OnClick(R.id.tvFinish)
    void onFinish(){
        Intent data = getIntent();
        List<UserData> selected = new ArrayList<>();
        List<UserData> list = adapter.getData();
        for(UserData item : list){
            if(item.isSelected()){
                selected.add(item);
            }
        }

        Bundle bd = new Bundle();
        bd.putSerializable( "customer" , (Serializable) selected );
        data.putExtras(bd);
        this.setResult(RESULT_OK , data );
        this.finish();
    }

    @Override
    public void onLoadMoreRequested() {
        int idx = pageIndex+1;
        operateType = OperateTypeEnum.LOADMORE;
        StartApi( idx );
    }

    @Override
    public void onRefresh() {
        operateType = OperateTypeEnum.REFRESH;
        pageIndex =1;
        StartApi(pageIndex);
    }
}
