package cn.com.youyouparttime.adapter;

import java.util.List;
import java.util.Map;

import cn.com.youyouparttime.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class CompanyJobListAdapter extends BaseAdapter {

	List<Map<String, String>> list;
	Context context;
	LayoutInflater inflater;
	
	public CompanyJobListAdapter(List<Map<String, String>> list, Context context) {
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
			convertView = inflater.inflate(R.layout.company_job_list_item, null);
			holder.isChoose = (CheckBox) convertView.findViewById(R.id.company_job_list_cb);
			holder.title = (TextView) convertView.findViewById(R.id.company_job_list_title);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (Boolean.parseBoolean(list.get(position).get("check"))) {
			holder.isChoose.setChecked(true);
		}else {
			holder.isChoose.setChecked(false);
		}
		holder.title.setText(list.get(position).get("title"));
		return convertView;
	}

	class ViewHolder {
		private CheckBox isChoose;
		private TextView title;
	}
	
}
