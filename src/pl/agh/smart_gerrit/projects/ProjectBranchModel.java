package pl.agh.smart_gerrit.projects;

public class ProjectBranchModel {
	private String ref;
	private String revision;
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}
}
