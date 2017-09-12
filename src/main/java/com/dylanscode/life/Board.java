package com.dylanscode.life;

import java.awt.Color;
import java.awt.Graphics2D;

public class Board {
	Main game;
	private int amountOfCellsX;
	private int amountOfCellsY;
	private int cellWidth;
	private int cellHeight;
	private Cell[][] cells;

	public Board(Main game, int amountOfCellsX) {
		this.game = game;
		this.setAmountOfCellsX(amountOfCellsX);
		this.setAmountOfCellsY((game.getHeight() * amountOfCellsX) / game.getWidth());
		this.cellWidth = game.getWidth() / amountOfCellsX;
		this.cellHeight = cellWidth;
	}
	public Cell[][] createCells(){
		cells = new Cell[++amountOfCellsY][++amountOfCellsX];
		int xCoord = 0;
		int yCoord = 0;
		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[y].length; x++) {
				cells[y][x] = new Cell(xCoord,yCoord,cellWidth);
				xCoord += cellWidth;
			}
			xCoord = 0;
			yCoord += cellWidth;
		}
		return cells;
	}
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		int y = 0;
		int x = 0;
		while (y < game.getHeight()) {
			g.drawLine(0, y, game.getWidth(), y);
			y += cellHeight;
		}
		while (x < game.getWidth()) {
			g.drawLine(x, 0, x, game.getHeight());
			x += cellWidth;
		}

	}

	public int getAmountOfCellsX() {
		return amountOfCellsX;
	}

	public void setAmountOfCellsX(int amountOfCellsX) {
		this.amountOfCellsX = amountOfCellsX;
	}
	public int getAmountOfCellsY() {
		return amountOfCellsY;
	}
	public void setAmountOfCellsY(int amountOfCellsY) {
		this.amountOfCellsY = amountOfCellsY;
	}
}
