package pl.agh.smart_gerrit;

import pl.agh.smart_gerrit.changes.model.ChangeModel;
import pl.agh.smart_gerrit.changes.model.FileInfoModel;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class ChangeDiffActivity extends Activity {

	public static final String EXTRA_KEY_FILE_INFO = "EXTRA_KEY_FILE_INFO";

	public static final String EXTRA_KEY_CHANGE_INFO = "EXTRA_KEY_CHANGE_INFO";

	FileInfoModel fileInfoModel;

	ChangeModel changeModel;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_diff);

		fileInfoModel = (FileInfoModel) getIntent().getSerializableExtra(EXTRA_KEY_FILE_INFO);
		changeModel = (ChangeModel) getIntent().getSerializableExtra(EXTRA_KEY_CHANGE_INFO);
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change_diff, menu);
		return true;
	}

}
