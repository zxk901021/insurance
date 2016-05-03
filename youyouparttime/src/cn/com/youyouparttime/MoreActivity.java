package cn.com.youyouparttime;

import org.json.JSONObject;

import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.CommonUtil;
import cn.com.youyouparttime.util.DialogUtil;
import cn.com.youyouparttime.util.HttpUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MoreActivity extends Activity implements OnClickListener{

	private TextView back;
	private RelativeLayout changePassword, about,introduce, 
							suggestion, clearCache,newVirsion;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		SysApplication.getInstance().addActivity(this);
		
		back = (TextView) findViewById(R.id.more_back);
		changePassword = (RelativeLayout) findViewById(R.id.person_change_password1);
		introduce = (RelativeLayout) findViewById(
				R.id.person_introduce);
		suggestion = (RelativeLayout) findViewById(
				R.id.person_suggestion);
		clearCache = (RelativeLayout) findViewById(
				R.id.person_clear_cache);
		newVirsion = (RelativeLayout) findViewById(
				R.id.person_new_virson);
		about = (RelativeLayout) findViewById(R.id.person_about1);
		changePassword.setOnClickListener(this);
		back.setOnClickListener(this);
		about.setOnClickListener(this);
		introduce.setOnClickListener(this);
		suggestion.setOnClickListener(this);
		clearCache.setOnClickListener(this);
		newVirsion.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more_back:
			finish();
			break;

		case R.id.person_change_password1:
			if (CommonUtil.isLogin(this) == 0) {
				DialogUtil.ifLoginDialog(this);
				return;
			}
			Intent changeIntent = new Intent(MoreActivity.this,
					ChangePasswordActivity.class);
			startActivity(changeIntent);
			break;
			
		case R.id.person_about1:
			Intent aboutIntent = new Intent(MoreActivity.this,
					YouDetailActivity.class);
			aboutIntent.putExtra("title", "关于悠悠兼职");
			aboutIntent.putExtra("catid", "20");
			startActivity(aboutIntent);
			break;
			
		case R.id.person_introduce:
			Intent introIntent = new Intent(MoreActivity.this,
					YouDetailActivity.class);
			introIntent.putExtra("title", "功能介绍");
			introIntent.putExtra("catid", "21");
			startActivity(introIntent);
			break;

		case R.id.person_suggestion:
			Intent suggestIntent = new Intent(MoreActivity.this,
					SuggestActivity.class);
			startActivity(suggestIntent);
			break;

		case R.id.person_clear_cache:
			Toast.makeText(MoreActivity.this, "缓存清理成功!", Toast.LENGTH_SHORT).show();
			break;

		case R.id.person_new_virson:
			try {
				String result = HttpUtil.post(UrlUtil.DETECTION_VERSION_URL);
				String versionName = new JSONObject(result).getJSONObject(
						"result").getString("versionName");
				String downUrl = new JSONObject(result).getJSONObject("result").getString("down_url");
				PackageInfo info = MoreActivity.this.getPackageManager()
						.getPackageInfo(MoreActivity.this.getPackageName(), 0);
				String version = info.versionName;
				Log.e("version", result);
				if (version.equals(versionName)) {
					Toast.makeText(MoreActivity.this, "已是最新版本!", Toast.LENGTH_SHORT)
							.show();
				} else {
					DialogUtil.uploadApp(MoreActivity.this, "http://tjtianxiang.com/Clinical.apk");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

}
