package com.sdstudio.iproxy;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.sdstudio.iproxy.actions.EditUserInformationAction;
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
	private static final Logger logger = LoggerFactory
			.getLogger(MainFrame.class);
	private ConfigurableApplicationContext applicationContext;
	private ProxyServer proxyServer;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu userMenu;
	private JButton startStopButton;
	private StartStopButtonAction startStopButtonAction;
	private EditUserInformationAction editUserInformationAction;

	public EditUserInformationAction getEditUserInformationAction() {
		return editUserInformationAction;
	}

	@Autowired
	public void setEditUserInformationAction(
			EditUserInformationAction editUserInformationAction) {
		this.editUserInformationAction = editUserInformationAction;
	}

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
		GridBagLayout layout = new GridBagLayout();
		getContentPane().setLayout(layout);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridy = 0;
		try {
			JLabel logo = new JLabel(Utils.getIcon("iproxy_logo_normal.png"));
			setIconImage(Utils.getImage("iproxy_icon.png"));
			getContentPane().add(logo);
			layout.addLayoutComponent(logo, constraints);
		} catch (IOException e1) {
			logger.error("Error in reading image.", e1);
		}
		getContentPane().add(startStopButton);
		layout.addLayoutComponent(startStopButton, constraints);
		menuBar = new JMenuBar();
		fileMenu = new JMenu(applicationContext.getMessage("menu.file", null,
				getLocale()));
		menuBar.add(fileMenu);
		fileMenu.add(getStartStopButtonAction());
		fileMenu.add(new AbstractAction(applicationContext.getMessage(
				"menu.file.close", null, getLocale())) {
			private static final long serialVersionUID = 7800783523993278284L;

			public void actionPerformed(ActionEvent e) {
				MainFrame.this.dispose();
			}
		});
		userMenu = new JMenu(applicationContext.getMessage("menu.user", null,
				getLocale()));
		userMenu.add(editUserInformationAction);
		menuBar.add(userMenu);
		setJMenuBar(menuBar);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setLocationByPlatform(true);
	}

	@Override
	public void dispose() {
		super.dispose();
		applicationContext.close();
		System.exit(0);
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
		setTitle(applicationContext.getMessage("mainframe.title", null,
				"iProxy", getLocale()));
	}
}
