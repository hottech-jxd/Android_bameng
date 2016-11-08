package com.bameng.ui.account;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.model.LocalAddressModel;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.SystemTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressActivity extends BaseActivity {

    public LocalAddressModel data;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.provinceL)
    LinearLayout provinceL;
    @Bind(R.id.spinner_province)
    Spinner spinnerProvince;
    @Bind(R.id.cityL)
    LinearLayout cityL;
    @Bind(R.id.spinner_city)
    Spinner spinnerCity;
    @Bind(R.id.areaL)
    LinearLayout areaL;
    @Bind(R.id.spinner_area)
    Spinner spinnerArea;
    public Resources resources;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        resources = this.getResources();
        data = application.localAddress;
        initView();
        StartApi();

    }

    @OnClick(R.id.btn_commit)
    void onBtnCommitClick() {
        //TODO implement
    }

    @Override
    protected void initView() {
        titleText.setText("地区");
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);
        //initProvinceDatas();
//        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mProvinceDatas);
//        spinnerProvince.setAdapter(adapter1);
//        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mCitisDatasMap);
//        ArrayAdapter<String> adapter3=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mAreaDatasMap);

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

    @OnClick(R.id.provinceL)
    void selectProvince() {

    }

    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
