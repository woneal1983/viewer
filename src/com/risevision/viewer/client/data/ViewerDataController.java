package com.risevision.viewer.client.data;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.risevision.common.client.json.JSOModel;
import com.risevision.core.api.types.ViewerStatus;
import com.risevision.viewer.client.ViewerEntryPoint;
import com.risevision.viewer.client.channel.ChannelConnectionController;
import com.risevision.viewer.client.info.ViewerDataInfo;
import com.risevision.viewer.client.player.RisePlayerController;
import com.risevision.viewer.client.widgets.ViewerNotificationsWidget;
import com.risevision.viewer.client.widgets.oem.DisplayRegisterWidget;

public class ViewerDataController extends ViewerDataControllerBase {

	private static final int INITIAL_STATE = 0;
//	private static final int CONTENT_STATE = 1;
//	private static final int POLLING_STATE = 2;
	private static final int BLOCKED_STATE = 3;
	private static final int CLOSED_STATE = 4;
	
	private static int state = INITIAL_STATE;
	
	private static Timer pollingTimer;
	private static boolean pollingTimerActive = false;
	
	public static int MINUTE_UPDATE_INTERVAL = 60 * 1000;

	private static Command channelCommand;
	
	public static void init(Command newDataReadyCommand, String type, String id) {
		dataReadyCommand = newDataReadyCommand;
		initObjects();
	
		if (ViewerEntryPoint.isEmbed()) {
			reportDataReady(getEmbedDataNative(ViewerEntryPoint.getId(), ViewerEntryPoint.getParentId()));
		}
//		else if (type.equals(ViewerEntryPoint.PREVIEW)) {
//			getPreviewDataNative();
//		}
		else {
			// [AD] moved this function to after the data is retrieved - workaround for Core bug 
			// where the Viewer API and Channel Token calls can't be made at the same time 
//			channelController.init(channelCommand);
			
			if (ViewerEntryPoint.isDisplay() && !ViewerEntryPoint.isEmbed()) {
				ViewerInstanceController.init();
			}
			
			ViewerDataProvider.retrieveData();
		}
	}
	
	private static void initObjects() {
		pollingTimer = new Timer() {
			@Override
			public void run() {
				// if the timer is running, it means the channel is disconnected.
//				channelController.connectChannel(ChannelConnectionController.REASON_RECONNECT);
				
				// reset BLOCKED_STATE
				if (isBlocked()) {
					state = INITIAL_STATE;
				}
				
				pollingTimerActive = false;
				
				ViewerDataProvider.retrieveData();
			}
		};
		
		channelCommand = new Command() {
			@Override
			public void execute() {
				onChannelCommand();
			}
		};
	}
	
	public static void setDuplicateInstance() {
		state = CLOSED_STATE;

		ChannelConnectionController.connectionCancelled();
		ViewerNotificationsWidget.getInstance().show("Multiple Display Instances found in this Browser session.", true);
	}
	
	private static void onChannelCommand() {
		// if channel connected, stop the update timer
//		if (ChannelConnectionController.updateRequired()) {
//			ViewerDataProvider.retrieveData();
//		}
//		else 
		if (viewerData != null && !ChannelConnectionController.isInactive()) {
			stopPolling();
		}
		else if (!isBlocked()) {
			startPolling(0);
//			retrieveData();
		}

	}
	
	public static void reportDataReady(JavaScriptObject jso) {
		reportDataReady(jso, false);
	}
	
	private static void reportDataReady(JavaScriptObject jso, boolean cached) {
		JSOModel jsoModel = (JSOModel) jso;

		ViewerDataInfo newViewerData = dataParser.populateDataProvider(jsoModel);
		
		if (newViewerData != null) {
			if (cached) {
				newViewerData.setContentDescriptor("cached");
			}
			setNewViewerData(newViewerData);
		}
	}
	
	@SuppressWarnings("deprecation")
	private static void setNewViewerData(ViewerDataInfo newViewerData) {
		if (state == CLOSED_STATE) {
			return;
		}
		
//		boolean pollForUpdate = false;
		stopPolling();
		
		switch (newViewerData.getStatusCode()) {
//		case ViewerStatus.NO_CHANGES:

		case ViewerStatus.OK:
//			state = CONTENT_STATE;

			if (!ViewerEntryPoint.isEmbed()) {
				ChannelConnectionController.init(channelCommand);
			}
			
			ViewerNotificationsWidget.getInstance().hide();
			break;
		case ViewerStatus.BLOCKED:
			state = BLOCKED_STATE;

			startPolling(ViewerDataParser.getInstance().getBlockRemaining());
			ViewerNotificationsWidget.getInstance().show(newViewerData.getStatusMessage(), viewerData == null);
			
			break;
		case ViewerStatus.NO_COOKIE:
		case ViewerStatus.ID_SHARING_VIOLATION:
		case ViewerStatus.CONTENT_NOT_FOUND:
			state = CLOSED_STATE;

			ChannelConnectionController.connectionCancelled();
			ViewerNotificationsWidget.getInstance().show(newViewerData.getStatusMessage(), viewerData == null);
			
			break;
		// TODO: Deprecated, please remove
		case ViewerStatus.UPDATE_INTERVAL_VIOLATION:
//			pollForUpdate = true;
			ViewerDataProvider.retrieveData();
			break;
		case ViewerStatus.DISPLAY_NOT_FOUND:
			state = CLOSED_STATE;

			if (RisePlayerController.getIsActive() && ViewerEntryPoint.isDisplay() && !ViewerEntryPoint.isEmbed()) {
				ChannelConnectionController.connectionCancelled();
				DisplayRegisterWidget.getInstance(false, true).show();
				break;
			}
		default:
			break;
		}
		
		updateViewerData(newViewerData);
		
//		if (!pollForUpdate) {
//			ChannelConnectionController.dataUpdated();
//		}
		
		if (ViewerEntryPoint.isDisplay() && !ViewerEntryPoint.isEmbed() 
				&& ChannelConnectionController.isInactive()) {
			startPolling(0);
		}
	}
	
	private static void startPolling(int interval) {
		if (!pollingTimerActive) {
			pollingTimerActive = true;
			
			if (interval == 0) {
				interval = ViewerDataParser.getInstance().getPollInterval();
			}
			else {
				interval += 2;
			}
			
			pollingTimer.schedule(interval * MINUTE_UPDATE_INTERVAL);
		}
	}
	
	private static void stopPolling() {
		pollingTimerActive = false;
		
		pollingTimer.cancel();
	}
	
	public static boolean isBlocked() {
		return state == BLOCKED_STATE;
	}

	public static native JavaScriptObject getLocalStorageData() /*-{
		return $wnd.retreiveViewerResponse();
	}-*/;
	
	private static native JavaScriptObject getEmbedDataNative(String id, String parentId) /*-{
//		return $wnd.getParentData(id, parentId);
	
	    try {
	    	if ($wnd.parent) {
	    		return $wnd.parent.getEmbedData(id, parentId);
	    	}
	    }
	    catch (err) {
	    	$wnd.writeToLog("getParentData - " + id + " - " + err.message);
	    }

	}-*/;

}
