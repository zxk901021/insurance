package cn.com.youyouparttime;

import org.json.JSONObject;

import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.HttpUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class YouDetailActivity extends Activity implements OnClickListener{
	
	private String title;
	private TextView titleTv;
	private TextView contentTv;
	private String content;
	private TextView back;
	private WebView view;
	private String catid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_you_detail);
		SysApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		catid = intent.getStringExtra("catid");
//		getData();
		initView();
	}
	
	@SuppressLint("SetJavaScriptEnabled") public void initView(){
		titleTv = (TextView) findViewById(R.id.you_detail_title);
		contentTv = (TextView) findViewById(R.id.you_detail_content);
		back = (TextView) findViewById(R.id.you_detail_back);
		back.setOnClickListener(this);
		view = (WebView) findViewById(R.id.you_detail_webview);
		titleTv.setText(title);
//		contentTv.setText(Html.fromHtml(content));
		view.getSettings().setJavaScriptEnabled(true);
		view.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});
		view.loadUrl(UrlUtil.INFOS_DETAIL + catid);
	}
	
	public String getData(){
		JSONObject object = new JSONObject();
		content = "";
		try {
			object.put("title", title);
			String result =  HttpUtil.postRequst(UrlUtil.YOU_DETAIL_URL, object);
			JSONObject data = new JSONObject(result);
			content = data.getJSONObject("content").getString("content");
			return content;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.you_detail_back:
			if (view.canGoBack()) {
				view.goBack();
			}else {
				finish();	
			}
			
			break;

		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.ACTION_DOWN == event.getAction()
				&& keyCode == KeyEvent.KEYCODE_BACK && view.canGoBack()) {
			view.goBack();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
