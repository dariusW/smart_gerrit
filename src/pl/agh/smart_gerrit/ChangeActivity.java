package pl.agh.smart_gerrit;

import pl.agh.smart_gerrit.changes.model.ChangeModel;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class ChangeActivity extends Activity {

	public static final String SELECTED_CHANGE_EXTRA = "selectedChange";

	private ChangeModel changeModel;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change);

		Intent intent = getIntent();
		ChangeModel changeModel = (ChangeModel) intent.getSerializableExtra(SELECTED_CHANGE_EXTRA);
		this.changeModel = changeModel;

		// TODO: extract info from changeModel and display it
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

		// TODO: get diff for this change
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

}
