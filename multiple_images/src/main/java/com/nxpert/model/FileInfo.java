package com.nxpert.model;

import lombok.Builder;

@Builder
public class FileInfo {
	private Long id;
	private String name;
	private String url;

	public FileInfo(Long id,String name, String url) {
		this.id=id;
		this.name = name;
		this.url = url;
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
	
	
}