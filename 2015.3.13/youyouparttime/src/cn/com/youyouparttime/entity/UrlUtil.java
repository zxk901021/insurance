package cn.com.youyouparttime.entity;

public class UrlUtil {
	public static String SITEURL = "http://uu.wx022.com/";
//	public static String SITEURL = "http://192.168.1.250/phpyun/";
	public static String LOGINURL = SITEURL+"home.php?m=login&c=login";
	public static String REGISTERURL = SITEURL +"home.php?m=register";
	public static String ADURL = SITEURL + "wap/index.php?m=ajax&c=getad";
	public static String JOB_TYPE_URL = SITEURL +"wap/index.php?m=ajax&c=jsonjobclass";
	public static String JOB_PROVINCE_URL = SITEURL + "wap/index.php?m=ajax&c=jsonprovince";
	public static String JOB_CITY_URL = SITEURL + "wap/index.php?m=ajax&c=jsoncity";
	public static String JOB_TIME_URL = SITEURL + "wap/index.php?m=ajax&c=jsonjobtime";
	public static String JOB_PAY_URL = SITEURL + "wap/index.php?m=ajax&c=jsonjobjs";
	public static String JOB_NEWEAST_URL = SITEURL + "wap/index.php?m=com&c=json_newestjob";
	public static String JOB_RECOMMEND_URL = SITEURL + "wap/index.php?m=com&c=json_recomjob";
	public static String JOB_SEARCH_URL = SITEURL + "wap/index.php?m=com&c=json_searchjob";
	public static String JOB_DETAIL_URL = SITEURL + "wap/index.php?m=com&c=json_view";
	public static String FIND_PASSWORD_URL = SITEURL + "home.php?m=login&c=findPW2";
	public static String KEY_WORDS_SEARCH_URL = SITEURL + "wap/index.php?m=com&c=json_searchbycol";
	public static String SUBMIT_COMMENT_URL = SITEURL + "wap/index.php?m=ajax&c=json_comment";
	public static String SUBMIT_COMPLAIN_URL = SITEURL + "wap/index.php?m=ajax&c=json_complain";
	public static String INTEGRITY_RECORD_URL = SITEURL + "wap/index.php?m=firm&c=com_integrity";
	public static String INTEGRITY_RECORD_DETAIL_URL = SITEURL + "wap/index.php?m=firm&c=com_integrity_job";
	public static String MY_COLLECT_URL = SITEURL + "wap/member/home.php?m=index&c=json_collect";
	public static String MY_APPLY_URL = SITEURL + "wap/member/home.php?m=index&c=json_sq";
	public static String SUBMIT_RESUME_URL = SITEURL + "wap/member/home.php?m=index&c=json_info";
	public static String CHANGE_PASSWORD_URL = SITEURL + "wap/member/home.php?m=index&c=xiugai_pw";
	public static String SUGGESSTION_URL = SITEURL + "wap/member/home.php?m=index&c=suggest";
	public static String INTERVIEW_MSG_STUDENT_URL = SITEURL + "wap/member/home.php?m=index&c=recruit_msg_list";
	public static String COMMENT_MSG_URL = SITEURL + "wap/member/home.php?m=index&c=comm_msg_list";
	public static String HTML_RESUME_URL = SITEURL + "wap/member/home.php?m=index&c=profile";
	public static String COLLECT_JOB_URL = SITEURL + "wap/index.php?m=com&c=jsonfav";
	public static String GET_ZILI_LIST_URL = SITEURL + "wap/member/home.php?m=index&c=job_employ";
	public static String GET_ZILI_STAR_URL = SITEURL + "wap/member/home.php?m=index&c=askstar_zl";
	public static String GET_INTEGERITY_STAR_URL = SITEURL + "wap/member/home.php?m=index&c=star_chengxin";
	public static String APPLY_RECORD_URL = SITEURL + "wap/index.php?m=ajax&c=json_sq_job";
	public static String THANKFUL_PARENTS_URL = SITEURL + "wap/member/home.php?m=index&c=askstar_ganen";
	public static String THANKFUL_SOCIETY_URL = SITEURL + "wap/member/home.php?m=index&c=askstar_ganen";
	public static String COMPANY_COMMENT_URL = SITEURL + "wap/member/home.php?m=index&c=comm_msg_list";
	public static String COMPANY_COMMENT_DETAIL_URL = SITEURL + "wap/member/home.php?m=index&c=comm_msg_show";
	public static String SYSTEM_COMMENT_URL = SITEURL + "wap/member/home.php?m=index&c=sys_msg_list";
	public static String INTERVIEW_MSG_URL = SITEURL + "wap/member/home.php?m=index&c=recruit_msg_list";
	public static String COMPANY_INFO_SUBMIT_URL = SITEURL + "wap/index.php?m=firm&c=json_info";
	public static String ADMIN_JOB_URL = SITEURL + "wap/index.php?m=firm&c=admin_resume";
	public static String COMPANY_TO_STUDENT_URL = SITEURL + "wap/index.php?m=firm&c=comment_stu";
	public static String SALARY_URL = SITEURL + "wap/index.php?m=firm&c=get_jobsalary";
	public static String SUBMIT_JOB_URL = SITEURL + "/wap/index.php?m=firm&c=json_jobadd";
	public static String SEARCH_JOB_URL = SITEURL + "wap/index.php?m=firm&c=search_resume";
	public static String COMPANY_MSG_URL = SITEURL + "wap/index.php?m=firm&c=msg_list";
	public static String COMPANY_AUTHENTICATION_URL = SITEURL + "wap/index.php?m=firm&c=concert";
	public static String COMPANY_INDUSTRY_URL = SITEURL + "wap/index.php?m=ajax&c=get_industry";
	public static String ORGANISATION_URL = SITEURL + "wap/index.php?m=ajax&c=get_pr";
	public static String SCALE_URL = SITEURL + "wap/index.php?m=ajax&c=get_mun";
	public static String MSG_ALL_STUDENT_URL  = SITEURL + "wap/member/home.php?m=index&c=msg_list";
	public static String GET_STUDENT_URL = SITEURL + "wap/index.php?m=firm&c=get_school";
	public static String GET_SPECIALTY_URL = SITEURL + "wap/index.php?m=firm&c=get_specialty";
	public static String PUT_AWAY_URL = SITEURL + "wap/index.php?m=firm&c=job_toshow";
	public static String FULL_URL = SITEURL + "wap/index.php?m=firm&c=recruit_out";
	public static String SOLD_OUT_URL = SITEURL + "wap/index.php?m=firm&c=job_tonoshow";
	public static String CHECK_RESUME_URL = SITEURL + "wap/index.php?m=firm&c=resume_list";	
	public static String COMPANY_INVIITE_URL = SITEURL + "wap/index.php?m=firm&c=send_recruit";
	public static String YOU_DETAIL_URL = SITEURL + "wap/index.php?m=ajax&c=get_news";
	public static String DETECTION_VERSION_URL = SITEURL + "wap/index.php?m=ajax&c=version";
	public static String VERIFY_URL = SITEURL + "home.php?m=register&c=sms";
	public static String COMPANY_DETAIL_URL = SITEURL + "wap/index.php?m=firm&c=json_show";
	public static String FIND_PASSWORD_STEP1_URL = SITEURL + "home.php?m=login&c=findPW1";
	public static String BROWSE_RESUME_URL  = SITEURL + "wap/member/home.php?m=index&c=json_getinfo";
	public static String UPLOAD_IMG_URL = SITEURL + "wap/index.php?m=ajax&c=upload&type=cert";
	public static String CHANGE_PHONE_URL = SITEURL + "wap/index.php?m=ajax&c=set_mobile";
	public static String SHARE_URL = SITEURL + "wap/index.php?m=com&c=view&id=";
	public static String RESERVE_INTERVIEW_URL = SITEURL + "wap/member/home.php?m=index&c=reserve_interview";
	public static String APPLY_INTERNSHIP_URL = SITEURL + "wap/member/home.php?m=index&c=sq_sxbg";
	public static String INTERVIEW_MSG_DETAIL_URL = SITEURL + "wap/member/home.php?m=index&c=recruit_msg_show";
	public static String SYSTEM_MSG_DETAIL_URL = SITEURL + "wap/member/home.php?m=index&c=sys_msg_show";
	public static String APPLY_JOB_URL = SITEURL + "wap/index.php?m=firm&c=msg_sq_show";
	public static String ZILI_STAR_URL = SITEURL + "wap/index.php?m=firm&c=msg_zl_show";
	public static String REWARD_IS_TRUE_URL = SITEURL + "wap/index.php?m=firm&c=confirm_reward";
	public static String DELETE_MSG_URL = SITEURL + "wap/index.php?m=firm&c=del_msg";
	public static String CHECK_CONTACT_URL = SITEURL + "wap/index.php?m=firm&c=msg_contact_show";
	public static String CHECK_INTERVIEW_URL = SITEURL + "wap/index.php?m=firm&c=msg_reserve_show";
	public static String CHECK_COMPANY_COMMENT_DETAIL_URL = SITEURL + "wap/index.php?m=firm&c=msg_comment_show";
	public static String STUDENT_INFO_URL = SITEURL + "wap/member/home.php?m=index&c=get_top";
	public static String COMPANY_INFO_URL = SITEURL + "wap/index.php?m=firm&c=get_top";
	public static String SET_IMG_PORTRAIT_URL = SITEURL + "wap/index.php?m=ajax&c=set_txpic";
	public static String DELETE_IMG_URL = SITEURL + "wap/index.php?m=ajax&c=del_pic";
	public static String INFOS_DETAIL = SITEURL + "wap/member/home.php?m=index&c=get_news&catid=";
	public static String STUDENT_CONTACT_INFO = SITEURL + "wap/member/home.php?m=index&c=contact_msg_show";
	public static String STUDENT_AGREE_CHECK = SITEURL + "wap/member/home.php?m=index&c=sq_contact_handle";
	public static String ADD_VIP_URL = SITEURL + "wap/member/home.php?m=index&c=add_v";
	public static String INTEGRITY_HTML_URL = SITEURL + "wap/member/home.php?m=index&c=sincerity_list";
}
