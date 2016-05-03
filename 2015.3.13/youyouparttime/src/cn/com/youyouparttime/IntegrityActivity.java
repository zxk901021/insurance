package cn.com.youyouparttime;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.com.youyouparttime.adapter.IntegrityAdapter;
import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.IntergrityInfo;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.HttpUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class IntegrityActivity extends Activity implements OnClickListener {

	String cid;
	private TextView integrityCount;
	private LinearLayout integrityBack;
	private ListView integrityList;
	private IntegrityAdapter adapter;
	List<IntergrityInfo> info = new ArrayList<IntergrityInfo>();
	private int commentSum;
	private TextView integrityPercent;
	private int percent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integrity);
		SysApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		cid = intent.getStringExtra("cid");
		info = getList();
		initView();

	}

	public void initView() {
		integrityCount = (TextView) findViewById(R.id.integrity_count);
		integrityPercent = (TextView) findViewById(R.id.integrity_percent);
		integrityBack = (LinearLayout) findViewById(R.id.integrity_back);
		integrityBack.setOnClickListener(this);
		integrityList = (ListView) findViewById(R.id.integrity_list);
		adapter = new IntegrityAdapter(info, this);
		integrityList.setAdapter(adapter);
		integrityList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(IntegrityActivity.this,
						IntegrityDetailActivity.class);
				intent.putExtra("cid", info.get(position).getcId());
				intent.putExtra("jobid", info.get(position).getJobId());
				startActivity(intent);
			}
		});
		int temp = 0;
		for (int i = 0; i < info.size(); i++) {
			commentSum += info.get(i).getIntegritySum();
			if (StrToInt(info.get(i).getIntergrityGood()) >= StrToInt(info.get(i).getIntergrityBad())) {
				temp += 1;
			}
		}
		integrityCount.setText("累计评价（" + commentSum + "）");
		int size = info.size();
		if (size == 0) {
			integrityPercent.setText("诚信度：0%");
		}else {
			integrityPercent.setText("诚信度：" + (int)(temp/info.size()*100)+ "%");
		}
		
		
	}

	public List<IntergrityInfo> getList() {
		JSONObject object = new JSONObject();

		try {
			object.put("cid", cid);
			String result = HttpUtil.postRequst(UrlUtil.INTEGRITY_RECORD_URL,
					object);
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
					} else if (commentType.equals("6")) {
						inte.setIntergrityNormal(count);
					} else if (commentType.equals("7")) {
						inte.setIntergrityBad(count);
					}
					inte.setIntegritySum(StrToInt(inte.getIntergrityGood())
							+ StrToInt(inte.getIntergrityNormal())
							+ StrToInt(inte.getIntergrityBad()));
				}
				info.add(inte);
			}
			return info;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
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
		case R.id.integrity_back:
			finish();
			break;

		default:
			break;
		}
	}
}
