package pl.agh.smart_gerrit;

import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ProjectsViewFragment extends Fragment {
	 /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
	private SharedPreferences prefs;
	
	private String host;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ProjectsViewFragment newInstance(int sectionNumber) {
    	ProjectsViewFragment fragment = new ProjectsViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ProjectsViewFragment() {
    	
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
    	host = prefs.getString("host", "");
    	
    	handler.post(new GetProjectsTask());
    	
        View rootView = inflater.inflate(R.layout.fragment_home_view, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeViewActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
    
    
    private Handler handler = new Handler();
    
    class GetProjectsTask implements Runnable{

		@Override
		public void run() {
			GerritClient connection = GerritClient.getInstance(getActivity());
			connection.get("projects", new GerritClient.AsyncResponseHandler() {
				
				@Override
				public void onSuccess(Map<String, String> jsonObject) {
					
					Log.i("GetProjectsTask", jsonObject.toString());
				}
			});
			
		}
    	
    }
    
}
