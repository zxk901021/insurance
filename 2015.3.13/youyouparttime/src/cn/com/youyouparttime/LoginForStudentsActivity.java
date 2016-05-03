package cn.com.youyouparttime;

import org.json.JSONObject;

import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.CommonUtil;
import cn.com.youyouparttime.util.HttpUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginForStudentsActivity extends Activity implements OnClickListener, OnCheckedChangeListener{

	private LinearLayout backtoWelcome;
	private TextView quickRegister;
	private EditText studentsUsername;
	private EditText studentsPassword;
	private CheckBox rememberPassword;
	private TextView forgetPassword;
	private Button login;
	private TextView noRegister;
	SharedPreferences rememberPass;
	Editor editor;
	private String username;
	private String password;
	private boolean isRemembered;
	private static boolean isExit = false;
	public static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
		super.handleMessage(msg);
		isExit = false;
		}
	};
	public static final int COMPANY_MODE = 2;
	private int mode;
	private ProgressDialog dialog;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_login);
		SysApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		mode = intent.getIntExtra("launchMode", 0);
		if (mode == COMPANY_MODE) {
			rememberPass = getSharedPreferences("companyPrefer", 0);
		}else {
			rememberPass = getSharedPreferences("myPrefer", 0);
		}
		
		editor = rememberPass.edit();
		Log.e("loginMode", mode + ":::");
		initView();
		
	}


	private void initView(){
		dialog = new ProgressDialog(this);
		backtoWelcome = (LinearLayout) findViewById(R.id.back_to_wel);
		backtoWelcome.setOnClickListener(this);
		quickRegister = (TextView) findViewById(R.id.quick_register);
		if (mode == 2) {
			quickRegister.setVisibility(View.GONE);
		}
		quickRegister.setOnClickListener(this);
		studentsUsername = (EditText) findViewById(R.id.username_students);
		studentsPassword = (EditText) findViewById(R.id.password_student);
		rememberPassword = (CheckBox) findViewById(R.id.remember_password);
		rememberPassword.setOnCheckedChangeListener(this);
		username = rememberPass.getString("username", "");
		password = rememberPass.getString("password", "");
		isRemembered = rememberPass.getBoolean("rememberPassword", false);
		
		studentsUsername.setText(CommonUtil.nullToEmpty(username));
		if (isRemembered) {
			studentsPassword.setText(CommonUtil.nullToEmpty(password));
			rememberPassword.setChecked(true);
		}
		
		forgetPassword = (TextView) findViewById(R.id.forget_password);
		forgetPassword.setOnClickListener(this);
		login = (Button) findViewById(R.id.login_student);
		login.setOnClickListener(this);
		noRegister = (TextView) findViewById(R.id.no_register);
		noRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		if (mode == COMPANY_MODE) {
			noRegister.setText("◊¢≤·’À∫≈");
		}
		noRegister.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_to_wel:
			Intent back = new Intent(LoginForStudentsActivity.this, MainActivity.class);
			startActivity(back);
			finish();
			break;

		case R.id.quick_register:
			Intent intent = new Intent(LoginForStudentsActivity.this, RegisterForStudentsActivity.class);
			startActivity(intent);
			break;
		case R.id.remember_password:
	
			break;
		case R.id.forget_password:
			Intent findPassword = new Intent(LoginForStudentsActivity.this, FindPasswordActivity.class);
			findPassword.putExtra("mode", mode);
			startActivity(findPassword);
			break;
		case R.id.login_student:
			username = studentsUsername.getText().toString();
			password = studentsPassword.getText().toString();
			if (TextUtils.isEmpty(username)) {
				Toast.makeText(LoginForStudentsActivity.this, "«Î ‰»Î’À∫≈", Toast.LENGTH_SHORT).show();
				return;
			}else if (!TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) {
				Toast.makeText(LoginForStudentsActivity.this, "«Î ‰»Î√‹¬Î", Toast.LENGTH_SHORT).show();
				return;
			}
			String type = mode == COMPANY_MODE ? "2" : "1" ;
			int login = mode == COMPANY_MODE ? 2 : 1 ;
			String msg = null;
			try {
				JSONObject object = new JSONObject();
				object.put("username", username);
				object.put("password", password);
				object.put("usertype", type);
//				Log.e("object", object.toString());
//				Log.e("result", new JSONObject(HttpUtil.postRequst(UrlUtil.LOGINURL, object))+"");
				JSONObject resultJson = new JSONObject(HttpUtil.postRequst(UrlUtil.LOGINURL, object,dialog));
				String result = resultJson.getString("result");
				msg = resultJson.getString("msg");
				String id = resultJson.getString("id");
				String usertype = resultJson.getString("usertype");
//				Log.e("aaaaaaa", result);
				if (result.equals("true")) {
					editor.putInt("isLogin", login);
					editor.putString("username", username);
					editor.putString("password", password);
					editor.putString("uid", id);
					editor.putString("usertype", usertype);
					editor.commit();
					SharedPreferences preferences = getSharedPreferences("myPrefer", 0);
					Editor edit = preferences.edit();
					edit.putInt("isLogin", login);
					edit.commit();
					Toast.makeText(LoginForStudentsActivity.this, "µ«¬Ω≥…π¶£°", Toast.LENGTH_SHORT).show();
					if (mode == COMPANY_MODE) {
						Intent companyIntent = new Intent(LoginForStudentsActivity.this, ReleasePartTimeActivity.class);
						startActivity(companyIntent);
						finish();
					}else {
						Intent susLogin = new Intent(LoginForStudentsActivity.this, PartTimeActivity.class);
						startActivity(susLogin);
						finish();
					}
					
					
				}else {
					Toast.makeText(LoginForStudentsActivity.this, msg, Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(LoginForStudentsActivity.this, msg, Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.no_register:
			if (mode == COMPANY_MODE) {
				Intent companyRegisterIntent = new Intent(LoginForStudentsActivity.this, RegisterForStudentsActivity.class);
				companyRegisterIntent.putExtra("launchMode", COMPANY_MODE);
				startActivity(companyRegisterIntent);
				return;
			}
			Intent intent2 = new Intent(LoginForStudentsActivity.this, PartTimeActivity.class);
			startActivity(intent2);
			finish();
			break;
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			Bundle userInfo = data.getExtras();
			String username = userInfo.getString("username");
			String password = userInfo.getString("password");
			studentsUsername.setText(username);
			studentsPassword.setText(password);
			break;

		case 10:
			boolean rememberPassword = data.getBooleanExtra("rememberPassword", true);
			editor.putBoolean("rememberPassword", rememberPassword);
			this.rememberPassword.setChecked(rememberPassword);
			break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
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
                 Toast.makeText(getApplicationContext(), "‘Ÿ∞¥“ª¥ŒÕÀ≥ˆ≥Ã–Ú",
                                 Toast.LENGTH_SHORT).show();
                 handler.sendEmptyMessageDelayed(0, 2000);
         } else {
        	 SysApplication.getInstance().exit();
         }
 }


	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			editor.putBoolean("rememberPassword", true);
			editor.commit();
		}else {
			editor.putBoolean("rememberPassword", false);
			editor.commit();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		isRemembered = rememberPass.getBoolean("rememberPassword", true);
		Log.e("resume", isRemembered+"'");
		rememberPassword.setChecked(isRemembered);
		if (!isRemembered) {
			studentsPassword.setText("");
		}
		
		Log.e("aa", "onResume()");
	}
}
