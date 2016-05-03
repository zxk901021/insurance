package cn.com.youyouparttime;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.Constant;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.HttpUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ResumeChooseItemActivity extends Activity implements OnClickListener{

	
	private TextView back;
	private TextView submit;
	private EditText newPhone;
	private EditText verifyCode;
	private Button sendVerifyCode;
	private int time = 60;
	private Timer timer;
	private SharedPreferences share;
	private Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resume_choose_item);
		SysApplication.getInstance().addActivity(this);
		back = (TextView) findViewById(R.id.resume_item_choose_back);
		submit = (TextView) findViewById(R.id.resume_item_submit);
		newPhone = (EditText) findViewById(R.id.phone_change_edt);
		verifyCode = (EditText) findViewById(R.id.choose_verify_edt);
		sendVerifyCode = (Button) findViewById(R.id.verify_btn);
		back.setOnClickListener(this);
		submit.setOnClickListener(this);
		sendVerifyCode.setOnClickListener(this);
		share = getSharedPreferences(Constant.STUDENT_PREFER, 0);
		editor = share.edit();
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
		case R.id.resume_item_choose_back:
			finish();
			break;

		case R.id.resume_item_submit:
			String code = verifyCode.getText().toString();
			int codeCount = code.length();
			if (TextUtils.isEmpty(newPhone.getText())) {
				Toast.makeText(ResumeChooseItemActivity.this, "手机号码错误", Toast.LENGTH_SHORT).show();
				break;
			}else if (TextUtils.isEmpty(verifyCode.getText()) || codeCount != 6) {
				Toast.makeText(ResumeChooseItemActivity.this, "请输入6位整数的验证码", Toast.LENGTH_SHORT).show();
			
				break;
				}
			String phone = newPhone.getText().toString();
			String uid = share.getString("uid", null);
			JSONObject object = new JSONObject();
			try {
				object.put("uid", uid);
				object.put("mobile", phone);
				object.put("mcode", code);
				String result = HttpUtil.postRequst(UrlUtil.CHANGE_PHONE_URL, object);
				String info = new JSONObject(result).getString("result");
				String msg = new JSONObject(result).getString("msg");
				if (info.equals("true")) {
					Intent intent = new Intent();
					intent.putExtra("phone", phone);
					setResult(10, intent);
					editor.putString("username", phone);
					editor.commit();
					Toast.makeText(ResumeChooseItemActivity.this, msg, Toast.LENGTH_SHORT).show();
					finish();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			break;
			
		case R.id.verify_btn:
			int length = newPhone.getText().length();
			if (length == 0) {
				Toast.makeText(ResumeChooseItemActivity.this, "请先输入您的手机号", Toast.LENGTH_SHORT).show();
				return;
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
				JSONObject object2 = new JSONObject();
				String number = newPhone.getText().toString();
				object2.put("mobile", number);
				Log.e("number", object2.toString());
				String result = HttpUtil.postRequst(UrlUtil.VERIFY_URL, object2);
				String msg = new JSONObject(result).getString("msg");
				Toast.makeText(ResumeChooseItemActivity.this, msg, Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			break;
		}
	}

}
