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

package org.digitalcampus.oppia.model;

import java.util.HashMap;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {

	private int userid;
	private String username;
	private String email;
	private String password;
	private String passwordAgain;
	private String passwordEncrypted;
	private String firstname;
	private String lastname;
	private String apiKey;
	private HashMap<String,String> extraData = new HashMap<String,String>();
	
	private boolean scoringEnabled = true;
	private boolean badgingEnabled = true;
	private int points = 0;
	private int badges = 0;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswordAgain() {
		return passwordAgain;
	}
	public void setPasswordAgain(String passwordAgain) {
		this.passwordAgain = passwordAgain;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getDisplayName() {
		return firstname + " " + lastname;
	}

	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public int getBadges() {
		return badges;
	}
	public void setBadges(int badges) {
		this.badges = badges;
	}
	public boolean isScoringEnabled() {
		return scoringEnabled;
	}
	public void setScoringEnabled(boolean scoringEnabled) {
		this.scoringEnabled = scoringEnabled;
	}
	

	public void addExtraData(String key, String value){
		this.extraData.put(key, value);
	}
	
	public boolean isBadgingEnabled() {
		return badgingEnabled;
	}
	
	public void setBadgingEnabled(boolean badgingEnabled) {
		this.badgingEnabled = badgingEnabled;
	}
	
	public String getPasswordEncrypted() {
		return this.passwordEncrypted;
	}
	
	public void setPasswordEncrypted() {
		try {
			byte[] bytesOfMessage = this.password.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("SHA1");
			this.passwordEncrypted = md.digest(bytesOfMessage).toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getExtraData(String key){
		if (extraData.containsKey(key)){
			return this.extraData.get(key);
		} else {
			return null;
		}
	}
	
	public HashMap<String,String> getExtraData(){
		return this.extraData;
	}
}
