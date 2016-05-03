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
		contentText.setText(Html.fromHtml("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp ���ã�<br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp ����֮ǰ�������﹤����&nbsp&nbsp&nbsp&nbsp�������ڲμ��������ٰ�ġ�����֮�ǡ���ѡ�����������ȷ��һ�����ڹ���ҵ�����ڼ�Ĺ����Ƿ�Ϊ"));
		contentTextTo.setText(Html.fromHtml("Ԫ���������飬������������ʵ�����ǳ���л������ϣ�<br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp ף������˳����"));
	}

}
