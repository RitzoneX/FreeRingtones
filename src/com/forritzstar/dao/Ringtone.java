package com.forritzstar.dao;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.MediaStore;

public class Ringtone {
	private int id;
	private String title;
	private String data;
	private Uri uri;

	public Ringtone() {
		super();
	}

	public Ringtone(int id, String title, String data) {
		super();
		this.id = id;
		this.title = title;
		this.data = data;
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

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Uri getUri() {
		if (uri == null)
			uri = ContentUris.withAppendedId(
					MediaStore.Audio.Media.getContentUriForPath(data), id);
		return uri;
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
