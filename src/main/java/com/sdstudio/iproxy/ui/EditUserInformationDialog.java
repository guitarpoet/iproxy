package com.sdstudio.iproxy.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.annotation.PostConstruct;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sdstudio.iproxy.Configuration;
import com.sdstudio.iproxy.core.MessageSupport;

@Component("EditUserInforamtionDialog")
public class EditUserInformationDialog extends JDialog {
	private static final long serialVersionUID = -8994424108848247966L;

	private Configuration configuration;
	private GridBagLayout layout;
	private JLabel usernameLabel;
	private JTextField usernameField;
	private JLabel passwordLabel;
	private JPasswordField passwordField;
	private JButton submitButton;
	private JButton cancelButton;
	private MessageSupport messageSupport;
	private AbstractAction submitAction;
	private KeyListener enterSubmitHandler;

	public MessageSupport getMessageSupport() {
		return messageSupport;
	}

	@Autowired
	public void setMessageSupport(MessageSupport messageSupport) {
		this.messageSupport = messageSupport;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	@Autowired
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	@PostConstruct
	public void init() {
		submitAction = new AbstractAction(getMessageSupport().getMessage(
				"submit")) {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				getConfiguration().put("ssh.user", usernameField.getText());
				getConfiguration().put("ssh.password",
						new String(passwordField.getPassword()));
				setVisible(false);
			}
		};
		enterSubmitHandler = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					submitAction.actionPerformed(null);
				}
			}
		};
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setModal(true);
		setTitle(getMessageSupport().getMessage("user.information.edit"));
		usernameLabel = new JLabel(getMessageSupport().getMessage("username"));
		usernameField = new JTextField(20);
		usernameField.addKeyListener(enterSubmitHandler);
		layout = new GridBagLayout();
		getContentPane().setLayout(layout);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(10, 10, 10, 10);
		getContentPane().add(usernameLabel);
		constraints.gridy = 0;
		layout.addLayoutComponent(usernameLabel, constraints);
		getContentPane().add(usernameField);
		layout.addLayoutComponent(usernameField, constraints);

		passwordLabel = new JLabel(getMessageSupport().getMessage("password"));
		getContentPane().add(passwordLabel);
		constraints.gridy = 1;
		layout.addLayoutComponent(passwordLabel, constraints);
		passwordField = new JPasswordField(20);
		passwordField.addKeyListener(enterSubmitHandler);
		layout.addLayoutComponent(passwordField, constraints);
		getContentPane().add(passwordField);
		constraints.gridy = 2;
		submitButton = new JButton(submitAction);
		getContentPane().add(submitButton);
		layout.addLayoutComponent(submitButton, constraints);
		cancelButton = new JButton(new AbstractAction(getMessageSupport()
				.getMessage("cancel")) {
			private static final long serialVersionUID = 137812729173193471L;

			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		getContentPane().add(cancelButton);
		layout.addLayoutComponent(cancelButton, constraints);
		pack();
		setResizable(false);
	}

	@Override
	public void setVisible(boolean b) {
		if (b) {
			usernameField.setText(getConfiguration().getString("ssh.user"));
			passwordField.setText(getConfiguration().getString("ssh.password"));
		}
		super.setVisible(b);
	}
}
