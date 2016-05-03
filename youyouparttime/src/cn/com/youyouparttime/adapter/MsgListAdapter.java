package cn.com.youyouparttime.adapter;

import java.util.List;

import cn.com.youyouparttime.R;
import cn.com.youyouparttime.entity.CompanyCommentMsg;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MsgListAdapter extends BaseAdapter {

	List<CompanyCommentMsg> list;
	Context context;
	LayoutInflater inflater;

	public MsgListAdapter(List<CompanyCommentMsg> list, Context context) {
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
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.company_comment_msg, null);
			holder.name = (TextView) convertView
					.findViewById(R.id.company_com_list_name);
			holder.time = (TextView) convertView
					.findViewById(R.id.company_com_list_time);
			holder.isRead = (TextView) convertView
					.findViewById(R.id.company_com_list_isread);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.time.setText(list.get(position).getTime());
		holder.name.setText(list.get(position).getName());
		holder.isRead.setText(list.get(position).getIsRead());
		if (list.get(position).getIsRead().equals("Î´¶Á")) {
			holder.isRead.setTextColor(Color.RED);
		}else {
			holder.isRead.setTextColor(Color.BLACK);
		}
		return convertView;
	}

	class Holder {
		private TextView name;
		private TextView time;
		private TextView isRead;
	}
}
