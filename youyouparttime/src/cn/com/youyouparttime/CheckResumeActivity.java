package cn.com.youyouparttime;

import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.UrlUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class CheckResumeActivity extends Activity {

	private TextView back;
	private WebView view;
	private String uid,username,sqid,jobid;
	private TextView name;
	
	@SuppressLint("SetJavaScriptEnabled") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_resume);
		SysApplication.getInstance().addActivity(this);
		back = (TextView) findViewById(R.id.resume_check_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		view = (WebView) findViewById(R.id.com_check_resume);
		name = (TextView) findViewById(R.id.check_resume_name);
		Intent intent = getIntent();
		uid = intent.getStringExtra("uid");
		username = intent.getStringExtra("name");
		sqid = intent.getStringExtra("sqid");
		jobid = intent.getStringExtra("jobid");
		name.setText(username);
		view.getSettings().setJavaScriptEnabled(true);
		view.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});
		view.loadUrl(UrlUtil.HTML_RESUME_URL + "&uid=" + uid + "&sqid=" + sqid + "&jobid=" + jobid);
	}
}
