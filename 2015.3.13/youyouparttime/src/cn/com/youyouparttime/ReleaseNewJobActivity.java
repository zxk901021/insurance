package cn.com.youyouparttime;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.Constant;
import cn.com.youyouparttime.entity.PartTimeInfo;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.CommonUtil;
import cn.com.youyouparttime.util.HttpUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ReleaseNewJobActivity extends Activity implements OnClickListener {

	private LinearLayout back;
	private TextView preview, ensure;
	private RelativeLayout type, time, area, salaryPayfor;
	private TextView typeContent, timeContent, areaContent, payUnit,
			salaryPayContent, companyNameEdt, companyIntroEdt,
			companyInchargeEdt, contactPhoneEdt, companyAdressEdt,
			tip;
	private String typeKey, areaKey, timeKey, salaryKey, jobTitle, companyName,
			huntNumber, jobIntro, jobDemand, interviewTime, interviewAddress,
			attention, companyIntro, companyIncharge, contactPhone,
			companyAdress, linkPerson, linkPhone, linkEmail, statement;
	private String uid, jobType, jobArea, jobTime, jobSalary;
	private SharedPreferences share;
	private EditText titleEdt, huntNumberEdt, jobIntroEdt, jobDemandEdt,
			interviewTimeEdt, interviewAddressEdt, attentionEdt, linkPersonEdt,
			linkPhoneEdt, linkEmailEdt, salaryContent,
			addressDetail;
	private PartTimeInfo info;
	private String isWt;
	private String companyNameStr, incharegePerson, contactPhoneStr, address,
			intro, salaryValue, addressDetailStr, salaryUnit, salaryPayValue,
			salaryPayKey;
	private String id;
	PartTimeInfo infos = new PartTimeInfo();
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_release_new_job);
		SysApplication.getInstance().addActivity(this);
		share = getSharedPreferences(Constant.COMPANY_PREFER, 0);
		uid = share.getString("uid", null);
		Intent intent = getIntent();
		isWt = intent.getStringExtra("is_wt");
		id = intent.getStringExtra("id");
		salaryKey = "47";

		companyNameStr = share.getString("companyname", "");
		incharegePerson = share.getString("person", "");
		contactPhoneStr = share.getString("phone", "");
		address = share.getString("address", "");
		intro = share.getString("intro", "");

		Log.e("uid", uid);
		init();
		if (id != null) {
			infos = CommonUtil.getDetailInfo(id, UrlUtil.JOB_DETAIL_URL);
			initValues();
		}
	}

	public void initValues() {
		titleEdt.setText(infos.getJobName());
		typeContent.setText(infos.getJobType());
		huntNumberEdt.setText(infos.getJobCount());
		salaryContent.setText(infos.getJobReward());
		timeContent.setText(CommonUtil.strToInt(infos.getJobWorkTime()));
		areaContent.setText(infos.getJobAddress());
		contactPhoneEdt.setText(infos.getCompanyPhone());
		linkPersonEdt.setText(infos.getContactPerson());
		linkPhoneEdt.setText(infos.getContactPhone());
	}

	public void init() {
		tip = (TextView) findViewById(R.id.release_job_info_statement);
		addressDetail = (EditText) findViewById(R.id.release_info_areacontent_detail);
		payUnit = (TextView) findViewById(R.id.release_info_pay_unit);
		interviewTimeEdt = (EditText) findViewById(R.id.release_info_interview_time_content);
		titleEdt = (EditText) findViewById(R.id.release_info_title_edit);
		companyNameEdt = (TextView) findViewById(R.id.release_info_company_name_edit);
		huntNumberEdt = (EditText) findViewById(R.id.release_info_people_count_edit);
		jobIntroEdt = (EditText) findViewById(R.id.release_job_info_intro_edit);
		jobDemandEdt = (EditText) findViewById(R.id.release_job_info_request_edit);
		interviewAddressEdt = (EditText) findViewById(R.id.release_job_info_interview_address_edit);
		attentionEdt = (EditText) findViewById(R.id.release_job_info_interview_attention_edit);
		companyIntroEdt = (TextView) findViewById(R.id.release_job_info_company_intro_edit);
		companyInchargeEdt = (TextView) findViewById(R.id.release_job_info_company_incharge_person_edit);
		contactPhoneEdt = (TextView) findViewById(R.id.release_job_info_company_contact_phone_edit);
		companyAdressEdt = (TextView) findViewById(R.id.release_job_info_interview_company_address_edit);
		linkPersonEdt = (EditText) findViewById(R.id.release_job_info_interview_contact_person_edit);
		linkPhoneEdt = (EditText) findViewById(R.id.release_job_info_interview_contact_phone_edit);
		linkEmailEdt = (EditText) findViewById(R.id.release_job_info_interview_contact_email_edit);
//		statementEdt = (EditText) findViewById(R.id.release_job_info_statement_edit);
		back = (LinearLayout) findViewById(R.id.release_job_info_back);
		preview = (TextView) findViewById(R.id.release_preview);
		ensure = (TextView) findViewById(R.id.release_ensure);
		type = (RelativeLayout) findViewById(R.id.release_type_layout);
		typeContent = (TextView) findViewById(R.id.release_info_type_content);
		time = (RelativeLayout) findViewById(R.id.release_time_layout);
		timeContent = (TextView) findViewById(R.id.release_info_time_content);
		area = (RelativeLayout) findViewById(R.id.release_area_layout);
		areaContent = (TextView) findViewById(R.id.release_info_areacontent);
		salaryContent = (EditText) findViewById(R.id.release_info_pay_content);
		salaryPayfor = (RelativeLayout) findViewById(R.id.release_salary_pay_for_layout);
		salaryPayContent = (TextView) findViewById(R.id.clearing_form_content);

		companyNameEdt.setText(companyNameStr);
		companyInchargeEdt.setText(incharegePerson);
		contactPhoneEdt.setText(contactPhoneStr);
		companyAdressEdt.setText(address);
		companyIntroEdt.setText(intro);

		back.setOnClickListener(this);
		payUnit.setOnClickListener(this);
		area.setOnClickListener(this);
		time.setOnClickListener(this);
		type.setOnClickListener(this);
		preview.setOnClickListener(this);
		ensure.setOnClickListener(this);
		back.setOnClickListener(this);
		salaryPayfor.setOnClickListener(this);
		tip.setText(Html.fromHtml(Constant.RELEASE_TIPS_CONTENT));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.release_job_info_back:
			finish();
			break;

		case R.id.release_preview:
			initWightValue();
			if (check(jobTitle, "兼职标题")) {
				return;
			} else if (check(typeKey, "兼职类型")) {
				return;
			} else if (check(huntNumber, "招聘人数")) {
				return;
			} else if (check(salaryValue, "薪资水平")) {
				return;
			} else if (check(salaryUnit, "薪资水平")) {
				return;
			} else if (check(timeKey, "兼职日期")) {
				Log.e("key", timeKey);
				return;
			} else if (check(areaKey, "工作区域")) {
				return;
			} else if (check(linkPerson, "联系人")) {
				return;
			} else if (check(linkPhone, "联系电话")) {
				return;
			} else if (check(addressDetailStr, "兼职地点")) {
				return;
			}
			info = new PartTimeInfo();
			info.setJobStatus("招聘中");
			info.setJobName(CommonUtil.nullToEmpty(jobTitle));
			Log.e("jobtitle", jobTitle + "'");
			info.setJobType(CommonUtil.nullToEmpty(jobType));
			info.setCompanyName(CommonUtil.nullToEmpty(companyName));
			info.setJobCount(CommonUtil.nullToEmpty(huntNumber));
			info.setJobReward(CommonUtil.nullToEmpty(salaryValue + salaryUnit));
			info.setJobWorkTime(CommonUtil.nullToEmpty(timeKey));
			Log.e("timeKeytimeKeytimeKeytimeKey", timeKey);
			info.setJobArea(CommonUtil.nullToEmpty(jobArea));
			if (TextUtils.isEmpty(jobIntro) && TextUtils.isEmpty(jobDemand)
					&& TextUtils.isEmpty(interviewTime)
					&& TextUtils.isEmpty(interviewAddress)
					&& TextUtils.isEmpty(attention)) {
				info.setJobContent("空");
			} else {
				info.setJobContent("工作内容描述：" + jobIntro + "<br>" + "工作要求："
						+ jobDemand + "<br>" + "面试时间：" + interviewTime + "<br>"
						+ "面试地点：" + interviewAddress + "<br>" + "注意事项："
						+ attention);
			}
			info.setCompanyIntroduce(CommonUtil.nullToEmpty(companyIntro));
			info.setCompanyPersonInCharge(CommonUtil
					.nullToEmpty(companyIncharge));
			info.setCompanyPhone(CommonUtil.nullToEmpty(contactPhone));
			info.setCompanyAdress(CommonUtil.nullToEmpty(companyAdress));
			info.setContactPerson(CommonUtil.nullToEmpty(linkPerson));
			info.setContactPhone(CommonUtil.nullToEmpty(linkPhone));
			info.setContactEmail(CommonUtil.nullToEmpty(linkEmail));
			info.setPeopleCount("0");
			info.setJobAreaDetail(addressDetailStr);
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
			String date = sdf.format(new Date());
			info.setJobReleaseTime(date);
			Intent previewIntent = new Intent(ReleaseNewJobActivity.this,
					PartTimeDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("info", info);
			previewIntent.putExtras(bundle);
			previewIntent.putExtra("flag", true);
			startActivity(previewIntent);
			break;

		case R.id.release_ensure:
			initWightValue();
			JSONObject object = new JSONObject();
			if (check(jobTitle, "兼职标题")) {
				return;
			} else if (check(typeKey, "兼职类型")) {
				return;
			} else if (check(huntNumber, "招聘人数")) {
				return;
			} else if (check(salaryValue, "薪资水平")) {
				return;
			} else if (check(salaryUnit, "薪资水平")) {
				return;
			} else if (check(timeKey, "兼职日期")) {
				return;
			} else if (check(areaKey, "工作区域")) {
				return;
			} else if (check(linkPerson, "联系人")) {
				return;
			} else if (check(linkPhone, "联系电话")) {
				return;
			} else if (check(addressDetailStr, "兼职地点")) {
				return;
			}
			try {
				object.put("uid", uid);
				object.put("name", jobTitle);
				object.put("job_class", typeKey);
				object.put("com_name", companyName);
				object.put("number", huntNumber);
				object.put("salary_value", salaryValue);
				object.put("salary_unit", salaryKey);
				object.put("job_time", timeKey);
				object.put("three_cityid", areaKey);
				object.put("description", jobIntro);
				object.put("jiesuan_class", salaryPayKey);
				object.put("job_need", jobDemand);
				object.put("view_time", interviewTime);
				object.put("view_address", interviewAddress);
				object.put("attention", attention);
				object.put("com_content", companyIntro);
				object.put("com_linkman", companyIncharge);
				object.put("com_linkphone", contactPhone);
				object.put("com_address", companyAdress);
				object.put("link_man", linkPerson);
				object.put("link_mobile", linkPhone);
				object.put("link_email", linkEmail);
				object.put("declare", statement);
				object.put("is_wt", isWt);
				object.put("address", addressDetailStr);
				Log.e("提交数据", object.toString());
				String result = HttpUtil.postRequst(UrlUtil.SUBMIT_JOB_URL,
						object);
				String msg = new JSONObject(result).getString("msg");
				String info = new JSONObject(result).getString("result");
				if (info.equals("true")) {
					Toast.makeText(ReleaseNewJobActivity.this, "请耐心等待审核结果",
							Toast.LENGTH_SHORT).show();
					
					finish();
				} else {
					Toast.makeText(ReleaseNewJobActivity.this, msg,
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case R.id.release_salary_pay_for_layout:
			Intent payIntent = new Intent(ReleaseNewJobActivity.this,
					ChooseJobListActivity.class);
			payIntent.putExtra("flag", 0);
			payIntent.putExtra("tag", 4);
			payIntent.putExtra("param", "accountclass");
			startActivityForResult(payIntent, 100);
			break;

		case R.id.release_type_layout:
			Intent type = new Intent(ReleaseNewJobActivity.this,
					ChooseJobListActivity.class);
			type.putExtra("flag", 0);
			type.putExtra("tag", 1);
			type.putExtra("param", "jobclass");
			startActivityForResult(type, 100);
			break;
		case R.id.release_time_layout:
			Intent time = new Intent(ReleaseNewJobActivity.this,
					ChooseJobListActivity.class);
			time.putExtra("flag", 1);
			time.putExtra("tag", 3);
			time.putExtra("param", "city");
			time.putExtra("time", timeKey);
			startActivityForResult(time, 100);
			break;
		case R.id.release_area_layout:
			Intent area = new Intent(ReleaseNewJobActivity.this,
					ChooseJobListActivity.class);
			area.putExtra("flag", 0);
			area.putExtra("tag", 2);
			area.putExtra("param", "province");
			startActivityForResult(area, 100);
			break;
		case R.id.release_info_pay_unit:
			Intent salary = new Intent(ReleaseNewJobActivity.this,
					ChooseJobListActivity.class);
			salary.putExtra("flag", 0);
			salary.putExtra("tag", 5);
			salary.putExtra("param", "result");
			startActivityForResult(salary, 100);

			break;
		}
	}

	public boolean check(String value, String tips) {
		if (TextUtils.isEmpty(value)) {
			Toast.makeText(ReleaseNewJobActivity.this, tips + "为必填项",
					Toast.LENGTH_LONG).show();
			return true;
		} else {
			return false;
		}
	}

	public void initWightValue() {
		jobTitle = titleEdt.getText().toString();// 兼职标题
		companyName = companyNameEdt.getText().toString();// 企业名称
		huntNumber = huntNumberEdt.getText().toString();// 招聘人数
		jobIntro = jobIntroEdt.getText().toString();// 岗位描述
		jobDemand = jobDemandEdt.getText().toString();// 工作要求
		interviewTime = interviewTimeEdt.getText().toString();// 面试时间
		interviewAddress = interviewAddressEdt.getText().toString();// 面试地点
		attention = attentionEdt.getText().toString();// 注意事项
		companyIntro = companyIntroEdt.getText().toString();// 企业简介
		companyIncharge = companyInchargeEdt.getText().toString();// 企业责任人
		contactPhone = contactPhoneEdt.getText().toString();// 联系电话
		companyAdress = companyAdressEdt.getText().toString();// 企业地址
		linkPerson = linkPersonEdt.getText().toString();// 联系人
		linkPhone = linkPhoneEdt.getText().toString();// 联系电话
//		statement = statementEdt.getText().toString();// 声明
		salaryValue = salaryContent.getText().toString();// 薪资水平
		addressDetailStr = addressDetail.getText().toString();// 兼职地点
		salaryUnit = payUnit.getText().toString();// 薪资水平单位
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("TAG", "onActivityResult");
		switch (resultCode) {
		case 1:
			jobType = data.getExtras().getString("value");
			typeKey = data.getExtras().getString("key");
			typeContent.setText(jobType);
			break;

		case 2:
			jobArea = data.getExtras().getString("value");
			areaKey = data.getExtras().getString("key");
			areaContent.setText(jobArea);
			Log.e("key", areaKey);
			break;

		case 3:
			jobTime = data.getExtras().getString("value");
			timeKey = jobTime;
			Log.e("jobTimejobTimejobTime", jobTime);
//			Log.e("timeKey", jobTime);
			timeContent.setText(CommonUtil.strToInt(jobTime));
			break;
		case 4:
			salaryPayValue = data.getExtras().getString("value");
			salaryPayKey = data.getExtras().getString("key");
			salaryPayContent.setText(salaryPayValue);
			Log.e("结算方式", salaryPayKey);
			break;

		case 5:
			jobSalary = data.getExtras().getString("value");
			salaryKey = data.getExtras().getString("key");
			payUnit.setText(jobSalary);
			break;
		}

		// case 4:
		// String jobPay = data.getExtras().getString("value");
		// payKey = data.getExtras().getString("key");
		// btnPay.setText(jobPay);
		// break;
		// }
		// adapter = new ClassifyGuideListAdapter(getList(), getActivity());
		// newestList.setAdapter(adapter);
		super.onActivityResult(requestCode, resultCode, data);
	}
}
