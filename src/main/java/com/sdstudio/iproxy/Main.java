package com.sdstudio.iproxy;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This is the main application class for the iproxy
 * 
 * @author Jack
 * @since Jun 23, 2010
 * @version 1.0
 */
public class Main {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"application-context.xml");
		if (args.length > 0 && args[0].equals("-nogui")) {
			applicationContext.getBean(ProxyServer.class).start();
		} else {
			MainFrame frame = (MainFrame) applicationContext
					.getBean("MainFrame");
			frame.pack();
			frame.setVisible(true);
		}
	}
}
