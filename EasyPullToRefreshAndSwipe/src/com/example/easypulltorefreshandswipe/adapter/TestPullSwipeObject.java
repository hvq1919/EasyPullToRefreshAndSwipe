package com.example.easypulltorefreshandswipe.adapter;

public class TestPullSwipeObject {
	private int id;
	private String name;
	private String sub;

	public TestPullSwipeObject() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public TestPullSwipeObject(int id, String name,  String sub) {
		super();
		this.id = id;
		this.name = name;
		this.sub = sub;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

}
