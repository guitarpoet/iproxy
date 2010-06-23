package com.sdstudio.iproxy;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.sdstudio.iproxy.core.ModelBase;

/**
 * The proxy server core for iproxy
 * 
 * @author Jack
 * @since Jun 23, 2010
 * @version 1.0
 */
@Component("ProxyServer")
public class ProxyServer extends ModelBase {
	private static Logger logger = LoggerFactory.getLogger(ProxyServer.class);
	private JSch jsch;
	private UserInfo userInfo;
	private Session session;
	private boolean running;

	public UserInfo getUserInfo() {
		return userInfo;
	}

	@Autowired
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public JSch getJsch() {
		return jsch;
	}

	@Autowired
	public void setJsch(JSch jsch) {
		this.jsch = jsch;
	}

	protected Session getSession() throws JSchException {
		if (session == null) {
			session = jsch.getSession("root", "58.211.114.109");
			session.setUserInfo(getUserInfo());
		}
		return session;
	}

	protected void addPortForwards() throws JSchException {
		getSession().setPortForwardingL(2010, "58.211.114.109", 80);
	}

	protected void setRunning(boolean running) {
		if (this.running == running)
			return;
		this.running = running;
		firePropertyChange("running", !running, running);
		if (running)
			logger.info("Proxy server started!");
		else
			logger.info("Proxy server stoped!");
	}

	public boolean isRunning() {
		return running;
	}

	public void start() {
		try {
			logger.info("Starting proxy server...");
			getSession().connect();
			addPortForwards();
			setRunning(true);
		} catch (JSchException e) {
			logger.error("Server start failed!", e);
			setRunning(false);
		}
	}

	@PreDestroy
	public void stop() {
		if (session != null) {
			session.disconnect();
			setRunning(false);
			session = null;
		}
	}
}
