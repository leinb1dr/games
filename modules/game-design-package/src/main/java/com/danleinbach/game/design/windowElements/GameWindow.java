package com.danleinbach.game.design.windowElements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


public abstract class GameWindow extends JFrame implements WindowListener, ComponentListener {

	private static final long serialVersionUID = 1L;
	private static int STANDARD_FPS = 80;
	protected static int sleepTime;
	protected GamePanel gp;

	/**
	 * Default constructor
	 */
	public GameWindow() {
		super();

		addWindowListener(this);
		setResizable(false);
	}

	/**
	 * Parameterized constructor that takes a sleep period
	 * and the name of the window.
	 * <p/>
	 * This constructor calls super and gives it the title of
	 * the window. It then creates the gui and add the listeners.
	 * finally it sets itself to visible to be used.
	 *
	 * @param sleepTime - Normal sleep time
	 * @param title     - Text that appears in the title bar
	 */
	public GameWindow(int sleepTime, String title) {
		super(title);
		setFullScreen();
		constructUserInterface(sleepTime);

		addWindowListener(this);
		pack();
		setVisible(true);
	}

	public GameWindow(int sleepTime, String title, int minWidth, int minHeight) {
		super(title);

		addWindowListener(this);

		setFullScreen();
		setMinimumSize(new Dimension(minWidth, minHeight));
		constructUserInterface(sleepTime);

		setVisible(true);
	}

	public abstract void setFullScreen();

	/**
	 * Create all of the components for the game window and
	 * add them.
	 * <p/>
	 * Since this changes depending on the requirements of the game
	 * it is left abstract so the implementor can decide how to
	 * design the UI.
	 *
	 * @param sleepTime - Normal sleep time
	 */
	protected abstract void constructUserInterface(int sleepTime);


	/**
	 * An override of the windowActivated method of the WindowListener. This
	 * is designed to resume the game when the window is activated. If the
	 * game panel is null it does nothing.
	 *
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowActivated(WindowEvent e) {
		if(gp != null) {
			gp.resumeGame();
		}
	}

	/**
	 * An override of the windowDeactivated method of the WindowListener. This
	 * is designed to pause the game when the window is deactivated. If the
	 * game panel is null it does nothing.
	 *
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	public void windowDeactivated(WindowEvent e) {
		if(gp != null) {
			gp.pauseGame();
		}
	}


	/**
	 * An override of the windowDeiconified method of the WindowListener. This
	 * is designed to resume the game when the window is changed from being
	 * minimized to normal. If the game panel is null it does nothing.
	 *
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	public void windowDeiconified(WindowEvent e) {
		if(gp != null) {
			gp.resumeGame();
		}
	}

	/**
	 * An override of the windowIconified method of the WindowListener. This
	 * is designed to pause the game when the window is changed from being
	 * normal to minimized. If the game panel is null it does nothing.
	 *
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	public void windowIconified(WindowEvent e) {
		if(gp != null) {
			gp.pauseGame();
		}
	}


	/**
	 * An override of the windowClosing method of the WindowListener. This
	 * is designed to stop the game when the window is closing. If the
	 * game panel is null it does nothing.
	 *
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent e) {
		if(gp != null) {
			gp.stopGame();
			gp.waitForTermination();
		}
	}

	/**
	 * An override of the windowClosed method of the WindowListener. In the
	 * abstract class GameWindow it does nothing.
	 *
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	public void windowClosed(WindowEvent e) {
	}

	/**
	 * An override of the windowClosed method of the WindowListener. In the
	 * abstract class GameWindow it does nothing.
	 *
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	public void windowOpened(WindowEvent e) {
	}

	/**
	 * Boot strap code for starting a game window. This includes setting
	 * the frames per second and calculating the period of the sleep time.
	 *
	 * @param args - Any command line arguments, only 1 number is optional.
	 */
	public static void mainBootStrap(String args[]) {
		int framesPerSec = STANDARD_FPS;
		if(args.length != 0) {
			try {
				framesPerSec = Integer.parseInt(args[0]);
			} catch(NumberFormatException e) {
				System.err.println("Could not read custom FPS, setting " +
						"to default: " + STANDARD_FPS);
			}
		}

		sleepTime = (int) 1000.0 / framesPerSec;
		sleepTime *= 1000000L;
		System.out.println("fps: " + framesPerSec + "; period: " + sleepTime + " ms");
	}

	/**
	 * Abstract method that can be used to do any further creation of the GameWindow.
	 * TODO candidate for removal.
	 */
	protected abstract void instantiateGameWindow();

	/**
	 * An override of the componentHiddent method of the ComponentListener. Its
	 * actions are up to the implementor. This is invoked when the component has
	 * visible set to false
	 *
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 */
	public void componentHidden(ComponentEvent e) {
	}

	/**
	 * An override of the componentMoved method of the ComponentListener. Its
	 * actions are up to the implementor. This is invoked when the component location
	 * is changed.
	 *
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 */
	public void componentMoved(ComponentEvent e) {
	}

	/**
	 * An override of the componentResized method of the ComponentListener. Its
	 * actions are up to the implementor. This is invoked when the component size
	 * is changed.
	 *
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 */
	public void componentResized(ComponentEvent e) {
	}

	/**
	 * An override of the componentShow method of the ComponentListener. Its
	 * actions are up to the implementor. This is invoked when the component has
	 * visible set to true
	 *
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 */
	public void componentShown(ComponentEvent e) {
	}


}
