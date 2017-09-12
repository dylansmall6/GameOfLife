package com.dylanscode.life;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class GameOfLife extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;
	BufferStrategy bs;
	Graphics g;
	public boolean isRunning = false;

	public static void main(String[] args) {
		new GameOfLife().start();
	}

	/**
	 * Main GameOfLife loop!
	 */
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000 / 60D;
		int frames = 0;
		int ticks = 0;
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		initialize();
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			while (delta >= 1) {
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}
			if (shouldRender) {
				frames++;
				paint(g);
			}
			if (System.currentTimeMillis() - lastTimer > 1000) {
				lastTimer += 1000;
				System.out.println("Frames: " + frames + " | Ticks: " + ticks);
				frames = 0;
				ticks = 0;
			}
		}
	}

	public void start() {
		isRunning = true;
		new Thread(this).start();
	}

	public void stop() {
		isRunning = false;
	}

	public void initialize() {
		setTitle("GameOfLife");
		setUndecorated(true);
		setVisible(true);
		setAlwaysOnTop(true);
		setResizable(false);
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		try {
			gd.setFullScreenWindow(this);
		} catch (Exception e) {
			gd.setFullScreenWindow(null);
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screen = tk.getScreenSize();
		setMinimumSize(screen);
		setPreferredSize(screen);
		setMaximumSize(screen);
		pack();
		setLocationRelativeTo(null);
		createBufferStrategy(2);
		bs = getBufferStrategy();
		g = getGraphics();
	}

	/**
	 * Runs 60 times / second (or at least should) updates the game
	 */
	public void tick() {

	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = null;
		if (bs != null) {
			do {
				try {
					g2 = (Graphics2D) bs.getDrawGraphics();
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setColor(Color.white);
					g2.fillRect(0, 0, getWidth(), getHeight());
				}catch(Exception e) {
					e.printStackTrace();
				}finally {
					if (g2 != null)
						g2.dispose();
				}
				bs.show();
			} while (bs.contentsLost());
		}
	}
}
