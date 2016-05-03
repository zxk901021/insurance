package cn.com.youyouparttime;

import org.json.JSONObject;

import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.HttpUtil;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ChangePasswordActivity extends Activity implements OnClickListener, OnTouchListener{

	private LinearLayout back;
	private EditText oldPassword,newPassword,ensureNewPassword;
	private ImageView oldCanSee,newCanSee,ensureNewCanSee;
	private Button submit;
	private SharedPreferences preferences;
	private String uid;
	private Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		SysApplication.getInstance().addActivity(this);
		preferences = getSharedPreferences("myPrefer", 0);
		editor = preferences.edit();
		uid = preferences.getString("uid", null);
		back = (LinearLayout) findViewById(R.id.change_password_back);
		oldPassword = (EditText) findViewById(R.id.old_password_edt);
		newPassword = (EditText) findViewById(R.id.new_password_edt);
		ensureNewPassword = (EditText) findViewById(R.id.ensure_new_password_edt);
		oldCanSee = (ImageView) findViewById(R.id.old_password_can_see);
		newCanSee = (ImageView) findViewById(R.id.new_password_can_see);
		ensureNewCanSee = (ImageView) findViewById(R.id.ensure_new_password_can_see);
		submit = (Button) findViewById(R.id.change_password_submit_btn);
		back.setOnClickListener(this);
		submit.setOnClickListener(this);
		oldCanSee.setOnTouchListener(this);
		newCanSee.setOnTouchListener(this);
		ensureNewCanSee.setOnTouchListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.change_password_back:
			finish();
			break;

		case R.id.change_password_submit_btn:
			String oldPass = oldPassword.getText().toString();
			String newPass = newPassword.getText().toString();
			String ensurePass = ensureNewPassword.getText().toString();
			if (TextUtils.isEmpty(oldPass) || TextUtils.isEmpty(newPass) || TextUtils.isEmpty(ensurePass)) {
				Toast.makeText(ChangePasswordActivity.this, "请正确输入密码信息！", Toast.LENGTH_SHORT).show();
				return;
			}else if (!TextUtils.equals(newPass, ensurePass)) {
				Toast.makeText(ChangePasswordActivity.this, "新密码两次输入的不同！", Toast.LENGTH_SHORT).show();
				return;
			}
			JSONObject object = new JSONObject();
			try {
				object.put("oldpw", oldPass);
				object.put("newpw", newPass);
				object.put("uid", uid);
				String result = HttpUtil.postRequst(UrlUtil.CHANGE_PASSWORD_URL, object);
				JSONObject resultJson = new JSONObject(result);
				String msg = resultJson.getString("msg");
				String res = resultJson.getString("result");
				if (res.equals("true")) {
					Toast.makeText(ChangePasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
					editor.putString("password", newPass);
					editor.commit();
					finish();
				}else {
					Toast.makeText(ChangePasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.old_password_can_see:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				oldPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				oldPassword.setSelection(newPassword.getText().length());
				break;

			case MotionEvent.ACTION_UP:
				oldPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
				oldPassword.setSelection(newPassword.getText().length());
				break;
			}
	
			break;

		case R.id.new_password_can_see:
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
		case R.id.ensure_new_password_can_see:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				ensureNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				ensureNewPassword.setSelection(ensureNewPassword.getText().length());
				break;

			case MotionEvent.ACTION_UP:
				ensureNewPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
				ensureNewPassword.setSelection(ensureNewPassword.getText().length());
				break;
			}
			break;
		}
		return true;
	}

	
}
