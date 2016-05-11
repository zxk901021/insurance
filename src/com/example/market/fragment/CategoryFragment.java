package com.example.market.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.example.market.R;
import com.example.market.activity.InsuranceDetailActivity;
import com.example.market.utils.Constants;
import com.lib.volley.HTTPUtils;
import com.lib.volley.VolleyListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

public class CategoryFragment extends Fragment implements OnClickListener{

	private ListView categoryList;

	private String categoryResult;



	private View layout;
	
	private ImageView img1, img2, img3;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		if (layout != null) {
			return layout;
		}
		layout = inflater.inflate(R.layout.fragment_category, container, false);
		return layout;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		Log.e("onActivityCreated", "onActivityCreated");
	}

	private void initView() {
		img1 = (ImageView) getActivity().findViewById(R.id.layout1_img);
		img2 = (ImageView) getActivity().findViewById(R.id.zhuanti_img1);
		img3 = (ImageView) getActivity().findViewById(R.id.zhuanti_img2);
		img1.setOnClickListener(this);
		img2.setOnClickListener(this);
		img3.setOnClickListener(this);
		categoryList = (ListView) getActivity()
				.findViewById(R.id.category_list);

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// 将layout从父组件中移除
		ViewGroup parent = (ViewGroup) layout.getParent();
		parent.removeView(layout);
	}

	
	private void gotoWebDetail(int flag){
		Intent intent = new Intent(getActivity(), InsuranceDetailActivity.class);
		intent.putExtra("flag", flag);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout1_img:
			gotoWebDetail(2);
			break;
			
		case R.id.zhuanti_img1:
			gotoWebDetail(2);
			break;
			
		case R.id.zhuanti_img2:
			gotoWebDetail(2);
			break;

		default:
			break;
		}
			
	}
	
	
	
}
