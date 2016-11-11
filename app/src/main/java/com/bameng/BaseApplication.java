package com.bameng;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.bameng.config.Constants;
import com.bameng.fragment.FragManager;
import com.bameng.model.BaiduLocation;
import com.bameng.model.BaseData;
import com.bameng.model.LocalAddressModel;
import com.bameng.model.UserData;
import com.bameng.model.VersionData;
import com.bameng.service.BaiduLocationService;
import com.bameng.utils.AssetsUtils;
import com.bameng.utils.CrashHandler;
import com.bameng.utils.JSONUtil;
import com.bameng.utils.PreferenceHelper;
import com.facebook.drawee.backends.pipeline.Fresco;

import static com.bameng.service.LocationService.Longitude;
import static com.bameng.service.LocationService.address;
import static com.bameng.service.LocationService.city;
import static com.bameng.service.LocationService.latitude;


/**
 * 系统级别的变量、方法
 * Application
 */
public class BaseApplication extends Application {
    //定位类型
    //public int localType;
    public BaiduLocation baiduLocation;
    //地址
    //public String address;
    //纬度
    //public double latitude;
    //经度
    //public double Longitude;
    public FragManager mFragManager;

    public LocalAddressModel localAddress;
    //是否有网络连接
    public boolean isConn = false;
    //城市
    //public String city;
    //public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;//地址从这开始
    //底部菜单是否隐藏 true显示， false隐藏
    //public boolean isMenuHide = false;
    //百度定位服务
    BaiduLocationService baiduLocationService;

    /**
     * 是否是左划或者返回
     * true 左划
     * false 返回
     */

    public static BaseApplication single;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        single = this;

        //加载异常处理模块
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());



        //mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        //mLocationClient.registerLocationListener(mMyLocationListener);
        baiduLocationService= new BaiduLocationService(this);
        baiduLocationService.registerListener(mMyLocationListener);
        baiduLocationService.start();

        // 初始化Volley实例
        //VolleyUtil.init(this);
        // 极光初始化
        // JPushInterface.setDebugMode(true);// 日志，生产环境关闭
        //JPushInterface.init ( this );
        //初始化shareSDK参数
        //ShareSDK.initSDK(getApplicationContext());

        // 极光初始化
        //PushHelper.init(this, BuildConfig.DEBUG, BuildConfig.Push_Url);

        //初始化 fresco
        Fresco.initialize(this);
    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//    }
//
//    @Override
//    public void onTerminate() {
//        super.onTerminate();
//    }
//
//    /**
//     * 解决有些android版本 AsyncTask 无法执行  onPostExecute方法的问题
//     *
//     * @throws
//     * @创建人：jinxiangdong
//     * @修改时间：2015年7月7日 上午11:23:32
//     * @方法描述：
//     * @方法名：solveAsyncTaskOnPostExecuteBug
//     * @参数：
//     * @返回：void
//     */
//    protected void solveAsyncTaskOnPostExecuteBug() {
//        try {
//            Class.forName("android.os.AsyncTask");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//    /**
//     * 获取手机IMEI码
//     */
//    public static String getPhoneIMEI() {
//        TelephonyManager tm = (TelephonyManager) single.getSystemService(Context.TELEPHONY_SERVICE);
//        return tm.getDeviceId();
//    }
//
//    public String readMemberId() {
//        return PreferenceHelper.readString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_ID,"0");
//    }
//
//    public void writeMemberId(String userId) {
//        PreferenceHelper.writeString(getApplicationContext(), Constants.MEMBER_INFO, Constants.MEMBER_ID, userId);
//    }

    /**
     * 判断网络是否连接
     */
    public static boolean checkNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null;// 网络是否连接
    }

    /**
     * 获取当前应用程序的版本号
     */
    public static String getAppVersion() {
        String version = "0";
        try {
            version = single.getPackageManager().getPackageInfo(single.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(BaseApplication.class.getName(), e.getMessage());
        }
        return version;
    }
    //判断是否为4.4版本。可设置沉浸模式
    public boolean isKITKAT() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
    /**
     * 实现定位回调监听
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null == location) {
                return;
            } else {
                int localType = location.getLocType();

                if (BDLocation.TypeGpsLocation == localType || BDLocation.TypeNetWorkLocation == localType || BDLocation.TypeOffLineLocation == localType) {
//                    latitude = location.getLatitude();
//                    Longitude = location.getLongitude();
//                    city = location.getCity();
//                    address = location.getAddrStr();
                    baiduLocation =new BaiduLocation();
                    baiduLocation.setAddress(location.getAddrStr());
                    baiduLocation.setCity(location.getCity());
                    baiduLocation.setCityCode(location.getCityCode());
                    baiduLocation.setCountry(location.getCountry());
                    baiduLocation.setCountryCode(location.getCountryCode());
                    baiduLocation.setLatitude(location.getLatitude());
                    baiduLocation.setLongitude(location.getLongitude());
                    baiduLocation.setProvince(location.getProvince());
                    baiduLocation.setStreet( location.getStreet());
                    baiduLocation.setDistrict(location.getDistrict());
                } else{
                    Log.i("BaiduLocation:", "百度定位失败 " + String.valueOf(location.getLocType()));
                }
            }

            // 将定位信息写入配置文件
            if (null != city) {
                PreferenceHelper.writeString(getApplicationContext(),
                        Constants.LOCATION_INFO, Constants.CITY, city);
            }
            if (null != address) {
                PreferenceHelper.writeString(getApplicationContext(),
                        Constants.LOCATION_INFO, Constants.ADDRESS, address);
            }
            PreferenceHelper.writeString(getApplicationContext(),
                    Constants.LOCATION_INFO, Constants.LATITUDE,
                    String.valueOf(latitude));
            PreferenceHelper.writeString(getApplicationContext(),
                    Constants.LOCATION_INFO, Constants.LONGITUDE,
                    String.valueOf(Longitude));
        }

    }

    /***
     * 加载地址信息
     */
    public void loadAddress() {
        if (null != localAddress) return;

        new Thread(new Runnable() {
            @Override
            public void run() {
                AssetsUtils assetsUtils = new AssetsUtils(getBaseContext());
                String json = assetsUtils.readAddress("addressData.json");
                JSONUtil<LocalAddressModel> jsonUtil = new JSONUtil<>();
                LocalAddressModel localAddress = new LocalAddressModel();
                localAddress = jsonUtil.toBean(json, localAddress);
                single.localAddress = localAddress;
            }
        }).start();
    }


    //加载基础信息
    public void loadBaseData(BaseData baseData) {
        PreferenceHelper.writeString(getApplicationContext(), "Base_info", "about", baseData.getAboutUrl());
        PreferenceHelper.writeString(getApplicationContext(), "Base_info", "Agreement", baseData.getAgreementUrl());
        PreferenceHelper.writeInt(getApplicationContext(), "Base_info", "userStatus", baseData.getUserStatus());
    }
    //加载版本信息
    public void loadUpdate(VersionData update) {
        PreferenceHelper.writeString(getApplicationContext(), "update_info", "serverVersion", update.getServerVersion());
        PreferenceHelper.writeString(getApplicationContext(), "update_info", "updateTip", update.getUpdateTip());
        PreferenceHelper.writeString(getApplicationContext(), "update_info", "updateUrl", update.getUpdateUrl());
        PreferenceHelper.writeInt(getApplicationContext(), "update_info", "updateType", update.getUpdateType());
    }

    public void writeUserToken(String token) {
        PreferenceHelper.writeString(getApplicationContext(), "token", "token", token);
    }
    public String readToken(){
        return PreferenceHelper.readString(getApplicationContext(),"token" ,"token","");
    }
    //加载个人信息
    public void writeUserInfo(UserData user) {
        String json = JSONUtil.getGson().toJson(user);
        PreferenceHelper.writeString(getApplicationContext(), Constants.LOGIN_USER_INFO, "user", json);
    }

    public UserData readUserInfo(){
        String json = PreferenceHelper.readString( getApplicationContext(), Constants.LOGIN_USER_INFO , "user", null);
        if(json==null) return null;
        return JSONUtil.getGson().fromJson(json, UserData.class);

    }

    public void clearAllCookies(){
        CookieManager.getInstance().removeAllCookie();
    }

}
