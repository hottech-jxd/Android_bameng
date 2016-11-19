package com.bameng.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.config.Constants;
import com.bameng.listener.PoponDismissListener;
import com.bameng.model.LocalAddressModel;
import com.bameng.utils.SystemTools;
import com.bameng.utils.WindowUtils;
import com.bameng.widgets.wheel.OnWheelChangedListener;
import com.bameng.widgets.wheel.WheelView;
import com.bameng.widgets.wheel.adapters.ArrayWheelAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 地址选择弹出框
 */
public class AddressPopWin extends PopupWindow implements OnWheelChangedListener {

    private
    Handler mHandler;
    private
    BaseApplication application;
    private
    Context context;
    private
    Activity aty;
    private
    LocalAddressModel data;
    private int type;
    private View popView;
    View emptyView = null;
    private WindowManager wManager;
    public WheelView mViewProvince;
    public WheelView mViewCity;
    public WheelView mViewArea;

    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mAreaDatasMap = new HashMap<String, String[]>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentAreaName;


    public AddressPopWin(
            Handler mHandler, BaseApplication application, Context context,
            LocalAddressModel data, int type, WindowManager wManager, Activity aty
    ) {

        this.mHandler = mHandler;
        this.application = application;
        this.data = data;
        this.context = context;
        this.type = type;
        this.wManager = wManager;
        this.aty = aty;
    }

    public void initView() {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popView = inflater.inflate(R.layout.address_item, null);
        SystemTools.loadBackground(popView, context.getResources().getDrawable(R.color.white));

        TextView cancel = (TextView) popView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissView();
            }
        });
        TextView sure = (TextView) popView.findViewById(R.id.sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选中设置值
                List<String> address = new ArrayList<String>();
                address.add(0, mCurrentProviceName);
                address.add(1, mCurrentCityName);
                address.add(2, mCurrentAreaName);
                Message message = mHandler.obtainMessage();
                message.what = Constants.SELECT_ADDRESS;
                message.obj = address;
                mHandler.sendMessage(message);
                dismissView();
            }
        });

        mViewProvince = (WheelView) popView.findViewById(R.id.id_province);
        mViewProvince.addChangingListener(this);
        mViewCity = (WheelView) popView.findViewById(R.id.id_city);
        mViewCity.addChangingListener(this);
        mViewArea = (WheelView) popView.findViewById(R.id.id_district);
        mViewArea.addChangingListener(this);
        setUpData();

        // 设置SelectPicPopupWindow的View
        this.setContentView(popView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(wManager.getDefaultDisplay().getWidth());
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        this.setFocusable(true);

        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPop);
        WindowUtils.backgroundAlpha(aty, 0.4f);
        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        this.setBackgroundDrawable(dw);
    }

    private void setUpData() {
        //渠道所有省
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewArea.setVisibleItems(7);



        updateCities();
        updateAreas();
    }

    protected void setCurrentProvice( String provinceName ){
        mCurrentProviceName = provinceName;
        if(TextUtils.isEmpty( mCurrentProviceName )) return;

        int index = -1;
        for(int i=0;i< mProvinceDatas.length;i++){
            String item = mProvinceDatas[i];
            if( item.equals(mCurrentProviceName) ) index = i;
        }
        if( index>-1 ){
            mViewProvince.setCurrentItem(index);
        }
    }

    protected void setCurrentCityName(String city){
        mCurrentCityName = city;
        if(TextUtils.isEmpty( mCurrentCityName )) return;
        int index = -1;
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if( cities==null || cities.length<1) return;
        for(int i=0;i< cities.length  ;i++){
            String item = cities[i];
            if( item.equals(city) ) index = i;
        }
        if( index>-1 ){
            mViewCity.setCurrentItem(index);
        }
    }

    public void setCurrentAddress(String province,String city,String area){
        setCurrentProvice( province);
        setCurrentCityName(city);
        setCurrentAreaName(area);
    }

    protected void setCurrentAreaName(String areaName){
        mCurrentAreaName = areaName;
        if(TextUtils.isEmpty( mCurrentAreaName )) return;
        int index = -1;
        String[] areas = mAreaDatasMap.get(mCurrentCityName);
        if( areas==null || areas.length<1) return;
        for(int i=0;i< areas.length  ;i++){
            String item = areas[i];
            if( item.equals(areaName) ) index = i;
        }
        if( index>-1 ){
            mViewArea.setCurrentItem(index);
        }
    }

    public void dismissView() {
        setOnDismissListener(new PoponDismissListener(aty));
        dismiss();
    }


    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {

        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewArea) {
            if(null==mAreaDatasMap.get(mCurrentCityName)) return;
            mCurrentAreaName = mAreaDatasMap.get(mCurrentCityName)[newValue];
        }
    }

    protected void initProvinceDatas() {
        List<String> provinceList = new ArrayList<String>();
        List<LocalAddressModel.ProvinceInner> provinceInner = data.getList();
        for (int i = 0; i < provinceInner.size(); i++) {
            provinceList.add(provinceInner.get(i).getName());
            List<LocalAddressModel.CityInner> cityInners = provinceInner.get(i).getCity();
            List<String> cityList = new ArrayList<String>();
            //获取省-市映射
            for (int j = 0; j < cityInners.size(); j++) {
                cityList.add(cityInners.get(j).getName());
                List<LocalAddressModel.AreaInner> areaInners = cityInners.get(j).getArea();
                List<String> areaList = new ArrayList<String>();
                for (int k = 0; k < areaInners.size(); k++) {
                    areaList.add(areaInners.get(k).getName());
                }
                mAreaDatasMap.put(cityInners.get(j).getName(), areaList.toArray(new String[areaList.size()]));
            }
            mCitisDatasMap.put(provinceInner.get(i).getName(), cityList.toArray(new String[cityList.size()]));
        }

        mProvinceDatas = provinceList.toArray(new String[provinceList.size()]);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mAreaDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mViewArea.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
        mViewArea.setCurrentItem(0);
        //默认选择区
        mCurrentAreaName = areas[0];
    }
}
