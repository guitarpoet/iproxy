package com.sdstudio.iproxy.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JWindow;

public class Splash extends JWindow {

	private ImageIcon icon = new ImageIcon(ImageIO.read(Thread.currentThread()
			.getContextClassLoader().getResource("images/iproxy.png")));

	private static final long serialVersionUID = 2645786053535352748L;

	public Splash() throws IOException {
		setPreferredSize(new Dimension(icon.getIconWidth(), icon
				.getIconHeight()));
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		icon.paintIcon(this, g, 0, 0);
	}
}
