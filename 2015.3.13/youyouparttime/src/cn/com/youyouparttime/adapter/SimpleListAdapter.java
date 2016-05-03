package cn.com.youyouparttime.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.com.youyouparttime.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SimpleListAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	Map<String, String> map;
	List<String> list;
	
	
	
	
	public SimpleListAdapter(Context context, Map<String, String> map) {
		super();
		this.context = context;
		this.map = map;
		list = new ArrayList<String>(map.values());
		inflater = LayoutInflater.from(context);
	}
	
	public SimpleListAdapter(Context context, List<String> list) {
		super();
		this.context = context;
		this.list = list;
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
			convertView = inflater.inflate(R.layout.simple_list, null);
			holder.textView = (TextView) convertView.findViewById(R.id.simple_list_text);
			holder.imageView = (ImageView) convertView.findViewById(R.id.simple_list_button);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.textView.setText(list.get(position));
		
		return convertView;
	}
	class ViewHolder {
		TextView textView;
		ImageView imageView;
	}
}
