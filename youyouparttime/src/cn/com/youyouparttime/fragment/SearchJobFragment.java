package cn.com.youyouparttime.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import cn.com.youyouparttime.ChooseJobListActivity;
import cn.com.youyouparttime.PartTimeDetailActivity;
import cn.com.youyouparttime.R;
import cn.com.youyouparttime.SearchActivity;
import cn.com.youyouparttime.adapter.PartTimeInfoAdapter;
import cn.com.youyouparttime.entity.PartTimeInfo;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.CommonUtil;
import cn.com.youyouparttime.util.HttpUtil;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchJobFragment extends Fragment implements OnClickListener {

	private TextView btnType;
	private TextView btnArea;
	private TextView btnTime;
	private TextView btnPay;
	private PullToRefreshListView newestList;
	private ImageView search;
	private int flag,tag;
	private String guideTitle,param;
	private String typeKey,areaKey,cityKey,city2Key,timeKey,payKey;
//	private ClassifyGuideListAdapter adapter;
	private PartTimeInfoAdapter adapter;
	List<PartTimeInfo> list = new ArrayList<PartTimeInfo>();
	private TextView listResult;
	private int pageCount = 1;
	private boolean scrollEnabled = true;
	
//	private EditText returnMsg;
	

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.e("TAG", "onCreateView");
		return inflater.inflate(R.layout.fragment_search_job, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e("TAG", "onActivityCreated");
		initView();
		
	}

	public void initView() {
		listResult = (TextView) getActivity().findViewById(R.id.newest_list_result);
		btnType = (TextView) getActivity().findViewById(R.id.btn_type);
		btnArea = (TextView) getActivity().findViewById(R.id.btn_area);
		btnTime = (TextView) getActivity().findViewById(R.id.btn_time);
		btnPay = (TextView) getActivity().findViewById(R.id.btn_pay);
		newestList = (PullToRefreshListView) getActivity().findViewById(R.id.newest_list);
		search = (ImageView) getActivity().findViewById(R.id.search_search);
		btnArea.setOnClickListener(this);
		btnTime.setOnClickListener(this);
		btnType.setOnClickListener(this);
		btnPay.setOnClickListener(this);
		search.setOnClickListener(this);
		
//		returnMsg = (EditText) getActivity().findViewById(R.id.return_msg);
		
//		adapter = new ClassifyGuideListAdapter(getList(), getActivity());
		newestList.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label); 
				new GetDataTask().execute(1);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				if (scrollEnabled) {
//					Toast.makeText(TestActivity.this, "正在加载。。。", Toast.LENGTH_SHORT).show();
					pageCount++;
					new GetDataTask().execute(pageCount);
					Log.e("pageCount", pageCount+ "");
					Log.e("scrollEnabled", "true");
				}else {
					Toast.makeText(getActivity(), "已经加载完了", Toast.LENGTH_SHORT).show();
					newestList.onRefreshComplete();
					Log.e("scrollEnabled", "false");
				}
			}
		});
		
		list = getList(1);
		if (list == null) {
			list = new ArrayList<PartTimeInfo>();
		}
		if (list.size() < 20) {
			newestList.setMode(Mode.PULL_FROM_START);
		}else {
			newestList.setMode(Mode.BOTH);
		}
		adapter = new PartTimeInfoAdapter(list, getActivity());
		if (list == null || list.size() == 0) {
			listResult.setVisibility(View.VISIBLE);
			newestList.setVisibility(View.GONE);
		}else {
			listResult.setVisibility(View.GONE);
			newestList.setVisibility(View.VISIBLE);
			newestList.setAdapter(adapter);
			newestList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(getActivity(), PartTimeDetailActivity.class);
					intent.putExtra("id", list.get(position - 1).getId());
					startActivity(intent);
				}
			});
		}
		
	}
	
	private class GetDataTask extends AsyncTask<Integer, Void, List<PartTimeInfo>> {

		private int temp;

		@Override
		protected List<PartTimeInfo> doInBackground(Integer... params) {
			List<PartTimeInfo> result = null;
			temp = params[0];
			Log.e("temp", temp+"");
			try {
				result = getList(params[0]);
				if (result == null) {
					Toast.makeText(getActivity(), "到底了", Toast.LENGTH_SHORT).show();
				}else {
					Log.e("result.size()", result.size()+"");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		
		
		@Override
		protected void onPostExecute(List<PartTimeInfo> result) {
			try {
				if (temp == 1) {
					list.clear();
//					boolean flag = list2.addAll(result);
					list = getList(1);
					adapter = new PartTimeInfoAdapter(list, getActivity());
					newestList.setAdapter(adapter);
					newestList.onRefreshComplete();
					Log.e("111", "111" );
					pageCount = 1;
					scrollEnabled = true;
					return;
				}else if (temp != 1 && result.size()> 0 && result.size()< 20*pageCount) {
//					list2.addAll(result);
					scrollEnabled = false;
					Log.e("222", "222");
				}else if (temp != 1 && result.size()== 20*pageCount) {
//					list2.addAll(result);
					scrollEnabled = true;
					Log.e("333", "333");
				}else if (result == null) {
					scrollEnabled = false;
					Log.e("444", "444");
				}
//				else {
//					Toast.makeText(TestActivity.this, "已加载到底部", Toast.LENGTH_SHORT).show();
//					return;
//				}
				
//				for (int i = 0; i < list2.size(); i++) {
//					Log.e("list2", i+"****"+list2.get(i).getJobName());
//				}
				adapter.notifyDataSetChanged();
				newestList.onRefreshComplete();
				Log.e("list2的size", list.size()+"*" + adapter.getCount());
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_type:
			flag = 0;
			tag = 1;
			guideTitle = "选择类型";
			param = "jobclass";
			gotoChooseIobListActivity();
			break;

		case R.id.btn_area:
			flag = 0;
			tag = 2;
			param = "province";
			guideTitle = "选择区域";
			gotoChooseIobListActivity();
			break;

		case R.id.btn_time:
			flag = 1;
			tag = 3;
			param = "city";
			guideTitle = "选择时间";
			gotoChooseIobListActivity();
			break;

		case R.id.btn_pay:
			flag = 0;
			tag = 4;
			param = "accountclass";
			guideTitle = "薪资结算";
			gotoChooseIobListActivity();
			break;

		case R.id.search_search:
//			flag = 0;
//			guideTitle = "搜索";
//			gotoChooseIobListActivity();
			Intent intent = new Intent(getActivity(), SearchActivity.class);
			startActivity(intent);
			break;
		}
	}

	public void gotoChooseIobListActivity() {
		Intent intent = new Intent(getActivity(), ChooseJobListActivity.class);
		intent.putExtra("flag", flag);
		intent.putExtra("tag", tag);
		intent.putExtra("param", param);
		intent.putExtra("title", guideTitle);
		intent.putExtra("time", timeKey);
		startActivityForResult(intent, 100);
//		startActivity(intent);
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("TAG", "onActivityResult");
		switch (resultCode) {
		case 1:
			String jobType = data.getExtras().getString("value");
			typeKey = data.getExtras().getString("key");
			btnType.setText(jobType);
			break;

		case 2:
			String jobArea = data.getExtras().getString("value");
			areaKey = data.getExtras().getString("key");
			btnArea.setText(jobArea);
			Log.e("key", areaKey);
			break;
		
		case 3:
			String jobTime = data.getExtras().getString("value");
			timeKey = jobTime;
			String time = CommonUtil.strToInt(jobTime);
			if (time == null | time.length() == 0) {
				btnTime.setText("选择时间");
			}else {
				btnTime.setText(CommonUtil.strToInt(jobTime));
			}
			break;
			
		case 4:
			String jobPay = data.getExtras().getString("value");
			payKey = data.getExtras().getString("key");
			btnPay.setText(jobPay);
			break;
		}
//		if (list == null) {
//			list = new ArrayList<PartTimeInfo>();
//		}
//		if (list.size() < 20) {
//			newestList.setMode(Mode.PULL_FROM_START);
//		}else {
//			newestList.setMode(Mode.BOTH);
//		}
//		adapter = new ClassifyGuideListAdapter(getList(), getActivity());
		if (list == null || list.size() == 0) {
			list = new ArrayList<PartTimeInfo>();
			list = getList(1);
		}else {
			list.clear();
			list = getList(1);
		}
		
		adapter = new PartTimeInfoAdapter(list, getActivity());
		
		if (list == null || list.size() == 0) {
			listResult.setVisibility(View.VISIBLE);
			newestList.setVisibility(View.GONE);
		}else {
			listResult.setVisibility(View.GONE);
			newestList.setVisibility(View.VISIBLE);
			newestList.setAdapter(adapter);
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public List<PartTimeInfo> getList(int pageCount){
		JSONObject object = new JSONObject();
//		list = new ArrayList<PartTimeInfo>();
		try {
			object.put("jobclass", CommonUtil.nullToEmpty(typeKey));
			object.put("cityclass", CommonUtil.nullToEmpty(areaKey));
			object.put("timeclass", CommonUtil.nullToEmpty(timeKey));
			object.put("payclass", CommonUtil.nullToEmpty(payKey));
			Log.e("keys", typeKey+"'"+areaKey+"'"+timeKey+"'"+payKey);
			Log.e("search^^^^^^^^^^^", object.toString());
			String result = null;
			if (pageCount == 1) {
				result = HttpUtil.postRequst(UrlUtil.JOB_SEARCH_URL, object);
			}else {
				object.put("page", pageCount);
				result = HttpUtil.postRequst(UrlUtil.JOB_SEARCH_URL, object);
			}
			if (TextUtils.isEmpty(new JSONObject(result).getString("joblist"))) {
				return null;
			}else {
				JSONObject jsonObject = new JSONObject(result);
//				returnMsg.setText(CommonUtil.nullToEmpty(result));
				JSONArray array = jsonObject.getJSONArray("joblist");
				for (int i = 0; i < array.length(); i++) {
					Log.e("jsonobject", array.getJSONObject(i).toString());
				}
				for (int i = 0; i < array.length(); i++) {
					PartTimeInfo info = new PartTimeInfo();
					String name = array.getJSONObject(i).getString("name");
					String type = array.getJSONObject(i).getString("jobclass");
					String address = array.getJSONObject(i).getString("city_name");
					String time = array.getJSONObject(i).getString("sdate");
					String account = array.getJSONObject(i).getString("jobhits");
					String id = array.getJSONObject(i).getString("jobid");
					String vip = array.getJSONObject(i).getString("vip");
					String hot = array.getJSONObject(i).getString("hotest");
					if (vip.equals("0")) {
						info.setVIP(false);
					}else {
						info.setVIP(true);
					}
					if (hot.equals("0")) {
						info.setHot(false);
					}else {
						info.setHot(true);
					}
					info.setJobName(name);
					info.setJobType(type);
					info.setJobAddress(address);
					info.setJobReleaseTime(time);
					info.setPeopleCount(account);
					info.setId(id);
					list.add(info);
					Log.e("list", list.size()+"'");
				}
				return list;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
