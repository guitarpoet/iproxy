package com.sdstudio.iproxy;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sdstudio.iproxy.actions.Actions;
import com.sdstudio.iproxy.core.MessageSupport;

/**
 * This is the main frame for the iproxy.
 * 
 * @author Jack
 * @since Jun 23, 2010
 * @version 1.0
 */
@Component("MainFrame")
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1164806431325163382L;
	private static final Logger logger = LoggerFactory
			.getLogger(MainFrame.class);
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu userMenu;
	private JMenu helpMenu;
	private JButton startStopButton;
	private Actions actions;
	private MessageSupport messageSupport;

	public MessageSupport getMessageSupport() {
		return messageSupport;
	}

	@Autowired
	public void setMessageSupport(MessageSupport messageSupport) {
		this.messageSupport = messageSupport;
	}

	public Actions getActions() {
		return actions;
	}

	@Autowired
	public void setActions(Actions actions) {
		this.actions = actions;
	}

	@PostConstruct
	public void init() {
		setTitle(getMessageSupport().getMessage("mainframe.title"));
		startStopButton = new JButton(getActions().getStartStopAction());
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
		fileMenu = new JMenu(getMessageSupport().getMessage("menu.file"));
		menuBar.add(fileMenu);
		fileMenu.add(getActions().getStartStopAction());
		fileMenu.add(new AbstractAction(getMessageSupport().getMessage("close")) {
			private static final long serialVersionUID = 7800783523993278284L;

			public void actionPerformed(ActionEvent e) {
				MainFrame.this.dispose();
			}
		});
		userMenu = new JMenu(getMessageSupport().getMessage("menu.user"));
		userMenu.add(getActions().getEditUserInformationAction());
		helpMenu = new JMenu(getMessageSupport().getMessage("menu.help"));
		helpMenu.add(getActions().getAboutUsAction());
		menuBar.add(userMenu);
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setResizable(false);
		setLocationByPlatform(true);
	}

	@Override
	@PreDestroy
	public void dispose() {
		super.dispose();
	}
}
