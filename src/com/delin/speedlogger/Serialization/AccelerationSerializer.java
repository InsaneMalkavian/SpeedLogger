package com.delin.speedlogger.Serialization;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.delin.speedlogger.Sensors.AccelerationProcessor;
import com.delin.speedlogger.Sensors.AccelerationProcessor.AccelerationEvent;
import com.delin.speedlogger.Utils.StorageProxy;

public class AccelerationSerializer extends Serializer {
	
	static final String FILE_EXTENSION = 	".acc";
	
	public AccelerationSerializer(AccelerationProcessor accel) {
		String filename;
		SimpleDateFormat dateFormat = new SimpleDateFormat(TIMEPATTERN_FILE);
		filename = StorageProxy.GetInstance().GetWorkingDir()+"/"+dateFormat.format(new Date())+FILE_EXTENSION;

		PrintStream printStream = null;

		try {
		    printStream = new PrintStream(new FileOutputStream(new File(filename)));
		    final List<AccelerationEvent> list = accel.GetEvents();
		    for (int i = 0; i < list.size(); i++) {
			    printStream.print(list.get(i).values[0]);
			    printStream.print(" ");
			    printStream.print(list.get(i).values[1]);
			    printStream.print(" ");
			    printStream.print(list.get(i).values[2]);
			    printStream.print(" ");
			    printStream.print(list.get(i).time);
			    printStream.print(" ");
			    printStream.println();
		    }
		}
		catch(Exception e) {
		    e.printStackTrace();
		}
		finally {
		    if(printStream != null) {
		        printStream.close();
		    }
		}
	}
}
