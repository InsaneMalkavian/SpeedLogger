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
	static final String SPEED = 			"speed";
	
	boolean mStopped=false;
	
	String mFilename = null;	
	FileWriter mWriter = null;
	File mFile = null;
	DocumentBuilderFactory docFactory = null;
	DocumentBuilder docBuilder = null;
	
	Document mDoc = null;
	Element mRootElement = null;
	Element mTrack = null;
	Element mTrackSegment = null;
	
	public GPXSerializer(String filename) {
		mFilename = filename;
		docFactory = DocumentBuilderFactory.newInstance();
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Attr attr;
		// root element
		mDoc = docBuilder.newDocument();
		mRootElement = mDoc.createElement(GPX_STR);
		mDoc.appendChild(mRootElement);
		
		attr = mDoc.createAttribute(VERSION_STR);
		attr.setValue(VERSION_VALUE);
		mRootElement.setAttributeNode(attr);
		attr = mDoc.createAttribute(CREATOR_STR);
		attr.setValue(CREATOR_VALUE);
		mRootElement.setAttributeNode(attr);
		
		// no metadata here
		
		// start new Track
		NewTrack();
	}
	
	public void AddFix(Location loc) {
		if (mDoc==null || mTrackSegment==null) {
			return; // we got a problem
		}
		Attr attr=null;
		Element secondary;
		Element point = mDoc.createElement(TRKPOINT_STR); // point
		
		// add lat/lon
		attr = mDoc.createAttribute(LATITUDE);
		attr.setValue(Double.toString(loc.getLatitude()));
		point.setAttributeNode(attr);
		attr = mDoc.createAttribute(LONGITUDE);
		attr.setValue(Double.toString(loc.getLongitude()));
		point.setAttributeNode(attr);
		
		if (loc.hasAltitude()) { // add altitude if available
			secondary = mDoc.createElement(ALTITUDE);
			secondary.setNodeValue(Double.toString(loc.getAltitude()));
			point.appendChild(secondary);
		}
		if (loc.hasSpeed()) { // add speed if available
			secondary = mDoc.createElement(SPEED);
			secondary.setNodeValue(Float.toString(loc.getSpeed()));
			point.appendChild(secondary);
		}
		mTrackSegment.appendChild(point); // attach point to segment
	}
	
	public void NewSegment() {
		if (mDoc==null || mTrack==null) {
			return; // we got a problem
		}
		mTrackSegment = mDoc.createElement(TRKSEG_STR);
		mTrack.appendChild(mTrackSegment);
	}
	
	public void NewTrack() {
		if (mDoc==null || mRootElement==null) {
			return; // we got a problem
		}
		mTrack = mDoc.createElement(TRACK_STR);
		mRootElement.appendChild(mTrack);
		NewSegment();
	}
	
	public void Stop() {
		if (!mStopped) {
			mStopped=true;
			// TODO: do all file operations here	
			// write the content into xml file
			mFile = new File(mFilename);
			try {
				// if file doesn't exists, then create it
				if (!mFile.exists()) {
					mFile.createNewFile();
				}
			}
			catch(Exception e) {
				return;
			}
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
