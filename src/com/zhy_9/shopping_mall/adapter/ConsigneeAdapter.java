package com.zhy_9.shopping_mall.adapter;

import java.util.List;

import com.example.market.R;
import com.example.market.bean.Consignee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ConsigneeAdapter extends BaseAdapter{

	private List<Consignee> data;
	Context context;
	LayoutInflater inflater;
	
	
	
	
	public ConsigneeAdapter(List<Consignee> data, Context context) {
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
			convertView = inflater.inflate(R.layout.consignee_list_item, null);
			holder = new ViewHolder();
			holder.address = (TextView) convertView.findViewById(R.id.consignee_address);
			holder.email = (TextView) convertView.findViewById(R.id.consignee_email);
			holder.name = (TextView) convertView.findViewById(R.id.consignee_name);
			holder.phone = (TextView) convertView.findViewById(R.id.consignee_phone);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.address.setText("详细地址：" + data.get(position).getDetailAdress());
		holder.email.setText("电子邮件地址：" + data.get(position).getEmail());
		holder.name.setText("收货人姓名：" + data.get(position).getName());
		holder.phone.setText("电话：" + data.get(position).getPhone());
		return convertView;
	}

	class ViewHolder {
		TextView name;
		TextView phone;
		TextView address;
		TextView email;
	}
	
}
