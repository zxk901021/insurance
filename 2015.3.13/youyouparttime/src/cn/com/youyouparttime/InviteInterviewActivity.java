package cn.com.youyouparttime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.com.youyouparttime.adapter.CompanyJobListAdapter;
import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.Constant;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.HttpUtil;
import cn.com.youyouparttime.util.Utility;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class InviteInterviewActivity extends Activity implements OnClickListener{

	private TextView back;
	private ListView list;
	private TextView addJob;
	private Button ensure, cancel;
	private CompanyJobListAdapter adapter;
	private List<Map<String, String>> data;
	private List<String> uids;
	private SharedPreferences share;
	private String uid;
	String usersId,jobsId;
	StringBuffer buffer = new StringBuffer();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite_interview);
		SysApplication.getInstance().addActivity(this);
		share = getSharedPreferences(Constant.COMPANY_PREFER, 0);
		uid = share.getString("uid", "");
		Intent intent = getIntent();
		uids = intent.getStringArrayListExtra("uids");
		initView();
		
	}

	public void initView(){
		back =(TextView) findViewById(R.id.invite_interview_back);
		list = (ListView) findViewById(R.id.invite_interview_list);
		addJob = (TextView) findViewById(R.id.add_new_job);
		addJob.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		ensure = (Button) findViewById(R.id.invite_ensure);
		cancel = (Button) findViewById(R.id.invite_cancel);
		addJob.setOnClickListener(this);
		ensure.setOnClickListener(this);
		cancel.setOnClickListener(this);
		back.setOnClickListener(this);
		data = new ArrayList<Map<String,String>>();
		data = getData();
		adapter = new CompanyJobListAdapter(data, this);
		list.setAdapter(adapter);
		Utility.setListViewHeightBasedOnChildren(list);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (Boolean.parseBoolean(data.get(position).get("check"))) {
					data.get(position).put("check", "false");
				}else {
					data.get(position).put("check", "true");
					buffer.append(data.get(position).get("jobid")+",");
					jobsId = buffer.toString().substring(0, buffer.length() -1);
					Log.e("jobsid", jobsId);
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

	public List<Map<String, String>> getData(){
		JSONObject object = new JSONObject();
		List<Map<String, String>> info = new ArrayList<Map<String,String>>();
		
		try {
			object.put("uid", uid);
			String result = HttpUtil.postRequst(UrlUtil.ADMIN_JOB_URL, object);	
			Log.e("result", new JSONObject(result).toString());
			JSONArray array = new JSONObject(result).getJSONArray("admin_resume");
			for (int i = 0; i < array.length(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("check", "false");
				map.put("title", array.getJSONObject(i).getString("name"));
				map.put("jobid", array.getJSONObject(i).getString("jobid"));
				info.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return info;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.invite_interview_back:
			finish();
			break;

		case R.id.add_new_job:
			Intent addIntent = new Intent(InviteInterviewActivity.this, ReleaseNewJobActivity.class);
			startActivity(addIntent);
			break;
			
		case R.id.invite_ensure:
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < uids.size(); i++) {
				buffer.append(uids.get(i)+ ",");
				usersId = buffer.toString().substring(0, buffer.length() - 1);
			}
			JSONObject object = new JSONObject();
			try {
				object.put("uid", usersId);
				object.put("jobid", jobsId);
				object.put("cid", uid);
				Log.e("jobid", object.toString());
				String result = HttpUtil.postRequst(UrlUtil.COMPANY_INVIITE_URL, object);
				String msg = new JSONObject(result).getString("msg");
				Toast.makeText(InviteInterviewActivity.this, msg, Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case R.id.invite_cancel:
			finish();
			break;
		}
	}
	
}
