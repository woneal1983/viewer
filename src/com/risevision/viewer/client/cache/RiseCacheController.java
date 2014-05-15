package com.risevision.viewer.client.cache;

import com.google.gwt.http.client.URL;
import com.risevision.common.client.utils.RiseUtils;

public class RiseCacheController {
//	private static RiseCacheUtils instance;
	private static boolean isActive = false; 
	
//	public RiseCacheUtils() {
//
//	}
	
//	public static RiseCacheUtils getInstance() {
//		if (instance == null) {
//			instance = new RiseCacheUtils();
//		}
//		return instance;
//	}
	
	public static String getCacheVideoUrl(String url, String extension) {
		if (!isActive) {
			return url;
		}
		
		if (url.toLowerCase().contains("youtube.com")) {
			return url;
		}
		
		if (RiseUtils.strIsNullOrEmpty(extension)) {
			extension = url.substring(url.lastIndexOf(".") + 1);
		}
		
		String response = "http://localhost:9494/video." + extension;
		response += "?url=" + URL.encodeQueryString(url);
//		response += "?url=" + URL.encodeQueryString(url.replace(" ", "+"));
		
		return response;
	}
	
//	public static String getCacheUrl(String url) {
//		if (!isActive) {
//			return url;
//		}
//		
//		String response = "http://localhost:9494/";
//		response += "?url=" + URL.encodeQueryString(url.replace(" ", "+"));
//		
//		return response;
//	}
	
	public static void pingCache() {
		if (!isActive) {
			String url = "http://localhost:9494/ping?callback=?";
			pingCacheNative(url);
		}
	}
	
	private static void pingResponseStatic() {
		isActive = true;
	}
	
	private static native void pingCacheNative(String url) /*-{
	    try {
	    	$wnd.writeToLog("Rise Cache ping request - start");
	    	
			$wnd.$.getJSON(url,
				{
					format: 'json'
				},
				function() {
		    	    try { 
//		    	    	debugger;
		    	    	
			        	$wnd.writeToLog("Rise Cache ping response - active");
		    	    	
		    	    	@com.risevision.viewer.client.cache.RiseCacheController::pingResponseStatic()();
		    	    }
		    	    catch (err) {
		    	    	$wnd.writeToLog("Rise Cache ping failed - " + url + " - " + err.message);
		    	    }
				}
			);
	    }
	    catch (err) {
	    	$wnd.writeToLog("Rise Cache ping error - " + url + " - " + err.message);
	    }
   	}-*/;

}
