package com.example.market.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.baidu.location.ad;
import com.example.market.R;
import com.example.market.activity.BoxActivity;
import com.example.market.activity.DetailActivity;
import com.example.market.activity.MainActivity;
import com.example.market.activity.ShakeActivity;
import com.example.market.activity.WebActivity;
import com.example.market.bean.Goods;
import com.example.market.bean.Insurance;
import com.example.market.utils.Constants;
import com.lib.volley.HTTPUtils;
import com.lib.volley.VolleyListener;
import com.zhy_9.shopping_mall.adapter.CollectAdapter;
import com.zhy_9.shopping_mall.adapter.GoodsGridViewAdapter;
import com.zhy_9.shopping_mall.widget.InternalGridView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class FindFragment extends Fragment implements OnClickListener {

	private View layout;
	private EditText searchEdt;
	private ImageView searchBtn;
	private InternalGridView searchResultGrid;

	private String searchContent;
	private String searchResult;
	private List<Goods> data;

	private GoodsGridViewAdapter adapter;
	
	private ListView searchList;
	private CollectAdapter colAdapter;
	private List<Insurance> list;
	private String[] names = new String[]{"幼儿生病保险","子女上学基金","老年意外险","老年重大疾病险","旅游意外险","旅游意外身亡险","意外失业险"};
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (layout != null) {
			// 防止多次new出片段对象，造成图片错乱问题
			return layout;
		}
		layout = inflater.inflate(R.layout.fragment_find, container, false);
		searchEdt = (EditText) layout.findViewById(R.id.search_edt);
		searchBtn = (ImageView) layout.findViewById(R.id.search_btn);
		searchResultGrid = (InternalGridView) layout
				.findViewById(R.id.search_result_list);
		searchList = (ListView) layout.findViewById(R.id.search_list);
		list = new ArrayList<Insurance>();
		for (int i = 0; i < names.length; i++) {
			Insurance ins = new Insurance();
			ins.setName(names[i]);
			list.add(ins);
		}
		colAdapter = new CollectAdapter(getActivity(), list);
		searchList.setAdapter(colAdapter);
		searchBtn.setOnClickListener(this);
		searchResultGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				gotoDetail(data.get(position).getId(), data.get(position)
						.getConUrl());
			}
		});
		return layout;
	}

	private void gotoDetail(String id, String url) {
		Intent intent = new Intent(getActivity(), DetailActivity.class);
		intent.putExtra("id", id);
		intent.putExtra("url", url);
		startActivity(intent);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// 将layout从父组件中移除
		ViewGroup parent = (ViewGroup) layout.getParent();
		parent.removeView(layout);
	}

	private void startToSearch() {
		Map<String, String> param = new HashMap<String, String>();
		param.put("keywords", searchContent);
		HTTPUtils.postVolley(getActivity(), Constants.URL.SITE_URL
				+ Constants.URL.SEARCH_GOODS, param, new VolleyListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

			}

			@Override
			public void onResponse(String result) {
				searchResult = result;
				handler.sendEmptyMessage(1);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_btn:
			searchList.setVisibility(View.VISIBLE);
			searchContent = searchEdt.getText().toString();
			if (!TextUtils.isEmpty(searchContent)) {
				startToSearch();
			}
			break;

		default:
			break;
		}
	}

	private void parseJson() {
		JSONArray array;
		try {
			array = new JSONArray(searchResult);
			data = new ArrayList<Goods>();
			int len = array.length();
			for (int i = 0; i < len; i++) {
				JSONObject ob = array.getJSONObject(i);
				Goods search = new Goods();
				search.setName(ob.getString("goods_name"));
				search.setShopPrice(ob.getString("shop_price"));
				search.setThumb(ob.getString("goods_thumb"));
				search.setConUrl(ob.getString("url"));
				search.setId(ob.getString("goods_id"));
				data.add(search);
			}
			adapter = new GoodsGridViewAdapter(data, getActivity());
			searchResultGrid.setAdapter(adapter);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (!TextUtils.isEmpty(searchResult)) {
					parseJson();
				}
				break;

			default:
				break;
			}
		};
	};

}
