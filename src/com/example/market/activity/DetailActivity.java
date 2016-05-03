package com.example.market.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.example.market.R;
import com.example.market.bean.GoodsDetail;
import com.example.market.utils.Constants;
import com.lib.uil.UILUtils;
import com.lib.volley.HTTPUtils;
import com.lib.volley.VolleyListener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends FragmentActivity implements OnClickListener {

	public static final int GOODS_DETAIL_MSG = 1;

	private GoodsDetail goodsDetail;
	private String goodsId;
	private String goodsDetailUrl;
	private ImageView goodsShowImg;
	private WebView goodsDetailWeb;
	private ProgressBar loading;
	private TextView goodsName;
	private ImageView back;
	private TextView collect;

	private Button addCart;

	private String goodsDetailResult;
	private String uid;

	private String colResult;
	private String addCardResult;

	private Button decrease, increase, amount;
	private int goodsCount = 1;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_detail);
		getIntentData();
		initView();
		widgetClick();
		initData();
	}

	private void getIntentData() {
		Intent intent = getIntent();
		goodsId = intent.getStringExtra("id");
		goodsDetailUrl = intent.getStringExtra("url");
		SharedPreferences sp = getSharedPreferences("MyPrefer",
				Context.MODE_PRIVATE);
		uid = sp.getString("uid", "");
		getDetailInfo();
		Log.e("intentData", goodsId + goodsDetailUrl);
	}

	private void getDetailInfo() {
		HTTPUtils.getVolley(DetailActivity.this, Constants.URL.SITE_URL
				+ "goods.php?id=" + goodsId, new VolleyListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

			}

			@Override
			public void onResponse(String result) {
				if (result != null) {
					goodsDetailResult = result;
					handler.sendEmptyMessage(GOODS_DETAIL_MSG);
				}
			}
		});
	}

	private void initView() {
		goodsShowImg = (ImageView) findViewById(R.id.detail_img);
		goodsDetailWeb = (WebView) findViewById(R.id.detail_goods_details);
		initWeb();
		loading = (ProgressBar) findViewById(R.id.progressBar1);
		goodsName = (TextView) findViewById(R.id.detail_goods_name);
		loading.setVisibility(View.GONE);
		back = (ImageView) findViewById(R.id.img_back);
		addCart = (Button) findViewById(R.id.detail_add_cart);
		collect = (TextView) findViewById(R.id.collect_goods);
		decrease = (Button) findViewById(R.id.detail_reduce_btn);
		increase = (Button) findViewById(R.id.detail_add_btn);
		amount = (Button) findViewById(R.id.detail_goods_count);
	}

	private void widgetClick() {
		back.setOnClickListener(this);
		addCart.setOnClickListener(this);
		collect.setOnClickListener(this);
		decrease.setOnClickListener(this);
		increase.setOnClickListener(this);
		amount.setOnClickListener(this);
	}

	private void initWeb() {
		goodsDetailWeb.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});
	}

	private void initData() {
		UILUtils.displayImage(DetailActivity.this, Constants.URL.IMAGE_SITE
				+ goodsDetailUrl, goodsShowImg);
		goodsDetailWeb.loadUrl(Constants.URL.SITE_URL + goodsDetailUrl);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GOODS_DETAIL_MSG:
				goodsDetail = new GoodsDetail();
				try {
					JSONObject ob = new JSONObject(goodsDetailResult);
					String nameStr = ob.getString("goods_name");
					String imgUrl = ob.getString("goods_img");
					goodsName.setText(nameStr);
					goodsDetail.setGoodsName(nameStr);
					goodsDetail.setGoodsThumb(imgUrl);
					UILUtils.displayImage(DetailActivity.this,
							Constants.URL.IMAGE_SITE + imgUrl, goodsShowImg);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				break;

			case 2:
				try {
					JSONObject object = new JSONObject(colResult);
					String flag = object.getString("error");
					String mess = object.getString("message");
					Toast.makeText(DetailActivity.this, mess,
							Toast.LENGTH_SHORT).show();
					if (flag.equals("0")) {
						collect.setText("“— ’≤ÿ");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				Toast.makeText(DetailActivity.this, colResult,
						Toast.LENGTH_SHORT).show();
				break;

			case 3:
				try {
					JSONObject ob = new JSONObject(addCardResult);
					String content = ob.getString("content");
					Toast.makeText(DetailActivity.this,
							Html.fromHtml(content).toString(),
							Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;

		case R.id.detail_add_cart:
			Map<String, String> map = new HashMap<String, String>();
			JSONObject object = new JSONObject();
			try {
				object.put("quick", "1");
				object.put("spec", "[]");
				object.put("goods_id", goodsId);
				object.put("number", goodsCount);
				object.put("parent", "0");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			map.put("goods", object.toString());
			HTTPUtils.postVolley(DetailActivity.this, Constants.URL.ADD_CART
					+ uid, map, new VolleyListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {

				}

				@Override
				public void onResponse(String result) {
					Log.e("cart", result);
					addCardResult = result;
					handler.sendEmptyMessage(3);
				}
			});
			break;

		case R.id.collect_goods:
			Map<String, String> col = new HashMap<String, String>();
			col.put("id", goodsId);
			col.put("user_id", uid);
			HTTPUtils.postVolley(DetailActivity.this, Constants.URL.COLLECT,
					col, new VolleyListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {

						}

						@Override
						public void onResponse(String result) {
							colResult = result;
							Log.e("col", colResult);
							handler.sendEmptyMessage(2);
						}
					});
			break;

		case R.id.detail_goods_count:

			break;

		case R.id.detail_reduce_btn:
			getGoodsCounts();
			if (goodsCount > 1) {
				goodsCount -= 1;
			}
			setGoodsCounts();
			break;

		case R.id.detail_add_btn:
			getGoodsCounts();
			goodsCount += 1;
			setGoodsCounts();
			break;

		default:
			break;
		}
	}

	private void getGoodsCounts() {
		String number = amount.getText().toString();
		int num = Integer.parseInt(number);
		goodsCount = num;
	}

	private void setGoodsCounts() {
		String number = Integer.toString(goodsCount);
		amount.setText(number);
	}
}
