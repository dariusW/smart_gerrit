package pl.agh.smart_gerrit;

import pl.agh.smart_gerrit.changes.ChangesViewFragment;
import pl.agh.smart_gerrit.changes.ChangesViewFragment.Type;
import pl.agh.smart_gerrit.projects.ProjectsViewFragment;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeViewActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private CharSequence mSubtitle;

	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_view);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if ((prefs.getString("host", "")).equals("")) {
			Toast.makeText(this, "Gerrit host URL not set! Please fell propper field in settings", Toast.LENGTH_LONG).show();
			Intent i = new Intent(this, SettingActivity.class);
			startActivityForResult(i, 0);
		} else if (!isNetworkAvailable()) {
			Toast.makeText(this, "Network not avaalable! Application wont work", Toast.LENGTH_LONG).show();
		}

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int id = extras.getInt(NotificationService.ARG_NOTIFICATION_ID);
			if (id==NotificationService.CR_NOTIFY) {
				NotificationManager myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				myNotificationManager.cancel(id);
				onNavigationDrawerItemSelected(3);
			}
		}

	}

	public boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		// if no network is available networkInfo will be null
		// otherwise check if we are connected
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}

	private int currentPosition = 0;

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		setCurrentPosition(position);
		if (position == 0) {
			fragmentManager.beginTransaction().replace(R.id.container, ProjectsViewFragment.newInstance()).commit();
		} else if (position == 1) {
			fragmentManager.beginTransaction().replace(R.id.container, ChangesViewFragment.newInstance()).commit();
		} else if (position == 2) {
			fragmentManager.beginTransaction().replace(R.id.container, ChangesViewFragment.newInstance(ChangesViewFragment.Type.OUT)).commit();
		} else if (position == 3) {
			fragmentManager.beginTransaction().replace(R.id.container, ChangesViewFragment.newInstance(ChangesViewFragment.Type.IN)).commit();
		} else {
			fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(666)).commit();
		}
	}

	private void onSearchQuerySubmited(String query) {
		FragmentManager fragmentManager = getFragmentManager();
		if (searchItem != null)
			searchItem.collapseActionView();
		if (getCurrentPosition() == 0) {
			fragmentManager.beginTransaction().replace(R.id.container, ProjectsViewFragment.newInstance(query)).commit();
		} else if (getCurrentPosition() == 1) {
			fragmentManager.beginTransaction().replace(R.id.container, ChangesViewFragment.newInstance(query)).commit();
		} else if (getCurrentPosition() == 2) {
			fragmentManager.beginTransaction().replace(R.id.container, ChangesViewFragment.newInstance(query, Type.OUT)).commit();
		} else if (getCurrentPosition() == 3) {
			fragmentManager.beginTransaction().replace(R.id.container, ChangesViewFragment.newInstance(query, Type.IN)).commit();
		}
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		case 4:
			mTitle = getString(R.string.title_section4);
			break;
		}
		getActionBar().setSubtitle("");
		getActionBar().setTitle(mTitle);
		getActionBar().setDisplayShowTitleEnabled(true);
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setTitle(mTitle);
		actionBar.setSubtitle(mSubtitle);
		actionBar.setDisplayShowTitleEnabled(true);
	}

	private SearchView mSearchView;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.home_view, menu);
			restoreActionBar();

			searchItem = menu.findItem(R.id.action_search);
			mSearchView = (SearchView) searchItem.getActionView();
			mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

				@Override
				public boolean onQueryTextSubmit(String query) {
					onSearchQuerySubmited(query);
					return true;
				}

				@Override
				public boolean onQueryTextChange(String newText) {

					return false;
				}
			});

			killItem = menu.findItem(R.id.action_kill);
			killItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem item) {
					Intent i = new Intent(HomeViewActivity.this, NotificationService.class);
					stopService(i);
					return true;
				}
			});

			return true;

		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent i = new Intent(this, SettingActivity.class);
			startActivityForResult(i, 0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	private MenuItem searchItem;
	private MenuItem killItem;

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_home_view, container, false);
			TextView textView = (TextView) rootView.findViewById(R.id.section_label);
			textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((HomeViewActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
		}
	}

}
