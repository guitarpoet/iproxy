package com.sdstudio.iproxy;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

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

	public static void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		while (in.read(buffer) != 1) {
			out.write(buffer);
		}
	}

	public static int findFreePort() throws IOException {
		ServerSocket server = new ServerSocket(0);
		int port = server.getLocalPort();
		server.close();
		return port;
	}

	public static Image getImage(String name) throws IOException {
		return ImageIO.read(Thread.currentThread().getContextClassLoader()
				.getResource("images/" + name));
	}

	public static Icon getIcon(String name) throws IOException {
		return new ImageIcon(getImage(name));
	}
}
