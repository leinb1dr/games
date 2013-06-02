package com.danleinbach.game.tetris;


import com.danleinbach.game.design.entities.GameEntity;
import com.danleinbach.game.design.mediaLoaders.imageLoader.GameImageLoader;
import com.danleinbach.game.design.mediaLoaders.imageLoader.imageTypes.ReelImage;

import java.awt.*;

public class CurrentBlock extends GameEntity<Boolean> {
	int[][] r;
	PlayArea board;
	int row = 0;
	int col = 0;
	Shapes currentShape;

	public CurrentBlock(int pWidth, int pHeight, PlayArea board) {
		super(pWidth, pHeight);

		this.board = board;
	}

	public Boolean update() {
		return null;
	}

	public void draw(Graphics2D g) {
		if(this.r == null) {
			return;
		}
		g.setColor(Color.black);
		for(int x = 0; x < this.r.length; x++) {
			for(int y = 0; y < this.r[x].length; y++) {
				int val = this.r[x][y];
				if(val > 0) {
					g.drawImage(((ReelImage) GameImageLoader.getInstance().getImage("block"))
							.getImage(Integer.valueOf(val)),
							(this.col + y) * 40 + this.board.getX(),
							(this.row + x) * 40 + this.board.getY(),
							40, 40, null);
				}
			}
		}
	}

	public Boolean update(int x, int y) {
		if(this.currentShape != null) {
			int tmpRow = this.row + y;
			int tmpCol = this.col + x;

			if((tmpRow + this.r.length <= this.board.getRows()) && (tmpRow >= - 3)) {
				if((tmpCol + this.r[0].length <= this.board.getCols()) && (tmpCol >= 0)) {
					for(int a = 0; a < this.r.length; a++) {
						for(int b = 0; b < this.r[a].length; b++) {
							if((this.r[a][b] >= 1) &&
									(tmpRow + a >= 0) &&
									(this.board.isOccupied(tmpRow + a, tmpCol + b))) {
								return Boolean.valueOf(false);
							}
						}
					}
				} else {
					return Boolean.valueOf(false);
				}
			} else {
				return Boolean.valueOf(false);
			}

			this.row = tmpRow;

			this.col = tmpCol;
			return Boolean.valueOf(true);
		}
		return Boolean.valueOf(true);
	}

	public void rotate() {
		boolean rotate = true;
		while(rotate) {
			rotate = false;
			this.r = this.currentShape.rotateRight(this.r);
			for(int a = 0; a < this.r.length; a++) {
				for(int b = 0; b < this.r[a].length; b++) {
					if((this.row + a >= 0) &&
							(this.r[a][b] >= 1) &&
							(this.board.isOccupied(this.row + a, this.col + b))) {
						rotate = true;
					}
				}
			}
		}
	}

	public boolean collision(PlayArea board) {
		return true;
	}

	public void commit() {
		this.board.settlePieces(this.row, this.col, this.r);
	}

	public void reset(Shapes next) {
		this.currentShape = next;
		if(this.currentShape != null) {
			this.r = this.currentShape.getGrid();
			this.row = - 3;
			this.col = ((int) Math.floor(this.board.getCols() / 2.0D));
		}
	}

	public Shapes getShape() {
		return this.currentShape;
	}

	public void nextBlock() {
		int x = Shapes.values().length;
		this.currentShape = Shapes.values()[((int) (Math.random() * x))];
		this.r = this.currentShape.getGrid();
		center();
	}

	public void center() {
		this.row = ((int) (Math.floor(this.board.getRows() / 2.0D) - Math.floor(this.r.length / 2.0D)));
		this.col = ((int) (Math.floor(this.board.getCols() / 2.0D) - Math.floor(this.r[0].length / 2.0D)));
	}

	public boolean swap(Shapes shape) {
		Shapes old = this.currentShape;
		this.currentShape = shape;
		this.r = this.currentShape.getGrid();
		for(int a = 0; a < this.r.length; a++) {
			for(int b = 0; b < this.r[a].length; b++) {
				if((this.row + a <= this.board.getRows()) && (this.row + b >= - 3)) {
					if((this.col + b <= this.board.getCols()) && (this.col + b >= 0)) {
						if((this.row + a >= 0) &&
								(this.r[a][b] >= 1) &&
								(this.board.isOccupied(this.row + a, this.col + b))) {
							this.currentShape = old;
							this.r = this.currentShape.getGrid();
							return false;
						}
					} else {
						this.currentShape = old;
						this.r = this.currentShape.getGrid();
						return false;
					}
				} else {
					this.currentShape = old;
					this.r = this.currentShape.getGrid();
					return false;
				}
			}
		}
		return true;
	}

	public void rotateLeft() {
		boolean rotate = true;
		while(rotate) {
			rotate = false;
			this.r = this.currentShape.rotateLeft(this.r);
			for(int a = 0; a < r.length; a++) {
				for(int b = 0; b < this.r[a].length; b++) {
					if((this.row + a >= 0) &&
							(this.r[a][b] >= 1) &&
							(this.board.isOccupied(this.row + a, this.col + b))) {
						rotate = true;
					}
				}
			}
		}
	}
}

/* Location:           C:\Users\Daniel\Desktop\Tetris.jar
 * Qualified Name:     CurrentBlock
 * JD-Core Version:    0.6.2
 */