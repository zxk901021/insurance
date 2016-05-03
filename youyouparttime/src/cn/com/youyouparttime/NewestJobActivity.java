package cn.com.youyouparttime;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.com.youyouparttime.adapter.PartTimeInfoAdapter;
import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.Constant;
import cn.com.youyouparttime.entity.PartTimeInfo;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.HttpUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NewestJobActivity extends Activity implements OnClickListener {

	private PullToRefreshListView newestList;
	private LinearLayout back;
	private PartTimeInfoAdapter adapter;
	List<PartTimeInfo> list = new ArrayList<PartTimeInfo>();
	private TextView title;
	private Intent intent;
	private String titleStr;
	private String key;
	private int mode;
	private TextView areaText,deleteText;
	private boolean flag = false;
	private String areaKey;
	private ProgressDialog dialog;
	private int pageCount = 1;
	private boolean scrollEnabled = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newest_job);
		SysApplication.getInstance().addActivity(this);
		initView();
	}

	public void initView() {
		dialog = new ProgressDialog(this);
		title = (TextView) findViewById(R.id.list_page_title);
		areaText = (TextView) findViewById(R.id.job_list_right_btn);
		deleteText = (TextView) findViewById(R.id.job_list_delete);
		intent = getIntent();
		mode = intent.getIntExtra("mode", 0);
		if (mode == Constant.NEWEAST_JOB_MODE || mode == Constant.RECOMMENT_JOB_MODE) {
			areaText.setVisibility(View.VISIBLE);
			deleteText.setVisibility(View.GONE);
		}else {
			areaText.setVisibility(View.GONE);
			deleteText.setVisibility(View.VISIBLE);
		}
		titleStr = intent.getStringExtra("title");
		key = intent.getStringExtra("key");
		title.setText(titleStr);
		list = getList(mode, key, pageCount);
		newestList = (PullToRefreshListView) findViewById(R.id.newest_job_list);
//		newestList.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//			Toast.makeText(NewestJobActivity.this, "这是第"+ position, Toast.LENGTH_SHORT).show();	
//			}
//		});
		back = (LinearLayout) findViewById(R.id.newest_back);
		areaText.setOnClickListener(this);
		deleteText.setOnClickListener(this);
		back.setOnClickListener(this);
		newestList.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label); 
				GetDataTask task = new GetDataTask(key, mode);
				task.execute(1);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				if (scrollEnabled) {
//					Toast.makeText(TestActivity.this, "正在加载。。。", Toast.LENGTH_SHORT).show();
					pageCount++;
					GetDataTask task = new GetDataTask(key, mode);
					task.execute(pageCount);
					Log.e("pageCount", pageCount+ "");
					Log.e("scrollEnabled", "true");
				}else {
					Toast.makeText(NewestJobActivity.this, "已经加载完了", Toast.LENGTH_SHORT).show();
					newestList.onRefreshComplete();
					Log.e("scrollEnabled", "false");
				}
			}
		});
		if (list.size() < 20) {
			newestList.setMode(Mode.PULL_FROM_START);
		}else {
			newestList.setMode(Mode.BOTH);
		}
		adapter = new PartTimeInfoAdapter(list, this);
		newestList.setAdapter(adapter);
		newestList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent detailIntent = new Intent(NewestJobActivity.this,
						PartTimeDetailActivity.class);
				detailIntent.putExtra("id", list.get(position - 1).getId());

				startActivity(detailIntent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.newest_back:
			finish();
			break;
			
		case R.id.job_list_right_btn:
			Intent areaIntent = new Intent(NewestJobActivity.this, ChooseJobListActivity.class);
			areaIntent.putExtra("flag", 0);
			areaIntent.putExtra("tag", 2);
			startActivityForResult(areaIntent, 30);
			break;
			
		case R.id.job_list_delete:
			if (!flag) {
				deleteText.setText("取消");
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setFlag(true);
				}
				adapter.notifyDataSetChanged();
				flag = true;
			}else {
				deleteText.setText("删除");
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setFlag(false);
				}
				adapter.notifyDataSetChanged();
				flag = false;
			}
			
			break;

		default:
			break;
		}
	}

	public List<PartTimeInfo> getData() {
		List<PartTimeInfo> list = new ArrayList<PartTimeInfo>();
		for (int i = 0; i < 10; i++) {
			PartTimeInfo info = new PartTimeInfo();
			info.setJobType("1");
			info.setJobCompany("1");
			info.setJobAddress("2");
			list.add(info);
		}
		return list;
	}

	public List<PartTimeInfo> getList(int mode, String key, int pageCount) {
//		list = new ArrayList<PartTimeInfo>();
		try {
			String result = null;
			if (mode == Constant.NEWEAST_JOB_MODE ) {
				if (pageCount == 1 ) {
					result = HttpUtil.post(UrlUtil.JOB_NEWEAST_URL);
				}else {
					JSONObject object = new JSONObject();
					object.put("page", pageCount);
					result = HttpUtil.postRequst(UrlUtil.JOB_NEWEAST_URL, object);
					Log.e("joblist", new JSONObject(result).getString("joblist").toString());
				}
				
			} else if (mode == Constant.RECOMMENT_JOB_MODE) {
				if (pageCount == 1) {
					String uid = intent.getStringExtra("uid");
					Log.e("uid", uid);
					JSONObject object = new JSONObject();
					object.put("uid", uid);
					result = HttpUtil.postRequst(UrlUtil.JOB_RECOMMEND_URL, object);
				}else {
					String uid = intent.getStringExtra("uid");
					Log.e("uid", uid);
					JSONObject object = new JSONObject();
					object.put("uid", uid);
					object.put("page", pageCount);
					result = HttpUtil.postRequst(UrlUtil.JOB_RECOMMEND_URL, object);
				}
			} else if (mode == Constant.MY_COLLECT_MODE) {
				if (pageCount == 1) {
					String uid = intent.getStringExtra("uid");
					JSONObject object = new JSONObject();
					object.put("uid", uid);
					result = HttpUtil.postRequst(UrlUtil.MY_COLLECT_URL, object);
				}else {
					String uid = intent.getStringExtra("uid");
					JSONObject object = new JSONObject();
					object.put("uid", uid);
					object.put("page", pageCount);
					result = HttpUtil.postRequst(UrlUtil.MY_COLLECT_URL, object);
				}
			} else if (mode == Constant.MY_APPLY_MODE) {
				if (pageCount == 1) {
					String uid = intent.getStringExtra("uid");
					JSONObject object = new JSONObject();
					object.put("uid", uid);
					result = HttpUtil.postRequst(UrlUtil.MY_APPLY_URL, object);
				}else {
					String uid = intent.getStringExtra("uid");
					JSONObject object = new JSONObject();
					object.put("uid", uid);
					object.put("page", pageCount);
					result = HttpUtil.postRequst(UrlUtil.MY_APPLY_URL, object);
				}
			}else if (mode == 5) {
				if (pageCount == 1) {
					JSONObject object = new JSONObject();
					object.put("cityclass", areaKey);
					result = HttpUtil.postRequst(UrlUtil.JOB_NEWEAST_URL, object);
				}else {
					JSONObject object = new JSONObject();
					object.put("cityclass", areaKey);
					object.put("page", pageCount);
					result = HttpUtil.postRequst(UrlUtil.JOB_NEWEAST_URL, object);
				}
			}
			if (TextUtils.isEmpty(new JSONObject(result).getString(key))) {
				return null;
			}else {
				JSONObject jsonObject = new JSONObject(result);
				JSONArray array = jsonObject.getJSONArray(key);
				for (int i = 0; i < array.length(); i++) {
					Log.e("jsonobject", array.getJSONObject(i).toString());
				}
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
					String favId = "";
					try {
						favId = array.getJSONObject(i).getString("fav_id");
					} catch (Exception e) {
						e.printStackTrace();
					}
					
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
					info.setFavId(favId);
					list.add(info);
				}
				return list;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private class GetDataTask extends AsyncTask<Integer, Void, List<PartTimeInfo>> {

		private String key;
		private int mode;
		private int temp;
		
		
		
		public GetDataTask(String key, int mode) {
			super();
			this.key = key;
			this.mode = mode;
		}


		@Override
		protected List<PartTimeInfo> doInBackground(Integer... params) {
			List<PartTimeInfo> result = null;
			temp = params[0];
			Log.e("temp", temp+"");
			try {
				result = getList(mode, key, params[0]);
				if (result == null) {
					Toast.makeText(NewestJobActivity.this, "到底了", Toast.LENGTH_SHORT).show();
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
					list.clear();
//					boolean flag = list2.addAll(result);
					list = getList(mode, key, 1);
					adapter = new PartTimeInfoAdapter(list, NewestJobActivity.this);
					newestList.setAdapter(adapter);
					newestList.onRefreshComplete();
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
				adapter.notifyDataSetChanged();
				newestList.onRefreshComplete();
				Log.e("list2的size", list.size()+"*" + adapter.getCount());
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}

	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 2) {
			String area = data.getStringExtra("value");
			areaKey = data.getStringExtra("key");
			areaText.setText(area);
			list = getList(5, Constant.NEWEAST_JOB_KEY, 1);
			adapter.notifyDataSetChanged();
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
}
