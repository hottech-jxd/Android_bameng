package com.bameng.ui.business;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.adapter.CashAdapter;
import com.bameng.config.Constants;
import com.bameng.model.CashCouponModel;
import com.bameng.model.CashCouponOutputModel;
import com.bameng.model.CloseEvent;
import com.bameng.model.OperateTypeEnum;
import com.bameng.model.PostModel;
import com.bameng.model.ScoreOutputModel;
import com.bameng.model.ShareModel;
import com.bameng.model.UserData;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.WebViewActivity;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.ui.news.ChooseObjectActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.bameng.utils.WindowUtils;
import com.bameng.widgets.BMShareView;
import com.bameng.widgets.SharePopupWindow;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.id;
import static android.R.attr.mode;
import static com.baidu.location.h.j.ad;

/***
 * 我的现金劵列表
 */
public class MyCashCardActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener , View.OnClickListener {

    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.recycleView)
    RecyclerView recyclerView;
    CashAdapter cashAdapter;
    View emptyView;
    final int REQUEST_CODE_SEND_INSHARE=100;
    ProgressDialog progressDialog;
    BMShareView bmShareView;
    CashCouponModel currentModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cash_card);
        ButterKnife.bind(this);
        initView();

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                loadData();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if( view.getId() == R.id.layShareCustomer){
            outShare( currentModel );
        }else if(view.getId() == R.id.layShareMY){
            inShare( currentModel );
        }
    }

    @Override
    protected void initView() {
        titleText.setText("我的现金券");
        //Drawable leftDraw = ContextCompat.getDrawable(this, R.mipmap.ic_back);
        //SystemTools.loadBackground(titleLeftImage, leftDraw);
        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);

        swipeRefreshLayout.setOnRefreshListener(this);
        cashAdapter = new CashAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cashAdapter);
        emptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty, (ViewGroup) recyclerView.getParent(), false);
        cashAdapter.setEmptyView(emptyView);

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                if( view.getId() == R.id.ivShare){
                    share( ((CashCouponModel)adapter.getItem(position)) );
                }
// else if(view.getId() == R.id.ivOutShare){
//                    CashCouponModel model = (CashCouponModel) adapter.getItem(position);
//                    outShare(model);
//                }
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
        loadData();
    }

    void loadData() {
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();
        Call<CashCouponOutputModel> call = apiService.MyCashCouponList(token, map);
        call.enqueue(new Callback<CashCouponOutputModel>() {
            @Override
            public void onResponse(Call<CashCouponOutputModel> call, Response<CashCouponOutputModel> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.code() != 200) {
                    ToastUtils.showLongToast(response.message());
                    return;
                }
                if (response.body() == null) {
                    ToastUtils.showLongToast("返回错误数据");
                    return;
                }
                if (response.body().getStatus() == Constants.STATUS_70035) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    EventBus.getDefault().post(new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(MyCashCardActivity.this, PhoneLoginActivity.class);
                    return;
                }
                if (response.body().getStatus() != 200) {
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }

                if (response.body().getData() == null) return;

                cashAdapter.setNewData(response.body().getData().getList());


            }

            @Override
            public void onFailure(Call<CashCouponOutputModel> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showLongToast( Constants.SERVER_ERROR );
            }
        });

    }

    void share( CashCouponModel model ){
        int type = BaseApplication.UserData() ==null ? -1 : BaseApplication.UserData().getUserIdentity();
        if( type != Constants.MENG_ZHU){
            outShare(model);
            return;
        }

        if(bmShareView==null){
            bmShareView = new BMShareView(this,this);
        }
        bmShareView.show();
        currentModel = model;
    }

    void inShare( CashCouponModel model ){
        Intent intent =new Intent( MyCashCardActivity.this , ChooseObjectActivity.class);
        intent.putExtra("couponId", model.getID() );
        ActivityUtils.getInstance().showActivityForResult( MyCashCardActivity.this , REQUEST_CODE_SEND_INSHARE , intent);
    }

    void outShare( final CashCouponModel model ){

        sendCash( 1 , model.getID(), "0", new shareListener() {
            @Override
            public void share(int couponId, String uid) {
                Map<String, String> map = new HashMap<>();
                int userid = BaseApplication.UserData().getUserId();
                map.put("userid", String.valueOf(userid) );
                map.put("cpid", String.valueOf(couponId ));
                String sign = AuthParamUtils.getSign(map);
                String url = model.getUrl();
                url +="?userid="+userid+"&cpid="+ model.getID()+"&sign="+sign;

                Bundle bd = new Bundle();
                bd.putString(Constants.INTENT_URL, url );

                bd.putBoolean(Constants.CAPTURE_SCREEN,true);


                ActivityUtils.getInstance().showActivity( MyCashCardActivity.this  , WebViewActivity.class , bd );
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) return;
        if( requestCode == REQUEST_CODE_SEND_INSHARE) {
            List<UserData> users = (List<UserData>) data.getSerializableExtra("customer");
            if (users == null || users.size() < 1) return;

            String ids = "";

            for (UserData item : users) {
                if (!ids.isEmpty()) {
                    ids += "|";
                }
                ids += item.getUserId();
            }

            int couponId = data.getIntExtra("couponId", 0);
            sendCash(2, couponId, ids, null);
        }
    }

    /***
     *
     */
    void sendCash( int type , final int couponId , final String uid , final shareListener shareListener){
        if(progressDialog==null){
            progressDialog =new ProgressDialog(this);
        }
        progressDialog.setMessage("加载中...");
        progressDialog.show();

        //String userid = String.valueOf(uid);
        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("couponId",  String.valueOf(couponId) );
        if( type == 1) {
            map.put("toUserId", uid );
        }else {
            map.put("ids", uid);
        }
        String sign = AuthParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getApiService();
        String token = BaseApplication.readToken();

        Call<PostModel> call;
        if( type ==1) {
            call = apiService.SendCashCoupon(token, map);
        }else{
            call = apiService.SendAllyCashCoupon(token, map);
        }

        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if(progressDialog!=null) progressDialog.dismiss();

                if(response.code() !=200){
                    ToastUtils.showLongToast(response.message());
                    return;
                }
                if(response.body()==null){
                    ToastUtils.showLongToast("返回错误数据");
                    return;
                }
                if(response.body().getStatus()==Constants.STATUS_70035){
                    EventBus.getDefault().post( new CloseEvent());
                    ActivityUtils.getInstance().skipActivity(MyCashCardActivity.this,PhoneLoginActivity.class);
                    return;
                }
                if(response.body().getStatus() != 200){
                    ToastUtils.showLongToast(response.body().getStatusText());
                    return;
                }
                //ToastUtils.showLongToast(response.body().getStatusText());
                onRefresh();
                if(shareListener!=null){
                    shareListener.share(couponId,uid);
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                if(progressDialog!=null)progressDialog.dismiss();
                ToastUtils.showLongToast( Constants.SERVER_ERROR );
            }
        });
    }

    public interface shareListener{
        void share(int couponId , String uid);
    }
}
