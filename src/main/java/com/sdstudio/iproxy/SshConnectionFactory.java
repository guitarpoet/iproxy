package com.sdstudio.iproxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.ethz.ssh2.Connection;

@Component("SshConnectionFactory")
public class SshConnectionFactory implements FactoryBean<Connection> {
	private Configuration configuration;

	private Connection connection;

	public Configuration getConfiguration() {
		return configuration;
	}

	@Autowired
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public Connection getObject() throws Exception {
		if (connection == null) {
			connection = new Connection(getConfiguration()
					.getString("ssh.host"));

		}
		return connection;
	}

	public Class<?> getObjectType() {
		return Connection.class;
	}

	public boolean isSingleton() {
		return true;
	}

}
