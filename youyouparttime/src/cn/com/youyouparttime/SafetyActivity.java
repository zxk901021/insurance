package cn.com.youyouparttime;

import cn.com.youyouparttime.base.SysApplication;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SafetyActivity extends Activity implements OnClickListener {

	private RelativeLayout addVip, safetyLecture, legalAid,
			commercialInsurance, compensateLayout;
	private TextView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safety);
		SysApplication.getInstance().addActivity(this);
		initView();
	}

	public void initView() {
		addVip = (RelativeLayout) findViewById(R.id.add_vip_layout);
		safetyLecture = (RelativeLayout) findViewById(R.id.safety_lecture_layout);
		legalAid = (RelativeLayout) findViewById(R.id.safety_lecture_layout2);
		commercialInsurance = (RelativeLayout) findViewById(R.id.commercial_insurance_layout);
		compensateLayout = (RelativeLayout) findViewById(R.id.compensation_layout);
		back = (TextView) findViewById(R.id.safety_back);
		addVip.setOnClickListener(this);
		safetyLecture.setOnClickListener(this);
		legalAid.setOnClickListener(this);
		commercialInsurance.setOnClickListener(this);
		compensateLayout.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_vip_layout:
			Intent vipIntent = new Intent(SafetyActivity.this, UpVipActivity.class);
			startActivity(vipIntent);
			break;

		case R.id.safety_lecture_layout:
			gotoDetail("兼职安全讲堂","19");
			break;
			
		case R.id.safety_lecture_layout2:
			gotoDetail("法律援助", "22");
			break;
			
		case R.id.commercial_insurance_layout:
			gotoDetail("商业保险", "23");
			break;
			
		case R.id.compensation_layout:
			gotoDetail("先行赔付", "24");
			break;
			
		case R.id.safety_back:
			finish();
			break;
		}
	}
	
	public void gotoDetail(String title,String catid){
		Intent aboutIntent = new Intent(SafetyActivity.this,
				YouDetailActivity.class);
		aboutIntent.putExtra("title", title);
		aboutIntent.putExtra("catid", catid);
		startActivity(aboutIntent);
	}
}
