package com.example.market.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.example.market.R;
import com.example.market.activity.InsuranceDetailActivity;
import com.example.market.activity.MainActivity;
import com.example.market.bean.Insurance;
import com.example.market.utils.Constants;
import com.lib.volley.HTTPUtils;
import com.lib.volley.VolleyListener;
import com.nineoldandroids.animation.ObjectAnimator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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


	private String hotGoodsResult, newestGoodsResult, jicaiGoodsResult,
			helpItemResult;

	private ViewPager mPager;
	private int[] mBanner = new int[] { R.drawable.one,
			R.drawable.two, R.drawable.three,
			R.drawable.four };
	private ImageView mImageView;
	private ImageView mImgCover;
	private int mLastPos;// 记录上一次ViewPager的位置
	private boolean isDragging;// 是否被拖拽
	private boolean isFoucusRight; // ScrollView是否滚动到右侧
	private View layout;

	private HorizontalScrollView mScrollView;
	private HorizontalScrollView mScrollView2;

	private DrawerLayout mDrawerLayout;

	private RelativeLayout mainLayout;
	private boolean isLogin;
	private String homepageResult;
	
	private List<Insurance> insurances;
	
	private TextView childrenTv1, childrenTv2, oldTv1, oldTv2, travelTv1, travelTv2, careerTv1, careerTv2;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				
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
		mainLayout = (RelativeLayout) layout.findViewById(R.id.RelativeLayout1);
		mDrawerLayout = (DrawerLayout) layout
				.findViewById(R.id.home_page_drawer);
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

}
