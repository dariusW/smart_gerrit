package pl.agh.smart_gerrit;

import java.util.Map;

import org.apache.http.Header;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class GerritClient {

	private GerritClient(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		host = prefs.getString("host", "").trim();
		// TODO: check validity of format "https://example.com" format expected

		username = prefs.getString("username", "");
		password = prefs.getString("password", "");

		if (!username.equals("") && !password.equals("")) {
			useAuthentication = true;
		}

		client = new AsyncHttpClient();
		if (useAuthentication) {
			client.setBasicAuth(username, password);
		}
	}

	public static abstract class AsyncResponseHandler {
		public abstract void onSuccess(Map<String, String> jsonObject);

		public void onFailure(int status, Header[] header, byte[] content,
				Throwable e) {
			Log.w("GerritClient", e.getMessage());
		}
	}

	private static final String AUTH_URL_SUFFIX = "/a";

	private SharedPreferences prefs;
	private String host;
	private String username;
	private String password;
	private boolean useAuthentication = false;
	private AsyncHttpClient client;
	public static boolean debug = true;

	public static GerritClient getInstance(Context context) {
		GerritClient client = new GerritClient(context);

		return client;
	}

	public void get(String subUrl, final AsyncResponseHandler handler) {
		String url = host + (useAuthentication ? AUTH_URL_SUFFIX : "") + "/"
				+ subUrl;

		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				if (debug) {
					Log.i("GerritClient", response);
				}
				response = response.substring(5);
				JsonParserFactory factory = JsonParserFactory.getInstance();
				JSONParser parser = factory.newJsonParser();
				@SuppressWarnings("unchecked")
				Map<String, String> jsonData = (Map<String, String>) parser
						.parseJson(response);

				handler.onSuccess(jsonData);
			}

			@Override
			public void onFailure(int status, Header[] header, byte[] content,
					Throwable e) {
				handler.onFailure(status, header, content, e);
			}
		});
	}
}
