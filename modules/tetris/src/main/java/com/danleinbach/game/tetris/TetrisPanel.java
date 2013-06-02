package com.danleinbach.game.tetris;


import com.danleinbach.game.design.mediaLoaders.audioLoader.GameAudioLoader;
import com.danleinbach.game.design.mediaLoaders.imageLoader.GameImageLoader;
import com.danleinbach.game.design.mediaLoaders.imageLoader.imageTypes.ReelImage;
import com.danleinbach.game.design.windowElements.GamePanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Observable;

public class TetrisPanel extends GamePanel {
	private static final long serialVersionUID = 1L;
	public static final String GAME_OVER = "GAME_OVER";
	public static final String START_GAME = "START_GAME";
	public static final String UPDATE_RECORDS = "UPDATE_RECORDS";
	private int waitTime;
	private int level = 1;
	private int updateFail;
	private PlayArea pa;
	private PlayArea nextShapeArea;
	private PlayArea holdShapeArea;
	private CurrentBlock currentBlock;
	private CurrentBlock nextBlock;
	private CurrentBlock heldBlock;
	private int lines;
	private int score;
	private MenuPopUp gameOverMenu;
	private DisplayRecords display;
	private SpeakerButton custom;
	private SpeakerButton classic;
	KeyListener gameKeys = new KeyListener() {
		public void keyTyped(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
				case 39:
					TetrisPanel.this.currentBlock.update(1, 0);
					break;
				case 37:
					TetrisPanel.this.currentBlock.update(- 1, 0);
					break;
				case 38:
					TetrisPanel.this.currentBlock.rotateLeft();
					break;
				case 40:
					TetrisPanel.this.currentBlock.update(0, 1);
					break;
				case 10:
					TetrisPanel.this.currentBlock.rotate();
					break;
				case 17:
					if(TetrisPanel.this.heldBlock.getShape() != null) {
						Shapes tmpBlock = TetrisPanel.this.currentBlock.getShape();
						if(TetrisPanel.this.currentBlock.swap(TetrisPanel.this.heldBlock.getShape())) {
							TetrisPanel.this.heldBlock.swap(tmpBlock);
						}
					} else {
						TetrisPanel.this.heldBlock.swap(TetrisPanel.this.currentBlock.getShape());
						TetrisPanel.this.currentBlock.swap(TetrisPanel.this.nextBlock.getShape());
						TetrisPanel.this.nextBlock.nextBlock();
					}
					TetrisPanel.this.heldBlock.center();
			}
		}
	};

	KeyListener keyboardReader = new KeyListener() {
		public void keyTyped(KeyEvent e) {
			if(TetrisPanel.this.gameOverMenu != null) {
				String tmp = Character.toString(e.getKeyChar());

				if(tmp.matches("[a-zA-Z]")) {
					TetrisPanel.this.gameOverMenu.appendName(e.getKeyChar());
				}
			}
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == 8) {
				TetrisPanel.this.gameOverMenu.backSpace();
			}
		}
	};

	public TetrisPanel(int sleepTime, int pWidth, int pHeight, BufferStrategy strategy) {
		super(sleepTime, pWidth, pHeight, strategy);
		this.debug = false;
		this.gameOver = true;

		addMouseListener(this);

		this.pa = new PlayArea(18, 10, 56, 24, pWidth, pHeight);
		this.pa.addObserver(this);
		this.nextShapeArea = new PlayArea(5, 6, 648, 24, pWidth, pHeight);
		this.holdShapeArea = new PlayArea(5, 6, 648, 248, pWidth, pHeight);

		this.currentBlock = new CurrentBlock(pWidth, pHeight, this.pa);
		this.nextBlock = new CurrentBlock(pWidth, pHeight, this.nextShapeArea);
		this.heldBlock = new CurrentBlock(pWidth, pHeight, this.holdShapeArea);

		this.custom = new SpeakerButton(648, 700, pWidth, pHeight, GameAudioLoader.getInstance().getAudio("beat2"));
		this.classic = new SpeakerButton(700, 700, pWidth, pHeight, GameAudioLoader.getInstance().getAudio("beat1"));
	}

	public void update(Observable arg0, Object arg1) {
		if((arg1 instanceof String)) {
			if(arg1.equals("GAME_OVER")) {
				this.gameOver = true;
				this.gameOverMenu = new MenuPopUp(this.pWidth, this.pHeight, this.score);
				this.gameOverMenu.addObserver(this);
				this.display = new DisplayRecords(this.pWidth, this.pHeight);

				addMouseMotionListener(this);

				addKeyListener(this.keyboardReader);
				removeKeyListener(this.gameKeys);
			} else if(arg1.equals("START_GAME")) {
				this.display = null;
				removeMouseMotionListener(this);

				removeKeyListener(this.keyboardReader);
				addKeyListener(this.gameKeys);
				this.gameOver = false;
				this.level = 1;
				this.lines = 0;
				this.score = 0;

				this.gameOverMenu.deleteObservers();
				this.gameOverMenu = null;

				this.pa.clear();

				this.nextBlock.nextBlock();
				this.currentBlock.reset(this.nextBlock.getShape());
				this.nextBlock.nextBlock();
				this.heldBlock.reset(null);
			} else if((arg1.equals("UPDATE_RECORDS")) &&
					(this.display != null)) {
				this.display.update();
			}
		} else if((arg1 instanceof Score)) {
			Score s = (Score) arg1;
			this.lines += s.getLines();
			this.score += s.getScore();
			this.level = ((int) (this.lines / 8.0D) + 1);
		}
	}

	protected void gameOver(Graphics2D g) {
		if(this.gameOverMenu == null) {
			this.gameOverMenu = new MenuPopUp(this.pWidth, this.pHeight, - 1);
			this.gameOverMenu.addObserver(this);

			addMouseMotionListener(this);

			addKeyListener(this.keyboardReader);
		}

		this.gameOverMenu.draw(g);
	}

	protected void gameUpdate() {
		if((! this.gameOver) && (! this.isPaused) &&
				(this.waitTime-- <= 0)) {
			if(this.currentBlock.update(0, 1).booleanValue()) {
				this.updateFail = 0;
			} else {
				this.updateFail += 1;
			}
			if(this.updateFail >= 2) {
				this.currentBlock.commit();
				this.currentBlock.reset(this.nextBlock.getShape());
				this.nextBlock.nextBlock();
				this.updateFail = 0;
			}
			this.waitTime = (50 - this.level * 3);
		}
	}

	public void mouseClicked(MouseEvent e) {
		if(this.gameOverMenu != null) {
			this.gameOverMenu.update(e.getX(), e.getY(), e.getButton());
		}
		if(this.custom.update(e.getX(), e.getY(), e.getButton())) {
			this.classic.update();
		} else if(this.classic.update(e.getX(), e.getY(), e.getButton())) {
			this.custom.update();
		}
	}

	public void mouseMoved(MouseEvent e) {
		if(this.gameOverMenu != null) {
			this.gameOverMenu.update(e.getX(), e.getY(), 0);
		}
	}

	protected void paintEntities(Graphics2D g) {
		int x = 100;
		int y = 100;
		this.pa.draw(g);
		this.nextShapeArea.draw(g);
		this.holdShapeArea.draw(g);
		this.currentBlock.draw(g);
		this.nextBlock.draw(g);
		this.heldBlock.draw(g);

		if(this.display != null) {
			this.display.draw(g);
		}
		g.drawImage(GameImageLoader.getInstance().getImage("bg").getImage(null), 0, 0, null);
		paintScores(g);
		this.custom.draw(g);
		this.classic.draw(g);
	}

	private void paintScores(Graphics2D g) {
		BufferedImage bi = new BufferedImage(240, 50, 2);
		Graphics2D g2 = (Graphics2D) bi.getGraphics();
		g2.setColor(Color.white);
		g2.setFont(new Font("Times New Roman", 1, 25));

		g2.drawImage(((ReelImage) GameImageLoader.getInstance().getImage("block")).getImage(Integer.valueOf(2)), 0, 0,
				240, 50, null);

		FontMetrics fm = g2.getFontMetrics();
		String msg = "Level: " + this.level;
		int width = fm.stringWidth(msg);
		int height = fm.getHeight();
		g2.drawString(msg,
				(bi.getWidth() - width) / 2.0F,
				bi.getHeight() - height + (bi.getHeight() - height) / 2);

		g.drawImage(bi, 648, 472, null);

		g2.drawImage(((ReelImage) GameImageLoader.getInstance().getImage("block")).getImage(Integer.valueOf(3)), 0, 0,
				240, 50, null);

		msg = "Lines: " + this.lines;
		width = fm.stringWidth(msg);
		height = fm.getHeight();
		g2.drawString(msg,
				(bi.getWidth() - width) / 2.0F,
				bi.getHeight() - height + (bi.getHeight() - height) / 2);

		g.drawImage(bi, 648, 547, null);

		g2.drawImage(((ReelImage) GameImageLoader.getInstance().getImage("block")).getImage(Integer.valueOf(4)), 0, 0,
				240, 50, null);

		msg = "Score: " + this.score;
		width = fm.stringWidth(msg);
		height = fm.getHeight();
		g2.drawString(msg,
				(bi.getWidth() - width) / 2.0F,
				bi.getHeight() - height + (bi.getHeight() - height) / 2);

		g.drawImage(bi, 648, 622, null);
	}

	protected void win(Graphics2D g) {
	}

	public KeyListener getGameKeyListener() {
		return this.gameKeys;
	}
}

/* Location:           C:\Users\Daniel\Desktop\Tetris.jar
 * Qualified Name:     TetrisPanel
 * JD-Core Version:    0.6.2
 */