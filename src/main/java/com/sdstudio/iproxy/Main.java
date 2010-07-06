package com.sdstudio.iproxy;

import org.springframework.context.ApplicationContext;
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
		// Set applicationcontext to thread local.
		new ThreadLocal<ApplicationContext>().set(applicationContext);
		MainFrame frame = (MainFrame) applicationContext.getBean("MainFrame");
		frame.pack();
		frame.setVisible(true);
	}
}