package pl.agh.smart_gerrit.changes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import pl.agh.smart_gerrit.ChangeActivity;
import pl.agh.smart_gerrit.ChangeDiffActivity;
import pl.agh.smart_gerrit.GerritClient;
import pl.agh.smart_gerrit.GerritClient.AsyncResponseHandler;
import pl.agh.smart_gerrit.GerritClientQuery;
import pl.agh.smart_gerrit.changes.model.ChangeModel;
import pl.agh.smart_gerrit.changes.model.FileInfoModel;
import android.R;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class FileInfoListFragment extends ListFragment {

	private FileInfoListAdapter adapter;

	private GerritClient client;

	private final Handler handler = new Handler();

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);

		client = GerritClient.getInstance(getActivity().getApplicationContext());

		adapter = new FileInfoListAdapter(getActivity().getApplicationContext(),
				R.layout.simple_list_item_1);
		setListAdapter(adapter);
	}

	@Override
	public void onStart() {
		handler.post(new GetChangeInfoTask());
		super.onStart();
	}

	@Override
	public void onStop() {
		adapter.clear();
		super.onStop();
	}

	@Override
	public void onListItemClick( ListView l, View v, int position, long id ) {
		Intent i = new Intent(getActivity(), ChangeDiffActivity.class);
		i.putExtra(ChangeDiffActivity.EXTRA_KEY_FILE_INFO, adapter.get(position));
		ChangeModel changeModel = ((ChangeActivity) getActivity()).getChangeModel();
		i.putExtra(ChangeDiffActivity.EXTRA_KEY_CHANGE_INFO, changeModel);
		startActivity(i);
	}

	class GetChangeInfoTask implements Runnable {

		@Override
		public void run() {
			adapter.clear();
			adapter.notifyDataSetChanged();
			GerritClientQuery changeInfoQuery = new GerritClientQuery() {

				@Override
				public List<String> getUrl() {
					ChangeModel changeModel = ((ChangeActivity) getActivity()).getChangeModel();
					List<String> urlPartList = new ArrayList<String>();
					urlPartList.add("changes");
					urlPartList.add(changeModel.getId().replace("/", "%2F"));
					urlPartList.add("revisions");
					urlPartList.add("current");
					urlPartList.add("files");
					return urlPartList;
				}

				@Override
				public Map<String, String> getParams() {
					return new HashMap<String, String>();
				}
			};
			client.get(changeInfoQuery, new AsyncResponseHandler() {

				@Override
				public void onSuccess( JsonElement json ) {
					Gson gson = new Gson();

					// deserialize file info list from JSON
					JsonObject fileListObj = json.getAsJsonObject();
					Set<Entry<String, JsonElement>> entrySet = fileListObj.entrySet();
					Iterator<Entry<String, JsonElement>> it = entrySet.iterator();

					while ( it.hasNext() ) {
						Entry<String, JsonElement> fileEntry = it.next();
						FileInfoModel fileInfoModel = gson.fromJson(fileEntry.getValue(),
								FileInfoModel.class);
						fileInfoModel.setFileName(fileEntry.getKey());

						Log.d("GetChangeInfoTask", "Deserialized file info: " + fileInfoModel);
						adapter.add(fileInfoModel);
					}

					adapter.notifyDataSetChanged();

				}
			});
		}
	}
}
