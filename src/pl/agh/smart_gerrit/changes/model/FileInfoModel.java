package pl.agh.smart_gerrit.changes.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class FileInfoModel implements Serializable {

	private String fileName;
	@Expose
	private String status;
	@Expose
	private Boolean binary;
	@Expose
	private String old_path;
	@Expose
	private Integer lines_inserted;
	@Expose
	private Integer lines_deleted;

	public String getStatus() {
		return status;
	}

	public void setStatus( String status ) {
		this.status = status;
	}

	public Boolean getBinary() {
		return binary;
	}

	public void setBinary( Boolean binary ) {
		this.binary = binary;
	}

	public String getOld_path() {
		return old_path;
	}

	public void setOld_path( String old_path ) {
		this.old_path = old_path;
	}

	public Integer getLines_inserted() {
		return lines_inserted;
	}

	public void setLines_inserted( Integer lines_inserted ) {
		this.lines_inserted = lines_inserted;
	}

	public Integer getLines_deleted() {
		return lines_deleted;
	}

	public void setLines_deleted( Integer lines_deleted ) {
		this.lines_deleted = lines_deleted;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName( String fileName ) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "FileInfoModel [fileName=" + fileName + ", status=" + status + ", binary=" + binary
				+ ", old_path=" + old_path + ", lines_inserted=" + lines_inserted
				+ ", lines_deleted=" + lines_deleted + "]";
	}

}
