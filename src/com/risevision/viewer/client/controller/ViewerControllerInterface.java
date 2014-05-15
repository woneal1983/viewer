package com.risevision.viewer.client.controller;

import com.risevision.common.client.info.PlaylistItemInfo;
import com.risevision.common.client.info.PresentationInfo;

public interface ViewerControllerInterface {	
	public void updateHtml(PresentationInfo presentation);

	public void setReady(String presFrame, boolean canPlay, boolean canStop, boolean canPause, boolean canReportReady, boolean canReportDone);	

	public void setError(String presFrame, String reason);
	
	public void setDone();
	
	public void play(boolean show);
	
	public void stop(boolean hide);
	
	public void pause(boolean hide);
	
	public void setReady(boolean isReady);
	
	public boolean isReady();
	
	public PlaylistItemInfo getItem();
}
