package com.example.market.activity;

import com.example.market.R;
import com.example.market.R.id;
import com.example.market.R.layout;
import com.example.market.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

public class InsuranceDetailActivity extends Activity implements OnClickListener{

	private WebView insuranceDetail;
	private ImageView back;
	private Button buy;
	private String detailUrl;
	private int flag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insurance_detail);
		Intent intent = getIntent();
		flag = intent.getIntExtra("flag", 1);
		initView();
	}
	
	private void initView(){
		insuranceDetail = (WebView) findViewById(R.id.insurance_webview);
		back = (ImageView) findViewById(R.id.insruance_back);
		buy = (Button) findViewById(R.id.insurance_buy);
		back.setOnClickListener(this);
		buy.setOnClickListener(this);
		insuranceDetail.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});
		WebSettings settings = insuranceDetail.getSettings();
		settings.setJavaScriptEnabled(true);
		detailUrl = getDetailUrl(flag);
		insuranceDetail.loadUrl(detailUrl);
	}
	
	private String getDetailUrl(int flag){
		String url = "";
		switch (flag) {
		case 1:
			url = "http://api2.renrenbx.com/mobile/product/productShow.html?productId=201601191453lianjiaotong";
			return url;

		case 2:
			url = "http://api2.renrenbx.com/mobile/product/productShow.html?productId=20160215104800000aiyabao";
			break;
			
		case 3:
			url = "http://api2.renrenbx.com/mobile/product/productShow.html?productId=55d46691e4b07d9ed1a2841c";
			return url;
		}
		return url;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.insurance_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.insruance_back:
			finish();
			break;

		case R.id.insurance_buy:
			Intent intent = new Intent(InsuranceDetailActivity.this, BuyActivity.class);
			startActivity(intent);
			break;
		}
			
	}
}
