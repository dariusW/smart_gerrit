package pl.agh.smart_gerrit.changes.model;

import pl.agh.smart_gerrit.R;
import android.content.Context;

public enum DiffType {
	A, B, AB;

	public int getColor( Context context ) {
		switch ( this ) {
		case A:
			return context.getResources().getColor(R.color.diffDelete);
		case B:
			return context.getResources().getColor(R.color.diffAdd);
		default:
			return context.getResources().getColor(R.color.white);
		}
	}
}
