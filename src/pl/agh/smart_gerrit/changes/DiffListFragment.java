package pl.agh.smart_gerrit.changes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import pl.agh.smart_gerrit.ChangeDiffActivity;
import pl.agh.smart_gerrit.GerritClient;
import pl.agh.smart_gerrit.GerritClient.AsyncResponseHandler;
import pl.agh.smart_gerrit.GerritClientQuery;
import pl.agh.smart_gerrit.changes.model.ChangeModel;
import pl.agh.smart_gerrit.changes.model.Diff;
import pl.agh.smart_gerrit.changes.model.DiffType;
import pl.agh.smart_gerrit.changes.model.FileInfoModel;
import android.R;
import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DiffListFragment extends ListFragment {

	private DiffContentListAdapter adapter;

	private FileInfoModel fileInfoModel;

	private ChangeModel changeModel;

	private final Handler handler = new Handler();

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		adapter = new DiffContentListAdapter(getActivity(), R.layout.simple_list_item_1);
		setListAdapter(adapter);

		fileInfoModel = (FileInfoModel) getActivity().getIntent().getSerializableExtra(
				ChangeDiffActivity.EXTRA_KEY_FILE_INFO);
		changeModel = (ChangeModel) getActivity().getIntent().getSerializableExtra(
				ChangeDiffActivity.EXTRA_KEY_CHANGE_INFO);

		handler.post(new GetDiffInfoTask());
	}

	class GetDiffInfoTask implements Runnable {

		private GerritClient client = GerritClient.getInstance(getActivity()
				.getApplicationContext());;

		@Override
		public void run() {
			GerritClientQuery diffInfoQuery = new GerritClientQuery() {

				@Override
				public List<String> getUrl() {
					List<String> urlPartList = new ArrayList<String>();
					urlPartList.add("changes");
					urlPartList.add(changeModel.getId().replace("/", "%2F"));
					urlPartList.add("revisions");
					urlPartList.add("current");
					urlPartList.add("files");
					urlPartList.add(fileInfoModel.getFileName().replace("/", "%2F"));
					urlPartList.add("diff");
					return urlPartList;
				}

				@Override
				public Map<String, String> getParams() {
					return new HashMap<String, String>();
				}
			};

			client.get(diffInfoQuery, new AsyncResponseHandler() {

				@Override
				public void onSuccess( JsonElement json ) {
					Gson gson = new Gson();

					JsonObject responseJsonObj = json.getAsJsonObject();
					for ( JsonElement diffJson : responseJsonObj.get("content").getAsJsonArray() ) {
						Set<Entry<String, JsonElement>> diffEntries = diffJson.getAsJsonObject()
								.entrySet();
						Iterator<Entry<String, JsonElement>> it = diffEntries.iterator();
						while ( it.hasNext() ) {
							Entry<String, JsonElement> diffEntry = it.next();
							ArrayList<String> diffContent = new ArrayList<String>();

							for ( JsonElement lineElement : diffEntry.getValue().getAsJsonArray() ) {
								diffContent.add(lineElement.getAsString());
							}

							Diff diff = new Diff(
									DiffType.valueOf(diffEntry.getKey().toUpperCase()), diffContent);
							adapter.add(diff);
						}
					}

					adapter.notifyDataSetChanged();
				}
			});
		}
	}

}
