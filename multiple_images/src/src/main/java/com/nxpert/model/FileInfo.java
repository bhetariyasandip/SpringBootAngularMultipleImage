package com.nxpert.model;

import lombok.Builder;

@Builder
public class FileInfo {
	private Long id;
	private String name;
	private String url;
	private String smallName;
	private String smallUrl;

	public FileInfo(Long id,String name, String url, String smallName, String smallUrl) {
		this.id=id;
		this.name = name;
		this.url = url;
		this.smallName = smallName;
		this.smallUrl = smallUrl;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSmallName() {
		return smallName;
	}

	public void setSmallName(String smallName) {
		this.smallName = smallName;
	}

	public String getSmallUrl() {
		return smallUrl;
	}

	public void setSmallUrl(String smallUrl) {
		this.smallUrl = smallUrl;
	}
	
	
	
}