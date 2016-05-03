package cn.com.youyouparttime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.ImageLoader.ImageListener;

import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.StudentResumeInfo;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.BitmapCache;
import cn.com.youyouparttime.util.CommonUtil;
import cn.com.youyouparttime.util.DialogUtil;
import cn.com.youyouparttime.util.UploadUtil;
import cn.com.youyouparttime.util.DialogUtil.DialogListener;
import cn.com.youyouparttime.util.HttpUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyResumeActivity extends Activity implements OnClickListener,
		OnLongClickListener {

	private LinearLayout back;
	private RelativeLayout resumeTime, editIntent, resumeBirth, schoolChoose,
			phoneChoice, jobArea, healthyCard;
	private EditText editName, editHeight, editEmail, editQQ, editMyself,
			editExperience, majorContent;
	private LinearLayout province, city, area;
	public static int RESUME_TIME_MODE = 1;
	private ImageView myPhoto, myPhoto1, myPhoto2;
	private Button man, woman;
	private TextView resumeTimeContent, resumeBirthContent, intentContent,
			resumeProvince, resumeCity, resumeArea, telephone, resumeName,
			schoolContent, areaContent, healthyContent;
	/**
	 * 各种返回的参数值
	 */
	private String jobTime, provinceKey, cityKey, areaKey, intentKey, name,
			sex, birthdayStr, height, username, email, qq, jobClass,
			experience, introduce, uid, schoolStr, schoolKey, imgUrl,jobAreaKey,school,specilty,healthKey;
	private Button submitResume;// 提交按钮
	SharedPreferences preferences;
	Editor editor;
	private String resumesName;
	private StudentResumeInfo detail;
	private File file;
	private int flag = 0;
	private int tag = 0;
	private StringBuffer buffer;
	RequestQueue queue;
	ImageLoader imageLoader;
	private String imgUrl1, imgUrl2, imgUrl3;
	private int img1, img2, img3;
	private String url1, url2, url3;
	private String[] temp = new String[3];
	private List<String> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_resume);
		SysApplication.getInstance().addActivity(this);
		buffer = new StringBuffer();
		preferences = getSharedPreferences("myPrefer", 0);
		editor = preferences.edit();
		username = preferences.getString("username", null);
		uid = preferences.getString("uid", null);
		resumesName = preferences.getString("personname", "");
		queue = Volley.newRequestQueue(this);
		imageLoader = new ImageLoader(queue, new BitmapCache());
		list = new ArrayList<String>();
		sex = "6";
		initView();
		initValue();
	}

	public void initValue() {
		JSONObject object = new JSONObject();
		try {
			object.put("uid", uid);
			String result = HttpUtil.postRequst(UrlUtil.BROWSE_RESUME_URL,
					object);
			JSONObject infoJson = new JSONObject(result).getJSONObject("info");
			detail = new StudentResumeInfo();
			detail.setImgUrl(infoJson.getString("photo"));
			detail.setTime(infoJson.getString("job_time_list"));
			detail.setJobIntent(infoJson.getString("job_class_list"));
			detail.setName(infoJson.getString("name"));
			detail.setSex(infoJson.getString("sex"));
			detail.setBirthday(infoJson.getString("birthday"));
			detail.setHeight(infoJson.getString("height"));
			detail.setCityId(infoJson.getString("three_cityid"));
			detail.setCity(infoJson.getString("city_list"));
			detail.setSchool(infoJson.getString("school"));
			detail.setEmail(infoJson.getString("email"));
			detail.setQq(infoJson.getString("qq"));
			detail.setPhone(infoJson.getString("telphone"));
			detail.setIntro(infoJson.getString("description"));
			detail.setExprience(infoJson.getString("work"));
			detail.setTimeId(infoJson.getString("job_time"));
			detail.setIntentId(infoJson.getString("job_classid"));
			detail.setSpecitly(infoJson.getString("specialty"));
			detail.setHealthkey(infoJson.getString("jkcart"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String temp = detail.getImgUrl();
		Log.e("temp", temp + "'" + temp.length());
		if (temp == null || temp.length() == 0) {
			myPhoto.setImageResource(R.drawable.resume_add_img);
			img1 = 0;
			img2 = 0;
			img3 = 0;
		} else if (temp != null & !temp.contains(",")) {
			ImageListener imageListener = ImageLoader.getImageListener(myPhoto,
					R.drawable.resume_add_img, R.drawable.resume_add_img);
			imageLoader.get(UrlUtil.SITEURL + temp, imageListener);
			img1 = 1;
			img2 = 0;
			img3 = 0;
			myPhoto1.setVisibility(View.VISIBLE);
			myPhoto1.setImageResource(R.drawable.resume_add_img);
			imgUrl1 = temp;
			this.temp[0] = temp;
			list.add(imgUrl1);
		} else if (temp != null && temp.contains(",")) {
			String[] imgUrls = temp.split(",");
			int count = imgUrls.length;
			if (count == 2) {
				ImageListener imageListener = ImageLoader.getImageListener(
						myPhoto, R.drawable.resume_add_img,
						R.drawable.resume_add_img);
				imageLoader.get(UrlUtil.SITEURL + imgUrls[0], imageListener);
				ImageListener imageListener2 = ImageLoader.getImageListener(
						myPhoto1, R.drawable.resume_add_img,
						R.drawable.resume_add_img);
				imageLoader.get(UrlUtil.SITEURL + imgUrls[1], imageListener2);
				myPhoto1.setVisibility(View.VISIBLE);
				myPhoto2.setVisibility(View.VISIBLE);
				myPhoto2.setImageResource(R.drawable.resume_add_img);
				img1 = 1;
				img2 = 1;
				img3 = 0;
				imgUrl1 = imgUrls[0];
				imgUrl2 = imgUrls[1];
				this.temp[0] = imgUrls[0];
				this.temp[1] = imgUrls[1];
				list.add(imgUrl1);
				list.add(imgUrl2);
			}
			if (count == 3) {
				ImageListener imageListener = ImageLoader.getImageListener(
						myPhoto, R.drawable.resume_add_img,
						R.drawable.resume_add_img);
				imageLoader.get(UrlUtil.SITEURL + imgUrls[0], imageListener);
				ImageListener imageListener2 = ImageLoader.getImageListener(
						myPhoto1, R.drawable.resume_add_img,
						R.drawable.resume_add_img);
				imageLoader.get(UrlUtil.SITEURL + imgUrls[1], imageListener2);
				ImageListener imageListener3 = ImageLoader.getImageListener(
						myPhoto2, R.drawable.resume_add_img,
						R.drawable.resume_add_img);
				imageLoader.get(UrlUtil.SITEURL + imgUrls[2], imageListener3);
				myPhoto1.setVisibility(View.VISIBLE);
				myPhoto2.setVisibility(View.VISIBLE);
				img1 = 1;
				img2 = 1;
				img3 = 1;
				imgUrl1 = imgUrls[0];
				imgUrl2 = imgUrls[1];
				imgUrl3 = imgUrls[2];
				this.temp[0] = imgUrls[0];
				this.temp[1] = imgUrls[1];
				this.temp[2] = imgUrls[2];
				list.add(imgUrl1);
				list.add(imgUrl2);
				list.add(imgUrl3);
			}
			
		}
		String[] areas = detail.getCity().split(",");
		String[] areasId = detail.getCityId().split(",");
		String province = null;
		String city = null;
		String area = null;
		if (areas.length == 3) {
			province = areas[0];
			city = areas[1];
			area = areas[2];
		}
		if (areasId.length == 3) {
			provinceKey = areasId[0];
			cityKey = areasId[1];
			areaKey = areasId[2];
		}

		if (province != null) {
			resumeProvince.setText(province);
		}
		if (city != null) {
			resumeCity.setText(city);
		}
		if (area != null) {
			resumeArea.setText(area);
		}
		String sexStr = detail.getSex();
		sex = sexStr;
		if (sexStr != null) {
			if (sexStr.equals("6")) {
				man.setBackgroundColor(Color.parseColor("#fa9600"));
				woman.setBackgroundColor(Color.WHITE);
				man.setTextColor(Color.WHITE);
				woman.setTextColor(Color.parseColor("#fa9600"));
			} else if (sexStr.equals("5")) {
				man.setBackgroundColor(Color.WHITE);
				woman.setBackgroundColor(Color.parseColor("#fa9600"));
				man.setTextColor(Color.parseColor("#fa9600"));
				woman.setTextColor(Color.WHITE);
			}
		}
		jobAreaKey = detail.getCityId();
		healthKey = detail.getHealthkey();
		if (healthKey != null) {
			if (healthKey.equals("0")) {
				healthyContent.setText("无");
			}else {
				healthyContent.setText("有");
			}
		}
		resumeTimeContent.setText(CommonUtil.nullToEmpty(detail.getTime()));
		intentContent.setText(CommonUtil.nullToEmpty(detail.getJobIntent()));
		editName.setText(CommonUtil.nullToEmpty(detail.getName()));
		resumeBirthContent
				.setText(CommonUtil.nullToEmpty(detail.getBirthday()));
		editHeight.setText(CommonUtil.nullToEmpty(detail.getHeight()));
		schoolContent.setText(CommonUtil.nullToEmpty(detail.getSchool()));
		editEmail.setText(CommonUtil.nullToEmpty(detail.getEmail()));
		editQQ.setText(CommonUtil.nullToEmpty(detail.getQq()));
		editMyself.setText(CommonUtil.nullToEmpty(detail.getIntro()));
		editExperience.setText(CommonUtil.nullToEmpty(detail.getExprience()));
		majorContent.setText(CommonUtil.nullToEmpty(detail.getSpecitly()));
		areaContent.setText(CommonUtil.nullToEmpty(detail.getCity()));
	}

	public void initView() {
		myPhoto = (ImageView) findViewById(R.id.resume_my_photo);
		myPhoto1 = (ImageView) findViewById(R.id.resume_my_photo1);
		myPhoto2 = (ImageView) findViewById(R.id.resume_my_photo2);
		resumeTime = (RelativeLayout) findViewById(R.id.resume_time);
		back = (LinearLayout) findViewById(R.id.edit_my_resume_back);
		editIntent = (RelativeLayout) findViewById(R.id.edit_intent);
		jobArea = (RelativeLayout) findViewById(R.id.edit_job_area);
		areaContent = (TextView) findViewById(R.id.job_area_text_content);
		resumeBirth = (RelativeLayout) findViewById(R.id.resume_birth_layout);
		healthyCard = (RelativeLayout) findViewById(R.id.healthy_layout);
		healthyContent = (TextView) findViewById(R.id.healthy_card_content);
		editName = (EditText) findViewById(R.id.apply_name_edit);
		editHeight = (EditText) findViewById(R.id.apply_height_edit);
		editEmail = (EditText) findViewById(R.id.apply_email_edit);
		editQQ = (EditText) findViewById(R.id.apply_qq_edit);
		editMyself = (EditText) findViewById(R.id.introduce_myself_edit);
		editExperience = (EditText) findViewById(R.id.experience_edit);
		province = (LinearLayout) findViewById(R.id.province_layout);
		city = (LinearLayout) findViewById(R.id.city_layout);
		area = (LinearLayout) findViewById(R.id.area_layout);
		resumeTimeContent = (TextView) findViewById(R.id.resume_time_content);
		resumeBirthContent = (TextView) findViewById(R.id.resume_birth_content);
		intentContent = (TextView) findViewById(R.id.intent_text_content);
		resumeProvince = (TextView) findViewById(R.id.resume_province);
		resumeCity = (TextView) findViewById(R.id.resume_city_content);
		resumeArea = (TextView) findViewById(R.id.resume_area_content);
		submitResume = (Button) findViewById(R.id.submit_resume);
		telephone = (TextView) findViewById(R.id.resume_telephone);
		resumeName = (TextView) findViewById(R.id.resumes_name);
		man = (Button) findViewById(R.id.man_btn);
		woman = (Button) findViewById(R.id.woman_btn);
		schoolChoose = (RelativeLayout) findViewById(R.id.school_choose);
		schoolContent = (TextView) findViewById(R.id.resume_school_content);
//		major = (RelativeLayout) findViewById(R.id.major_choose);
		majorContent = (EditText) findViewById(R.id.resume_major_content);
		phoneChoice = (RelativeLayout) findViewById(R.id.phone_choice);
//		major.setOnClickListener(this);
		healthyCard.setOnClickListener(this);
		jobArea.setOnClickListener(this);
		phoneChoice.setOnClickListener(this);
		schoolChoose.setOnClickListener(this);
		man.setOnClickListener(this);
		woman.setOnClickListener(this);
		resumeName.setText(resumesName);
		telephone.setText(username);
		myPhoto.setOnClickListener(this);
		myPhoto1.setOnClickListener(this);
		myPhoto2.setOnClickListener(this);

		myPhoto.setOnLongClickListener(this);
		myPhoto1.setOnLongClickListener(this);
		myPhoto2.setOnLongClickListener(this);

		back.setOnClickListener(this);
		submitResume.setOnClickListener(this);
		province.setOnClickListener(this);
		city.setOnClickListener(this);
		area.setOnClickListener(this);
		resumeTime.setOnClickListener(this);
		editIntent.setOnClickListener(this);
		resumeBirth.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.resume_my_photo:
			if (img1 == 1) {
				return;
			}
			flag = 1;
			getPhoto(1);

			break;

		case R.id.resume_my_photo1:
			if (img2 == 1) {
				return;
			}
			flag = 2;
			getPhoto(3);

			break;

		case R.id.resume_my_photo2:
			if (img3 == 1) {
				return;
			}
			flag = 3;
			getPhoto(5);

			break;

		case R.id.resume_time:
			Intent intent = new Intent(MyResumeActivity.this,
					ChooseJobListActivity.class);
			intent.putExtra("flag", RESUME_TIME_MODE);
			intent.putExtra("tag", RESUME_TIME_MODE);
			String timeKey = "";
			if (jobTime == null || jobTime.length() == 0) {
				timeKey =  detail.getTimeId();
			} else {
				timeKey = jobTime;
			}
			intent.putExtra("time", timeKey);
			startActivityForResult(intent, RESUME_TIME_MODE);
			break;

		case R.id.edit_my_resume_back:
			finish();
			break;
			
		case R.id.major_choose:
			Intent majorIntent = new Intent(MyResumeActivity.this, SchoolMajorActivity.class);
			majorIntent.putExtra("flag", 20);
			startActivityForResult(majorIntent, 20);
			break;
			
		case R.id.school_choose:
			Intent schoolIntent = new Intent(MyResumeActivity.this,
					SchoolMajorActivity.class);
			schoolIntent.putExtra("flag", 21);
			startActivityForResult(schoolIntent, 21);
			break;
		case R.id.edit_intent:
			DialogUtil.intentDialog(this, new DialogListener() {

				@Override
				public void refreshUI(String string, String key) {
					jobClass = string;
					intentContent.setText(string);
					intentContent.setTextColor(Color.BLACK);
					intentKey = key;
					Log.e("intentkey", intentKey);
				}

				@Override
				public void refreshUI(String string) {

				}
			});
			break;
			
		case R.id.edit_job_area:
			DialogUtil.areaDialog(this, new DialogListener() {
				
				@Override
				public void refreshUI(String string, String key) {
					areaContent.setText(string);
					jobAreaKey = key;
					Log.e("123456789", jobAreaKey);
				}
				
				@Override
				public void refreshUI(String string) {
					
				}
			});
			break;
			
			
		case R.id.healthy_layout:
			DialogUtil.healthDialog(this, new DialogListener() {
				
				@Override
				public void refreshUI(String string, String key) {
					
				}
				
				@Override
				public void refreshUI(String string) {
					healthyContent.setText(string);
					if (string.equals("有")) {
						healthKey = "1";
					}else {
						healthKey = "0";
					}
				}
			});
			break;

		case R.id.province_layout:
			DialogUtil.setAreaDialog(this, new DialogListener() {

				@Override
				public void refreshUI(String string, String key) {
					resumeProvince.setText(string);
					provinceKey = key;
				}

				@Override
				public void refreshUI(String string) {

				}
			});
			break;

		case R.id.city_layout:
			if (provinceKey == null || provinceKey.length() == 0) {
				Toast.makeText(MyResumeActivity.this, "请先选择省份",
						Toast.LENGTH_SHORT).show();
				return;
			}
			DialogUtil.setCityDialog(this, new DialogListener() {

				@Override
				public void refreshUI(String string, String key) {
					resumeCity.setText(string);
					cityKey = key;
				}

				@Override
				public void refreshUI(String string) {

				}
			}, UrlUtil.JOB_CITY_URL, provinceKey);
			break;

		case R.id.area_layout:
			if (provinceKey == null || provinceKey.length() == 0) {
				Toast.makeText(MyResumeActivity.this, "请先选择省份",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (cityKey == null || cityKey.length() == 0) {
				Toast.makeText(MyResumeActivity.this, "请先选择城市",
						Toast.LENGTH_SHORT).show();
				return;
			}
			DialogUtil.setCityDialog(this, new DialogListener() {

				@Override
				public void refreshUI(String string, String key) {
					resumeArea.setText(string);
					areaKey = key;
				}

				@Override
				public void refreshUI(String string) {

				}
			}, UrlUtil.JOB_CITY_URL, cityKey);
			break;

		case R.id.resume_birth_layout:
			DialogUtil.dateDialog(this, new DialogListener() {

				@Override
				public void refreshUI(String string) {
					birthdayStr = string;
					resumeBirthContent.setText(string);
					resumeBirthContent.setTextColor(Color.BLACK);
				}

				@Override
				public void refreshUI(String string, String key) {

				}
			});

			break;

		case R.id.phone_choice:
			Intent phoneIntent = new Intent(MyResumeActivity.this,
					ResumeChooseItemActivity.class);
			startActivityForResult(phoneIntent, 10);
			break;

		case R.id.submit_resume:
			JSONObject object = new JSONObject();
			getValue();
			Log.e("上传图片的地址为：", temp[0] + "   " + temp[1] + "     " + temp[2]);
			try {
				object.put("uid", uid);
				object.put("name", name);
				object.put("sex", sex);
				if (birthdayStr == null || birthdayStr.length() == 0) {
					birthdayStr = resumeBirthContent.getText().toString();
				}
				object.put("birthday", birthdayStr);
				object.put("height", height);
				// if (buffer == null || buffer.length() == 0) {
				// imgUrl = "";
				// } else {
				// imgUrl = buffer.toString()
				// .substring(0, buffer.length() - 1);
				// }
				if (list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i) != null || list.get(i).length() != 0) {
							buffer.append(list.get(i) + ",");
						}
					}
					imgUrl = buffer.toString().substring(0, buffer.length() - 1);
				}else {
					imgUrl = "";
				}
				
				object.put("photo", imgUrl);
				object.put("telphone", username);
				object.put("email", email);
				if (schoolStr == null || schoolStr.length() == 0) {
					schoolStr = schoolContent.getText().toString();
				}
				object.put("school", schoolStr);
				if (intentKey == null || intentKey.length() == 0) {
					object.put("job_classid", detail.getIntentId());
				} else {
					object.put("job_classid", intentKey);
				}
//				String three_cityid = chooseCityKey(provinceKey, cityKey,
//						areaKey);
//				if (three_cityid == null || three_cityid.length() == 2) {
//					object.put("three_cityid", detail.getCityId());
//				} else {
//					object.put("three_cityid", three_cityid);
//				}
				object.put("three_cityid", jobAreaKey);
				object.put("work", experience);
				object.put("description", introduce);
				object.put("qq", qq);
				object.put("specialty", specilty);
				object.put("jkcart", healthKey);
				if (jobTime == null || jobTime.length() == 0) {
					object.put("job_time", detail.getTimeId());
				} else {
					object.put("job_time", jobTime);
				}
				Log.e("object", object.toString());
				String result = HttpUtil.postRequst(UrlUtil.SUBMIT_RESUME_URL,
						object);
				Log.e("result", result);
				JSONObject resultJson = new JSONObject(result);
				String msg = resultJson.getString("msg");
				String a = resultJson.getString("result");
				if (a.equals("true")) {
					editor.putBoolean("hasSubmit", true);
					editor.putString("personname", name);
					String sexValue = "";
					if (sex.equals("6")) {
						sexValue = "男";
					}else {
						sexValue = "女";
					}
					editor.putString("sex", sexValue);
					editor.putString("school", schoolStr);
					editor.commit();
					Toast.makeText(MyResumeActivity.this, "简历提交成功!",
							Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(MyResumeActivity.this, msg,
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case R.id.man_btn:
			man.setBackgroundColor(Color.parseColor("#fa9600"));
			woman.setBackgroundColor(Color.WHITE);
			man.setTextColor(Color.WHITE);
			woman.setTextColor(Color.parseColor("#fa9600"));
			sex = "6";
			break;

		case R.id.woman_btn:
			man.setBackgroundColor(Color.WHITE);
			woman.setBackgroundColor(Color.parseColor("#fa9600"));
			man.setTextColor(Color.parseColor("#fa9600"));
			woman.setTextColor(Color.WHITE);
			sex = "5";
			break;

		

		}
	}

	public void getPhoto(final int temp) {
		DialogUtil.showDialog(MyResumeActivity.this, new DialogListener() {

			@Override
			public void refreshUI(String string) {
				Log.e("string", string + "2");
				if (string.equals("1")) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("image/*");
					intent.putExtra("flag", flag);
					startActivityForResult(intent, temp);
				} else {
					File file = null;
					try {
						file = new File(Environment
								.getExternalStorageDirectory()
								.getAbsolutePath()
								+ File.separator + "portrait.jpg");
					} catch (Exception e) {
						e.printStackTrace();
					}
					Uri uri = Uri.fromFile(file);
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
					startActivityForResult(intent, temp + 1);
				}
			}

			@Override
			public void refreshUI(String string, String key) {

			}
		});
	}

	public String chooseCityKey(String a, String b, String c) {
		if (a == null) {
			return "0,0,0";
		} else if (b == null) {
			return a + "0,0";
		} else if (c == null) {
			return a + "," + b + ",0";
		} else {
			return a + "," + b + "," + c;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESUME_TIME_MODE) {
			jobTime = data.getStringExtra("value");
			resumeTimeContent.setText(CommonUtil.strToInt(jobTime));
			resumeTimeContent.setTextColor(Color.BLACK);
		}
		if (resultCode == 9) {
			schoolStr = data.getStringExtra("value");
			schoolKey = data.getExtras().getString("key");
			schoolContent.setText(schoolStr);
		}
		if (resultCode == 10) {
			String newPhone = data.getStringExtra("phone");
			telephone.setText(newPhone);
		}
		if (resultCode == 20) {
			specilty = data.getStringExtra("value");
			majorContent.setText(specilty);
		}
		if (resultCode == 21) {
			school = data.getStringExtra("value");
			schoolContent.setText(school);
		}
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case 1:
			if (data != null) {
				Uri uri = data.getData();
				crop(uri, 100);
			}
			break;

		case 2:
			crop2(7);
			break;

		case 3:
			if (data != null) {
				Uri uri = data.getData();
				crop(uri, 200);
			}
			break;

		case 4:
			crop2(8);
			break;

		case 5:
			if (data != null) {
				Uri uri = data.getData();
				crop(uri, 300);
			}
			break;

		case 6:
			crop2(9);
			break;
		case 7:
			if (data != null) {
				Bitmap bb = data.getParcelableExtra("data");
				try {
					file = saveBitmap(bb);
					url1 = getImgUrl(0);
					setViewVisilible(0);
				} catch (IOException e) {
					e.printStackTrace();
				}
				setBackgroud(bb);
			}
			break;

		case 8:
			if (data != null) {
				Bitmap bb = data.getParcelableExtra("data");
				try {
					file = saveBitmap(bb);
					url2 = getImgUrl(1);
					setViewVisilible(1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				setBackgroud(bb);
			}
			break;

		case 9:
			if (data != null) {
				Bitmap bb = data.getParcelableExtra("data");
				try {
					file = saveBitmap(bb);
					url3 = getImgUrl(2);
					setViewVisilible(1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				setBackgroud(bb);
			}
			break;
		case 100:
			if (data != null) {
				Bitmap bitmap = data.getParcelableExtra("data");
				try {
					file = saveBitmap(bitmap);
					url1 = getImgUrl(0);
					setViewVisilible(0);
				} catch (IOException e) {
					e.printStackTrace();
				}
				setBackgroud(bitmap);
			}
			break;
		case 200:
			if (data != null) {
				Bitmap bitmap = data.getParcelableExtra("data");
				try {
					file = saveBitmap(bitmap);
					url2 = getImgUrl(1);
					setViewVisilible(1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				setBackgroud(bitmap);
			}
			break;
		case 300:
			if (data != null) {
				Bitmap bitmap = data.getParcelableExtra("data");
				try {
					file = saveBitmap(bitmap);
					url3 = getImgUrl(2);
					setViewVisilible(1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				setBackgroud(bitmap);
			}
			break;
		}
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

	public void setBackgroud(Bitmap bitmap) {
		switch (flag) {
		case 1:
			myPhoto.setImageBitmap(bitmap);
			break;

		case 2:
			myPhoto1.setImageBitmap(bitmap);
			break;

		case 3:
			myPhoto2.setImageBitmap(bitmap);
			break;

		}
	}

	public void crop2(int temp) {
		file = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + "portrait.jpg");
		Intent i = new Intent("com.android.camera.action.CROP");
		i.setType("image/*");
		i.setDataAndType(Uri.fromFile(file), "image/jpeg");
		i.putExtra("crop", "true");

		i.putExtra("aspectX", 1);

		i.putExtra("aspectY", 1);

		i.putExtra("outputX", 100);

		i.putExtra("outputY", 100);

		i.putExtra("return-data", true);

		Log.e("tag", tag + "");
		this.startActivityForResult(i, temp);
	}

	private void crop(Uri uri, int temp) {
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

		Log.e("tag", tag + "");
		intent.putExtra("outputFormat", "JPEG");// 图片格式
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
		startActivityForResult(intent, temp);
	}

	public void getValue() {
		name = editName.getText().toString();
		height = editHeight.getText().toString();
		email = editEmail.getText().toString();
		qq = editQQ.getText().toString();
		introduce = editMyself.getText().toString();
		experience = editExperience.getText().toString();
		specilty = majorContent.getText().toString();
	}

	public String getImgUrl(final int i) {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				String json = UploadUtil
						.uploadFile(file, UrlUtil.UPLOAD_IMG_URL + "&id=" + uid
								+ "&table=resume");
				try {

					temp[i] = new JSONObject(json).getString("filename");
					list.add(temp[i]);
					Log.e("temp[i]", temp[i]);
					String result = new JSONObject(json).getString("result");
					if (i == 0) {
						if (result.equals("true")) {
							img1 = 1;
						}
					} else if (i == 1) {
						if (result.equals("true")) {
							img2 = 1;
						}
					} else if (i == 2) {
						if (result.equals("true")) {
							img3 = 1;
						}
					}
					// if (img3 == 1) {
					// Log.e("点击的imageview为：", "3");
					// } else if (img3 != 1 && img2 == 1) {
					// Log.e("点击的imageview为：", "2");
					// } else if (img3 != 1 && img2 != 1 && img1 == 1) {
					// Log.e("点击的imageview为：", "1");
					// }

					// buffer.append(url + ",");

					if (result.equals("true")) {
						tag++;
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
		return temp[i];
	}

	public void setViewVisilible(int tag) {
		if (tag == 0) {
			myPhoto.setVisibility(View.VISIBLE);
			myPhoto1.setVisibility(View.VISIBLE);
			myPhoto2.setVisibility(View.INVISIBLE);
		} else if (tag == 1) {
			myPhoto.setVisibility(View.VISIBLE);
			myPhoto1.setVisibility(View.VISIBLE);
			myPhoto2.setVisibility(View.VISIBLE);
		} else if (tag == 2) {
			myPhoto.setVisibility(View.VISIBLE);
			myPhoto1.setVisibility(View.INVISIBLE);
			myPhoto2.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		file = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + "portrait.jpg");
		if (file.exists()) {
			file.delete();
		}
		File f = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + "temp.png");
		if (f.exists()) {
			f.delete();
		}
	}

	public String setImageProtrait(String url) {
		JSONObject object = new JSONObject();
		String info = null;
		try {
			object.put("uid", uid);
			object.put("picurl", url);
			String result = HttpUtil.postRequst(UrlUtil.SET_IMG_PORTRAIT_URL,
					object);
			String msg = new JSONObject(result).getString("msg");
			info = new JSONObject(result).getString("result");
			Toast.makeText(MyResumeActivity.this, msg, Toast.LENGTH_SHORT)
					.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	public String deleteImage(String url) {
		JSONObject object = new JSONObject();
		String info = null;
		try {
			object.put("uid", uid);
			object.put("delurl", url);
			String result = HttpUtil.postRequst(UrlUtil.DELETE_IMG_URL, object);
			String msg = new JSONObject(result).getString("msg");
			info = new JSONObject(result).getString("result");
			Toast.makeText(MyResumeActivity.this, msg, Toast.LENGTH_SHORT)
					.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	public void imageSet() {
		switch (list.size()) {
		case 0:
			Log.e("aaaaaaaaaaa", "aaaaaaaaaaa1");
			img1 = 0;
			setViewVisilible(2);
			myPhoto.setImageResource(R.drawable.resume_add_img);
			break;

		case 1:
			Log.e("aaaaaaaaaaa", "aaaaaaaaaaa2");
			setViewVisilible(0);
			img2 = 0;
			myPhoto1.setImageResource(R.drawable.resume_add_img);
			ImageListener imageListener = ImageLoader.getImageListener(myPhoto,
					R.drawable.resume_add_img, R.drawable.resume_add_img);
			imageLoader.get(UrlUtil.SITEURL + list.get(0), imageListener);
			break;

		case 2:
			Log.e("aaaaaaaaaaa", "aaaaaaaaaaa3");
			setViewVisilible(1);
			img3 = 0;
			myPhoto2.setImageResource(R.drawable.resume_add_img);
			ImageListener imageListener1 = ImageLoader.getImageListener(
					myPhoto, R.drawable.resume_add_img,
					R.drawable.resume_add_img);
			imageLoader.get(UrlUtil.SITEURL + list.get(0), imageListener1);
			ImageListener imageListener2 = ImageLoader.getImageListener(
					myPhoto1, R.drawable.resume_add_img,
					R.drawable.resume_add_img);
			imageLoader.get(UrlUtil.SITEURL + list.get(1), imageListener2);
			break;
		}
	}

	@Override
	public boolean onLongClick(View v) {
		switch (v.getId()) {
		case R.id.resume_my_photo:
			if (img1 == 1) {
				int count = list.size();
				Log.e("counts", count + "a");
				DialogUtil.setImageProperty(MyResumeActivity.this,
						new DialogListener() {

							@Override
							public void refreshUI(String string, String key) {

							}

							@Override
							public void refreshUI(String string) {
								if (string.equals("1")) {
									setImageProtrait(temp[0]);
								}
								if (string.equals("2")) {
									String a = deleteImage(temp[0]);
									if (a.equals("true")) {
										list.remove(0);
										int count = list.size();
										Log.e("counts", count + "c");
										imageSet();
									}
								}
							}
						});
			}
			break;

		case R.id.resume_my_photo1:
			if (img2 == 1) {

				DialogUtil.setImageProperty(MyResumeActivity.this,
						new DialogListener() {

							@Override
							public void refreshUI(String string, String key) {

							}

							@Override
							public void refreshUI(String string) {
								if (string.equals("1")) {
									setImageProtrait(temp[1]);
								}
								if (string.equals("2")) {
									deleteImage(temp[1]);
									list.remove(1);
									imageSet();
								}
							}
						});

			}
			break;

		case R.id.resume_my_photo2:
			if (img3 == 1) {

				DialogUtil.setImageProperty(MyResumeActivity.this,
						new DialogListener() {

							@Override
							public void refreshUI(String string, String key) {

							}

							@Override
							public void refreshUI(String string) {
								if (string.equals("1")) {
									setImageProtrait(temp[2]);
								}
								if (string.equals("2")) {
									deleteImage(temp[2]);
									list.remove(2);
									imageSet();
								}
							}
						});

			}
			break;
		}

		return false;
	}
}
