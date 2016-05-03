package cn.com.youyouparttime;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import cn.com.youyouparttime.adapter.ZiliListAdapter;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.entity.ZiliEntity;
import cn.com.youyouparttime.util.HttpUtil;

public class ZiliListActivity extends Activity {

	private LinearLayout back;
	private ListView ziliList;
	private ZiliListAdapter adapter;
	SharedPreferences preferences;
	String uid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zili_list);
		preferences = getSharedPreferences("myPrefer", 0);
		uid = preferences.getString("uid", null);
		back = (LinearLayout) findViewById(R.id.zili_list_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			finish();	
			}
		});
		ziliList = (ListView) findViewById(R.id.zili_list);
		adapter = new ZiliListAdapter(getData(), this);
		ziliList.setAdapter(adapter);
	}

	public List<ZiliEntity> getData(){
		JSONObject object = new JSONObject();
		List<ZiliEntity> list = new ArrayList<ZiliEntity>();
		try {
			object.put("uid", uid);
			String result = HttpUtil.postRequst(UrlUtil.GET_ZILI_LIST_URL, object);
			JSONObject jsonObject = new JSONObject(result);
			Log.e("a", jsonObject.toString());
			JSONArray array = jsonObject.getJSONArray("employ_list");
			for (int i = 0; i < array.length(); i++) {
				JSONObject json = array.getJSONObject(i);
				ZiliEntity entity = new ZiliEntity();
				entity.setTitle(json.getString("name"));
				entity.setTime(json.getString("stime"));
				entity.setSubmitStatus(json.getString("zl_state"));
				entity.setJobId(json.getString("jobid"));
				entity.setcId(json.getString("cid"));
				entity.setJobType(json.getString("jobclass"));
				Log.e("b", entity.getJobType());
				list.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
}
