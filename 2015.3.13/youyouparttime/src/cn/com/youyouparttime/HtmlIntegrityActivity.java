package cn.com.youyouparttime;

import cn.com.youyouparttime.entity.Constant;
import cn.com.youyouparttime.entity.UrlUtil;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class HtmlIntegrityActivity extends Activity {

	
	private TextView back;
	private WebView view;
	SharedPreferences share;
	private String uid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_html_integrity);
		share = getSharedPreferences(Constant.STUDENT_PREFER, 0);
		uid = share.getString("uid", "");
		back = (TextView) findViewById(R.id.html_integrity_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		view = (WebView) findViewById(R.id.integrity_html);
		view.getSettings().setJavaScriptEnabled(true);
		view.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});
		view.loadUrl(UrlUtil.INTEGRITY_HTML_URL + "&uid="+uid);
		
	}

}
