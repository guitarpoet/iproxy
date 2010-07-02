package com.sdstudio.iproxy;

import java.io.IOException;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.LocalPortForwarder;

import com.sdstudio.iproxy.core.ModelBase;
import com.sdstudio.iproxy.ui.EditUserInformationDialog;

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
	private Connection connection;
	private boolean running;
	private Configuration configuration;
	private LocalPortForwarder lpf;
	private EditUserInformationDialog dialog;

	public EditUserInformationDialog getDialog() {
		return dialog;
	}

	@Autowired
	public void setDialog(EditUserInformationDialog dialog) {
		this.dialog = dialog;
	}

	public Connection getConnection() {
		return connection;
	}

	@Autowired
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	@Autowired
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
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

	@PostConstruct
	public void init() {
		logger.info("Initializing the proxy server...");
	}

	public void start() {
		try {
			logger.info("Starting proxy server...");
			getConnection().connect(null, 5000, 5000);
			if (getConfiguration().getString("ssh.user") == null
					|| getConfiguration().getString("ssh.password") == null)
				getDialog().setVisible(true);
			getConnection().authenticateWithPassword(
					getConfiguration().getString("ssh.user"),
					getConfiguration().getString("ssh.password"));
			setRunning(true);
			lpf = getConnection().createLocalPortForwarder(4321,
					getConfiguration().getString("ssh.host"), 80);
		} catch (Exception e) {
			logger.error("Server start failed!", e);
			JOptionPane.showMessageDialog(null, messageSource.getMessage(
					"server.start.failed", new Object[] { e.getMessage() },
					Locale.getDefault()));
			releaseResources();
		}
	}

	@PreDestroy
	public void stop() {
		releaseResources();
	}

	private void releaseResources() {
		try {
			if (lpf != null)
				lpf.close();
		} catch (IOException e) {
			logger.error("Error closing local port forwarding.", e);
		}
		connection.close();
		setRunning(false);
	}
}
