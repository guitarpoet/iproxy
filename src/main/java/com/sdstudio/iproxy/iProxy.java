package com.sdstudio.iproxy;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.http.client.HttpClient;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.sdstudio.iproxy.core.ModelBase;
import com.sdstudio.iproxy.event.MessageEvent;

/**
 * The instance class for iProxy
 * 
 * @author Jack
 * @since Jul 6, 2010
 * @version 1.0
 */
@Component("iProxy")
public class iProxy extends ModelBase implements ApplicationContextAware {
	private Logger logger = LoggerFactory.getLogger(iProxy.class);
	private Configuration configuration;
	private Server server;
	private iProxyJettyHandler handler;
	private ConfigurableApplicationContext applicationContext;
	private ProxyServer proxyServer;
	private HttpClient httpClient;

	public HttpClient getHttpClient() {
		return httpClient;
	}

	@Autowired
	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public ProxyServer getProxyServer() {
		return proxyServer;
	}

	@Autowired
	public void setProxyServer(ProxyServer proxyServer) {
		this.proxyServer = proxyServer;
	}

	public iProxyJettyHandler getHandler() {
		return handler;
	}

	@Autowired
	public void setHandler(iProxyJettyHandler handler) {
		this.handler = handler;
	}

	public Server getServer() {
		return server;
	}

	@Autowired
	public void setServer(Server server) {
		this.server = server;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	@Autowired
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void ping() {
		new MessageEvent(this, "message", messageSource.getMessage(
				"iproxy.running.title", null, Utils.getLocale()),
				messageSource.getMessage("iproxy.running.message", null,
						Utils.getLocale())).dispatch();
	}

	@PostConstruct
	public void start() throws Exception {
		logger.info("Starting iProxy...");
		logger.debug("Reading the configuration : {}.", getConfiguration());
		SocketConnector sc = new SocketConnector();
		sc.setPort(1234);
		getServer().setConnectors(new Connector[] { sc });
		getServer().addHandler(getHandler());
		getServer().start();
	}

	public void startProxy() {
		getProxyServer().start();
	}

	public void stopProxy() {
		getProxyServer().stop();
	}

	@PreDestroy
	public void destroy() throws Exception {
		logger.info("Stoping iProxy");
		getServer().stop();
		logger.info("iProxy stopped.");
	}

	public void stop() {
		applicationContext.close();
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
	}
}
