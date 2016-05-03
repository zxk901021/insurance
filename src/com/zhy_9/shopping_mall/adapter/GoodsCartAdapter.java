package com.zhy_9.shopping_mall.adapter;

import java.util.List;

import com.example.market.R;
import com.example.market.bean.GoodsCart;
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

public class GoodsCartAdapter extends BaseAdapter {

	private List<GoodsCart> data;
	Context context;
	LayoutInflater inflater;

	public GoodsCartAdapter(List<GoodsCart> data, Context context) {
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
			convertView = inflater.inflate(R.layout.goods_cart_item, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView
					.findViewById(R.id.goods_cart_img);
			holder.name = (TextView) convertView
					.findViewById(R.id.goods_cart_name);
			holder.number = (TextView) convertView
					.findViewById(R.id.goods_cart_number);
			holder.price = (TextView) convertView
					.findViewById(R.id.goods_cart_price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		UILUtils.displayImage(context, Constants.URL.IMAGE_SITE + data.get(position).getImgUrl(),
				holder.img);
		holder.name.setText(data.get(position).getName());
		holder.number.setText("购买数量：" + data.get(position).getNumber());
		holder.price.setText("商品总价：" + Html.fromHtml(data.get(position).getPrice()));
		return convertView;
	}

	class ViewHolder {
		ImageView img;
		TextView name;
		TextView number;
		TextView price;
	}

}
