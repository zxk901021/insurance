package cn.com.youyouparttime;

import java.util.ArrayList;
import java.util.List;

import cn.com.youyouparttime.adapter.FragmentAdapter;
import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.fragment.HomePageFragment;
import cn.com.youyouparttime.fragment.PersonCenterFragment;
import cn.com.youyouparttime.fragment.SearchJobFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PartTimeActivity extends FragmentActivity implements OnClickListener{

	private HomePageFragment homePageFragment;
	private PersonCenterFragment personCenterFragment;
	private SearchJobFragment searchJobFragment;
	private ViewPager container;
	private List<Fragment> fragments;
	private LinearLayout homePage;
	private LinearLayout searchJob;
	private LinearLayout personCenter;
	private FragmentAdapter adapter;
	private ImageView home,classify,person;
	private TextView homeText,classifyText,personText;
	private static boolean isExit = false;
	public static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
		super.handleMessage(msg);
		isExit = false;
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_part_time);
		SysApplication.getInstance().addActivity(this);
//		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().penaltyDialog().build());
//		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
		init();
	}

	private void init(){
		homePageFragment = new HomePageFragment();
		personCenterFragment = new PersonCenterFragment();
		searchJobFragment = new SearchJobFragment();
		container = (ViewPager) findViewById(R.id.student_pager);
		homePage = (LinearLayout) findViewById(R.id.back_to_home);
		homePage.setOnClickListener(this);
		searchJob = (LinearLayout) findViewById(R.id.search_job);
		searchJob.setOnClickListener(this);
		personCenter = (LinearLayout) findViewById(R.id.person_center);
		personCenter.setOnClickListener(this);
		home = (ImageView) findViewById(R.id.home_img);
		classify = (ImageView) findViewById(R.id.classify_img);
		person = (ImageView) findViewById(R.id.center_img);
		homeText = (TextView) findViewById(R.id.home_text);
		classifyText = (TextView) findViewById(R.id.classify_text);
		personText = (TextView) findViewById(R.id.center_text);
		fragments = new ArrayList<Fragment>();
		fragments.add(homePageFragment);
		fragments.add(searchJobFragment);
		fragments.add(personCenterFragment);
		adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
		container.setAdapter(adapter);
		container.setCurrentItem(0);
		home.setImageResource(R.drawable.home_selected);
		classify.setImageResource(R.drawable.classify_no_press);
		person.setImageResource(R.drawable.person_center_no_press);
		homeText.setTextColor(Color.parseColor("#fa9600"));
		classifyText.setTextColor(Color.parseColor("#a4a4a4"));
		personText.setTextColor(Color.parseColor("#a4a4a4"));
		container.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					home.setImageResource(R.drawable.home_selected);
					classify.setImageResource(R.drawable.classify_no_press);
					person.setImageResource(R.drawable.person_center_no_press);
					homeText.setTextColor(Color.parseColor("#fa9600"));
					classifyText.setTextColor(Color.parseColor("#a4a4a4"));
					personText.setTextColor(Color.parseColor("#a4a4a4"));
					break;

				case 1:
					home.setImageResource(R.drawable.home_selected_no_press);
					classify.setImageResource(R.drawable.classify);
					person.setImageResource(R.drawable.person_center_no_press);
					homeText.setTextColor(Color.parseColor("#a4a4a4"));
					classifyText.setTextColor(Color.parseColor("#fa9600"));
					personText.setTextColor(Color.parseColor("#a4a4a4"));
					break;
					
				case 2:
					home.setImageResource(R.drawable.home_selected_no_press);
					classify.setImageResource(R.drawable.classify_no_press);
					person.setImageResource(R.drawable.person_center);
					homeText.setTextColor(Color.parseColor("#a4a4a4"));
					classifyText.setTextColor(Color.parseColor("#a4a4a4"));
					personText.setTextColor(Color.parseColor("#fa9600"));
					break;
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}

	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_to_home:
			if (container.getCurrentItem() != 0) {
				container.setCurrentItem(0);
			}
			home.setImageResource(R.drawable.home_selected);
			classify.setImageResource(R.drawable.classify_no_press);
			person.setImageResource(R.drawable.person_center_no_press);
			homeText.setTextColor(Color.parseColor("#fa9600"));
			classifyText.setTextColor(Color.parseColor("#a4a4a4"));
			personText.setTextColor(Color.parseColor("#a4a4a4"));
			break;

		case R.id.search_job:
			if (container.getCurrentItem() != 1) {
				container.setCurrentItem(1);
			}
			home.setImageResource(R.drawable.home_selected_no_press);
			classify.setImageResource(R.drawable.classify);
			person.setImageResource(R.drawable.person_center_no_press);
			homeText.setTextColor(Color.parseColor("#a4a4a4"));
			classifyText.setTextColor(Color.parseColor("#fa9600"));
			personText.setTextColor(Color.parseColor("#a4a4a4"));
			break;
		case R.id.person_center:
			if (container.getCurrentItem() != 2) {
				container.setCurrentItem(2);
			}
			home.setImageResource(R.drawable.home_selected_no_press);
			classify.setImageResource(R.drawable.classify_no_press);
			person.setImageResource(R.drawable.person_center);
			homeText.setTextColor(Color.parseColor("#a4a4a4"));
			classifyText.setTextColor(Color.parseColor("#a4a4a4"));
			personText.setTextColor(Color.parseColor("#fa9600"));
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	 private void exit() {
         if (!isExit) {
                 isExit = true;
                 Toast.makeText(getApplicationContext(), "再按一次退出程序",
                                 Toast.LENGTH_SHORT).show();
                 handler.sendEmptyMessageDelayed(0, 2000);
         } else {
        	 SysApplication.getInstance().exit();
         }
 }

}
