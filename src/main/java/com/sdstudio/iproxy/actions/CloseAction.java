package com.sdstudio.iproxy.actions;

import java.awt.event.ActionEvent;

import org.springframework.stereotype.Component;

import com.sdstudio.iproxy.Utils;

@Component("CloseAction")
public class CloseAction extends BaseAction {

	private static final long serialVersionUID = 8535758871594508411L;

	public void actionPerformed(ActionEvent e) {
		Utils.getMainFrame().dispose();
	}

	@Override
	protected String getLabelCode() {
		return "close";
	}

}
