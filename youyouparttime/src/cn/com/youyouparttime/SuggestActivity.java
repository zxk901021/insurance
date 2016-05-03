package cn.com.youyouparttime;

import org.json.JSONObject;

import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.Constant;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.CommonUtil;
import cn.com.youyouparttime.util.HttpUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SuggestActivity extends Activity implements OnClickListener{
	
	private LinearLayout back;
	private TextView submit,title;
	private EditText suggestContent;
	private String suggestStr;
	private SharedPreferences preferences;
	private String uid;
	private boolean isApply;
	private String jobId,cId;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggest);
		SysApplication.getInstance().addActivity(this);
		preferences = getSharedPreferences("myPrefer", 0);
		Intent intent = getIntent();
		isApply = intent.getBooleanExtra("apply", false);
		jobId = intent.getStringExtra("jobid");
		cId = intent.getStringExtra("cid");
		back = (LinearLayout) findViewById(R.id.suggest_back);
		title = (TextView) findViewById(R.id.suggest_title);
		submit = (TextView) findViewById(R.id.suggest_submit);
		suggestContent = (EditText) findViewById(R.id.suggest_content);
		submit.setOnClickListener(this);
		back.setOnClickListener(this);
		if (isApply) {
			title.setText("我兼职我骄傲");
			suggestContent.setText(Html.fromHtml(Constant.COMMONT_STR));
		}
		
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.suggest_back:
			finish();
			break;

		case R.id.suggest_submit:
			suggestStr = suggestContent.getText().toString();
			uid = preferences.getString("uid", null);
			if (TextUtils.isEmpty(suggestStr)) {
				Toast.makeText(SuggestActivity.this, "您输入的信息为空！", Toast.LENGTH_SHORT).show();
				return;
			}
			JSONObject object = new JSONObject();
			if (isApply) {
				try {
					object.put("uid", uid);
					object.put("jobid", jobId);
					object.put("cid", cId);
					String content = suggestContent.getText().toString();
					String number = CommonUtil.getNumber(content);
					object.put("reward", number);
					Log.e("content", object.toString());
					String result = HttpUtil.postRequst(UrlUtil.GET_ZILI_STAR_URL, object);
					JSONObject resultJson = new JSONObject(result);
					String a = resultJson.getString("result");
					String msg = resultJson.getString("msg");
					if (a.equals("true")) {
						Toast.makeText(SuggestActivity.this, msg, Toast.LENGTH_SHORT).show();
						finish();
					}else {
						Toast.makeText(SuggestActivity.this, msg, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				try {
					object.put("uid", uid);
					object.put("content", suggestStr);
					String result = HttpUtil.postRequst(UrlUtil.SUGGESSTION_URL, object);
					JSONObject resultJson = new JSONObject(result);
					String a = resultJson.getString("result");
					String msg = resultJson.getString("msg");
					if (a.equals("true")) {
						Toast.makeText(SuggestActivity.this, msg, Toast.LENGTH_SHORT).show();
						finish();
					}else {
						Toast.makeText(SuggestActivity.this, msg, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			break;
		}
	}

	
}
