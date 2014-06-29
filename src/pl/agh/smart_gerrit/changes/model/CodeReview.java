package pl.agh.smart_gerrit.changes.model;

import com.google.gson.annotations.SerializedName;

public class CodeReview {

	@SerializedName ( "Code-Review" )
	private String reviewValue;

	public CodeReview( String reviewValue ) {
		super();
		this.reviewValue = reviewValue;
	}

	public String getReviewValue() {
		return reviewValue;
	}

	public void setReviewValue( String reviewValue ) {
		this.reviewValue = reviewValue;
	}

}
