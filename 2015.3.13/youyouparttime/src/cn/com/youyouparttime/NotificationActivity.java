package cn.com.youyouparttime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class NotificationActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		Intent intent = this.getIntent();
		System.out.println("Get intent:" + intent);
		final String url = intent.getStringExtra("url");
		Bundle bundle = intent.getExtras();
		final int m = bundle.getInt("flag");

		Log.i("avi", "tempActicity中接收到的  flag: " + m);
		Log.i("avi", "tempActivity中接收到的  url: " + url);

		Button bt_cancel = (Button) findViewById(R.id.bt_pause);
		Button bt_pause = (Button) findViewById(R.id.bt_start);
		Button bt_restart = (Button) findViewById(R.id.bt_cancle);

		bt_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent cancel = new Intent()
						.setAction("android.basic.notification.click.cancel");
				Bundle bundel2 = new Bundle();
				bundel2.putInt("flag", m);
				cancel.putExtras(bundel2);
				cancel.putExtra("url", url);
				sendBroadcast(cancel);
				finish();

			}
		});

		bt_pause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent pause = new Intent()
						.setAction("android.basic.notification.click.pause");
				Bundle bundel2 = new Bundle();
				bundel2.putInt("flag", m);
				pause.putExtras(bundel2);
				pause.putExtra("url", url);
				sendBroadcast(pause);
				finish();
			}
		});

		bt_restart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent cancel = new Intent()
						.setAction("android.basic.notification.click.restart");
				Bundle bundel2 = new Bundle();
				bundel2.putInt("flag", m);
				cancel.putExtras(bundel2);
				cancel.putExtra("url", url);
				sendBroadcast(cancel);
				finish();
			}
		});
	}


}
