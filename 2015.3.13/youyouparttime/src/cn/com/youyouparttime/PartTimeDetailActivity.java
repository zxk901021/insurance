package cn.com.youyouparttime;

import org.json.JSONObject;

import cn.com.youyouparttime.base.SysApplication;
import cn.com.youyouparttime.entity.PartTimeInfo;
import cn.com.youyouparttime.entity.UrlUtil;
import cn.com.youyouparttime.util.CommonUtil;
import cn.com.youyouparttime.util.DialogUtil;
import cn.com.youyouparttime.util.HttpUtil;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PartTimeDetailActivity extends Activity implements OnClickListener {

	private String id;
	private TextView jobName, releaseTime, focusCount, jobStatus;
	private TextView jobType, companyName, jobNumber, jobPay, jobTime, jobArea, jobAreaDetail;
	private TextView jobContent;
	private TextView companyIntroduce, companyPersonInCharge, companyPhone,
			companyAddress, personInCharge, phone, address;
	private TextView contactPerson, contactPhone;
	private TextView commentGood, commentNormal, commentBad;
	private TextView commentUser1, commentUser2, commentUser3;
	private TextView commentUser1Contet, commentUser2Contet,
			commentUser3Contet;
	private TextView bottomText1, bottomText2, bottomText3;
	private TextView tips;
	private TextView resetEdit;
	private LinearLayout back;
	private Button seeMore, integrityRecord, publishComment, checkResume;
	private LinearLayout shareBtn, complainBtn, contactBtn;
	private ImageView collectJob, bottomImg1, bottomImg2, bottomImg3;
	PartTimeInfo info = new PartTimeInfo();
	private boolean isHide = true;
	SharedPreferences preferences;
	public static int COMMENT_TYPE_CODE = 0;
	public static int COMPLAIN_TYPE_CODE = 1;
	private String uid, usertype, linkPhone;
	private boolean isCompany;
	private boolean flag;
	private boolean collectFlag;
	private RelativeLayout commentLayout;
	private LinearLayout bottomLayout;
	private ImageView jobTimeImge;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_part_time_detail);
		SysApplication.getInstance().addActivity(this);
		preferences = getSharedPreferences("myPrefer", 0);
		uid = preferences.getString("uid", "-1");
		usertype = preferences.getString("usertype", "-1");
		Intent intent = getIntent();
		flag = intent.getBooleanExtra("flag", false);
		isCompany = intent.getBooleanExtra("iscompany", false);
		if (isCompany) {
			uid = intent.getStringExtra("uid");
		}
		id = intent.getStringExtra("id");
		// Log.e("detailId", id);
		if (flag) {
			info = (PartTimeInfo) intent.getSerializableExtra("info");
		} else {
			info = CommonUtil.getDetailInfo(id, UrlUtil.JOB_DETAIL_URL);
		}

		linkPhone = info.getContactPhone();
		initView();
		
		setValue();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (!flag) {
			info = CommonUtil.getDetailInfo(id, UrlUtil.JOB_DETAIL_URL);
			commentGood.setText("好("
					+ setCommentCount(info.getCommentGoodCount()) + ")");
			commentNormal.setText("一般("
					+ setCommentCount(info.getCommentNormalCount()) + ")");
			commentBad.setText("差("
					+ setCommentCount(info.getCommentBadCount()) + ")");
			commentUser1.setText(info.getUser1());
			commentUser2.setText(info.getUser2());
			commentUser3.setText(info.getUser3());
			commentUser1Contet.setText(info.getUser1Comment());
			commentUser2Contet.setText(info.getUser2Comment());
			commentUser3Contet.setText(info.getUser3Comment());
		}
	}

	public void initView() {
		jobAreaDetail = (TextView) findViewById(R.id.detail_part_area_content_detail);
		resetEdit = (TextView) findViewById(R.id.detail_reset_edit);
		tips = (TextView) findViewById(R.id.detail_tips);
		jobStatus = (TextView) findViewById(R.id.job_status);
		jobName = (TextView) findViewById(R.id.job_name);
		releaseTime = (TextView) findViewById(R.id.detail_time);
		focusCount = (TextView) findViewById(R.id.detail_focus);
		jobType = (TextView) findViewById(R.id.detail_part_type_content);
		companyName = (TextView) findViewById(R.id.detail_part_company_content);
		jobNumber = (TextView) findViewById(R.id.detail_part_people_count_content);
		jobPay = (TextView) findViewById(R.id.detail_part_pay_content);
		jobTime = (TextView) findViewById(R.id.detail_part_time_content);
		jobArea = (TextView) findViewById(R.id.detail_part_area_content);
		jobContent = (TextView) findViewById(R.id.detail_job_content_content);
		companyIntroduce = (TextView) findViewById(R.id.company_intro_content);
		companyPersonInCharge = (TextView) findViewById(R.id.company_person_charge_content);
		companyPhone = (TextView) findViewById(R.id.company_person_phone_content);
		companyAddress = (TextView) findViewById(R.id.company_person_adress_content);
		personInCharge = (TextView) findViewById(R.id.company_person_charge);
		phone = (TextView) findViewById(R.id.company_person_phone);
		address = (TextView) findViewById(R.id.company_person_adress);
		contactPerson = (TextView) findViewById(R.id.contact_person_content);
		contactPhone = (TextView) findViewById(R.id.contact_phone_content);
		seeMore = (Button) findViewById(R.id.see_more);
		integrityRecord = (Button) findViewById(R.id.integrity_record);
		back = (LinearLayout) findViewById(R.id.part_time_detail_back);
		publishComment = (Button) findViewById(R.id.publish_comment);
		shareBtn = (LinearLayout) findViewById(R.id.detail_share_btn);
		complainBtn = (LinearLayout) findViewById(R.id.detail_complain_btn);
		contactBtn = (LinearLayout) findViewById(R.id.detail_contact_btn);
		collectJob = (ImageView) findViewById(R.id.collect_job);
		commentGood = (TextView) findViewById(R.id.detail_content_good);
		commentNormal = (TextView) findViewById(R.id.detail_content_normal);
		commentBad = (TextView) findViewById(R.id.detail_content_bad);
		commentUser1 = (TextView) findViewById(R.id.detail_content_user1);
		commentUser2 = (TextView) findViewById(R.id.detail_content_user2);
		commentUser3 = (TextView) findViewById(R.id.detail_content_user3);
		commentUser1Contet = (TextView) findViewById(R.id.detail_content_user1_content);
		commentUser2Contet = (TextView) findViewById(R.id.detail_content_user2_content);
		commentUser3Contet = (TextView) findViewById(R.id.detail_content_user3_content);
		bottomText1 = (TextView) findViewById(R.id.detail_text1);
		bottomText2 = (TextView) findViewById(R.id.detail_text2);
		bottomText3 = (TextView) findViewById(R.id.detail_text3);
		bottomImg1 = (ImageView) findViewById(R.id.detail_img1);
		bottomImg2 = (ImageView) findViewById(R.id.detail_img2);
		bottomImg3 = (ImageView) findViewById(R.id.detail_img3);
		jobTimeImge = (ImageView) findViewById(R.id.detail_part_time_image);
		// adressIcon = (ImageView) findViewById(R.id.company_adreess_icon);
		checkResume = (Button) findViewById(R.id.check_resumes);
		commentLayout = (RelativeLayout) findViewById(R.id.detail_comment_layout);
		bottomLayout = (LinearLayout) findViewById(R.id.detail_bottom_menu);
		if (isCompany) {
			checkResume.setVisibility(View.VISIBLE);
			collectJob.setVisibility(View.GONE);
			bottomImg1.setImageResource(R.drawable.shangjia);
			bottomImg2.setImageResource(R.drawable.full);
			bottomImg3.setImageResource(R.drawable.xiajia);
			bottomText1.setText("重新上架");
			bottomText2.setText("已招满");
			bottomText3.setText("下架");
			resetEdit.setVisibility(View.VISIBLE);
			collectJob.setVisibility(View.GONE);
		}
		if (flag) {
			integrityRecord.setVisibility(View.INVISIBLE);
			collectJob.setVisibility(View.GONE);
			commentLayout.setVisibility(View.GONE);
			bottomLayout.setVisibility(View.GONE);
		}
		resetEdit.setOnClickListener(this);
		companyAddress.setOnClickListener(this);
		checkResume.setOnClickListener(this);
		collectJob.setOnClickListener(this);
		shareBtn.setOnClickListener(this);
		complainBtn.setOnClickListener(this);
		contactBtn.setOnClickListener(this);
		seeMore.setOnClickListener(this);
		back.setOnClickListener(this);
		integrityRecord.setOnClickListener(this);
		publishComment.setOnClickListener(this);
		jobTimeImge.setOnClickListener(this);
	}

	public void setValue() {
		jobStatus.setText(info.getJobStatus());
		jobName.setText(info.getJobName());
		releaseTime.setText(info.getJobReleaseTime());
		focusCount.setText(info.getPeopleCount());
		jobType.setText(info.getJobType());
		jobNumber.setText(info.getJobCount());
		jobPay.setText(info.getJobReward());
		jobArea.setText(info.getJobArea());
		tips.setText(Html.fromHtml(CommonUtil.nullToEmpty(info.getTips())));
		companyName.setText(info.getCompanyName());
		if (flag) {
			jobTime.setText(info.getJobWorkTime());
		} else {
			jobTime.setText(CommonUtil.strToInt(info.getJobWorkTime()));
		}
		if (info.isCollect()) {
			collectJob.setImageResource(R.drawable.collect_star);
			collectFlag = true;
		} else {
			collectJob.setImageResource(R.drawable.no_collect);
			collectFlag = false;
		}
		if (TextUtils.isEmpty(info.getJobContent())) {
			jobContent.setText("空");
		}else {
			jobContent.setText(Html.fromHtml(CommonUtil.nullToEmpty(info
					.getJobContent())));
		}
		
		companyIntroduce.setText(info.getCompanyIntroduce());
		contactPerson.setText(info.getContactPerson());
		contactPhone.setText(info.getContactPhone());
		companyPersonInCharge.setText(info.getCompanyPersonInCharge());
		companyPhone.setText(info.getCompanyPhone());
		companyAddress.setText(info.getCompanyAdress());
		commentGood.setText("好(" + setCommentCount(info.getCommentGoodCount())
				+ ")");
		commentNormal.setText("一般("
				+ setCommentCount(info.getCommentNormalCount()) + ")");
		commentBad.setText("差(" + setCommentCount(info.getCommentBadCount())
				+ ")");
		commentUser1.setText(info.getUser1());
		commentUser2.setText(info.getUser2());
		commentUser3.setText(info.getUser3());
		commentUser1Contet.setText(info.getUser1Comment());
		commentUser2Contet.setText(info.getUser2Comment());
		commentUser3Contet.setText(info.getUser3Comment());
		jobAreaDetail.setText(CommonUtil.nullToEmpty(info.getJobAreaDetail()));

	}

	public String setCommentCount(String count) {
		String counts = null;
		if (count == null || count.length() == 0) {
			counts = "0";
		} else {
			counts = count;
		}
		return counts;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.detail_reset_edit:
			Intent resetIntent = new Intent(PartTimeDetailActivity.this, ReleaseNewJobActivity.class);
			resetIntent.putExtra("id", id);
			startActivity(resetIntent);
			break;	

		case R.id.check_resumes:
			Intent checkIntent = new Intent(PartTimeDetailActivity.this,
					ResumeListActivity.class);
			checkIntent.putExtra("jobid", id);
			checkIntent.putExtra("uid", uid);
			checkIntent.putExtra("isResume", true);
			startActivity(checkIntent);
			break;

		case R.id.company_person_adress_content:
			Intent mapIntent = new Intent(PartTimeDetailActivity.this,
					MapActivity.class);
			mapIntent.putExtra("address", info.getCompanyAdress());
			startActivity(mapIntent);
			break;

		case R.id.see_more:
			if (isHide) {
				companyPersonInCharge.setVisibility(View.VISIBLE);
				companyPhone.setVisibility(View.VISIBLE);
				companyAddress.setVisibility(View.VISIBLE);
				personInCharge.setVisibility(View.VISIBLE);
				phone.setVisibility(View.VISIBLE);
				address.setVisibility(View.VISIBLE);
				// adressIcon.setVisibility(View.VISIBLE);
				seeMore.setText("收起隐藏");
				isHide = false;
			} else {
				companyPersonInCharge.setVisibility(View.GONE);
				companyPhone.setVisibility(View.GONE);
				companyAddress.setVisibility(View.GONE);
				personInCharge.setVisibility(View.GONE);
				phone.setVisibility(View.GONE);
				address.setVisibility(View.GONE);
				// adressIcon.setVisibility(View.GONE);
				seeMore.setText("查看更多");
				isHide = true;
			}
			break;

		case R.id.part_time_detail_back:
			finish();
			break;

		case R.id.integrity_record:
			String cid = info.getCid();
			Intent checkIntegrity = new Intent(PartTimeDetailActivity.this,
					IntegrityActivity.class);
			checkIntegrity.putExtra("cid", cid);
			startActivity(checkIntegrity);
			break;

		case R.id.publish_comment:
			gotoComment(COMMENT_TYPE_CODE);
			break;

		case R.id.detail_share_btn:
			if (isCompany) {
				bottonMenu(UrlUtil.PUT_AWAY_URL);
				return;
			}
			loginCheck();
			showShare(id);
			break;
			
		case R.id.detail_part_time_image:
			Intent timeIntent = new Intent(PartTimeDetailActivity.this, DetailParttimeActivity.class);
			timeIntent.putExtra("time", info.getJobWorkTime());
			startActivity(timeIntent);
			break;

		case R.id.detail_complain_btn:
			if (isCompany) {
				bottonMenu(UrlUtil.FULL_URL);
				return;
			}
			loginCheck();
			DialogUtil.complainDialog(PartTimeDetailActivity.this,
					COMPLAIN_TYPE_CODE, info, uid, usertype);
			break;

		case R.id.detail_contact_btn:
			if (isCompany) {
				bottonMenu(UrlUtil.SOLD_OUT_URL);
				return;
			}
			loginCheck();
			boolean isSubmit = preferences.getBoolean("hasSubmit", false);
			if (!isSubmit) {
				Toast.makeText(PartTimeDetailActivity.this,
						"请先完善您的个人信息，去填简历吧！", Toast.LENGTH_SHORT).show();
				return;
			}
			// DialogUtil.contactDialog(this, info.getJobid(), info.getCid(),
			// uid, linkPhone);
			String name = preferences.getString("personname", "");
			String school = preferences.getString("school", "");
			String sex = preferences.getString("sex", "");
			String title = info.getJobName();
			DialogUtil.contactDialog(this, info.getJobid(), info.getCid(), uid,
					linkPhone, name, school, sex, title);
			break;

		case R.id.collect_job:
			if (collectFlag) {
				deleteJob();
				collectJob.setImageResource(R.drawable.no_collect);
				collectFlag = false;
			} else {
				collectJob();
				collectJob.setImageResource(R.drawable.collect_star);
				collectFlag = true;
			}

			break;
		}
	}

	public void bottonMenu(String url) {
		JSONObject object = new JSONObject();
		String msg = "";
		try {
			object.put("jobid", id);
			Log.e("jobid", id);
			String result = HttpUtil.postRequst(url, object);
			msg = new JSONObject(result).getString("msg");
			Toast.makeText(PartTimeDetailActivity.this, msg, Toast.LENGTH_SHORT)
					.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loginCheck() {
		int loginCode = CommonUtil.isLogin(PartTimeDetailActivity.this);
		if (loginCode == 0) {
			Toast.makeText(PartTimeDetailActivity.this, "请先登录",
					Toast.LENGTH_SHORT).show();
			return;
		}
	}

	public void gotoComment(int typeCode) {
		int loginCode = CommonUtil.isLogin(PartTimeDetailActivity.this);
		if (loginCode == 0) {
			Toast.makeText(PartTimeDetailActivity.this, "请先登录",
					Toast.LENGTH_SHORT).show();
			return;
		}
		boolean isSubmit = preferences.getBoolean("hasSubmit", false);
		if (!isSubmit) {
			Toast.makeText(PartTimeDetailActivity.this, "请先完善个人信息，然后再进行评价",
					Toast.LENGTH_SHORT).show();
			return;
		}
		String jobId = info.getJobid();
		String cId = info.getCid();

		Intent intent = new Intent(PartTimeDetailActivity.this,
				CommentActivity.class);
		intent.putExtra("jobid", jobId);
		intent.putExtra("cid", cId);
		intent.putExtra("uid", uid);
		Log.e("weqeqeq", cId + " 3123" + jobId);
		intent.putExtra("usertype", usertype);
		intent.putExtra("type", typeCode);// 跳转页面类型，0为评论页，1为投诉页；
		startActivity(intent);
	}

	public void collectJob() {
		String jobId = info.getJobid();
		String uid = preferences.getString("uid", null);
		JSONObject object = new JSONObject();
		try {
			object.put("uid", uid);
			object.put("jobid", jobId);
			String result = HttpUtil
					.postRequst(UrlUtil.COLLECT_JOB_URL, object);
			JSONObject jsonObject = new JSONObject(result);
			String msg = jsonObject.getString("msg");
			Toast.makeText(PartTimeDetailActivity.this, msg, Toast.LENGTH_SHORT)
					.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void deleteJob() {
		String jobId = info.getJobid();
		String uid = preferences.getString("uid", null);
		JSONObject object = new JSONObject();
		try {
			object.put("uid", uid);
			object.put("jobid", jobId);
			object.put("drop", "1");
			String result = HttpUtil
					.postRequst(UrlUtil.COLLECT_JOB_URL, object);
			JSONObject jsonObject = new JSONObject(result);
			String msg = jsonObject.getString("msg");
			Toast.makeText(PartTimeDetailActivity.this, msg, Toast.LENGTH_SHORT)
					.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showShare(String uid) {
		ShareSDK.initSDK(this, "4e36dbd819a2");
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字
		oks.setNotification(R.drawable.p8, getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(info.getJobName());
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(UrlUtil.SHARE_URL + uid);
		// text是分享文本，所有平台都需要这个字段
		oks.setText("找兼职就用悠兼职  ");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImageUrl("http://yy.wx022.com/up/applogo.png");
//		oks.setImagePath("/sdcard/applogo.png");// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(UrlUtil.SHARE_URL + uid);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("这个兼职真不错！大家来看看吧");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);
	}
}
