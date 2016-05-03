package cn.com.youyouparttime.adapter;

import java.util.List;

import cn.com.youyouparttime.R;
import cn.com.youyouparttime.entity.PartTimeInfo;
import cn.com.youyouparttime.util.CommonUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ClassifyGuideListAdapter extends BaseAdapter {

	
	LayoutInflater inflater;
	List<PartTimeInfo> list;
	Context context;
	
	
	
	public ClassifyGuideListAdapter(List<PartTimeInfo> list, Context context) {
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
			convertView = inflater.inflate(R.layout.classify_list_item, null);
			holder.typeLogo = (ImageView) convertView.findViewById(R.id.classify_list_type);
			holder.partTimeName = (TextView) convertView.findViewById(R.id.classify_list_name);
			holder.isVip = (ImageView) convertView.findViewById(R.id.classify_vip_logo);
			holder.isHot = (ImageView) convertView.findViewById(R.id.classify_hot_logo);
			holder.focus = (TextView) convertView.findViewById(R.id.classify_list_focus);
			holder.address = (TextView) convertView.findViewById(R.id.classify_list_address);
			holder.time = (TextView) convertView.findViewById(R.id.classify_list_time);
			
			
			convertView.setTag(holder);
			
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.typeLogo.setImageResource(CommonUtil.setJobTypeImg(list.get(position).getJobType()));
		holder.partTimeName.setText(list.get(position).getJobName());
		holder.isVip.setImageResource(R.drawable.vip_logo);
		if (list.get(position).isVIP()) {
			holder.isVip.setVisibility(View.VISIBLE);
		}else {
			holder.isVip.setVisibility(View.GONE);
		}
		holder.isHot.setImageResource(R.drawable.hot_logo);
		if (list.get(position).isHot()) {
			holder.isHot.setVisibility(View.VISIBLE);
		}else {
			holder.isHot.setVisibility(View.GONE);
		}
		
		holder.focus.setText(list.get(position).getPeopleCount());
		holder.address.setText(list.get(position).getJobAddress());
		holder.time.setText(list.get(position).getJobReleaseTime());
		return convertView;
	}

	
	class ViewHolder{
		private ImageView typeLogo;
		private TextView partTimeName;
		private ImageView isVip;
		private ImageView isHot;
		private TextView focus;
		private TextView address;
		private TextView time;
	}
}
