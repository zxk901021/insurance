package cn.com.youyouparttime;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import cn.com.youyouparttime.adapter.ResumeListAdapter;
import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.Constant;
import cn.com.youyouparttime.entity.ResumeListEntity;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.HttpUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ResumeListActivity extends Activity implements OnClickListener{

	
	private LinearLayout back;
	private TextView btn;
	private TextView title;
	private PullToRefreshListView list;
	private String type,area,time,card,minHeight,maxHeight,school;
	private ResumeListAdapter adapter;
	List<ResumeListEntity> data = new ArrayList<ResumeListEntity>();;
	int count = 0;
	private boolean isResume;
	private String jobid,cid,uid;
	private boolean flag;
	private SharedPreferences share;
	private int pageCount = 1;
	private boolean scrollEnabled = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resume_list);
		SysApplication.getInstance().addActivity(this);
		share = getSharedPreferences(Constant.COMPANY_PREFER, 0);
		uid = share.getString("uid", null);
		Intent intent = getIntent();
		isResume = intent.getBooleanExtra("isResume", false);
		if (isResume) {
			jobid = intent.getStringExtra("jobid");
			cid = intent.getStringExtra("uid");
		}
		type = intent.getStringExtra("jobclass");
		area = intent.getStringExtra("cityclass");
		time = intent.getStringExtra("jobtime");
		card = intent.getStringExtra("jk_card");
		minHeight = intent.getStringExtra("min-height");
		maxHeight = intent.getStringExtra("max-height");
		school = intent.getStringExtra("school");
		initView();
	}
	public void initView(){
		title = (TextView) findViewById(R.id.resume_list_title);
		back = (LinearLayout) findViewById(R.id.resume_list_back);
		btn = (TextView) findViewById(R.id.resume_list_text);
		list = (PullToRefreshListView) findViewById(R.id.resume_list_list);
		list.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label); 
				new GetDataTask().execute(1);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				if (scrollEnabled) {
//					Toast.makeText(TestActivity.this, "正在加载。。。", Toast.LENGTH_SHORT).show();
					pageCount++;
					new GetDataTask().execute(pageCount);
					Log.e("pageCount", pageCount+ "");
					Log.e("scrollEnabled", "true");
				}else {
					Toast.makeText(ResumeListActivity.this, "已经加载完了", Toast.LENGTH_SHORT).show();
					list.onRefreshComplete();
					Log.e("scrollEnabled", "false");
				}
			}
		});
		
		if (isResume) {
			data = getData2(1);
			title.setText("收到简历");
			btn.setVisibility(View.INVISIBLE);
		}else {
			data = getData(1);
		}
		if (data == null) {
			data = new ArrayList<ResumeListEntity>();
		}
		if (data.size()< 50) {
			list.setMode(Mode.PULL_FROM_START);
		}else {
			list.setMode(Mode.BOTH);
		}
//		adapter = new ResumeListAdapter(data, this, flag);
		adapter = new ResumeListAdapter(data, this, flag, getPhotoUrl());
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (count == 0) {
					Intent resumeDetail = new Intent(ResumeListActivity.this, CheckResumeActivity.class);
					resumeDetail.putExtra("uid", data.get(position -1).getUid());
					resumeDetail.putExtra("sqid", uid);
					resumeDetail.putExtra("jobid", data.get(position - 1).getJobid());
					resumeDetail.putExtra("cid", data.get(position - 1).getCid());
					resumeDetail.putExtra("name", data.get(position - 1).getName());
					startActivity(resumeDetail);
				}else {
					if (data.get(position - 1).isSelected()) {
						data.get(position - 1).setSelected(false);
					}else {
						data.get(position - 1).setSelected(true);
					}
					adapter.notifyDataSetChanged();
				}
			}
		});
		list.setOnScrollListener(mScrollListener);
		back.setOnClickListener(this);
		btn.setOnClickListener(this);
	}
	
	OnScrollListener mScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_FLING:
				adapter.setFlagBusy(true);
				break;
			case OnScrollListener.SCROLL_STATE_IDLE:
				adapter.setFlagBusy(false);
				break;
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				adapter.setFlagBusy(false);
				break;
			default:
				break;
			}
			adapter.notifyDataSetChanged();
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.resume_list_back:
			finish();
			break;

		case R.id.resume_list_text:
			
			if (count == 0) {
				flag = true;
//				adapter = new ResumeListAdapter(data, this, flag);
				adapter = new ResumeListAdapter(data, this, flag, getPhotoUrl());
				list.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				count++;
				btn.setText("下一步");
			}else {
				ArrayList<String> uidList = new ArrayList<String>();
				for (int i = 0; i < data.size(); i++) {
					if (data.get(i).isSelected()) {
						uidList.add(data.get(i).getUid());
					}
				}
				Intent intent = new Intent(ResumeListActivity.this, InviteInterviewActivity.class);
				intent.putStringArrayListExtra("uids", uidList);
				startActivity(intent);
			}
			
			
			break;
		}
	}
	
	public String[] getPhotoUrl (){
		int size = data.size();
		String[] imgUrl = new String[size];
		for (int i = 0; i < size; i++) {
			imgUrl[i] = data.get(i).getPhotoPath();
		}
		return imgUrl;
	}
	
	public List<ResumeListEntity> getData2(int pageCount){
//		List<ResumeListEntity> list = new ArrayList<ResumeListEntity>();
		JSONObject object = new JSONObject();
		try {
			object.put("jobid", jobid);
			object.put("cid", cid);
			String result = null;
			if (pageCount == 1) {
				result = HttpUtil.postRequst(UrlUtil.CHECK_RESUME_URL, object);
			}else {
				object.put("page", pageCount);
				result = HttpUtil.postRequst(UrlUtil.CHECK_RESUME_URL, object);
			}
			if (TextUtils.isEmpty(new JSONObject(result).getString("resume_list"))) {
				return null;
			}else {
				JSONArray array = new JSONObject(result).getJSONArray("resume_list");
				for (int i = 0; i < array.length(); i++) {
					ResumeListEntity entity = new ResumeListEntity();
					entity.setSelected(false);
					entity.setPhotoPath(UrlUtil.SITEURL + array.getJSONObject(i).getString("photo"));
					entity.setName(array.getJSONObject(i).getString("name"));
					entity.setJobType(array.getJSONObject(i).getString("job_class_list"));
					entity.setSchool(array.getJSONObject(i).getString("school"));
					entity.setFoucusCounts(array.getJSONObject(i).getString("hits"));
					entity.setUid(array.getJSONObject(i).getString("uid"));
					entity.setJobid(array.getJSONObject(i).getString("jobid"));
					entity.setCid(array.getJSONObject(i).getString("cid"));
					data.add(entity);
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return data;
	}
	
	
	
	public List<ResumeListEntity> getData(int pageCount){
//		List<ResumeListEntity> list = new ArrayList<ResumeListEntity>();
		JSONObject object = new JSONObject();
		try {
			object.put("jobclass", type);
			object.put("cityclass", area);
			object.put("jobtime", time);
			object.put("min-height", minHeight);
			object.put("max-height", maxHeight);
			object.put("school", school);
			object.put("jk_card", card);
//			object.put("page", 254);
			Log.e("info", object.toString());
			String result = null;
//			result = HttpUtil.postRequst(UrlUtil.SEARCH_JOB_URL, object);
			if (pageCount == 1) {
				result = HttpUtil.postRequst(UrlUtil.SEARCH_JOB_URL, object);
			}else {
				object.put("page", pageCount);
				result = HttpUtil.postRequst(UrlUtil.SEARCH_JOB_URL, object);
			}
			if (TextUtils.isEmpty(new JSONObject(result).getString("resume_list"))) {
				return null;
			}else {
				JSONArray array = new JSONObject(result).getJSONArray("resume_list");
				for (int i = 0; i < array.length(); i++) {
					ResumeListEntity entity = new ResumeListEntity();
					entity.setPhotoPath(UrlUtil.SITEURL + array.getJSONObject(i).getString("photo"));
					entity.setSelected(false);
					entity.setName(array.getJSONObject(i).getString("name"));
					entity.setJobType(array.getJSONObject(i).getString("job_class_list"));
					entity.setSchool(array.getJSONObject(i).getString("school"));
					entity.setFoucusCounts(array.getJSONObject(i).getString("hits"));
					entity.setUid(array.getJSONObject(i).getString("uid"));
					data.add(entity);
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return data;
	}
	
	
	private class GetDataTask extends AsyncTask<Integer, Void, List<ResumeListEntity>> {

		private int temp;
		
		@Override
		protected List<ResumeListEntity> doInBackground(Integer... params) {
			List<ResumeListEntity> result = null;
			temp = params[0];
			Log.e("temp", temp+"");
			try {
				if (isResume) {
					result = getData2(params[0]);
				}else {
					result = getData(params[0]);
				}
				
				if (result == null) {
					Toast.makeText(ResumeListActivity.this, "到底了", Toast.LENGTH_SHORT).show();
				}else {
					Log.e("result.size()", result.size()+"");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		
		
		@Override
		protected void onPostExecute(List<ResumeListEntity> result) {
			try {
				if (temp == 1) {
					data.clear();
//					boolean flag = list2.addAll(result);
					if (isResume) {
						data = getData2(1);
					}else {
						data = getData(1);
					}
					adapter = new ResumeListAdapter(data, ResumeListActivity.this, flag, getPhotoUrl());
					list.setAdapter(adapter);
					list.onRefreshComplete();
					Log.e("111", "111" );
					pageCount = 1;
					scrollEnabled = true;
					return;
				}else if (temp != 1 && result.size()> 0 && result.size()< 50*pageCount) {
//					list2.addAll(result);
					scrollEnabled = false;
					Log.e("222", "222");
				}else if (temp != 1 && result.size()== 50*pageCount) {
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
				list.onRefreshComplete();
				Log.e("list2的size", data.size()+"*" + adapter.getCount());
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}

	}
}
