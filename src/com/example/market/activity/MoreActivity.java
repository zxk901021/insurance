package com.example.market.activity;

import com.example.market.R;
import com.example.market.app.SysApplication;
import com.example.market.dialogfragment.CacheDialogFragment;
import com.example.market.dialogfragment.LogoutDialogFragment;
import com.example.market.utils.CacheUtils;

import android.os.Bundle;
import android.provider.Settings;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

public class MoreActivity extends FragmentActivity implements OnClickListener {

	private TextView mTvCacheSize;
	private SeekBar mSeekBarBrightness;
	private View mViewNightMode;
	private ToggleButton mTgLight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_more);
		mViewNightMode = findViewById(R.id.layout_night_mode);
		mTvCacheSize = (TextView) findViewById(R.id.tv_cache_size);
		mSeekBarBrightness = (SeekBar) findViewById(R.id.seekBar_light);
		mTgLight = (ToggleButton) findViewById(R.id.tgbtn_lightness_ctrl);
		findViewById(R.id.layout_location).setOnClickListener(this);
		findViewById(R.id.img_back).setOnClickListener(this);
		findViewById(R.id.layout_about).setOnClickListener(this);
		findViewById(R.id.layout_clear_cache).setOnClickListener(this);
		findViewById(R.id.layout_recom_apps).setOnClickListener(this);
		findViewById(R.id.btn_logout).setOnClickListener(this);
		mTgLight.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mViewNightMode.setVisibility(View.VISIBLE);
					initBrightness();
				} else {
					mViewNightMode.setVisibility(View.GONE);
					int tmpInt = Settings.System.getInt(getContentResolver(),
							Settings.System.SCREEN_BRIGHTNESS, -1);
					WindowManager.LayoutParams wl = getWindow().getAttributes();
					float tmpFloat = (float) tmpInt / 255;
					if (tmpFloat > 0 && tmpFloat <= 1) {
						wl.screenBrightness = tmpFloat;
					}
					getWindow().setAttributes(wl);

				}
			}
		});
		initCacheSize();
	}

	/**
	 * 获取缓存大小
	 */
	private void initCacheSize() {
		String cacheSize = CacheUtils.getCacheSize(this);
		mTvCacheSize.setText(cacheSize);
	}

	/**
	 * 亮度调节
	 */
	private void initBrightness() {
		// 取得当前亮度
		int normal = Settings.System.getInt(getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS, 255);
		// 进度条绑定当前亮度
		mSeekBarBrightness.setProgress(normal);
		mSeekBarBrightness
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// 取得当前进度
						int tmpInt = seekBar.getProgress();

						// 当进度小于80时，设置成80，防止太黑看不见的后果。
						if (tmpInt < 80) {
							tmpInt = 80;
						}
						// 根据当前进度改变亮度
						WindowManager.LayoutParams wl = getWindow()
								.getAttributes();
						float tmpFloat = (float) tmpInt / 255;
						if (tmpFloat > 0 && tmpFloat <= 1) {
							wl.screenBrightness = tmpFloat;
						}
						getWindow().setAttributes(wl);
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
					}
				});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;
		case R.id.layout_location:// 设置位置
//			startActivity(new Intent(this, LocationActivity.class));
			break;
		case R.id.layout_about: // 关于
			startActivity(new Intent(this, AboutActivity.class));
			break;
		case R.id.layout_clear_cache: // 清除缓存
			clearCache();
			break;
		case R.id.btn_logout: // 注销
			LogoutDialogFragment fragment = new LogoutDialogFragment();
			fragment.show(getSupportFragmentManager(), null);
			break;

		default:
			break;
		}
	}

	/**
	 * 清除缓存
	 */
	private void clearCache() {
		CacheDialogFragment cacheDialogFragment = new CacheDialogFragment();
		cacheDialogFragment.setTextView(mTvCacheSize);
		cacheDialogFragment.show(getSupportFragmentManager(), null);
	}

}
