package pl.agh.smart_gerrit.projects;

import java.util.Map.Entry;

import pl.agh.smart_gerrit.GerritClient;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class ProjectsViewFragment extends ListFragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String QUERY = "q";

	private GerritClient client;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static ProjectsViewFragment newInstance() {
		ProjectsViewFragment fragment = new ProjectsViewFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public static ProjectsViewFragment newInstance(String query) {
		ProjectsViewFragment fragment = new ProjectsViewFragment();
		Bundle args = new Bundle();
		args.putString(QUERY, query);
		fragment.setArguments(args);
		return fragment;
	}

	public ProjectsViewFragment() {

	}

	private ProjectsListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		adapter = new ProjectsListAdapter(getActivity(), getId());
		setListAdapter(adapter);
		client = GerritClient.getInstance(getActivity());
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {

		handler.post(new GetProjectsTask(ProjectQueryBuilder
				.getBuider(ProjectQueryBuilder.Type.LIST)));

		getListView().setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int loadedItems = firstVisibleItem + visibleItemCount;
				if ((loadedItems == totalItemCount) && !isLoading) {
					handler.post(new GetProjectsTask(ProjectQueryBuilder
							.getBuider(ProjectQueryBuilder.Type.LIST)
							.setOffset(totalItemCount)));

				}

			}

		});
		super.onStart();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		ProjectModel clickedModel = adapter.getItem(position);
		Intent intent = new Intent(getActivity(), ProjectActivity.class);
		intent.putExtra("id", clickedModel.getId());
		getActivity().startActivityForResult(intent, 1);
	}

	private Handler handler = new Handler();

	private volatile boolean isLoading = false;

	class GetProjectsTask implements Runnable {

		private final ProjectQueryBuilder.Query params;

		public GetProjectsTask(ProjectQueryBuilder.Query params) {
			this.params = params;
		}

		@Override
		public void run() {
			if (!isLoading) {
				isLoading = true;
				if (getArguments().getString(QUERY) != null) {
					this.params.setPrefix(getArguments().getString(QUERY)
							.trim());
				}
				client.get(this.params,
						new GerritClient.AsyncResponseHandler() {

							@Override
							public void onSuccess(JsonElement json) {
								Gson gson = new Gson();
								for (Entry<String, JsonElement> projectEntry : json
										.getAsJsonObject().entrySet()) {
									ProjectModel model = gson.fromJson(
											projectEntry.getValue(),
											ProjectModel.class);
									adapter.add(model);

								}
								isLoading = false;
								Log.i("GetProjectsTask", json.toString());
							}
						});
			}

		}

	}

}
