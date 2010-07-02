package com.sdstudio.iproxy.actions;

import java.awt.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sdstudio.iproxy.ui.EditUserInformationDialog;

@Component("EditUserInformationAction")
public class EditUserInformationAction extends BaseAction {
	private static final long serialVersionUID = 8447253953572491898L;
	private EditUserInformationDialog dialog;

	public EditUserInformationDialog getDialog() {
		return dialog;
	}

	@Autowired
	public void setDialog(EditUserInformationDialog dialog) {
		this.dialog = dialog;
	}

	public void actionPerformed(ActionEvent e) {
		dialog.setVisible(true);
	}

	@Override
	protected String getLabelCode() {
		return "user.information";
	}

}