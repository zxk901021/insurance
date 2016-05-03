package cn.com.youyouparttime;

import cn.com.youyouparttime.entity.UrlUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HtmlResumeActivity extends Activity implements OnClickListener{

	
	private LinearLayout back;
	private WebView view;
	private SharedPreferences shared;
	private TextView gotoEdit;
	private TextView usersName;
	String uid;
	String name;
	String sqid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_html_resume);
		Intent intent = getIntent();
		sqid = intent.getStringExtra("sqid");
		uid = intent.getStringExtra("uid");
		shared = getSharedPreferences("myPrefer", 0);
//		uid = shared.getString("uid", null);
		name = shared.getString("personname", "");
		initView();
	}

	@SuppressLint("SetJavaScriptEnabled") public void initView(){
		back = (LinearLayout) findViewById(R.id.resume_review_back);
		view = (WebView) findViewById(R.id.resume_webview);
		gotoEdit = (TextView) findViewById(R.id.resume_goto_edit);
		usersName = (TextView) findViewById(R.id.users_name);
		usersName.setText(name);
		back.setOnClickListener(this);
		gotoEdit.setOnClickListener(this);
		view.getSettings().setJavaScriptEnabled(true);
		view.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});
		view.loadUrl(UrlUtil.HTML_RESUME_URL+"&uid="+uid + "&sqid=" + sqid);
		Log.e("url", UrlUtil.HTML_RESUME_URL+"&uid="+uid + "&sqid=" + sqid);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.resume_review_back:
			finish();
			break;

		case R.id.resume_goto_edit:
			Intent intent = new Intent(HtmlResumeActivity.this, MyResumeActivity.class);
			startActivity(intent);
			finish();
			break;
		}
	}

}
