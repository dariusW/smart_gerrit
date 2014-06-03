package pl.agh.smart_gerrit.projects;

import org.apache.http.Header;

import pl.agh.smart_gerrit.GerritClient;
import pl.agh.smart_gerrit.GerritClient.AsyncResponseHandler;
import pl.agh.smart_gerrit.GerritClientQuery;
import pl.agh.smart_gerrit.R;
import pl.agh.smart_gerrit.changes.ChangesQueryBuilder;
import pl.agh.smart_gerrit.changes.CommitStatus;
import pl.agh.smart_gerrit.changes.model.ChangeModel;
import pl.agh.smart_gerrit.projects.ProjectQueryBuilder.Query;
import pl.agh.smart_gerrit.projects.ProjectQueryBuilder.Type;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class ProjectActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handler = new Handler();
		client = GerritClient.getInstance(this);

		setContentView(R.layout.project_view);
	}

	private Handler handler;
	private GerritClient client;
	private ProjectModel runningModel;

	@Override
	protected void onStart() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String id = extras.getString("id");

			final Query query = ProjectQueryBuilder.getBuider(Type.ITEM).setId(
					id);
			handler.post(new Runnable() {

				@Override
				public void run() {
					client.get(query, new AsyncResponseHandler() {

						@Override
						public void onSuccess(JsonElement json) {
							Gson gson = new Gson();
							final ProjectModel model = gson.fromJson(json,
									ProjectModel.class);
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									runningModel = model;
									getActionBar().setTitle(model.getName());
									((TextView) findViewById(R.id.project_view_parent))
											.setText(model.getParent());

									((TextView) findViewById(R.id.project_view_status))
											.setText(model.getState());

									((TextView) findViewById(R.id.project_view_description))
											.setText(model.getDescription());
								}
							});

							Log.d("ProjectActivity", json.toString());

						}
					});

				}
			});

			final Query query2 = ProjectQueryBuilder.getBuider(Type.BRANCHES)
					.setId(id).setBranchQuery();
			handler.post(new Runnable() {

				@Override
				public void run() {
					client.get(query2, new AsyncResponseHandler() {
						public void onSuccess(JsonElement json) {
							Gson gson = new Gson();
							StringBuilder branchesDescBuilder = new StringBuilder();
							for (JsonElement branchEntry : json
									.getAsJsonArray()) {
								ProjectBranchModel model = gson.fromJson(
										branchEntry, ProjectBranchModel.class);
								branchesDescBuilder.append(model.getRef());
								branchesDescBuilder.append("\n");
							}
							final String branchesText = branchesDescBuilder
									.toString();
							runOnUiThread(new Runnable() {

								@Override
								public void run() {

									((TextView) findViewById(R.id.project_view_branches))
											.setText(branchesText);
								}
							});
						};
					});

				}
			});

			final GerritClientQuery query3 = ChangesQueryBuilder.getBuider()
					.setProject(id).setStatus(CommitStatus.OPEN);
			handler.post(new Runnable() {

				@Override
				public void run() {
					client.get(query3, new AsyncResponseHandler() {
						public void onSuccess(JsonElement json) {
							Gson gson = new Gson();
							StringBuilder changesTextBuilder = new StringBuilder();
							for (JsonElement branchEntry : json
									.getAsJsonArray()) {
								ChangeModel model = gson.fromJson(branchEntry,
										ChangeModel.class);
								changesTextBuilder.append(model.getSubject());
								changesTextBuilder.append(" | by ");
								changesTextBuilder.append(model.getOwner().getName());
								changesTextBuilder.append("\n");

							}
							

							final String changesText = changesTextBuilder.toString();
							runOnUiThread(new Runnable() {

								@Override
								public void run() {

									((TextView) findViewById(R.id.project_view_changes))
											.setText(changesText);
								}
							});
						};
					});

				}
			});
		} else {
			finishActivity(0);
		}
		super.onStart();
	}

}
