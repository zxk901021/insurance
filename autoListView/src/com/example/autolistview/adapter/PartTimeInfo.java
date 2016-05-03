package com.example.autolistview.adapter;

import java.io.Serializable;

public class PartTimeInfo implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;//����id
	private String jobid;//
	private String cid;//
	private String companyName;
	private String jobName;//��ְ������
	private String jobAddress;//�ص�
	private String jobReleaseTime;//����ʱ��
	private String jobStatus;//��Ƹ״̬
	private String jobType;//��ְ����
	private String jobCompany;//������
	private String jobCount;//����
	private String jobReward;//����
	private String jobContent;//��������
	private String jobWorkTime;//����ʱ��
	private String jobRequest;//��λҪ��
	private String jobDetail;//��������
	private boolean isVIP;//�Ƿ��V
	private boolean isHot;//�Ƿ���Hot��ʶ
	private String peopleCount;//��������
	private String jobArea;//��������
	private String companyIntroduce;//��ҵ���
	private String companyPersonInCharge;//��ҵ������
	private String companyPhone;//��ҵ��ϵ�绰
	private String companyAdress;//��ҵ��ַ
	private String contactPerson;//��ϵ��
	private String contactPhone;//��ϵ�绰
	private String contactEmail;//��ϵ����
	private boolean isCollect;
	private String commentCount;
	private String commentGoodCount;
	private String commentNormalCount;
	private String commentBadCount;
	private String user1;
	private String user1Comment;
	private String user2;
	private String user2Comment;
	private String user3;
	private String user3Comment;
	private boolean flag;
	private String favId;
	private String tips;//��ܰ��ʾ
	private String jobAreaDetail;
	
	
	public String getJobAreaDetail() {
		return jobAreaDetail;
	}
	public void setJobAreaDetail(String jobAreaDetail) {
		this.jobAreaDetail = jobAreaDetail;
	}
	public PartTimeInfo() {
		super();
	}
	public PartTimeInfo(String jobName, String jobAddress,
			String jobReleaseTime, String jobType, String jobReward,
			boolean isVIP, boolean isHot, String peopleCount) {
		super();
		this.jobName = jobName;
		this.jobAddress = jobAddress;
		this.jobReleaseTime = jobReleaseTime;
		this.jobType = jobType;
		this.jobReward = jobReward;
		this.isVIP = isVIP;
		this.isHot = isHot;
		this.peopleCount = peopleCount;
	}
	
	public String getJobid() {
		return jobid;
	}
	public void setJobid(String jobid) {
		this.jobid = jobid;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getJobArea() {
		return jobArea;
	}
	public void setJobArea(String jobArea) {
		this.jobArea = jobArea;
	}
	public String getCompanyIntroduce() {
		return companyIntroduce;
	}
	public void setCompanyIntroduce(String companyIntroduce) {
		this.companyIntroduce = companyIntroduce;
	}
	public String getCompanyPersonInCharge() {
		return companyPersonInCharge;
	}
	public void setCompanyPersonInCharge(String companyPersonInCharge) {
		this.companyPersonInCharge = companyPersonInCharge;
	}
	public String getCompanyPhone() {
		return companyPhone;
	}
	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}
	public String getCompanyAdress() {
		return companyAdress;
	}
	public void setCompanyAdress(String companyAdress) {
		this.companyAdress = companyAdress;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobAddress() {
		return jobAddress;
	}
	public void setJobAddress(String jobAddress) {
		this.jobAddress = jobAddress;
	}
	public String getJobReleaseTime() {
		return jobReleaseTime;
	}
	public void setJobReleaseTime(String jobReleaseTime) {
		this.jobReleaseTime = jobReleaseTime;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public String getJobCompany() {
		return jobCompany;
	}
	public void setJobCompany(String jobCompany) {
		this.jobCompany = jobCompany;
	}
	public String getJobCount() {
		return jobCount;
	}
	public void setJobCount(String jobCount) {
		this.jobCount = jobCount;
	}
	public String getJobReward() {
		return jobReward;
	}
	public void setJobReward(String jobReward) {
		this.jobReward = jobReward;
	}
	public String getJobContent() {
		return jobContent;
	}
	public void setJobContent(String jobContent) {
		this.jobContent = jobContent;
	}
	public String getJobWorkTime() {
		return jobWorkTime;
	}
	public void setJobWorkTime(String jobWorkTime) {
		this.jobWorkTime = jobWorkTime;
	}
	public String getJobRequest() {
		return jobRequest;
	}
	public void setJobRequest(String jobRequest) {
		this.jobRequest = jobRequest;
	}
	public String getJobDetail() {
		return jobDetail;
	}
	public void setJobDetail(String jobDetail) {
		this.jobDetail = jobDetail;
	}
	public boolean isVIP() {
		return isVIP;
	}
	public void setVIP(boolean isVIP) {
		this.isVIP = isVIP;
	}
	public boolean isHot() {
		return isHot;
	}
	public void setHot(boolean isHot) {
		this.isHot = isHot;
	}
	public String getPeopleCount() {
		return peopleCount;
	}
	public void setPeopleCount(String peopleCount) {
		this.peopleCount = peopleCount;
	}
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	
	public String getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}
	public String getCommentGoodCount() {
		return commentGoodCount;
	}
	public void setCommentGoodCount(String commentGoodCount) {
		this.commentGoodCount = commentGoodCount;
	}
	public String getCommentNormalCount() {
		return commentNormalCount;
	}
	public void setCommentNormalCount(String commentNormalCount) {
		this.commentNormalCount = commentNormalCount;
	}
	public String getCommentBadCount() {
		return commentBadCount;
	}
	public void setCommentBadCount(String commentBadCount) {
		this.commentBadCount = commentBadCount;
	}
	public String getUser1() {
		return user1;
	}
	public void setUser1(String user1) {
		this.user1 = user1;
	}
	public String getUser1Comment() {
		return user1Comment;
	}
	public void setUser1Comment(String user1Comment) {
		this.user1Comment = user1Comment;
	}
	public String getUser2() {
		return user2;
	}
	public void setUser2(String user2) {
		this.user2 = user2;
	}
	public String getUser2Comment() {
		return user2Comment;
	}
	public void setUser2Comment(String user2Comment) {
		this.user2Comment = user2Comment;
	}
	public String getUser3() {
		return user3;
	}
	public void setUser3(String user3) {
		this.user3 = user3;
	}
	public String getUser3Comment() {
		return user3Comment;
	}
	public void setUser3Comment(String user3Comment) {
		this.user3Comment = user3Comment;
	}
	public boolean isCollect() {
		return isCollect;
	}
	public void setCollect(boolean isCollect) {
		this.isCollect = isCollect;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getFavId() {
		return favId;
	}
	public void setFavId(String favId) {
		this.favId = favId;
	}
	public String getTips() {
		return tips;
	}
	public void setTips(String tips) {
		this.tips = tips;
	}
	
	
}
