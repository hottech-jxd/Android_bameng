package com.bameng.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.model.CustomListOutput;
import com.bameng.model.CustomerModel;
import com.bameng.model.PostModel;
import com.bameng.service.ApiService;
import com.bameng.service.ZRetrofitUtil;
import com.bameng.ui.business.CustomerExamineActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.baidu.location.h.j.t;
import static com.bameng.R.id.status;

/**
 * Created by 47483 on 2016.11.09.
 */
public class CustomDetailsAdapter extends BaseAdapter implements View.OnClickListener{
    private List<CustomerModel> Customers;
    private Context mContext;
    private Activity aty;
    public CustomerModel customerModel;

    public CustomDetailsAdapter(List<CustomerModel> Customers, Context mContext, Activity aty) {
        this.Customers = Customers;
        this.mContext = mContext;
        this.aty = aty;
    }

    @Override
    public int getCount() {
        return Customers.size();
    }

    @Override
    public Object getItem(int position) {
        return Customers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        CustomerModel Customer = Customers.get(position);
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.customdone_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.name.setText("客户名称:"+Customer.getName());
        holder.moblie.setText("联系方式:"+Customer.getMobile());
        if (Customer.getStatus()==1) {
            holder.status.setText("已同意");
            holder.layBtn.setVisibility(View.GONE);
        }else if (Customer.getStatus()==2){
            holder.status.setText("已拒绝");
            holder.layBtn.setVisibility(View.GONE);
        }else {
            holder.status.setText("未审核");
            holder.layBtn.setVisibility(View.VISIBLE);
        }
        holder.agress.setTag(Customer);
        holder.reject.setTag(Customer);
        holder.progressBar.setVisibility( Customer.isDoing()? View.VISIBLE:View.GONE);
        holder.agress.setVisibility(Customer.isDoing()? View.GONE:View.VISIBLE);
        holder.reject.setVisibility(Customer.isDoing()? View.GONE:View.VISIBLE);

        holder.agress.setOnClickListener(this);
        holder.reject.setOnClickListener(this);
        holder.img.setOnClickListener(this);
        holder.img.setTag(Customer);
        holder.status.setOnClickListener(this);
        holder.status.setTag(Customer);
        holder.name.setOnClickListener(this);
        holder.name.setTag(Customer);
        holder.moblie.setOnClickListener(this);
        holder.moblie.setTag(Customer);

        return convertView;
    }

    class ViewHolder {
        @BindView(R2.id.img)
        SimpleDraweeView img;
        @BindView(R2.id.name)
        TextView name;
        @BindView(R2.id.moblie)
        TextView moblie;
        @BindView(R2.id.lay_btn)
        LinearLayout layBtn;
        @BindView(R2.id.status)
        TextView status;
        @BindView(R2.id.btnAgree)
        Button agress;
        @BindView(R2.id.btnReject)
        Button reject;
        @BindView(R2.id.progressBar)
        ProgressBar progressBar;
        @BindView(R2.id.llItem)
        LinearLayout llItem;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnAgree){
            CustomerModel customerModel = (CustomerModel) view.getTag();
            customerModel.setDoing(true);
            this.notifyDataSetChanged();
            audit( customerModel , 1);


        }else if(view.getId()==R.id.btnReject){
            CustomerModel customerModel = (CustomerModel) view.getTag();
            customerModel.setDoing(true);
            this.notifyDataSetChanged();
            audit(customerModel , 2);
        }else if( view.getId() == R.id.img|| view.getId() == R.id.name || view.getId() == R.id.status || view.getId()== R.id.moblie ){
            CustomerModel customerModel = (CustomerModel) view.getTag();
            Bundle bundle = new Bundle();
            bundle.putSerializable("customerinfo", customerModel);
            ActivityUtils.getInstance().showActivity( (Activity) mContext , CustomerExamineActivity.class,bundle);
        }
    }

    protected void audit(final CustomerModel customerModel , int status ){

        Map<String, String> map = new HashMap<>();
        map.put("version", BaseApplication.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        map.put("cid", String.valueOf( customerModel.getID()  ) );
        map.put("status",  String.valueOf( status ));
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        map.put("sign", sign);
        ApiService apiService = ZRetrofitUtil.getInstance().create(ApiService.class);
        String token = BaseApplication.readToken();
        Call<PostModel> call = apiService.audit(token, map);
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if( response.code() !=200){
                    customerModel.setDoing(false);
                    CustomDetailsAdapter.this.notifyDataSetChanged();
                    ToastUtils.showLongToast( response.message() );
                    return;
                }
                if( response.body() !=null && response.body().getStatus() ==200 ){
                    Customers.remove( customerModel );
                    CustomDetailsAdapter.this.notifyDataSetChanged();
                }else{
                    customerModel.setDoing(false);
                    CustomDetailsAdapter.this.notifyDataSetChanged();
                    ToastUtils.showLongToast( response.body() !=null ?  response.body().getStatusText() : "失败");
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                ToastUtils.showLongToast("请求失败");
                customerModel.setDoing(false);
                CustomDetailsAdapter.this.notifyDataSetChanged();
            }
        });
    }
}