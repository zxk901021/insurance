package com.zhy_9.shopping_mall.adapter;

import java.util.List;

import com.example.market.R;
import com.example.market.bean.MemberListModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MemberInfoAdapter extends BaseAdapter {
	
	private List<MemberListModel> data;
	private Context context;
	private LayoutInflater inflater;

	
	
	public MemberInfoAdapter(List<MemberListModel> data, Context context) {
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
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_member_list, null);
			holder = new ViewHolder();
			holder.firstTv = (TextView) convertView.findViewById(R.id.first_tv);
			holder.secondTv = (TextView) convertView.findViewById(R.id.second_tv);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.firstTv.setText(data.get(position).getFirstTv());
		holder.secondTv.setText(data.get(position).getSecondTv());
		holder.thirdTv.setText(data.get(position).getThirdTv());
		return convertView;
	}

	class ViewHolder{
		private TextView firstTv;
		private TextView secondTv;
		private TextView thirdTv;
	}
	
}
