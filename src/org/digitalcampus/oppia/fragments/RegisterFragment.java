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

package org.digitalcampus.oppia.fragments;

import java.util.ArrayList;

import org.nurhi.oppia.R;
import org.digitalcampus.oppia.activity.OppiaMobileActivity;
import org.digitalcampus.oppia.application.MobileLearning;
import org.digitalcampus.oppia.listener.SubmitListener;
import org.digitalcampus.oppia.model.User;
import org.digitalcampus.oppia.task.Payload;
import org.digitalcampus.oppia.task.RegisterTask;
import org.digitalcampus.oppia.utils.UIUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.LinearLayout.LayoutParams;

public class RegisterFragment extends Fragment implements SubmitListener {

	public static final String TAG = RegisterFragment.class.getSimpleName();
	
	private SharedPreferences prefs;
	private EditText usernameField;
	private EditText emailField;
	private EditText passwordField;
	private EditText passwordAgainField;
	private EditText firstnameField;
	private EditText lastnameField;
	
	private EditText phonenoField; 
	private Spinner currentlyWorkingFacilityField;
	private EditText nurhiTrainingField;
	private Spinner staffTypeField;
	
	private LinearLayout fpMethodsProvided;
	private Spinner highestEducationLevelField;
	private Spinner religionField;
	private Spinner sexField;
	private EditText ageField;
	
	private Button registerButton;
	private ProgressDialog pDialog;
	
	public static RegisterFragment newInstance() {
		RegisterFragment myFragment = new RegisterFragment();
	    return myFragment;
	}

	public RegisterFragment(){
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		prefs = PreferenceManager.getDefaultSharedPreferences(super.getActivity());
		View vv = super.getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_register, null);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		vv.setLayoutParams(lp);
		return vv;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		usernameField = (EditText) super.getActivity().findViewById(R.id.register_form_username_field);
		emailField = (EditText) super.getActivity().findViewById(R.id.register_form_email_field);
		passwordField = (EditText) super.getActivity().findViewById(R.id.register_form_password_field);
		passwordAgainField = (EditText) super.getActivity().findViewById(R.id.register_form_password_again_field);
		firstnameField = (EditText) super.getActivity().findViewById(R.id.register_form_firstname_field);
		lastnameField = (EditText) super.getActivity().findViewById(R.id.register_form_lastname_field);

		phonenoField = (EditText) super.getActivity().findViewById(R.id.register_form_phone_no_field);
		
		currentlyWorkingFacilityField = (Spinner) super.getActivity().findViewById(R.id.currently_working_facility_spinner);
		ArrayAdapter<CharSequence> cwfadapter = ArrayAdapter.createFromResource(super.getActivity(),
		        R.array.registerFormCurrentlyWorkingFacility, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		cwfadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		currentlyWorkingFacilityField.setAdapter(cwfadapter);
		
		nurhiTrainingField = (EditText) super.getActivity().findViewById(R.id.register_form_nurhi_training_field);
		
		staffTypeField = (Spinner) super.getActivity().findViewById(R.id.staff_type_spinner);
		ArrayAdapter<CharSequence> stadapter = ArrayAdapter.createFromResource(super.getActivity(),
		        R.array.registerFormStaffType, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		stadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		staffTypeField.setAdapter(stadapter);
		
		fpMethodsProvided = (LinearLayout) super.getActivity().findViewById(R.id.register_form_fp_methods);
		fpMethodsProvided.removeAllViews();
		String[] fpMethods = getResources().getStringArray(R.array.registerFormFPMethods);
		for (String s: fpMethods){
			CheckBox chk= new CheckBox(super.getActivity());  
    		chk.setText(s);
    		fpMethodsProvided.addView(chk);
		}
		
		highestEducationLevelField = (Spinner) super.getActivity().findViewById(R.id.education_level_spinner);
		ArrayAdapter<CharSequence> heladapter = ArrayAdapter.createFromResource(super.getActivity(),
		        R.array.registerFormEducationalLevel, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		heladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		highestEducationLevelField.setAdapter(heladapter);
		
		religionField = (Spinner) super.getActivity().findViewById(R.id.religion_spinner);
		ArrayAdapter<CharSequence> radapter = ArrayAdapter.createFromResource(super.getActivity(),
		        R.array.registerFormReligion, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		radapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		religionField.setAdapter(radapter);
		
		sexField = (Spinner) super.getActivity().findViewById(R.id.sex_spinner);
		ArrayAdapter<CharSequence> sadapter = ArrayAdapter.createFromResource(super.getActivity(),
		        R.array.registerFormSex, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		sexField.setAdapter(sadapter);
		
		ageField = (EditText) super.getActivity().findViewById(R.id.register_form_age_field);
		
		registerButton = (Button) super.getActivity().findViewById(R.id.register_btn);
		registerButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				onRegisterClick(v);
			}
		});
	}

	public void submitComplete(Payload response) {
		pDialog.dismiss();
		if (response.isResult()) {
			User u = (User) response.getData().get(0);
			// set params
			Editor editor = prefs.edit();
	    	editor.putString("prefUsername", usernameField.getText().toString());
	    	editor.putString("prefApiKey", u.getApiKey());
	    	editor.putString("prefDisplayName", u.getDisplayName());
	    	editor.putInt("prefPoints", u.getPoints());
	    	editor.putInt("prefBadges", u.getBadges());
	    	editor.putBoolean("prefScoringEnabled", u.isScoringEnabled());
	    	editor.putBoolean("prefBadgingEnabled", u.isBadgingEnabled());
	    	editor.commit();
	    	
	    	startActivity(new Intent(super.getActivity(), OppiaMobileActivity.class));
	    	super.getActivity().finish();
		} else {
			try {
				JSONObject jo = new JSONObject(response.getResultResponse());
				UIUtils.showAlert(super.getActivity(),R.string.error,jo.getString("error"));
			} catch (JSONException je) {
				UIUtils.showAlert(super.getActivity(),R.string.error,response.getResultResponse());
			}
		}
	}

	public void onRegisterClick(View view) {
		// get form fields
		String username = (String) usernameField.getText().toString().trim();
		String email = (String) emailField.getText().toString();
		String password = (String) passwordField.getText().toString();
		String passwordAgain = (String) passwordAgainField.getText().toString();
		String firstname = (String) firstnameField.getText().toString();
		String lastname = (String) lastnameField.getText().toString();
		
		// do validation
		// check username
		if (username.length() == 0) {
			UIUtils.showAlert(super.getActivity(),R.string.error,R.string.error_register_no_username);
			return;
		}
			
		if (username.contains(" ")) {
			UIUtils.showAlert(super.getActivity(),R.string.error,R.string.error_register_username_spaces);
			return;
		}
		
		// TODO check valid email address format
		// android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
		if (email.length() == 0) {
			UIUtils.showAlert(super.getActivity(),R.string.error,R.string.error_register_no_email);
			return;
		}
		// check password length
		if (password.length() < MobileLearning.PASSWORD_MIN_LENGTH) {
			UIUtils.showAlert(super.getActivity(),R.string.error,getString(R.string.error_register_password,  MobileLearning.PASSWORD_MIN_LENGTH ));
			return;
		}
		// check password match
		if (!password.equals(passwordAgain)) {
			UIUtils.showAlert(super.getActivity(),R.string.error,R.string.error_register_password_no_match);
			return;
		}
		// check firstname
		if (firstname.length() < 2) {
			UIUtils.showAlert(super.getActivity(),R.string.error,R.string.error_register_no_firstname);
			return;
		}

		// check lastname
		if (lastname.length() < 2) {
			UIUtils.showAlert(super.getActivity(),R.string.error,R.string.error_register_no_lastname);
			return;
		}

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
			UIUtils.showAlert(super.getActivity(),getString(R.string.error), "Please enter a username");
			return;
		}
				
		// TODO check valid email address format
		// android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
		if (email.length() == 0) {
			UIUtils.showAlert(super.getActivity(),getString(R.string.error), "Please enter an email address");
			return;
		}
		// check password length
		if (password.length() < MobileLearning.PASSWORD_MIN_LENGTH) {
			UIUtils.showAlert(super.getActivity(),getString(R.string.error), "Your password must be "+ MobileLearning.PASSWORD_MIN_LENGTH +" or more characters");
			return;
		}
		// check password match
		if (!password.equals(passwordAgain)) {
			UIUtils.showAlert(super.getActivity(),getString(R.string.error), "Your passwords don't match");
			return;
		}
		// check firstname
		if (firstname.length() < 2) {
			UIUtils.showAlert(super.getActivity(),getString(R.string.error), "Please enter your firstname");
			return;
		}

		// check lastname
		if (lastname.length() < 2) {
			UIUtils.showAlert(super.getActivity(),getString(R.string.error), "Please enter your lastname");
			return;
		}

		// check NURHI extra data
		if(phoneNo.length()<6){
			UIUtils.showAlert(super.getActivity(),getString(R.string.error), "Please enter your phone number");
			return;
		}
		if(currentWorkingFacility.length() == 0){
			UIUtils.showAlert(super.getActivity(),getString(R.string.error), "Please enter your current working facility");
			return;
		}
		if(nurhiTraining.length() == 0){
			UIUtils.showAlert(super.getActivity(),getString(R.string.error), "Please enter your the number of NURHI sponsored training you have attended");
			return;
		}
		if(staffType.length() == 0){
			UIUtils.showAlert(super.getActivity(),getString(R.string.error), "Please enter your staff type");
			return;
		}
		if(fpMethods.length() == 0){
			UIUtils.showAlert(super.getActivity(),getString(R.string.error), "Please enter the family planning methods your facility provides.");
			return;
		}
		if(educationLevel.length() == 0){
			UIUtils.showAlert(super.getActivity(),getString(R.string.error), "Please enter your education level.");
			return;
		}
		if(religion.length() == 0){
			UIUtils.showAlert(super.getActivity(),getString(R.string.error), "Please enter your religion.");
			return;
		}
		if(sex.length() == 0){
			UIUtils.showAlert(super.getActivity(),getString(R.string.error), "Please enter your sex.");
			return;
		}
		if(age.length() == 0){
			UIUtils.showAlert(super.getActivity(),getString(R.string.error), "Please enter your age.");
			return;
		}
		
		pDialog = new ProgressDialog(super.getActivity());
		pDialog.setTitle(R.string.register_alert_title);
		pDialog.setMessage(getString(R.string.register_process));
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
		RegisterTask rt = new RegisterTask(super.getActivity());
		rt.setRegisterListener(this);
		rt.execute(p);
	}
}
