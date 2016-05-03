package cn.com.youyouparttime;

import org.json.JSONObject;

import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.Constant;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.HttpUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PartTimePrideActivity extends Activity implements OnClickListener {

	private LinearLayout back, integrityBtnLayout;
	private RelativeLayout ziliLayout, integrityLayout, thankfulLayout,
			thankfulLayout2;
	private TextView ziliContent, parentContent, societyContent,
			integrityContent, prideSumRewards, prideSumRewardsContent;
	private Button lightupZili, lightupIntegrity, lightupThankful,
			lightupThankful2, checkIntegrityList;
	private boolean flag, flag1, flag2, flag3;
	private String uid;
	private SharedPreferences sharedPreferences;
	private ImageView icon1, icon2, icon3, icon4;
	private ImageView ziliStar, parentStar, societyStar, integrityStar;
	private String zl, thankParent, thankSociety, integrity;
	private String sumRewards;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_part_time_pride);
		SysApplication.getInstance().addActivity(this);
		sharedPreferences = getSharedPreferences("myPrefer", 0);
		uid = sharedPreferences.getString("uid", null);
		sumRewards = sharedPreferences.getString("sumRewards", "0");
		getInfo();
		initView();
	}

	public void initView() {
		back = (LinearLayout) findViewById(R.id.part_pride_back);
		parentContent = (TextView) findViewById(R.id.thankful_text_content_detail);
		societyContent = (TextView) findViewById(R.id.thankful_text_content_detail2);
		integrityContent = (TextView) findViewById(R.id.integrity_text_content_detail);
		ziliLayout = (RelativeLayout) findViewById(R.id.zili_layout);
		ziliContent = (TextView) findViewById(R.id.pride_text_content_detail);
		lightupZili = (Button) findViewById(R.id.light_up_zili_btn);
		integrityLayout = (RelativeLayout) findViewById(R.id.integrity_layout);
		lightupIntegrity = (Button) findViewById(R.id.light_up_integrity_btn);
		thankfulLayout = (RelativeLayout) findViewById(R.id.thankful_layout);
		thankfulLayout2 = (RelativeLayout) findViewById(R.id.thankful_layout2);
		lightupThankful = (Button) findViewById(R.id.light_up_thankful_btn);
		lightupThankful2 = (Button) findViewById(R.id.light_up_thankful_btn2);
		icon1 = (ImageView) findViewById(R.id.pride_img1);
		icon2 = (ImageView) findViewById(R.id.pride_img2);
		icon3 = (ImageView) findViewById(R.id.pride_img3);
		icon4 = (ImageView) findViewById(R.id.pride_img4);
		ziliStar = (ImageView) findViewById(R.id.pride_image1);
		parentStar = (ImageView) findViewById(R.id.thankful_image);
		societyStar = (ImageView) findViewById(R.id.thankful_image2);
		integrityStar = (ImageView) findViewById(R.id.integrity_image);
		prideSumRewards = (TextView) findViewById(R.id.pride_text_content_sum_rewards);
		prideSumRewardsContent = (TextView) findViewById(R.id.pride_text_content_sum_rewards_content);
		integrityBtnLayout = (LinearLayout) findViewById(R.id.integrity_btn_layout);
		checkIntegrityList = (Button) findViewById(R.id.check_integrity_list);
		lightupThankful.setOnClickListener(this);
		lightupThankful2.setOnClickListener(this);
		thankfulLayout.setOnClickListener(this);
		thankfulLayout2.setOnClickListener(this);
		integrityLayout.setOnClickListener(this);
		lightupIntegrity.setOnClickListener(this);
		lightupZili.setOnClickListener(this);
		back.setOnClickListener(this);
		ziliLayout.setOnClickListener(this);
		checkIntegrityList.setOnClickListener(this);
		ziliContent.setText(Html.fromHtml(Constant.SELF_STAR_CONTENT));
		parentContent.setText(Html.fromHtml(Constant.THANKFUL_STAR_CONTENT + Constant.THANKFUL_PARENT_CONTENT));
		societyContent.setText(Html.fromHtml(Constant.THANKFUL_STAR_CONTENT + Constant.THANKFUL_SOCIETY_CONTENT));
		integrityContent
				.setText(Html.fromHtml(Constant.INTEGRITY_STAR_CONTENT));
		prideSumRewardsContent.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		prideSumRewardsContent.setText(sumRewards);
		if (zl.equals("0")) {
			ziliStar.setImageResource(R.drawable.zili_star1);
		}
		if (zl.equals("1")) {
			ziliStar.setImageResource(R.drawable.zili_star1_light);
		}
		if (zl.equals("2")) {
			ziliStar.setImageResource(R.drawable.zili_star2_light);
		}
		if (zl.equals("3")) {
			ziliStar.setImageResource(R.drawable.zili_star3_light);
		}
		if (thankParent.equals("0")) {
			parentStar.setImageResource(R.drawable.thanks_parent);
		}
		if (thankParent.equals("1")) {
			parentStar.setImageResource(R.drawable.thanks_parents_light);
		}
		if (thankSociety.equals("0")) {
			societyStar.setImageResource(R.drawable.thanks_society);
		}
		if (thankSociety.equals("1")) {
			societyStar.setImageResource(R.drawable.thanks_society_light);
		}
		if (integrity.equals("0")) {
			integrityStar.setImageResource(R.drawable.integrity);
		}
		if (integrity.equals("1")) {
			integrityStar.setImageResource(R.drawable.integrity_light);
		}
	}

	public void getInfo() {
		JSONObject object = new JSONObject();
		JSONObject infoJson = new JSONObject();
		try {
			object.put("uid", uid);
			String result = HttpUtil.postRequst(UrlUtil.STUDENT_INFO_URL,
					object);
			infoJson = new JSONObject(result);
			zl = infoJson.getJSONObject("userinfo").getString("zl");
			thankParent = infoJson.getJSONObject("userinfo").getString(
					"ge_parent");
			thankSociety = infoJson.getJSONObject("userinfo").getString(
					"ge_society");
			integrity = infoJson.getJSONObject("userinfo").getString("cx");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.part_pride_back:
			finish();
			break;

		case R.id.zili_layout:
			if (!flag) {
				ziliContent.setVisibility(View.VISIBLE);
				lightupZili.setVisibility(View.VISIBLE);
				prideSumRewards.setVisibility(View.VISIBLE);
				prideSumRewardsContent.setVisibility(View.VISIBLE);
				icon1.setImageResource(R.drawable.icon_down);
				flag = true;
			} else {
				ziliContent.setVisibility(View.GONE);
				lightupZili.setVisibility(View.GONE);
				prideSumRewards.setVisibility(View.GONE);
				prideSumRewardsContent.setVisibility(View.GONE);
				icon1.setImageResource(R.drawable.icon);
				flag = false;

			}
			break;

		case R.id.light_up_zili_btn:
			Intent intent = new Intent(PartTimePrideActivity.this,
					ZiliListActivity.class);
			startActivity(intent);

			break;

		case R.id.integrity_layout:
			if (!flag3) {
				integrityContent.setVisibility(View.VISIBLE);
				integrityBtnLayout.setVisibility(View.VISIBLE);
				icon4.setImageResource(R.drawable.icon_down);
				flag3 = true;
			} else {
				integrityContent.setVisibility(View.GONE);
				integrityBtnLayout.setVisibility(View.GONE);
				icon4.setImageResource(R.drawable.icon);
				flag3 = false;
			}
			break;
		case R.id.light_up_integrity_btn:
			JSONObject object = new JSONObject();
			try {
				object.put("uid", uid);
				String result = HttpUtil.postRequst(
						UrlUtil.GET_INTEGERITY_STAR_URL, object);
				JSONObject json = new JSONObject(result);
				String msg = json.getString("msg");
				Toast.makeText(PartTimePrideActivity.this, msg,
						Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case R.id.thankful_layout:
			if (!flag1) {
				parentContent.setVisibility(View.VISIBLE);
				lightupThankful.setVisibility(View.VISIBLE);
				icon2.setImageResource(R.drawable.icon_down);
				flag1 = true;
			} else {
				parentContent.setVisibility(View.GONE);
				lightupThankful.setVisibility(View.GONE);
				icon2.setImageResource(R.drawable.icon);
				flag1 = false;
			}
			break;

		case R.id.thankful_layout2:
			if (!flag2) {
				societyContent.setVisibility(View.VISIBLE);
				lightupThankful2.setVisibility(View.VISIBLE);
				icon3.setImageResource(R.drawable.icon_down);
				flag2 = true;
			} else {
				societyContent.setVisibility(View.GONE);
				lightupThankful2.setVisibility(View.GONE);
				icon3.setImageResource(R.drawable.icon);
				flag2 = false;
			}
			break;

		case R.id.light_up_thankful_btn:
			Intent thankfulIntent = new Intent(PartTimePrideActivity.this,
					ThankfulActivity.class);
			thankfulIntent.putExtra("mode", 1);
			startActivity(thankfulIntent);
			break;

		case R.id.light_up_thankful_btn2:
			Intent thankfulIntent2 = new Intent(PartTimePrideActivity.this,
					ThankfulActivity.class);
			thankfulIntent2.putExtra("mode", 2);
			startActivity(thankfulIntent2);
			break;
		case R.id.check_integrity_list:
			Intent checkIntent = new Intent(PartTimePrideActivity.this, HtmlIntegrityActivity.class);
			
			startActivity(checkIntent);
			break;
		}
		
		
	}

}
