package com.zhy_9.shopping_mall.adapter;

import java.util.List;

import com.example.market.R;
import com.example.market.activity.DetailActivity;
import com.example.market.activity.GoodsGridActivity;
import com.example.market.bean.CategoryModel;
import com.zhy_9.shopping_mall.widget.InternalGridView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CategoryListAdapter extends BaseAdapter {

	private List<CategoryModel> data;
	private LayoutInflater inflater;
	private Context context;

	public CategoryListAdapter(List<CategoryModel> data, Context context) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		CategoryHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.category_list_item, null);
			holder = new CategoryHolder();
			holder.categoryTitle = (TextView) convertView
					.findViewById(R.id.category_list_item_title);
			holder.categoryGrid = (InternalGridView) convertView
					.findViewById(R.id.category_list_item_grid);
			convertView.setTag(holder);
		} else {
			holder = (CategoryHolder) convertView.getTag();
		}
		holder.categoryTitle.setText(data.get(position).getName());
		if (data.get(position).getList() == null) {

		} else {
			holder.categoryGrid.setAdapter(new CategoryGridAdapter(data.get(
					position).getList(), context));
			holder.categoryGrid.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int pos, long id) {
					Intent intent = new Intent(context, GoodsGridActivity.class);
					intent.putExtra("id", data.get(position).getList().get(pos).getId());
					context.startActivity(intent);
				}
			});
		}
		return convertView;
	}

	class CategoryHolder {
		private TextView categoryTitle;
		private InternalGridView categoryGrid;
	}

}
