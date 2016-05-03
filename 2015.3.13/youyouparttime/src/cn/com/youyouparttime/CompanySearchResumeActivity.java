package cn.com.youyouparttime;


import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.util.CommonUtil;
import cn.com.youyouparttime.util.DialogUtil;
import cn.com.youyouparttime.util.DialogUtil.DialogListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CompanySearchResumeActivity extends Activity implements
		OnClickListener {

	private LinearLayout back;
	private RelativeLayout type, area, time, healthCard, height, school, attent;
	private TextView typeContent, areaContent, timeContent, schoolContent,
			heightContent, healthContent;
	private String typeKey, areaKey, timeKey, schoolStr, schoolKey, max, min,
			healthyStr;
	private Button search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_search_resume);
		SysApplication.getInstance().addActivity(this);
		initView();
	}

	public void initView() {
		back = (LinearLayout) findViewById(R.id.company_search_resume_back);
		type = (RelativeLayout) findViewById(R.id.search_resume_type);
		area = (RelativeLayout) findViewById(R.id.search_resume_area);
		time = (RelativeLayout) findViewById(R.id.search_resume_time);
		healthCard = (RelativeLayout) findViewById(R.id.search_resume_healthy);
		height = (RelativeLayout) findViewById(R.id.search_resume_height);
		school = (RelativeLayout) findViewById(R.id.search_resume_school);
		attent = (RelativeLayout) findViewById(R.id.search_resume_job_attent);
		typeContent = (TextView) findViewById(R.id.search_resume_type_content);
		areaContent = (TextView) findViewById(R.id.search_resume_area_content);
		timeContent = (TextView) findViewById(R.id.search_resume_time_content);
		search = (Button) findViewById(R.id.start_search_resume);
		schoolContent = (TextView) findViewById(R.id.search_resume_school_content);
		heightContent = (TextView) findViewById(R.id.search_resume_height_content);
		healthContent = (TextView) findViewById(R.id.search_resume_healthy_content);
		back.setOnClickListener(this);
		search.setOnClickListener(this);
		type.setOnClickListener(this);
		area.setOnClickListener(this);
		time.setOnClickListener(this);
		attent.setOnClickListener(this);
		healthCard.setOnClickListener(this);
		height.setOnClickListener(this);
		school.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.company_search_resume_back:
			finish();
			break;

		case R.id.search_resume_type:
			Intent type = new Intent(CompanySearchResumeActivity.this,
					ChooseJobListActivity.class);
			type.putExtra("flag", 0);
			type.putExtra("tag", 1);
			type.putExtra("param", "jobclass");
			startActivityForResult(type, 100);
			break;
		case R.id.search_resume_area:
			Intent area = new Intent(CompanySearchResumeActivity.this,
					ChooseJobListActivity.class);
			area.putExtra("flag", 0);
			area.putExtra("tag", 2);
			area.putExtra("param", "province");
			startActivityForResult(area, 100);
			break;
		case R.id.search_resume_time:
			Intent time = new Intent(CompanySearchResumeActivity.this,
					ChooseJobListActivity.class);
			time.putExtra("flag", 1);
			time.putExtra("tag", 3);
			time.putExtra("param", "city");
			time.putExtra("time", timeKey);
			startActivityForResult(time, 100);
			break;
		case R.id.start_search_resume:
			Intent searchIntent = new Intent(CompanySearchResumeActivity.this,
					ResumeListActivity.class);
			searchIntent.putExtra("jobclass", typeKey);
			searchIntent.putExtra("cityclass", areaKey);
			searchIntent.putExtra("jobtime", timeKey);
			searchIntent.putExtra("min-height", min);
			searchIntent.putExtra("max-height", max);
			searchIntent.putExtra("school", schoolKey);
			searchIntent.putExtra("jk_card", healthyStr);
			startActivity(searchIntent);
			// JSONObject object = new JSONObject();
			// try {
			// object.put("jobclass", typeKey);
			// object.put("cityclass", areaKey);
			// object.put("jobtime", timeKey);
			// object.put("min-height", "");
			// object.put("max-height", "");
			// object.put("school", "");
			// object.put("jk_card", "");
			// String result = HttpUtil.postRequst(UrlUtil.SEARCH_JOB_URL,
			// object);
			// String msg = new JSONObject(result).getString("msg");
			// Toast.makeText(CompanySearchResumeActivity.this, msg,
			// Toast.LENGTH_SHORT).show();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			 break;

		case R.id.search_resume_healthy:
			DialogUtil.healthDialog(this, new DialogListener() {

				@Override
				public void refreshUI(String string) {
					healthyStr = string;
					healthContent.setText(string);
				}

				@Override
				public void refreshUI(String string, String key) {
					
				}
			});
			break;

		case R.id.search_resume_school:
			Intent schoolIntent = new Intent(CompanySearchResumeActivity.this,
					SchoolMajorActivity.class);
			schoolIntent.putExtra("flag", 21);
			startActivityForResult(schoolIntent, 21);
			break;

		case R.id.search_resume_height:
			Intent intent = new Intent(CompanySearchResumeActivity.this,
					HeightActivity.class);
			startActivityForResult(intent, 100);
			break;
			
		case R.id.search_resume_job_attent:
			Intent atten = new Intent(CompanySearchResumeActivity.this,
					ChooseJobListActivity.class);
			atten.putExtra("flag", 0);
			atten.putExtra("tag", 1);
			atten.putExtra("param", "jobclass");
			startActivityForResult(atten, 100);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("TAG", "onActivityResult");
		switch (resultCode) {
		case 1:
			String jobType = data.getExtras().getString("value");
			typeKey = data.getExtras().getString("key");
			typeContent.setText(jobType);
			break;

		case 2:
			String jobArea = data.getExtras().getString("value");
			areaKey = data.getExtras().getString("key");
			areaContent.setText(jobArea);
			Log.e("key", areaKey);
			break;

		case 3:
			String jobTime = data.getExtras().getString("value");
			timeKey = jobTime;
			timeContent.setText(CommonUtil.strToInt(jobTime));
			break;

		case 9:
			schoolStr = data.getExtras().getString("value");
			schoolKey = data.getExtras().getString("key");
			schoolContent.setText(schoolStr);
			schoolContent.setTextColor(Color.BLACK);
			break;
		case 10:
			max = data.getExtras().getString("max");
			min = data.getExtras().getString("min");
			if (max != null && min != null && max.length() > 0
					&& min.length() > 0) {
				heightContent.setText(min + " ~ " + max + " cm");
			} else {
				heightContent.setText("²»ÏÞ");
			}
			
		case 21:
				schoolStr = data.getStringExtra("value");
				schoolContent.setText(schoolStr);
			break;
			// case 5:
			// String jobSalary = data.getExtras().getString("value");
			// salaryKey = data.getExtras().getString("key");
			// salaryContent.setText(jobSalary);
			// break;
		}
	}
}
