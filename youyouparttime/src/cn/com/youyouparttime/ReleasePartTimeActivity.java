package cn.com.youyouparttime;


import org.json.JSONObject;

import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.HttpUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ReleasePartTimeActivity extends Activity implements OnClickListener{

	private RelativeLayout companyInfo;
	private RelativeLayout releaseParttime;
	private RelativeLayout companyMsg;
	private RelativeLayout applyAuthentication;
	private RelativeLayout adminJob;
	private RelativeLayout companyIntegrity;
	private TextView companyName;
	private static boolean isExit = false;
	public static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
		super.handleMessage(msg);
		isExit = false;
		}
	};
	private SharedPreferences preferences;
	private Editor editor;
	private String name;
	private String userType;
	private String uid;
	private Button backLogin;
	private String cx,cert;
	private ImageView cxImg,certImg;
	private boolean isSubmit;
	private String username;
	private String companyNameStr;
	private String newMsgInfo;
	private int newMsgCount;
	private boolean hasNewMsg;
	private ImageView msgImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_release_part_time);
		SysApplication.getInstance().addActivity(this);
		preferences = getSharedPreferences("companyPrefer", 0);
		editor = preferences.edit();
		uid = preferences.getString("uid", null);
		userType = preferences.getString("usertype", null);
		isSubmit = preferences.getBoolean("hasSubmit", false);
		Log.e("isSubmit", isSubmit+"'");
		username = preferences.getString("username", "");
		initView();
		getInfo();
		if (cx.equals("0")) {
			cxImg.setImageResource(R.drawable.company_icon2_1);
		}else if (cx.equals("1")) {
			cxImg.setImageResource(R.drawable.company_icon2);
		}
		if (cert.equals("0")) {
			certImg.setImageResource(R.drawable.company_icon1_1);
			editor.putBoolean("certSucess", false);
			editor.commit();
		}else {
			certImg.setImageResource(R.drawable.company_icon1);
			editor.putBoolean("certSucess", true);
			editor.commit();
		}
		if (hasNewMsg) {
			msgImage.setBackgroundResource(R.drawable.company_msg);
		}else {
			msgImage.setBackgroundResource(R.drawable.company_icon7);
		}
	}
	
	public void getInfo(){
		JSONObject object = new JSONObject();
		JSONObject info = new JSONObject();
		try {
			object.put("uid", uid);
			String result = HttpUtil.postRequst(UrlUtil.COMPANY_INFO_URL, object);
			info = new JSONObject(result);
			Log.e("公司信息", info.toString());
			name = info.getJSONObject("userinfo").getString("name");
//			editor.putString("companyname", name);
//			editor.commit();
			cx = info.getJSONObject("userinfo").getString("cx");
			cert = info.getJSONObject("userinfo").getString("cert");
			newMsgInfo = info.getJSONObject("userinfo").getString("newmsg");
			newMsgCount = Integer.parseInt(newMsgInfo);
			if (newMsgCount > 0) {
				hasNewMsg = true;
			}else {
				hasNewMsg = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initView(){
		companyInfo = (RelativeLayout) findViewById(R.id.company_detail);
		companyName = (TextView) findViewById(R.id.company_name);
//		if (name != null && name.length() > 0) {
//			companyName.setText(name);
//		}else {
//			companyName.setText(username);
//		}
		if (isSubmit) {
			companyNameStr = preferences.getString("companyname", null);
			companyName.setText(companyNameStr);
			companyName.setTextColor(getResources().getColor(R.color.text_color));
		}else {
			companyName.setText("请先完善您的资料");
			companyName.setTextColor(getResources().getColor(R.color.detail_second_text));
		}
		msgImage = (ImageView) findViewById(R.id.company_msg_img);
		cxImg = (ImageView) findViewById(R.id.company_cx);
		certImg = (ImageView) findViewById(R.id.company_cert);
		releaseParttime = (RelativeLayout) findViewById(R.id.company_release_parttime);
		companyMsg = (RelativeLayout) findViewById(R.id.company_my_msg);
		applyAuthentication = (RelativeLayout) findViewById(R.id.apply_authentication);
		adminJob = (RelativeLayout) findViewById(R.id.admin_job_station);
		companyIntegrity = (RelativeLayout) findViewById(R.id.company_integrity);
		companyIntegrity.setOnClickListener(this);
		adminJob.setOnClickListener(this);
		applyAuthentication.setOnClickListener(this);
		companyMsg.setOnClickListener(this);
		releaseParttime.setOnClickListener(this);
		companyInfo.setOnClickListener(this);
		
		
		backLogin = (Button) findViewById(R.id.back_to_login);
		backLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences share = getSharedPreferences("myPrefer", 0);
				Editor editor = share.edit();
				editor.putInt("isLogin", 0);
				editor.commit();
				Intent intent = new Intent(ReleasePartTimeActivity.this, LoginForStudentsActivity.class);
				intent.putExtra("launchMode", 2);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.company_detail:
			Intent intent = new Intent(ReleasePartTimeActivity.this, CompanyInfoSubmitActivity.class);
			startActivity(intent);
			break;

		case R.id.company_release_parttime:
			Log.e("true or false", isSubmit + "!!!");
			if (!isSubmit) {
				Toast.makeText(ReleasePartTimeActivity.this, "请先完善企业信息!", Toast.LENGTH_SHORT).show();
				return;
			}
			Intent releaseIntent = new Intent(ReleasePartTimeActivity.this, CompanyReleaseActivity.class);
			startActivity(releaseIntent);
			
			break;
			
		case R.id.company_my_msg:
			if (!isSubmit) {
				Toast.makeText(ReleasePartTimeActivity.this, "请先完善企业信息!", Toast.LENGTH_SHORT).show();
				return;
			}
			Intent msgIntent = new Intent(ReleasePartTimeActivity.this, MsgListActivity.class);
			msgIntent.putExtra("commenttype", 4);
			msgIntent.putExtra("usertype", userType);
			startActivity(msgIntent);
			break;
			
		case R.id.apply_authentication:
			if (!isSubmit) {
				Toast.makeText(ReleasePartTimeActivity.this, "请先完善企业信息，再认证!", Toast.LENGTH_SHORT).show();
				return;
			}
			Intent applyIntent = new Intent(ReleasePartTimeActivity.this, ApplyAuthenticationActivity.class);
			startActivity(applyIntent);
			break;
			
		case R.id.admin_job_station:
			if (!isSubmit) {
				Toast.makeText(ReleasePartTimeActivity.this, "请先完善企业信息!", Toast.LENGTH_SHORT).show();
				return;
			}
			Intent adminIntent = new Intent(ReleasePartTimeActivity.this, AdmintJobActivity.class);
			startActivity(adminIntent);
			break;
			
		case R.id.company_integrity:
			if (!isSubmit) {
				Toast.makeText(ReleasePartTimeActivity.this, "请先完善企业信息!", Toast.LENGTH_SHORT).show();
				return;
			}
			Intent integrityIntent = new Intent(ReleasePartTimeActivity.this, CompanyIntegrityActivity.class);
			startActivity(integrityIntent);
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	 private void exit() {
         if (!isExit) {
                 isExit = true;
                 Toast.makeText(getApplicationContext(), "再按一次退出程序",
                                 Toast.LENGTH_SHORT).show();
                 handler.sendEmptyMessageDelayed(0, 2000);
         } else {
        	 SysApplication.getInstance().exit();
         }
 }
	 
	 
	 @Override
	protected void onResume() {
		super.onResume();
		companyNameStr = preferences.getString("companyname", null);
		Log.e("companyNameStr", companyNameStr + "'");
		isSubmit = preferences.getBoolean("hasSubmit", false);
		if (companyNameStr != null && companyNameStr.length() > 0) {
			companyName.setText(companyNameStr);
		}
	}

	 @Override
	protected void onRestart() {
		super.onRestart();
		getInfo();
		if (cx.equals("0")) {
			cxImg.setImageResource(R.drawable.company_icon2_1);
		}else if (cx.equals("1")) {
			cxImg.setImageResource(R.drawable.company_icon2);
		}
		if (cert.equals("0")) {
			certImg.setImageResource(R.drawable.company_icon1_1);
			editor.putBoolean("certSucess", false);
			editor.commit();
		}else {
			certImg.setImageResource(R.drawable.company_icon1);
			editor.putBoolean("certSucess", true);
			editor.commit();
		}
		if (hasNewMsg) {
			msgImage.setBackgroundResource(R.drawable.company_msg);
		}else {
			msgImage.setBackgroundResource(R.drawable.company_icon7);
		}
	}
	 
}
