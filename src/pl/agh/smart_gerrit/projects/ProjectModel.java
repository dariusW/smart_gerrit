package pl.agh.smart_gerrit.projects;

import java.util.List;

public class ProjectModel {
	private String id;
	private String name;
	private List<String> webLinks;
	private String description;
	private String parent;
	private String state;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getWebLinks() {
		return webLinks;
	}

	public void setWebLinks(List<String> webLinks) {
		this.webLinks = webLinks;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
