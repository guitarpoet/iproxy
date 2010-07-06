package com.sdstudio.iproxy.event;

import org.springframework.context.ApplicationEvent;

import com.sdstudio.iproxy.Utils;
import com.sdstudio.iproxy.core.MessageSupport;

public class MessageEvent extends ApplicationEvent {
	private static final long serialVersionUID = 9080000451238167439L;
	private String channel = "message";
	private Object message;
	private LevelType level = LevelType.Info;
	private String title;

	public MessageEvent(Object target, Object message) {
		this(target, "iProxy", message);
	}

	public MessageEvent(Object target, String title, Object message) {
		this(target, LevelType.Info, title, message);
	}

	public MessageEvent(Object target, LevelType level, String title,
			Object message) {
		this(target, "message", level, title, message);
	}

	public MessageEvent(Object target, Object message,
			MessageSupport messageSupport) {
		this(target, messageSupport.getMessage((String) message));
	}

	public MessageEvent(Object target, String title, Object message,
			MessageSupport messageSupport) {
		this(target, messageSupport.getMessage(title), messageSupport
				.getMessage((String) message));
	}

	public MessageEvent(Object target, LevelType level, String title,
			Object message, MessageSupport messageSupport) {
		this(target, level, messageSupport.getMessage(title), messageSupport
				.getMessage((String) message));
	}

	public MessageEvent(Object target, String channel, LevelType level,
			String title, Object message) {
		super(target);
		this.level = level;
		this.channel = channel;
		this.message = message;
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	public LevelType getLevel() {
		return level;
	}

	public void setLevel(LevelType level) {
		this.level = level;
	}

	public void dispatch() {
		Utils.applicationContext.publishEvent(this);
	}
}
