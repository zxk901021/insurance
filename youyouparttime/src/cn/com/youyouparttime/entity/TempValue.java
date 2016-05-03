package cn.com.youyouparttime.entity;

public class TempValue implements Comparable<TempValue>{

	private String id;
	private String name;
	private int sort;

	public TempValue() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	@Override
	public int compareTo(TempValue another) {
		if (sort > another.getSort()) {
			return 1;
		}else if (sort == another.getSort()) {
			return 0;
		}else {
			return -1;
		}
	};

}
