package com.example.market.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.example.market.R;
import com.example.market.activity.BoxActivity;
import com.example.market.activity.DetailActivity;
import com.example.market.activity.FavorActivity;
import com.example.market.activity.HelpWebActivity;
import com.example.market.activity.InsuranceDetailActivity;
import com.example.market.activity.LoginActivity;
import com.example.market.activity.MainActivity;
import com.example.market.activity.OrdersActivity;
import com.example.market.activity.PurseActivity;
import com.example.market.activity.SearchActivity;
import com.example.market.activity.WebActivity;
import com.example.market.bean.Goods;
import com.example.market.bean.GoodsInfo;
import com.example.market.bean.HelpCenterItem;
import com.example.market.bean.Insurance;
import com.example.market.utils.Constants;
import com.example.market.utils.NotificationUtil;
import com.google.gson.JsonObject;
import com.lib.volley.HTTPUtils;
import com.lib.volley.VolleyListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.zhy_9.shopping_mall.adapter.GoodsGridViewAdapter;
import com.zhy_9.shopping_mall.adapter.HelpItemAdapter;
import com.zhy_9.shopping_mall.widget.InternalGridView;
import com.zhy_9.shopping_mall.widget.InternalListView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class HomeFragment extends Fragment implements OnClickListener {

	// private PullToRefreshScrollView mPtrScrollView;

	private SwipeRefreshLayout refreshLayout;

	private InternalGridView hotGoodsGrid, newestGoodsGrid, jicaiGoodsGrid;

	private InternalListView helpItemList;

	private String hotGoodsResult, newestGoodsResult, jicaiGoodsResult,
			helpItemResult;
	private List<Goods> hotGoodsData, newestGoodsData, jicaiGoodsData;
	private List<HelpCenterItem> helpItemData;
	private GoodsGridViewAdapter hotAdapter, newestAdapter, jicaiAdapter;
	private HelpItemAdapter helpItemAdapter;

	private ViewPager mPager;
	private int[] mBanner = new int[] { R.drawable.one,
			R.drawable.two, R.drawable.three,
			R.drawable.four };
	private GoodsInfo[] mInfos;
	private ImageView mImageView;
	private ImageView mImgCover;
	private int mLastPos;// 记录上一次ViewPager的位置
	private boolean isDragging;// 是否被拖拽
	private boolean isFoucusRight; // ScrollView是否滚动到右侧
	private View layout;

	private HorizontalScrollView mScrollView;
	private HorizontalScrollView mScrollView2;

	private DrawerLayout mDrawerLayout;
	private FrameLayout mFrame;

	private RelativeLayout mainLayout;
	private Button login;
	private boolean isLogin;
	private String homepageResult;
	
	private List<Insurance> insurances;
	
	private TextView childrenTv1, childrenTv2, oldTv1, oldTv2, travelTv1, travelTv2, careerTv1, careerTv2;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				JSONArray ar;
				hotGoodsData = new ArrayList<Goods>();

				try {
					ar = new JSONArray(hotGoodsResult);
					for (int i = 0; i < ar.length(); i++) {
						JSONObject ob = ar.getJSONObject(i);
						Goods hot = new Goods();
						hot.setName(ob.getString("name"));
						hot.setShopPrice(ob.getString("shop_price"));
						hot.setThumb(ob.getString("thumb"));
						hot.setConUrl(ob.getString("conurl"));
						hot.setId(ob.getString("id"));
						hotGoodsData.add(hot);
					}
					hotAdapter = new GoodsGridViewAdapter(hotGoodsData,
							getActivity());
					hotGoodsGrid.setAdapter(hotAdapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			case 2:
				JSONArray array;
				newestGoodsData = new ArrayList<Goods>();
				try {
					array = new JSONArray(newestGoodsResult);
					for (int i = 0; i < array.length(); i++) {
						JSONObject ob = array.getJSONObject(i);
						Goods newest = new Goods();
						newest.setName(ob.getString("name"));
						newest.setShopPrice(ob.getString("shop_price"));
						newest.setThumb(ob.getString("thumb"));
						newest.setConUrl(ob.getString("conurl"));
						newest.setId("id");
						newestGoodsData.add(newest);
					}
					newestAdapter = new GoodsGridViewAdapter(newestGoodsData,
							getActivity());
					newestGoodsGrid.setAdapter(newestAdapter);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case 3:
				JSONArray arrays;
				jicaiGoodsData = new ArrayList<Goods>();
				try {
					arrays = new JSONArray(jicaiGoodsResult);
					Log.e("jicai", jicaiGoodsResult);
					for (int i = 0; i < arrays.length(); i++) {
						JSONObject ob = arrays.getJSONObject(i);
						Goods newest = new Goods();
						newest.setName(ob.getString("goods_name"));
						newest.setShopPrice(ob.getString("last_price"));
						newest.setThumb(ob.getString("thumb"));
						newest.setId(ob.getString("goods_id"));
						jicaiGoodsData.add(newest);
					}
					jicaiAdapter = new GoodsGridViewAdapter(jicaiGoodsData,
							getActivity());
					jicaiGoodsGrid.setAdapter(jicaiAdapter);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case 4:
				JSONArray help;
				helpItemData = new ArrayList<HelpCenterItem>();
				try {
					help = new JSONArray(helpItemResult);
					for (int i = 0; i < help.length(); i++) {
						HelpCenterItem centerItem = new HelpCenterItem();
						centerItem.setCatName(help.getJSONObject(i).getString(
								"cat_name"));
						String time = help.getJSONObject(i).getString(
								"add_time");
						String url = help.getJSONObject(i).getString("cat_id");
						long times = Long.parseLong(time);
						Date date = new Date(times);
						SimpleDateFormat format = new SimpleDateFormat("MM-dd");
						String addTime = format.format(date);
						centerItem.setTime(addTime);
						centerItem.setUrl(url);
						helpItemData.add(centerItem);
					}
					helpItemAdapter = new HelpItemAdapter(helpItemData,
							getActivity());
					helpItemList.setAdapter(helpItemAdapter);
					helpItemList
							.setOnItemClickListener(new OnItemClickListener() {
								public void onItemClick(
										android.widget.AdapterView<?> parent,
										View view, int position, long id) {
									Intent intent = new Intent(getActivity(),
											HelpWebActivity.class);
									intent.putExtra("url",
											helpItemData.get(position).getUrl());
									startActivity(intent);
								};
							});
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
				
			case 5:
				try {
					JSONObject object = new JSONObject(homepageResult);
					String code = object.getString("code");
					
					if (code.equals("001")) {
						insurances = new ArrayList<Insurance>();
						JSONArray insur = new JSONArray();
						insur = object.getJSONArray("list");
						for (int i = 0; i < insur.length(); i ++ ){
							JSONObject ins = insur.getJSONObject(i);
							Insurance insurance = new Insurance();
							insurance.setId(ins.getString("id"));
							insurance.setName(ins.getString("name"));
							insurance.setParentId(ins.getString("parent_id"));
							insurances.add(insurance);
						}
						childrenTv1.setText(insurances.get(0).getName());
						childrenTv2.setText(insurances.get(1).getName());
						oldTv1.setText(insurances.get(2).getName());
						oldTv2.setText(insurances.get(3).getName());
						travelTv1.setText(insurances.get(4).getName());
						travelTv2.setText(insurances.get(5).getName());
						careerTv1.setText(insurances.get(6).getName());
						careerTv2.setText(insurances.get(7).getName());
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
			refreshLayout.setRefreshing(false);
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initGoodsInfos();
		if (layout != null) {
			// 防止多次new出片段对象，造成图片错乱问题
			return layout;
		}
		layout = inflater.inflate(R.layout.fragment_home, container, false);
		SharedPreferences sp = getActivity().getSharedPreferences("MyPrefer",
				Context.MODE_PRIVATE);
		isLogin = sp.getBoolean("isLogin", false);
		initView();
		initPager();
		autoScroll();
		initData();
		initInsurance();

		return layout;
	}
	
	private void initInsurance(){
		HTTPUtils.getVolley(getActivity(), Constants.INTENT_KEY.INSURANCE_HOMEPAGE, new VolleyListener() {
			
			@Override
			public void onErrorResponse(VolleyError arg0) {
				
			}
			
			@Override
			public void onResponse(String response) {
				homepageResult = response;
				handler.sendEmptyMessage(5);
			}
		});
	}

	private void initData() {
		HTTPUtils.getVolley(getActivity(), Constants.URL.SITE_URL
				+ Constants.URL.HOT_GOODS, new VolleyListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

			}

			@Override
			public void onResponse(String result) {
				hotGoodsResult = result;
				if (!TextUtils.isEmpty(hotGoodsResult)) {
					handler.sendEmptyMessage(1);
				}

			}
		});
		HTTPUtils.getVolley(getActivity(), Constants.URL.SITE_URL
				+ Constants.URL.NEWEST_GOODS, new VolleyListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

			}

			@Override
			public void onResponse(String result) {
				newestGoodsResult = result;
				if (!TextUtils.isEmpty(newestGoodsResult)) {
					handler.sendEmptyMessage(2);
				}
			}
		});
		HTTPUtils.getVolley(getActivity(), Constants.URL.SITE_URL
				+ Constants.URL.JICAI_GOODS, new VolleyListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

			}

			@Override
			public void onResponse(String result) {
				jicaiGoodsResult = result;
				if (!TextUtils.isEmpty(jicaiGoodsResult)) {
					handler.sendEmptyMessage(3);
				}
			}
		});
		HTTPUtils.getVolley(getActivity(), Constants.URL.SITE_URL
				+ Constants.URL.HELP_CENTER, new VolleyListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

			}

			@Override
			public void onResponse(String result) {
				helpItemResult = result;
				handler.sendEmptyMessage(4);
			}
		});
	}

	private void initGoodsInfos() {
		mInfos = new GoodsInfo[] {
				new GoodsInfo(
						"100001",
						"Levi's李维斯男士休闲时尚潮流短袖T恤82176-0005 灰/白 L",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods01.jpg",
						"服饰鞋包", 153.00, "好评96%", 1224, 1, 0),
				new GoodsInfo(
						"100002",
						"Levi's李维斯505系列男士舒适直脚牛仔裤00505-1185 牛仔色 36 34",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods02.jpg",
						"服饰鞋包", 479.00, "好评95%", 645, 0, 0),
				new GoodsInfo(
						"100003",
						"GXG男装 京东专供 2015夏装新款 男士时尚白色修身圆领短袖T恤#42244315 白色 M",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods03.jpg",
						"服饰鞋包", 149.00, "暂无评价", 1856, 0, 0),
				new GoodsInfo(
						"100004",
						"Apple iPad mini ME276CH/A 配备 Retina 显示屏 7.9英寸平板电脑 （16G WiFi版）深空灰色",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods04.jpg",
						"电脑数码", 2138.00, "好评97%", 865, 0, 0),
				new GoodsInfo(
						"100005",
						"联想（ThinkPad）轻薄系列E450C(008CD) 14英寸笔记本电脑 （i3-4005U 4GB 500G+8GSSD 1G WIN8.1）",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods05.jpg",
						"电脑数码", 3299.00, "好评95%", 236, 0, 0),
				new GoodsInfo(
						"100006",
						"罗技（Logitech）G502 自适应游戏鼠标",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods06.jpg",
						"服饰鞋包", 499.00, "好评95%", 115, 0, 0),
				new GoodsInfo(
						"100007",
						"瑞士军刀（Swissgear）SA7777WH 12英寸时尚休闲型双肩电脑背包 米白色",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods07.jpg",
						"服饰鞋包", 199.00, "好评95%", 745, 0, 0),
				new GoodsInfo(
						"100008",
						"创见（Transcend） 340系列 256G SATA3 固态硬盘(TS256GSSD340)",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods08.jpg",
						"电脑数码", 569.00, "好评95%", 854, 1, 0),
				new GoodsInfo(
						"100009",
						"佳能（Canon） EOS 700D 单反套机 （EF-S 18-135mm f/3.5-5.6 IS STM镜头）",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods09.jpg",
						"电脑数码", 5099.00, "好评94%", 991, 0, 0),
				new GoodsInfo(
						"100010",
						"飞轮威尔（F-WHEEL) 智能电动独轮车 自平衡独轮车 海豚系列拉杆 支架 音响 蓝牙 白色D1续航20KM无支架",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods10.jpg",
						"运动户外", 2999.00, "好评93%", 1145, 0, 0),
				new GoodsInfo(
						"100011",
						"永久21速26寸铝合金自行车 禧玛诺变速 铝肩可调锁死减震山地车 QJ243 自营",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods11.jpg",
						"运动户外", 1088.00, "好评92%", 909, 0, 0),
				new GoodsInfo(
						"100012",
						"我们都一样年轻又彷徨 自营",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods12.jpg",
						"图书音像", 25.40, "好评95%", 1443, 0, 0),
				new GoodsInfo(
						"100013",
						"近在远方",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods13.jpg",
						"图书音像", 19.70, "好评98%", 3702, 0, 0),
				new GoodsInfo(
						"100014",
						"自在的旅行",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods14.jpg",
						"图书音像", 38.40, "好评97%", 442, 1, 0),
				new GoodsInfo(
						"100015",
						"Photoshop专业抠图技法 赠光盘1张",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods15.jpg",
						"图书音像", 57.80, "好评93%", 765, 0, 0) };
	}

	private void initView() {
		childrenTv1 = (TextView) layout.findViewById(R.id.child_tv1);
		childrenTv2 = (TextView) layout.findViewById(R.id.child_tv2);
		oldTv1 = (TextView) layout.findViewById(R.id.old_tv1);
		oldTv2 = (TextView) layout.findViewById(R.id.old_tv2);
		travelTv1 = (TextView) layout.findViewById(R.id.travel_tv1);
		travelTv2 = (TextView) layout.findViewById(R.id.travel_tv2);
		careerTv1 = (TextView) layout.findViewById(R.id.career_tv1);
		careerTv2 = (TextView) layout.findViewById(R.id.career_tv2);
		layout.findViewById(R.id.img_home_category).setOnClickListener(this);
		layout.findViewById(R.id.img_home_search_code).setOnClickListener(this);
		layout.findViewById(R.id.layout_my_focus).setOnClickListener(this);
		layout.findViewById(R.id.layout_logistics).setOnClickListener(this);
		layout.findViewById(R.id.layout_top_up).setOnClickListener(this);
		layout.findViewById(R.id.layout_film).setOnClickListener(this);
		layout.findViewById(R.id.layout_game_top_up).setOnClickListener(this);
		layout.findViewById(R.id.layout_purse).setOnClickListener(this);
		layout.findViewById(R.id.layout_jingdou).setOnClickListener(this);
		layout.findViewById(R.id.layout_more).setOnClickListener(this);
		layout.findViewById(R.id.layout_recom).setOnClickListener(this);
		layout.findViewById(R.id.layout_recom2).setOnClickListener(this);
		layout.findViewById(R.id.layout_recom3).setOnClickListener(this);
		layout.findViewById(R.id.layout_recom4).setOnClickListener(this);
		layout.findViewById(R.id.layout_recom5).setOnClickListener(this);
		layout.findViewById(R.id.img_banner6).setOnClickListener(this);
		layout.findViewById(R.id.img_banner7).setOnClickListener(this);
		layout.findViewById(R.id.layout_special).setOnClickListener(this);
		layout.findViewById(R.id.layout_special2).setOnClickListener(this);
		layout.findViewById(R.id.layout_special3).setOnClickListener(this);
		layout.findViewById(R.id.layout_special4).setOnClickListener(this);
		layout.findViewById(R.id.img_banner8).setOnClickListener(this);
		layout.findViewById(R.id.img_banner9).setOnClickListener(this);
		layout.findViewById(R.id.home_children).setOnClickListener(this);
		layout.findViewById(R.id.home_children_2).setOnClickListener(this);
		layout.findViewById(R.id.home_old).setOnClickListener(this);
		layout.findViewById(R.id.old_man).setOnClickListener(this);
		layout.findViewById(R.id.home_go).setOnClickListener(this);
		layout.findViewById(R.id.home_go_2).setOnClickListener(this);
		layout.findViewById(R.id.home_job).setOnClickListener(this);
		layout.findViewById(R.id.home_job_2).setOnClickListener(this);
		login = (Button) layout.findViewById(R.id.home_pages_login_btn);
		login.setOnClickListener(this);
		if (isLogin) {
			login.setText("已登陆");
			login.setEnabled(false);
		}
		mainLayout = (RelativeLayout) layout.findViewById(R.id.RelativeLayout1);
		mDrawerLayout = (DrawerLayout) layout
				.findViewById(R.id.home_page_drawer);
		mFrame = (FrameLayout) layout.findViewById(R.id.home_slide_frame);
		hotGoodsGrid = (InternalGridView) layout
				.findViewById(R.id.home_page_hot_grid_view);
		newestGoodsGrid = (InternalGridView) layout
				.findViewById(R.id.home_page_newest_grid_view);
		jicaiGoodsGrid = (InternalGridView) layout
				.findViewById(R.id.home_page_jicai_grid_view);
		hotGoodsGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				gotoDetail(hotGoodsData.get(position).getId(), hotGoodsData
						.get(position).getConUrl());
			}
		});
		newestGoodsGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				gotoDetail(newestGoodsData.get(position).getId(),
						newestGoodsData.get(position).getConUrl());
			}
		});
		jicaiGoodsGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				gotoDetail(jicaiGoodsData.get(position).getId(), jicaiGoodsData
						.get(position).getConUrl());
			}
		});
		helpItemList = (InternalListView) layout
				.findViewById(R.id.help_center_item_list);
		refreshLayout = (SwipeRefreshLayout) layout
				.findViewById(R.id.swipe_refresh);
		refreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
//				handler.sendEmptyMessageDelayed(1, 2500);
			}
		});
		refreshLayout.setColorSchemeColors(getResources().getColor(
				R.color.title_bg));
		// mPtrScrollView = (PullToRefreshScrollView) layout
		// .findViewById(R.id.ptrScrollView_home);
		// mPtrScrollView
		// .setOnRefreshListener(new OnRefreshListener<ScrollView>() {
		// public void onRefresh(
		// PullToRefreshBase<ScrollView> refreshView) {
		// new GetDataTask().execute();
		// }
		// });

		mImgCover = (ImageView) layout.findViewById(R.id.img_cover);
		mImageView = (ImageView) layout.findViewById(R.id.img_indicator01);
		mScrollView = (HorizontalScrollView) layout
				.findViewById(R.id.layout_recom_banner);
		mScrollView2 = (HorizontalScrollView) layout
				.findViewById(R.id.layout_special_banner);
	}

	private void activeCategory() {
		MainActivity activity = (MainActivity) getActivity();
		activity.activeCategory();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// 将layout从父组件中移除
		ViewGroup parent = (ViewGroup) layout.getParent();
		parent.removeView(layout);
	}

	private void initPager() {
		mPager = (ViewPager) layout.findViewById(R.id.pager_banner);
		FragmentManager fm = getChildFragmentManager();
		MyPagerAdapter adapter = new MyPagerAdapter(fm);
		mPager.setAdapter(adapter);
		mPager.setCurrentItem(1000);
		mPager.setOnPageChangeListener(new MyPagerListener());
	}

	/**
	 * 自动滚动
	 */
	private void autoScroll() {
		mPager.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (!isDragging) {
					// 若用户没有拖拽，则自动滚动
					mPager.setCurrentItem(mPager.getCurrentItem() + 1);
				}
				if (isFoucusRight) {
					mScrollView.fullScroll(ScrollView.FOCUS_LEFT);
				} else {
					mScrollView.fullScroll(ScrollView.FOCUS_RIGHT);
				}
				isFoucusRight = !isFoucusRight;
				mPager.postDelayed(this, 3000);
			}
		}, 3000);
		mScrollView2.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (isFoucusRight) {
					mScrollView2.fullScroll(ScrollView.FOCUS_RIGHT);
				} else {
					mScrollView2.fullScroll(ScrollView.FOCUS_LEFT);
				}
				mScrollView2.postDelayed(this, 3000);
			}
		}, 4000);
	}

	class MyPagerAdapter extends FragmentStatePagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			BannerItemFragment fragment = new BannerItemFragment();
			fragment.setResId(mBanner[position % mBanner.length]);
			fragment.setGoodsInfo(mInfos[position % mBanner.length]);
			return fragment;
		}

		@Override
		public int getCount() {
			return 10000;
		}

	}

	class MyPagerListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			int width = mImgCover.getWidth();
			LayoutParams layoutParams = (LayoutParams) mImageView
					.getLayoutParams();
			int rightMargin = layoutParams.rightMargin;
			int endPos = (width + rightMargin) * (position % 4);
			int startPos = 0;
			if (mLastPos < position) {
				// 图片向右移动
				startPos = (width + rightMargin) * (position % 4 - 1);
			} else {
				// 图片向左移动
				startPos = (width + rightMargin) * (position % 4 + 1);
			}
			ObjectAnimator.ofFloat(mImgCover, "translationX", startPos, endPos)
					.setDuration(300).start();
			mLastPos = position;
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			switch (state) {
			case ViewPager.SCROLL_STATE_DRAGGING:
				// 用户拖拽
				isDragging = true;
				break;
			case ViewPager.SCROLL_STATE_IDLE:
				// 空闲状态
				isDragging = false;
				break;
			case ViewPager.SCROLL_STATE_SETTLING:
				// 被释放时
				isDragging = false;
				break;

			default:
				break;
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_home_category: // 切换到分类
			// activeCategory();
			mDrawerLayout.openDrawer(Gravity.LEFT);
			// mDrawerLayout.setFocusable(true);
			// mainLayout.setFocusable(false);
			// mainLayout.requestDisallowInterceptTouchEvent(false);
			mFrame.requestDisallowInterceptTouchEvent(true);
			// getActivity().findViewById(android.R.id.tabhost).clearFocus();
			break;
		case R.id.img_home_search_code: // 二维码扫描
			((MainActivity) getActivity()).scanQRCode();
			break;
//		case R.id.layout_my_focus: // 我的关注
//			startActivity(new Intent(getActivity(), FavorActivity.class));
//			break;
//		case R.id.layout_logistics: // 物流查询
//			startActivity(new Intent(getActivity(), OrdersActivity.class));
//			break;
//		case R.id.layout_top_up: // 充值
//			Intent intent = new Intent(getActivity(), WebActivity.class);
//			intent.putExtra("direction", 1);
//			startActivity(intent);
//			break;
//		case R.id.layout_film: // 电影票
//			Intent intent2 = new Intent(getActivity(), WebActivity.class);
//			intent2.putExtra("direction", 3);
//			startActivity(intent2);
//			break;
//		case R.id.layout_game_top_up: // 游戏充值
//			Intent intent3 = new Intent(getActivity(), WebActivity.class);
//			intent3.putExtra("direction", 2);
//			startActivity(intent3);
//			break;
//		case R.id.layout_purse: // 小金库
//			startActivity(new Intent(getActivity(), PurseActivity.class));
//			break;
//		case R.id.layout_jingdou: // 领取京豆
//			Intent intent4 = new Intent(getActivity(), WebActivity.class);
//			intent4.putExtra("direction", 4);
//			startActivity(intent4);
//			break;
//		case R.id.layout_more: // 更多
//			startActivity(new Intent(getActivity(), BoxActivity.class));
//			break;
//		case R.id.layout_recom: // 值得买
//			gotoDetail(7);
//			break;
//		case R.id.layout_recom2: // 精选推荐
//			gotoDetail(8);
//			break;
//		case R.id.layout_recom3: // 闪购
//			gotoDetail(9);
//			break;
//		case R.id.layout_recom4: // 团购
//			gotoDetail(10);
//			break;
//		case R.id.layout_recom5: // 京东众筹
//			gotoDetail(11);
//			break;
		case R.id.img_banner6:
			gotoDetail(12);
			break;
		case R.id.img_banner7:
			gotoDetail(13);
			break;
		case R.id.layout_special: // 小食袋
			gotoDetail(0);
			break;
		case R.id.layout_special2: // 爱生活
			gotoDetail(1);
			break;
		case R.id.layout_special3: // 风尚季
			gotoDetail(2);
			break;
		case R.id.layout_special4: // 血压计
			gotoDetail(3);
			break;
		case R.id.img_banner8:
			gotoDetail(13);
			break;
		case R.id.img_banner9:
			gotoDetail(14);
			break;
		case R.id.home_pages_login_btn: // 马上登陆
			Intent intent5 = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent5);

			break;

		case R.id.home_children:
			String name = childrenTv1.getText().toString();
			if (name != null) {
				gotoWebDetail(1, name);
			}
			
			break;
			
		case R.id.home_children_2:
			String name1 = childrenTv2.getText().toString();
			if (name1 != null) {
				gotoWebDetail(1, name1);
			}
			break;
			
		case R.id.home_old:
			String name2 = oldTv1.getText().toString();
			if (name2 != null) {
				gotoWebDetail(1, name2);
			}
			break;
			
		case R.id.old_man:
			String name3 = oldTv2.getText().toString();
			if (name3 != null) {
				gotoWebDetail(1, name3);
			}
			break;
			
		case R.id.home_go:
			String name4 = travelTv1.getText().toString();
			if (name4 != null) {
				gotoWebDetail(1, name4);
			}
			break;
			
		case R.id.home_go_2:
			String name5 = travelTv2.getText().toString();
			if (name5 != null) {
				gotoWebDetail(1, name5);
			}
			break;
			
		case R.id.home_job:
			String name6 = careerTv1.getText().toString();
			if (name6 != null) {
				gotoWebDetail(1, name6);
			}
			break;
			
		case R.id.home_job_2:
			String name7 = careerTv2.getText().toString();
			if (name7 != null) {
				gotoWebDetail(1, name7);
			}
			break;
		default:
			break;
		}
	}
	
	private void gotoWebDetail(int flag, String name){
		Intent intent = new Intent(getActivity(), InsuranceDetailActivity.class);
		intent.putExtra("flag", flag);
		intent.putExtra("name", name);
		startActivity(intent);
	}

	/**
	 * 转到商品详情
	 */
	private void gotoDetail(int index) {
		Intent intent = new Intent(getActivity(), DetailActivity.class);
		intent.putExtra(Constants.INTENT_KEY.INFO_TO_DETAIL, mInfos[index]);
		startActivity(intent);
	}

	private void gotoSearch() {
		Intent intent = new Intent(getActivity(), SearchActivity.class);
		startActivity(intent);
		// activity开启无动画
		getActivity().overridePendingTransition(0, 0);
	}

	// private class GetDataTask extends AsyncTask<Void, Void, String[]> {
	// protected String[] doInBackground(Void... params) {
	// // 下拉刷新
	// try {
	// Thread.sleep(3000);
	// } catch (InterruptedException e) {
	// }
	// return null;
	// }
	//
	// protected void onPostExecute(String[] result) {
	// mPtrScrollView.onRefreshComplete();// 关闭刷新动画
	// }
	//
	// }

	private void gotoDetail(String id, String url) {
		Intent intent = new Intent(getActivity(), DetailActivity.class);
		intent.putExtra("id", id);
		intent.putExtra("url", url);
		startActivity(intent);
	}

}
