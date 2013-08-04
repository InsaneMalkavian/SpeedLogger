package com.delin.speedlogger.Serialization;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.delin.speedlogger.Sensors.AccelerationProcessor;

public class AccelerationSerializer extends Serializer {
	
	static final String FILE_EXTENSION = 	".acc";
	
	public AccelerationSerializer(AccelerationProcessor accel) {
		String filename;
		SimpleDateFormat dateFormat = new SimpleDateFormat(TIMEPATTERN_FILE);
		filename = STORAGE_DIR+"/"+dateFormat.format(new Date())+FILE_EXTENSION;

		// write the content into the file
		File mFile = new File(filename);
		File mDir = mFile.getParentFile();
		try {
			// if file doesn't exists, then create it
			if(!mDir.exists()) mDir.mkdirs(); // create all needed dirs
			if (!mFile.exists()) mFile.createNewFile();
		}
		catch(Exception e) {
			return;
		}
		try {
			//StreamResult result = new StreamResult(mFile);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}
}
