package cn.com.youyouparttime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.CommonUtil;
import cn.com.youyouparttime.util.HttpUtil;
import cn.com.youyouparttime.util.UploadUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ThankfulActivity extends Activity implements OnClickListener{

	
	private LinearLayout back;
	private Button submit;
	private int mode;
	private EditText editInfo;
	private String uid;
	SharedPreferences share;
	String editContent;
	private ImageView addView;
	private String imgUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thankful);
		Intent intent = getIntent();
		mode = intent.getIntExtra("mode", -1);
		share = getSharedPreferences("myPrefer", 0);
		uid = share.getString("uid", null);
		initView();
	}

	public void initView(){
		back = (LinearLayout) findViewById(R.id.thankful_back);
		submit = (Button) findViewById(R.id.thankful_submit);
		editInfo = (EditText) findViewById(R.id.thankful_edit);
		addView = (ImageView) findViewById(R.id.add_img);
		addView.setOnClickListener(this);
		back.setOnClickListener(this);
		submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.thankful_back:
			finish();
			break;

		case R.id.thankful_submit:
			JSONObject object = new JSONObject();
			editContent = editInfo.getText().toString();
			String value = String.valueOf(mode);
			try {
				object.put("uid", uid);
				object.put("ask_img", imgUrl);
				object.put("ask_content", editContent);
				object.put("type", value);
				Log.e("uploadData", object.toString());
				String result = HttpUtil.postRequst(getUrl(mode), object);
				String msg = new JSONObject(result).getString("msg");
				Toast.makeText(ThankfulActivity.this, msg, Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.add_img:
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			startActivityForResult(intent, 1);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
		case 100:
			if (data != null) {
				Bitmap bitmap = data.getParcelableExtra("data");
				try {
					File file = saveBitmap(bitmap);
					getImgUrl(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
				addView.setImageBitmap(bitmap);
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
			startActivityForResult(intent, 100);
		}
	
	public String getUrl(int value) {
		String url = null;
		switch (value) {
		case 1:
			url = UrlUtil.THANKFUL_PARENTS_URL;
			break;

		case 2:
			url = UrlUtil.THANKFUL_SOCIETY_URL;
			break;
		}
		return url;
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
