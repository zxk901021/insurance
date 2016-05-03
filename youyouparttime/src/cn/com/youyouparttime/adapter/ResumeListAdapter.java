package cn.com.youyouparttime.adapter;

import java.util.List;


import cn.com.youyouparttime.R;
import cn.com.youyouparttime.entity.ResumeListEntity;
import cn.com.youyouparttime.util.ImageLoader;
import cn.com.youyouparttime.view.CircleImageView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ResumeListAdapter extends BaseAdapter {


	List<ResumeListEntity> list;
	Context context;
	LayoutInflater inflater;
	boolean flag;
	
	private boolean mBusy = false;
	private String[] urlArrays;
	public void setFlagBusy(boolean busy) {
		this.mBusy = busy;
	}

	
	private ImageLoader mImageLoader;

	public ResumeListAdapter(List<ResumeListEntity> list, Context context,
			boolean flag, String[] urls) {
		super();
		this.list = list;
		this.context = context;
		this.flag = flag;
		urlArrays = urls;
		mImageLoader = new ImageLoader(context);
		inflater = LayoutInflater.from(context);
	}

	public ImageLoader getImageLoader(){
		return mImageLoader;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.resume_list_item, null);
			holder.isSelected = (CheckBox) convertView
					.findViewById(R.id.resume_list_is_selected);
			holder.photo = (CircleImageView) convertView
					.findViewById(R.id.resume_list_photo);
			holder.name = (TextView) convertView
					.findViewById(R.id.resume_list_name);
			holder.type = (TextView) convertView
					.findViewById(R.id.resume_list_type);
			holder.school = (TextView) convertView
					.findViewById(R.id.resume_list_school);
			holder.counts = (TextView) convertView
					.findViewById(R.id.resume_list_foucus_counts);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		if (flag) {
			holder.isSelected.setVisibility(View.VISIBLE);
		} else {
			holder.isSelected.setVisibility(View.GONE);
		} 
		String url = "";
		url = urlArrays[position % urlArrays.length];
		
		
		holder.photo.setImageResource(R.drawable.applogo);
		if (!mBusy) {
			mImageLoader.DisplayImage(url, holder.photo, false);
		} else {
			mImageLoader.DisplayImage(url, holder.photo, false);		
		}
		
		holder.name.setText(list.get(position).getName());
		holder.type.setText(list.get(position).getJobType());
		holder.school.setText(list.get(position).getSchool());
		holder.counts.setText(list.get(position).getFoucusCounts());
		holder.isSelected.setChecked(list.get(position).isSelected());
		return convertView;
	}

	class Holder {
		private CheckBox isSelected;
		private CircleImageView photo;
		private TextView name;
		private TextView type;
		private TextView school;
		private TextView counts;
	}

	
}
