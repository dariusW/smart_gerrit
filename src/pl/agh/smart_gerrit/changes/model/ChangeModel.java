package pl.agh.smart_gerrit.changes.model;

import com.google.gson.annotations.Expose;


public class ChangeModel {
	@Expose
	private String kind;
	@Expose
	private String id;
	@Expose
	private String project;
	@Expose
	private String branch;
	@Expose
	private String change_id;
	@Expose
	private String subject;
	@Expose
	private String status;
	@Expose
	private String created;
	@Expose
	private String updated;
	@Expose
	private Boolean mergeable;
	@Expose
	private Integer insertions;
	@Expose
	private Integer deletions;
	@Expose
	private String _sortkey;
	@Expose
	private Integer _number;
	@Expose
	private Owner owner;

	public String getKind() {
	return kind;
	}

	public void setKind(String kind) {
	this.kind = kind;
	}

	public String getId() {
	return id;
	}

	public void setId(String id) {
	this.id = id;
	}

	public String getProject() {
	return project;
	}

	public void setProject(String project) {
	this.project = project;
	}

	public String getBranch() {
	return branch;
	}

	public void setBranch(String branch) {
	this.branch = branch;
	}

	public String getChange_id() {
	return change_id;
	}

	public void setChange_id(String change_id) {
	this.change_id = change_id;
	}

	public String getSubject() {
	return subject;
	}

	public void setSubject(String subject) {
	this.subject = subject;
	}

	public String getStatus() {
	return status;
	}

	public void setStatus(String status) {
	this.status = status;
	}

	public String getCreated() {
	return created;
	}

	public void setCreated(String created) {
	this.created = created;
	}

	public String getUpdated() {
	return updated;
	}

	public void setUpdated(String updated) {
	this.updated = updated;
	}

	public Boolean getMergeable() {
	return mergeable;
	}

	public void setMergeable(Boolean mergeable) {
	this.mergeable = mergeable;
	}

	public Integer getInsertions() {
	return insertions;
	}

	public void setInsertions(Integer insertions) {
	this.insertions = insertions;
	}

	public Integer getDeletions() {
	return deletions;
	}

	public void setDeletions(Integer deletions) {
	this.deletions = deletions;
	}

	public String get_sortkey() {
	return _sortkey;
	}

	public void set_sortkey(String _sortkey) {
	this._sortkey = _sortkey;
	}

	public Integer get_number() {
	return _number;
	}

	public void set_number(Integer _number) {
	this._number = _number;
	}

	public Owner getOwner() {
	return owner;
	}

	public void setOwner(Owner owner) {
	this.owner = owner;
	}
}
