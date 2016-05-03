package cn.com.youyouparttime.fragment;

import java.util.concurrent.ScheduledExecutorService;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;

import cn.com.youyouparttime.HtmlResumeActivity;
import cn.com.youyouparttime.MyResumeActivity;
import cn.com.youyouparttime.NewestJobActivity;
import cn.com.youyouparttime.R;
import cn.com.youyouparttime.SearchActivity;
import cn.com.youyouparttime.adapter.ViewPagerAdapter;
import cn.com.youyouparttime.entity.Constant;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.BitmapCache;
import cn.com.youyouparttime.util.CommonUtil;
import cn.com.youyouparttime.util.HttpUtil;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class HomePageFragment extends Fragment implements OnClickListener,
		OnPageChangeListener {

	private ViewPager pager;
	private RelativeLayout newest;
	private RelativeLayout recommend;
	private RelativeLayout resume;
	private ImageView search;
	private LinearLayout tipsLayout;
	private int isLogin;
	private ImageView[] mImageView;
	private ImageView[] tips;
//	private int[] imageID;
	private ViewPagerAdapter pagerAdapter;
	private boolean isContinue = true;
	private int position;
	private ScheduledExecutorService executorService;
	SharedPreferences preferences;
	private String uid;
	private boolean hasSubmitResume;
	private int adCount;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_home_page, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		try {
			mImageView = getImageUrl();
		} catch (Exception e) {
			ImageView view = new ImageView(getActivity());
			view.setImageResource(R.drawable.defalut_ad);
			mImageView = new ImageView[]{view};
			e.printStackTrace();
		}
		preferences = getActivity().getSharedPreferences(Constant.STUDENT_PREFER, 0);
		uid = preferences.getString("uid", null);
		hasSubmitResume = preferences.getBoolean("hasSubmit", false);
		isLogin = CommonUtil.isLogin(getActivity());
//		imageID = new int[] { R.drawable.pttravel_help,
//				R.drawable.pttravel_release_img, R.drawable.banner,
//				R.drawable.headbg };
		tips = new ImageView[adCount];
		for (int i = 0; i < tips.length; i++) {
			ImageView imageView = new ImageView(getActivity());
			imageView.setLayoutParams(new LayoutParams(10, 10));
			tips[i] = imageView;
			if (i == 0) {
				tips[i].setBackgroundResource(R.drawable.doc_show);
			} else {
				tips[i].setBackgroundResource(R.drawable.doc_unshow);
			}

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 5;
			layoutParams.rightMargin = 5;
			tipsLayout.addView(imageView, layoutParams);
		}
		// mImageView = new ImageView[imageID.length];
		// for(int i=0; i<mImageView.length; i++){
		// ImageView imageView = new ImageView(getActivity());
		// mImageView[i] = imageView;
		// imageView.setBackgroundResource(imageID[i]);
		// }
		pagerAdapter = new ViewPagerAdapter(mImageView);
		pager.setAdapter(pagerAdapter);
		pager.setOnPageChangeListener(this);
		pager.setCurrentItem((mImageView.length) * 100);
	}

	public void initView() {
		pager = (ViewPager) getActivity().findViewById(R.id.ad_pager);
		newest = (RelativeLayout) getActivity().findViewById(R.id.newest_job);
		recommend = (RelativeLayout) getActivity().findViewById(R.id.recommend);
		resume = (RelativeLayout) getActivity().findViewById(R.id.resume);
		tipsLayout = (LinearLayout) getActivity()
				.findViewById(R.id.tips_layout);
		search = (ImageView) getActivity().findViewById(R.id.home_search);
		search.setOnClickListener(this);
		newest.setOnClickListener(this);
		recommend.setOnClickListener(this);
		resume.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.newest_job:

			Intent a = new Intent(getActivity(), NewestJobActivity.class);
			a.putExtra("title", "最新兼职");
			a.putExtra("mode", Constant.NEWEAST_JOB_MODE);
			a.putExtra("key", Constant.NEWEAST_JOB_KEY);
			startActivity(a);
			break;

		case R.id.recommend:
			if (isLogin == 0) {
				Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!hasSubmitResume) {
				Toast.makeText(getActivity(), "建议您去完善简历中的求职意向，以便为您推荐更适合的兼职岗位", Toast.LENGTH_LONG).show();
			}
			Intent b = new Intent(getActivity(), NewestJobActivity.class);
			b.putExtra("title", "为你推荐");
			b.putExtra("uid", uid);
			b.putExtra("mode", Constant.RECOMMENT_JOB_MODE);
			b.putExtra("key", Constant.NEWEAST_JOB_KEY);
			startActivity(b);
			break;
		case R.id.resume:
			if (isLogin == 0) {
				Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
				return;
			}
			if (hasSubmitResume) {
				Intent htmlIntent = new Intent(getActivity(),
						HtmlResumeActivity.class);
				htmlIntent.putExtra("uid", uid);
				htmlIntent.putExtra("sqid", uid);
				startActivity(htmlIntent);
				return;
			} else {
				Intent intent = new Intent(getActivity(),
						MyResumeActivity.class);
				startActivity(intent);
			}

			break;

		case R.id.home_search:
			Intent searchIntent = new Intent(getActivity(),
					SearchActivity.class);
			startActivity(searchIntent);
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		setImageBackground(arg0 % mImageView.length);
	}

	private void setImageBackground(int selectItems) {
		for (int i = 0; i < tips.length; i++) {
			if (i == selectItems) {
				tips[i].setBackgroundResource(R.drawable.doc_show);
			} else {
				tips[i].setBackgroundResource(R.drawable.doc_unshow);
			}
		}
	}

	public ImageView[] getImageUrl() {
		ImageView[] view = null;
		try {
			String result = HttpUtil.post(UrlUtil.ADURL);
			JSONObject object = new JSONObject(result);
			JSONArray array = object.getJSONArray("adlist");
			adCount = array.length();
			view = new ImageView[adCount];
			String[] imageUrl = new String[adCount];
			RequestQueue queue = Volley.newRequestQueue(getActivity());
			ImageLoader imageLoader = new ImageLoader(queue, new BitmapCache());

			for (int i = 0; i < array.length(); i++) {
				final ImageView imageView = new ImageView(getActivity());
				imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				view[i] = imageView;
				imageView.setScaleType(ScaleType.FIT_XY);
				imageUrl[i] = array.getJSONObject(i).getString("pic");
				Log.e("url", imageUrl[i]);
				ImageListener imageListener = ImageLoader.getImageListener(
						imageView, R.drawable.defalut_ad, R.drawable.defalut_ad);
				imageLoader.get(imageUrl[i], imageListener);
				// ImageRequest imageRequest = new ImageRequest(imageUrl[i],
				// new Response.Listener<Bitmap>() {
				//
				// @Override
				// public void onResponse(Bitmap response) {
				//
				// imageView.setImageBitmap(response);
				// }
				// }, 0, 0, Config.RGB_565,
				// new Response.ErrorListener() {
				//
				// @Override
				// public void onErrorResponse(VolleyError error) {
				// imageView.setImageResource(R.drawable.banner);
				// }
				// });
				// queue.add(imageRequest);
			}

			return view;
		} catch (Exception e) {
			
			for (int j = 0; j < view.length; j++) {
				view[j].setImageResource(R.drawable.defalut_ad);

			}
			e.printStackTrace();
			return view;
		}
	}


	// @Override
	// public void run() {
	// while (true) {
	// if (isContinue) {
	// position = (position + 1) % mImageView.length;
	// viewHandler.sendEmptyMessage(position);
	//
	// try {
	// Thread.sleep(1000);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// }

	// viewHandler = new Handler(){
	// public void handleMessage(android.os.Message msg) {
	//
	// pager.setCurrentItem(msg.what);
	// super.handleMessage(msg);
	// }
	// };

	// public void onStart() {
	// super.onStart();
	// executorService = Executors.newSingleThreadScheduledExecutor();
	// executorService.scheduleWithFixedDelay(new ViewPagerTask(), 2, 2,
	// TimeUnit.SECONDS);
	// }
	//
	// private Handler viewHandler = new Handler(){
	//
	// public void handleMessage(android.os.Message msg) {
	// pager.setCurrentItem(position);
	// }
	//
	// };
	//
	// private class ViewPagerTask implements Runnable{
	//
	// @Override
	// public void run() {
	// position = (position + 1) % mImageView.length;
	// viewHandler.obtainMessage().sendToTarget();
	// }
	// }

}
