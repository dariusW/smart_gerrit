package pl.agh.smart_gerrit.changes;

import java.util.ArrayList;
import java.util.List;

import pl.agh.smart_gerrit.R;
import pl.agh.smart_gerrit.changes.model.ChangeModel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ChangesListAdapter extends ArrayAdapter<ChangeModel> {
	public ChangesListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.context = context;
	}

	private final Context context;
	private final List<ChangeModel> changes = new ArrayList<ChangeModel>();

	private class ViewHolder {
		public TextView desc;
		public TextView name;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = null;

		if (convertView != null) {
			rowView = convertView;
		} else {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.projects_list_item_view,
					parent, false);

			TextView desc = (TextView) rowView.findViewById(R.id.project_desc);
			TextView name = (TextView) rowView.findViewById(R.id.project_name);
			ViewHolder holder = new ViewHolder();
			holder.name = name;
			holder.desc = desc;
			rowView.setTag(holder);

		}
		ViewHolder tag = (ViewHolder) rowView.getTag();
		if (tag != null) {
			tag.name.setText(changes.get(position).getId());
			tag.desc.setText(changes.get(position).getProject());
		}
		return rowView;
	}

	@Override
	public void add(ChangeModel object) {
		changes.add(object);
		super.add(object);
	}
}
