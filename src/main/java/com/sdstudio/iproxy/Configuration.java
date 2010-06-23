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
		}
	}

	public void save() {
		File file = new File(getConfigDir());
		if (!file.exists())
			try {
				file.createNewFile();
				config.store(new FileWriter(file),
						"The configuration file for iproxy");
			} catch (IOException e) {
				logger.error("Error in storing the config file.", e);
			}
	}

	@PreDestroy
	public void dispose() {
		save();
	}
}
