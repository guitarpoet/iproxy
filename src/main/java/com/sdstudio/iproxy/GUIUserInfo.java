package com.sdstudio.iproxy;

import java.util.Locale;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.UserInfo;

/**
 * This is the UserInfo implementation for jsch in swing
 * 
 * @author Jack
 * @since Jun 23, 2010
 * @version 1.0
 */
@Component("GUIUserInfo")
public class GUIUserInfo implements UserInfo, MessageSourceAware {
	private String[] yesNoOptions;
	private MainFrame mainFrame;
	private String password;
	private JPasswordField passwordField = new JPasswordField(20);

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	@Autowired
	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public String getPassphrase() {
		return null;
	}

	public String getPassword() {
		return password;
	}

	public boolean promptPassphrase(String message) {
		return false;
	}

	public boolean promptPassword(String message) {
		if (JOptionPane.showConfirmDialog(getMainFrame(),
				new Object[] { passwordField }, message,
				JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
			password = new String(passwordField.getPassword());
			return true;
		}
		return false;
	}

	public boolean promptYesNo(String message) {
		return JOptionPane.showOptionDialog(getMainFrame(), message, "Warning",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
				yesNoOptions, yesNoOptions[0]) == 0;
	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(getMainFrame(), message);
	}

	public void setMessageSource(MessageSource messageSource) {
		yesNoOptions = new String[] {
				messageSource.getMessage("yes", null, "yes", Locale
						.getDefault()),
				messageSource.getMessage("no", null, "no", Locale.getDefault()) };
	}
}
