package cn.com.youyouparttime.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.com.youyouparttime.CommentActivity;
import cn.com.youyouparttime.R;
import cn.com.youyouparttime.entity.PartTimeInfo;
import cn.com.youyouparttime.entity.TempValue;
import cn.com.youyouparttime.entity.UrlUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class CommonUtil {

	private static final CommonLog log = LogFactory.createLog();

	public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}
		return true;
	}

	public static String getRootFilePath() {
		if (hasSDCard()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/";// filePath:/sdcard/
		} else {
			return Environment.getDataDirectory().getAbsolutePath() + "/data/"; // filePath:
																				// /data/data/
		}
	}
	
	
	public static void showDialog(ProgressDialog progDialog) {
//		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
//		progDialog.setMessage("正在获取地址");
		progDialog.show();
	}
	
	
	public static void dismissDialog(ProgressDialog progDialog) {
		
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	public static boolean checkNetState(Context context) {
		boolean netstate = false;
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						netstate = true;
						break;
					}
				}
			}
		}
		return netstate;
	}

	public static void showToask(Context context, String tip) {
		Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
	}

	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}

	private Toast mToast;

	public static int isLogin(Context context) {
		SharedPreferences myPrefer = context
				.getSharedPreferences("myPrefer", 0);
		int loginMode = myPrefer.getInt("isLogin", 0);
		Log.e("islogin", loginMode + "'");
		return loginMode;
	}

	public void showToast(Context context, String text) {
		if (mToast == null) {
			mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

	public static List<String> getKey(Map<String, String> map, String value) {
		List<String> list = new ArrayList<String>();
		@SuppressWarnings("rawtypes")
		Set set = map.entrySet();
		String key = null;
		@SuppressWarnings("rawtypes")
		Iterator it = set.iterator();
		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) it.next();
			if (entry.getValue().equals(value)) {
				key = (String) entry.getKey();
				list.add(key);
			}
		}
		return list;
	}

	public static PartTimeInfo getDetailInfo(String id, String url) {

		JSONObject object = new JSONObject();
		PartTimeInfo info = new PartTimeInfo();
		JSONObject b = new JSONObject();
		JSONObject c = new JSONObject();
		try {
			object.put("id", id);
			Log.e("id", id);
			String result = HttpUtil.postRequst(url, object);
			JSONObject a = new JSONObject(result);
			b = a.getJSONObject("job");
			c = b.getJSONObject("0");

			String comName = c.getString("com_name");
			String jobArea = c.getString("city_name");
			String jobStatus = c.getString("show_status");
			String jobid = c.getString("jobid");
			String cid = c.getString("cid");
			String contactPhone = c.getString("job_linkphone");
			String contactEmail = c.getString("job_linkemail");
			String focusCount = c.getString("jobhits");
			String jobType = c.getString("jobclass");
			String jobTime = c.getString("jobtime");
			String releaseTime = c.getString("sdate");
			String jobContent = c.getString("description");
			String jobName = c.getString("name");
			String jobPay = c.getString("salary");
			String jobPeopleNumber = c.getString("job_num");
			String companyPerson = c.getString("com_linkman");
			String companyAdress = c.getString("com_address");
			String companyIntroduce = c.getString("com_content");
			String companyPhone = c.getString("com_linkphone");
			String contactPerson = c.getString("job_linkman");
			String jobAreaDetail = c. getString("address");

			String isCollect = b.getString("is_fav");
			String tips = b.getString("wxts");
			if (isCollect.equals("0")) {
				info.setCollect(false);
			} else {
				info.setCollect(true);
			}
			info.setCompanyName(comName);
			info.setJobArea(jobArea);

			info.setJobStatus(jobStatus);
			info.setCid(cid);
			info.setJobid(jobid);
			info.setJobName(jobName);
			info.setJobReleaseTime(releaseTime);
			info.setPeopleCount(focusCount);
			info.setJobType(jobType);
			info.setJobCount(jobPeopleNumber);
			info.setJobReward(jobPay);
			info.setJobWorkTime(jobTime);
			info.setJobContent(jobContent);
			info.setCompanyIntroduce(companyIntroduce);
			info.setCompanyPersonInCharge(companyPerson);
			info.setCompanyPhone(companyPhone);
			info.setCompanyAdress(companyAdress);
			info.setContactPerson(contactPerson);
			info.setContactPhone(contactPhone);
			info.setContactEmail(contactEmail);
			info.setTips(tips);
			info.setJobAreaDetail(jobAreaDetail);
			Log.e("result", result);
			Log.e("job", b.toString());
			Log.e("0", c.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			JSONArray d = new JSONArray();
			JSONArray e = new JSONArray();
			d = b.getJSONArray("comment_count");
			e = b.getJSONArray("comment_list");

			int count = e.length();
			if (d != null | d.length() > 0) {
				for (int i = 0; i < d.length(); i++) {
					String commtype = d.getJSONObject(i).getString("commtype");
					String typeCount = d.getJSONObject(i).getString("count");
					if (commtype.equals("5")) {
						info.setCommentGoodCount(typeCount);
					} else if (commtype.equals("6")) {
						info.setCommentNormalCount(typeCount);
					} else if (commtype.equals("7")) {
						info.setCommentBadCount(typeCount);
					}
				}
			}
			if (e != null | e.length() > 0) {
				for (int j = 0; j < e.length(); j++) {
					String name = e.getJSONObject(j).getString("name");
					String content = e.getJSONObject(j).getString("content");
					if (j == 0) {
						info.setUser1(name);
						info.setUser1Comment(content);
					} else if (j == 1) {
						info.setUser2(name);
						info.setUser2Comment(content);

					} else if (j == 2) {
						info.setUser3(name);
						info.setUser3Comment(content);
					}
				}
			}
			String commentCount = String.valueOf(count);
			info.setCommentCount(commentCount);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return info;
	}

	public static String nullToEmpty(String str) {
		String string = "";
		if (str == null) {
			str = string;
		}
		return str;
	}

	public static Map<String, String> getData(int tag, String param) {
		Map<String, String> map = new HashMap<String, String>();
		String url = null;
		try {
			url = getUrl(tag);
			String result = HttpUtil.post(url);
			JSONObject resultJson = new JSONObject(result);
			Log.e("aaaa", resultJson.toString());
			JSONObject typeJson;

			if (resultJson != null) {
				typeJson = resultJson.getJSONObject(param);
				@SuppressWarnings("rawtypes")
				Iterator it = typeJson.keys();
				while (it.hasNext()) {
					String key = String.valueOf(it.next());
					String value = typeJson.getString(key);
					map.put(key, value);
				}

				return map;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}
	
	public static List<TempValue> getListData(int tag, String param){
		List<TempValue> temp = new ArrayList<TempValue>();
		String url = null;
		try {
			url = getUrl(tag);
			String result = HttpUtil.post(url);
			JSONObject resultJson = new JSONObject(result);
			Log.e("object", resultJson.toString());
			JSONArray typeJson = resultJson.getJSONArray(param);
			if (resultJson != null) {
				for (int i = 0; i < typeJson.length(); i++) {
					TempValue value = new TempValue();
					String id = typeJson.getJSONObject(i).getString("id");
					String name = typeJson.getJSONObject(i).getString("name");
					String sortStr = typeJson.getJSONObject(i).getString("sort");
					int sort = Integer.parseInt(sortStr);
					value.setId(id);
					value.setName(name);
					value.setSort(sort);
					temp.add(value);
					Collections.sort(temp);
				}
				return temp;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}
	
	
	public static Map<String, String> getData(int tag, String param, String name) {
		Map<String, String> map = new HashMap<String, String>();
		String url = null;
		try {
			url = getUrl(tag);
			JSONObject object = new JSONObject();
			object.put("name", name);
			String result = HttpUtil.postRequst(url, object);
			JSONObject resultJson = new JSONObject(result);
			Log.e("aaaa", resultJson.toString());
			JSONObject typeJson;

			if (resultJson != null) {
				typeJson = resultJson.getJSONObject(param);
				@SuppressWarnings("rawtypes")
				Iterator it = typeJson.keys();
				while (it.hasNext()) {
					String key = String.valueOf(it.next());
					String value = typeJson.getString(key);
					map.put(key, value);
				}

				return map;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	public static String getUrl(int tag) {
		String url = null;
		switch (tag) {
		case 1:
			url = UrlUtil.JOB_TYPE_URL;
			break;

		case 2:
			url = UrlUtil.JOB_PROVINCE_URL;
			break;
		case 3:
			url = UrlUtil.JOB_TIME_URL;
			break;
		case 4:
			url = UrlUtil.JOB_PAY_URL;
			break;
		case 5:
			url = UrlUtil.SALARY_URL;
			break;
		case 6:
			url = UrlUtil.COMPANY_INDUSTRY_URL;
			break;
		case 7:
			url = UrlUtil.ORGANISATION_URL;
			break;
		case 8:
			url = UrlUtil.SCALE_URL;
			break;
		case 9:
			url = UrlUtil.GET_SPECIALTY_URL;
			break;
			
		case 10:
			url = UrlUtil.GET_STUDENT_URL;
			break;
		}
		return url;
	}

	public static Map<String, String> queryCity(String url, String key) {
		JSONObject jsonObject = new JSONObject();
		JSONObject resultJson;
		Map<String, String> map = new HashMap<String, String>();
		try {
			jsonObject.put("id", key);
			String result = HttpUtil.postRequst(url, jsonObject);
			resultJson = new JSONObject(result);
			JSONObject city = resultJson.getJSONObject("city");
			Iterator it = city.keys();
			while (it.hasNext()) {
				String valueKey = String.valueOf(it.next());
				String value = city.getString(valueKey);
				map.put(valueKey, value);
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public static void gotoComment(int typeCode, PartTimeInfo info,
			Activity context, String uid, String usertype, String text) {

		String jobId = info.getJobid();
		String cId = info.getCid();

		Intent intent = new Intent(context, CommentActivity.class);
		intent.putExtra("jobid", jobId);
		intent.putExtra("cid", cId);
		intent.putExtra("uid", uid);
		intent.putExtra("usertype", usertype);
		intent.putExtra("text", text);
		intent.putExtra("type", typeCode);// 跳转页面类型，0为评论页，1为投诉页；
		context.startActivity(intent);
	}

	public static void cleanCache() {

	}

	public static String strToInt(String str) {
		String result = null;
		if (str == null || str.length() == 0) {
			return "";
		}
		if (str.length() == 1 | str.length() == 2) {
			int day = Integer.valueOf(str);
			int dayInt = day / 3;
			int a = day % 3;
			String b = null;
			String c = null;
			if (a == 0) {
				b = toDay(dayInt);
			} else {
				b = toDay(dayInt + 1);
			}

			c = dayOrNight(a);
			result = b + c;
			Log.e("tag", "tag");
			return result;
		} else {
			String[] days = str.split(",");
			Log.e("size", "size");
			Log.e("days", days.toString());
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < days.length; i++) {
				String a = strToInt(days[i]);

				buffer.append(a);
			}
			result = buffer.toString();
			return result;
		}
	}

	public static String toDay(int dayInt) {
		String day = null;
		switch (dayInt) {
		case 1:
			day = "星期一";
			break;

		case 2:
			day = "星期二";
			break;
		case 3:
			day = "星期三";
			break;
		case 4:
			day = "星期四";
			break;
		case 5:
			day = "星期五";
			break;
		case 6:
			day = "星期六";
			break;
		case 7:
			day = "星期日";
			break;
		}
		return day;
	}

	public static String dayOrNight(int a) {
		String night = null;
		switch (a) {
		case 0:
			night = "晚上";
			break;
		case 1:
			night = "上午";
			break;

		case 2:
			night = "下午";
			break;
		}
		return night;
	}

	public static void touchListener(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			v.setBackgroundColor(Color.parseColor("#fa9600"));

			break;
		case MotionEvent.ACTION_UP:
			v.setBackgroundColor(Color.WHITE);
			break;
		}
	}

	public static int setJobTypeImg(String type) {
		int imgId = R.drawable.cuxiao;
		if (type.equals("安保")) {
			imgId = R.drawable.anbao;
		} else if (type.equals("促销")) {
			imgId = R.drawable.cuxiao;
		} else if (type.equals("导游")) {
			imgId = R.drawable.daoyou;
		} else if (type.equals("服务")) {
			imgId = R.drawable.fuwu;
		} else if (type.equals("话务")) {
			imgId = R.drawable.huawu;
		} else if (type.equals("活动执行")) {
			imgId = R.drawable.huodongzhixing;
		} else if (type.equals("家教")) {
			imgId = R.drawable.jiajiao;
		} else if (type.equals("教师")) {
			imgId = R.drawable.jiaoshi;
		} else if (type.equals("客服")) {
			imgId = R.drawable.kefu;
		} else if (type.equals("临时工")) {
			imgId = R.drawable.linshigong;
		} else if (type.equals("礼仪")) {
			imgId = R.drawable.liyi;
		} else if (type.equals("模特")) {
			imgId = R.drawable.mote;
		} else if (type.equals("派发")) {
			imgId = R.drawable.paifa;
		} else if (type.equals("其他")) {
			imgId = R.drawable.qita;
		} else if (type.equals("设计")) {
			imgId = R.drawable.sheji;
		} else if (type.equals("收银员")) {
			imgId = R.drawable.shouyingyuan;
		} else if (type.equals("文件调查")) {
			imgId = R.drawable.wenjuandiaocha;
		} else if (type.equals("文员")) {
			imgId = R.drawable.wenyuan;
		} else if (type.equals("销售")) {
			imgId = R.drawable.xiaoshou;
		}
		return imgId;
	}

	public static void uploadImg(final File f) {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				UploadUtil.uploadFile(f, UrlUtil.UPLOAD_IMG_URL);
			}
		});
		thread.start();
	}

	public static String getNumber(String content) {

		Pattern pattern = Pattern.compile("[^0-9]");
		Matcher matcher = pattern.matcher(content);
		String all = matcher.replaceAll("");
		System.out.println("phone:" + all);
		// 2
		String number = Pattern.compile("[^0-9]").matcher(content)
				.replaceAll("");
		return number;
	}

}
