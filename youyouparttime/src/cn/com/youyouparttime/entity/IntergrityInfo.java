package cn.com.youyouparttime.entity;

public class IntergrityInfo {

	private String cId;// ��˾id
	private String jobId;// ����id
	private String intergrityName;// ��ְ����
	private String intergrityGood;// ������
	private String intergrityNormal;// ������
	private String intergrityBad;// ������
	private String intergrityMember;// ��Ա����
	private String intergrityComment;// ��������
	private int integritySum;// �������ܺ�

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
