package com.sdstudio.iproxy;

import javax.annotation.PostConstruct;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.sdstudio.iproxy.actions.StartStopButtonAction;

/**
 * This is the main frame for the iproxy.
 * 
 * @author Jack
 * @since Jun 23, 2010
 * @version 1.0
 */
@Component("MainFrame")
public class MainFrame extends JFrame implements ApplicationContextAware {
	private static final long serialVersionUID = 1164806431325163382L;
	private ConfigurableApplicationContext applicationContext;
	private ProxyServer proxyServer;
	private JButton startStopButton;
	private StartStopButtonAction startStopButtonAction;

	public StartStopButtonAction getStartStopButtonAction() {
		return startStopButtonAction;
	}

	@Autowired
	public void setStartStopButtonAction(
			StartStopButtonAction startStopButtonAction) {
		this.startStopButtonAction = startStopButtonAction;
	}

	public ProxyServer getProxyServer() {
		return proxyServer;
	}

	@Autowired
	public void setProxyServer(ProxyServer proxyServer) {
		this.proxyServer = proxyServer;
	}

	@PostConstruct
	public void init() {
		startStopButton = new JButton(getStartStopButtonAction());
		getContentPane().add(startStopButton);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	@Override
	public void dispose() {
		super.dispose();
		applicationContext.close();
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
		setTitle(applicationContext.getMessage("mainframe.title", null,
				"iProxy", getLocale()));
	}
}
