package com.sdstudio.iproxy.actions;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

import javax.swing.AbstractAction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

import com.sdstudio.iproxy.ProxyServer;

@Component("StartStopButtonAction")
public class StartStopButtonAction extends AbstractAction implements
		PropertyChangeListener, MessageSourceAware {
	private static final long serialVersionUID = 379590111051336452L;
	private ProxyServer proxyServer;
	private MessageSource messageSource;

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
		if (!(Boolean) evt.getNewValue()) {
			putValue(NAME, messageSource.getMessage("startstop.button.start",
					null, "Start", Locale.getDefault()));
		} else {
			putValue(NAME, messageSource.getMessage("startstop.button.stop",
					null, "Stop", Locale.getDefault()));
		}
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
		putValue(NAME, messageSource.getMessage("startstop.button.start", null,
				"Start", Locale.getDefault()));
	}
}
