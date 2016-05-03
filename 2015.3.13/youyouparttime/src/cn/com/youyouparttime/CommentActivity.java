package cn.com.youyouparttime;

import org.json.JSONObject;

import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.HttpUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CommentActivity extends Activity implements OnClickListener {

	private LinearLayout back;
	private EditText editComment;
	private RadioGroup group;
	private TextView submit, commentTitle;
	String uid, usertype, jobid, cid, reason, content, text;
	int type;
	private TextView warn;
	private RelativeLayout checkLayout;
	private String commentType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		SysApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		type = intent.getIntExtra("type", -1);
		uid = intent.getStringExtra("uid");
		usertype = intent.getStringExtra("usertype");
		jobid = intent.getStringExtra("jobid");
		cid = intent.getStringExtra("cid");
		text = intent.getStringExtra("text");
		initView();
		submit.setText(type == 0 ? "发表" : "提交");
		commentTitle.setText(type == 0 ? "发表评论" : "投诉");
		if (type == 1) {
			editComment.setText(text);

		}
		editComment.setSelection(editComment.getText().toString().length());
		warn.setVisibility(type == 0 ? View.GONE : View.VISIBLE);
		checkLayout.setVisibility(type == 0 ? View.VISIBLE : View.GONE);
	}

	public void initView() {
		back = (LinearLayout) findViewById(R.id.comment_back);
		editComment = (EditText) findViewById(R.id.comment_content);
		submit = (TextView) findViewById(R.id.submit);
		commentTitle = (TextView) findViewById(R.id.comment_title);
		warn = (TextView) findViewById(R.id.comment_warn);
		checkLayout = (RelativeLayout) findViewById(R.id.comment_check_layout);
		group = (RadioGroup) findViewById(R.id.comment_group);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.comment_good:
					commentType = "5";
					break;

				case R.id.comment_normal:
					commentType = "6";
					break;
				case R.id.comment_bad:
					commentType = "7";
					break;
				}
			}
		});
		back.setOnClickListener(this);
		submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comment_back:
			finish();
			break;

		case R.id.submit:
			if (type == 0) {
				if (commentType == null) {
					Toast.makeText(CommentActivity.this, "请选择评价类别",
							Toast.LENGTH_SHORT).show();
					return;
				}
				submitContent(UrlUtil.SUBMIT_COMMENT_URL, "评论成功");
			} else {
				submitContent(UrlUtil.SUBMIT_COMPLAIN_URL, "投诉成功");
			}
			break;
		}
	}

	public void submitContent(String url, String info) {
		String comment = editComment.getText().toString();
		JSONObject object = new JSONObject();
		try {
			object.put("uid", uid);
			object.put("usertype", usertype);
			object.put("cid", cid);
			object.put("jobid", jobid);
			object.put("content", comment);
			object.put("reason", commentType);
			Log.e("ids", object.toString());
			String result = HttpUtil.postRequst(url, object);
			JSONObject resultJson = new JSONObject(result);
			String data = resultJson.getString("result");
			String msg = resultJson.getString("msg");
			if (data.equals("true")) {
				Toast.makeText(CommentActivity.this, info, Toast.LENGTH_LONG)
						.show();
				finish();
			} else {
				Toast.makeText(CommentActivity.this, msg, Toast.LENGTH_LONG)
						.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
