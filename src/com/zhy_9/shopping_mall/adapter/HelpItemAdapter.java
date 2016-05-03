package com.zhy_9.shopping_mall.adapter;

import java.util.List;

import com.example.market.R;
import com.example.market.bean.HelpCenterItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HelpItemAdapter extends BaseAdapter {

	private List<HelpCenterItem> data;
	private LayoutInflater inflater;
	private Context context;
	
	
	
	public HelpItemAdapter(List<HelpCenterItem> data, Context context) {
		super();
		this.data = data;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HelpItemHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.help_center_item_layout, null);
			holder = new HelpItemHolder();
			holder.title = (TextView) convertView.findViewById(R.id.help_item_title);
			holder.time = (TextView) convertView.findViewById(R.id.help_item_time);
			convertView.setTag(holder);
		}else {
			holder = (HelpItemHolder) convertView.getTag();
		}
		holder.title.setText(data.get(position).getCatName());
		holder.time.setText(data.get(position).getTime());
		return convertView;
	}

	class HelpItemHolder{
		private TextView title;
		private TextView time;
	}
	
}
