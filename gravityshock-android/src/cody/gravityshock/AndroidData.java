package cody.gravityshock;

import android.app.Activity;
import android.content.SharedPreferences;

public class AndroidData implements PersistantData {
	SharedPreferences prefs;
	public AndroidData(Activity a) {
		prefs = a.getPreferences(0);
	}
	@Override
	public void SaveString(String key, String value) {
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putString(key, value);
		editor.commit();
	}

	@Override
	public String LoadString(String key) {
		return prefs.getString(key, null);
	}

}
