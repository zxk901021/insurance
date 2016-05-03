package cn.com.youyouparttime.entity;

public class IntergrityInfo {

	private String cId;// 公司id
	private String jobId;// 工作id
	private String intergrityName;// 兼职标题
	private String intergrityGood;// 好评数
	private String intergrityNormal;// 中评数
	private String intergrityBad;// 差评数
	private String intergrityMember;// 会员名称
	private String intergrityComment;// 评价内容
	private int integritySum;// 评价数总和

	public IntergrityInfo() {
		super();
	}

	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getIntergrityName() {
		return intergrityName;
	}

	public void setIntergrityName(String intergrityName) {
		this.intergrityName = intergrityName;
	}

	public String getIntergrityGood() {
		return intergrityGood;
	}

	public void setIntergrityGood(String intergrityGood) {
		this.intergrityGood = intergrityGood;
	}

	public String getIntergrityNormal() {
		return intergrityNormal;
	}

	public void setIntergrityNormal(String intergrityNormal) {
		this.intergrityNormal = intergrityNormal;
	}

	public String getIntergrityBad() {
		return intergrityBad;
	}

	public void setIntergrityBad(String intergrityBad) {
		this.intergrityBad = intergrityBad;
	}

	public String getIntergrityMember() {
		return intergrityMember;
	}

	public void setIntergrityMember(String intergrityMember) {
		this.intergrityMember = intergrityMember;
	}

	public String getIntergrityComment() {
		return intergrityComment;
	}

	public void setIntergrityComment(String intergrityComment) {
		this.intergrityComment = intergrityComment;
	}

	public int getIntegritySum() {
		return integritySum;
	}

	public void setIntegritySum(int integritySum) {
		this.integritySum = integritySum;
	}

}
