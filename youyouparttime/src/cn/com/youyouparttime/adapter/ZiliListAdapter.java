package cn.com.youyouparttime.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.youyouparttime.ImprovementActivity;
import cn.com.youyouparttime.R;
import cn.com.youyouparttime.SuggestActivity;
import cn.com.youyouparttime.entity.ZiliEntity;
import cn.com.youyouparttime.util.CommonUtil;

public class ZiliListAdapter extends BaseAdapter {

	List<ZiliEntity> list;
	Context context;
	LayoutInflater inflater;
	
	
	
	public ZiliListAdapter(List<ZiliEntity> list, Context context) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.zili_list_item, null);
			holder.image = (ImageView) convertView.findViewById(R.id.zili_image);
			holder.title = (TextView) convertView.findViewById(R.id.zili_item_title);
			holder.time	= (TextView) convertView.findViewById(R.id.zili_item_time);
			holder.status = (TextView) convertView.findViewById(R.id.zili_submit_status);
			holder.submit = (Button) convertView.findViewById(R.id.apply_ensure_zili_btn);
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();
		}
		try {
			holder.image.setImageResource(CommonUtil.setJobTypeImg(list.get(position).getJobType()));
			Log.e("img", list.get(position).getJobType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		holder.title.setText(list.get(position).getTitle());
		holder.time.setText(list.get(position).getTime());
		holder.status.setText(exchangeStatus(list.get(position).getSubmitStatus()));
		holder.submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ImprovementActivity.class);
				intent.putExtra("apply", true);
				intent.putExtra("jobid", list.get(position).getJobId());
				intent.putExtra("cid", list.get(position).getcId());
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	class Holder{
		private ImageView image;
		private TextView title;
		private TextView time;
		private TextView status;
		private Button submit;
		
	}
	
	public String exchangeStatus(String value){
		String status = null;
		if (value.equals("0")) {
			status = "确认中";
		}else if (value.equals("1")) {
			status = "已确认";
		}else if (value.equals("2")) {
			status = "确认失败";
		}
		return status;
	}
}
