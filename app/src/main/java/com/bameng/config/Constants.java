package com.bameng.config;


public class Constants {
	// 屏幕高度
	public static int SCREEN_HEIGHT = 800;

	// 屏幕宽度
	public static int SCREEN_WIDTH = 480;

	// 屏幕密度
	public static float SCREEN_DENSITY = 1.5f;

	// 分享成功
	public static final int SHARE_SUCCESS = 0X1000;

	// 分享取消
	public static final int SHARE_CANCEL = 0X2000;

	// 分享失败
	public static final int SHARE_ERROR = 0X3000;

	//左侧划状态
	public static final int    LEFT_IMG_SIDE = 0X33310;
	// 返回状态
	public static final int    LEFT_IMG_BACK = 0X33311;
	/**
	 ******************************************* 参数设置信息结束
	 * ******************************************
	 */
	/**
	 * 菜单标记1
	 */
	public final static String TAG_1 = "home";

	/**
	 * 菜单标记2
	 */
	public final static String TAG_2 = "newest";

	/**
	 * 菜单标记3
	 */
	public final static String TAG_3 = "list";

	/**
	 * 菜单标记4
	 */
	public final static String TAG_4 = "profile";
	//切换内容页
	public static final int SWITCH_UI = 0x00000010;



	//test url
	public final static String url= "http://bmapi.fancat.cn";
	//test miyao
	public final static String APP_SECRET="BAMEENG20161021_TEST";
	// url
	//public final static String url= "http://bmapi.fancat.cn";
	//App_Secret
	//public final static String APP_SECRET="BAMEENG20161021";

	//标准时间
	public final static String TIME_FORMAT   = "yyyy-MM-dd HH:mm:ss";
	//标准时间01
	public static final String DATE_FORMAT   = "yyyy-MM-dd";


	/**
	 * *******************preference参数设置
	 */
	// 登录信息文件
	public final static String LOGIN_USER_INFO = "login_user_info";

	/**
	 * ************************定位信息设置
	 */
	public static final String LOCATION_INFO = "location_info";
	public static final String ADDRESS       = "address";
	public static final String LATITUDE      = "latitude";
	public static final String LONGITUDE     = "Longitude";
	public static final String CITY          = "city";

	//http请求参数
	//获取具体页面的商品类别
	public static final String HTTP_OBTAIN_CATATORY = "/goods/obtainCatagory?";
	//获取商品信息
	public static final String HTTP_OBTAIN_GOODS    = "/goods/obtainGoods?";
	//new view
	public static final String WEB_TAG_NEWFRAME     = "__newframe";
	//上传图片
	public static final String WEB_TAG_COMMITIMG    = "partnermall520://pickimage";
	//登出
	public static final String WEB_TAG_LOGOUT       = "partnermall520://logout";
	//信息保护
	public static final String WEB_TAG_INFO         = "partnermall520://togglepb";
	//关闭当前页
	public static final String WEB_TAG_FINISH       = "partnermall520://closepage";
	//share
	public static final String WEB_TAG_SHARE        = "partnermall520://shareweb";
	//弹出框
	public static final String WEB_TAG_ALERT        = "partnermall520://alert";
	//支付
	public static final String WEB_TAG_PAYMENT      = "partnermall520://payment";
	//用户信息修改
	public static final String WEB_TAG_USERINFO     = "partnermall520://userinfo?";
	//联系客服
	public static final String WEB_CONTACT          = "mqqwpa://im/chat";
	//支付
	public static final String WEB_PAY      = "/Mall/AppAlipay.aspx";
	//鉴权失效
	public static final String AUTH_FAILURE = "/UserCenter/Login.aspx";

	public static final String AUTH_FAILURE_PHONE = "/invite/MobileLogin.aspx";

	//绑定微信
	public static final String URL_BINDINGWEIXING="/UserCenter/BindingWeixin.aspx";

	//切换账户
	public static final String URL_APPACCOUNTSWITCHER="/UserCenter/AppAccountSwitcher.aspx";

	/**
	 * 个人中心 页面
	 */
	public final static String URL_PERSON_INDEX="usercenter/index.aspx";

	//鉴权失效
	public static final int LOGIN_AUTH_ERROR = 2131;

	//鉴权成功
	public static final int LOGIN_AUTH_SUCCESS=2132;

	//网页支付
	public static final int PAY_NET = 2222;

	//是否测试环境
	public static final boolean IS_TEST = true;

	//handler code
	//1、success
	public static final int SUCCESS_CODE = 0;
	//2、error code
	public static final int ERROR_CODE   = 1;
	//3、null code
	public static final int NULL_CODE    = 2;

	//微信登录:用户存在
	public static final int MSG_USERID_FOUND    = 1;
	//微信登录：用户不存在
	public static final int MSG_USERID_NO_FOUND = 0;
	public static final int MSG_LOGIN           = 2;
	public static final int MSG_AUTH_CANCEL     = 3;
	public static final int MSG_AUTH_ERROR      = 4;
	public static final int MSG_AUTH_COMPLETE   = 5;

	public static final String INTENT_URL   = "INTENT_URL";
	public static final String INTENT_TITLE = "INTENT_TITLE";

	/**
	 * 修改密码
	 */
	public static final String MODIFY_PSW = "modifyPsw";

	/**
	 * 侧滑菜单加载页面
	 */
	public static final int LOAD_PAGE_MESSAGE_TAG = 4381;

	/**
	 * 关闭载入用户数据
	 */
	public static final int LOAD_SWITCH_USER_OVER = 8888;

	/**
	 * tile栏刷新页面
	 */
	public static final int FRESHEN_PAGE_MESSAGE_TAG = 4380;

	/**
	 * 初始化菜单失败
	 */
	public static final int INIT_MENU_ERROR = 6361;

	/**
	 * 切换用户
	 */
	public static final int SWITCH_USER_NOTIFY = 9988;

	/**
	 * 首页Url
	 */
	public static final String HOME_PAGE_URL = "http://www.baidu.com";

	/**
	 * 基本信息
	 */
	public static final String BASE_INFO   = "base_ifo";
	/**
	 * 当前加载的页面
	 */
	public static final String CURRENT_URL = "current_url";

	//页面的类型
	/**
	 * 设置界面
	 */
	public static final String PAGE_TYPE_SETTING = "setting";

	/**
	 * 微信支付appID
	 */
	public static final String WXPAY_ID    = "wx2f2604e380cf6be1";
	public static final String WXPAY_SECRT = "ae3a7d851f24bfc97047954fa3975cec";
	public static final String PAY_URL     = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

	// 商户PID
	public static final String PARTNER      = "";
	// 商户收款账号
	public static final String SELLER       = "";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE  = "";
	// 支付宝公钥
	public static final String RSA_PUBLIC   = "";
	public static final int    SDK_PAY_FLAG = 1;

	public static final int SDK_CHECK_FLAG = 2;

	// API密钥，在商户平台设置(微信支付商户)
	public static final String wxpayApikey    = "0db0d4908a6ae6a09b0a7727878f0ca6";
	//微信parterKey
	public static final String wxpayParterkey = "1251040401";

	//微信支付
	public static final String WX_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";


	/**
	 * 操作平台码
	 */
	public static final String OPERATION_CODE = "BUYER_ANDROID_2015DC";

	public static final String APPKEY = "b73ca64567fb49ee963477263283a1bf";

	/**
	 * capCode
	 */
	public static final String CAP_CODE = "default";

	public final static int ANIMATION_COUNT = 2000;

	/**
	 * app系统配置
	 */
	public static final String SYS_INFO    = "sysInfo";
	public static final String SYS_PACKAGE = "sysPackage";
	public static final String FIRST_OPEN  = "firstOpen";

	/**
	 * app颜色配置
	 */
	public static final String COLOR_INFO   = "colorInfo";
	public static final String COLOR_MAIN   = "mainColor";
	public static final String COLOR_SECOND = "secondColor";

	public static final String CUSTOMER_ID = "customerid={}";
	public static final String USER_ID     = "userid={}";

	public static final String ALIPAY_NOTIFY = "alipay_notify";
	public static final String WEIXIN_NOTIFY = "weixin_notify";
	public static final String IS_WEB_WEIXINPAY = "is_web_weixinpay";
	public static final String IS_WEB_ALIPAY = "is_web_alipay";

	public static final String COMMON_SHARE_LOGO = "http://1804.img.pp.sohu.com.cn/images/2013/1/14/16/2/6205e011f029437o_13cfbf362e6g85.jpg";

	//数据包版本号
	public static final String DATA_INIT            = "data_init";
	//会员信息
	public static final String PACKAGE_VERSION              = "package_version";



	public final static String HUOTU_PUSH_KEY ="huotu_push_key";

	/**
	 * 接收短信等待时间（毫米）
	 */
	public static int SMS_Wait_Time = 60000;

	/**
	 * 发送语音验证码等待时间（毫秒）
	 */
	public static int SMS_SEND_VOICE_TIME = 8000;
}
