package com.sdstudio.iproxy.actions;

import javax.annotation.PostConstruct;
import javax.swing.AbstractAction;

import org.springframework.beans.factory.annotation.Autowired;

import com.sdstudio.iproxy.core.MessageSupport;

public abstract class BaseAction extends AbstractAction {
	private static final long serialVersionUID = -5710263350702525640L;
	private MessageSupport messageSupport;

	public MessageSupport getMessageSupport() {
		return messageSupport;
	}

	@Autowired
	public void setMessageSupport(MessageSupport messageSupport) {
		this.messageSupport = messageSupport;
	}

	protected abstract String getLabelCode();

	@PostConstruct
	public void updateLabel() {
		putValue(NAME, getMessageSupport().getMessage(getLabelCode()));
	}
}
