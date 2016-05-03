package com.example.market.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.example.market.R;
import com.example.market.utils.Constants;
import com.lib.volley.HTTPUtils;
import com.lib.volley.VolleyListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HelpWebActivity extends Activity {

	private String url;
	private WebView web;
	private String requestData;
	private String detailUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help_web);
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		Log.e("url", url);
		getRequestUrl();
		initView();
	}

	private void getRequestUrl() {
		HTTPUtils.getVolley(HelpWebActivity.this, Constants.URL.SITE_URL + url,
				new VolleyListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

					}

					@Override
					public void onResponse(String result) {
						requestData = result;
						handler.sendEmptyMessage(1);
					}
				});
	}

	private void initView() {
		web = (WebView) findViewById(R.id.help_item_web);
		web.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				try {
					JSONArray array = new JSONArray(requestData);
					JSONObject ob = array.getJSONObject(0);
					detailUrl = ob.getString("showurl");
					Log.e("detailUrl", detailUrl);
					handler.sendEmptyMessage(2);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				break;

			case 2:
				web.loadUrl(Constants.URL.SITE_URL + detailUrl);
				break;

			default:
				break;
			}
		};
	};

}
