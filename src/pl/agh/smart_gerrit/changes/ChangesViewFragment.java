package pl.agh.smart_gerrit.changes;

import pl.agh.smart_gerrit.GerritClient;
import pl.agh.smart_gerrit.HomeViewActivity;
import pl.agh.smart_gerrit.changes.model.ChangeModel;
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class ChangesViewFragment extends ListFragment {
	
	public static enum Type {
		ALL, OUT, IN
	}
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String QUERY = "q";

	private static final String ARG_LIST_TYPE = "list_type";

	private GerritClient client;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static ChangesViewFragment newInstance() {
		ChangesViewFragment fragment = new ChangesViewFragment();
		Bundle args = new Bundle();
		args.putInt(HomeViewActivity.ARG_SECTION_NUMBER, 2);
		fragment.setArguments(args);
		return fragment;
	}

	public static ChangesViewFragment newInstance(Type type) {
		ChangesViewFragment fragment = new ChangesViewFragment();
		Bundle args = new Bundle();
		args.putInt(HomeViewActivity.ARG_SECTION_NUMBER, type.ordinal()+2);
		args.putString(ARG_LIST_TYPE, type.name());
		fragment.setArguments(args);
		return fragment;
	}

	public static ChangesViewFragment newInstance(String query) {
		ChangesViewFragment fragment = new ChangesViewFragment();
		Bundle args = new Bundle();
		args.putInt(HomeViewActivity.ARG_SECTION_NUMBER, 2);
		args.putString(QUERY, query);
		fragment.setArguments(args);
		return fragment;
	}

	public static ChangesViewFragment newInstance(String query, Type type) {
		ChangesViewFragment fragment = new ChangesViewFragment();
		Bundle args = new Bundle();
		args.putInt(HomeViewActivity.ARG_SECTION_NUMBER, type.ordinal()+2);
		args.putString(ARG_LIST_TYPE, type.name());
		args.putString(QUERY, query);
		fragment.setArguments(args);
		return fragment;
	}

	public ChangesViewFragment() {

	}

	private ChangesListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		adapter = new ChangesListAdapter(getActivity(), getId());
		setListAdapter(adapter);
		client = GerritClient.getInstance(getActivity());
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {

		if (!isLoading)
			handler.post(new GetChangeTask(ChangesQueryBuilder.getBuider().setStatus(CommitStatus.OPEN)));

		getListView().setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				//int loadedItems = firstVisibleItem + visibleItemCount;
				//if ((loadedItems == totalItemCount) && !isLoading) {
					// handler.post(new
					// GetChangeTask(ChangesQueryBuilder.getBuider().setStatus(CommitStatus.OPEN)));

				//}

			}

		});
		super.onStart();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((HomeViewActivity) activity).onSectionAttached(getArguments().getInt(HomeViewActivity.ARG_SECTION_NUMBER));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		ChangeModel clickedModel = adapter.getItem(position);

		// TODO: add change Activity
		//Intent intent = new Intent(getActivity(), ProjectActivity.class);
		//intent.putExtra("id", clickedModel.getId());
		//getActivity().startActivityForResult(intent, 1);
	}

	private final Handler handler = new Handler();

	private volatile boolean isLoading = false;

	class GetChangeTask implements Runnable {

		private final ChangesQueryBuilder.Query params;

		public GetChangeTask(ChangesQueryBuilder.Query params) {
			this.params = params;
		}

		@Override
		public void run() {
			if (!isLoading) {
				isLoading = true;
				if (getArguments().getString(QUERY) != null) {
					this.params.setProject(getArguments().getString(QUERY).trim());

					getActivity().getActionBar().setSubtitle(getArguments().getString(QUERY).trim());
				}
				if (getArguments().getString(ARG_LIST_TYPE) != null && getArguments().getString(ARG_LIST_TYPE).equals(Type.OUT.name())) {
					this.params.setMy();
				}
				if (getArguments().getString(ARG_LIST_TYPE) != null && getArguments().getString(ARG_LIST_TYPE).equals(Type.IN.name())) {
					this.params.setAssignedToMe();
				}
				client.get(this.params, new GerritClient.AsyncResponseHandler() {

					@Override
					public void onSuccess(JsonElement json) {
						Gson gson = new Gson();

						for (JsonElement changeObject : json.getAsJsonArray()) {
							ChangeModel model = gson.fromJson(changeObject.getAsJsonObject(), ChangeModel.class);
							adapter.add(model);

						}
						isLoading = false;
						Log.i("GetChangeTask", json.toString());
					}
				});
			}

		}

	}

}
