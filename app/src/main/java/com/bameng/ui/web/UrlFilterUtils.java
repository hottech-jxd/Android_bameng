//package com.bameng.ui.web;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.webkit.WebView;
//
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.bameng.BaseApplication;
//import com.bameng.widgets.ProgressPopupWindow;
//
//
//import java.lang.ref.WeakReference;
//import java.util.Map;
//
///**
// * 拦截页面操作类
// */
//public class UrlFilterUtils {
//    private WeakReference<Activity> ref;
//    private Handler mHandler;
//    private BaseApplication application;
//    public ProgressPopupWindow payProgress;
//
//    public UrlFilterUtils (Activity aty, Handler mHandler, BaseApplication application  ) {
//        this.mHandler = mHandler;
//        this.application = application;
//        this.ref = new WeakReference<>(aty);
//        payProgress = new ProgressPopupWindow ( aty );
//    }
//
//    /**
//     * webview拦截url作相应的处理
//     * @param view
//     * @param url
//     * @return
//     */
//    public boolean shouldOverrideUrlBySFriend (WebView view, String url ) {
//        if( ref.get() ==null) return false;
//
//        if ( url.contains ( Constants.WEB_TAG_NEWFRAME ) ) {
//            String urlStr = url.substring ( 0, url.indexOf ( Constants.WEB_TAG_NEWFRAME ) );
//            Bundle bundle = new Bundle( );
//            bundle.putString ( Constants.INTENT_URL, urlStr );
//            ActivityUtils.getInstance ( ).showActivity ( ref.get() , WebViewActivity.class, bundle );
//            return true;
//        } else if ( url.contains ( Constants.WEB_CONTACT ) ){
//            //拦截客服联系
//            //获取QQ号码
//            String qq = url.substring ( 0, url.indexOf ( "&version=" ));
//            //调佣本地的QQ号码
//            try{
//                ref.get().startActivity ( new Intent( Intent.ACTION_VIEW, Uri.parse ( qq ) ) );
//            } catch ( Exception e ){
//                if(e.getMessage ().contains ( "No Activity found to handle Intent" )){
//                    NoticePopWindow noticePop = new NoticePopWindow ( ref.get() , "请安装QQ客户端");
//                    noticePop.showNotice ();
//                    noticePop.showAtLocation ( ref.get().getWindow().getDecorView() , Gravity.CENTER, 0, 0 );
//                }
//            }
//            return true;
//        }
//        else if(url.contains(Constants.WEB_TAG_USERINFO)){
//            //修改用户信息
//            //判断修改信息的类型
//            return true;
//        }else if(url.contains(Constants.WEB_TAG_LOGOUT)){
//            //处理登出操作
//            //鉴权失效
//            //清除登录信息
//            application.logout ();
//
//            Uri uri = Uri.parse(url.toLowerCase());
//            String redirectURL = uri.getQueryParameter("redirecturl");
//
//            if(!TextUtils.isEmpty( redirectURL )){
//                redirectURL = Uri.decode( redirectURL );
//            }
//
//
//            if( !TextUtils.isEmpty( redirectURL ) && !redirectURL.toLowerCase().startsWith("http://") ){
//                if( !redirectURL.startsWith("/") ) redirectURL = "/"+ redirectURL;
//                redirectURL = uri.getHost() + redirectURL;
//            }
//            //跳转到登录界面
//            Bundle bd = new Bundle();
//            bd.putString("redirecturl", redirectURL);
//            //跳转到登录界面
//            ActivityUtils.getInstance ().showActivity( ref.get() , PhoneLoginActivity.class , bd );
//        }else if(url.contains(Constants.WEB_TAG_INFO)){
//            //处理信息保护
//            return true;
//        }else if(url.contains(Constants.WEB_TAG_FINISH)){
//            if(view.canGoBack())
//                view.goBack();
//        }
//        else if(url.contains ( Constants.WEB_PAY ) ){
////            //支付进度
////            payProgress.showProgress ( "正在加载支付信息" );
////            payProgress.showAtLocation ( ref.get().getWindow().getDecorView(),  Gravity.CENTER, 0, 0 );
////            //支付模块
////            //获取信息
////            //截取问号后面的
////            //订单号
////            String tradeNo = null;
////            String customerID = null;
////            String paymentType = null;
////            PayModel payModel = new PayModel ();
////            url = url.substring ( url.indexOf ( ".aspx?" )+6, url.length () );
////            String[] str = url.split ( "&" );
////            for(String map : str)
////            {
////                String[] values = map.split ( "=" );
////                if(2 == values.length)
////                {
////                    if("trade_no".equals ( values[0] ))
////                    {
////                        tradeNo = values[1];
////                        payModel.setTradeNo ( tradeNo );
////                    }
////                    else if("customerID".equals ( values[0] ))
////                    {
////                        customerID = values[1];
////                        payModel.setCustomId ( customerID );
////                    }
////                    else if("paymentType".equals ( values[0] ))
////                    {
////                        paymentType = values[1];
////                        payModel.setPaymentType ( paymentType );
////                    }
////                }
////                else
////                {
////                    Log.i( UrlFilterUtils.class.getName() ,"支付参数出错.");
////                }
////            }
////            //获取用户等级
////            StringBuilder builder = new StringBuilder (  );
////            builder.append ( Constants.getINTERFACE_PREFIX() + "order/GetOrderInfo" );
////            builder.append ( "?orderid="+tradeNo );
////            AuthParamUtils param = new AuthParamUtils ( application, System.currentTimeMillis (), builder.toString () );
////            String orderUrl = param.obtainUrlOrder ( );
////            HttpUtil.getInstance ( ).doVolleyPay ( ref.get() ,  mHandler, application, orderUrl, payModel, payProgress );
//
//
//
//            getPayInfo(url , ref.get() );
//
//            return true;
//
//        }
//        else if(url.contains ( Constants.AUTH_FAILURE ) || url.contains( Constants.AUTH_FAILURE_PHONE) ){
//            //鉴权失效
//            //清除登录信息
//            application.logout ();
//
//
//            Uri uri = Uri.parse(url.toLowerCase());
//            String redirectURL = uri.getQueryParameter("redirecturl");
//
//            if(!TextUtils.isEmpty( redirectURL )){
//                redirectURL = Uri.decode( redirectURL );
//            }
//
//            if( !TextUtils.isEmpty( redirectURL ) && !redirectURL.toLowerCase().startsWith("http://") ){
//                if( !redirectURL.startsWith("/") ) redirectURL = "/"+ redirectURL;
//                redirectURL = uri.getHost() + redirectURL;
//
//                if( !redirectURL.toLowerCase().startsWith("http://") ){
//                    redirectURL = "http://"+ redirectURL;
//                }
//            }
//            //跳转到登录界面
//
//            Bundle bd = new Bundle();
//            bd.putString("redirecturl", redirectURL);
//
//            ActivityUtils.getInstance ().showActivity ( ref.get() , PhoneLoginActivity.class , bd);
//            return true;
//        }else if( url.toLowerCase().contains( Constants.URL_BINDINGWEIXING.toLowerCase() )){//拦截绑定微信url
//
//            Uri uri = Uri.parse(url.toLowerCase());
//            String redirectURL = uri.getQueryParameter("redirecturl");
//
//            if(!TextUtils.isEmpty( redirectURL )){
//                redirectURL = Uri.decode( redirectURL );
//            }
//
//            if( !TextUtils.isEmpty( redirectURL ) && !redirectURL.toLowerCase().startsWith("http://") ){
//                if( !redirectURL.startsWith("/") ) redirectURL = "/"+ redirectURL;
//                redirectURL = uri.getHost() + redirectURL;
//
//                if( !redirectURL.toLowerCase().startsWith("http://") ){
//                    redirectURL = "http://"+ redirectURL;
//                }
//
//            }
//
//            EventBus.getDefault().post(new BindEvent(true , redirectURL ));
//            return true;
//        }
//        else if(url.toLowerCase().contains(Constants.URL_APPACCOUNTSWITCHER.toLowerCase())){//切换帐号 url
//            String userId= Uri.parse(url).getQueryParameter("u");
//            EventBus.getDefault().post(new SwitchUserByUserIDEvent(userId));
//            return true;
//        }else if(UIUtils.isIndexPage( url )){//当用户点击商品详情的首页按钮时，需要处理判断
//            ActivityUtils.getInstance().showActivity( ref.get() , HomeActivity.class ,"redirecturl", url);
//            return true;
//        }else if( url.toLowerCase().contains( Constants.URL_PERSON_INDEX ) ){//当用户点击商品详情的个人中心按钮时，需要处理判断
//            ActivityUtils.getInstance().showActivity( ref.get() , HomeActivity.class ,"redirecturl", url);
//            return true;
//        }
////        else if( url.toLowerCase().contains("test.api.open.huobanplus.com:8081/cs/webchannelhtml.js")){ ///cest
////            view.loadUrl("http://192.168.3.22:8080/cs/webChannelHtml.js?customerId=3447&goodsId=20626&userId=17867&orderId=&t=1.2");
////            return true;
////        }
//        else{
//            //跳转到新界面
//            view.loadUrl(url , SignUtil.signHeader());
//            return true;
//        }
//        return false;
//    }
//
//
//    private void getPayInfo(String url , final Activity aty ){
//        if( aty ==null) return;
//
//        payProgress.showProgress ( "正在加载支付信息" );
//        payProgress.showAtLocation ( aty.getWindow().getDecorView(),  Gravity.CENTER, 0, 0 );
//
//        final PayModel payModel = new PayModel ();
//
//        Uri uri = Uri.parse(url);
//        final String orderId = uri.getQueryParameter("trade_no");
//        String customerId = uri.getQueryParameter("customerID");
//        String payType = uri.getQueryParameter("paymentType");
//        if( orderId ==null || orderId.isEmpty() ){
//            payProgress.dismissView();
//            ToastUtils.showLongToast("支付缺少订单信息");
//            return;
//        }
//
//        payModel.setCustomId(customerId);
//        payModel.setPaymentType(payType);
//        payModel.setTradeNo(orderId);
//
//        HttpUtil.getInstance().getOrderInfo( orderId , new Response.Listener<OrderInfoModel>() {
//            @Override
//            public void onResponse(OrderInfoModel orderInfoModel) {
//                if( payProgress!=null) payProgress.dismissView();
//                if( aty == null  ) return;
//
//                if(200 == orderInfoModel.getCode ()) {
//                    Map<String, String> data = orderInfoModel.getData();
//                    if (null == data) {
//                        //支付信息获取错误
//                        ToastUtils.showLongToast("获取订单信息失败。");
//                        return;
//                        //NoticePopWindow noticePop = new NoticePopWindow(aty, "获取订单信息失败。");
//                        //noticePop.showNotice();
//                        //noticePop.showAtLocation(aty.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//                    } else {
//                        String sign = data.get("sign");
//                        String orderId_2 = data.get("orderid");
//                        data.remove("sign");
//                        String sign_2 = new AuthParamUtils("").getSign(data , Constants.getAPP_SECRET() );
//                        if( sign == null || orderId_2 == null || !sign.equals(sign_2) || !orderId_2.equals(orderId) ){
//                            ToastUtils.showLongToast("订单信息验证失败！");
//                            return;
//                        }
//                        String name = data.get("name");
//
//                        payModel.setAmount(HttpUtil.formatToDecimal(data.get("finalamount")));
//                        payModel.setDetail( name );
//                        PayPopWindow payPopWindow = new PayPopWindow(aty, mHandler, payModel);
//                        payPopWindow.showAtLocation(aty.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
//                    }
//                } else{
//                    //支付信息获取错误
//                    //NoticePopWindow noticePop = new NoticePopWindow (  aty,  "获取订单信息失败。");
//                    //noticePop.showNotice ( );
//                    //noticePop.showAtLocation ( aty.getWindow().getDecorView() , Gravity.CENTER, 0, 0 );
//                    ToastUtils.showLongToast("获取订单信息失败。");
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                if(payProgress==null)return;
//                payProgress.dismissView();
//            }
//        });
//
//    }
//
//}
