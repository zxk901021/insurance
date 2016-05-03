package cn.com.youyouparttime;

import org.json.JSONObject;

import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.Constant;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.CommonUtil;
import cn.com.youyouparttime.util.HttpUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MsgDetailActivity extends Activity implements OnClickListener {

	private LinearLayout back;
	private TextView deleteMsg;
	private TextView msgTitle;
	private TextView msgTime;
	private TextView msgContent;
	private TextView msgAnnotation;
	private Button msgBtn1;
	private Button msgBtn2;
	private String msgid, type;
	private String title, time, content, annotation;
	private String jobId;
	private SharedPreferences share;
	private String uid,sqid,cid;
	private String isAgree;
	private String userType;
	private String userId;
	private String type2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg_detail);
		SysApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		msgid = intent.getStringExtra("msgid");
		type = intent.getStringExtra("type");
		userType = intent.getStringExtra("usertype");
		type2 = intent.getStringExtra("type2");
		share = getSharedPreferences(Constant.COMPANY_PREFER, 0);
		uid = share.getString("uid", null);
		getData();
		initView();
		setData();
	}

	public void initView() {
		back = (LinearLayout) findViewById(R.id.msg_detail_back);
		deleteMsg = (TextView) findViewById(R.id.delete_msg);
		msgTitle = (TextView) findViewById(R.id.msg_detail_title);
		msgTime = (TextView) findViewById(R.id.msg_detail_time);
		msgContent = (TextView) findViewById(R.id.msg_detail_content);
		msgAnnotation = (TextView) findViewById(R.id.detail_text_annotation);
		msgBtn1 = (Button) findViewById(R.id.detail_btn1);
		msgBtn2 = (Button) findViewById(R.id.detail_btn2);
		back.setOnClickListener(this);
		deleteMsg.setOnClickListener(this);
		msgBtn1.setOnClickListener(this);
		msgBtn2.setOnClickListener(this);
		if (type.equals("recruit")) {
			msgBtn1.setVisibility(View.VISIBLE);
			msgBtn2.setVisibility(View.VISIBLE);
			msgBtn1.setText("查看岗位详情");
			msgBtn2.setText("接受邀请");
		}
		if (type.equals("sq")) {
			msgBtn1.setVisibility(View.VISIBLE);
			msgAnnotation.setVisibility(View.VISIBLE);
			msgBtn1.setText("查看简历详情");
		}
		if (type.equals("zl")) {
			msgBtn1.setVisibility(View.VISIBLE);
			msgBtn2.setVisibility(View.VISIBLE);
			msgBtn1.setText("工资属实");
			msgBtn2.setText("工资不属实");
		}
		if (type.equals("reserve")) {
			msgBtn1.setVisibility(View.VISIBLE);
			msgAnnotation.setVisibility(View.VISIBLE);
			msgBtn1.setText("查看简历详情");
		}
		if (type.equals("contact")) {
			if (type2.equals("2")) {
				msgBtn1.setVisibility(View.VISIBLE);
				msgBtn1.setText("同意");
				msgBtn2.setVisibility(View.VISIBLE);
				msgBtn2.setText("拒绝");
			}else if (type2.equals("1")) {
				if (isAgree != null) {
					if (isAgree.equals("2")) {
						msgBtn1.setVisibility(View.VISIBLE);
						msgBtn1.setText("查看简历详情");
					}
			}
			
			}
			
		}
	}

	public void getData() {
		JSONObject object = new JSONObject();
		JSONObject infoJson = new JSONObject();
		JSONObject msgJson = new JSONObject();
		if (type.equals("recruit")) {
			try {
				object.put("msgid", msgid);
				String result = HttpUtil.postRequst(
						UrlUtil.INTERVIEW_MSG_DETAIL_URL, object);
				infoJson = new JSONObject(result);
				msgJson = infoJson.getJSONObject("msg_show");
				title = msgJson.getString("title");
				time = msgJson.getString("datetime");
				jobId = msgJson.getString("jobid");
				content = msgJson.getString("name");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if (type.equals("comment")) {
			try {
				object.put("msgid", msgid);
				Log.e("comment", object.toString());
				String result = HttpUtil.postRequst(
						UrlUtil.CHECK_COMPANY_COMMENT_DETAIL_URL, object);
				infoJson = new JSONObject(result);
				msgJson = infoJson.getJSONObject("msg_show");
				title = msgJson.getString("title");
				time = msgJson.getString("datetime");
				content = msgJson.getString("content");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (type.equals("sys")) {
			try {
				object.put("msgid", msgid);
				Log.e("tostring", object.toString());
				String result = HttpUtil.postRequst(
						UrlUtil.SYSTEM_MSG_DETAIL_URL, object);
				infoJson = new JSONObject(result);
				msgJson = infoJson.getJSONObject("msg_show");
				title = msgJson.getString("title");
				time = msgJson.getString("datetime");
				content = msgJson.getString("content");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (type.equals("sq")) {
			try {
				object.put("msgid", msgid);
				String result = HttpUtil.postRequst(UrlUtil.APPLY_JOB_URL,
						object);
				infoJson = new JSONObject(result);
				msgJson = infoJson.getJSONObject("msg_show");
				title = msgJson.getString("title");
				time = msgJson.getString("datetime");
				// content = msgJson.getString("content");
				annotation = msgJson.getString("job_name");
				userId = msgJson.getString("uid");
				sqid = msgJson.getString("cid");
				jobId = msgJson.getString("jobid");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (type.equals("zl")) {
			try {
				object.put("msgid", msgid);
				String result = HttpUtil.postRequst(
						UrlUtil.ZILI_STAR_URL, object);
				infoJson = new JSONObject(result);
				msgJson = infoJson.getJSONObject("msg_show");
				title = msgJson.getString("title");
				time = msgJson.getString("datetime");
				content = msgJson.getString("content");
//				zlId = msgJson.getString("zlid");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (type.equals("reserve")) {
			try {
				object.put("msgid", msgid);
				String result = HttpUtil.postRequst(
						UrlUtil.CHECK_INTERVIEW_URL, object);
				infoJson = new JSONObject(result);
				msgJson = infoJson.getJSONObject("msg_show");
				title = msgJson.getString("title");
				time = msgJson.getString("datetime");
				content = msgJson.getString("content");
//				zlId = msgJson.getString("zlid");
				cid = msgJson.getString("cid");
				jobId = msgJson.getString("jobid");
				userId = msgJson.getString("uid");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (type.equals("contact")) {
			try {
				object.put("msgid", msgid);
				String result = null;
				if (type2.equals("1")) {
					result = HttpUtil.postRequst(
							UrlUtil.CHECK_CONTACT_URL, object);
				}else {
					result = HttpUtil.postRequst(
							UrlUtil.STUDENT_CONTACT_INFO, object);
				}
				
				infoJson = new JSONObject(result);
				msgJson = infoJson.getJSONObject("msg_show");
				title = msgJson.getString("title");
				time = msgJson.getString("datetime");
//				content = msgJson.getString("content");
				isAgree = msgJson.getString("audit");
				userId = msgJson.getString("uid");
				sqid = msgJson.getString("cid");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setData() {
		msgTitle.setText(CommonUtil.nullToEmpty(title));
		msgTime.setText(CommonUtil.nullToEmpty(time));
		msgContent.setText(CommonUtil.nullToEmpty(content));
		if (type.equals("sq")) {
			msgAnnotation.setText("应聘岗位：" + annotation);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.msg_detail_back:
			finish();
			break;

		case R.id.detail_btn1:
			if (type.equals("recruit")) {
				Intent checkIntent = new Intent(MsgDetailActivity.this,
						PartTimeDetailActivity.class);
				checkIntent.putExtra("id", jobId);
				startActivity(checkIntent);
			}
			if (type.equals("zl")) {
				JSONObject object = new JSONObject();
				try {
					object.put("zlid", msgid);
					object.put("audit", "1");
					String result = HttpUtil.postRequst(UrlUtil.REWARD_IS_TRUE_URL, object);
					String msg = new JSONObject(result).getString("msg");
					Toast.makeText(MsgDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
					String a = new JSONObject(result).getString("result");
					if (a.equals("true")) {
						finish();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (type.equals("sq")) {
				Intent sqIntent = new Intent(MsgDetailActivity.this, CheckResumeActivity.class);
				sqIntent.putExtra("uid", userId);
				sqIntent.putExtra("sqid", sqid);
				sqIntent.putExtra("jobid", jobId);
				startActivity(sqIntent);
			}
			if (type.equals("contact")) {
				if (type2.equals("2")) {
					JSONObject object = new JSONObject();
					try {
						object.put("uid", userId);
						object.put("sqid", sqid);
						object.put("audit", "2");
						String result = HttpUtil.postRequst(UrlUtil.STUDENT_AGREE_CHECK, object);
						String msg = new JSONObject(result).getString("msg");
						Toast.makeText(MsgDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
						String a = new JSONObject(result).getString("result");
						if (a.equals("true")) {
							finish();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}
				Intent contactIntent = new Intent(MsgDetailActivity.this, CheckResumeActivity.class);
				contactIntent.putExtra("uid", userId);
				contactIntent.putExtra("sqid", sqid);
				startActivity(contactIntent);
			}
			if (type.equals("reserve")) {
				Intent reserveIntent = new Intent(MsgDetailActivity.this, CheckResumeActivity.class);
				reserveIntent.putExtra("uid", userId);
				reserveIntent.putExtra("sqid", cid);
				reserveIntent.putExtra("jobid", jobId);
				startActivity(reserveIntent);
			}

			break;

		case R.id.detail_btn2:
			if (type.equals("recruit")) {
				JSONObject object = new JSONObject();
				try {
					object.put("msgid", msgid);
					String result = HttpUtil.postRequst(
							UrlUtil.RESERVE_INTERVIEW_URL, object);
					String msg = new JSONObject(result).getString("msg");
					Toast.makeText(MsgDetailActivity.this, msg,
							Toast.LENGTH_SHORT).show();
					String a = new JSONObject(result).getString("result");
					if (a.equals("true")) {
						finish();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if (type.equals("zl")) {
				JSONObject object = new JSONObject();
				try {
					object.put("zlid", msgid);
					object.put("audit", "2");
					String result = HttpUtil.postRequst(UrlUtil.REWARD_IS_TRUE_URL, object);
					String msg = new JSONObject(result).getString("msg");
					Toast.makeText(MsgDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
					String a = new JSONObject(result).getString("result");
					if (a.equals("true")) {
						finish();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (type.equals("contact")) {
				if (type2.equals("2")) {
					JSONObject object = new JSONObject();
					try {
						object.put("uid", userId);
						object.put("sqid", sqid);
						object.put("audit", "1");
						String result = HttpUtil.postRequst(UrlUtil.STUDENT_AGREE_CHECK, object);
						String msg = new JSONObject(result).getString("msg");
						Toast.makeText(MsgDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
						String a = new JSONObject(result).getString("result");
						if (a.equals("true")) {
							finish();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				Intent contactIntent = new Intent(MsgDetailActivity.this, CheckResumeActivity.class);
				contactIntent.putExtra("uid", userId);
				contactIntent.putExtra("sqid", sqid);
				startActivity(contactIntent);
			}
			break;
			
		case R.id.delete_msg:
			JSONObject object = new JSONObject();
			try {
				object.put("type", type);
				object.put("delid", msgid);
				object.put("uid", uid);
				object.put("usertype", userType);
				String result = HttpUtil.postRequst(UrlUtil.DELETE_MSG_URL, object);
				String msg = new JSONObject(result).getString("msg");
				Toast.makeText(MsgDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
				String a = new JSONObject(result).getString("result");
				if (a.equals("true")) {
					msgTitle.setVisibility(View.INVISIBLE);
					msgTime.setVisibility(View.INVISIBLE);
					msgContent.setVisibility(View.INVISIBLE);
					msgAnnotation.setVisibility(View.INVISIBLE);
					msgBtn1.setVisibility(View.INVISIBLE);
					msgBtn2.setVisibility(View.INVISIBLE);
					finish();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}
}
