package com.example.market.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.example.market.R;
import com.example.market.bean.Insurance;
import com.example.market.db.InsuranceSQLiteDatabase;
import com.example.market.fragment.MineFragment;
import com.example.market.utils.Constants;
import com.google.gson.JsonArray;
import com.lib.volley.HTTPUtils;
import com.lib.volley.VolleyListener;
import com.zhy_9.shopping_mall.adapter.CollectAdapter;

import A.t;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MemberCenterListActivity extends Activity implements
		OnClickListener {

	private ListView infoList;
	private String uid;
	private ImageView back;
	private int mode;
	private RelativeLayout bottom;
	private TextView title;
	private String requestData;

	private ListView collectList;
	private InsuranceSQLiteDatabase db;
	private CollectAdapter collectAdapter;
	private List<Insurance> collectData;
	private Button submit;
	private EditText edt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_center_list);
		SharedPreferences sp = getSharedPreferences("MyPrefer",
				Context.MODE_PRIVATE);
		uid = sp.getString("uid", "");
		Intent intent = getIntent();
		mode = intent.getIntExtra("mode", 0);
		initView();
		setTitle();
		widgetClick();
	}

	private void initView() {
		infoList = (ListView) findViewById(R.id.member_info_listview);
		back = (ImageView) findViewById(R.id.member_info_list_back);
		bottom = (RelativeLayout) findViewById(R.id.member_list_bottom);
		title = (TextView) findViewById(R.id.member_info_list_title);
		collectList = (ListView) findViewById(R.id.collect_list);
		edt = (EditText) findViewById(R.id.mes_edt);
		submit = (Button) findViewById(R.id.mes_btn);
		
		if (mode == 2) {
			bottom.setVisibility(View.VISIBLE);
		}
		if (mode == 4) {
			infoList.setVisibility(View.GONE);
			collectList.setVisibility(View.VISIBLE);
			db = new InsuranceSQLiteDatabase(this);
			collectData = new ArrayList<Insurance>();
			collectData = db.query();
			collectAdapter = new CollectAdapter(this, collectData);
			collectList.setAdapter(collectAdapter);
			collectList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(MemberCenterListActivity.this, InsuranceDetailActivity.class);
					intent.putExtra("name", collectData.get(position).getName());
					startActivity(intent);
				}
			});
			
		}
		if (mode == 6) {
			edt.setVisibility(View.VISIBLE);
			submit.setVisibility(View.VISIBLE);
			submit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(MemberCenterListActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		collectData = db.query();
		collectAdapter.notifyDataSetChanged();
		collectAdapter = new CollectAdapter(this, collectData);
		collectList.setAdapter(collectAdapter);
		
	}
	
	
	private void setTitle() {
		String titleStr = null;
		switch (mode) {
		case 1:
			titleStr = "我的订单";
			break;
		case 2:
			titleStr = "收货地址";
			break;
		case 3:
			titleStr = "我的退换货";
			break;
		case 4:
			titleStr = "我的收藏";
			break;
		case 5:
			titleStr = "我的购物车";
			break;
		case 6:
			titleStr = "我的留言";
			break;
		case 7:
			titleStr = "我的评论";
			break;

		default:
			break;

		}
		title.setText(titleStr);
	}

	private void widgetClick() {
		back.setOnClickListener(this);
	}

	private String getRequestUrl(int mode) {
		String url = null;
		switch (mode) {

		case MineFragment.MY_CART:

			return url;

		case MineFragment.MY_MESSAGE:

			return url;

		case MineFragment.MY_COMMENT:

			return url;

		default:
			return url;
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.member_info_list_back:
			finish();
			break;

		default:
			break;
		}
	}

}
