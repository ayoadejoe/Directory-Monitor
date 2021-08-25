package folderMonitor;

import java.util.prefs.Preferences;

public class Settings {
	
	private Preferences prefs;
	private String dirID = "directories";
	private String toEmail, domain, fromEmail, fromPassword;
	private String directories;
	
	
	public Settings() {
		
		System.out.println("Parent:"+this.getClass().getName());
		prefs = Preferences.userRoot().node(this.getClass().getName());
		directories = prefs.get(dirID, "Home"); 
		
		toEmail = prefs.get("toeml", "admin@xxxxx.com"); 
		domain = prefs.get("domID", "mail.xxxxx.com"); 
		fromEmail = prefs.get("fromID", "admin@xxxxx.com");
		fromPassword = prefs.get("pwdID", "xyz"); 
		
	}
	
	
	void updateEmailSettings(String toEmail, String domain, String fromEmail, String fromPassword) {
		prefs.put("toeml", toEmail); 
		prefs.put("domID", domain); 
		prefs.put("fromID", fromEmail);
		prefs.put("pwdID", fromPassword); 
	}
	
	void updateHomeSettings(String directory) {
		prefs.put(dirID, directory);
	}


	public Preferences getPrefs() {
		return prefs;
	}


	public String getToEmail() {
		return toEmail;
	}


	public String getDomain() {
		return domain;
	}


	public String getFromEmail() {
		return fromEmail;
	}


	public String getFromPassword() {
		return fromPassword;
	}


	public String getDirectories() {
		return directories;
	}
	
	

}
