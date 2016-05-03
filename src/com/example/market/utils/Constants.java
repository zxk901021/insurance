package com.example.market.utils;

public class Constants {

	public static final class INTENT_KEY {
		public static final String MENU_TO_GOODS_LIST = "category_menu";
		public static final int LOGIN_REQUEST_CODE = 20000;
		public static final int LOGIN_RESULT_SUCCESS_CODE = 20001;
		public static final int REQUEST_CART_TO_DETAIL = 50000;
		/** 从我的京东到更多，返回时刷新登录信息 */
		public static final int REQUEST_MOREACTIVITY = 60000;
		/** 将GoodsInfo对象传给商品列表界面 */
		public static final String INFO_TO_DETAIL = "goodsinfo_to_detail";
		/** 从我的关注跳转到首页 */
		public static final String FROM_FAVOR = "from_favor";
		/** 从详情跳转到购物车 */
		public static final String FROM_DETAIL = "from_detail";
		/** 刷新购物车商品数 */
		public static final String REFRESH_INCART = "refresh_incart";
		/** 通过Bmob登录成功 */
		public static final String LOGIN_BMOB_SUCCESS = "bmob_success";
		/** 注销 */
		public static final String LOGOUT = "logout";
		
		public static final String INSURANCE_REGISTER = "http://www.yiyingjie.com/InsuranceInterface/user_registration.php?";
	
		public static final String INSURANCE_LOGIN = "http://www.yiyingjie.com/InsuranceInterface/user_login.php?";
		
		public static final String INSURANCE_HOMEPAGE = "http://www.yiyingjie.com/InsuranceInterface/home_page.php?num=2";
	}

	public static final class BROADCAST_FILTER {
		public static final String FILTER_CODE = "broadcast_filter";
		public static final String EXTRA_CODE = "broadcast_intent";
	}

	public static final class URL {
		/** 应用推荐 */
		public static final String APPS = "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/apps/apps.txt";
		/** 菜单 */
		public static final String MENUJSON = "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/menujson/menujson.txt";

		/** ---------------------------------------------------------- **/
		public static final String SITE_URL = "http://192.168.1.152/ardapi/ardapi/";

		public static final String IMAGE_SITE = "http://192.168.1.152/ardapi/";

		public static final String HOT_GOODS = "index.php?fenlei=hot";

		public static final String NEWEST_GOODS = "index.php?fenlei=new";

		public static final String JICAI_GOODS = "index.php?fenlei=jicai";

		public static final String HELP_CENTER = "index.php?fenlei=bzhu";

		public static final String LOGIN = "user.php?act=act_login";

		public static final String CATEGORY = "index.php?fenlei=leftfl";

		public static final String SEARCH_GOODS = "search.php";

		public static final String CHECK_VIP = "distribute.php?act=fenxiao1&minid=minid&maxid=maxid";

		public static final String CATEGORY_LIST = "category.php?id=";

		public static final String USER_INFO = SITE_URL + "user.php?user_id=";

		public static final String DELIVERY_ADDRESS = SITE_URL
				+ "user.php?act=address_list";

		public static final String COLLECTION_LIST = SITE_URL + "user.php?act=collection_list";

		public static final String DELETE_COLLECTION = "user.php?act=delete_collection";

		public static final String ADD_CART = SITE_URL
				+ "flow.php?step=add_to_cart&sess_id=";

		public static final String ADD_DELIVERY_ADDRESS = SITE_URL
				+ "user.php?act=act_update_address";

		public static final String CHANGE_USER_INFO = SITE_URL
				+ "user.php?act=act_edit_profile&user_id=";

		public static final String CHANGE_PASSWORD = SITE_URL
				+ "user.php?act=act_edit_password&user_id=";

		public static final String MY_ORDER_LIST = SITE_URL
				+ "user.php?act=order_list";

		public static final String MY_EXCHANGE_GOODS = SITE_URL
				+ "user.php?act=back_list";

		public static final String SIGN_UP = SITE_URL
				+ "user.php?act=act_register";
		
		public static final String COLLECT = SITE_URL + "user.php?act=collect";
		
		public static final String CART_GOODS = SITE_URL + "flow.php?sess_id=";

	}

}
