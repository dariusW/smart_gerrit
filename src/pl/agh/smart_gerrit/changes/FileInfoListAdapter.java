package pl.agh.smart_gerrit.changes;

import java.util.ArrayList;

import pl.agh.smart_gerrit.changes.model.FileInfoModel;
import android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FileInfoListAdapter extends ArrayAdapter<FileInfoModel> {

	private final ArrayList<FileInfoModel> fileInfoList = new ArrayList<FileInfoModel>();
	private final Context context;

	public FileInfoListAdapter( Context context, int textViewResourceId ) {
		super(context, textViewResourceId);
		this.context = context;
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TextView rowView = (TextView) inflater.inflate(R.layout.simple_list_item_1, parent, false);
		FileInfoModel fileInfo = fileInfoList.get(position);
		String status = fileInfo.getStatus() != null ? fileInfo.getStatus() : "M";
		rowView.setText("[" + status + "] " + fileInfo.getFileName());
		rowView.setBackgroundColor(context.getResources().getColor(R.color.black));
		return rowView;
	}

	@Override
	public int getCount() {
		return fileInfoList.size();
	}

	@Override
	public void add( FileInfoModel fileInfo ) {
		fileInfoList.add(fileInfo);
	}

	@Override
	public void clear() {
		fileInfoList.clear();
	}

	public FileInfoModel get( int position ) {
		return fileInfoList.get(position);
	}

}
