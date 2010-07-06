package com.sdstudio.iproxy.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

public class MessageEvent extends ApplicationEvent {
	private static final long serialVersionUID = 9080000451238167439L;
	private String channel = "log";
	private Object message;
	private LevelType level = LevelType.Info;

	public MessageEvent(Object target, String channel, Object message) {
		this(target, message, LevelType.Info, channel);
	}

	public MessageEvent(Object target, Object message) {
		this(target, message, LevelType.Info);
	}

	public MessageEvent(Object target, Object message, LevelType level) {
		this(target, message, level, "log");
	}

	public MessageEvent(Object target, Object message, LevelType level,
			String channel) {
		super(target);
		this.level = level;
		this.channel = channel;
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
		new ThreadLocal<ApplicationContext>().get().publishEvent(this);
	}
}
