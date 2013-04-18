package com.delin.speedlogger.Activities;

import com.delin.speedlogger.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class DevPrefsActivity extends PreferenceActivity {
	private final String prefsName = "devPrefs";

	@Override
	public void onCreate(Bundle savedInstanceState){
	    super.onCreate(savedInstanceState);
	    getPreferenceManager().setSharedPreferencesName(prefsName);
	    addPreferencesFromResource(R.xml.dev_preferences);
	}

}
