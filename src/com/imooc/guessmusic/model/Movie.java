package com.imooc.guessmusic.model;

public class Movie {
	private String name;
	private String url;
	private int id;
	private int namelength;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNamelength() {
		return namelength;
	}
	public void setNamelength(int namelength) {
		this.namelength = namelength;
	}

}
