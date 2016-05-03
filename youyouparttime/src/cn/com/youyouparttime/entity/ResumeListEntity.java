package cn.com.youyouparttime.entity;

public class ResumeListEntity {

	private String uid;
	private String name;
	private String jobType;
	private String school;
	private String foucusCounts;
	private boolean isSelected;
	private String photoPath;
	private String jobid;
	private String cid;
	
	public ResumeListEntity() {
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getJobType() {
		return jobType;
	}


	public void setJobType(String jobType) {
		this.jobType = jobType;
	}


	public String getSchool() {
		return school;
	}


	public void setSchool(String school) {
		this.school = school;
	}


	public String getFoucusCounts() {
		return foucusCounts;
	}


	public void setFoucusCounts(String foucusCounts) {
		this.foucusCounts = foucusCounts;
	}




	public boolean isSelected() {
		return isSelected;
	}


	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}


	public String getPhotoPath() {
		return photoPath;
	}


	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}


	public String getUid() {
		return uid;
	}


	public void setUid(String uid) {
		this.uid = uid;
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

	
	
}
