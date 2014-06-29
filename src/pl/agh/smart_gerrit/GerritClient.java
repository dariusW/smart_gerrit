package pl.agh.smart_gerrit;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class GerritClient {

	private static final int DEFAULT_RETRY = 5;

	private static final int DEFAULT_TIMEOUT = 60000;

	private GerritClient( Context context ) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		host = prefs.getString("host", "").trim();
		if ( !host.startsWith("http://") && !host.startsWith("https://") ) {
			host = "http://" + host;
		}
		// TODO: check validity of format "https://example.com" format expected

		username = prefs.getString("username", "");
		password = prefs.getString("password", "");

		if ( !username.equals("") && !password.equals("") ) {
			useAuthentication = true;
		}

		client = new AsyncHttpClient();
		if ( useAuthentication ) {
			client.setBasicAuth(username, password);
		}
		client.setTimeout(DEFAULT_TIMEOUT);
		client.setMaxRetriesAndTimeout(DEFAULT_RETRY, DEFAULT_TIMEOUT);
	}

	public static abstract class AsyncResponseHandler {

		public abstract void onSuccess( JsonElement json );

		public void onFailure( int status, Header[] header, byte[] content, Throwable e ) {
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
	public static boolean readableJSON = false;

	public static GerritClient getInstance( Context context ) {
		GerritClient client = new GerritClient(context);

		return client;
	}

	public void get( GerritClientQuery queryBuilder, final AsyncResponseHandler handler ) {
		String subUrl = "";
		for ( String part : queryBuilder.getUrl() ) {
			subUrl += part + "/";
		}
		String url = host + (useAuthentication ? AUTH_URL_SUFFIX : "") + "/" + subUrl;

		Map<String, String> params = queryBuilder.getParams();
		boolean firtParam = true;
		for ( String key : params.keySet() ) {
			url += (firtParam ? "?" : "&") + key;
			if ( firtParam )
				firtParam = !firtParam;
			if ( params.get(key) != null ) {
				url += "=" + params.get(key);
			}
		}
		if ( !readableJSON ) {
			url += (firtParam ? "?" : "&") + "pp=0";
		}
		if ( debug )
			Log.d("GerritClient", url);

		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess( String response ) {
				if ( debug ) {
					Log.i("GerritClient", response);
				}
				response = response.substring(5);
				JsonParser jsonParser = new JsonParser();
				JsonElement jsonElement = jsonParser.parse(response);

				handler.onSuccess(jsonElement);
			}

			@Override
			public void onFailure( int status, Header[] header, byte[] content, Throwable e ) {
				handler.onFailure(status, header, content, e);
			}
		});
	}

	public void post( Context context, GerritClientQuery query, String jsonEntity,
			String contentType, final AsyncResponseHandler handler )
			throws UnsupportedEncodingException {

		String subUrl = "";
		for ( String part : query.getUrl() ) {
			subUrl += part + "/";
		}
		String url = host + (useAuthentication ? AUTH_URL_SUFFIX : "") + "/" + subUrl;
		Log.d("GerritClient", url);
		Log.d("GerritClient", "Sending JSON: " + jsonEntity);

		HttpEntity httpEntity = new StringEntity(jsonEntity);
		client.post(context, url, (HttpEntity) httpEntity, contentType,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess( String response ) {
						if ( debug ) {
							Log.i("GerritClient", response);
						}
						response = response.substring(5);
						JsonParser jsonParser = new JsonParser();
						JsonElement jsonElement = jsonParser.parse(response);

						handler.onSuccess(jsonElement);
					}

					@Override
					public void onFailure( int status, Header[] header, byte[] content, Throwable e ) {
						handler.onFailure(status, header, content, e);
					}
				});

	}
}
