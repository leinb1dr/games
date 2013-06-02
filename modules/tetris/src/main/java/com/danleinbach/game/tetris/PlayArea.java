package com.danleinbach.game.tetris;


import com.danleinbach.game.design.entities.GameEntity;
import com.danleinbach.game.design.mediaLoaders.imageLoader.GameImageLoader;
import com.danleinbach.game.design.mediaLoaders.imageLoader.imageTypes.ReelImage;

import java.awt.*;

public class PlayArea extends GameEntity<Void> {
	protected int[][] pieces;
	protected int rows;
	protected int cols;
	protected Rectangle area;

	public PlayArea(int pWidth, int pHeight) {
		super(pWidth, pHeight);
	}

	public PlayArea(int rows, int cols, int pWidth, int pHeight) {
		super(pWidth, pHeight);
		this.rows = rows;
		this.cols = cols;
		this.pieces = new int[rows][cols];
		int x = (pWidth / 2 - cols * 40) / 2;
		int y = (pHeight - rows * 40) / 2;
		this.area = new Rectangle(x, y, cols * 40, rows * 40);
	}

	public PlayArea(int rows, int cols, int x, int y, int pWidth, int pHeight) {
		super(pWidth, pHeight);
		this.rows = rows;
		this.cols = cols;
		this.pieces = new int[rows][cols];
		this.area = new Rectangle(x, y, cols * 40, rows * 40);
	}

	public int getRows() {
		return this.rows;
	}

	public int getCols() {
		return this.cols;
	}

	public int getX() {
		return this.area.x;
	}

	public int getY() {
		return this.area.y;
	}

	public Void update() {
		return null;
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.yellow);
		for(int row = 0; row < this.rows; row++) {
			for(int col = 0; col < this.cols; col++) {
				int val = this.pieces[row][col];

				g.drawImage(
						((ReelImage) GameImageLoader.getInstance().getImage("block")).getImage(Integer.valueOf(val)),
						col * 40 + this.area.x, row * 40 + this.area.y, 40, 40, null);
			}
		}
	}

	public Void update(int x, int y) {
		return null;
	}

	public boolean contains(Rectangle rec) {
		return this.area.intersects(rec);
	}

	public void settlePieces(int row, int col, int[][] grid) {
		boolean gameOver = false;
		for(int x = 0; x < grid.length; x++) {
			for(int y = 0; y < grid[x].length; y++) {
				if((x + row >= 0) && (x + row < this.rows) && (y + col >= 0) && (y + col < this.cols)) {
					if(grid[x][y] != 0) {
						this.pieces[(x + row)][(y + col)] = grid[x][y];
						setChanged();
					}
				} else {
					gameOver = true;
					setChanged();
				}
			}
		}
		if(hasChanged()) {
			if(gameOver) {
				notifyObservers("GAME_OVER");
				return;
			}

			notifyObservers(new Score(0, 10));
		}

		checkForFullRow();
	}

	private void checkForFullRow() {
		int rowsRemoved = 0;
		for(int x = 0; x < this.rows; x++) {
			int rowSum = 0;
			for(int y = 0; y < this.cols; y++) {
				if(this.pieces[x][y] != 0) {
					rowSum++;
				}
			}
			if(rowSum == 10) {
				removeRow(x);
				rowsRemoved++;
				x--;
			}
		}
		if(rowsRemoved > 0) {
			setChanged();
			int score = 100 * rowsRemoved + 25 * (rowsRemoved - 1);
			notifyObservers(new Score(rowsRemoved, score));
		}
	}

	private void removeRow(int row) {
		for(int x = row; x >= 0; x--) {
			for(int y = 0; y < this.cols; y++) {
				if(x > 0) {
					this.pieces[x][y] = this.pieces[(x - 1)][y];
				} else {
					this.pieces[x][y] = 0;
				}
			}
		}
	}

	public boolean isOccupied(int row, int col) {
		if((row >= 0) && (row < this.rows) && (col >= 0) && (col < this.cols)) {
			return this.pieces[row][col] >= 1;
		}
		return true;
	}

	public void clear() {
		this.pieces = new int[this.rows][this.cols];
	}
}

/* Location:           C:\Users\Daniel\Desktop\Tetris.jar
 * Qualified Name:     PlayArea
 * JD-Core Version:    0.6.2
 */