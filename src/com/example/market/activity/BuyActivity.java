package com.example.market.activity;

import android.os.Bundle;

import com.example.market.R;

import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BuyActivity extends Activity implements OnClickListener {

	private Button buy;
	private TextView repeat;
	private EditText name, id, names, ids;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy);
		initView();
	}

	private void initView() {
		buy = (Button) findViewById(R.id.insurance_buy_btn);
		buy.setOnClickListener(this);
		repeat = (TextView) findViewById(R.id.insurance_repeat);
		repeat.setOnClickListener(this);
		name = (EditText) findViewById(R.id.insurance_name);
		id = (EditText) findViewById(R.id.insurance_idnumber);
		names = (EditText) findViewById(R.id.insurance_name1);
		ids = (EditText) findViewById(R.id.insurance_idnumber1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.buy, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.insurance_buy_btn:
			Toast.makeText(BuyActivity.this, "投保成功！您的保单将在5个工作日内生效！",
					Toast.LENGTH_SHORT).show();
			break;

		case R.id.insurance_repeat:
			String nameStr = name.getText().toString();
			String idStr = id.getText().toString();
			if (nameStr != null) {
				names.setText(nameStr);
			} else {
				name.setText("");
			}
			if (idStr != null) {
				ids.setText(idStr);
			} else {
				ids.setText("");
			}
			break;
		}
	}

}
