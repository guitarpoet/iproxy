package com.sdstudio.iproxy;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This is the main application class for the iproxy
 * 
 * @author Jack
 * @since Jun 23, 2010
 * @version 1.0
 */
public class Main {
	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		try {
			logger.info("Checking if there is an iProxy running...");
			HttpClient client = new DefaultHttpClient();
			HttpHost host = new HttpHost("localhost", 1234);
			HttpGet get = new HttpGet("/iproxy?method=ping");
			client.execute(host, get);
			// If get the response, there is an iProxy running on this machine.
			// Stop this.
			logger.info("There is an iProxy running, quitting.");
			return;
		} catch (IOException e) {
			logger.info("No iproxy is running, starting iproxy.");
		}
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"application-context.xml");
		Utils.applicationContext = applicationContext;
		MainFrame frame = (MainFrame) applicationContext.getBean("MainFrame");
		frame.pack();
		frame.setVisible(true);
	}
}