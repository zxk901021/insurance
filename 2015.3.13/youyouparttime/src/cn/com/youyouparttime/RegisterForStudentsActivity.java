package cn.com.youyouparttime;

import java.util.Timer;
import java.util.TimerTask;

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
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterForStudentsActivity extends Activity implements OnClickListener, OnTouchListener, OnCheckedChangeListener{

	private LinearLayout back;
	private EditText phoneNumber;
	private EditText verifyCode;
	private Button sendVerifyCode;
	private EditText password;
	private ImageView seePassword;
	private Button register;
	private String phoneStr;
	private String verifyStr;
	private String passwordStr;
	private TextView registerTitle;
	JSONObject object,resultJson;
	String result,msg,uid;
	SharedPreferences preferences;
	Editor editor;
	private CheckBox agreeRules;
	private boolean isAgree = false;
	private int verifyCount;
	public static final int COMPANY_MODE = 2;
	private int mode;
	private int time = 60;
	private Timer timer;
	private TextView rules;
	
	 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_for_students);
		SysApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		mode = intent.getIntExtra("launchMode", 0);
		initView();
	}
	public void initView(){
		registerTitle = (TextView) findViewById(R.id.register_title);
		if (mode == COMPANY_MODE) {
			registerTitle.setText("企业注册");
		}
		rules = (TextView) findViewById(R.id.text_rules);
		back = (LinearLayout) findViewById(R.id.back_to_login);
		phoneNumber = (EditText) findViewById(R.id.register_name);
		verifyCode = (EditText) findViewById(R.id.register_verify_code);
		sendVerifyCode = (Button) findViewById(R.id.send_verify);
		password = (EditText) findViewById(R.id.register_pass);
		register = (Button) findViewById(R.id.register);
		seePassword = (ImageView) findViewById(R.id.register_student_can_see);
		agreeRules = (CheckBox) findViewById(R.id.register_check);
		if (mode == COMPANY_MODE) {
			preferences = getSharedPreferences("companyPrefer", 0);
		}else {
			preferences = getSharedPreferences("myPrefer", 0);
		}
		editor = preferences.edit();
		
		rules.setOnClickListener(this);
		sendVerifyCode.setOnClickListener(this);
		seePassword.setOnTouchListener(this);
		register.setOnClickListener(this);
		back.setOnClickListener(this);
		agreeRules.setOnCheckedChangeListener(this);
		
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			  if(msg.what==0){
				  sendVerifyCode.setClickable(true);
				  sendVerifyCode.setText("发送验证码");
				  sendVerifyCode.setBackgroundResource(R.drawable.verrify_btn_shape);
                  timer.cancel();
                  }else{
                	  sendVerifyCode.setText("发送中" + msg.what+"秒");
                	  sendVerifyCode.setBackgroundResource(R.drawable.verify_btn_press_shape);
                  }
		};
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.text_rules:
			Intent rulesIntent = new Intent(RegisterForStudentsActivity.this,
					YouDetailActivity.class);
			rulesIntent.putExtra("title", "用户协议");
			rulesIntent.putExtra("catid", "27");
			startActivity(rulesIntent);
			break;
		
		case R.id.back_to_login:
//			Intent intent = new Intent(RegisterForStudentsActivity.this, LoginForStudentsActivity.class);
//			startActivity(intent);
			finish();
			break;
			
		case R.id.send_verify:
			int length = phoneNumber.getText().length();
			if (length == 0) {
				Toast.makeText(RegisterForStudentsActivity.this, "请先输入您的手机号", Toast.LENGTH_SHORT).show();
				return ;
				
			}
			time = 60;
			sendVerifyCode.setClickable(false);
			timer = new Timer();
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					handler.sendEmptyMessage(time--);
				}
			}, 0, 1000);
			
			try {
				JSONObject object = new JSONObject();
				String number = phoneNumber.getText().toString();
				object.put("mobile", number);
				Log.e("number", object.toString());
				String result = HttpUtil.postRequst(UrlUtil.VERIFY_URL, object);
				String msg = new JSONObject(result).getString("msg");
				Toast.makeText(RegisterForStudentsActivity.this, msg, Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			break;

		case R.id.register:
			isAgree = agreeRules.isChecked();
			verifyCount = verifyCode.getText().length();
			if (TextUtils.isEmpty(phoneNumber.getText())) {
				Toast.makeText(RegisterForStudentsActivity.this, "手机号码错误", Toast.LENGTH_SHORT).show();
				break;
			}else if (TextUtils.isEmpty(verifyCode.getText()) || verifyCount != 6) {
				Toast.makeText(RegisterForStudentsActivity.this, "请输入6位整数的验证码", Toast.LENGTH_SHORT).show();
				break;
			}else if (TextUtils.isEmpty(password.getText())) {
				Toast.makeText(RegisterForStudentsActivity.this, "密码请输入由英文字符、数字或下划线组成的6~16位密码", Toast.LENGTH_SHORT).show();
				break;
			}else {
				if (isAgree) {

					phoneStr = phoneNumber.getText().toString();
					verifyStr = verifyCode.getText().toString();
					passwordStr = password.getText().toString();
					String type = mode == COMPANY_MODE ? "2" : "1";
					object = strToJson(phoneStr, passwordStr, verifyStr, type);
					try {
						
//						result = HttpUtil.postRequst(UrlUtil.registerurl, object);
//						Log.e("result!!!",result);
						resultJson = new JSONObject(HttpUtil.postRequst(UrlUtil.REGISTERURL, object));
						result = resultJson.getString("result");
						Log.e("result!!!",result+"");
						msg = resultJson.getString("msg");
						
						if (result.equals("true")) {
							uid = resultJson.getString("id");
							Toast.makeText(RegisterForStudentsActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
							if (mode == COMPANY_MODE) {
								Intent comIntent = new Intent(RegisterForStudentsActivity.this, CompanyInfoSubmitActivity.class);
								startActivity(comIntent);
							}else {
								Intent goback = new Intent(RegisterForStudentsActivity.this, PartTimeActivity.class);
//								goback.putExtra("username", phoneStr);
//								goback.putExtra("password", passwordStr);
//								startActivityForResult(goback, 0);
								startActivity(goback);
							}
							int login = mode == COMPANY_MODE ? 2 : 1;
							editor.putString("uid", uid);
							editor.putBoolean("hasSubmit", false);
							editor.putInt("isLogin", login);
							editor.putString("username", phoneStr);
							editor.putString("password", passwordStr);
							editor.commit();
							SharedPreferences preferences = getSharedPreferences("myPrefer", 0);
							Editor edit = preferences.edit();
							edit.putInt("isLogin", login);
							edit.commit();
							finish();
						}else {
							Toast.makeText(RegisterForStudentsActivity.this, msg, Toast.LENGTH_SHORT).show();
						}
						
//						
//						String string = HttpUtil.postRequst(UrlUtil.registerurl, object);
//						Log.e("result!!!", string);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else {
					Toast.makeText(RegisterForStudentsActivity.this, "请先同意相关协议", Toast.LENGTH_SHORT).show();
				}
			}
			
			break;
		}
	}
	
	public JSONObject strToJson(String username, String password, String mcode, String usertype){
		
		try {
			JSONObject object = new JSONObject();
			object.put("username", username);
			object.put("password", password);
			object.put("usertype", usertype);
			object.put("mcode", mcode);
			return object;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			password.setSelection(password.getText().length());
			break;

		case MotionEvent.ACTION_UP:
			password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
			password.setSelection(password.getText().length());
			break;
		}
		return true;
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			isAgree = true;
		}else {
			isAgree = false;
		}
	}
	
	
	@Override
	protected void onDestroy() {
		if (timer != null) {
			timer.cancel();
		}
		super.onDestroy();
	}
}
