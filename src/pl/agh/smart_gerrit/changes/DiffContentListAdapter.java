package pl.agh.smart_gerrit.changes;

import pl.agh.smart_gerrit.changes.model.Diff;
import android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DiffContentListAdapter extends ArrayAdapter<Diff> {

	private final Context context;

	public DiffContentListAdapter( Context context, int textViewResourceId ) {
		super(context, textViewResourceId);
		this.context = context;
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TextView rowView = (TextView) inflater.inflate(R.layout.simple_list_item_1, parent, false);
		Diff diff = getItem(position);
		StringBuilder diffStr = new StringBuilder();
		for ( String diffLine : diff.getContent() ) {
			diffStr.append(diffLine);
			diffStr.append("\n");
		}
		rowView.setText(diffStr.toString());
		rowView.setBackgroundColor(diff.getDiffType().getColor(context));
		return rowView;
	}

}
