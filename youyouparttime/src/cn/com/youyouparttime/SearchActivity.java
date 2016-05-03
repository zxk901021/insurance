package cn.com.youyouparttime;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.com.youyouparttime.adapter.ClassifyGuideListAdapter;
import cn.com.youyouparttime.adapter.PartTimeInfoAdapter;
import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.PartTimeInfo;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.HttpUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SearchActivity extends Activity implements OnClickListener{

	private LinearLayout back;
	private EditText searchContent;
	String contentStr;
	private ListView searchList;
//	private ClassifyGuideListAdapter adapter;
	private PartTimeInfoAdapter adapter;
	private TextView noResult;
	List<PartTimeInfo> list = new ArrayList<PartTimeInfo>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		SysApplication.getInstance().addActivity(this);
		initView();
	}

	public void initView(){
		back = (LinearLayout) findViewById(R.id.search_back);
		searchContent = (EditText) findViewById(R.id.search_content);
		searchList = (ListView) findViewById(R.id.search_list);
		noResult = (TextView) findViewById(R.id.search_result_text);
		
		back.setOnClickListener(this);
		searchContent.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				contentStr = searchContent.getText().toString(); 
					list = getList();
					if (list == null || list.size() == 0) {
						if (searchList.getVisibility() == View.VISIBLE) {
							searchList.setVisibility(View.GONE);
						}
						noResult.setVisibility(View.VISIBLE);
					}else {
						if (noResult.getVisibility() == View.VISIBLE) {
							noResult.setVisibility(View.GONE);
						}
//						adapter = new ClassifyGuideListAdapter(list, SearchActivity.this);
						adapter = new PartTimeInfoAdapter(list, SearchActivity.this);
						searchList.setAdapter(adapter);
						searchList.setVisibility(View.VISIBLE);
					}
					
					searchList.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
								String Id = list.get(position).getId();
								Intent intent = new Intent(SearchActivity.this, PartTimeDetailActivity.class);
								intent.putExtra("id", Id);
								startActivity(intent);
						}
					});
				
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_back:
			finish();
			break;

		default:
			break;
		}
	}
	public List<PartTimeInfo> getList(){
		JSONObject object = new JSONObject();
		list = new ArrayList<PartTimeInfo>();
		try {
			object.put("keywords", contentStr);
			String result = HttpUtil.postRequst(UrlUtil.KEY_WORDS_SEARCH_URL, object);
			JSONObject jsonObject = new JSONObject(result);
			JSONArray array = jsonObject.getJSONArray("joblist");
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
				String id = array.getJSONObject(i).getString("jobid");
				String vip = array.getJSONObject(i).getString("vip");
				String hot = array.getJSONObject(i).getString("hotest");
				if (vip.equals("0")) {
					info.setVIP(false);
				}else {
					info.setVIP(true);
				}
				if (hot.equals("0")) {
					info.setHot(false);
				}else {
					info.setHot(true);
				}
				info.setJobName(name);
				info.setJobType(type);
				info.setJobAddress(address);
				info.setJobReleaseTime(time);
				info.setPeopleCount(account);
				info.setId(id);
				list.add(info);
				Log.e("list", list.size()+"'");
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
