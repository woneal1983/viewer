// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.viewer.client.widgets.oem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.viewer.client.ViewerEntryPoint;
import com.risevision.viewer.client.player.RisePlayerController;

public class DisplayRegisterWidget extends DisplayRegisterBaseWidget {
	
	private final String HTML_ERROR_NONE = "<div style='color:red'>&nbsp;NONE</div>";
	private final String HTML_ERROR_DUPLICATE = "<div style='color:red'>&nbsp;(DUPLICATE)</div>";
	private final String HTML_ERROR_NOT_FOUND = "<div style='color:red'>&nbsp;(NOT FOUND)</div>";
		
	private static DisplayRegisterWidget instance;
	
	private HorizontalPanel hp1 = new HorizontalPanel();
	private HorizontalPanel hpButtons1 = new HorizontalPanel();
	private HorizontalPanel hpButtons2 = new HorizontalPanel();

	private VerticalPanel vpButtons = new VerticalPanel();

	private Label duplicateDisplayLabel = new Label("Duplicate Display ID. Presentation cannot start.");
	private Label notFoundDisplayLabel = new Label("Display ID was not found. Presentation cannot start.");
	
	private Label DisplayIdLabel = new Label("Display ID =");
	private HTML DisplayIdError = new HTML(HTML_ERROR_NONE);
	
	private Button btEnterDisplayId = new DisplayRegisterButtonWidget("Enter Display ID");
	private Button btEnterClaimId = new DisplayRegisterButtonWidget("Enter Claim ID");
//	private Button btStart = new DisplayRegisterButtonWidget("Start");
	private Button btQuit = new DisplayRegisterButtonWidget("Quit");
	private Button btHelp = new DisplayRegisterButtonWidget("Help");
	
//	private boolean isDuplicate = false;
	
	public DisplayRegisterWidget(boolean isDuplicate, boolean isNotFound) {
//		super(!isDuplicate);
		super(false);

//		this.isDuplicate = isDuplicate;
		
		styleControls();

		if (isDuplicate) {
			DisplayIdLabel.setText("Display ID = " + ViewerEntryPoint.getDisplayId());
			DisplayIdError.setHTML(HTML_ERROR_DUPLICATE);
			innerPanel.add(duplicateDisplayLabel);
		}		
		else if (isNotFound) {
			DisplayIdLabel.setText("Display ID = " + ViewerEntryPoint.getDisplayId());
			DisplayIdError.setHTML(HTML_ERROR_NOT_FOUND);
			innerPanel.add(notFoundDisplayLabel);
		}		

		
		hp1.add(DisplayIdLabel);
		hp1.add(DisplayIdError);

		hpButtons1.add(btEnterDisplayId);
		hpButtons1.add(btEnterClaimId);

//		hpButtons2.add(btStart);
		hpButtons2.add(btQuit);
		hpButtons2.add(btHelp);
		
		vpButtons.add(hpButtons1);
		vpButtons.add(hpButtons2);
				
		innerPanel.add(hp1);
		innerPanel.add(vpButtons);

		initActions();
		
	}
	
	private void styleControls() {
		hpButtons1.setWidth("280px");
		hpButtons1.setSpacing(5);

		hpButtons2.setWidth("280px");
		hpButtons2.setSpacing(5);
		
	}
	
	private void initActions() {
//		btStart.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				if (!isDuplicate)
//					closeAndStartPresentation();
//			}
//		});
		btQuit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				stopCountdownTimer();
				RisePlayerController.shutdown();
			}
		});
		btEnterClaimId.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				stopCountdownTimer();
				EnterClaimIdWidget.getInstance(false).show();
			}
		});
		btEnterDisplayId.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				stopCountdownTimer();
				EnterDisplayIdWidget.getInstance().show();
			}
		});
		btHelp.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				stopCountdownTimer();
				HelpFrameWidget.getInstance().show();
			}
		});
	}
	
	public static DisplayRegisterWidget getInstance(boolean isDuplicate, boolean isNotFound) {
		try {
			if (instance == null)
				instance = new DisplayRegisterWidget(isDuplicate, isNotFound);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}

//	public void show() {
//		super.show();
//		super.setPopupPosition((int)(Window.getClientWidth()/2 - 300), 100);
//	}
	
}
