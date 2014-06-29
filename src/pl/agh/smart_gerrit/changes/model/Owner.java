package pl.agh.smart_gerrit.changes.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class Owner implements Serializable {

	@Expose
	private String name;

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}
}
