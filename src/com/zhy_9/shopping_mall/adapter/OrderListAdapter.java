package com.zhy_9.shopping_mall.adapter;

import java.util.List;

import com.example.market.R;
import com.example.market.bean.GoodsOrder;
import com.lib.uil.UILUtils;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderListAdapter extends BaseAdapter {

	private List<GoodsOrder> data;
	private Context context;
	private LayoutInflater inflate;
	
	
	
	
	public OrderListAdapter(List<GoodsOrder> data, Context context) {
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
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflate.inflate(R.layout.order_list_item, null);
			holder = new ViewHolder();
			holder.status = (TextView) convertView.findViewById(R.id.order_status);
			holder.goodsImg = (ImageView) convertView.findViewById(R.id.order_goods_img);
			holder.number = (TextView) convertView.findViewById(R.id.order_num);
			holder.price = (TextView) convertView.findViewById(R.id.order_price);
			holder.time = (TextView) convertView.findViewById(R.id.order_time);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.status.setText(data.get(position).getStatus());
		UILUtils.displayImage(context, data.get(position).getGoodsImg(), holder.goodsImg);
		holder.number.setText("订单编号：" + data.get(position).getNumber());
		holder.price.setText("订单金额：" + Html.fromHtml(data.get(position).getPrice()));
		holder.time.setText("下单时间：" + data.get(position).getTime());
		return convertView;
	}

	
	class ViewHolder{
		TextView status;
		ImageView goodsImg;
		TextView number;
		TextView price;
		TextView time;
	}
	
}
