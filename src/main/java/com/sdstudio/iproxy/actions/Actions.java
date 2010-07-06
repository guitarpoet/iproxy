package com.sdstudio.iproxy.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("Actions")
public class Actions {
	private AboutUsAction aboutUsAction;
	private CloseAction closeAction;
	private EditUserInformationAction editUserInformationAction;
	private StartStopButtonAction startStopAction;
	private ShowMainFrameAction showMainFrameAction;

	public ShowMainFrameAction getShowMainFrameAction() {
		return showMainFrameAction;
	}

	@Autowired
	public void setShowMainFrameAction(ShowMainFrameAction showMainFrameAction) {
		this.showMainFrameAction = showMainFrameAction;
	}

	public AboutUsAction getAboutUsAction() {
		return aboutUsAction;
	}

	@Autowired
	public void setAboutUsAction(AboutUsAction aboutUsAction) {
		this.aboutUsAction = aboutUsAction;
	}

	public CloseAction getCloseAction() {
		return closeAction;
	}

	@Autowired
	public void setCloseAction(CloseAction closeAction) {
		this.closeAction = closeAction;
	}

	public EditUserInformationAction getEditUserInformationAction() {
		return editUserInformationAction;
	}

	@Autowired
	public void setEditUserInformationAction(
			EditUserInformationAction editUserInformationAction) {
		this.editUserInformationAction = editUserInformationAction;
	}

	public StartStopButtonAction getStartStopAction() {
		return startStopAction;
	}

	@Autowired
	public void setStartStopAction(StartStopButtonAction startStopAction) {
		this.startStopAction = startStopAction;
	}

}
