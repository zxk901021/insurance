package com.zhy_9.shopping_mall.adapter;

import java.util.List;

import com.example.market.R;
import com.example.market.bean.Insurance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.jpush.android.util.h;

public class CollectAdapter extends BaseAdapter{

	private Context context;
	private List<Insurance> data;
	private LayoutInflater inflater;
	
	
	public CollectAdapter(Context context, List<Insurance> data) {
		super();
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertview, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertview == null) {
			holder = new ViewHolder();
			convertview = inflater.inflate(R.layout.collect_layout, null);
			holder.tv = (TextView) convertview.findViewById(R.id.collect_tv);
			convertview.setTag(holder);
		}else {
			holder = (ViewHolder) convertview.getTag();
		}
		holder.tv.setText(data.get(arg0).getName());
		
		return convertview;
	}

	
	class ViewHolder{
		private TextView tv;
	}
}
