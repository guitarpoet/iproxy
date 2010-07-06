package com.sdstudio.iproxy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository("configuration")
public class Configuration {
	private static Logger logger = LoggerFactory.getLogger(Configuration.class);
	private Properties config = new Properties();

	private String getConfigDir() {
		return Utils.combinePaths(Utils.getUserHomeDir(), ".iproxy");
	}

	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(config.getProperty(key));
	}

	public int getInteger(String key) {
		return Integer.parseInt(config.getProperty(key));
	}

	public double getDouble(String key) {
		return Double.parseDouble(config.getProperty(key));
	}

	public String getString(String key) {
		return config.getProperty(key);
	}

	@PostConstruct
	public void init() {
		File file = new File(getConfigDir());
		if (file.exists()) {
			try {
				config.load(new FileReader(file));
			} catch (FileNotFoundException e) {
				logger.error("Impossible!!!", e);
			} catch (IOException e) {
				logger.error("Got an IOException!", e);
			}
		} else {
			try {
				logger.info("Can't find the configuration file in user's home, reading the default configurations.");
				config.load(Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("config.default.properties"));
			} catch (IOException e) {
				logger.error("Error in reading default configration file.", e);
			}
		}
	}

	public void put(String key, Object value) {
		config.setProperty(key, String.valueOf(value));
	}

	@PreDestroy
	public void save() {
		File file = new File(getConfigDir());
		if (!file.exists())
			try {
				file.createNewFile();

			} catch (IOException e) {
				logger.error("Error in creating the config file.", e);
			}
		try {
			config.store(new FileWriter(file),
					"The configuration file for iproxy");
		} catch (IOException e) {
			logger.error("Error in writing the configurations.", e);
		}
	}

	@Override
	public String toString() {
		return config.toString();
	}
}
