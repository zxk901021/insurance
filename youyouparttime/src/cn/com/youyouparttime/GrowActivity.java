package cn.com.youyouparttime;

import org.json.JSONObject;

import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.Constant;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.HttpUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GrowActivity extends Activity implements OnClickListener{

	private RelativeLayout trainLayout;
	private RelativeLayout internshipLayout;
	private RelativeLayout makeFriendsLayout;
	private TextView back;
	private Button applyInternship;
	private SharedPreferences share;
	private String uid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grow);
		SysApplication.getInstance().addActivity(this);
		trainLayout = (RelativeLayout) findViewById(R.id.train_layout);
		back = (TextView) findViewById(R.id.grow_back);
		internshipLayout = (RelativeLayout) findViewById(R.id.internship_layout);
		makeFriendsLayout = (RelativeLayout) findViewById(R.id.make_friend_layout);
		applyInternship = (Button) findViewById(R.id.apply_internship_btn);
		share = getSharedPreferences(Constant.STUDENT_PREFER, 0);
		uid = share.getString("uid", null);
		applyInternship.setOnClickListener(this);
		internshipLayout.setOnClickListener(this);
		makeFriendsLayout.setOnClickListener(this);
		back.setOnClickListener(this);
		trainLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.train_layout:
			Intent trainIntent = new Intent(GrowActivity.this,
					YouDetailActivity.class);
			trainIntent.putExtra("title", "兼职培训");
			trainIntent.putExtra("catid", "25");
			startActivity(trainIntent);
			break;

		case R.id.grow_back:
			finish();
			break;
			
		case R.id.internship_layout:
			if (applyInternship.getVisibility() == View.GONE) {
				applyInternship.setVisibility(View.VISIBLE);
			}else {
				applyInternship.setVisibility(View.GONE);
			}
			
			break;
			
		case R.id.make_friend_layout:
			Intent makeIntent = new Intent(GrowActivity.this,
					YouDetailActivity.class);
			makeIntent.putExtra("title", "交友活动");
			makeIntent.putExtra("catid", "26");
			startActivity(makeIntent);
			break;
			
		case R.id.apply_internship_btn:
			JSONObject object = new JSONObject();
			try {
				object.put("uid", uid);
				String result = HttpUtil.postRequst(UrlUtil.APPLY_INTERNSHIP_URL, object);
				String msg = new JSONObject(result).getString("msg");
				Toast.makeText(GrowActivity.this, msg, Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

}
