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

	//生产  url
	//public final static String url="http://bmapi.mellkit.com";

	//test url
	public final static String url= "http://bmapi.51flashmall.com";


	//dev url
	//public final static String url= "http://bmapi.fancat.cn";
	//test miyao
	public final static String APP_SECRET="BAMEENG20161021";
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

	public final static String BASE_DATA ="BASE_DATA";

	/**
	 * ************************定位信息设置
	 */
	public static final String LOCATION_INFO = "location_info";
	public static final String ADDRESS       = "address";
	public static final String LATITUDE      = "latitude";
	public static final String LONGITUDE     = "Longitude";
	public static final String CITY          = "city";


	public static final String INTENT_URL   = "INTENT_URL";
	public static final String INTENT_TITLE = "INTENT_TITLE";

	public static final String CAPTURE_SCREEN = "CAPTURE_SCREEN";


	/**
	 * 基本信息
	 */
	public static final String BASE_INFO   = "base_ifo";



	/**
	 * 操作平台码
	 */
	public static final String OPERATION_CODE = "BUYER_ANDROID_2015DC";

	public static final String APPKEY = "b73ca64567fb49ee963477263283a1bf";

	/**
	 * 接收短信等待时间（毫米）
	 */
	public static int SMS_Wait_Time = 60000;

	/**
	 * 发送语音验证码等待时间（毫秒）
	 */
	public static int SMS_SEND_VOICE_TIME = 8000;

	/**
	 * 头像
	 */
	public static int User_1=1;
	/***
	 * 昵称
	 */
	public static int User_2=2;
	/***
	 * 手机
	 */
	public static int User_3=3;
	/***
	 * 姓名
	 */
	public static int User_4=4;
	/***
	 * 性别
	 */
	public static int User_5=5;
	/***
	 * 地区
	 */
	public static int User_6=6;
	/***
	 * 其他扩展
	 */
	public static int User_7=7;


	/**
	 * 选择地址
	 */
	public static final int SELECT_ADDRESS = 0x00000032;

	/***
	 * 未成交
	 */
	public static final int ORDER_NODEAL=0;
	/****
	 * 已成交
	 */
	public static final int ORDER_DEAL=1;
	/***
	 * 退单
	 */
	public static final int ORDER_BACK = 2;

	/***
	 * 已登录
	 */
	public  static final int USER_LOGIN_STATUS_LOGINED=1;
	/***
	 * 冻结
	 */
	public static final int USER_LOGIN_STATUS_FREEZE = 0;
	/***
	 * 未登录
	 */
	public static final int USER_LOGIN_STATUS_NOlOGIN = -1;
	/***
	 *
	 */
	public static final int STATUS_70035 = 70035;
	/***
	 * 盟主 类型
	 */
	public static final int MENG_ZHU=1;
	/***
	 * 盟友 类型
	 */
	public static final int MENG_YOU = 0;
	/***
	 * 分店 类型
	 */
	public static final int SHOP_BRANCH = 2;
	/***
	 * 总店 类型
	 */
	public static final  int SHOP_HEAD = 1;

	public static String SERVER_ERROR="服务器开小差了";

	public static String MESSAGE_INFO="message_info";
}
