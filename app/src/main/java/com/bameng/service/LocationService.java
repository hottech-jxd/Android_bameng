package com.bameng.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bameng.BaseApplication;

/**
 * 基于百度的定位服务
 */
public class LocationService extends Service {

    //纬度值
    public static String latitude = null;
    //经度值
    public static String Longitude = null;
    //地址
    public static String address = null;
    //当前城市
    public static String city = null;
    /**
     * 高精度模式
     */
    private LocationClientOption.LocationMode mode = LocationClientOption.LocationMode.Hight_Accuracy;
    /**
     * 定位客户端
     */
    private LocationClient mLocationClient = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //mLocationClient = ((BaseApplication) getApplication()).mLocationClient;

        //设置定位参数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(mode);
        option.setCoorType("bd09ll"); //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        option.setOpenGps(true); //打开gps
        option.setScanSpan(5000); //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setNeedDeviceDirect(true);//可选，设置是否需要设备方向结果
        mLocationClient.setLocOption(option);

        //mLocationClient.registerLocationListener( new MyLocationListener());

        mLocationClient.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

}
