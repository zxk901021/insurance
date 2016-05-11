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


}
