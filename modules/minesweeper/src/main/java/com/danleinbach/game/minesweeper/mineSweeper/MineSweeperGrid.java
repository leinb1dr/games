package com.danleinbach.game.minesweeper.mineSweeper;


import com.danleinbach.game.design.entities.GameEntity;
import com.danleinbach.game.design.mediaLoaders.audioLoader.GameAudioLoader;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MineSweeperGrid extends GameEntity<Integer> {


	private static final int ORIGINAL_WAIT_FRAMES = 30;
	private ArrayList<Point> cascadeBlocks;
	private MineTile grid[][];
	private Rectangle background;
	private int rows;
	private int cols;
	private int bombs;
	private int boxSpace;
	private int remaining;
	private static boolean cascading = false;
	private int cascadeWaitFrames;
	private Integer boxSize;
	private ArrayList<Point> cascadeBombs;
	private ArrayList<Point> alreadyChecked;
	private String message;
	private double frameSpeed = 1;
	private Timer t;

	public MineSweeperGrid(int pWidth, int pHeight, int rows, int cols, int bombs) {
		super(pWidth, pHeight);

		this.cascadeBlocks = new ArrayList<Point>();
		this.cascadeBombs = new ArrayList<Point>();
		this.alreadyChecked = new ArrayList<Point>();
		this.rows = rows;
		this.cols = cols;
		this.bombs = bombs;
		grid = new MineTile[rows][cols];
		sizeGrid(pWidth, pHeight);
		t = new Timer();
		setBombs();
	}

	public void sizeGrid(int width, int height) {
		drawWidth = width;
		drawHeight = height;
		background = new Rectangle(0, 0, drawWidth, drawHeight);
//		int boxWidth = (pWidth - (boxSpace*(cols+2))) / cols;
//		int boxHeight = (pHeight - (boxSpace*(rows+2))) / rows;
		int boxWidth = (int) (((double) drawWidth) / cols);
		int boxHeight = (int) (((double) drawHeight) / rows);
		boxSize = boxWidth < boxHeight ? boxWidth : boxHeight;
		boxSpace = 0;
//		boxSpace = (int) (.1 * boxSize);
		System.out.println(boxSize);
		int startX;
		int x = startX = (drawWidth / 2) - (((boxSize + boxSpace) * cols) / 2);
		int y = (drawHeight / 2) - (((boxSize + boxSpace) * rows) / 2);
		//		int x = startX = boxSpace;
		//		int y = boxSpace;
		for(int rowIndex = 0; rowIndex < rows; rowIndex++) {
			for(int colIndex = 0; colIndex < cols; colIndex++) {
				MineTile mt = grid[rowIndex][colIndex];
				if(mt == null) {
					grid[rowIndex][colIndex] = new MineTile(new Rectangle(x, y, boxSize, boxSize));
				} else {
					grid[rowIndex][colIndex].getShape().setSize(boxSize, boxSize);
					grid[rowIndex][colIndex].getShape().setLocation(x, y);
				}


				x += boxSize + boxSpace;
			}
			y += boxSize + boxSpace;
			x = startX;
		}

	}

	private void setBombs() {
		int x, y;
		int count = 0;
		while(count < bombs) {
			x = (int) (Math.random() * rows);
			y = (int) (Math.random() * cols);
			if(grid[x][y].getValue() == MineTile.BOMB) {
				continue;
			}
			grid[x][y].setAsBomb();
			count++;
			for(int r = - 1; r <= 1; r++) {
				for(int c = - 1; c <= 1; c++) {
					if((x + r) >= 0 && (x + r) < rows) {
						if((y + c) >= 0 && (y + c) < cols) {
							grid[x + r][y + c].updateValue();
						}
					}
				}
			}
		}
	}

	public void mouseOver(int x, int y) {
		for(int rowIndex = 0; rowIndex < rows; rowIndex++) {
			for(int colIndex = 0; colIndex < cols; colIndex++) {
				grid[rowIndex][colIndex].setHover(x, y);
			}
		}
	}

	public boolean isCascading() {
		return cascading;
	}

	@Override
	public Integer update() {
		if(cascadeBlocks.size() > 0 && cascadeWaitFrames-- == 0) {
			ArrayList<Point> oldList = new ArrayList<Point>(cascadeBlocks);
			//			Collections.copy(oldList, cascadeBlocks);
			cascadeBlocks.clear();
			for(Point p : oldList) {
				cascade(p.x, p.y);
			}
			cascadeWaitFrames = 6;
			cascading = ! cascadeBlocks.isEmpty();
		} else if(cascadeBombs.size() > 0 && cascadeWaitFrames-- == 0) {

			ArrayList<Point> oldList = new ArrayList<Point>(cascadeBombs);
			//			Collections.copy(oldList, cascadeBlocks);
			cascadeBombs.clear();
			for(Point p : oldList) {
				bombCascade(p.x, p.y);
			}
			if(! (cascading = ! cascadeBombs.isEmpty())) {
				setChanged();
				notifyObservers(message);
			}

			cascadeWaitFrames = (int) (ORIGINAL_WAIT_FRAMES * frameSpeed);
			frameSpeed *= .9;
		} else if(message == null) {
			checkForWin();
		}
		return 0;
	}

	private void checkForWin() {

		int count = 0;
		remaining = 0;

		for(int rowIndex = 0; rowIndex < rows; rowIndex++) {
			for(int colIndex = 0; colIndex < cols; colIndex++) {
				MineTile t = grid[rowIndex][colIndex];
				if(t.isFlagged()) {
					remaining++;
				}
				if(t.isHidden() == true) {
					count++;
				}
			}
		}
		remaining = bombs - remaining;

		if(count == bombs) {
			setChanged();
			notifyObservers(MineSweeperPanel.WIN);
		}


	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.darkGray);
		g.fill(background);
		for(int rowIndex = 0; rowIndex < rows; rowIndex++) {
			for(int colIndex = 0; colIndex < cols; colIndex++) {
				grid[rowIndex][colIndex].draw(g);

			}
		}
	}

	@Override
	public Integer update(int x, int y) {
		for(int rowIndex = 0; rowIndex < rows; rowIndex++) {
			for(int colIndex = 0; colIndex < cols; colIndex++) {
				int val = grid[rowIndex][colIndex].update(x, y);
				if(val == 0) {
					t.schedule(tt, 12);
					cascade(rowIndex, colIndex);
					return - 2;
				} else if(val == - 1) {
					bombCascade(rowIndex, colIndex);
					message = MineSweeperPanel.GAME_OVER;

				}

			}
		}
		return - 2;

	}

	TimerTask tt = new TimerTask() {

		@Override
		public void run() {
			System.out.println("Played");
			GameAudioLoader.getInstance().getAudio("good").play();
		}
	};

	private void bombCascade(int x, int y) {
		for(int r = - 1; r <= 1; r++) {
			for(int c = - 1; c <= 1; c++) {
				if((x + r) >= 0 && (x + r) < rows) {
					if((y + c) >= 0 && (y + c) < cols) {
						if(grid[x + r][y + c].getValue() == MineTile.BOMB && grid[x + r][y + c].isHidden() == true) {
							if(grid[x + r][y + c].isFlagged()) {
								grid[x + r][y + c].setFlagged();
							}
							grid[x + r][y + c].update();

						}
						Point p = new Point(x + r, y + c);
						if(! alreadyChecked.contains(p)) {
							cascadeBombs.add(p);
							alreadyChecked.add(p);
						}
					}
				}
			}
		}

	}

	private void cascade(int x, int y) {
		for(int r = - 1; r <= 1; r++) {
			for(int c = - 1; c <= 1; c++) {
				if((x + r) >= 0 && (x + r) < rows) {
					if((y + c) >= 0 && (y + c) < cols) {
						int val = grid[x + r][y + c].update();
						if(val == 0) {
							cascadeBlocks.add(new Point(x + r, y + c));
						}
						//							cascade(x+r, y+c);
					}
				}
			}
		}

	}

	public void update(int x, int y, int button) {
		if(cascadeBlocks.size() > 0) {
			return;
		}

		if(button == MouseEvent.BUTTON3) {
			for(int rowIndex = 0; rowIndex < rows; rowIndex++) {
				for(int colIndex = 0; colIndex < cols; colIndex++) {
					grid[rowIndex][colIndex].setFlagged(x, y);
				}
			}
		} else {
			update(x, y);
		}


	}

	public int getRemaining() {
		return remaining;
	}

}
