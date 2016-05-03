package cn.com.youyouparttime;

import cn.com.youyouparttime.base.SysApplication;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class HeightActivity extends Activity {

	private LinearLayout back;
	private EditText minHeight,maxHeight;
	private Button submit;
	String min,max;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_height);
		SysApplication.getInstance().addActivity(this);
		back = (LinearLayout) findViewById(R.id.height_back);
		minHeight = (EditText) findViewById(R.id.height_edt1);
		maxHeight = (EditText) findViewById(R.id.height_edt2);
		
		submit = (Button) findViewById(R.id.submit_height);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				min = minHeight.getText().toString();
				max = maxHeight.getText().toString();
				Intent intent = new Intent();
				intent.putExtra("max", max);
				intent.putExtra("min", min);
				setResult(10, intent);
				finish();
			}
		});
	}

}
