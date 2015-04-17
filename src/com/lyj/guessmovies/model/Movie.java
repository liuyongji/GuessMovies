package com.lyj.guessmovies.model;

public class Movie {
	private String name;
	private String url;
	private String brief;
	public char[] getNameCharacters() {
		return name.toCharArray();
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNamelength() {
		return getNameCharacters().length;
	}
	public String getBrief() {
		return brief;
	}
	public void setBrief(String brief) {
		this.brief = brief;
	}

}
