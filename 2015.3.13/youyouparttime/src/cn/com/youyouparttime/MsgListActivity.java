package cn.com.youyouparttime;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.com.youyouparttime.adapter.MsgListAdapter;
import cn.com.youyouparttime.entity.CompanyCommentMsg;
import cn.com.youyouparttime.entity.Constant;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.CommonUtil;
import cn.com.youyouparttime.util.HttpUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class MsgListActivity extends Activity {

	private ImageView back;
	private ListView listView;
	SharedPreferences share;
	String uid;
	private MsgListAdapter adapter;
	List<CompanyCommentMsg> list;
	int mode;
	private String userType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg_list);
		Intent intent = getIntent();
		mode = intent.getIntExtra("commenttype", 0);
		userType = intent.getStringExtra("usertype");
		back = (ImageView) findViewById(R.id.msg_list_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		listView = (ListView) findViewById(R.id.msg_list);
		if (mode == 4) {
			share = getSharedPreferences(Constant.COMPANY_PREFER, 0);
		}else {
			share = getSharedPreferences(Constant.STUDENT_PREFER, 0);
		}
		uid = share.getString("uid", null);
		list = new ArrayList<CompanyCommentMsg>();
		list = getData(mode);
		adapter = new MsgListAdapter(list, this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					String msgId = list.get(position).getMsgId();
					list.get(position).setIsRead("ÒÑ¶Á");
					
					adapter.notifyDataSetChanged();
					Intent intent = new Intent(MsgListActivity.this, MsgDetailActivity.class);
					intent.putExtra("msgid", msgId);
					Log.e("msgid", msgId);
					intent.putExtra("mode", mode);
					intent.putExtra("type", list.get(position).getType());
					intent.putExtra("usertype", userType);
					intent.putExtra("type2", CommonUtil.nullToEmpty(list.get(position).getTypeTwo()));
					startActivityForResult(intent, 1);
					
			}
		});
	}

	public List<CompanyCommentMsg> getData(int mode){
		JSONObject object = new JSONObject();
		List<CompanyCommentMsg> list = new ArrayList<CompanyCommentMsg>();
		try {
			object.put("uid", uid);
			JSONObject jsonObject = new JSONObject(HttpUtil.postRequst(getUrl(mode), object));
			JSONArray array = jsonObject.getJSONArray("msg_list");
			for (int i = 0; i < array.length(); i++) {
				CompanyCommentMsg commentMsg = new CompanyCommentMsg();
				commentMsg.setName(array.getJSONObject(i).getString("name"));
				commentMsg.setTime(array.getJSONObject(i).getString("datetime"));
				commentMsg.setIsRead(isReadChange(array.getJSONObject(i).getString("is_browse")));
				commentMsg.setMsgId(array.getJSONObject(i).getString("msgid"));
				commentMsg.setType(array.getJSONObject(i).getString("type"));
				try {
					commentMsg.setTypeTwo(array.getJSONObject(i).getString("type2"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				list.add(commentMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public String isReadChange(String isRead){
		String result = null;
		result = isRead.equals("1") ? "Î´¶Á" : "ÒÑ¶Á";
		return result;
	}
	
	public String getUrl (int mode){
		String url = null;
		switch (mode) {
		case 0:
			url = UrlUtil.MSG_ALL_STUDENT_URL;
			break;
		case 1:
			url = UrlUtil.COMPANY_COMMENT_URL;
			break;
		case 2:
			url = UrlUtil.SYSTEM_COMMENT_URL;
			break;
		case 3:
			url = UrlUtil.INTERVIEW_MSG_URL;
			break;
		case 4:
			url = UrlUtil.COMPANY_MSG_URL;
			break;
		}
		
		return url;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			list = getData(mode);
			adapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	@Override
	protected void onRestart() {
		super.onRestart();
		list = getData(mode);
		adapter = new MsgListAdapter(list, this);
		listView.setAdapter(adapter);
	}
}
