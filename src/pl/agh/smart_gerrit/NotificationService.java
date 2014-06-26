package pl.agh.smart_gerrit;

import java.util.ArrayList;
import java.util.List;

import pl.agh.smart_gerrit.changes.ChangesQueryBuilder;
import pl.agh.smart_gerrit.changes.CommitStatus;
import pl.agh.smart_gerrit.changes.model.ChangeModel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class NotificationService extends Service {
	public static final String ARG_NOTIFICATION_ID = "notificationId";
	public static final int CR_NOTIFY = 4123;
	private long mStartTime = 0L;
	private final Handler mHandler = new Handler();
	private Runnable mUpdateTimeTask;
	private volatile boolean isLoading = false;
	private GerritClient client;
	private boolean silent = true;
	private NotificationManager myNotificationManager;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		client = GerritClient.getInstance(this);
		mUpdateTimeTask = new Runnable() {
			public void run() {
				long millis = SystemClock.uptimeMillis() - mStartTime;
				int seconds = (int) (millis / 1000);
				int minutes = seconds / 60;
				seconds = seconds % 60;

				if (!isLoading) {
					isLoading = true;
					if (!silent)
						Toast.makeText(NotificationService.this, "Checking status", Toast.LENGTH_SHORT).show();
					ChangesQueryBuilder.Query params = ChangesQueryBuilder.getBuider().setStatus(CommitStatus.OPEN);
					params.setAssignedToMe();
					client.get(params, new GerritClient.AsyncResponseHandler() {

						@Override
						public void onSuccess(JsonElement json) {
							Gson gson = new Gson();
							List<ChangeModel> list = new ArrayList<ChangeModel>();
							for (JsonElement changeObject : json.getAsJsonArray()) {
								ChangeModel model = gson.fromJson(changeObject.getAsJsonObject(), ChangeModel.class);
								list.add(model);
							}
							isLoading = false;

							if (list.isEmpty() == false) {
								displayNotification();
							}

							Log.i("GetChangeTask", json.toString());
						}
					});
				}

				mHandler.postAtTime(this, mStartTime + (((minutes * 60) + seconds + 120) * 1000));
				mHandler.postDelayed(mUpdateTimeTask, 120000);
			}
		};
		mStartTime = SystemClock.uptimeMillis();
		mHandler.removeCallbacks(mUpdateTimeTask);
		mHandler.postDelayed(mUpdateTimeTask, 100);
		if (!silent)
			Toast.makeText(this, getString(R.string.service_name) + " started.", Toast.LENGTH_SHORT).show();

		// Intent notificationIntent = new Intent(this, HomeViewActivity.class);
		// Notification notification = new Notification(this,
		// R.drawable.ic_launcher, "Smart Gerrit", System.currentTimeMillis(),
		// "Smart Gerrit","There are changes waiting for review!",
		// notificationIntent);
		// PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
		// notificationIntent, 0);
		// startForeground(startId, notification);

		return Service.START_STICKY;
	}

	private void displayNotification() {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setContentTitle("New commit waiting for review!");
		mBuilder.setContentText("New commit was assigned for you to review");
		mBuilder.setTicker("New Commit!");
		mBuilder.setSmallIcon(R.drawable.ic_launcher);

		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, HomeViewActivity.class);
	      resultIntent.putExtra(ARG_NOTIFICATION_ID, CR_NOTIFY);

		// This ensures that navigating backward from the Activity leads out of
		// the app to Home page
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

		// Adds the back stack for the Intent
		stackBuilder.addParentStack(HomeViewActivity.class);

		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT // can
																											// only
																											// be
																											// used
																											// once
				);
		// start the activity when the user clicks the notification text
		mBuilder.setContentIntent(resultPendingIntent);

		myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// pass the Notification object to the system
		myNotificationManager.notify(CR_NOTIFY, mBuilder.build());
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO for communication return IBinder implementation
		return null;
	}

	@Override
	public void onDestroy() {
		mHandler.removeCallbacks(mUpdateTimeTask);
		if (!silent)
			Toast.makeText(this, getString(R.string.service_name) + " finished.", Toast.LENGTH_SHORT).show();
	}
}
