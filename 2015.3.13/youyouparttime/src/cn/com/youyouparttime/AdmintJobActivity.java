package cn.com.youyouparttime;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import cn.com.youyouparttime.adapter.AdminJobAdapter;
import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.CompanyResume;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class AdmintJobActivity extends Activity implements OnClickListener{

	private LinearLayout back;
	private PullToRefreshListView list;
	private AdminJobAdapter adapter;
	List<CompanyResume> data = new ArrayList<CompanyResume>();
	private SharedPreferences share;
	private String uid;
	private int pageCount = 1;
	private boolean scrollEnabled = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admint_job);
		SysApplication.getInstance().addActivity(this);
		share = getSharedPreferences("companyPrefer", 0);
		uid = share.getString("uid", null);
		initView();
	}

	public void initView(){
		back =(LinearLayout) findViewById(R.id.admin_job_back);
		list = (PullToRefreshListView) findViewById(R.id.admin_job_list);
		back.setOnClickListener(this);
		data = getData(1);
		if (data == null) {
			data = new ArrayList<CompanyResume>();
		}
		adapter = new AdminJobAdapter(data, this);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(AdmintJobActivity.this, PartTimeDetailActivity.class);
				intent.putExtra("id", data.get(position - 1).getJobId());
				intent.putExtra("uid", uid);
				intent.putExtra("iscompany", true);
				startActivity(intent);
			}
		});
		
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
					Toast.makeText(AdmintJobActivity.this, "已经加载完了", Toast.LENGTH_SHORT).show();
					list.onRefreshComplete();
					Log.e("scrollEnabled", "false");
				}
			}
		});
		if (data == null) {
			data = new ArrayList<CompanyResume>();
		}
		if (data.size()< 20) {
			list.setMode(Mode.PULL_FROM_START);
		}else {
			list.setMode(Mode.BOTH);
		}
	}

	
	public List<CompanyResume> getData(int pageCount){
//		List<CompanyResume> comList = new ArrayList<CompanyResume>();
		JSONObject object = new JSONObject();
		try {
			object.put("uid", uid);
			String result = null;
			if (pageCount == 1) {
				result = HttpUtil.postRequst(UrlUtil.ADMIN_JOB_URL, object);
			}else {
				object.put("page", pageCount);
				result = HttpUtil.postRequst(UrlUtil.ADMIN_JOB_URL, object);
			}
			if (TextUtils.isEmpty(new JSONObject(result).getString("admin_resume"))) {
				return null;
			}else {
				JSONArray array = new JSONObject(result).getJSONArray("admin_resume");
				for (int i = 0; i < array.length(); i++) {
					CompanyResume resume = new CompanyResume();
					resume.setJobType(array.getJSONObject(i).getString("jobclass"));
					resume.setJobTitle(array.getJSONObject(i).getString("name"));
					resume.setJobTime(array.getJSONObject(i).getString("sdate"));
					resume.setJobStatus(array.getJSONObject(i).getString("show_status"));
					resume.setJobResumeCounts(array.getJSONObject(i).getString("resume_num"));
					resume.setJobId(array.getJSONObject(i).getString("jobid"));
					data.add(resume);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return data;
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.admin_job_back:
			finish();
			break;

		default:
			break;
		}
	}
	
	private class GetDataTask extends AsyncTask<Integer, Void, List<CompanyResume>> {

		private int temp;
		
		@Override
		protected List<CompanyResume> doInBackground(Integer... params) {
			List<CompanyResume> result = null;
			temp = params[0];
			Log.e("temp", temp+"");
			try {
				result = getData(params[0]);
				
				if (result == null) {
					Toast.makeText(AdmintJobActivity.this, "到底了", Toast.LENGTH_SHORT).show();
				}else {
					Log.e("result.size()", result.size()+"");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		
		
		@Override
		protected void onPostExecute(List<CompanyResume> result) {
			try {
				if (temp == 1) {
					data.clear();
//					boolean flag = list2.addAll(result);
					data = getData(1);
					adapter = new AdminJobAdapter(data, AdmintJobActivity.this);
					list.setAdapter(adapter);
					list.onRefreshComplete();
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
				list.onRefreshComplete();
				Log.e("list2的size", data.size()+"*" + adapter.getCount());
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}

	}
}
