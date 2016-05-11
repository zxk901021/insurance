package com.example.market.activity;

import java.lang.reflect.Method;

import com.example.market.R;
import com.example.market.app.SysApplication;
import com.example.market.dialogfragment.ExitDialogFragment;
import com.example.market.dialogfragment.LightCtrlDialogFragment;
import com.example.market.fragment.CartFragment;
import com.example.market.fragment.FindFragment;
import com.example.market.fragment.HomeFragment;
import com.example.market.fragment.MineFragment;
import com.example.market.fragment.CategoryFragment;
import com.example.market.utils.Constants;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private FragmentTabHost mTabHost;
	private Class[] mFragments = new Class[] { HomeFragment.class,
			CategoryFragment.class, FindFragment.class, CartFragment.class,
			MineFragment.class };
	private int[] mTabSelectors = new int[] { R.drawable.main_bottom_tab_home,
			R.drawable.main_bottom_tab_category,
			R.drawable.main_bottom_tab_find, R.drawable.main_bottom_tab_cart,
			R.drawable.main_bottom_tab_mine };
	private String[] mTabSpecs = new String[] { "home", "category", "find",
			"cart", "mine" };

	private boolean isBack; // �Ƿ�����������ؼ�
	private boolean isLogined; // �Ƿ��ѵ�¼
	private String uid; // �û���
	private String icon; // �û�ͷ���ַ
	private MyReceiver receiver;
	private boolean isFromFavor; // �Ƿ�ӹ�ע������ת��
	private boolean isFromDetail; // �Ƿ�����������ת��
	private TextView mTvNumInCart; // ���ﳵ��Ʒ��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_main);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		addTab();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// ע��㲥������
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter(
				Constants.BROADCAST_FILTER.FILTER_CODE);
		registerReceiver(receiver, filter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ����Ǵӹ�ע������ת������ת��HomePage
		// ������onReceive()��ֱ�����ã���������IllegalStateException: Can not perform this
		// action after onSaveInstanceState
		if (isFromFavor) {
			mTabHost.setCurrentTab(0);
			isFromFavor = false;
		} else if (isFromDetail) {
			mTabHost.setCurrentTab(3);
			isFromDetail = false;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}


	private void addTab() {
		for (int i = 0; i < 5; i++) {
			View tabIndicator = getLayoutInflater().inflate(
					R.layout.tab_indicator, null);
			ImageView imageView = (ImageView) tabIndicator
					.findViewById(R.id.imageView1);
			imageView.setImageResource(mTabSelectors[i]);
			if (i == 3) {
				mTvNumInCart = (TextView) tabIndicator
						.findViewById(R.id.textView1);
			}
			mTabHost.addTab(
					mTabHost.newTabSpec(mTabSpecs[i])
							.setIndicator(tabIndicator), mFragments[i], null);
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// ����ɨ�������ڽ�������ʾ��
		if (resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			// TODO ����ɨ����
			Toast.makeText(this, scanResult, Toast.LENGTH_LONG).show();
			if (scanResult.indexOf("http//") != -1) {
				Uri uri = Uri.parse(scanResult);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		}
	}

	/**
	 * ����
	 */
	public void activeCategory() {
		mTabHost.setCurrentTab(1);
	}
	
	public void activeCart(){
		mTabHost.setCurrentTab(3);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			// ����ǰ������ҳ�����ȷ�����ҳ
			if (mTabHost.getCurrentTab() != 0) {
				mTabHost.setCurrentTab(0);
				return false;
			}
			// ˫���������棬Ĭ�Ϸ���true������finish()
			if (!isBack) {
				isBack = true;
				Toast.makeText(this, "�ٰ�һ�η��ؼ��ص�����", Toast.LENGTH_SHORT).show();
				mTabHost.postDelayed(new Runnable() {

					@Override
					public void run() {
						isBack = false;
					}
				}, 2000);
				return false;
			}

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * ��Ƭ�ε��ã����õ�¼��Ϣ
	 * 
	 * @param isLogined
	 * @param uid
	 * @param icon
	 */
	public void setIsLogined(boolean isLogined, String uid, String icon) {
		this.isLogined = isLogined;
		this.uid = uid;
		this.icon = icon;
	}

	/**
	 * ��Ƭ�ε��ã���ȡ�Ƿ��ѵ�¼
	 * 
	 * @return
	 */
	public boolean getLogined() {
		return isLogined;
	}

	/**
	 * ��Ƭ�ε��ã���ȡ�û���
	 * 
	 * @return
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * ��Ƭ�ε��ã���ȡ�û�ͷ���ַ
	 * 
	 * @return
	 */
	public String getIcon() {
		return icon;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setIconEnable(menu, true);
		MenuItem item = menu.add(0, 1, 1, "����");
		item.setIcon(R.drawable.main_menu_setup);
		MenuItem item2 = menu.add(0, 2, 2, "�����¼");
		item2.setIcon(R.drawable.main_menu_history);
		MenuItem item3 = menu.add(0, 3, 3, "�������");
		item3.setIcon(R.drawable.main_menu_feedback);
		MenuItem item4 = menu.add(0, 4, 4, "���ȵ���");
		item4.setIcon(R.drawable.main_menu_lightmode_day);
		MenuItem item5 = menu.add(0, 5, 5, "����");
		item5.setIcon(R.drawable.main_menu_about);
		MenuItem item6 = menu.add(0, 6, 6, "�˳�");
		item6.setIcon(R.drawable.main_menu_exit);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1: // ����
			startActivity(new Intent(this, MoreActivity.class));
			break;
		case 4: // ���ȵ���
			LightCtrlDialogFragment lightCtrlDialogFragment = new LightCtrlDialogFragment();
			lightCtrlDialogFragment.show(getSupportFragmentManager(), null);
			break;
		case 5: // ����
			startActivity(new Intent(this, AboutActivity.class));
			break;
		case 6: // �˳�
			ExitDialogFragment fragment = new ExitDialogFragment();
			fragment.show(getSupportFragmentManager(), null);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// enableΪtrueʱ���˵����ͼ����Ч��enableΪfalseʱ��Ч��4.0ϵͳĬ����Ч
	private void setIconEnable(Menu menu, boolean enable) {
		try {
			Class<?> clazz = Class
					.forName("com.android.internal.view.menu.MenuBuilder");
			Method m = clazz.getDeclaredMethod("setOptionalIconsVisible",
					boolean.class);
			m.setAccessible(true);

			// MenuBuilderʵ��Menu�ӿڣ������˵�ʱ����������menu��ʵ����MenuBuilder����(java�Ķ�̬����)
			m.invoke(menu, enable);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String extra = intent
					.getStringExtra(Constants.BROADCAST_FILTER.EXTRA_CODE);
			if (extra.equals(Constants.INTENT_KEY.FROM_FAVOR)) {
				isFromFavor = true;
			}  else if (extra.equals(Constants.INTENT_KEY.FROM_DETAIL)) {
				isFromDetail = true;
			} else if (extra.equals(Constants.INTENT_KEY.LOGOUT)) {
				isLogined = false;
			}
		}

	}

}
