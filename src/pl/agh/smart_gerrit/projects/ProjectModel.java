package pl.agh.smart_gerrit.projects;

import java.util.List;
import java.net.URLDecoder;

public class ProjectModel {
	private String id;
	private String name;
	private List<String> webLinks;
	private String description;
	private String parent;
	private String state;

	public String getName() {
		return decode(name);
	}

	public void setName(String name) {
		this.name = decode(name);
	}

	public List<String> getWebLinks() {
		return webLinks;
	}

	public void setWebLinks(List<String> webLinks) {
		this.webLinks = webLinks;
	}

	public String getParent() {
		return decode(parent);
	}

	public void setParent(String parent) {
		this.parent = decode(parent);
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDescription() {
		return decode(description);
	}

	public void setDescription(String description) {
		this.description = decode(description);
	}

	public String getId() {
		return decode(id);
	}

	public void setId(String id) {
		this.id = decode(id);
	}

	private static String decode(String encodecTxt) {
		if (encodecTxt == null)
			return encodecTxt;
		try {
			return URLDecoder.decode(encodecTxt, "UTF-8");
		} catch (Exception e) {
			return encodecTxt;
		}

	}
}
