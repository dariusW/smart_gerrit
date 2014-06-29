package pl.agh.smart_gerrit.changes.model;

import com.google.gson.annotations.SerializedName;

public class CodeReview {

	@SerializedName ( "Code-Review" )
	private String reviewValue;

	@SerializedName ( "Verified" )
	private String verified;

	public CodeReview( String reviewValue, String verified ) {
		super();
		this.reviewValue = reviewValue;
		this.verified = verified;
	}

	public String getReviewValue() {
		return reviewValue;
	}

	public void setReviewValue( String reviewValue ) {
		this.reviewValue = reviewValue;
	}

	public String getVerified() {
		return verified;
	}

	public void setVerified( String verified ) {
		this.verified = verified;
	}

}
