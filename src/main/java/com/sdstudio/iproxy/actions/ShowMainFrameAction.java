package com.sdstudio.iproxy.actions;

import java.awt.event.ActionEvent;

import org.springframework.stereotype.Component;

import com.sdstudio.iproxy.Utils;

@Component("ShowMainFrameAction")
public class ShowMainFrameAction extends BaseAction {
	private static final long serialVersionUID = 5239703554711702641L;

	public void actionPerformed(ActionEvent e) {
		Utils.getMainFrame().setVisible(true);
	}

	@Override
	protected String getLabelCode() {
		return "mainframe.show";
	}
}
