package com.sdstudio.iproxy.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.annotation.PostConstruct;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

import com.sdstudio.iproxy.Configuration;

@Component("EditUserInforamtionDialog")
public class EditUserInformationDialog extends JDialog implements
		MessageSourceAware {
	private static final long serialVersionUID = -8994424108848247966L;

	private Configuration configuration;
	private MessageSource messageSource;
	private GridBagLayout layout;
	private JLabel usernameLabel;
	private JTextField usernameField;
	private JLabel passwordLabel;
	private JPasswordField passwordField;
	private JButton submitButton;
	private JButton cancelButton;

	public Configuration getConfiguration() {
		return configuration;
	}

	@Autowired
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	@PostConstruct
	public void init() {
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setModal(true);
		setTitle(messageSource.getMessage("user.information.edit", null,
				getLocale()));
		usernameLabel = new JLabel(messageSource.getMessage("username", null,
				getLocale()));
		usernameField = new JTextField(20);
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

		passwordLabel = new JLabel(messageSource.getMessage("password", null,
				getLocale()));
		getContentPane().add(passwordLabel);
		constraints.gridy = 1;
		layout.addLayoutComponent(passwordLabel, constraints);
		passwordField = new JPasswordField(20);
		layout.addLayoutComponent(passwordField, constraints);
		getContentPane().add(passwordField);
		constraints.gridy = 2;
		submitButton = new JButton(new AbstractAction(messageSource.getMessage(
				"submit", null, getLocale())) {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				getConfiguration().put("ssh.user", usernameField.getText());
				getConfiguration().put("ssh.password",
						new String(passwordField.getPassword()));
				setVisible(false);
			}
		});
		getContentPane().add(submitButton);
		layout.addLayoutComponent(submitButton, constraints);
		cancelButton = new JButton(new AbstractAction(messageSource.getMessage(
				"cancel", null, getLocale())) {
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

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
