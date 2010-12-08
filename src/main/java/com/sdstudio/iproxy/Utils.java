package com.sdstudio.iproxy;

import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

/**
 * The utility functions for iproxy
 * 
 * @author Jack
 * @since Jun 23, 2010
 * @version 1.0
 */
public class Utils {
	private static Locale locale;
	public static ApplicationContext applicationContext;

	public static String getUserHomeDir() {
		return System.getProperty("user.home");
	}

	public static String getTmpDir() {
		return System.getProperty("java.io.tmpdir");
	}

	public static File getOrCreate(String path, boolean isDir)
			throws IOException {
		File f = new File(path);
		if (f.exists()) {
			return f;
		}
		if (isDir) {
			f.mkdirs();
		} else {
			// Create parent dir if necessary
			getOrCreate(f.getParent(), true);
			f.createNewFile();
		}
		return f;
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
		while (true) {
			synchronized (buffer) {
				int amountRead = in.read(buffer);
				if (amountRead == -1) {
					break;
				}
				out.write(buffer, 0, amountRead);
			}
		}
	}

	public static int findFreePort() throws IOException {
		ServerSocket server = new ServerSocket(0);
		int port = server.getLocalPort();
		server.close();
		return port;
	}

	public static InputStream getResource(String name) {
		return Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(name);
	}

	public static Image getImage(String name) throws IOException {
		return ImageIO.read(getResource("images/" + name));
	}

	public static Icon getIcon(String name) throws IOException {
		return new ImageIcon(getImage(name));
	}

	public static MainFrame getMainFrame() {
		return applicationContext.getBean(MainFrame.class);
	}

	public static Locale getLocale() {
		if (locale == null) {
			locale = Locale.getDefault();
		}

		return locale;
	}

	public static String getFileAsString(String name) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		copy(getResource(name), out);
		return new String(out.toByteArray());
	}

	public static void setLocale(Locale locale) {
		getMainFrame().setLocale(locale);
	}
}
