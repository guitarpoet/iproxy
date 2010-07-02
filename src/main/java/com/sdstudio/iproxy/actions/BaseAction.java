package com.sdstudio.iproxy.actions;

import java.util.Locale;

import javax.swing.AbstractAction;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

public abstract class BaseAction extends AbstractAction implements
		MessageSourceAware {
	private static final long serialVersionUID = -5710263350702525640L;
	protected MessageSource messageSource;

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
		updateLabel();
	}

	protected abstract String getLabelCode();

	protected void updateLabel() {
		putValue(
				NAME,
				messageSource.getMessage(getLabelCode(), null,
						Locale.getDefault()));
	}
}
