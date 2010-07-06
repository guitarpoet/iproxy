package com.sdstudio.iproxy.actions;

import java.awt.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sdstudio.iproxy.iProxy;

@Component("CloseAction")
public class CloseAction extends BaseAction {

	private static final long serialVersionUID = 8535758871594508411L;

	private iProxy iproxy;

	public iProxy getIproxy() {
		return iproxy;
	}

	@Autowired
	public void setIproxy(iProxy iproxy) {
		this.iproxy = iproxy;
	}

	public void actionPerformed(ActionEvent e) {
		getIproxy().stop();
	}

	@Override
	protected String getLabelCode() {
		return "close";
	}

}
