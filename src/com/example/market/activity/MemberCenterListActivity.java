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
import com.example.market.bean.Consignee;
import com.example.market.bean.Goods;
import com.example.market.bean.GoodsOrder;
import com.example.market.bean.Insurance;
import com.example.market.db.InsuranceSQLiteDatabase;
import com.example.market.fragment.MineFragment;
import com.example.market.utils.Constants;
import com.google.gson.JsonArray;
import com.lib.volley.HTTPUtils;
import com.lib.volley.VolleyListener;
import com.zhy_9.shopping_mall.adapter.CollectAdapter;
import com.zhy_9.shopping_mall.adapter.ConsigneeAdapter;
import com.zhy_9.shopping_mall.adapter.GoodsGridViewAdapter;
import com.zhy_9.shopping_mall.adapter.OrderListAdapter;
import com.zhy_9.shopping_mall.widget.InternalGridView;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MemberCenterListActivity extends Activity implements
		OnClickListener {

	private ListView infoList;
	private String uid;
	private Button addDeliveryAddress;
	private ImageView back;
	private int mode;
	private RelativeLayout bottom;
	private TextView title;
	private String requestData;

	private InternalGridView goodsGrid;
	private GoodsGridViewAdapter gridAdapter;
	private List<Goods> gridData;

	private OrderListAdapter orderAdapter;
	private List<GoodsOrder> orderData;

	private ConsigneeAdapter consigneeAdapter;
	private List<Consignee> consigneeData;
	private ListView collectList;
	private InsuranceSQLiteDatabase db;
	private CollectAdapter collectAdapter;
	private List<Insurance> collectData;

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
		getData();
	}

	private void initView() {
		infoList = (ListView) findViewById(R.id.member_info_listview);
		addDeliveryAddress = (Button) findViewById(R.id.add_new_address);
		back = (ImageView) findViewById(R.id.member_info_list_back);
		bottom = (RelativeLayout) findViewById(R.id.member_list_bottom);
		title = (TextView) findViewById(R.id.member_info_list_title);
		goodsGrid = (InternalGridView) findViewById(R.id.member_info_gridview);
		collectList = (ListView) findViewById(R.id.collect_list);
		if (mode == 2) {
			bottom.setVisibility(View.VISIBLE);
		}
		if (mode == 4) {
			infoList.setVisibility(View.GONE);
			goodsGrid.setVisibility(View.VISIBLE);
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
		addDeliveryAddress.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	private String getRequestUrl(int mode) {
		String url = null;
		switch (mode) {
		case MineFragment.MY_ORDER:
			url = Constants.URL.MY_ORDER_LIST;
			return url;

		case MineFragment.DELIVERY_ADDRESS:
			url = Constants.URL.DELIVERY_ADDRESS;
			return url;

		case MineFragment.MY_EXCHANGE:
			url = Constants.URL.MY_EXCHANGE_GOODS;
			return url;

		case MineFragment.MY_COLLOCET:
			url = Constants.URL.COLLECTION_LIST;
			return url;

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

	private void getData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("user_id", uid);
		HTTPUtils.postVolley(MemberCenterListActivity.this,
				getRequestUrl(mode), map, new VolleyListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

					}

					@Override
					public void onResponse(String result) {
						Log.e("member", result);
						requestData = result;
						handler.sendEmptyMessage(mode);

					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_new_address:
			Intent intent = new Intent(MemberCenterListActivity.this,
					DeliveryAdressEditActivity.class);
			startActivity(intent);
			break;

		case R.id.member_info_list_back:
			finish();
			break;

		default:
			break;
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MineFragment.MY_ORDER:
				orderData = new ArrayList<GoodsOrder>();
				JSONArray order;
				try {
					order = new JSONArray(requestData);
					int len = order.length();
					Log.e("len", len + "");
					for (int i = 0; i < len; i++) {
						JSONObject ob = order.getJSONObject(i);
						GoodsOrder goodsOrder = new GoodsOrder();
						goodsOrder.setStatus(ob.getString("order_status"));
						// goodsOrder.setGoodsImg(ob.getString(""));
						goodsOrder.setNumber(ob.getString("order_sn"));
						goodsOrder.setPrice(ob.getString("total_fee"));
						goodsOrder.setTime(ob.getString("order_time"));
						goodsOrder.setGoodsId(ob.getString("order_id"));
						orderData.add(goodsOrder);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				orderAdapter = new OrderListAdapter(orderData,
						MemberCenterListActivity.this);
				infoList.setAdapter(orderAdapter);
				break;

			case MineFragment.DELIVERY_ADDRESS:
				consigneeData = new ArrayList<Consignee>();
				try {
					JSONObject ob = new JSONObject(requestData);
					JSONArray ar = ob.getJSONArray("consignee_list");
					int len = ar.length();
					for (int i = 0; i < len; i++) {
						JSONObject object = ar.getJSONObject(i);
						Consignee con = new Consignee();
						con.setDetailAdress(object.getString("address"));
						con.setEmail(object.getString("email"));
						con.setName(object.getString("consignee"));
						con.setPhone(object.getString("tel"));
						consigneeData.add(con);
					}
					consigneeAdapter = new ConsigneeAdapter(consigneeData,
							MemberCenterListActivity.this);
					infoList.setAdapter(consigneeAdapter);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case MineFragment.MY_COLLOCET:
				gridData = new ArrayList<Goods>();
				JSONArray ar;
				try {
					ar = new JSONArray(requestData);
					int len = ar.length();
					for (int i = 0; i < len; i++) {
						JSONObject ob = ar.getJSONObject(i);
						Goods goods = new Goods();
						goods.setName(ob.getString("goods_name"));
						goods.setShopPrice(ob.getString("shop_price"));
						goods.setThumb("");
						goods.setId(ob.getString("goods_id"));
						goods.setUrl(ob.getString("url"));
						gridData.add(goods);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				gridAdapter = new GoodsGridViewAdapter(gridData,
						MemberCenterListActivity.this);
				goodsGrid.setAdapter(gridAdapter);
				break;

			default:
				break;
			}
		};
	};
}
