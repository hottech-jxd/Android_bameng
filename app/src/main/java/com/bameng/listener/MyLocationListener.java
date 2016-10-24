//package com.bameng.listener;
//
//import android.util.Log;
//
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//
//
///**
// * Created by Administrator on 2015/8/25.
// *
// * 实现实时位置回调监听
// */
//public class MyLocationListener implements BDLocationListener {
//    @Override
//    public void onReceiveLocation(BDLocation location) {
//        //Receive Location
//        if( null == location)return;
//
//        BaiduLocationInfo locationBean=null;
//
//        StringBuffer sb = new StringBuffer(256);
//        sb.append("time : ");
//        sb.append(location.getTime());
//        sb.append("\nlocType code : ");
//        sb.append(location.getLocType());
//        sb.append("\nlatitude : ");
//        sb.append(location.getLatitude());
//        sb.append("\nlontitude : ");
//        sb.append(location.getLongitude());
//        sb.append("\nradius : ");
//        sb.append(location.getRadius());
//
//        if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
//            sb.append("\nspeed : ");
//            sb.append(location.getSpeed());// 单位：公里每小时
//            sb.append("\nsatellite : ");
//            sb.append(location.getSatelliteNumber());
//            sb.append("\nheight : ");
//            sb.append(location.getAltitude());// 单位：米
//            sb.append("\ndirection : ");
//            sb.append(location.getDirection());
//            sb.append("\ncityCode : ");
//            sb.append(location.getCityCode());
//            sb.append("\naddr : ");
//            sb.append(location.getAddrStr());
//            sb.append("\ndescribe : ");
//            sb.append("gps定位成功");
//
//            locationBean = new BaiduLocationInfo();
//            locationBean.setLatitude(location.getLatitude());
//            locationBean.setLongitude(location.getLongitude());
//            locationBean.setAddress(location.getAddrStr());
//            locationBean.setCity(location.getCity());
//            locationBean.setCityCode(location.getCityCode());
//            locationBean.setLocalType(location.getLocType());
//
//        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
//            sb.append("\ncityCode : ");
//            sb.append(location.getCityCode());
//            sb.append("\naddr : ");
//            sb.append(location.getAddrStr());
//            //运营商信息
//            sb.append("\noperationers : ");
//            sb.append(location.getOperators());
//            sb.append("\ndescribe : ");
//            sb.append("网络定位成功");
//
//            locationBean = new BaiduLocationInfo();
//            locationBean.setLatitude(location.getLatitude());
//            locationBean.setLongitude(location.getLongitude());
//            locationBean.setAddress(location.getAddrStr());
//            locationBean.setCity(location.getCity());
//            locationBean.setCityCode(location.getCityCode());
//            locationBean.setLocalType(location.getLocType());
//
//        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//            sb.append("\ncityCode : ");
//            sb.append(location.getCityCode());
//            sb.append("\ndescribe : ");
//            sb.append("离线定位成功，离线定位结果也是有效的");
//
//            locationBean = new BaiduLocationInfo();
//            locationBean.setLatitude(location.getLatitude());
//            locationBean.setLongitude(location.getLongitude());
//            locationBean.setAddress(location.getAddrStr());
//            locationBean.setCity(location.getCity());
//            locationBean.setCityCode(location.getCityCode());
//            locationBean.setLocalType(location.getLocType());
//
//        } else if (location.getLocType() == BDLocation.TypeServerError) {
//            sb.append("\ndescribe : ");
//            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//            sb.append("\ndescribe : ");
//            sb.append("网络不同导致定位失败，请检查网络是否通畅");
//        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//            sb.append("\ndescribe : ");
//            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//        }
//        //sb.append("\nlocationdescribe : ");// 位置语义化信息
//        //sb.append(location.getLocationDescribe());
//
//        Log.i("BaiduLocationApiDem", sb.toString());
//        Log.i("BaiduLocationApiDem", sb.toString());
//
////        if( null != locationBean ){
////            PreferenceHelper.writeString(SellerApplication.getInstance(),
////                    Constant.LOCATION_INFO, Constant.PRE_LOCATION_CITY_CODE, locationBean.getCityCode());
////            PreferenceHelper.writeString(SellerApplication.getInstance(),
////                    Constant.LOCATION_INFO,"city", locationBean.getCity());
////            PreferenceHelper.writeString(SellerApplication.getInstance(),
////                    Constant.LOCATION_INFO, "address", locationBean.getAddress());
////        }
//    }
//
//
//}
