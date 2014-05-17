package pl.agh.smart_gerrit.projects;

import pl.agh.smart_gerrit.R;
import android.app.Activity;
import android.os.Bundle;

public class ProjectActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project_view);
	}
 
}
