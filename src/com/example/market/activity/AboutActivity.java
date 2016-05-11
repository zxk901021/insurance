package com.example.market.activity;

import com.example.market.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AboutActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		findViewById(R.id.layout_upgrade).setOnClickListener(this);
		findViewById(R.id.img_back).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;

		case R.id.layout_upgrade:
			Toast.makeText(AboutActivity.this, "已是最新版本！", Toast.LENGTH_SHORT)
					.show();
			break;

		default:
			break;
		}
	}

}
