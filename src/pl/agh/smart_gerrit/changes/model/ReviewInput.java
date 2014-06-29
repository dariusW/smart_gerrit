package pl.agh.smart_gerrit.changes.model;

import com.google.gson.annotations.SerializedName;

public class ReviewInput {

	private String message;

	@SerializedName ( "labels" )
	private CodeReview codeReview;

	public ReviewInput( String review, String verified ) {
		codeReview = new CodeReview(review, verified);
	}

}
