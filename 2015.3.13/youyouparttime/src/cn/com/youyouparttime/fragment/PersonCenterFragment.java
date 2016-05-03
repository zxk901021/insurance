package cn.com.youyouparttime.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URLEncoder;


import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.ImageLoader.ImageListener;

import cn.com.youyouparttime.ChangePasswordActivity;
import cn.com.youyouparttime.GrowActivity;
import cn.com.youyouparttime.HtmlResumeActivity;
import cn.com.youyouparttime.LoginForStudentsActivity;
import cn.com.youyouparttime.MoreActivity;
import cn.com.youyouparttime.MsgListActivity;
import cn.com.youyouparttime.MyResumeActivity;
import cn.com.youyouparttime.NewestJobActivity;
import cn.com.youyouparttime.PartTimePrideActivity;
import cn.com.youyouparttime.R;
import cn.com.youyouparttime.SafetyActivity;
import cn.com.youyouparttime.SuggestActivity;
import cn.com.youyouparttime.YouDetailActivity;
import cn.com.youyouparttime.entity.Constant;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.BitmapCache;
import cn.com.youyouparttime.util.CommonUtil;
import cn.com.youyouparttime.util.DialogUtil;
import cn.com.youyouparttime.util.DialogUtil.DialogListener;
import cn.com.youyouparttime.util.HttpUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PersonCenterFragment extends Fragment implements OnClickListener {

	private TextView logout;
	private int isLogin;
	private RelativeLayout gotoEditResume;
	private TextView studentUsername, studentPhone;
	LayoutInflater inflater;
	SharedPreferences preferences;
	Editor editor;
	private ImageView photo;
	private RelativeLayout myCollect, myApply, myMsg, myPride, mySafe, myGrow,
//			changePassword, aboutPartTime, 
			introduce, suggestion, clearCache,
			newVirsion, more;
	String uid, username, userphone;
	private boolean hasSubmitResume;
	private String imgPath;
	private String userType;
	private String zl,thankParent,thankSociety,integrity;
	private String sumRewards;
	RequestQueue queue;
	ImageLoader imageLoader;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_person_center, container,
				false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	public void initView() {
		isLogin = CommonUtil.isLogin(getActivity());
		preferences = getActivity().getSharedPreferences("myPrefer", 0);
		editor = preferences.edit();
		uid = preferences.getString("uid", null);
		userType = preferences.getString("usertype", null);
		username = preferences.getString("personname", "");
		userphone = preferences.getString("username", "");
		logout = (TextView) getActivity().findViewById(R.id.logout);
		String a = isLogin == 0 ? "登陆" : "注销";
		logout.setText(a);
		logout.setOnClickListener(this);
		photo = (ImageView) getActivity().findViewById(R.id.photo);
		gotoEditResume = (RelativeLayout) getActivity().findViewById(
				R.id.photo_bg);
		gotoEditResume.setOnClickListener(this);
//		photo.setOnClickListener(this);
		myCollect = (RelativeLayout) getActivity().findViewById(
				R.id.person_my_collect);
		myApply = (RelativeLayout) getActivity().findViewById(
				R.id.person_my_apply);
		myMsg = (RelativeLayout) getActivity().findViewById(R.id.person_my_msg);
		myPride = (RelativeLayout) getActivity().findViewById(
				R.id.person_my_pride);
		mySafe = (RelativeLayout) getActivity().findViewById(
				R.id.person_my_safe);
		myGrow = (RelativeLayout) getActivity().findViewById(
				R.id.person_my_grow);
		more = (RelativeLayout) getActivity().findViewById(R.id.person_more);
//		changePassword = (RelativeLayout) getActivity().findViewById(
//				R.id.person_change_password);
//		aboutPartTime = (RelativeLayout) getActivity().findViewById(
//				R.id.person_about);
		introduce = (RelativeLayout) getActivity().findViewById(
				R.id.person_introduce);
		suggestion = (RelativeLayout) getActivity().findViewById(
				R.id.person_suggestion);
		clearCache = (RelativeLayout) getActivity().findViewById(
				R.id.person_clear_cache);
		newVirsion = (RelativeLayout) getActivity().findViewById(
				R.id.person_new_virson);
		studentUsername = (TextView) getActivity().findViewById(
				R.id.student_username);
		studentPhone = (TextView) getActivity()
				.findViewById(R.id.student_phone);
		
		if (CommonUtil.isLogin(getActivity()) != 0) {
			getInfo();
			studentUsername.setText(username);
			studentPhone.setText(userphone);
		}
		editor.putString("zl", zl);
		editor.putString("parent", thankParent);
		editor.putString("society", thankSociety);
		editor.putString("cx", integrity);
		editor.putString("sumRewards", sumRewards);
		editor.commit();

		more.setOnClickListener(this);
		myCollect.setOnClickListener(this);
		myApply.setOnClickListener(this);
		myMsg.setOnClickListener(this);
		myPride.setOnClickListener(this);
		mySafe.setOnClickListener(this);
		myGrow.setOnClickListener(this);
//		changePassword.setOnClickListener(this);
//		aboutPartTime.setOnClickListener(this);
		introduce.setOnClickListener(this);
		suggestion.setOnClickListener(this);
		clearCache.setOnClickListener(this);
		newVirsion.setOnClickListener(this);

		inflater = getActivity().getLayoutInflater();
		
		if (imgPath == null || imgPath.length() == 0) {
			
		}else {
			queue = Volley.newRequestQueue(getActivity());
			imageLoader = new ImageLoader(queue, new BitmapCache());
			ImageListener imageListener = ImageLoader.getImageListener(
					photo, R.drawable.resume_add_img, R.drawable.resume_add_img);
			imageLoader.get(UrlUtil.SITEURL + imgPath, imageListener);
		}
		
		
	}
	
	
	public void getInfo(){
		JSONObject object = new JSONObject();
		JSONObject infoJson = new JSONObject();
		try {
			object.put("uid", uid);
			String result = HttpUtil.postRequst(UrlUtil.STUDENT_INFO_URL, object);
			infoJson = new JSONObject(result);
			imgPath = infoJson.getJSONObject("userinfo").getString("photo");
			username = infoJson.getJSONObject("userinfo").getString("name");
			zl = infoJson.getJSONObject("userinfo").getString("zl");
			thankParent = infoJson.getJSONObject("userinfo").getString("ge_parent");
			thankSociety = infoJson.getJSONObject("userinfo").getString("ge_society");
			integrity = infoJson.getJSONObject("userinfo").getString("cx");
			sumRewards = infoJson.getJSONObject("userinfo").getString("all_reward");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.person_more:
			Intent moreIntent = new Intent(getActivity(), MoreActivity.class);
			startActivity(moreIntent);
			break;
			
		case R.id.logout:
			if (logout.getText().equals("注销")) {
				editor.putInt("isLogin", 0);
				editor.commit();
			}
			Intent intent = new Intent(getActivity(),
					LoginForStudentsActivity.class);
			startActivity(intent);
			getActivity().finish();
			break;

		case R.id.photo:
			DialogUtil.showDialog(getActivity(), new DialogListener() {

				@Override
				public void refreshUI(String string) {
					Log.e("string", string + "2");
					if (string.equals("1")) {
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						intent.setType("image/*");
						// intent.putExtra("crop", "true");
						// intent.putExtra("aspectX", 1);
						// intent.putExtra("aspectY", 1);
						// intent.putExtra("outputX", 128);
						// intent.putExtra("outputY", 128);
						// intent.putExtra("return-data", true);
						Log.e("activity", getActivity().toString());
						startActivityForResult(intent, 1);
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
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

						startActivityForResult(intent, 2);
					}
				}

				@Override
				public void refreshUI(String string, String key) {

				}
			});
			break;

		case R.id.photo_bg:
			if (isLogin == 0) {
				Toast.makeText(getActivity(), "请先登录！", 1).show();
				return;
			}
			hasSubmitResume = preferences.getBoolean("hasSubmit", false);
			if (hasSubmitResume) {
				Intent htmlIntent = new Intent(getActivity(),
						HtmlResumeActivity.class);
				htmlIntent.putExtra("uid", uid);
				htmlIntent.putExtra("sqid", uid);
				startActivity(htmlIntent);
				return;
			} else {
				Intent editResumeIntent = new Intent(getActivity(),
						MyResumeActivity.class);
				startActivity(editResumeIntent);
			}
			break;

		case R.id.person_my_collect:
			if (isLogin == 0) {
				DialogUtil.ifLoginDialog(getActivity());
				return;
			}
			Intent collectIntent = new Intent(getActivity(),
					NewestJobActivity.class);
			collectIntent.putExtra("uid", uid);
			collectIntent.putExtra("title", "我的收藏");
			collectIntent.putExtra("mode", Constant.MY_COLLECT_MODE);
			collectIntent.putExtra("key", Constant.MY_COLLECT_KEY);
			startActivity(collectIntent);
			break;

		case R.id.person_my_apply:
			if (isLogin == 0) {
				DialogUtil.ifLoginDialog(getActivity());
				return;
			}
			Intent applyIntent = new Intent(getActivity(),
					NewestJobActivity.class);
			applyIntent.putExtra("uid", uid);
			applyIntent.putExtra("title", "我的申请");
			applyIntent.putExtra("mode", Constant.MY_APPLY_MODE);
			applyIntent.putExtra("key", Constant.MY_APPLY_KEY);
			startActivity(applyIntent);
			break;

		case R.id.person_my_msg:
			if (CommonUtil.isLogin(getActivity()) == 0) {
				DialogUtil.ifLoginDialog(getActivity());
				return;
			}
			Intent msgIntent = new Intent(getActivity(), MsgListActivity.class);
			msgIntent.putExtra("commenttype", 0);
			msgIntent.putExtra("usertype", userType);
			startActivity(msgIntent);
			break;

		case R.id.person_my_pride:
			if (CommonUtil.isLogin(getActivity()) == 0) {
				DialogUtil.ifLoginDialog(getActivity());
				return;
			}
			Intent prideIntent = new Intent(getActivity(),
					PartTimePrideActivity.class);
			startActivity(prideIntent);
			break;

		case R.id.person_my_grow:
			if (CommonUtil.isLogin(getActivity()) == 0) {
				DialogUtil.ifLoginDialog(getActivity());
				return;
			}
			Intent growIntent = new Intent(getActivity(), GrowActivity.class);
			startActivity(growIntent);
			break;

		case R.id.person_my_safe:
			if (CommonUtil.isLogin(getActivity()) == 0) {
				DialogUtil.ifLoginDialog(getActivity());
				return;
			}
			Intent safeIntent = new Intent(getActivity(), SafetyActivity.class);
			startActivity(safeIntent);
			break;

		case R.id.person_change_password:
			if (CommonUtil.isLogin(getActivity()) == 0) {
				DialogUtil.ifLoginDialog(getActivity());
				return;
			}
			Intent changeIntent = new Intent(getActivity(),
					ChangePasswordActivity.class);
			startActivity(changeIntent);
			break;

		case R.id.person_about:
			Intent aboutIntent = new Intent(getActivity(),
					YouDetailActivity.class);
			aboutIntent.putExtra("title", "关于悠悠兼职");
			aboutIntent.putExtra("catid", "20");
			startActivity(aboutIntent);
			break;

		case R.id.person_introduce:
			Intent introIntent = new Intent(getActivity(),
					YouDetailActivity.class);
			introIntent.putExtra("title", "功能介绍");
			introIntent.putExtra("catid", "21");
			startActivity(introIntent);
			break;

		case R.id.person_suggestion:
			Intent suggestIntent = new Intent(getActivity(),
					SuggestActivity.class);
			startActivity(suggestIntent);
			break;

		case R.id.person_clear_cache:
			Toast.makeText(getActivity(), "缓存清理成功!", Toast.LENGTH_SHORT).show();
			break;

		case R.id.person_new_virson:
			try {
				String result = HttpUtil.post(UrlUtil.DETECTION_VERSION_URL);
				String versionName = new JSONObject(result).getJSONObject(
						"result").getString("versionName");
				String downUrl = new JSONObject(result).getJSONObject("result").getString("down_url");
				PackageInfo info = getActivity().getPackageManager()
						.getPackageInfo(getActivity().getPackageName(), 0);
				String version = info.versionName;
				Log.e("version", result);
				if (version.equals(versionName)) {
					Toast.makeText(getActivity(), "已是最新版本!", Toast.LENGTH_SHORT)
							.show();
				} else {
					DialogUtil.uploadApp(getActivity(), "http://tjtianxiang.com/Clinical.apk");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case 1:
			if (data != null) {
				Uri uri = data.getData();
				crop(uri);

			}
			break;

		case 2:

			final File f = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + File.separator + "portrait.jpg");
			Intent i = new Intent("com.android.camera.action.CROP");
			i.setType("image/*");
			i.setDataAndType(Uri.fromFile(f), "image/jpeg");
			i.putExtra("crop", "true");

			i.putExtra("aspectX", 1);

			i.putExtra("aspectY", 1);

			i.putExtra("outputX", 100);

			i.putExtra("outputY", 100);

			i.putExtra("return-data", true);
			CommonUtil.uploadImg(f);

			this.startActivityForResult(i, 7);
			break;
		case 7:
			Bitmap bb = data.getParcelableExtra("data");
			photo.setImageBitmap(bb);
			break;
		case 100:
			if (data != null) {
				Bitmap bitmap = data.getParcelableExtra("data");
				photo.setImageBitmap(bitmap);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void crop(Uri uri) {
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
		Log.e("uri", uri.toString() + "****"
				+ Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator + "portrait.jpg");
		intent.putExtra("outputFormat", "JPEG");// 图片格式
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
		String path = uri.getPath();
		File file = new File(path);
		CommonUtil.uploadImg(file);
		startActivityForResult(intent, 100);
	}

	public byte[] bitmapToByte(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
		return os.toByteArray();
	}

	public String byteToBase64(byte[] bs) {
		String tString = "";
		String tSenString = "";
		tString = Base64.encodeToString(bs, 0);
		try {
			tSenString = URLEncoder.encode(tString, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tString;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		File f = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + "portrait.jpg");
		if (f.exists()) {
			f.delete();
		}
	}

}
