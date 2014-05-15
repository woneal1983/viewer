package com.risevision.viewer.client.widgets.oem;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.viewer.client.ViewerEntryPoint;
import com.risevision.viewer.client.player.RisePlayerController;

public class DisplayRegisterBaseWidget extends PopupPanel {
		
	protected final String MESSAGE_NOT_VERIFIED = "Not Verified";
	
	private AbsolutePanel outerPanel = new AbsolutePanel();
	protected VerticalPanel innerPanel = new VerticalPanel();
	private Label countdownLabel = new Label("Presentation will begin in 30 seconds.");

	private int countdownSeconds;
	private Timer timer;

	protected Boolean showCountdown = false;
	protected boolean hasParent = true;
	
	public DisplayRegisterBaseWidget(boolean showCountdown) {
		super(false, true);
		
		this.showCountdown = showCountdown;

		styleControls();

		add(outerPanel);		
		
		innerPanel.add(new HTML("<span style='line-height:16px;'>&nbsp;</span>"));
		
		if (showCountdown)
			innerPanel.add(countdownLabel);

		outerPanel.add(innerPanel, 0, 7);
				
		initActions();
	}
	
	private void styleControls() {
		
		outerPanel.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		
		innerPanel.getElement().getStyle().setPadding(10, Unit.PX);
		innerPanel.setSize("400px", "240px");
		innerPanel.addStyleName("inner-border");
		innerPanel.addStyleName("gradient-overlay-middle");
		innerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);		

		setSize("402px", "250px");
		
		addStyleName("rounded-border");
		addStyleName("gradient-overlay-up");

		getElement().getStyle().setProperty("zIndex", "999");

		countdownLabel.getElement().getStyle().setFontSize(16, Unit.PX);
		countdownLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);

	}
	
	private void initActions() {
	}
	
//	public static DisplayRegisterBaseWidget getInstance(Boolean showCountdown) {
//		try {
//			if (instance == null)
//				instance = new DisplayRegisterBaseWidget(showCountdown);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return instance;
//	}

	public void show() {
		show(true);
	}

	public void show(boolean hasParent) {
		super.show();
		super.setPopupPosition((int)(Window.getClientWidth()/2 - 200), 100);
		
		this.hasParent = hasParent;
		
		if (showCountdown) {
			startCountdownTimer();
			countdownLabel.setVisible(true);
		}
	}
	
	protected void startCountdownTimer() {
		timer = new Timer() {
			@Override
			public void run() {
				countdownSeconds -=1;
				countdownLabel.setText("Presentation will begin in " + countdownSeconds + " seconds.");
				if (countdownSeconds == 0) {
					closeAndStartPresentation();
				}
					
			}
		};
		
		countdownSeconds = 30;
		timer.scheduleRepeating(1000);
	}
	
	protected void stopCountdownTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
			countdownLabel.setVisible(false);
		}
	}

	protected void closeAndStartPresentation() {
		stopCountdownTimer();
		hide();
		ViewerEntryPoint.loadPresentation();
	}

	protected void closeAndRestartViewer() {
		stopCountdownTimer();
		//hide();
		RisePlayerController.restart();
		//TODO: show error if Player is not responding
	}

}
