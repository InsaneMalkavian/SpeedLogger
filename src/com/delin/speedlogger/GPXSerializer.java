package com.delin.speedlogger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.annotation.TargetApi;
import android.location.Location;

public class GPXSerializer {
	static final String GPX_STR =			"gpx";
	static final String VERSION_STR =		"version";
	static final String VERSION_VALUE =		"1.1";
	static final String CREATOR_STR =		"creator";
	static final String CREATOR_VALUE =		"SpeedLogger";
	static final String METADATA_STR =		"metadata";
	static final String TRACK_STR = 		"trk";
	static final String TRKSEG_STR = 		"trkseg";
	static final String NAME_STR =			"name";
	static final String TRKPOINT_STR = 		"trkpt";
	static final String LATITUDE = 			"lat";
	static final String LONGITUDE = 		"lon";
	static final String ALTITUDE = 			"ele";
	
	boolean mStopped=false;
	
	FileWriter mWriter = null;
	File mFile = null;
	DocumentBuilderFactory docFactory = null;
	DocumentBuilder docBuilder = null;
	
	Document mDoc = null;
	Element mTrack = null;
	Element mTrackSegment = null;
	
	@TargetApi(8)
	public GPXSerializer(String filename) {
		docFactory = DocumentBuilderFactory.newInstance();
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}		

		mFile = new File(filename);
		try {
			// if file doesnt exists, then create it
			if (!mFile.exists()) {
				mFile.createNewFile();
			}
		}
		catch(Exception e) {			
		}
		Attr attr;
		// root element
		mDoc = docBuilder.newDocument();
		Element rootElement = mDoc.createElement(GPX_STR);
		mDoc.appendChild(rootElement);
		
		attr = mDoc.createAttribute(VERSION_STR);
		attr.setValue(VERSION_VALUE);
		rootElement.setAttributeNode(attr);
		attr = mDoc.createAttribute(CREATOR_STR);
		attr.setValue(CREATOR_VALUE);
		rootElement.setAttributeNode(attr);
		
		// no metadata here
		
		// start new Track and new Track segment
		mTrack = mDoc.createElement(TRACK_STR);
		rootElement.appendChild(mTrack);
		mTrackSegment = mDoc.createElement(TRKSEG_STR);
		mTrack.appendChild(mTrackSegment);
	}
	
	public void AddFix(Location loc) {
		Attr attr;
		Element secondary;
		Element point = mDoc.createElement(TRKPOINT_STR);
		attr = mDoc.createAttribute(LATITUDE);
		attr.setValue(Double.toString(loc.getLatitude()));
		point.setAttributeNode(attr);
		attr = mDoc.createAttribute(LONGITUDE);
		attr.setValue(Double.toString(loc.getLongitude()));
		point.setAttributeNode(attr);
		if (loc.hasAltitude()) {
			secondary = mDoc.createElement(ALTITUDE);
			secondary.setNodeValue(Double.toString(loc.getAltitude()));
			point.appendChild(secondary);
		}
		mTrackSegment.appendChild(point);
	}
	
	public void Stop() {
		if (!mStopped) {
			mStopped=true;
			// TODO: do all file operations here	
			// write the content into xml file
			try {
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				DOMSource source = new DOMSource(mDoc);
				StreamResult result = new StreamResult(mFile);
	
				// Output to console for testing
				// StreamResult result = new StreamResult(System.out);
				transformer.transform(source, result);
				System.out.println("File saved!");
			}
			catch(TransformerException tfe) {
				tfe.printStackTrace();
			}
		}
	}
	
	protected void finalize () {
		Stop();
	}
}
