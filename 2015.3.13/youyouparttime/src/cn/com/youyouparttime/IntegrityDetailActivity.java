package cn.com.youyouparttime;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.com.youyouparttime.adapter.IntegrityDetailAdapter;
import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.IntergrityInfo;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.HttpUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class IntegrityDetailActivity extends Activity implements OnClickListener{

	
	private LinearLayout back;
	private ListView integrityDetailList;
	private String cid;
	private String jobid;
	List<IntergrityInfo> list = new ArrayList<IntergrityInfo>();
	IntegrityDetailAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integrity_detail);
		SysApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		cid =intent.getStringExtra("cid");
		jobid = intent.getStringExtra("jobid");
		list = getList();
		initView();
	}

	public void initView(){
		back = (LinearLayout) findViewById(R.id.integrity_detail_back);
		back.setOnClickListener(this);
		integrityDetailList = (ListView) findViewById(R.id.integrity_detail_list);
		adapter = new IntegrityDetailAdapter(list, this);
		integrityDetailList.setAdapter(adapter);
	}
	
	public List<IntergrityInfo> getList(){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cid", cid);
			jsonObject.put("jobid", jobid);
			String result = HttpUtil.postRequst(UrlUtil.INTEGRITY_RECORD_DETAIL_URL, jsonObject);
			JSONObject resultJson = new JSONObject(result);
			JSONObject reJsonObject = resultJson.getJSONObject("comm_list");
			JSONArray array = reJsonObject.getJSONArray("comment_list");
			for (int i = 0; i < array.length(); i++) {
				IntergrityInfo info = new IntergrityInfo();
				info.setIntergrityMember(array.getJSONObject(i).getString("name"));
				info.setIntergrityComment(array.getJSONObject(i).getString("content"));
				list.add(info);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.integrity_detail_back:
			finish();
			break;

		default:
			break;
		}
	}
}
