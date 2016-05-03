package com.zhy_9.shopping_mall.adapter;

import java.util.List;

import com.example.market.R;
import com.example.market.bean.Goods;
import com.example.market.utils.Constants;
import com.lib.uil.UILUtils;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GoodsGridViewAdapter extends BaseAdapter {

	private List<Goods> data;
	private LayoutInflater inflate;
	private Context context;

	public GoodsGridViewAdapter(List<Goods> data, Context context) {
		super();
		this.data = data;
		this.context = context;
		inflate = LayoutInflater.from(context);
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
		GoodsHolder holder = null;
		if (convertView == null) {
			convertView = inflate
					.inflate(R.layout.goods_grid_view_layout, null);
			holder = new GoodsHolder();
			holder.goodsImg = (ImageView) convertView
					.findViewById(R.id.goods_grid_img);
			holder.goodsName = (TextView) convertView
					.findViewById(R.id.goods_grid_name);
			holder.goodsPrice = (TextView) convertView
					.findViewById(R.id.goods_grid_price);
			convertView.setTag(holder);
		} else {
			holder = (GoodsHolder) convertView.getTag();
		}
		holder.goodsName.setText(data.get(position).getName());
		holder.goodsPrice.setText(Html.fromHtml(data.get(position).getShopPrice()).toString());
		UILUtils.displayImage(
				context,
				Constants.URL.IMAGE_SITE + data.get(position).getThumb(),
				holder.goodsImg);

		return convertView;
	}

	class GoodsHolder {
		private ImageView goodsImg;
		private TextView goodsName;
		private TextView goodsPrice;
	}

}
