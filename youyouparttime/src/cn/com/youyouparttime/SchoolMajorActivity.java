package cn.com.youyouparttime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.CommonUtil;
import cn.com.youyouparttime.util.HttpUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SchoolMajorActivity extends Activity {

	private ImageView back;
	private TextView text,title;
	private EditText edit;
	private String searchStr;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private String[] data;
	private Map<String, String> map;
	private List<String> list;
	private LinearLayout layout;
	int flag;
	private String  name;
	private TextView listText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_school_major);
		SysApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		flag = intent.getIntExtra("flag", 0);
		
		
		initView();
		
	}

	public void initView(){
		title = (TextView) findViewById(R.id.school_major_hint_text);
		back = (ImageView) findViewById(R.id.school_major_back);
		text = (TextView) findViewById(R.id.school_major_hint_text2);
		edit = (EditText) findViewById(R.id.school_major_edit);
		listView = (ListView) findViewById(R.id.school_major_list);
		layout = (LinearLayout) findViewById(R.id.school_major_list_layout);
		listText = (TextView) findViewById(R.id.school_major_list_text);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		if (flag == 20) {
			title.setText("请输入专业匹配");
			edit.setHint("请输入专业");
		}else {
			title.setText("请输入学校匹配");
			edit.setHint("请输入学校");
		}
		
		text.setText(Html.fromHtml("注：仅匹配前20条，请尽量详细输入。 <br> 支持2位以上的中文匹配。"));
		edit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (!TextUtils.isEmpty(edit.getText())) {
					searchStr = edit.getText().toString();
					if (flag == 20) {
						map = CommonUtil.getData(9, "result", searchStr);
					}else {
						map = CommonUtil.getData(10, "result", searchStr);
					}
					list = new ArrayList<String>();
					list = new ArrayList<String>(map.values());
					if (searchStr.length() >= 2) {
						
						layout.setVisibility(View.VISIBLE);
					}else {
						layout.setVisibility(View.GONE);
					}
				}
				if (list != null && list.size() > 0) {
					listView.setVisibility(View.VISIBLE);
					listText.setVisibility(View.GONE);
					data = (String[]) list.toArray(new String[list.size()]);
					Log.e("data.length", data.length + "");
					adapter = new ArrayAdapter<String>(SchoolMajorActivity.this, R.layout.array_list_item, R.id.array_item_text, data);
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
//							edit.setText(data[position]);
							Intent intent = new Intent();
							intent.putExtra("value", data[position]);
							setResult(flag, intent);
							finish();
						}
					});
				}else {
					listView.setVisibility(View.GONE);
					listText.setVisibility(View.VISIBLE);
				}
			}
		});
		
		
	}
	
	
	
}
