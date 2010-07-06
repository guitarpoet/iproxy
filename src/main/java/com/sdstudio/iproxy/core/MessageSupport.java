package com.sdstudio.iproxy.core;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

import com.sdstudio.iproxy.Utils;

@Component("MessageSupport")
public class MessageSupport implements MessageSourceAware {
	private MessageSource messageSource;

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public String getMessage(String code) {
		return getMessage(code, new Object[0]);
	}

	public String getMessage(String code, Object... args) {
		return messageSource.getMessage(code, args, Utils.getLocale());
	}
}
