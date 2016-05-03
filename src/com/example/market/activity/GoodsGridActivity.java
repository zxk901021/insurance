package com.example.market.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.example.market.R;
import com.example.market.bean.Goods;
import com.example.market.utils.Constants;
import com.lib.volley.HTTPUtils;
import com.lib.volley.VolleyListener;
import com.zhy_9.shopping_mall.adapter.GoodsGridViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class GoodsGridActivity extends Activity {

	private GridView goodsGrid;

	private String id, categoryListResult;

	private List<Goods> data;

	private GoodsGridViewAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_grid);
		initView();
		getIntentData();
		getDataFromWeb();
		gridItemClick();
	}

	private void initView() {
		goodsGrid = (GridView) findViewById(R.id.category_goods_grid);

	}

	private void getIntentData() {
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
	}

	private void getDataFromWeb() {
		HTTPUtils.getVolley(GoodsGridActivity.this, Constants.URL.SITE_URL
				+ Constants.URL.CATEGORY_LIST + id, new VolleyListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

			}

			@Override
			public void onResponse(String result) {
				categoryListResult = result;
				handler.sendEmptyMessage(1);
			}
		});
	}

	private void gridItemClick() {
		goodsGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(GoodsGridActivity.this,
						DetailActivity.class);
				intent.putExtra("id", data.get(position).getId());
				intent.putExtra("url", data.get(position).getConUrl());
				startActivity(intent);
			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				data = new ArrayList<Goods>();
				try {
					JSONArray array = new JSONArray(categoryListResult);
					int len = array.length();
					for (int i = 0; i < len - 1; i++) {
						if (array.get(i) == null) {
							break;
						}
						JSONObject json = array.getJSONObject(i);
						String object = json.toString();
						if (!TextUtils.isEmpty(object)) {
							Goods goods = new Goods();
							goods.setName(json.getString("goods_name"));
							goods.setMarketPrice(json.getString("market_price"));
							goods.setShopPrice(json.getString("shop_price"));
							goods.setGoodsImg(json.getString("goods_img"));
							goods.setThumb(json.getString("goods_thumb"));
							goods.setId(json.getString("goods_id"));
							goods.setConUrl(json.getString("url"));
							data.add(goods);
						}

					}
					adapter = new GoodsGridViewAdapter(data,
							GoodsGridActivity.this);
					goodsGrid.setAdapter(adapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		};
	};

}
