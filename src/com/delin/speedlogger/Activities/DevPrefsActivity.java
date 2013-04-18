package com.delin.speedlogger.Activities;

import com.delin.speedlogger.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class DevPrefsActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState){
	    super.onCreate(savedInstanceState);
	    String prefsName = getString(R.string.DevPrefs);
	    getPreferenceManager().setSharedPreferencesName(prefsName);
	    addPreferencesFromResource(R.xml.dev_preferences);
	}

}
