// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.viewer.client.widgets;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class ViewerNotificationsWidget extends PopupPanel {
	private static ViewerNotificationsWidget instance;
	
	private AbsolutePanel contentPanel = new AbsolutePanel();
	private HTML gradientOverlay = new HTML();
	private Label messageLabel = new Label();
	
	// used as an inline element to re-size the panel to the size of the text
	// if we use the messageLabel as inline (relative positioned) the overlay element will be on top
	private Label dummyMessageLabel = new Label();
	
	public ViewerNotificationsWidget() {
		super(true, true);
		
		add(contentPanel);
		
		contentPanel.add(gradientOverlay, -10, -10);
		contentPanel.add(messageLabel, 0, 0);
		contentPanel.add(dummyMessageLabel);
				
		styleControls();
	}
	
	private void styleControls() {
		setSize("600px", "100%");

		this.getElement().addClassName("rounded-border");
		this.getElement().addClassName("header-style");
		this.getElement().getStyle().setBackgroundColor("#065ac0");
		this.getElement().getStyle().setProperty("zIndex", "999");
		
		contentPanel.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		
		gradientOverlay.setSize("600px", "100%");
		gradientOverlay.getElement().addClassName("rounded-border");
		gradientOverlay.getElement().addClassName("gradient-overlay-up");
		
		messageLabel.getElement().getStyle().setFontSize(16, Unit.PX);
		messageLabel.getElement().getStyle().setColor("#ffffff");
		messageLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		messageLabel.setWidth("100%");
		messageLabel.getElement().getStyle().setProperty("textAlign", "center");
		
		dummyMessageLabel.getElement().getStyle().setFontSize(16, Unit.PX);
		dummyMessageLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		dummyMessageLabel.getElement().getStyle().setVisibility(Visibility.HIDDEN);
	}
	
	public static ViewerNotificationsWidget getInstance() {
		try {
			if (instance == null)
				instance = new ViewerNotificationsWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

	public void show(String message) {
		show(message, false);
	}
	
	public void show(String message, boolean persist) {
		super.show();
		super.setPopupPosition((int)(Window.getClientWidth()/2 - 300), 120);
		
		messageLabel.setText(message);
		dummyMessageLabel.setText(message);
		
		setAutoHideEnabled(!persist);
		if (!persist) {
			Timer t = new Timer() {
				@Override
				public void run() {
					hide();
				}
			};
			t.schedule(45 * 1000);
		}
	}
	
}
