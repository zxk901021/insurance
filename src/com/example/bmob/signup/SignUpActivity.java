package com.example.bmob.signup;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

import com.android.volley.VolleyError;
import com.example.market.R;
import com.example.market.activity.MainActivity;
import com.example.market.utils.Constants;
import com.lib.volley.HTTPUtils;
import com.lib.volley.VolleyListener;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends Activity implements OnClickListener {

	private EditText mEditUser;
	private EditText mEditPsw;
	private Button mBtnSignUp;

	private boolean isBtnChecked = true;
	private EditText mEditPswVal;
	private String signResult;
	private String registerResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		// 初始化Bmob
		// Bmob.initialize(this, "736e69baa6ed388b6bf4e86acb9390ff");
		mBtnSignUp = (Button) findViewById(R.id.btn_sign_up);
		mEditUser = (EditText) findViewById(R.id.edit_uid);
		mEditPsw = (EditText) findViewById(R.id.edit_psw);
		mEditPswVal = (EditText) findViewById(R.id.edit_psw_val);
		CheckBox btnCheck = (CheckBox) findViewById(R.id.btn_check);
		TextWatcher watcher = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// setSignable();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		};
		mEditUser.addTextChangedListener(watcher);
		mEditPsw.addTextChangedListener(watcher);
		mEditPswVal.addTextChangedListener(watcher);
		// btnCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView,
		// boolean isChecked) {
		// isBtnChecked = isChecked;
		// setSignable();
		// }
		//
		// });
		mBtnSignUp.setOnClickListener(this);
		findViewById(R.id.img_back).setOnClickListener(this);
	}

	/**
	 * 是否符合注册条件
	 */
	private void setSignable() {
		if (isBtnChecked) {

			if (mEditUser.getText().toString().length() > 5
					&& mEditPsw.getText().toString().length() > 5
					&& mEditPswVal.getText().toString().length() > 5) {
				mBtnSignUp.setEnabled(true);
			} else {
				mBtnSignUp.setEnabled(false);
			}
		} else {
			mBtnSignUp.setEnabled(false);
		}
	}
	
	public void register(){
		String username = mEditUser.getText().toString();
		String password = mEditPsw.getText().toString();
		String pwd = mEditPswVal.getText().toString();
		
		if (!password.equals(pwd)) {
			mEditPswVal.setText("");
			mEditPswVal.setError("两次输入的密码不一致");
			return;
		}
		HTTPUtils.getVolley(SignUpActivity.this, 
				Constants.INTENT_KEY.INSURANCE_REGISTER + "user_name=" + username + "&pwd=" + password , 
				new VolleyListener() {
			
			@Override
			public void onErrorResponse(VolleyError arg0) {
				
			}
			
			@Override
			public void onResponse(String arg0) {
				registerResult = arg0;
				
			}
		});
	}

	private void signup() {
		String userName = mEditUser.getText().toString();
		String pwd = mEditPsw.getText().toString();
		String pwdVal = mEditPswVal.getText().toString();

		if (!pwd.equals(pwdVal)) {
			mEditPswVal.setText("");
			mEditPswVal.setError("两次输入的密码不一致");
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", userName);
		map.put("password", pwd);
		map.put("confirm_password", pwdVal);
		HTTPUtils.postVolley(SignUpActivity.this, Constants.URL.SIGN_UP, map,
				new VolleyListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

					}

					@Override
					public void onResponse(String result) {
						Log.e("sign", result);
						signResult = result;
						handler.sendEmptyMessage(1);
						
					}
				});
		// JDUser user = new JDUser();
		// user.setUsername(userName);
		// user.setPassword(pwd);
		// user.signUp(this, new SaveListener() {
		// public void onSuccess() {
		// Toast.makeText(SignUpActivity.this, "注册成功， 请登录",
		// Toast.LENGTH_LONG).show();
		// finish();
		// }
		//
		// public void onFailure(int arg0, String arg1) {
		// mEditUser.setError("用户名已存在");
		// }
		// });
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;
		case R.id.btn_sign_up:
//			signup();
			register();
			break;

		default:
			break;
		}
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				try {
					JSONObject ob = new JSONObject(signResult);
					String flag = ob.getString("jg");
					if (flag.equals("1")) {
						String uid = ob.getString("user_id");
						Toast.makeText(SignUpActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
						SharedPreferences sp = getSharedPreferences("MyPrefer", Context.MODE_PRIVATE);
						Editor editor = sp.edit();
						editor.putString("uid", uid);
						editor.commit();
						Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}else {
						Toast.makeText(SignUpActivity.this, flag, Toast.LENGTH_SHORT).show();
					}
					
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			case 2:
				try {
					JSONObject object = new JSONObject(registerResult);
					String result = object.getString("code");
					if (result.equals("001")) {
						Toast.makeText(SignUpActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
						finish();
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
