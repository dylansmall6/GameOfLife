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

import com.dylanscode.input.KeyHandler;
import com.dylanscode.input.MouseHandler;
import com.dylanscode.input.MouseMotionHandler;

public class Main extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;
	public boolean simRunning = false;
	public int ticksPerSecond = 60;
	private double nsPerTick = 1000000000D / ticksPerSecond;
	BufferStrategy bs;
	Graphics g;
	Board board;
	KeyHandler keyHandler;
	MouseHandler mouseHandler;
	MouseMotionHandler mouseMotionHandler;
	Cell[][] cells;
	public boolean isRunning = false;

	public static void main(String[] args) {
		new Main().start();
	}

	/**
	 * Main GameOfLife loop!
	 */
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		nsPerTick = 1000000000D / ticksPerSecond;
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
		board = new Board(this, 100);
		cells = board.createCells();
		keyHandler = new KeyHandler(this);
		mouseHandler = new MouseHandler(this);
		mouseMotionHandler = new MouseMotionHandler(this, mouseHandler);
	}

	/**
	 * Runs 60 times / second (or at least should) updates the game
	 */
	public void tick() {
		if (keyHandler.ESC.isPressed()) {
			System.exit(0);
		}
		if ((mouseHandler.LEFT_CLICK.isClicked() || mouseMotionHandler.handler.LEFT_CLICK.isDragged())
				&& (!simRunning)) {
			int eventX = mouseHandler.LEFT_CLICK.getX();
			int eventY = mouseHandler.LEFT_CLICK.getY();
			for (int y = 0; y < cells.length; y++) {
				for (int x = 0; x < cells[y].length; x++) {
					if (cells[y][x].isOnMouse(eventX, eventY))
						cells[y][x].setInhabited(true);
				}
			}
		}
		if ((mouseHandler.RIGHT_CLICK.isClicked() || mouseMotionHandler.handler.RIGHT_CLICK.isDragged())
				&& (!simRunning)) {
			int eventX = mouseHandler.RIGHT_CLICK.getX();
			int eventY = mouseHandler.RIGHT_CLICK.getY();
			for (int y = 0; y < cells.length; y++) {
				for (int x = 0; x < cells[y].length; x++) {
					if (cells[y][x].isOnMouse(eventX, eventY))
						cells[y][x].setInhabited(false);
				}
			}
		}
		if (!simRunning && keyHandler.ENTER.isPressed()) {
			simRunning = true;
			setTicksPerSecond(3);
		}
		if (simRunning) {
			update(cells);
		}
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
					board.draw(g2);
					for (int y = 0; y < cells.length; y++) {
						for (int x = 0; x < cells[y].length; x++) {
							cells[y][x].draw(g2);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (g2 != null)
						g2.dispose();
				}
				bs.show();
			} while (bs.contentsLost());
		}
	}
	/**
	 * updates game of life. too tired to fix. goodnight
	 * @param cells
	 */
	public void update(Cell[][] cells) {
		@SuppressWarnings("unused")
		int amountOfNeighbors = 0;
		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[y].length; x++) {
				// bottom right
				int x2 = (x + 1) <= board.getAmountOfCellsX()-1 ? x + 1 : 0;
				int y2 = (y + 1) <= board.getAmountOfCellsY()-1 ? y + 1 : 0;
				if (cells[y2][x2].isInhabited())
					amountOfNeighbors++;
				// bottom middle
				if (cells[y2][x].isInhabited())
					amountOfNeighbors++;
				// middle right
				if (cells[y][x2].isInhabited())
					amountOfNeighbors++;
				y2 = (y - 1) >= 0 ? y - 1 : board.getAmountOfCellsY() - 1;
				// top right
				if (cells[y2][x2].isInhabited())
					amountOfNeighbors++;
				// top middle
				if (cells[y2][x].isInhabited())
					amountOfNeighbors++;
				x2 = (x - 1) >= 0 ? x - 1 : board.getAmountOfCellsX() - 1;
				// top left
				if (cells[y2][x2].isInhabited())
					amountOfNeighbors++;
				//middle left
				if(cells[y][x2].isInhabited())
					amountOfNeighbors++;
				//bottom left
				y2 = (y + 1) <= board.getAmountOfCellsY()-1 ? y + 1 : 0;
				if(cells[y2][x2].isInhabited())
					amountOfNeighbors++;
				//check to see what to do to the cell
				if(amountOfNeighbors<2 && cells[y][x].isInhabited()) {
					cells[y][x].setInhabited(false);
				}else if((amountOfNeighbors==2 || amountOfNeighbors == 3) && cells[y][x].isInhabited()) {
					cells[y][x].setInhabited(true);
				}else if(amountOfNeighbors > 3 && cells[y][x].isInhabited()){
					cells[y][x].setInhabited(false);
				}else if(amountOfNeighbors==3 && !(cells[y][x].isInhabited())) {
					cells[y][x].setInhabited(true);
				}
			}
		}
	}

	public void setTicksPerSecond(int amount) {
		this.nsPerTick = 1000000000D / amount;
	}
}
