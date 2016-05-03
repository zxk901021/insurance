package cn.com.youyouparttime;

import cn.com.youyouparttime.base.SysApplication;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailParttimeActivity extends Activity {

	private TextView back;
	private ImageView img1,img2,img3,img4,img5,img6,img7,img8,img9,
	img10,img11,img12,img13,img14,img15,img16,img17,
	img18,img19,img20,img21;
	private String time;
	private String[] times;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_parttime_time);
		SysApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		time = intent.getStringExtra("time");
		back = (TextView) findViewById(R.id.time_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		img1 = (ImageView) findViewById(R.id.time_img_1);
		img2 = (ImageView) findViewById(R.id.time_img_2);
		img3 = (ImageView) findViewById(R.id.time_img_3);
		img4 = (ImageView) findViewById(R.id.time_img_4);
		img5 = (ImageView) findViewById(R.id.time_img_5);
		img6 = (ImageView) findViewById(R.id.time_img_6);
		img7 = (ImageView) findViewById(R.id.time_img_7);
		img8 = (ImageView) findViewById(R.id.time_img_8);
		img9 = (ImageView) findViewById(R.id.time_img_9);
		img10 = (ImageView) findViewById(R.id.time_img_10);
		img11 = (ImageView) findViewById(R.id.time_img_11);
		img12 = (ImageView) findViewById(R.id.time_img_12);
		img13 = (ImageView) findViewById(R.id.time_img_13);
		img14 = (ImageView) findViewById(R.id.time_img_14);
		img15 = (ImageView) findViewById(R.id.time_img_15);
		img16 = (ImageView) findViewById(R.id.time_img_16);
		img17 = (ImageView) findViewById(R.id.time_img_17);
		img18 = (ImageView) findViewById(R.id.time_img_18);
		img19 = (ImageView) findViewById(R.id.time_img_19);
		img20 = (ImageView) findViewById(R.id.time_img_20);
		img21 = (ImageView) findViewById(R.id.time_img_21);
		times = time.split(",");
		for (int i = 0; i < times.length; i++) {
			if (times[i] != null && !times[i].equals("")) {
				int a = Integer.parseInt(times[i]);
				setBackground(a);
			}
			
			
		}
	}
	
	public void setBackground(int position){
		switch (position) {
		case 0:
			img1.setImageResource(R.drawable.time_pressed);
			break;

		case 1:
			img2.setImageResource(R.drawable.time_pressed);
			break;
			
		case 2:
			img3.setImageResource(R.drawable.time_pressed);
			break;
			
		case 3:
			img4.setImageResource(R.drawable.time_pressed);
			break;
			
		case 4:
			img5.setImageResource(R.drawable.time_pressed);
			break;
			
		case 5:
			img6.setImageResource(R.drawable.time_pressed);
			break;
			
		case 6:
			img7.setImageResource(R.drawable.time_pressed);
			break;
			
		case 7:
			img8.setImageResource(R.drawable.time_pressed);
			break;
			
		case 8:
			img9.setImageResource(R.drawable.time_pressed);
			break;
			
		case 9:
			img10.setImageResource(R.drawable.time_pressed);
			break;
			
		case 10:
			img11.setImageResource(R.drawable.time_pressed);
			break;
			
		case 11:
			img12.setImageResource(R.drawable.time_pressed);
			break;
		case 12:
			img13.setImageResource(R.drawable.time_pressed);
			break;
			
		case 13:
			img14.setImageResource(R.drawable.time_pressed);
			break;
			
		case 14:
			img15.setImageResource(R.drawable.time_pressed);
			break;
			
		case 15:
			img16.setImageResource(R.drawable.time_pressed);
			break;
			
		case 16:
			img17.setImageResource(R.drawable.time_pressed);
			break;
			
		case 17:
			img18.setImageResource(R.drawable.time_pressed);
			break;
			
		case 18:
			img19.setImageResource(R.drawable.time_pressed);
			break;
			
		case 19:
			img20.setImageResource(R.drawable.time_pressed);
			break;
			
		case 20:
			img21.setImageResource(R.drawable.time_pressed);
			break;
		}
	}
}
