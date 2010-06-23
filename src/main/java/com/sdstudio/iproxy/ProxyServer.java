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
	private Configuration configuration;

	public Configuration getConfiguration() {
		return configuration;
	}

	@Autowired
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

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
			String user = getConfiguration().getString("ssh.user");
			String host = getConfiguration().getString("ssh.host");
			session = jsch.getSession(user, host);
			session.setUserInfo(getUserInfo());
		}
		return session;
	}

	protected void addPortForwards() throws JSchException {
		String host = getConfiguration().getString("ssh.host");
		if (getConfiguration().getBoolean("http.proxy.enabled")) {
			int httpProxyPort = getConfiguration()
					.getInteger("http.proxy.port");
			int httpPort = getConfiguration().getInteger("http.port");
			logger.info("Forward http at port {} -> host {} port {}.",
					new Object[] { httpProxyPort, host, httpPort });
			getSession().setPortForwardingL(httpProxyPort, host, httpPort);
		}
		if (getConfiguration().getBoolean("https.proxy.enabled")) {
			int httpsProxyPort = getConfiguration().getInteger(
					"https.proxy.port");
			int httpsPort = getConfiguration().getInteger("https.port");
			logger.info("Forward https at port {} -> host {} port {}.",
					new Object[] { httpsProxyPort, host, httpsPort });
			getSession().setPortForwardingL(httpsProxyPort, host, httpsPort);
		}
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
