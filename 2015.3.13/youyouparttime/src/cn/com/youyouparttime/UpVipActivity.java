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
import cn.com.youyouparttime.util.CommonUtil;
import cn.com.youyouparttime.util.DialogUtil;
import cn.com.youyouparttime.util.HttpUtil;
import cn.com.youyouparttime.util.UploadUtil;
import cn.com.youyouparttime.util.DialogUtil.DialogListener;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UpVipActivity extends Activity implements OnClickListener{

	private TextView back;
	private ImageView photo;
	private Button submit;
	private File file;
	private String imgUrl;
	private String uid;
	private SharedPreferences shared;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_up_vip);
		SysApplication.getInstance().addActivity(this);
		back = (TextView) findViewById(R.id.add_vip_back);
		photo = (ImageView) findViewById(R.id.stu_id_photo);
		submit = (Button) findViewById(R.id.add_vip_submit);
		shared = getSharedPreferences(Constant.STUDENT_PREFER, 0);
		uid = shared.getString("uid", null);
		back.setOnClickListener(this);
		photo.setOnClickListener(this);
		submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_vip_back:
			finish();
			break;

		case R.id.stu_id_photo:
			DialogUtil.showDialog(UpVipActivity.this, new DialogListener() {

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
						startActivityForResult(intent, 1);
					} else {
						file = null;
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
			
		case R.id.add_vip_submit:
			JSONObject object = new JSONObject();
			String msg = "";
			try {
				object.put("uid", uid);
				object.put("img", imgUrl);
				String result = HttpUtil.postRequst(UrlUtil.ADD_VIP_URL, object);
				msg = new JSONObject(result).getString("msg");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			Toast.makeText(UpVipActivity.this, msg, Toast.LENGTH_SHORT).show();
			finish();
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

			file = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + File.separator + "portrait.jpg");
			Intent i = new Intent("com.android.camera.action.CROP");
			i.setType("image/*");
			i.setDataAndType(Uri.fromFile(file), "image/jpeg");
			i.putExtra("crop", "true");

			i.putExtra("aspectX", 1);

			i.putExtra("aspectY", 1);

			i.putExtra("outputX", 250);

			i.putExtra("outputY", 250);

			i.putExtra("return-data", true);
			this.startActivityForResult(i, 7);
			break;
		case 7:
			Bitmap bb = data.getParcelableExtra("data");
			try {
				File file = saveBitmap(bb);
				getImgUrl(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			photo.setImageBitmap(bb);
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
				photo.setImageBitmap(bitmap);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	
	public String getImgUrl(final File file){
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				String json = UploadUtil.uploadFile(file, UrlUtil.UPLOAD_IMG_URL);
				try {
					
					imgUrl = new JSONObject(json).getString("filename");
					String result = new JSONObject(json).getString("result");
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
		return imgUrl;
	}
	
	public File saveBitmap(Bitmap map) throws IOException{
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
		intent.putExtra("outputFormat", "JPEG");// 图片格式
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
		String path = uri.getPath();
		file = new File(path);
//		CommonUtil.uploadImg(file);
		startActivityForResult(intent, 100);
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
