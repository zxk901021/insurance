package cn.com.youyouparttime.adapter;

import java.util.List;

import org.json.JSONObject;

import cn.com.youyouparttime.R;
import cn.com.youyouparttime.entity.PartTimeInfo;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.CommonUtil;
import cn.com.youyouparttime.util.HttpUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PartTimeInfoAdapter extends BaseAdapter {

	
	LayoutInflater inflater;
	List<PartTimeInfo> list;
	Context context;
	
	
	public PartTimeInfoAdapter(List<PartTimeInfo> list, Context context) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.part_time_info, null);
			holder.typeLogo = (ImageView) convertView.findViewById(R.id.part_time_type);
			holder.partTimeName = (TextView) convertView.findViewById(R.id.part_time_name);
			holder.isVip = (ImageView) convertView.findViewById(R.id.vip_logo);
			holder.isHot = (ImageView) convertView.findViewById(R.id.hot_logo);
			holder.money = (TextView) convertView.findViewById(R.id.part_time_reward);
			holder.address = (TextView) convertView.findViewById(R.id.part_time_address);
			holder.time = (TextView) convertView.findViewById(R.id.part_time_time);
			holder.count = (TextView) convertView.findViewById(R.id.part_time_count);
			holder.delete = (Button) convertView.findViewById(R.id.part_time_delete);
			
			convertView.setTag(holder);
			
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.typeLogo.setImageResource(CommonUtil.setJobTypeImg(list.get(position).getJobType()));
		holder.partTimeName.setText(list.get(position).getJobName());
		holder.isVip.setImageResource(R.drawable.vip_logo);
		holder.isHot.setImageResource(R.drawable.hot_logo);
		if (list.get(position).isVIP()) {
			holder.isVip.setVisibility(View.VISIBLE);
		}else {
			holder.isVip.setVisibility(View.GONE);
		}
		if (list.get(position).isHot()) {
			holder.isHot.setVisibility(View.VISIBLE);
		}else {
			holder.isHot.setVisibility(View.GONE);
		}
		if (list.get(position).isFlag()) {
			holder.delete.setVisibility(View.VISIBLE);
		}else {
			holder.delete.setVisibility(View.GONE);
		}
		holder.money.setText(list.get(position).getJobReward());
		holder.address.setText(list.get(position).getJobAddress());
		holder.time.setText(list.get(position).getJobReleaseTime());
		holder.count.setText(list.get(position).getPeopleCount());
		holder.delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String result = deleteCollect(list.get(position).getFavId());
				if (result.equals("true")) {
					list.remove(position);
					notifyDataSetChanged();
				}
				
			}
		});
		return convertView;
	}
	
	public String deleteCollect(String favId){
		JSONObject object = new JSONObject();
		String call = null;
		try {
			object.put("fav_id", favId);
			String result = HttpUtil.postRequst(UrlUtil.MY_COLLECT_URL, object);
			String msg = new JSONObject(result).getString("msg");
			call = new JSONObject(result).getString("result");
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return call;
	}

	
	class ViewHolder{
		private ImageView typeLogo;
		private TextView partTimeName;
		private ImageView isVip;
		private ImageView isHot;
		private TextView money;
		private TextView address;
		private TextView time;
		private TextView count;
		private Button delete;
	}
}
