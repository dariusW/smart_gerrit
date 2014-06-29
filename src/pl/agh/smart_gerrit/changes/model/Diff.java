package pl.agh.smart_gerrit.changes.model;

import java.util.ArrayList;

public class Diff {

	private DiffType diffType;

	private ArrayList<String> content = new ArrayList<String>();

	public Diff() {
		super();
	}

	public Diff( DiffType diffType, ArrayList<String> content ) {
		super();
		this.diffType = diffType;
		this.content = content;
	}

	public DiffType getDiffType() {
		return diffType;
	}

	public void setDiffType( DiffType diffType ) {
		this.diffType = diffType;
	}

	public ArrayList<String> getContent() {
		return content;
	}

	public void setContent( ArrayList<String> content ) {
		this.content = content;
	}

}
