/* 
 * This file is part of OppiaMobile - http://oppia-mobile.org/
 * 
 * OppiaMobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * OppiaMobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with OppiaMobile. If not, see <http://www.gnu.org/licenses/>.
 */

package org.digitalcampus.oppia.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.digitalcampus.oppia.application.MobileLearning;
import org.digitalcampus.oppia.listener.SubmitListener;
import org.digitalcampus.oppia.model.User;
import org.digitalcampus.oppia.task.Payload;
import org.digitalcampus.oppia.task.RegisterTask;
import org.nurhi.oppia.R;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class RegisterActivity extends AppActivity implements SubmitListener {

	public static final String TAG = RegisterActivity.class.getSimpleName();

	private static final int ONCLICK_TASK_NULL = 0;
	private static final int ONCLICK_TASK_REGISTERED = 10;

	private SharedPreferences prefs;
	private EditText usernameField;
	private EditText emailField;
	private EditText passwordField;
	private EditText passwordAgainField;
	private EditText firstnameField;
	private EditText lastnameField;
	
	private EditText phonenoField; //
	private Spinner currentlyWorkingFacilityField;
	private EditText nurhiTrainingField;
	private Spinner staffTypeField;
	
	private LinearLayout fpMethodsProvided;
	private Spinner highestEducationLevelField;
	private Spinner religionField;
	private Spinner sexField;
	private EditText ageField;
    
	private ProgressDialog pDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		usernameField = (EditText) findViewById(R.id.register_form_username_field);
		emailField = (EditText) findViewById(R.id.register_form_email_field);
		passwordField = (EditText) findViewById(R.id.register_form_password_field);
		passwordAgainField = (EditText) findViewById(R.id.register_form_password_again_field);
		firstnameField = (EditText) findViewById(R.id.register_form_firstname_field);
		lastnameField = (EditText) findViewById(R.id.register_form_lastname_field);
		
		phonenoField = (EditText) findViewById(R.id.register_form_phone_no_field);
		
		currentlyWorkingFacilityField = (Spinner) findViewById(R.id.currently_working_facility_spinner);
		ArrayAdapter<CharSequence> cwfadapter = ArrayAdapter.createFromResource(this,
		        R.array.registerFormCurrentlyWorkingFacility, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		cwfadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		currentlyWorkingFacilityField.setAdapter(cwfadapter);
		
		nurhiTrainingField = (EditText) findViewById(R.id.register_form_nurhi_training_field);
		
		staffTypeField = (Spinner) findViewById(R.id.staff_type_spinner);
		ArrayAdapter<CharSequence> stadapter = ArrayAdapter.createFromResource(this,
		        R.array.registerFormStaffType, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		stadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		staffTypeField.setAdapter(stadapter);
		
		fpMethodsProvided = (LinearLayout) findViewById(R.id.register_form_fp_methods);
		fpMethodsProvided.removeAllViews();
		String[] fpMethods = getResources().getStringArray(R.array.registerFormFPMethods);
		for (String s: fpMethods){
			CheckBox chk= new CheckBox(this);  
    		chk.setText(s);
    		fpMethodsProvided.addView(chk);
		}
		
		highestEducationLevelField = (Spinner) findViewById(R.id.education_level_spinner);
		ArrayAdapter<CharSequence> heladapter = ArrayAdapter.createFromResource(this,
		        R.array.registerFormEducationalLevel, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		heladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		highestEducationLevelField.setAdapter(heladapter);
		
		religionField = (Spinner) findViewById(R.id.religion_spinner);
		ArrayAdapter<CharSequence> radapter = ArrayAdapter.createFromResource(this,
		        R.array.registerFormReligion, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		radapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		religionField.setAdapter(radapter);
		
		sexField = (Spinner) findViewById(R.id.sex_spinner);
		ArrayAdapter<CharSequence> sadapter = ArrayAdapter.createFromResource(this,
		        R.array.registerFormSex, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		sexField.setAdapter(sadapter);
		
		ageField = (EditText) findViewById(R.id.register_form_age_field);
	}

	public void submitComplete(Payload response) {
		pDialog.dismiss();
		if (response.isResult()) {
			User u = (User) response.getData().get(0);
			// set params
			Editor editor = prefs.edit();
	    	editor.putString(getString(R.string.prefs_username), usernameField.getText().toString());
	    	editor.putString(getString(R.string.prefs_api_key), u.getApi_key());
	    	editor.putString(getString(R.string.prefs_display_name), u.getDisplayName());
	    	editor.putInt(getString(R.string.prefs_points), u.getPoints());
	    	editor.putInt(getString(R.string.prefs_points), u.getBadges());
	    	editor.putBoolean(getString(R.string.prefs_scoring_enabled), u.isScoringEnabled());
	    	editor.putBoolean(getString(R.string.prefs_badging_enabled), u.isBadgingEnabled());
	    	editor.commit();

			showAlert("Register", "Registration successful", ONCLICK_TASK_REGISTERED);

		} else {
			showAlert("Register", response.getResultResponse(), ONCLICK_TASK_NULL);
		}

	}

	public void onRegisterClick(View view) {
		// get form fields
		String username = (String) usernameField.getText().toString();
		String email = (String) emailField.getText().toString();
		String password = (String) passwordField.getText().toString();
		String passwordAgain = (String) passwordAgainField.getText().toString();
		String firstname = (String) firstnameField.getText().toString();
		String lastname = (String) lastnameField.getText().toString();
		
		//extra data for NURHI		
		String phoneNo = (String) phonenoField.getText().toString(); 
		String currentWorkingFacility = currentlyWorkingFacilityField.getSelectedItem().toString();
		String nurhiTraining = (String) nurhiTrainingField.getText().toString(); 
		String staffType = staffTypeField.getSelectedItem().toString();
		String fpMethods = "";
		int count = fpMethodsProvided.getChildCount();
		for (int i=0; i<count; i++) {
			CheckBox cb = (CheckBox) fpMethodsProvided.getChildAt(i);
			if(cb.isChecked()){
				fpMethods+=cb.getText().toString()+", ";
			}
		}
		Log.d(TAG,fpMethods);
		String educationLevel = highestEducationLevelField.getSelectedItem().toString();
		String religion = religionField.getSelectedItem().toString();
		String sex = sexField.getSelectedItem().toString();
		String age = (String) ageField.getText().toString();
		
		// do validation
		// TODO change to be proper lang strings
		// check firstname
		if (username.length() == 0) {
			this.showAlert(getString(R.string.error), "Please enter a username", ONCLICK_TASK_NULL);
			return;
		}
				
		// TODO check valid email address format
		// android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
		if (email.length() == 0) {
			this.showAlert(getString(R.string.error), "Please enter an email address", ONCLICK_TASK_NULL);
			return;
		}
		// check password length
		if (password.length() < MobileLearning.PASSWORD_MIN_LENGTH) {
			this.showAlert(getString(R.string.error), "Your password must be "+ MobileLearning.PASSWORD_MIN_LENGTH +" or more characters", ONCLICK_TASK_NULL);
			return;
		}
		// check password match
		if (!password.equals(passwordAgain)) {
			this.showAlert(getString(R.string.error), "Your passwords don't match", ONCLICK_TASK_NULL);
			return;
		}
		// check firstname
		if (firstname.length() < 2) {
			this.showAlert(getString(R.string.error), "Please enter your firstname", ONCLICK_TASK_NULL);
			return;
		}

		// check lastname
		if (lastname.length() < 2) {
			this.showAlert(getString(R.string.error), "Please enter your lastname", ONCLICK_TASK_NULL);
			return;
		}

		// check NURHI extra data
		if(phoneNo.length()<6){
			this.showAlert(getString(R.string.error), "Please enter your phone number", ONCLICK_TASK_NULL);
			return;
		}
		if(currentWorkingFacility.length() == 0){
			this.showAlert(getString(R.string.error), "Please enter your current working facility", ONCLICK_TASK_NULL);
			return;
		}
		if(nurhiTraining.length() == 0){
			this.showAlert(getString(R.string.error), "Please enter your the number of NURHI sponsored training you have attended", ONCLICK_TASK_NULL);
			return;
		}
		if(staffType.length() == 0){
			this.showAlert(getString(R.string.error), "Please enter your staff type", ONCLICK_TASK_NULL);
			return;
		}
		if(fpMethods.length() == 0){
			this.showAlert(getString(R.string.error), "Please enter the family planning methods your facility provides.", ONCLICK_TASK_NULL);
			return;
		}
		if(educationLevel.length() == 0){
			this.showAlert(getString(R.string.error), "Please enter your education level.", ONCLICK_TASK_NULL);
			return;
		}
		if(religion.length() == 0){
			this.showAlert(getString(R.string.error), "Please enter your religion.", ONCLICK_TASK_NULL);
			return;
		}
		if(sex.length() == 0){
			this.showAlert(getString(R.string.error), "Please enter your sex.", ONCLICK_TASK_NULL);
			return;
		}
		if(age.length() == 0){
			this.showAlert(getString(R.string.error), "Please enter your age.", ONCLICK_TASK_NULL);
			return;
		}
		
		pDialog = new ProgressDialog(this);
		pDialog.setTitle("Register");
		pDialog.setMessage("Registering...");
		pDialog.setCancelable(true);
		pDialog.show();

		ArrayList<Object> users = new ArrayList<Object>();
    	User u = new User();
		u.setUsername(username);
		u.setPassword(password);
		u.setPasswordAgain(passwordAgain);
		u.setFirstname(firstname);
		u.setLastname(lastname);
		u.setEmail(email);
		u.addExtraData("phoneno", phoneNo);
		u.addExtraData("currently_working_facility", currentWorkingFacility);
		u.addExtraData("nurhi_sponsor_training", nurhiTraining);
		u.addExtraData("staff_type", staffType);
		u.addExtraData("fp_methods_provided", fpMethods);
		u.addExtraData("highest_education_level", educationLevel);
		u.addExtraData("religion", religion);
		u.addExtraData("sex", sex);
		u.addExtraData("age", age);
		users.add(u);
		Payload p = new Payload(users);
		RegisterTask lt = new RegisterTask(this);
		lt.setLoginListener(this);
		lt.execute(p);
	}

	private void showAlert(String title, String msg, int onClickTask) {
		AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
		builder.setTitle(title);
		builder.setMessage(msg);
		switch (onClickTask) {
			case ONCLICK_TASK_NULL:
				builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// do nothing
					}
				});
				break;
			case ONCLICK_TASK_REGISTERED :
				builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// return to main activity
						RegisterActivity.this.startActivity(new Intent(RegisterActivity.this, OppiaMobileActivity.class));
						RegisterActivity.this.finish();
					}
				});
				break;
		}
		builder.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_register, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent i = new Intent(this, PrefsActivity.class);
			startActivity(i);
			return true;
		case R.id.menu_about:
			Intent iA = new Intent(this, AboutActivity.class);
			startActivity(iA);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
