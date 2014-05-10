package com.forritzstar.dao;

public class Ringtone {
	private int id;
	private String title;
	private String uri;

	public Ringtone() {
		super();
	}

	public Ringtone(int id, String title, String uri) {
		super();
		this.id = id;
		this.title = title;
		this.uri = uri;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ringtone other = (Ringtone) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
