package com.sdstudio.iproxy.actions;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sdstudio.iproxy.ProxyServer;

@Component("StartStopButtonAction")
public class StartStopButtonAction extends BaseAction implements
		PropertyChangeListener {
	private static final long serialVersionUID = 379590111051336452L;
	private ProxyServer proxyServer;

	public ProxyServer getProxyServer() {
		return proxyServer;
	}

	@Autowired
	public void setProxyServer(ProxyServer proxyServer) {
		this.proxyServer = proxyServer;
		proxyServer.addPropertyChangeListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (getProxyServer().isRunning())
			getProxyServer().stop();
		else
			getProxyServer().start();
	}

	public void propertyChange(PropertyChangeEvent evt) {
		updateLabel();
	}

	@Override
	protected String getLabelCode() {
		if (getProxyServer().isRunning())
			return "stop";
		else
			return "start";
	}
}
