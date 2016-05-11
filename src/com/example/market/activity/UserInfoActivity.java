package com.example.market.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.example.market.R;
import com.example.market.utils.Constants;
import com.lib.volley.HTTPUtils;
import com.lib.volley.VolleyListener;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class UserInfoActivity extends Activity implements OnClickListener {

	private ImageView back;
	private String uid;
	private SharedPreferences sp;
	private String infoResult;
	private String userName;
	private String phoneName;
	private String email;
	private String sex;
	private String birthday;
	private String oldPassword, newPassword, confirmPassword;
	private EditText nameEdt, phoneEdt, emailEdt;
	private EditText oldPasswordEdt, newPasswordEdt, confirmPasswordEdt;
	private Button changeUserInfo, changePassword;
	private String changePwdResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		initView();
		widgetClick();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.user_info_back);
		changeUserInfo = (Button) findViewById(R.id.change_user_info);
		nameEdt = (EditText) findViewById(R.id.user_info_name);
		phoneEdt = (EditText) findViewById(R.id.address_phone);
		emailEdt = (EditText) findViewById(R.id.user_info_email);
		changePassword = (Button) findViewById(R.id.change_password_btn);
		oldPasswordEdt = (EditText) findViewById(R.id.old_password_edt);
		newPasswordEdt = (EditText) findViewById(R.id.new_password_edt);
		confirmPasswordEdt = (EditText) findViewById(R.id.confirm_password_edt);
	}


	private void widgetClick() {
		back.setOnClickListener(this);
		changeUserInfo.setOnClickListener(this);
		changePassword.setOnClickListener(this);
	}

	private void initUploadValue() {
		userName = nameEdt.getText().toString();
		phoneName = phoneEdt.getText().toString();
		email = emailEdt.getText().toString();
		sex = "1";
		birthday = "20000101";
	}

	private void getPasswordValue() {
		oldPassword = oldPasswordEdt.getText().toString();
		newPassword = newPasswordEdt.getText().toString();
		confirmPassword = confirmPasswordEdt.getText().toString();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_info_back:
			finish();
			break;



		default:
			break;
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				try {
					JSONObject object = new JSONObject(infoResult);
					userName = object.getString("real_name");
					phoneName = object.getString("mobile_phone");
					email = object.getString("email");
					sex = object.getString("sex");
					birthday = object.getString("birthday");
					if (!TextUtils.isEmpty(userName)) {
						nameEdt.setText(userName);
					}
					if (!TextUtils.isEmpty(phoneName)) {
						phoneEdt.setText(phoneName);
					}
					if (!TextUtils.isEmpty(email)) {
						emailEdt.setText(email);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			case 2:
				try {
					JSONObject ob = new JSONObject(changePwdResult);
					String tip = ob.getString("error");
					if (tip.equals("OK")) {
						Toast.makeText(UserInfoActivity.this, "ÃÜÂëÐÞ¸Ä³É¹¦",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(UserInfoActivity.this, tip,
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				break;

			default:
				break;
			}
		};
	};
}
