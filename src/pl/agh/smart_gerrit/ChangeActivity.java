package pl.agh.smart_gerrit;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.agh.smart_gerrit.GerritClient.AsyncResponseHandler;
import pl.agh.smart_gerrit.changes.model.ChangeModel;
import pl.agh.smart_gerrit.changes.model.ReviewInput;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class ChangeActivity extends Activity {

	public static final String SELECTED_CHANGE_EXTRA = "selectedChange";

	private ChangeModel changeModel;

	private int reviewPosition = 2;

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change);

		Intent intent = getIntent();
		ChangeModel changeModel = (ChangeModel) intent.getSerializableExtra(SELECTED_CHANGE_EXTRA);
		this.changeModel = changeModel;

		// TODO: extract info from fileInfoModel and display it
		TextView textView = (TextView) findViewById(R.id.changeProjectTextView);
		textView.setText(changeModel.getProject());

		textView = (TextView) findViewById(R.id.changeBranchTextView);
		textView.setText(changeModel.getBranch());

		textView = (TextView) findViewById(R.id.changeIdTextView);
		textView.setText(changeModel.getId());

		textView = (TextView) findViewById(R.id.changeUpdateDateTextView);
		textView.setText(changeModel.getUpdated());

		textView = (TextView) findViewById(R.id.changeSubjectTextView);
		textView.setText(changeModel.getSubject());

		progressDialog = new ProgressDialog(this);
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change, menu);
		return true;
	}

	public ChangeModel getChangeModel() {
		return changeModel;
	}

	public void review( View view ) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final CharSequence[] items = { "+2", "+1", "0", "-1", "-2" };
		builder.setTitle("Review").setSingleChoiceItems(items, 2, new OnClickListener() {

			@Override
			public void onClick( DialogInterface dialog, int which ) {
				reviewPosition = which;
			}
		}).setPositiveButton("Review", new OnClickListener() {

			@Override
			public void onClick( DialogInterface dialog, int which ) {
				new PostReviewTask().execute((String) items[reviewPosition]);
				// if review was +2 merge change
				if ( reviewPosition == 0 ) {
					new PostSubmitChange().execute();
				}
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	public void showProgressBar() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				progressDialog.show();
			}
		});
	}

	public void hideProgressBar() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				progressDialog.hide();
			}
		});
	}

	private class PostReviewTask extends AsyncTask<String, Void, Void> {

		private GerritClient client = GerritClient.getInstance(getApplicationContext());

		@Override
		protected Void doInBackground( final String... params ) {
			GerritClientQuery query = new GerritClientQuery() {

				@Override
				public List<String> getUrl() {
					ChangeModel changeModel = ChangeActivity.this.getChangeModel();
					List<String> urlPartList = new ArrayList<String>();
					urlPartList.add("changes");
					urlPartList.add(changeModel.getId().replace("/", "%2F"));
					urlPartList.add("revisions");
					urlPartList.add("current");
					urlPartList.add("review");
					return urlPartList;
				}

				@Override
				public Map<String, String> getParams() {
					return new HashMap<String, String>();
				}
			};

			String reviewNote = params[0];
			String verified = "+2".equals(reviewNote) ? "+1" : "0";
			ReviewInput reviewInput = new ReviewInput(reviewNote, verified);
			Gson gson = new Gson();

			try {
				client.post(ChangeActivity.this, query, gson.toJson(reviewInput),
						"application/json", new AsyncResponseHandler() {

							@Override
							public void onSuccess( JsonElement json ) {
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										Toast.makeText(getApplicationContext(),
												"Review successful", Toast.LENGTH_SHORT).show();
									}
								});
							}
						});
			} catch ( UnsupportedEncodingException e ) {
				e.printStackTrace();
			}

			return null;

		}
	}

	private class PostSubmitChange extends AsyncTask<String, Void, Void> {

		private GerritClient client = GerritClient.getInstance(getApplicationContext());

		@Override
		protected Void doInBackground( String... params ) {
			showProgressBar();
			GerritClientQuery query = new GerritClientQuery() {

				@Override
				public List<String> getUrl() {
					ChangeModel changeModel = ChangeActivity.this.getChangeModel();
					List<String> urlPartList = new ArrayList<String>();
					urlPartList.add("changes");
					urlPartList.add(changeModel.getId().replace("/", "%2F"));
					urlPartList.add("submit");
					return urlPartList;
				}

				@Override
				public Map<String, String> getParams() {
					return new HashMap<String, String>();
				}
			};

			try {
				client.post(ChangeActivity.this, query, "{\"wait_for_merge\": true}",
						"application/json", new AsyncResponseHandler() {

							@Override
							public void onSuccess( JsonElement json ) {
								final String statusStr = json.getAsJsonObject().get("status")
										.getAsString();
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										String notMergedStr = ("MERGED".equals(statusStr)) ? " "
												: " NOT ";
										Toast.makeText(getApplicationContext(),
												"Change was" + notMergedStr, Toast.LENGTH_SHORT)
												.show();
									}
								});
							}
						});
			} catch ( UnsupportedEncodingException e ) {
				e.printStackTrace();
			}

			hideProgressBar();
			return null;
		}

	}

}
