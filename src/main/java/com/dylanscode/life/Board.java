package com.dylanscode.life;

import java.awt.Color;
import java.awt.Graphics2D;

public class Board {
	Main game;
	private int amountOfCellsX;
	private int cellWidth;
	private int cellHeight;
	public Board(Main game, int amountOfCellsX) {
		this.game = game;
		this.setAmountOfCellsX(amountOfCellsX);
		this.cellWidth = game.getWidth() / amountOfCellsX;
		this.cellHeight = cellWidth;
	}
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		int y=0;
		int x=0;
		while(y < game.getHeight()) {
			g.drawLine(0, y, game.getWidth(), y);
			y+= cellHeight;
		}
		while(x < game.getWidth()) {
			g.drawLine(x, 0, x, game.getHeight());
			x+= cellWidth;
		}
		
	}
	public int getAmountOfCellsX() {
		return amountOfCellsX;
	}
	public void setAmountOfCellsX(int amountOfCellsX) {
		this.amountOfCellsX = amountOfCellsX;
	}
}
