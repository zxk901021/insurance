package com.example.market.fragment;

import java.util.ArrayList;
import java.util.List;


import com.example.market.R;
import com.example.market.bean.Insurance;
import com.zhy_9.shopping_mall.adapter.CollectAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

	private String searchContent;
	private String searchResult;

	
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
		return layout;
	}


	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// 将layout从父组件中移除
		ViewGroup parent = (ViewGroup) layout.getParent();
		parent.removeView(layout);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_btn:
			searchList.setVisibility(View.VISIBLE);
			searchContent = searchEdt.getText().toString();
			if (!TextUtils.isEmpty(searchContent)) {
			}
			break;

		default:
			break;
		}
	}


	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (!TextUtils.isEmpty(searchResult)) {
				}
				break;

			default:
				break;
			}
		};
	};

}
