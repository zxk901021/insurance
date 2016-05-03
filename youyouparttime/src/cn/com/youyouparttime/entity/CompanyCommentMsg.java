package cn.com.youyouparttime.entity;

public class CompanyCommentMsg {

	private String msgId;
	private String name;
	private String time;
	private String isRead;
	private String commentDetailContent;
	private String commentDetailName;
	private String type;
	private String typeTwo;

	public CompanyCommentMsg() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getIsRead() {
		return isRead;
	}
	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getCommentDetailContent() {
		return commentDetailContent;
	}
	public void setCommentDetailContent(String commentDetailContent) {
		this.commentDetailContent = commentDetailContent;
	}
	public String getCommentDetailName() {
		return commentDetailName;
	}
	public void setCommentDetailName(String commentDetailName) {
		this.commentDetailName = commentDetailName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeTwo() {
		return typeTwo;
	}
	public void setTypeTwo(String typeTwo) {
		this.typeTwo = typeTwo;
	}
	
	
}
