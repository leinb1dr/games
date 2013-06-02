package com.danleinbach.game.minesweeper.mineSweeper;


import com.danleinbach.game.design.windowElements.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;

/**
 * MineSweeperWindow is the main window for the Mine Sweeper application.
 * <p/>
 * This holds the frame and any other panels for displaying information. This
 * windows calculates the size of the game surface and adds the new game panel
 * to itself.
 *
 * @author Daniel
 */
public class MineSweeperWindow extends GameWindow {


	private static final long serialVersionUID = 1L;
	protected static final String CUSTOM_GAME = "CUSTOM_GAME";
	protected static final String NEW_GAME = "NEW_GAME";
	private JTextField timeText;
	private JTextField bombsText;
	protected NewMineSweeper newGameDialog;

	/**
	 * Constructs a new MineSweeperWindow with a sleep time of the given period.
	 * It also sets the minimun size to be 800 and the default close operation to
	 * exit on close.
	 *
	 * @param period
	 */
	public MineSweeperWindow(int period) {
		super(period, "Mine Sweeper");
		setMinimumSize(new Dimension(800, 800));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/**
	 * Make GUI adds all components to the screen. This GUI has a
	 * menu bar with options for exiting and starting a new game.
	 * it also has two text boxes to display some game information.
	 *
	 * @see GameWindow#constructUserInterface(int)
	 */
	@Override
	protected void constructUserInterface(final int period) {
		final Container c = getContentPane();    // default BorderLayout used

		JPanel ctrls = new JPanel();   // a row of textfields
		ctrls.setLayout(new BoxLayout(ctrls, BoxLayout.X_AXIS));

		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu("File");

		MenuItem scores = new MenuItem("Records");
		scores.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new DisplayScores();
			}
		});

		MenuItem newGame = new MenuItem("New Game");

		// New Game action listener that creates a new game and set the screen to 10 rows
		// 10 columns and 5 bombs.
		newGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				NewGameDialog newDialog = new NewGameDialog("New Game");
				newDialog.setName(NEW_GAME);
				newDialog.addWindowListener(MineSweeperWindow.this);

				newGameDialog = newDialog;
			}
		});

		MenuItem customGame = new MenuItem("Custom Game");
		// Custom game action listener that displays a menu for creating a game of custom size.
		customGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PropertiesWindow newDialog = new PropertiesWindow();
				newDialog.setName(CUSTOM_GAME);
				newDialog.addWindowListener(MineSweeperWindow.this);

				newGameDialog = newDialog;

			}

		});

		MenuItem exitItem = new MenuItem("Exit");

		// Adds an action listener that exits the game.
		exitItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(gp != null) {
					gp.stopGame();
				}
				gp.waitForTermination();
				MineSweeperWindow.this.dispose();

			}
		});

		menu.add(newGame);
		menu.add(customGame);
		menu.add(scores);
		menu.add(exitItem);
		menuBar.add(menu);

		setMenuBar(menuBar);

		timeText = new JTextField();
		timeText.setFocusable(false);

		bombsText = new JTextField();
		bombsText.setFocusable(false);

		JPanel text = new JPanel(new GridLayout(1, 2));
		text.add(timeText);
		text.add(bombsText);

		c.add(text, "South");
	}

	@Override
	protected void instantiateGameWindow() {

	}

	/**
	 * Main method taht starts execution of the game.
	 *
	 * @param args
	 */
	public static void main(String args[]) {
		mainBootStrap(args);

		new MineSweeperWindow(sleepTime);

	}

	/**
	 * Displays the number of bombs remaining.
	 * <p/>
	 * The number of bombs remaining is determined by the MineSweeperGrid.
	 *
	 * @param remaining - The number of bombs remaining
	 * @see MineSweeperGrid#getRemaining()
	 */
	public void setBombs(int remaining) {
		bombsText.setText("Bombs left: " + remaining);
	}

	/**
	 * Sets the current time of the execution of the game. This method is
	 * called by the game panel when the time updates.
	 *
	 * @param timeSpentInGame - Number of seconds in the game
	 */
	public void setTime(int timeSpentInGame) {
		timeText.setText("Game Time: " + timeSpentInGame + " secs");

	}

	/**
	 * When the window closes end the current game.
	 * If the window that closed was not the MineSweeperWindow i.e. the
	 * Properties window, then create a new game from the properties set
	 * in the properties window.
	 *
	 * @see GameWindow#windowClosed(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosed(WindowEvent e) {
		Container c = getContentPane();
//		boolean confirmed = false;
		if(gp != null) {
			gp.stopGame();
			gp.waitForTermination();
			c.remove(gp);
			gp = null;
		}


		if(newGameDialog.isConfirm()) {

			Insets frameInsets = getInsets();
			Dimension bottom = bombsText.getPreferredSize();

			setResizable(true);

			int pWidth = getWidth() - (frameInsets.left + frameInsets.right);
			int pHeight = (int) (getHeight() - (frameInsets.top + frameInsets.bottom) - bottom.getHeight());

			gp = new MineSweeperPanel(sleepTime, pWidth, pHeight, newGameDialog.getRows(),
					newGameDialog.getCols(), newGameDialog.getBombs(), MineSweeperWindow.this);
			c.add(gp, "Center");
			validate();
		} else {
			System.out.println("Cancel");
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		super.windowClosing(e);
		RecordProvider.getInstance().saveRecords();
	}


	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void setFullScreen() {
		// TODO Auto-generated method stub

	}

}
