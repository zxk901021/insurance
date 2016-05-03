package com.example.market.bean;

import java.util.List;

public class CatId {

	private String id;

	private String name;

	private String url;

	private List<CatId> list;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public void setCat_id(List<CatId> cat_id) {
		this.list = cat_id;
	}

	public List<CatId> getCat_id() {
		return this.list;
	}

}
