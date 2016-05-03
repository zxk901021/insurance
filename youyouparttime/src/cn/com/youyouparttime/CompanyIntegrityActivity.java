package cn.com.youyouparttime;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.com.youyouparttime.adapter.CompanyEvaluateAdapter;
import cn.com.youyouparttime.adapter.IntegrityAdapter;
import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.CompanyEvaluate;
import cn.com.youyouparttime.entity.IntergrityInfo;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.HttpUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CompanyIntegrityActivity extends Activity implements OnClickListener{

	
	private Button othersToMe;
	private Button meToOther;
	private LinearLayout back;
	private ListView integrityListView,integrityListView2;
	IntegrityAdapter integrityAdapter;
	CompanyEvaluateAdapter evaluateAdapter;
	private List<IntergrityInfo> data1;
	private List<CompanyEvaluate> data2;
	SharedPreferences share;
	private String uid;
	private TextView integrityPercent;
	private int commentSum;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_integrity);
		SysApplication.getInstance().addActivity(this);
		share = getSharedPreferences("companyPrefer", 0);
		uid = share.getString("uid", null);
		initView();
	}

	public void initView(){
		othersToMe = (Button) findViewById(R.id.other_to_me);
		meToOther = (Button) findViewById(R.id.me_to_other);
		back = (LinearLayout) findViewById(R.id.company_integrity_back);
		integrityListView = (ListView) findViewById(R.id.company_integrity_list);
		integrityListView2 = (ListView) findViewById(R.id.company_integrity_list2); 
		integrityPercent = (TextView) findViewById(R.id.company_integrity_);
		data1 = new ArrayList<IntergrityInfo>();
		data1 = getData();
		integrityAdapter = new IntegrityAdapter(data1, this);
		integrityListView.setAdapter(integrityAdapter);
		data2 = getList();
		evaluateAdapter = new CompanyEvaluateAdapter(data2, this);
		integrityListView2.setAdapter(evaluateAdapter);
		integrityListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(CompanyIntegrityActivity.this, IntegrityDetailActivity.class);
				intent.putExtra("cid", data1.get(position).getcId());
				intent.putExtra("jobid", data1.get(position).getJobId());
				startActivity(intent);
			}
		});
		int temp = 0;
		for (int i = 0; i < data1.size(); i++) {
			commentSum += data1.get(i).getIntegritySum();
			if (StrToInt(data1.get(i).getIntergrityGood()) >= StrToInt(data1.get(i).getIntergrityBad())) {
				temp += 1;
			}
		}
		integrityPercent.setText("³ÏÐÅ¶È£º" + (int)(temp/data1.size()*100)+ "%");
		back.setOnClickListener(this);
		othersToMe.setOnClickListener(this);
		meToOther.setOnClickListener(this);
	}

	public int StrToInt(String value) {
		int temp = 0;
		if (value == null) {
			return temp;
		} else {
			temp = Integer.parseInt(value);
			return temp;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.other_to_me:
			othersToMe.setBackgroundColor(Color.parseColor("#fa9600"));
			meToOther.setBackgroundColor(Color.parseColor("#aaaaaa"));
			integrityListView.setVisibility(View.VISIBLE);
			integrityListView2.setVisibility(View.GONE);
			break;

		case R.id.me_to_other:
			othersToMe.setBackgroundColor(Color.parseColor("#aaaaaa"));
			meToOther.setBackgroundColor(Color.parseColor("#fa9600"));
			integrityListView2.setVisibility(View.VISIBLE);
			integrityListView.setVisibility(View.GONE);
			break;
			
		case R.id.company_integrity_back:
			finish();
			break;
		}
	}
	
	public List<IntergrityInfo> getData (){
		JSONObject object = new JSONObject();
		List<IntergrityInfo> list = new ArrayList<IntergrityInfo>();
		try {
			object.put("cid", uid);
			String result = HttpUtil.postRequst(UrlUtil.INTEGRITY_RECORD_URL, object);
			JSONObject resultJson = new JSONObject(result);
			JSONArray array = resultJson.getJSONArray("comm_list");
			
			for (int i = 0; i < array.length(); i++) {
				IntergrityInfo inte = new IntergrityInfo();
				JSONObject temp = array.getJSONObject(i);
				Log.e("temp", temp.toString());
				inte.setJobId(temp.getString("jobid"));
				inte.setcId(temp.getString("cid"));
				inte.setIntergrityName(temp.getString("name"));
				JSONArray jsonArray = temp.getJSONArray("comment_count");
				Log.e("jsonArray", jsonArray.toString());
				for (int j = 0; j < jsonArray.length(); j++) {
					JSONObject temp2 = jsonArray.getJSONObject(j);
					Log.e("temp2", temp2.toString());
					String commentType = temp2.getString("commtype");
					String count = temp2.getString("count");
					Log.e("count", count);
					if (commentType.equals("5")) {
						inte.setIntergrityGood(count);
					}else if (commentType.equals("6")) {
						inte.setIntergrityNormal(count);
					}else if (commentType.equals("7")) {
						inte.setIntergrityBad(count);
					}
						
				}
				list.add(inte);
			}
				return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<CompanyEvaluate> getList(){
		List<CompanyEvaluate> list = new ArrayList<CompanyEvaluate>();
		JSONObject jsonObject = new JSONObject();
		
		try {
			jsonObject.put("cid", uid);
			String result = HttpUtil.postRequst(UrlUtil.COMPANY_TO_STUDENT_URL, jsonObject);
			JSONArray array = new JSONObject(result).getJSONArray("comm_list");
			for (int i = 0; i < array.length(); i++) {
				CompanyEvaluate evaluate = new CompanyEvaluate();
				evaluate.setUser(array.getJSONObject(i).getString("name"));
				evaluate.setEvaluateContent(array.getJSONObject(i).getString("reason_name"));
				evaluate.setEvaluateDetail(array.getJSONObject(i).getString("content"));
				list.add(evaluate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
}
