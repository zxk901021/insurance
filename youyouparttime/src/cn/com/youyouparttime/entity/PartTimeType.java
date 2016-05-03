package cn.com.youyouparttime.entity;

public class PartTimeType implements Comparable<PartTimeType> {

	private String id;
	private String typeName;
	private boolean isChoosed;
	private int sort;

	public PartTimeType() {
		super();
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public boolean isChoosed() {
		return isChoosed;
	}

	public void setChoosed(boolean isChoosed) {
		this.isChoosed = isChoosed;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	@Override
	public int compareTo(PartTimeType another) {
		if (sort > another.getSort()) {
			return 1;
		} else if (sort == another.getSort()) {
			return 0;
		} else {
			return -1;
		}
	}

}
