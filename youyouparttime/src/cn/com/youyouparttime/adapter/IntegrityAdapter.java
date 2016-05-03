package cn.com.youyouparttime.adapter;

import java.util.List;

import cn.com.youyouparttime.R;
import cn.com.youyouparttime.entity.IntergrityInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class IntegrityAdapter extends BaseAdapter {
	
	List<IntergrityInfo> list;
	LayoutInflater inflater;
	Context context;
	
	
	
	public IntegrityAdapter(List<IntergrityInfo> list, Context context) {
		super();
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.integrity_list_item, null);
			holder.intergrityTitle = (TextView) convertView.findViewById(R.id.integrity_item_title);
			holder.intergrityGood = (TextView) convertView.findViewById(R.id.integrity_item_good_count);
			holder.intergrityNormal = (TextView) convertView.findViewById(R.id.integrity_item_normal_count);
			holder.intergrityBad = (TextView) convertView.findViewById(R.id.integrity_item_bad_count);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.intergrityTitle.setText(list.get(position).getIntergrityName());
		holder.intergrityGood.setText("("+list.get(position).getIntergrityGood()+")");
		holder.intergrityNormal.setText("("+list.get(position).getIntergrityNormal()+")");
		holder.intergrityBad.setText("("+list.get(position).getIntergrityBad()+")");
		return convertView;
	}

	
	
	class ViewHolder {
		private TextView intergrityTitle;
		private TextView intergrityGood;
		private TextView intergrityNormal;
		private TextView intergrityBad;
	}
	
}
