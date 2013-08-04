package com.delin.speedlogger.Serialization;

import android.os.Environment;

public class Serializer {
	static final String TIMEPATTERN = 		"yyyy-MM-dd'T'HH:mm:ss'Z'";
	static final String TIMEPATTERN_FILE = 	"yyyy-MM-dd_HH-mm-ss";
	static final String APP_NAME =		"SpeedLogger";
	static final String STORAGE_DIR = 		Environment.getExternalStorageDirectory().getPath()
			+"/"+APP_NAME;
}
