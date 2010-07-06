package com.sdstudio.iproxy.actions;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.springframework.stereotype.Component;

import com.sdstudio.iproxy.Utils;

@Component("AboutUsAction")
public class AboutUsAction extends BaseAction {

	private static final long serialVersionUID = -6218583603760073980L;

	public void actionPerformed(ActionEvent e) {
		try {
			JOptionPane.showMessageDialog(null,
					getMessageSupport().getMessage("about_us.message"),
					getMessageSupport().getMessage("about_us"),
					JOptionPane.YES_OPTION,
					Utils.getIcon("iproxy_logo_normal.png"));
		} catch (Exception e1) {
		}
	}

	@Override
	protected String getLabelCode() {
		return "about_us";
	}

}
