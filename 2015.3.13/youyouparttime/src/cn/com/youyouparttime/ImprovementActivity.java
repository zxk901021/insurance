package cn.com.youyouparttime;

import cn.com.youyouparttime.base.SysApplication;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.EditText;
import android.widget.TextView;

public class ImprovementActivity extends Activity {

	private TextView back, submit, contentText, contentTextTo;
	private EditText salaryContent;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_improvement);
		SysApplication.getInstance().addActivity(this);
		back = (TextView) findViewById(R.id.improvement_back);
		submit = (TextView) findViewById(R.id.improvement_submit);
		contentText = (TextView) findViewById(R.id.improvement_content_text);
		contentTextTo = (TextView) findViewById(R.id.improvement_content_text2);
		salaryContent = (EditText) findViewById(R.id.improvement_content);
		contentText.setText(Html.fromHtml("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 您好！<br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 我是之前在您那里工作的&nbsp&nbsp&nbsp&nbsp，我正在参加悠游网举办的“自立之星”评选活动，请您帮我确认一下我在贵企业工作期间的工资是否为"));
		contentTextTo.setText(Html.fromHtml("元，如无异议，请点击“工资属实”，非常感谢您的配合！<br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 祝您工作顺利！"));
	}

}
