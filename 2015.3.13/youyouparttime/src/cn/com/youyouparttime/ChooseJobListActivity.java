package cn.com.youyouparttime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import cn.com.youyouparttime.adapter.SimpleListAdapter;
import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.AreaEntity;
import cn.com.youyouparttime.entity.TempValue;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.CommonUtil;
import cn.com.youyouparttime.util.DialogUtil;
import cn.com.youyouparttime.util.HttpUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class ChooseJobListActivity extends Activity implements OnClickListener {

	private int flag, tag;
	private int count = 0;
	private ListView list;
	private LinearLayout guideLayout;
	private ScrollView timeScroll;
	private Button ensureBtn;
	private TextView guideTitle;
	private ImageView img1,img2,img3,img4,img5,img6,img7,img8,img9,
						img10,img11,img12,img13,img14,img15,img16,img17,
							img18,img19,img20,img21;
	private String title, param;
	SimpleListAdapter adapter;
	Map<String, String> map = new HashMap<String, String>();
	List<String> data;
	String key;
	String value;
	private String timeKey;
	boolean[] a = new boolean[21];
	String[] times;
	List<AreaEntity> entity;
	List<TempValue> tempValues;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_job_list);
		SysApplication.getInstance().addActivity(this);
		initView();
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		guideTitle.setText(title);
		flag = intent.getIntExtra("flag", -1);
		tag = intent.getIntExtra("tag", 0);
		param = intent.getStringExtra("param");
		timeKey = intent.getStringExtra("time");
		Log.e("timeKey", timeKey + "'");
		if (timeKey != null && timeKey.length() > 0) {
			times = timeKey.split(",");
			
			for (int i = 0; i < times.length; i++) {
				Log.e("timeKey", times[i] + "'");
				int temp = Integer.parseInt(times[i]);
				a[temp-1] = true;
				setBackground(temp);
			}
		}
		switch (flag) {
		case 0:
			list.setVisibility(View.VISIBLE);
			if (tag == 2) {
//				map = queryCity(UrlUtil.JOB_CITY_URL, "38");
				entity = DialogUtil.getArea();
				data = new ArrayList<String>();
				for (int i = 0; i < entity.size(); i++) {
//					Log.e("sort", entity.get(i).getSort()+"");
//					String value = entity.get(i).getArea();
//					String key = entity.get(i).getAreaId();
//					map.put(key, value);
					data.add(entity.get(i).getArea());
				}
				adapter = new SimpleListAdapter(this, data);
			}else {
				tempValues = CommonUtil.getListData(tag, param);
				data = new ArrayList<String>();
				for (int i = 0; i < tempValues.size(); i++) {
					data.add(tempValues.get(i).getName());
				}
				adapter = new SimpleListAdapter(this, data);
//				map = CommonUtil.getData(tag, param);
//				adapter = new SimpleListAdapter(this, map);
			}
			
			
			list.setAdapter(adapter);
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Log.e("TAG", "TAG*TAG*TAG1111111111");
					adapter.notifyDataSetChanged();
					if (tag != 2) {
//						data = new ArrayList<String>(map.values());
						value = tempValues.get(position).getName();
//						List<String> a = CommonUtil.getKey(map, value);
						key = tempValues.get(position).getId();
					}else {
						value = entity.get(position).getArea();
						key = entity.get(position).getAreaId();
					}
					
					final Intent intent = new Intent();
//					if (tag == 2 && count != 2) {
//						map.clear();
//						map = queryCity(UrlUtil.JOB_CITY_URL);
////						adapter.notifyDataSetChanged();
//						adapter = new SimpleListAdapter(ChooseJobListActivity.this, map);
//						adapter.notifyDataSetChanged();
//						list.setAdapter(adapter);
//						list.setOnItemClickListener(new OnItemClickListener() {
//
//							@Override
//							public void onItemClick(AdapterView<?> parent,
//									View view, int position, long id) {
//								Log.e("TAG", "TAG*TAG*TAG22222222222222222");
//								data = new ArrayList<String>(map.values());
//								String aaa = data.get(position);
//								List<String> aa = CommonUtil.getKey(map, aaa);
//								key = aa.get(0);
//								
//								map = queryCity(UrlUtil.JOB_CITY_URL);
//								adapter = new SimpleListAdapter(ChooseJobListActivity.this, map);
//								adapter.notifyDataSetChanged();
//								list.setAdapter(adapter);
//								count = 1;
//								list.setOnItemClickListener(new OnItemClickListener() {
//
//									@Override
//									public void onItemClick(
//											AdapterView<?> parent, View view,
//											int position, long id) {
//										Log.e("TAG", "TAG*TAG*TAG33333333333");
//										data = new ArrayList<String>(map.values());
//										value = data.get(position);
//										String aaa = data.get(position);
//										List<String> aa = CommonUtil.getKey(map, aaa);
//										key = aa.get(0);
//										count = 2;
//										Log.e("keybbbb", key);
//										intent.putExtra("value", value);
//										intent.putExtra("key", key);
//										setResult(tag, intent);
//										finish();
//									}
//								});
//								
//							}
//						});
//						Log.e("keyaaaaa", key);
//						return ;
//						
//					
//						
//						
//					}
					
					intent.putExtra("value", value);
					intent.putExtra("key", key);
					setResult(tag, intent);
					finish();
				}
			});
			break;

		case 1:
			timeScroll.setVisibility(View.VISIBLE);
			break;
		}
		
		
	}

	public void initView() {
		list = (ListView) findViewById(R.id.guide_list);
		ensureBtn = (Button) findViewById(R.id.choose_time_ensure);
		guideTitle = (TextView) findViewById(R.id.guide_title);
		guideLayout = (LinearLayout) findViewById(R.id.guide_back);
		timeScroll = (ScrollView) findViewById(R.id.time_scroll);
		img1 = (ImageView) findViewById(R.id.img_1);
		img2 = (ImageView) findViewById(R.id.img_2);
		img3 = (ImageView) findViewById(R.id.img_3);
		img4 = (ImageView) findViewById(R.id.img_4);
		img5 = (ImageView) findViewById(R.id.img_5);
		img6 = (ImageView) findViewById(R.id.img_6);
		img7 = (ImageView) findViewById(R.id.img_7);
		img8 = (ImageView) findViewById(R.id.img_8);
		img9 = (ImageView) findViewById(R.id.img_9);
		img10 = (ImageView) findViewById(R.id.img_10);
		img11 = (ImageView) findViewById(R.id.img_11);
		img12 = (ImageView) findViewById(R.id.img_12);
		img13 = (ImageView) findViewById(R.id.img_13);
		img14 = (ImageView) findViewById(R.id.img_14);
		img15 = (ImageView) findViewById(R.id.img_15);
		img16 = (ImageView) findViewById(R.id.img_16);
		img17 = (ImageView) findViewById(R.id.img_17);
		img18 = (ImageView) findViewById(R.id.img_18);
		img19 = (ImageView) findViewById(R.id.img_19);
		img20 = (ImageView) findViewById(R.id.img_20);
		img21 = (ImageView) findViewById(R.id.img_21);
		ensureBtn.setOnClickListener(this);
		guideLayout.setOnClickListener(this);
		img1.setOnClickListener(this);
		img2.setOnClickListener(this);
		img3.setOnClickListener(this);
		img4.setOnClickListener(this);
		img5.setOnClickListener(this);
		img6.setOnClickListener(this);
		img7.setOnClickListener(this);
		img8.setOnClickListener(this);
		img9.setOnClickListener(this);
		img10.setOnClickListener(this);
		img11.setOnClickListener(this);
		img12.setOnClickListener(this);
		img13.setOnClickListener(this);
		img14.setOnClickListener(this);
		img15.setOnClickListener(this);
		img16.setOnClickListener(this);
		img17.setOnClickListener(this);
		img18.setOnClickListener(this);
		img19.setOnClickListener(this);
		img20.setOnClickListener(this);
		img21.setOnClickListener(this);
	}



	
	
	// public void getData2(){
	// List<Map<String, String>> list = new ArrayList<Map<String,String>>();
	// Map<String, String> map = new HashMap<String, String>();
	// String url = UrlUtil.JOB_PAY_URL;
	// JSONArray resultJson;
	// List<JSONObject> jsonList;
	// try {
	// // url = getUrl(tag);
	// String result = HttpUtil.post(url);
	// Log.e("result", result);
	// JSONObject arrayResult = new JSONObject(result);
	// resultJson = arrayResult.getJSONArray("accountclass");
	// jsonList = new ArrayList<JSONObject>();
	// JSONObject object = null;
	//
	// if (resultJson != null) {
	// for (int i = 0; i < resultJson.length(); i++) {
	// object = resultJson.getJSONObject(i);
	// @SuppressWarnings("rawtypes")
	// Iterator it = object.keys();
	// while (it.hasNext()) {
	// String key = String.valueOf(it.next());
	// String value = object.getString(key);
	// map.put(key, value);
	//
	// }
	// }
	//
	// // @SuppressWarnings("rawtypes")
	// // Iterator it = typeJson.keys();
	// // while (it.hasNext()) {
	// // String key = String.valueOf(it.next());
	// // String value = typeJson.getString(key);
	// // map.put(key, value);
	// // }
	// //
	// // return map;
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// // return map;
	// }
	
	public void setBackground(int position){
		switch (position) {
		case 1:
			if (a[0]) {
				img1.setImageResource(R.drawable.time_pressed);
			}else {
				img1.setImageResource(R.drawable.time_no_press);
			}
			
			break;

		case 2:
			if (a[1]) {
				img2.setImageResource(R.drawable.time_pressed);
			}else {
				img2.setImageResource(R.drawable.time_no_press);
			}
			
			break;
			
		case 3:
			if (a[2]) {
				img3.setImageResource(R.drawable.time_pressed);
			}else {
				img3.setImageResource(R.drawable.time_no_press);
			}
			
			break;
			
		case 4:
			if (a[3]) {
				img4.setImageResource(R.drawable.time_pressed);
			}else {
				img4.setImageResource(R.drawable.time_no_press);
			}
			
			break;
			
		case 5:
			if (a[4]) {
				img5.setImageResource(R.drawable.time_pressed);
			}else {
				img5.setImageResource(R.drawable.time_no_press);
			}
			
			break;
			
		case 6:
			if (a[5]) {
				img6.setImageResource(R.drawable.time_pressed);
			}else {
				img6.setImageResource(R.drawable.time_no_press);
			}
			
			break;
			
		case 7:
			if (a[6]) {
				img7.setImageResource(R.drawable.time_pressed);
			}else {
				img7.setImageResource(R.drawable.time_no_press);
			}
			
			break;
			
		case 8:
			if (a[7]) {
				img8.setImageResource(R.drawable.time_pressed);
			}else {
				img8.setImageResource(R.drawable.time_no_press);
			}
			
			break;
			
		case 9:
			if (a[8]) {
				img9.setImageResource(R.drawable.time_pressed);
			}else {
				img9.setImageResource(R.drawable.time_no_press);
			}
			
			break;
			
		case 10:
			if (a[9]) {
				img10.setImageResource(R.drawable.time_pressed);
			}else {
				img10.setImageResource(R.drawable.time_no_press);
			}
			
			break;
			
		case 11:
			if (a[10]) {
				img11.setImageResource(R.drawable.time_pressed);
			}else {
				img11.setImageResource(R.drawable.time_no_press);
			}
			
			break;
			
		case 12:
			if (a[11]) {
				img12.setImageResource(R.drawable.time_pressed);
			}else {
				img12.setImageResource(R.drawable.time_no_press);
			}
			
			break;
		case 13:
			if (a[12]) {
				img13.setImageResource(R.drawable.time_pressed);
			}else {
				img13.setImageResource(R.drawable.time_no_press);
			}
			
			break;
			
		case 14:
			if (a[13]) {
				img14.setImageResource(R.drawable.time_pressed);
			}else {
				img14.setImageResource(R.drawable.time_no_press);
			}
			
			break;
			
		case 15:
			if (a[14]) {
				img15.setImageResource(R.drawable.time_pressed);
			}else {
				img15.setImageResource(R.drawable.time_no_press);
			}
			
			break;
			
		case 16:
			if (a[15]) {
				img16.setImageResource(R.drawable.time_pressed);
			}else {
				img16.setImageResource(R.drawable.time_no_press);
			}
			
			break;
			
		case 17:
			if (a[16]) {
				img17.setImageResource(R.drawable.time_pressed);
			}else {
				img17.setImageResource(R.drawable.time_no_press);
			}
			
			break;
			
		case 18:
			if (a[17]) {
				img18.setImageResource(R.drawable.time_pressed);
			}else {
				img18.setImageResource(R.drawable.time_no_press);
			}
			
			break;
			
		case 19:
			if (a[18]) {
				img19.setImageResource(R.drawable.time_pressed);
			}else {
				img19.setImageResource(R.drawable.time_no_press);
			}
			
			break;
			
		case 20:
			if (a[19]) {
				img20.setImageResource(R.drawable.time_pressed);
			}else {
				img20.setImageResource(R.drawable.time_no_press);
			}
			
			break;
			
		case 21:
			if (a[20]) {
				img21.setImageResource(R.drawable.time_pressed);
			}else {
				img21.setImageResource(R.drawable.time_no_press);
			}
			
			break;
		}
	}
	
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.guide_back:
			finish();
			break;

		case R.id.img_1:
			
			if (!a[0]) {
				img1.setImageResource(R.drawable.time_pressed);
				a[0] = true;
			}else {
				img1.setImageResource(R.drawable.time_no_press);
				a[0] = false;
			}
			break;
		case R.id.img_2:
			if (!a[1]) {
				img2.setImageResource(R.drawable.time_pressed);
				a[1] = true;
			}else {
				img2.setImageResource(R.drawable.time_no_press);
				a[1] = false;
			}
			break;
		case R.id.img_3:
			if (!a[2]) {
				img3.setImageResource(R.drawable.time_pressed);
				a[2] = true;
			}else {
				img3.setImageResource(R.drawable.time_no_press);
				a[2] = false;
			}
			break;
		case R.id.img_4:
			if (!a[3]) {
				img4.setImageResource(R.drawable.time_pressed);
				a[3] = true;
			}else {
				img4.setImageResource(R.drawable.time_no_press);
				a[3] = false;
			}
			break;
		case R.id.img_5:
			if (!a[4]) {
				img5.setImageResource(R.drawable.time_pressed);
				a[4] = true;
			}else {
				img5.setImageResource(R.drawable.time_no_press);
				a[4] = false;
			}
			break;
		case R.id.img_6:
			if (!a[5]) {
				img6.setImageResource(R.drawable.time_pressed);
				a[5] = true;
			}else {
				img6.setImageResource(R.drawable.time_no_press);
				a[5] = false;
			}
			break;
		case R.id.img_7:
			if (!a[6]) {
				img7.setImageResource(R.drawable.time_pressed);
				a[6] = true;
			}else {
				img7.setImageResource(R.drawable.time_no_press);
				a[6] = false;
			}
			break;
		case R.id.img_8:
			if (!a[7]) {
				img8.setImageResource(R.drawable.time_pressed);
				a[7] = true;
			}else {
				img8.setImageResource(R.drawable.time_no_press);
				a[7] = false;
			}
			break;
		case R.id.img_9:
			if (!a[8]) {
				img9.setImageResource(R.drawable.time_pressed);
				a[8] = true;
			}else {
				img9.setImageResource(R.drawable.time_no_press);
				a[8] = false;
			}
			break;
		case R.id.img_10:
			if (!a[9]) {
				img10.setImageResource(R.drawable.time_pressed);
				a[9] = true;
			}else {
				img10.setImageResource(R.drawable.time_no_press);
				a[9] = false;
			}
			break;
		case R.id.img_11:
			if (!a[10]) {
				img11.setImageResource(R.drawable.time_pressed);
				a[10] = true;
			}else {
				img11.setImageResource(R.drawable.time_no_press);
				a[10] = false;
			}
			break;
		case R.id.img_12:
			if (!a[11]) {
				img12.setImageResource(R.drawable.time_pressed);
				a[11] = true;
			}else {
				img12.setImageResource(R.drawable.time_no_press);
				a[11] = false;
			}
			break;
		case R.id.img_13:
			if (!a[12]) {
				img13.setImageResource(R.drawable.time_pressed);
				a[12] = true;
			}else {
				img13.setImageResource(R.drawable.time_no_press);
				a[12] = false;
			}
			break;
		case R.id.img_14:
			if (!a[13]) {
				img14.setImageResource(R.drawable.time_pressed);
				a[13] = true;
			}else {
				img14.setImageResource(R.drawable.time_no_press);
				a[13] = false;
			}
			break;
		case R.id.img_15:
			if (!a[14]) {
				img15.setImageResource(R.drawable.time_pressed);
				a[14] = true;
			}else {
				img15.setImageResource(R.drawable.time_no_press);
				a[14] = false;
			}
			break;
		case R.id.img_16:
			if (!a[15]) {
				img16.setImageResource(R.drawable.time_pressed);
				a[15] = true;
			}else {
				img16.setImageResource(R.drawable.time_no_press);
				a[15] = false;
			}
			break;
		case R.id.img_17:
			if (!a[16]) {
				img17.setImageResource(R.drawable.time_pressed);
				a[16] = true;
			}else {
				img17.setImageResource(R.drawable.time_no_press);
				a[16] = false;
			}
			break;
		case R.id.img_18:
			if (!a[17]) {
				img18.setImageResource(R.drawable.time_pressed);
				a[17] = true;
			}else {
				img18.setImageResource(R.drawable.time_no_press);
				a[17] = false;
			}
			break;
		case R.id.img_19:
			if (!a[18]) {
				img19.setImageResource(R.drawable.time_pressed);
				a[18] = true;
			}else {
				img19.setImageResource(R.drawable.time_no_press);
				a[18] = false;
			}
			break;
		case R.id.img_20:
			if (!a[19]) {
				img20.setImageResource(R.drawable.time_pressed);
				a[19] = true;
			}else {
				img20.setImageResource(R.drawable.time_no_press);
				a[19] = false;
			}
			break;
		case R.id.img_21:
			if (!a[20]) {
				img21.setImageResource(R.drawable.time_pressed);
				a[20] = true;
			}else {
				img21.setImageResource(R.drawable.time_no_press);
				a[20] = false;
			}
			break;
			
		case R.id.choose_time_ensure:
			StringBuffer sb = new StringBuffer();
			String result = null;
			for (int i = 0; i < a.length; i++) {
				if (a[i]) {
					sb.append(i+1+",");
				}
			}
			if (sb != null && sb.length() > 0) {
				result = sb.substring(0, sb.length()-1);
			}
			Intent intent = new Intent();
			intent.putExtra("value", result);
			setResult(tag, intent);
			finish();
			break;
			
		}
	}
	public Map<String, String> queryCity(String url, String keyStr){
		JSONObject jsonObject = new JSONObject();
		JSONObject resultJson;
		Map<String, String> map = new HashMap<String, String>();
		try {
			jsonObject.put("id", keyStr);
			String result = HttpUtil.postRequst(url, jsonObject);
			resultJson = new JSONObject(result);
			JSONObject city = resultJson.getJSONObject("city"); 
			@SuppressWarnings("rawtypes")
			Iterator it = city.keys();
			while (it.hasNext()) {
				String key = String.valueOf(it.next());
				String value = city.getString(key);
				map.put(key, value);
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
}
