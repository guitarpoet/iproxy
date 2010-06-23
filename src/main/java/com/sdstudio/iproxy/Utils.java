package com.sdstudio.iproxy;

import org.apache.commons.lang.StringUtils;

/**
 * The utility functions for iproxy
 * 
 * @author Jack
 * @since Jun 23, 2010
 * @version 1.0
 */
public class Utils {
	public static String getUserHomeDir() {
		return System.getProperty("user.home");
	}

	public static String getFileSeperato() {
		return System.getProperty("file.separator");
	}

	public static String combinePaths(String... paths) {
		return StringUtils.join(paths, getFileSeperato());
	}
}
