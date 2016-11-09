package com.bameng.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.model.CustomerModel;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 47483 on 2016.11.09.
 */
public class CustomDetailsAdapter extends BaseAdapter {
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.customdone_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            CustomerModel Customer = Customers.get(position);
            holder.name.setText(Customer.getName());
            holder.moblie.setText(Customer.getMobile());
            if (Customer.getStatus()==1) {
                holder.status.setText("已同意");
                holder.layBtn.setVisibility(View.GONE);
            }else if (Customer.getStatus()==2){
                holder.status.setText("已拒绝");
                holder.layBtn.setVisibility(View.GONE);
            }else {
                holder.status.setText("审核中");
                holder.layBtn.setVisibility(View.VISIBLE);
            }
            //holder.img.setImageURI(Customer.get);
        }
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.img)
        SimpleDraweeView img;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.moblie)
        TextView moblie;
        @Bind(R.id.lay_btn)
        LinearLayout layBtn;
        @Bind(R.id.status)
        TextView status;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}