package com.bameng.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.model.CustomerModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 47483 on 2016.11.08.
 */
public class CustomerAdapter extends BaseAdapter{

    private List<CustomerModel> Customers;
    private Context mContext;
    private Activity aty;

    public CustomerAdapter(List<CustomerModel> Customers, Context mContext, Activity aty)
    {
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
        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.choose_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            CustomerModel Customer = Customers.get(position);
            holder.name.setText(Customer.getName());
            holder.moblie.setText(Customer.getMobile());
        }
        return convertView;
    }

    class ViewHolder
    {
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.moblie)
        TextView moblie;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
