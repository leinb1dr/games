package com.danleinbach.game.minesweeper.mineSweeper;


import com.danleinbach.game.design.mediaLoaders.audioLoader.GameAudioLoader;
import com.danleinbach.game.design.mediaLoaders.imageLoader.GameImageLoader;
import com.danleinbach.game.design.windowElements.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.util.Observable;

/**
 * MineSweeperPanel holds and controls the game play. It updates, renders,
 * and draws the graphics to the screen.
 *
 * @author Daniel
 */
public class MineSweeperPanel extends GamePanel {


	private static final long serialVersionUID = 1L;
	public static final String GAME_OVER = "BOOM!";
	public static final String WIN = "NO_BOOM!";
	private MineSweeperGrid msg;
	private MineSweeperWindow msw;
	//	private int remaining;
	private boolean endMessageDisplayed;

	/**
	 * Constructs a new MineSweeperPanel to hold the game and begin rendering.
	 *
	 * @param period            - Length of time to sleep
	 * @param pWidth            - Width of the screen
	 * @param pHeight           - Height of the screen
	 * @param rows              - Rows for the sweeper grid
	 * @param cols              - Columns for the sweeper grid
	 * @param bombs             - Number of bombs to hide
	 * @param mineSweeperWindow - a reference to the Window that is holding the pane
	 */
	public MineSweeperPanel(long period, int pWidth, int pHeight, int rows, int cols, int bombs,
							MineSweeperWindow mineSweeperWindow) {
		super(period, pWidth, pHeight);
		msw = mineSweeperWindow;
		msg = new MineSweeperGrid(pWidth, pHeight, rows, cols, bombs);
		msg.addObserver(this);

		GameImageLoader images = GameImageLoader.getInstance();
		GameAudioLoader audio = GameAudioLoader.getInstance();
		try {
			images.loadImages("imageFile.txt");
			audio.loadAudio("audioFile.txt");
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}


	}

	@Override
	public void mouseMoved(MouseEvent e) {
		msg.mouseOver(e.getX(), e.getY());
	}

	@Override
	public void componentResized(ComponentEvent e) {
		MineSweeperPanel.this.pWidth = getWidth();
		MineSweeperPanel.this.pHeight = getHeight();
		msg.sizeGrid(getWidth(), getHeight());
	}

	/**
	 * Paint all of the entities that MineSweeperPanel is responsible for.
	 *
	 * @see GamePanel#paintEntities(java.awt.Graphics2D)
	 */
	@Override
	protected void paintEntities(Graphics2D g) {
		msg.draw(g);

	}

	/**
	 * Update the game and any entities in the game as long as it is not
	 * game over or paused.
	 * <p/>
	 * If the game has been paused or set to game over then no updates
	 * should occur to entities
	 *
	 * @see GamePanel#gameUpdate()
	 */
	@Override
	protected void gameUpdate() {
		if(! gameOver && ! isPaused) {
			msw.setTime(inGameTime);
			msw.setBombs(msg.getRemaining());
			msg.update();
		}
	}

	/**
	 * Handle the mouse press on the game panel.
	 * <p/>
	 * Pass the mouse press to the MineSweeperGrid so
	 * it can determine if the click needs to be handled.
	 *
	 * @see GamePanel#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if(! isPaused && ! gameOver && ! msg.isCascading()) {
			msg.update(e.getX(), e.getY(), e.getButton());
		}
	}

	/**
	 * Display the game over message.
	 *
	 * @see GamePanel#gameOver(java.awt.Graphics2D)
	 */
	@Override
	protected void gameOver(Graphics2D g) {
		if(! endMessageDisplayed) {
			JOptionPane.showMessageDialog(null, "You Lost!");
			endMessageDisplayed = true;
		}


	}

	/**
	 * If one of the entities that MineSweeperPanel has changed in a way
	 * significant to MineSweeperPanel then it will notify MineSweeperPanel
	 * throught the Oberservable setChanged and notify methods.
	 *
	 * @see java.util.Observer#update(java.util.Observable, Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		if(arg.equals(MineSweeperPanel.GAME_OVER)) {
			gameOver = true;
			win = false;
		} else if(arg.equals(MineSweeperPanel.WIN)) {
			gameOver = true;
			win = true;
		}

	}

	/**
	 * Show win message
	 *
	 * @see GamePanel#win(java.awt.Graphics2D)
	 */
	@Override
	protected void win(Graphics2D g) {
		if(! endMessageDisplayed) {
			int score = inGameTime;
			RecordProvider rp = RecordProvider.getInstance();
			if(rp.isScoreRecord(score)) {
				String name = JOptionPane
						.showInputDialog(this, "You won!\nNew record\nScore: " + inGameTime + "\n Please enter Name:",
								"New Record!");
				rp.addRecord(new Record(Difficulty.getCurrentDifficulty(), name, score));

			} else {
				JOptionPane.showMessageDialog(this, "You won!\nScore: " + inGameTime);
			}

			endMessageDisplayed = true;
		}

	}

}
