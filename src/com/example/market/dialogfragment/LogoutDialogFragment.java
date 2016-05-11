package com.example.market.dialogfragment;



import com.example.market.R;
import com.example.market.app.SysApplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class LogoutDialogFragment extends DialogFragment implements
		OnClickListener {

	private FragmentActivity mActivity;
	private View btnLogout;
	private View divider;
	private int logType;
	/** �ǳ�������Ӧ��listener */

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = getActivity();
		View inflate = inflater.inflate(R.layout.fragment_dialog_logout, null);
		inflate.findViewById(R.id.tv_exit).setOnClickListener(this);
		divider = inflate.findViewById(R.id.divider);
		btnLogout = inflate.findViewById(R.id.tv_logout);
		btnLogout.setOnClickListener(this);
		initLogin();
		getDialog().getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return inflate;
	}

	private void initLogin() {
		// ��ȡ��¼����
		SharedPreferences sp = mActivity.getSharedPreferences("login_type",
				Context.MODE_PRIVATE);
		int type = sp.getInt("login_type", 0);
		switch (type) {
		case 0: // δ��¼��ֻ��ʾ�˳��ͻ��˰�ť
			btnLogout.setVisibility(View.GONE);
			divider.setVisibility(View.GONE);
			break;
		case 1: // ͨ��Bmob��¼
			logType = 1;
			break;
		case 2: // ͨ��΢����¼
			logType = 2;
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		dismiss();
		switch (v.getId()) {
		case R.id.tv_exit: // �˳�Ӧ��
			SysApplication.getInstance().exit();
			break;
		case R.id.tv_logout: // ע��
			logout();
			break;

		default:
			break;
		}
	}

	private void logout() {
		dismiss();
		switch (logType) {

		default:
			break;
		}
	}

	


	/**
	 * ���͹㲥ע��
	 */
	private void anounceLogout() {
		// ����sp
		SharedPreferences sp = mActivity.getSharedPreferences("login_type",
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt("login_type", 0);
		edit.commit();
		Intent intent = new Intent();
		intent.setAction(com.example.market.utils.Constants.BROADCAST_FILTER.FILTER_CODE);
		intent.putExtra(
				com.example.market.utils.Constants.BROADCAST_FILTER.EXTRA_CODE,
				com.example.market.utils.Constants.INTENT_KEY.LOGOUT);
		mActivity.sendBroadcast(intent);
	}


}
