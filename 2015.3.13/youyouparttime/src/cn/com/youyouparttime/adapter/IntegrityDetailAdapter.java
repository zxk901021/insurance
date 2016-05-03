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

public class IntegrityDetailAdapter extends BaseAdapter {

	List<IntergrityInfo> list;
	LayoutInflater inflater;
	Context context;
	
	
	
	public IntegrityDetailAdapter(List<IntergrityInfo> list, Context context) {
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
			convertView = inflater.inflate(R.layout.integrity_detail_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.integrity_detail_name);
			holder.content = (TextView) convertView.findViewById(R.id.integrity_detail_content);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText("ª·‘±£∫"+list.get(position).getIntergrityMember());
		holder.content.setText(list.get(position).getIntergrityComment());
		return convertView;
	}

	
	class ViewHolder {
		private TextView name;
		private TextView content;
	}
}
