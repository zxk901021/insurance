package cn.com.youyouparttime;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.HttpUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FindPasswordActivity extends Activity implements OnClickListener{

	private EditText phoneNumber;
	private EditText verifyCode;
	private Button sendCode;
	private Button next;
	private LinearLayout back;
	String username,code; 
	private int time = 60;
	private Timer timer;
	int mode;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_password);
		SysApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		mode = intent.getIntExtra("mode", -1);
		initView();
		
	}
	public void initView(){
		phoneNumber = (EditText) findViewById(R.id.find_phone_edt);
		verifyCode = (EditText) findViewById(R.id.find_verify_edt);
		sendCode = (Button) findViewById(R.id.find_send_verify);
		next = (Button) findViewById(R.id.find_next);
		back = (LinearLayout) findViewById(R.id.find_back);
		sendCode.setOnClickListener(this);
		next.setOnClickListener(this);
		back.setOnClickListener(this);
		
		
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			  if(msg.what==0){
				  sendCode.setClickable(true);
				  sendCode.setText("发送验证码");
				  sendCode.setBackgroundResource(R.drawable.verrify_btn_shape);
                  timer.cancel();
                  }else{
                	  sendCode.setText("发送中" + msg.what+"秒");
                	  sendCode.setBackgroundResource(R.drawable.verify_btn_press_shape);
                  }
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.find_back:
			finish();
			break;

		case R.id.find_send_verify:
			int length = phoneNumber.getText().length();
			if (length == 0) {
				Toast.makeText(FindPasswordActivity.this, "请先输入您的手机号", Toast.LENGTH_SHORT).show();
				return ;
				
			}
			time = 60;
			sendCode.setClickable(false);
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
				Toast.makeText(FindPasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			break;

		case R.id.find_next:
			if (TextUtils.isEmpty(phoneNumber.getText())) {
				Toast.makeText(FindPasswordActivity.this, "请填写手机号", Toast.LENGTH_SHORT).show();
			}else {
				username = phoneNumber.getText().toString();
			}
			if (TextUtils.isEmpty(verifyCode.getText())) {
				Toast.makeText(FindPasswordActivity.this, "请填写验证码", Toast.LENGTH_SHORT).show();
			}else {
				code = verifyCode.getText().toString();
			}
			JSONObject object = new JSONObject();
			try {
				object.put("username", username);
				object.put("mcode", code);
				String result = HttpUtil.postRequst(UrlUtil.FIND_PASSWORD_STEP1_URL, object);
				String msg = new JSONObject(result).getString("msg");
				String results = new JSONObject(result).getString("result");
				Toast.makeText(FindPasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
				if (results.equals("true")) {
					Intent intent = new Intent(FindPasswordActivity.this, FindPassword2Activity.class);
					intent.putExtra("username", username);
					intent.putExtra("mode", mode);
					startActivity(intent);
					finish();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
			break;

		}
	}

	
}
