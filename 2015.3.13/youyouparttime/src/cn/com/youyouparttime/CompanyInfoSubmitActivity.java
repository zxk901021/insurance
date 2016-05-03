package cn.com.youyouparttime;

import org.json.JSONObject;

import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.CompanyInfo;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.CommonUtil;
import cn.com.youyouparttime.util.DialogUtil;
import cn.com.youyouparttime.util.DialogUtil.DialogListener;
import cn.com.youyouparttime.util.HttpUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CompanyInfoSubmitActivity extends Activity implements
		OnClickListener {

	private EditText companyName;
	private EditText contactPerson;
	private TextView contactPhone;
	private EditText email;
	private EditText address;
	private EditText companyIntro;
	private Button submit;
	private LinearLayout back;
	private String name, person, phone, emailStr, addressStr, intro;
	private SharedPreferences share;
	private String uid;
	private Editor editor;
	private CompanyInfo info;
	private String username;
	private boolean hasSubmit;
	private TextView companyNameText, contactPersonText, emailText,
			addressText, companyIntroText;
	private String temp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_info_submit);
		SysApplication.getInstance().addActivity(this);
		share = getSharedPreferences("companyPrefer", 0);
		editor = share.edit();
		uid = share.getString("uid", null);
		username = share.getString("username", "");
		hasSubmit = share.getBoolean("hasSubmit", false);
		if (hasSubmit) {
			getData();
		}
		initView();

	}

	public void initView() {
		companyName = (EditText) findViewById(R.id.company_edit_name);
		contactPerson = (EditText) findViewById(R.id.company_edit_incharge_person);
		contactPhone = (TextView) findViewById(R.id.company_edit_contact_phone);
		email = (EditText) findViewById(R.id.company_edit_contact_email);
		address = (EditText) findViewById(R.id.company_edit_contact_address);
		companyIntro = (EditText) findViewById(R.id.company_edit_intro);
		submit = (Button) findViewById(R.id.submit_company_info);
		back = (LinearLayout) findViewById(R.id.submit_company_info_back);
		companyNameText = (TextView) findViewById(R.id.company_edit_name_text);
		contactPersonText = (TextView) findViewById(R.id.company_edit_incharge_person_text);
		emailText = (TextView) findViewById(R.id.company_edit_contact_email_text);
		addressText = (TextView) findViewById(R.id.company_edit_contact_address_text);
		companyIntroText = (TextView) findViewById(R.id.company_edit_intro_text);
		if (hasSubmit) {
			companyNameText.setText(name);
			contactPersonText.setText(person);
			emailText.setText(emailStr);
			addressText.setText(addressStr);
			companyIntroText.setText(intro);

			companyNameText.setVisibility(View.VISIBLE);
			contactPersonText.setVisibility(View.VISIBLE);
			emailText.setVisibility(View.VISIBLE);
			addressText.setVisibility(View.VISIBLE);
			companyIntroText.setVisibility(View.VISIBLE);

			companyName.setVisibility(View.GONE);
			contactPerson.setVisibility(View.GONE);
			email.setVisibility(View.GONE);
			address.setVisibility(View.GONE);
			companyIntro.setVisibility(View.GONE);
			submit.setVisibility(View.GONE);

		}
		submit.setOnClickListener(this);
		back.setOnClickListener(this);
		contactPhone.setText(username);

	}

	public void getData() {
		// JSONObject object = new JSONObject();
		// info = new CompanyInfo();
		// try {
		// object.put("id", uid);
		// String result = HttpUtil.postRequst(UrlUtil.COMPANY_DETAIL_URL,
		// object);
		// JSONObject jsonObject = new JSONObject(result);
		// info.setComName(jsonObject.getJSONObject("company").getString("name"));
		// info.setChargePerson(jsonObject.getJSONObject("company").getString("com_linkman"));
		// info.setPhone(jsonObject.getJSONObject("company").getString("com_linkphone"));
		// info.setEmail(jsonObject.getJSONObject("company").getString("com_linkmail"));
		// info.setAddress(jsonObject.getJSONObject("company").getString("com_address"));
		// info.setComIntro(jsonObject.getJSONObject("company").getString("com_content"));
		// companyName.setText(CommonUtil.nullToEmpty(info.getComName()));
		// contactPerson.setText(CommonUtil.nullToEmpty(info.getChargePerson()));
		// // contactPhone.setText(CommonUtil.nullToEmpty(info.getPhone()));
		// email.setText(CommonUtil.nullToEmpty(info.getEmail()));
		// address.setText(CommonUtil.nullToEmpty(info.getAddress()));
		// companyIntro.setText(Html.fromHtml(CommonUtil.nullToEmpty(info.getComIntro())));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		name = share.getString("companyname", "");
		person = share.getString("person", "");
		intro = share.getString("intro", "");
		emailStr = share.getString("emailStr", "");
		addressStr = share.getString("address", "");
		Log.e("values", name + person + intro + emailStr + addressStr);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit_company_info:
			name = companyName.getText().toString();
			person = contactPerson.getText().toString();
			phone = contactPhone.getText().toString();
			emailStr = email.getText().toString();
			addressStr = address.getText().toString();
			intro = companyIntro.getText().toString();
			if (TextUtils.isEmpty(name)) {
				Toast.makeText(CompanyInfoSubmitActivity.this, "企业名称不能为空！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(person)) {
				Toast.makeText(CompanyInfoSubmitActivity.this, "责任人不能为空！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(emailStr)) {
				Toast.makeText(CompanyInfoSubmitActivity.this, "邮箱不能为空！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(addressStr)) {
				Toast.makeText(CompanyInfoSubmitActivity.this, "地址不能为空！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(intro)) {
				Toast.makeText(CompanyInfoSubmitActivity.this, "企业简介不能为空！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (!TextUtils.isEmpty(intro) && intro.length() < 30) {
				Toast.makeText(CompanyInfoSubmitActivity.this, "企业简介少于30字！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			DialogUtil.ensureSubmitDialog(CompanyInfoSubmitActivity.this,
					new DialogListener() {

						@Override
						public void refreshUI(String string, String key) {

						}

						@Override
						public void refreshUI(String string) {
							temp = string;
							if (temp != null) {
								if (temp.equals("1")) {
									JSONObject object = new JSONObject();

									try {
										object.put("name", name);
										object.put("linkman", person);
										object.put("linkphone", phone);
										object.put("linkmail", emailStr);
										object.put("address", addressStr);
										object.put("content", intro);
										object.put("uid", uid);
										Log.e("object", object.toString());
										String result = HttpUtil.postRequst(
												UrlUtil.COMPANY_INFO_SUBMIT_URL, object);
										String info = new JSONObject(result).getString("result");
										String msg = new JSONObject(result).getString("msg");
										if (info.equals("true")) {
											Toast.makeText(CompanyInfoSubmitActivity.this, msg,
													Toast.LENGTH_SHORT).show();
											editor.putString("companyname", name);
											editor.putString("person", person);
											editor.putString("phone", phone);
											editor.putString("emailStr", emailStr);
											editor.putString("address", addressStr);
											editor.putString("intro", intro);
											editor.putBoolean("hasSubmit", true);
											editor.commit();
											Intent submitIntent = new Intent(
													CompanyInfoSubmitActivity.this,
													ReleasePartTimeActivity.class);
											startActivity(submitIntent);
											finish();
										} else {
											Toast.makeText(CompanyInfoSubmitActivity.this, msg,
													Toast.LENGTH_SHORT).show();
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								} else {
									return;
								}
							}
						}
					});
			
			

			break;

		case R.id.submit_company_info_back:
			if (!hasSubmit) {
				Intent subIntent = new Intent(CompanyInfoSubmitActivity.this,
						ReleasePartTimeActivity.class);
				startActivity(subIntent);
				finish();
			} else {
				finish();
			}

			break;
		}
	}
}
