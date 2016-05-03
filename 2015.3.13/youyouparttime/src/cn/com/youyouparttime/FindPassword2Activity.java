package cn.com.youyouparttime;

import org.json.JSONObject;

import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.Constant;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.CommonUtil;
import cn.com.youyouparttime.util.HttpUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FindPassword2Activity extends Activity implements OnClickListener, OnTouchListener {

	private LinearLayout back;
	private EditText newPassword;
	private EditText ensurePassword;
	private ImageView enterView;
	private ImageView ensurView;
	private Button submit;
	SharedPreferences preferences;
	Editor editor;
	Handler handler = null;
	int mode;
	String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_find_password2);
		Intent intent = getIntent();
		mode = intent.getIntExtra("mode", -1);
		username = intent.getStringExtra("username");
		if (mode == 2) {
			preferences = getSharedPreferences(Constant.COMPANY_PREFER, 0);
		}else {
			preferences = getSharedPreferences("myPrefer", 0);
		}
		
		editor = preferences.edit();
		initView();
		
		
	}

	public void initView() {
		back = (LinearLayout) findViewById(R.id.find2_back);
		newPassword = (EditText) findViewById(R.id.find2_phone_edt);
		ensurePassword = (EditText) findViewById(R.id.find2_verify_edt);
		enterView = (ImageView) findViewById(R.id.find2_can_see);
		ensurView = (ImageView) findViewById(R.id.find2_can_see_see);
		submit = (Button) findViewById(R.id.find2_submit);
		back.setOnClickListener(this);
		enterView.setOnTouchListener(this);
		ensurView.setOnTouchListener(this);
		submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.find2_back:
			finish();
			break;

		case R.id.find2_submit:
			if (TextUtils.equals(newPassword.getText().toString(), ensurePassword.getText().toString())&&! TextUtils.isEmpty(newPassword.getText())) {
				
				
				String password = newPassword.getText().toString();
				String userType ;
				JSONObject object = new JSONObject();
				if (mode == 2) {
					userType = "2";
				}else {
					userType = "1";
				}
				try {
					object.put("username", username);
					object.put("newpw", password);
					object.put("usertype", userType);
					String request = HttpUtil.postRequst(UrlUtil.FIND_PASSWORD_URL, object);
					JSONObject resultJson = new JSONObject(request);
					String result = resultJson.getString("result");
					if (result.equals("true")) {
						Toast.makeText(FindPassword2Activity.this, "修改密码成功啦", Toast.LENGTH_SHORT).show();
						editor.putBoolean("rememberPassword", false);
						editor.commit();
//						intent.setClass(FindPassword2Activity.this, LoginForStudentsActivity.class);
//						intent.putExtra("rememberPassword", false);
						finish();
						
					}else {
						String msg = resultJson.getString("msg");
						Toast.makeText(FindPassword2Activity.this, msg, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			else {
				Toast.makeText(FindPassword2Activity.this, "两次输入密码不正确", Toast.LENGTH_SHORT).show();
			}
			
			break;
		default:
			break;
		}
	}

	@SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.find2_can_see:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				newPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				newPassword.setSelection(newPassword.getText().length());
				break;

			case MotionEvent.ACTION_UP:
				newPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
				newPassword.setSelection(newPassword.getText().length());
				break;
			}
			break;

		case R.id.find2_can_see_see:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				ensurePassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				ensurePassword.setSelection(ensurePassword.getText().length());
				break;

			case MotionEvent.ACTION_UP:
				ensurePassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
				ensurePassword.setSelection(ensurePassword.getText().length());
				break;
			}
			break;
		}
		
		return true;
	}

}
