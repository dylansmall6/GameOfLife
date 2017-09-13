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
	public int[][] positions = { { -1, 1 }, { 0, 1 }, { 1, 1 }, { -1, 0 }, { 1, 0 }, { -1, -1 }, { 0, -1 }, { 1, -1 } };

	public Board(Main game, int amountOfCellsX) {
		this.game = game;
		this.setAmountOfCellsX(amountOfCellsX);
		this.setAmountOfCellsY((game.getHeight() * amountOfCellsX) / game.getWidth());
		this.cellWidth = game.getWidth() / amountOfCellsX;
		this.cellHeight = cellWidth;
	}

	public Cell[][] createCells() {
		cells = new Cell[++amountOfCellsY][++amountOfCellsX];
		int xCoord = 0;
		int yCoord = 0;
		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[y].length; x++) {
				cells[y][x] = new Cell(xCoord, yCoord, cellWidth);
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

	public void findNeighbors(Cell[][] cells) {
		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[y].length; x++) {
				for (int i = 0; i < positions.length; i++) {
					int neighborY = positions[i][0] + y;
					int neighborX = positions[i][1] + x;
					if (neighborY < 0)
						neighborY = this.getAmountOfCellsY() - 1;
					if (neighborY >= this.getAmountOfCellsY())
						neighborY = 0;
					if (neighborX < 0)
						neighborX = this.getAmountOfCellsX() - 1;
					if (neighborX >= this.getAmountOfCellsX())
						neighborX = 0;
					if (cells[neighborY][neighborX].isInhabited()) {
						cells[y][x].setNeighbors(cells[y][x].getNeighbors() + 1);
					}	
				}
				if(game.isDebug())
					System.out.println("Cell at ("+x+","+y+") has " + cells[y][x].getNeighbors()+ " neighbors!");
			}
		}
	}
	public void resetNeighbors(Cell[][] cells) {
		for(int y=0;y<cells.length;y++) {
			for(int x=0;x<cells[y].length;x++) {
				cells[y][x].setNeighbors(0);
			}
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
