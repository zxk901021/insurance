package com.example.market.activity;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.VolleyError;
import com.example.market.R;
import com.example.market.utils.Constants;
import com.lib.volley.HTTPUtils;
import com.lib.volley.VolleyListener;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class DeliveryAdressEditActivity extends Activity implements
		OnClickListener {

	private Button saveAddress;
	private ImageView back;
	private EditText nameEdt, phoneEdt, emailEdt, detailAddressEdt, zipCodeEdt;
	private String name, phone, email, detailAddress, zipCode;
	private String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delivery_address_edit);
		SharedPreferences sp = getSharedPreferences("MyPrefer",
				Context.MODE_PRIVATE);
		uid = sp.getString("uid", "");
		initView();
		widgetClick();
	}

	private void initView() {
		saveAddress = (Button) findViewById(R.id.save_delivert_address);
		back = (ImageView) findViewById(R.id.delivery_address_back);
		saveAddress.setBackgroundResource(R.drawable.grey_corner_btn);
		nameEdt = (EditText) findViewById(R.id.address_name);
		phoneEdt = (EditText) findViewById(R.id.address_phone);
		emailEdt = (EditText) findViewById(R.id.address_email);
		detailAddressEdt = (EditText) findViewById(R.id.address_detail);
		zipCodeEdt = (EditText) findViewById(R.id.address_code);
		// saveAddress.setBackgroundColor(getResources().getColor(R.color.grgray));
	}

	private void widgetClick() {
		saveAddress.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	private void getRequstData() {
		name = nameEdt.getText().toString();
		phone = phoneEdt.getText().toString();
		email = emailEdt.getText().toString();
		detailAddress = detailAddressEdt.getText().toString();
		zipCode = zipCodeEdt.getText().toString();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.save_delivert_address:
			getRequstData();
			saveAddress.setBackgroundResource(R.drawable.corner_btn);
			Map<String, String> map = new HashMap<String, String>();
			map.put("user_id", uid);
			map.put("address_id", "0");
			map.put("consignee", name);
			map.put("email", email);
			map.put("country", "");
			map.put("province", "天津");
			map.put("city", "天津");
			map.put("district", "西青区");
			map.put("address", detailAddress);
			map.put("zipcode", zipCode);
			map.put("tel", phone);
			HTTPUtils.postVolley(DeliveryAdressEditActivity.this,
					Constants.URL.ADD_DELIVERY_ADDRESS, map,
					new VolleyListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {

						}

						@Override
						public void onResponse(String result) {
							Log.e("address", result);
						}
					});
			break;

		case R.id.delivery_address_back:
			finish();
			break;

		default:
			break;
		}

	}
}
