package com.sdstudio.iproxy;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

@Component("SystrayMonitor")
public class SystrayMonitor implements MessageSourceAware {
	private static Logger logger = LoggerFactory
			.getLogger(SystrayMonitor.class);
	private SystemTray systray;
	private PopupMenu menu;
	private MessageSource messageSource;
	private TrayIcon trayIcon;

	@PostConstruct
	public void init() throws AWTException, NoSuchMessageException, IOException {
		if (SystemTray.isSupported()) {
			logger.info("Installing the system tray...");
			systray = SystemTray.getSystemTray();
			menu = new PopupMenu();
			MenuItem closeMenuItem = new MenuItem(messageSource.getMessage(
					"close", null, Utils.getLocale()));
			closeMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Utils.getMainFrame().dispose();
				}
			});
			menu.add(closeMenuItem);
			trayIcon = new TrayIcon(Utils.getImage("iproxy_tray_icon.png"),
					messageSource.getMessage("iproxy.title", null,
							Utils.getLocale()), menu);
			systray.add(trayIcon);
		}
	}

	@PreDestroy
	public void dispose() {
		if (SystemTray.isSupported() && trayIcon != null) {
			logger.info("Removing the system tray icon.");
			systray.remove(trayIcon);
		}
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
