package com.zhy_9.shopping_mall.adapter;

import java.util.List;

import com.example.market.R;
import com.example.market.bean.CatId;
import com.example.market.bean.CategoryModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryGridAdapter extends BaseAdapter {

	private List<CatId> data;
	private Context context;
	private LayoutInflater inflater;
	
	
	
	public CategoryGridAdapter(List<CatId> data, Context context) {
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
		CategoryGridHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.category_grid_layout, null);
			holder = new CategoryGridHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.category_grid_img);
			holder.tv = (TextView) convertView.findViewById(R.id.category_grid_tv);
			convertView.setTag(holder);
		}else {
			holder = (CategoryGridHolder) convertView.getTag();
		}
		holder.tv.setText(data.get(position).getName());
		return convertView;
	}

	class CategoryGridHolder{
		private ImageView img;
		private TextView tv;
	}
	
}
