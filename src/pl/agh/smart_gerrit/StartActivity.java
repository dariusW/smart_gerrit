package pl.agh.smart_gerrit;

import org.apache.http.Header;

import pl.agh.smart_gerrit.GerritClient.AsyncResponseHandler;
import pl.agh.smart_gerrit.projects.ProjectQueryBuilder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonElement;

public class StartActivity extends Activity {

	private Context context;
	private EditText login;
	private EditText passwd;
	private EditText host;
	private Handler handler = new Handler();
	private Button loginBtn;
	private GerritClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;

		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();

		setContentView(R.layout.start_view);

		login = (EditText) findViewById(R.id.login);
		passwd = (EditText) findViewById(R.id.passwd);
		host = (EditText) findViewById(R.id.host);

		addButtonListeners();
	}

	private void addButtonListeners() {
		loginBtn = (Button) findViewById(R.id.start_login_btn);
		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				enableGUI(false);

				login = (EditText) findViewById(R.id.login);
				passwd = (EditText) findViewById(R.id.passwd);
				host = (EditText) findViewById(R.id.host);

				SharedPreferences settings = PreferenceManager
						.getDefaultSharedPreferences(context);
				SharedPreferences.Editor settingsEditor = settings.edit();
				settingsEditor
						.putString("username", login.getText().toString());
				settingsEditor.putString("password", passwd.getText()
						.toString());
				settingsEditor.putString("host", host.getText().toString());
				settingsEditor.apply();

				handler.post(new CheckConnection());

			}
		});
	}

	private void enableGUI(boolean enable) {
		login.setEnabled(enable);
		passwd.setEnabled(enable);
		host.setEnabled(enable);
		loginBtn.setEnabled(enable);
	}

	private class CheckConnection implements Runnable {

		@Override
		public void run() {
			if (client == null) {
				client = GerritClient.getInstance(context);
			}
			client.get(ProjectQueryBuilder
					.getBuider(ProjectQueryBuilder.Type.LIST),
					new AsyncResponseHandler() {

						@Override
						public void onSuccess(JsonElement json) {
							Intent intent = new Intent(StartActivity.this,
									HomeViewActivity.class);
							startActivity(intent);

						}

						@Override
						public void onFailure(int status, Header[] header,
								byte[] content, Throwable e) {
							Toast.makeText(context, "Cannot connect to host",
									Toast.LENGTH_LONG).show();

							enableGUI(true);

							super.onFailure(status, header, content, e);
						}

					});

		}

	}
}
