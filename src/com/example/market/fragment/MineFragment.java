package com.example.market.fragment;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.util.ac;

import com.android.volley.VolleyError;
import com.example.market.R;
import com.example.market.activity.LoginActivity;
import com.example.market.activity.MainActivity;
import com.example.market.activity.MemberCenterListActivity;
import com.example.market.activity.MoreActivity;
import com.example.market.activity.PurseActivity;
import com.example.market.activity.UserInfoActivity;
import com.example.market.utils.Constants;
import com.example.market.utils.NotificationUtil;
import com.lib.uil.UILUtils;
import com.lib.volley.HTTPUtils;
import com.lib.volley.VolleyListener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class MineFragment extends Fragment implements OnClickListener {

	public static final int MY_ORDER = 1;// 我的订单
	public static final int DELIVERY_ADDRESS = 2;// 收货地址
	public static final int MY_EXCHANGE = 3;// 我的退换货
	public static final int MY_COLLOCET = 4;// 我的收藏
	public static final int MY_CART = 5;// 我的购物车
	public static final int MY_MESSAGE = 6;// 我的留言
	public static final int MY_COMMENT = 7;// 我的评论

	private View layout;
	private View mViewNotLogined;
	private View mViewLogined;
	private TextView mTvUid;
	private ImageView mImgUserIcon;
	private String uid;
	private String icon;
	private String userInfo;
	private ImageView userHeaderView;
	private TextView userName;
	private String userNameStr;
	private TextView isManager;
	private String isManagerStr;
	private LinearLayout deliveryAdress;
	private ScrollView scroll;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (layout != null) {
			initLogin();
			// 防止多次new出片段对象，造成图片错乱问题
			return layout;
		}
		layout = inflater.inflate(R.layout.fragment_mine, container, false);
		initView();
		setOnListener();
		initLogin();
		return layout;
	}

	/**
	 * 初始化登录信息
	 * 
	 * @param activity
	 * @param isLogined
	 */
	private void initLogin() {
		checkLoginStatus();
	}

	private void checkLoginStatus() {
		SharedPreferences sp = getActivity().getSharedPreferences("MyPrefer",
				Context.MODE_PRIVATE);
		boolean isLogined = sp.getBoolean("isLogin", false);
		if (isLogined) {
			// 读取登录类型
			uid = sp.getString("uid", "");
			mViewNotLogined.setVisibility(View.GONE);
			mViewLogined.setVisibility(View.VISIBLE);
			mTvUid.setText(uid);
		} else {
			mViewNotLogined.setVisibility(View.VISIBLE);
			mViewLogined.setVisibility(View.GONE);
		}
	}

	private void setOnListener() {
		layout.findViewById(R.id.layout_mine_order).setOnClickListener(this);
		layout.findViewById(R.id.layout_mine_wallet).setOnClickListener(this);
		layout.findViewById(R.id.layout_mine_messages).setOnClickListener(this);
		layout.findViewById(R.id.personal_login_button)
				.setOnClickListener(this);
		layout.findViewById(R.id.layout_mine_collects).setOnClickListener(this);
		layout.findViewById(R.id.layout_mine_history).setOnClickListener(this);
		layout.findViewById(R.id.layout_mine_appoint).setOnClickListener(this);
		layout.findViewById(R.id.layout_mine_discuss).setOnClickListener(this);
		layout.findViewById(R.id.tv_more).setOnClickListener(this);
		layout.findViewById(R.id.person_center_get_tihuoquan).setOnClickListener(this);
		layout.findViewById(R.id.person_center_next).setOnClickListener(this);
		layout.findViewById(R.id.person_center_delivery_address)
				.setOnClickListener(this);
		layout.findViewById(R.id.person_center_shoping_cart_layout)
				.setOnClickListener(this);
		layout.findViewById(R.id.person_center_quit_layout).setOnClickListener(
				this);
		scroll = (ScrollView) layout.findViewById(R.id.scrollView_mine);
		// layout.findViewById(R.id.layout_mine_feedback).setOnClickListener(this);
		// layout.findViewById(R.id.layout_mine_android_my_jd_assitant)
		// .setOnClickListener(this);
		// layout.findViewById(R.id.layout_mine_account_center).setOnClickListener(this);
		// layout.findViewById(R.id.layout_mine_service_manager).setOnClickListener(this);
	}

	private void initView() {
		mViewNotLogined = layout.findViewById(R.id.layout_not_logined);
		mViewLogined = layout.findViewById(R.id.layout_logined);
		mTvUid = (TextView) layout.findViewById(R.id.tv_uid);
		mImgUserIcon = (ImageView) layout.findViewById(R.id.user_icon);
		userHeaderView = (ImageView) layout.findViewById(R.id.user_icon);
		userName = (TextView) layout.findViewById(R.id.tv_uid);
		isManager = (TextView) layout.findViewById(R.id.tv_level);
		deliveryAdress = (LinearLayout) layout
				.findViewById(R.id.person_center_delivery_address);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// 将layout从父组件中移除
		ViewGroup parent = (ViewGroup) layout.getParent();
		parent.removeView(layout);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.INTENT_KEY.LOGIN_REQUEST_CODE) {
			if (resultCode == Constants.INTENT_KEY.LOGIN_RESULT_SUCCESS_CODE) {
				SharedPreferences sp = getActivity().getSharedPreferences(
						"login_type", Context.MODE_PRIVATE);
				int type = sp.getInt("login_type", 0);
				String uid = "";
				String icon = "";
				switch (type) {
				case 1:
					uid = data.getStringExtra("uid");
					break;
				case 2:
					uid = data.getStringExtra("screen_name");
					icon = data.getStringExtra("profile_image_url");
					UILUtils.displayImage(getActivity(), icon, mImgUserIcon);
					break;

				default:
					break;
				}
				mTvUid.setText(uid);
				mViewNotLogined.setVisibility(View.GONE);
				mViewLogined.setVisibility(View.VISIBLE);
				// 将登录结果设置给MainActivity
				MainActivity activity = (MainActivity) getActivity();
				activity.setIsLogined(true, uid, icon);
			} else if (resultCode == 1024) {
				SharedPreferences sp = getActivity().getSharedPreferences(
						"MyPrefer", Context.MODE_PRIVATE);
				String uid = sp.getString("uid", "");
				HTTPUtils.getVolley(getActivity(), Constants.URL.USER_INFO
						+ uid, new VolleyListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

					}

					@Override
					public void onResponse(String result) {
						userInfo = result;
						handler.sendEmptyMessage(1);
					}
				});
				mViewNotLogined.setVisibility(View.GONE);
				mViewLogined.setVisibility(View.VISIBLE);
			}
		} else if (requestCode == Constants.INTENT_KEY.REQUEST_MOREACTIVITY) {
			initLogin();
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.personal_login_button: // 登录
			login();
			break;

		case R.id.tv_more: // 更多
			intent.setClass(getActivity(), MoreActivity.class);
			startActivityForResult(intent,
					Constants.INTENT_KEY.REQUEST_MOREACTIVITY);
			break;
		case R.id.layout_mine_order: // 用户信息
			intent.setClass(getActivity(), UserInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.layout_mine_wallet: // 分销中心
			intent.setClass(getActivity(), PurseActivity.class);
			startActivity(intent);
			break;
		case R.id.layout_mine_messages: // 我的退换货
			intent.setClass(getActivity(), MemberCenterListActivity.class);
			intent.putExtra("mode", MY_EXCHANGE);
			startActivity(intent);
			break;
		case R.id.layout_mine_collects: // 我的订单
			// startActivity(new Intent(getActivity(), FavorActivity.class));
			intent.setClass(getActivity(), MemberCenterListActivity.class);
			intent.putExtra("mode", MY_ORDER);
			startActivity(intent);
			break;
		case R.id.layout_mine_history: // 我的留言
			intent.setClass(getActivity(), MemberCenterListActivity.class);
			intent.putExtra("mode", MY_MESSAGE);
			startActivity(intent);
			break;
		case R.id.layout_mine_appoint: // 我的收藏
			intent.setClass(getActivity(), MemberCenterListActivity.class);
			intent.putExtra("mode", MY_COLLOCET);
			startActivity(intent);
			break;
		case R.id.layout_mine_discuss: // 我的评论
			intent.setClass(getActivity(), MemberCenterListActivity.class);
			intent.putExtra("mode", MY_COMMENT);
			startActivity(intent);
			break;

		case R.id.person_center_next: // 呼叫客服

			intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ "58883222"));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		

			// SharedPreferences sp = getActivity().getSharedPreferences(
			// "MyPrefer", Context.MODE_PRIVATE);
			// String uid = sp.getString("uid", null);
			// Log.e("uid", uid);
			// Map<String, String> map = new HashMap<String, String>();
			// map.put("user_id", uid);
			// HTTPUtils.postVolley(getActivity(), Constants.URL.SITE_URL
			// + Constants.URL.CHECK_VIP, map, new VolleyListener() {
			//
			// @Override
			// public void onErrorResponse(VolleyError arg0) {
			//
			// }
			//
			// @Override
			// public void onResponse(String result) {
			// Log.e("vip", result);
			// }
			// });
			break;
		case R.id.person_center_get_tihuoquan:
			NotificationUtil.notificationMethod(getActivity());
			break;

		case R.id.person_center_quit_layout:
			SharedPreferences sp = getActivity().getSharedPreferences(
					"MyPrefer", Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putBoolean("isLogin", false);
			editor.commit();
			checkLoginStatus();
			scroll.scrollTo(0, 0);
			break;

		case R.id.person_center_shoping_cart_layout:
			MainActivity activity = (MainActivity) getActivity();
			activity.activeCart();
			// intent.setClass(getActivity(), MemberCenterListActivity.class);
			// intent.putExtra("mode", MY_CART);
			// startActivity(intent);
			break;

		case R.id.person_center_delivery_address:// 收货地址
			intent.setClass(getActivity(), MemberCenterListActivity.class);
			intent.putExtra("mode", 2);
			startActivity(intent);
			break;

		default:
			break;
		// case R.id.layout_mine_account_center: // 账户与安全
		// Intent intent4 = new Intent(getActivity(), WebActivity.class);
		// intent4.putExtra("direction", 12);
		// startActivity(intent4);
		// break;
		// case R.id.layout_mine_service_manager: // 服务管家
		// Intent intent5 = new Intent(getActivity(), WebActivity.class);
		// intent5.putExtra("direction", 10);
		// startActivity(intent5);
		// break;
		// case R.id.layout_mine_feedback: // 意见反馈
		// new FeedbackAgent(getActivity()).startFeedbackActivity();
		// break;
		// case R.id.layout_mine_android_my_jd_assitant: // 贴心服务
		// Intent intent = new Intent(getActivity(), WebActivity.class);
		// intent.putExtra("direction", 5);
		// startActivity(intent);
		// break;

		}
	}

	private void login() {
		Intent intent = new Intent(getActivity(), LoginActivity.class);
		startActivityForResult(intent, Constants.INTENT_KEY.LOGIN_REQUEST_CODE);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				try {
					JSONObject userInfoJson = new JSONObject(userInfo);
					userNameStr = userInfoJson.getString("username");
					isManagerStr = userInfoJson.getString("tianxin");
					if (!TextUtils.isEmpty(userNameStr)) {
						userName.setText(userNameStr);
					}
					if (!TextUtils.isEmpty(isManagerStr)) {
						String temp = isManagerStr.equals("1") ? "是" : "否";
						isManager.setText("掌柜：" + temp);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		};
	};

}
