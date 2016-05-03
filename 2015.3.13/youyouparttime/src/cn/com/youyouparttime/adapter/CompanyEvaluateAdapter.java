package cn.com.youyouparttime.adapter;

import java.util.List;

import cn.com.youyouparttime.R;
import cn.com.youyouparttime.entity.CompanyEvaluate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CompanyEvaluateAdapter extends BaseAdapter {

	List<CompanyEvaluate> list;
	Context context;
	LayoutInflater inflater;
	
	
	

	public CompanyEvaluateAdapter(List<CompanyEvaluate> list, Context context) {
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
			convertView = inflater.inflate(R.layout.company_evaluate_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.company_evaluate_title);
			holder.content = (TextView) convertView.findViewById(R.id.company_evaluate_content);
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();
		}
		holder.title.setText("对   "+list.get(position).getUser()+" 同学评价  "+"“"+list.get(position).getEvaluateContent()+"”");
		holder.content.setText("评价详情：" + list.get(position).getEvaluateDetail());
		return convertView;
	}

	class Holder {
		private TextView title;
		private TextView content;
		
	}
	
}
