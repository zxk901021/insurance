package cn.com.youyouparttime;

import cn.com.youyouparttime.base.SysApplication;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class CompanyReleaseActivity extends Activity implements OnClickListener{

	private RelativeLayout releaseNewJob;
	private RelativeLayout searchResume;
	private RelativeLayout entrustRelease;
	private LinearLayout back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_release);
		SysApplication.getInstance().addActivity(this);
		initView();
	}

	public void initView(){
		releaseNewJob = (RelativeLayout) findViewById(R.id.company_release_new_parttime);
		searchResume = (RelativeLayout) findViewById(R.id.company_search_resume);
		entrustRelease = (RelativeLayout) findViewById(R.id.company_entrust);
		back = (LinearLayout) findViewById(R.id.company_release_jobs_back);
		back.setOnClickListener(this);
		entrustRelease.setOnClickListener(this);
		searchResume.setOnClickListener(this);
		releaseNewJob.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.company_release_jobs_back:
			finish();
			break;
			
		case R.id.company_release_new_parttime:
			Intent releaseIntent = new Intent(CompanyReleaseActivity.this, ReleaseNewJobActivity.class);
			releaseIntent.putExtra("is_wt", "0");
			startActivity(releaseIntent);
			break;

		case R.id.company_search_resume:
			Intent searchResumeIntent = new Intent(CompanyReleaseActivity.this, CompanySearchResumeActivity.class);
			startActivity(searchResumeIntent);
			break;
			
		case R.id.company_entrust:
			Intent entruseIntent = new Intent(CompanyReleaseActivity.this, ReleaseNewJobActivity.class);
			entruseIntent.putExtra("is_wt", "1");
			startActivity(entruseIntent);
			break;
		}
	}
}
