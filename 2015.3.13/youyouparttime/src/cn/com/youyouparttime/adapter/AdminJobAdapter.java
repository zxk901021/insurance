package cn.com.youyouparttime.adapter;

import java.util.List;

import cn.com.youyouparttime.R;
import cn.com.youyouparttime.entity.CompanyResume;
import cn.com.youyouparttime.util.CommonUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdminJobAdapter extends BaseAdapter {

	List<CompanyResume> list;
	Context context;
	LayoutInflater inflater;

	public AdminJobAdapter(List<CompanyResume> list, Context context) {
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
			convertView = inflater.inflate(R.layout.admin_job_list_item, null);
			holder.jobType = (ImageView) convertView
					.findViewById(R.id.admin_list_item_type);
			holder.jobTitle = (TextView) convertView
					.findViewById(R.id.admin_list_item_title);
			holder.jobTime = (TextView) convertView
					.findViewById(R.id.admin_list_item_time);
			holder.jobStatus = (TextView) convertView
					.findViewById(R.id.admin_list_item_status);
			holder.jobResumeCounts = (TextView) convertView
					.findViewById(R.id.admin_list_item_resume_counts);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.jobType.setImageResource(CommonUtil.setJobTypeImg(list.get(
				position).getJobType()));
		holder.jobTitle.setText(list.get(position).getJobTitle());
		holder.jobTime.setText(list.get(position).getJobTime());
		holder.jobStatus.setText(list.get(position).getJobStatus());
		holder.jobResumeCounts.setText("收到的简历("
				+ list.get(position).getJobResumeCounts() + ")");

		return convertView;
	}

	class Holder {
		private ImageView jobType;
		private TextView jobTitle;
		private TextView jobTime;
		private TextView jobStatus;
		private TextView jobResumeCounts;
	}

}
