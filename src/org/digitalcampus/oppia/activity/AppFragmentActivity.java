package org.digitalcampus.oppia.activity;

import java.util.ArrayList;

import org.digitalcampus.mobile.learning.R;
import org.digitalcampus.oppia.application.ScheduleReminders;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class AppFragmentActivity extends SherlockFragmentActivity {
	
	public static final String TAG = AppActivity.class.getSimpleName();
	
	private ScheduleReminders reminders;

	
	/**
	 * @param activities
	 */
	public void drawReminders(ArrayList<org.digitalcampus.oppia.model.Activity> activities){
		try {
			reminders = (ScheduleReminders) findViewById(R.id.schedule_reminders);
			reminders.initSheduleReminders(activities);
		} catch (NullPointerException npe) {
			// do nothing
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case android.R.id.home:
				this.finish();
				return true;
		}
		return true;
	}

}
