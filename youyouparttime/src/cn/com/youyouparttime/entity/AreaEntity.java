package cn.com.youyouparttime.entity;

public class AreaEntity implements Comparable<AreaEntity>{
	
	private String areaId;
	private String area;
	private boolean isChoosed;
	private int sort;
	
	

	public AreaEntity() {
		super();
	}



	public String getAreaId() {
		return areaId;
	}



	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}



	public String getArea() {
		return area;
	}



	public void setArea(String area) {
		this.area = area;
	}



	public boolean isChoosed() {
		return isChoosed;
	}



	public void setChoosed(boolean isChoosed) {
		this.isChoosed = isChoosed;
	}



	public int getSort() {
		return sort;
	}



	public void setSort(int sort) {
		this.sort = sort;
	}



	@Override
	public int compareTo(AreaEntity another) {
		if (sort > another.getSort()) {
			return 1;
		}else if (sort == another.getSort()) {
			return 0;
		}else {
			return -1;
		}
	}



}
