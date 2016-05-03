package cn.com.youyouparttime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.Constant;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.HttpUtil;
import cn.com.youyouparttime.util.UploadUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ApplyAuthenticationActivity extends Activity implements
		OnClickListener {

	private LinearLayout back;
	private TextView inchargePerson, companyEdt;
	private TextView type, organization, scale;
	private Button submit;
	private Button submitLicense, submitIDCard;
	private SharedPreferences share;
	private Editor editor;
	private String uid;
	private RelativeLayout typeLayout, organiLayout, scaleLayout;
	private String industryKey, organiKey, scaleStrKey;
	private TextView license, idCard;
	int flag, tag;
	String param;
	String busLiceUrl;
	String idCardUrl;
	String temp,temp2;
	String result;
	String industry;
	String organi;
	String scaleStr;
	private String companyName,person;
	private boolean certSucess;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_authentication);
		SysApplication.getInstance().addActivity(this);
		share = getSharedPreferences(Constant.COMPANY_PREFER, 0);
		editor = share.edit();
		uid = share.getString("uid", null);
		companyName = share.getString("companyname", "");
		person = share.getString("person", "");
		certSucess = share.getBoolean("certSucess", false);
		
		initView();
	}

	public void initView() {
		typeLayout = (RelativeLayout) findViewById(R.id.apply_authentication_industry);
		organiLayout = (RelativeLayout) findViewById(R.id.apply_authentication_character);
		scaleLayout = (RelativeLayout) findViewById(R.id.apply_authentication_scale);
		back = (LinearLayout) findViewById(R.id.apply_authentication_back);
		companyEdt = (TextView) findViewById(R.id.apply_company_name_edt);
		inchargePerson = (TextView) findViewById(R.id.apply_company_charge_person_edt);
		submit = (Button) findViewById(R.id.apply_submit_btn);
		type = (TextView) findViewById(R.id.apply_company_industry_content);
		organization = (TextView) findViewById(R.id.apply_company_character_content);
		scale = (TextView) findViewById(R.id.apply_company_scale_content);
		submitLicense = (Button) findViewById(R.id.upload_license);
		submitIDCard = (Button) findViewById(R.id.upload_identity);
		license = (TextView) findViewById(R.id.license_text);
		idCard = (TextView) findViewById(R.id.id_card_text);
		companyEdt.setText(companyName);
		inchargePerson.setText(person);
		industry = share.getString("industry", "");
		organi = share.getString("organization", "");
		scaleStr = share.getString("scale", "");
		type.setText(industry);
		organization.setText(organi);
		scale.setText(scaleStr);
		
		industryKey = share.getString("industryKey", null);
		organiKey = share.getString("organizationKey", null);
		scaleStrKey = share.getString("scaleKey", null);
		
		if (!certSucess) {
			submitIDCard.setOnClickListener(this);
			submitLicense.setOnClickListener(this);
			submit.setOnClickListener(this);
			typeLayout.setOnClickListener(this);
			organiLayout.setOnClickListener(this);
			scaleLayout.setOnClickListener(this);
		}else {
			submit.setText("您已认证");
		}
		
		
		
		
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.apply_authentication_back:
			finish();
			break;

		case R.id.apply_submit_btn:
			JSONObject object = new JSONObject();
			try {
				object.put("uid", uid);
				Log.e("uid", uid);
				object.put("hr", industryKey);
				object.put("pr", organiKey);
				object.put("mun", scaleStrKey);
				object.put("yyzz", busLiceUrl);
				object.put("sfz", idCardUrl);
				object.put("link_man", person);
				Log.e("uploadobject", object.toString());
				String result = HttpUtil.postRequst(
						UrlUtil.COMPANY_AUTHENTICATION_URL, object);
				
				String msg = new JSONObject(result).getString("msg");
				String info = new JSONObject(result).getString("result");
				if (info.equals("true")) {
					Toast.makeText(ApplyAuthenticationActivity.this, "请耐心等待审核结果",
							Toast.LENGTH_SHORT).show();
					editor.putString("industry", industry);
					editor.putString("organization", organi);
					editor.putString("scale", scaleStr);
					editor.putString("industryKey", industryKey);
					editor.putString("organizationKey", organiKey);
					editor.putString("scaleKey", scaleStrKey);
					editor.commit();
					finish();
				}else {
					Toast.makeText(ApplyAuthenticationActivity.this, msg,
							Toast.LENGTH_SHORT).show();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case R.id.apply_authentication_industry:
			flag = 0;
			tag = 6;
			param = "hy";
			gotoSearch();
			break;

		case R.id.apply_authentication_character:
			flag = 0;
			tag = 7;
			param = "pr";
			gotoSearch();
			break;

		case R.id.apply_authentication_scale:
			flag = 0;
			tag = 8;
			param = "mun";
			gotoSearch();
			break;

		case R.id.upload_license:
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			startActivityForResult(intent, 1);
			break;

		case R.id.upload_identity:
			Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
			intent2.addCategory(Intent.CATEGORY_OPENABLE);
			intent2.setType("image/*");
			startActivityForResult(intent2, 2);
			break;
		}
	}

	public void gotoSearch() {
		Intent intent = new Intent(ApplyAuthenticationActivity.this,
				ChooseJobListActivity.class);
		intent.putExtra("flag", flag);
		intent.putExtra("tag", tag);
		intent.putExtra("param", param);
		startActivityForResult(intent, tag);
		// startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("TAG", "onActivityResult");
		switch (resultCode) {

		case 6:
			industry = data.getExtras().getString("value");
			industryKey = data.getExtras().getString("key");
			type.setText(industry);
			break;

		case 7:
			organi = data.getExtras().getString("value");
			organiKey = data.getExtras().getString("key");
			organization.setText(organi);
			// Log.e("key", areaKey);
			break;

		case 8:
			scaleStr = data.getExtras().getString("value");
			scaleStrKey = data.getExtras().getString("key");
			scale.setText(scaleStr);
			break;

		}
		switch (requestCode) {
		case 1:
			if (data != null) {
				Uri uri = data.getData();
				int flag = 1;
				crop(uri, flag);

			}
			break;
			
		case 2:
			if (data != null) {
				Uri uri = data.getData();
				int flag = 2;
				crop(uri, flag);

			}
			break;

		case 100:
			if (data != null) {
				Bitmap bitmap = data.getParcelableExtra("data");
				try {
					File file = saveBitmap(bitmap);
					getImgUrl(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
					license.setText("上传成功");
				
			}
			break;
			
		case 200:
			if (data != null) {
				Bitmap bitmap = data.getParcelableExtra("data");
				try {
					File file = saveBitmap(bitmap);
					getImgUrlTo(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
					idCard.setText("上传成功");
				
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public File saveBitmap(Bitmap map) throws IOException {
		File f = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + "temp.png");
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		map.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

	public synchronized String getImgUrl(final File file) {
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				String json = UploadUtil.uploadFile(file,
						UrlUtil.UPLOAD_IMG_URL);
				try {
					busLiceUrl = new JSONObject(json).getString("filename");
					result = new JSONObject(json).getString("result");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
		return temp;
	}

	public synchronized String getImgUrlTo(final File file) {
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				String json = UploadUtil.uploadFile(file,
						UrlUtil.UPLOAD_IMG_URL);
				try {
					idCardUrl = new JSONObject(json).getString("filename");
					result = new JSONObject(json).getString("result");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
		return temp;
	}
	
	
	private void crop(Uri uri, int flag) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		intent.putExtra("outputFormat", "JPEG");// 图片格式
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
		if (flag == 1 ) {
			startActivityForResult(intent, 100);
		}else if (flag == 2) {
			startActivityForResult(intent, 200);
		}
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		File f = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + "temp.png");
		if (f.exists()) {
			f.delete();
		}
	}
}
