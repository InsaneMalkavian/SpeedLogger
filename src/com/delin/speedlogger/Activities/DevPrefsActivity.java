package com.delin.speedlogger.Activities;

import java.io.File;

import com.delin.speedlogger.R;
import com.delin.speedlogger.Utils.StorageProxy;

import android.os.Bundle;
import android.os.Environment;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

public class DevPrefsActivity extends PreferenceActivity {
	ListPreference gpxFiles;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
	    super.onCreate(savedInstanceState);
	    String prefsName = getString(R.string.DevPrefs);
	    getPreferenceManager().setSharedPreferencesName(prefsName);
	    addPreferencesFromResource(R.xml.dev_preferences);
	    gpxFiles = (ListPreference) findPreference(getString(R.string.FileWithGPS));
	    FindGPX();
	}
	
	// adds all filenames from GPS folder into the gpx list
	private void FindGPX() {
		File dir = new File(StorageProxy.GetInstance().GetFileGPSDir());
		String[] filenames = dir.list();
		if (filenames!=null && filenames.length > 0) {
			gpxFiles.setEntries(filenames.clone()); // avoid sharing
			for(int i=0; i<filenames.length; ++i) {
				filenames[i] = StorageProxy.FILE_GPS + File.separator + filenames[i];
			}
			gpxFiles.setEntryValues(filenames);
			if (gpxFiles.getValue() == null) gpxFiles.setValueIndex(0);
		}
		else {
			gpxFiles.setEnabled(false);
		}
	}
}