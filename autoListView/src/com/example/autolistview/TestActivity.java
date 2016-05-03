package com.example.autolistview;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.autolistview.adapter.PartTimeInfo;
import com.example.autolistview.adapter.PartTimeInfoAdapter;
//import com.example.autolistview.adapter.PartTimeInfoAdapter.ScrollToLastCallBack;
import com.example.autolistview.utils.Constant;
import com.example.autolistview.utils.HttpUtil;
import com.example.autolistview.utils.UrlUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


public class TestActivity extends Activity{

	PullToRefreshListView mListView ;
	PartTimeInfoAdapter adapter2;
	List<PartTimeInfo> list2 = new ArrayList<PartTimeInfo>();
	private int pageCount = 1;
	private Button btn;
	private boolean scrollEnabled = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		mListView = (PullToRefreshListView) findViewById(R.id.refreshlv);
		
		
//		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
//
//			@Override
//			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(), 
//						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label); 
//				new GetDataTask().execute(1);
//			}
//		});
		
		mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label); 
				new GetDataTask().execute(1);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				if (scrollEnabled) {
//					Toast.makeText(TestActivity.this, "正在加载。。。", Toast.LENGTH_SHORT).show();
					pageCount++;
					new GetDataTask().execute(pageCount);
					Log.e("pageCount", pageCount+ "");
					Log.e("scrollEnabled", "true");
				}else {
					Toast.makeText(TestActivity.this, "已经加载完了", Toast.LENGTH_SHORT).show();
					mListView.onRefreshComplete();
					Log.e("scrollEnabled", "false");
				}
			}
		});
		
		
//		mListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
//
//			@Override
//			public void onLastItemVisible() {
//				Toast.makeText(TestActivity.this, "加载中。。。", Toast.LENGTH_SHORT).show();
//			}
//		});
		
		
//		mListView.setMode(Mode.PULL_FROM_START);
		mListView.setMode(Mode.BOTH);
//		mListView.setMode(Mode.PULL_FROM_END);
		list2 = getList(Constant.NEWEAST_JOB_MODE, Constant.NEWEAST_JOB_KEY, pageCount);
		adapter2 = new PartTimeInfoAdapter(list2, TestActivity.this
//				, 
//				new ScrollToLastCallBack() {
//			
//			@Override
//			public void onScrollToLast(Integer pos) {
//				Log.e("onScrollToLast", "onScrollToLast");
//				if (scrollEnabled) {
//					Toast.makeText(TestActivity.this, "正在加载。。。", Toast.LENGTH_SHORT).show();
//					pageCount++;
//					new GetDataTask().execute(pageCount);
//					Log.e("pageCount", pageCount+ "");
//				}else {
//					Toast.makeText(TestActivity.this, "已经加载完了", Toast.LENGTH_SHORT).show();
//				}
//				
//			}
//		}, 
//		actualListView
		);
		mListView.setAdapter(adapter2);
		btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			String result = null;
			JSONObject object = new JSONObject();
			try {
				object.put("page", pageCount);
				result = HttpUtil.postRequst(UrlUtil.JOB_NEWEAST_URL, object);
				Log.e("result", result);
				String a = new JSONObject(result).getString("joblist").toString();
				boolean flag = a.equals("");
				Log.e("joblist", new JSONObject(result).getString("joblist").toString()+"love" + flag);
//				JSONObject josn = new JSONObject(result);
//				JSONArray array = josn.getJSONArray("joblist");
//				
//				Log.e("@@@@@", josn.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			pageCount++;
			}
		});
	}

	public List<PartTimeInfo> getList(int mode, String key, int pageCount) {
		try {
			String result = null;
			if (mode == Constant.NEWEAST_JOB_MODE && pageCount == 1) {
				result = HttpUtil.post(UrlUtil.JOB_NEWEAST_URL);
			} 
			else {
				JSONObject object = new JSONObject();
				object.put("page", pageCount);
				result = HttpUtil.postRequst(UrlUtil.JOB_NEWEAST_URL, object);
				Log.e("joblist", new JSONObject(result).getString("joblist").toString());
			}
			if (TextUtils.isEmpty(new JSONObject(result).getString("joblist"))) {
				return null;
			}else {
				JSONObject jsonObject = new JSONObject(result);
				JSONArray array = jsonObject.getJSONArray(key);
				for (int i = 0; i < array.length(); i++) {
					PartTimeInfo info = new PartTimeInfo();
					String name = array.getJSONObject(i).getString("name");
					String type = array.getJSONObject(i).getString("jobclass");
					String address = array.getJSONObject(i).getString("city_name");
					String time = array.getJSONObject(i).getString("sdate");
					String account = array.getJSONObject(i).getString("jobhits");
					String pay = array.getJSONObject(i).getString("salary");
					String id = array.getJSONObject(i).getString("jobid");
					String vip = array.getJSONObject(i).getString("vip");
					String hot = array.getJSONObject(i).getString("hotest");
//					String favId = "";
//					try {
//						favId = array.getJSONObject(i).getString("fav_id");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
					
					if (vip.equals("0")) {
						info.setVIP(false);
					} else {
						info.setVIP(true);
					}
					if (hot.equals("0")) {
						info.setHot(false);
					} else {
						info.setHot(true);
					}
					info.setJobName(name);
					info.setJobType(type);
					info.setJobAddress(address);
					info.setJobReleaseTime(time);
					info.setPeopleCount(account);
					info.setJobReward(pay);
					info.setId(id);
//					info.setFavId(favId);
					list2.add(info);
				}
				return list2;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list2;
	}

	
	private class GetDataTask extends AsyncTask<Integer, Void, List<PartTimeInfo>> {

		private int temp;
		
		@Override
		protected List<PartTimeInfo> doInBackground(Integer... params) {
			List<PartTimeInfo> result = null;
			temp = params[0];
			Log.e("temp", temp+"");
			try {
				result = getList(Constant.NEWEAST_JOB_MODE, Constant.NEWEAST_JOB_KEY, params[0]);
				if (result == null) {
					Toast.makeText(TestActivity.this, "到底了", Toast.LENGTH_SHORT).show();
				}else {
					Log.e("result.size()", result.size()+"");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		
		
		@Override
		protected void onPostExecute(List<PartTimeInfo> result) {
			try {
				if (temp == 1) {
					list2.clear();
//					boolean flag = list2.addAll(result);
					list2 = getList(Constant.NEWEAST_JOB_MODE, Constant.NEWEAST_JOB_KEY, 1);
					adapter2 = new PartTimeInfoAdapter(list2, TestActivity.this);
					mListView.setAdapter(adapter2);
					mListView.onRefreshComplete();
					Log.e("111", "111" );
					pageCount = 1;
					scrollEnabled = true;
					return;
				}else if (temp != 1 && result.size()> 0 && result.size()< 20*pageCount) {
//					list2.addAll(result);
					scrollEnabled = false;
					Log.e("222", "222");
				}else if (temp != 1 && result.size()== 20*pageCount) {
//					list2.addAll(result);
					scrollEnabled = true;
					Log.e("333", "333");
				}else if (result == null) {
					scrollEnabled = false;
					Log.e("444", "444");
				}
//				else {
//					Toast.makeText(TestActivity.this, "已加载到底部", Toast.LENGTH_SHORT).show();
//					return;
//				}
				
//				for (int i = 0; i < list2.size(); i++) {
//					Log.e("list2", i+"****"+list2.get(i).getJobName());
//				}
				adapter2.notifyDataSetChanged();
				mListView.onRefreshComplete();
				Log.e("list2的size", list2.size()+"*" + adapter2.getCount());
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}

	}



}
