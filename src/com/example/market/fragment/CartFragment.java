package com.example.market.fragment;



import com.example.market.R;
import com.example.market.activity.InsuranceDetailActivity;
import com.example.market.activity.MainActivity;
import com.example.market.utils.Constants;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class CartFragment extends Fragment implements OnClickListener {

	private View layout;
	private View layoutNull;
	private View mViewLogin;
	private ListView mListView;
	private TextView mTvPrice; // �ϼ�
	private TextView mTvTotal; // �ܶ�
	private TextView mTvCount; // ѡ����Ʒ��
	private TextView mTvEdit;
	private TextView mTvAddUp;


	private double price; // �ܼ�
	private int num; // ѡ�е���Ʒ��
	private String uid;
	private String goodsCartResult;

	private boolean isEdit; // �Ƿ����ڱ༭


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MainActivity activity = (MainActivity) getActivity();
		SharedPreferences sp = getActivity().getSharedPreferences("MyPrefer", Context.MODE_PRIVATE);
		uid = sp.getString("uid", "");
		boolean isLogined = sp.getBoolean("isLogin", false);
		if (layout != null) {
//			initData();
			
			if (isLogined) {
				mViewLogin.setVisibility(View.GONE);
			}
			// ��ֹ���new��Ƭ�ζ������ͼƬ��������
			return layout;
		}
		layout = inflater.inflate(R.layout.fragment_cart, container, false);
		initView();
		if (isLogined) {
			mViewLogin.setVisibility(View.GONE);
		}
		
		setOnListener();
		return layout;
	}

	private void setOnListener() {
		mTvEdit.setOnClickListener(this);
		// ��ֹ�����͸
		layout.findViewById(R.id.huodong_layout1_img).setOnClickListener(this);
		layout.findViewById(R.id.huodong_zhuanti_img1).setOnClickListener(this);
		layout.findViewById(R.id.huodong_zhuanti_img2).setOnClickListener(this);
	}

	private void initView() {
		layoutNull = layout.findViewById(R.id.layout_null);
		mTvEdit = (TextView) layout.findViewById(R.id.tv_edit_cart);
		mTvPrice = (TextView) layout.findViewById(R.id.tv_price);
		mTvCount = (TextView) layout.findViewById(R.id.tv_count);
	}

	private void initData() {
		// �첽�����ݿ��л�ȡ����
//		new InCartTask().execute();
	}


	/**
	 * ֪ͨ���¹��ﳵ��Ʒ����
	 */
	private void notifyInCartNumChanged() {
		// ֪ͨ��ҳˢ�¹��ﳵ��Ʒ��
		Intent intent = new Intent();
		intent.setAction(Constants.BROADCAST_FILTER.FILTER_CODE);
		intent.putExtra(Constants.BROADCAST_FILTER.EXTRA_CODE,
				Constants.INTENT_KEY.REFRESH_INCART);
		getActivity().sendBroadcast(intent);
	}

	private void initListView() {
		
		View foot = getActivity().getLayoutInflater().inflate(
				R.layout.foot_cart_list, null);
		mTvAddUp = (TextView) foot.findViewById(R.id.tv_add_up);
		mListView.addFooterView(foot, null, false);
	}
	

	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// ��layout�Ӹ�������Ƴ�
		ViewGroup parent = (ViewGroup) layout.getParent();
		parent.removeView(layout);
	}

	/**
	 * ��ȡ����
	 * 
	 * @param tvNum
	 * @return
	 */
	private int getNum(TextView tvNum) {
		String num = tvNum.getText().toString().trim();
		return Integer.valueOf(num);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.INTENT_KEY.LOGIN_REQUEST_CODE) {
			if (resultCode == Constants.INTENT_KEY.LOGIN_RESULT_SUCCESS_CODE) {
				SharedPreferences sp = getActivity().getSharedPreferences(
						"login_type", Context.MODE_PRIVATE);
				int type = sp.getInt("login_type", 0);
				String uid = "";
				String icon = "";
				switch (type) {
				case 1:
					uid = data.getStringExtra("uid");
					break;
				case 2:
					uid = data.getStringExtra("screen_name");
					icon = data.getStringExtra("profile_image_url");
					break;

				default:
					break;
				}
				mViewLogin.setVisibility(View.GONE);
				MainActivity activity = (MainActivity) getActivity();
				// ����¼������ø�MainActivity
				activity.setIsLogined(true, uid, icon);
			}
		} else if (requestCode == Constants.INTENT_KEY.REQUEST_CART_TO_DETAIL) {
			// ˢ�¹��ﳵ
			initData();
			// ˢ�¼۸�
		}
	}


	class ViewHolder {
		CheckBox btnCheck;
		Button btnAdd;
		Button btnReduce;
		Button btnNumEdit;
		ImageView imgGoods;
		TextView tvGoodsName;
		TextView tvGoodsPrice;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			
		case R.id.huodong_layout1_img:
			gotoWebDetail(3);
			break;
			
		case R.id.huodong_zhuanti_img1:
			gotoWebDetail(3);
			break;
			
		case R.id.huodong_zhuanti_img2:
			gotoWebDetail(3);
			break;

		default:
			break;
		}
	}
	
	private void gotoWebDetail(int flag) {
		Intent intent = new Intent(getActivity(), InsuranceDetailActivity.class);
		intent.putExtra("flag", flag);
		startActivity(intent);
	}

}
