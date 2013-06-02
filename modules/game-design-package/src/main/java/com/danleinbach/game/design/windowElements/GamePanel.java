package com.danleinbach.game.design.windowElements;

import com.danleinbach.game.design.windowElements.bufferTypes.BufferFactory;
import com.danleinbach.game.design.windowElements.bufferTypes.BufferProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.text.DecimalFormat;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * GamePanel is a panel that controls the updating and rendering of the
 * game. The panel is used in a JFrame and can be added to any window.
 * The private method {@link #gameUpdate()} is a protected class and
 * is implemented in the concrete implementing class.
 *
 * @author Daniel
 */
public abstract class GamePanel extends JPanel
		implements Runnable, Observer, ComponentListener, MouseListener, MouseMotionListener {

	private static final int MAX_FRAME_SKIPS = 5;
	private static final long MAX_STATS_INTERVAL = 1000L;
	private static final int NO_DELAYS_PER_YIELD = 16;
	protected static final int NUM_FPS = 10;

	private static final long serialVersionUID = 1L;

	private ExecutorService animatorService;
	private boolean running;

	private int frameCount;
	private int framesSkipped;
	private int totalFramesSkipped;
	private double averageFPS;
	protected double[] fpsHistory;
	private double averageUPS;
	protected double[] upsHistory;

	private Graphics2D dbg;
//	private Image dbImage;

	private DecimalFormat df = new DecimalFormat("0.##");
	protected Font font;
	protected FontMetrics metrics;

	protected boolean win;
	protected boolean gameOver;
	protected boolean isPaused;

	private long gameStartTime;
	protected long sleepTime;
	protected int inGameTime;
	private long totalRealTime;
	private long prevStatsTime;

	protected int pHeight = 0;
	protected int pWidth = 0;

	private int statsCount;
	private long statsInterval;
	protected boolean debug;

	private BufferProvider buffer;
	private boolean resized;

	/**
	 * Default constructor
	 */
	public GamePanel() {
		super();
	}

	/**
	 * Parameterized constructor that takes three parameters: one long
	 * and two integers.
	 * <p/>
	 * This constructor is used to set the sleep time and to tell the panel
	 * how large the game surface is.
	 *
	 * @param sleepTime - How long the game will sleep for
	 * @param pWidth    - The width of the game area
	 * @param pHeight   - The height of the game area
	 */
	public GamePanel(long sleepTime, int pWidth, int pHeight) {
		this(sleepTime, pWidth, pHeight, null);

	}

	public GamePanel(long sleepTime, int pWidth, int pHeight,
					 BufferStrategy strategy) {

		buffer = BufferFactory.getBuffer(strategy, this, pWidth, pHeight);

		this.sleepTime = sleepTime;
		this.pWidth = pWidth;
		this.pHeight = pHeight;
		addComponentListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

		setBackground(Color.white);

		setFocusable(true);
		requestFocus();
		readyForTermination();

		font = new Font("SansSerif", Font.BOLD, 24);
		metrics = this.getFontMetrics(font);

		fpsHistory = new double[NUM_FPS];
		upsHistory = new double[NUM_FPS];
		for(int i = 0; i < NUM_FPS; i++) {
			fpsHistory[i] = 0.0;
			upsHistory[i] = 0.0;
		}

	}

	/**
	 * Start the game when the panel is added to a component.
	 *
	 * @see javax.swing.JComponent#addNotify()
	 */
	@Override
	public void addNotify() {
		super.addNotify();
		this.startGame();
	}

	/**
	 * Print any game over messages.
	 * <p/>
	 * Since game over messages are unique to the game, its design
	 * is left up to the implementor.
	 *
	 * @param g -Graphics object that contains screen elements
	 */
	protected abstract void gameOver(Graphics2D g);

	/**
	 * Game rendering is the second step in the game display process. This
	 * mehtod creates and/or updates the tools necessary for drawing on the screen.
	 * <p/>
	 * Creates a new image the size of the game play area. If the game play area
	 * changes in size the image must be resized. Then the image is used to create
	 * a graphics object.
	 * <p/>
	 * Then a white canvas is painted the size of the play area
	 */
	private void gameRender() {
		if(resized) {
			buffer.resize(pWidth, pHeight);
		}
		this.dbg = buffer.getGraphics();

		this.dbg.setColor(Color.white);
		this.dbg.fillRect(0, 0, pWidth, pHeight);

		this.dbg.setColor(Color.black);

		this.paintEntities(dbg);

		if(debug) {
			this.dbg.setColor(Color.blue);
			this.dbg.setFont(this.font);

			this.dbg.drawString(
					"Average FPS/UPS: " + this.df.format(this.averageFPS) + ", "
							+ this.df.format(this.averageUPS), 20, 25);
		}
	}

	/**
	 * This is the first step in the game display process. This performs natural
	 * game updates. This can be anything that should occure every frame.
	 */
	protected abstract void gameUpdate();

	/**
	 * Used to paint any entites that exist in the game.
	 * <p/>
	 * Since the number of entities is unknown to each individual game this
	 * is left up to the implementor.
	 *
	 * @param g - Reference to a graphics object that contains screen elements
	 */
	protected abstract void paintEntities(Graphics2D g);

	private void paintScreen() {


		if(this.gameOver && ! this.win && this.running) {
			this.gameOver(this.dbg);
		} else if(this.gameOver && this.win && this.running) {
			this.win(dbg);
		}

		buffer.draw();

	}

	/**
	 * Sets the pause flag to true.
	 * <p/>
	 * The pause flag tells the game whether or not to update
	 * the entities.
	 */
	public void pauseGame() {
		this.isPaused = true;
	}

	/**
	 * Prints all of the statistics stored about the game.
	 * <p/>
	 * This is called when the game has ended and prints out things like
	 * the frame count, average ups/fps, and the game time.
	 */
	private void printDebugStats() {
		System.out.println("Frame Count/Loss: " + this.frameCount + " / "
				+ this.totalFramesSkipped);
		System.out.println("Average FPS: " + this.df.format(this.averageFPS));
		System.out.println("Average UPS: " + this.df.format(this.averageUPS));
		System.out.println("Time Spent: " + this.inGameTime + " secs");
	}

	/**
	 * Ready for terminiation adds a key listener that allows a user to end the
	 * game with classic button clicks, Escape, Q, End, and C with control held.
	 * The result is to set the game running to false.
	 * TODO this is a candidate for change. How can we change the keys or make
	 * them do different things
	 */
	protected void readyForTermination() {
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if(keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_Q
						|| keyCode == KeyEvent.VK_END
						|| keyCode == KeyEvent.VK_C && e.isControlDown()) {
					GamePanel.this.running = false;
				}
			}
		});
	}

	/**
	 * Resume game sets the game flag to false.
	 * Since the game flag indicates that the game should not update,
	 * resume clears it and sets it to false so the game will continue updating.
	 */
	public void resumeGame() {
		this.isPaused = false;
	}

	/**
	 * Run is required when implementing runnable. This method is called
	 * when a thread is told to start.
	 * <p/>
	 * The process this method uses is:
	 * <ol>
	 * <li>Update</li>
	 * <li>Render</li>
	 * <li>Draw</li>
	 * <ol>
	 * This controls the flow of the game. First updates to the game screen
	 * and any entities occur. Then the rendering of the game screen and the
	 * entities to a buffer. Finally the buffered image is drawn to the screen.
	 * The last part of the game is to sleep to produce the proper frames per second.
	 *
	 * @see Runnable#run()
	 */
	public void run() {
		long beforeTime, afterTime, timeDiff, sleepTime;
		long overSleepTime = 0L;
		int noDelays = 0;
		long excess = 0L;
		this.gameStartTime = System.nanoTime();
		this.prevStatsTime = this.gameStartTime;
		beforeTime = this.gameStartTime;

		this.running = true;
		while(this.running) {

			this.gameUpdate();
			this.gameRender();
			this.paintScreen();

			afterTime = System.nanoTime();
			timeDiff = afterTime - beforeTime;
			sleepTime = this.sleepTime - timeDiff - overSleepTime;

			if(sleepTime > 0) {
				try {
					Thread.sleep(sleepTime / 1000000L);
				} catch(InterruptedException ex) {
				}
				overSleepTime = System.nanoTime() - afterTime - sleepTime;
			} else {
				excess -= sleepTime;
				overSleepTime = 0L;

				if(++ noDelays >= NO_DELAYS_PER_YIELD) {
					Thread.yield();
					noDelays = 0;
				}
			}

			beforeTime = System.nanoTime();

			int skips = 0;
			while(excess > this.sleepTime && skips < MAX_FRAME_SKIPS) {
				excess -= this.sleepTime;
				this.gameUpdate();
				skips++;
			}

			this.framesSkipped += skips;

			this.debugStatistics();
		}

		this.printDebugStats();
	}

	public void waitForTermination() {
		animatorService.shutdown();
		try {
			System.out.println(animatorService.awaitTermination(sleepTime, TimeUnit.MILLISECONDS));
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Starting the game includes creating a new thread and starting it.
	 * Since this game panel implements runnable, it is passed to the thread
	 * and started.
	 */
	private void startGame() {
		if(this.animatorService == null || ! this.running) {
			animatorService = Executors.newFixedThreadPool(1);
			animatorService.execute(new Thread(this));
		}
	}

	/**
	 * Stop game sets the running flag to false. This will stop the thread from
	 * looping and exit.
	 */
	public void stopGame() {
		this.running = false;
	}

	/**
	 * Debug statistics records information that is useful to determining
	 * performance. These include frames/updates per second, in game and total
	 * time, and frame count.
	 */
	private void debugStatistics() {

		this.frameCount++;
		this.statsInterval += this.sleepTime;

		if(this.statsInterval >= MAX_STATS_INTERVAL) {

			// Time in Game
			long currentGameTime = System.nanoTime();
			this.inGameTime = (int) ((currentGameTime - this.gameStartTime) / 1000000000L);

			// Total time elapsed
			long realTime = currentGameTime - this.prevStatsTime;
			this.totalRealTime += realTime;

			this.totalFramesSkipped += this.framesSkipped;

			double actualFPS = 0;
			double actualUPS = 0;

			if(this.totalRealTime > 0) {
				actualFPS = (double) this.frameCount / this.totalRealTime
						* 1000000000L;

				actualUPS = ((double) this.frameCount + this.totalFramesSkipped)
						/ this.totalRealTime * 1000000000L;
			}

			this.fpsHistory[this.statsCount % NUM_FPS] = actualFPS;
			this.upsHistory[this.statsCount % NUM_FPS] = actualUPS;
			this.statsCount++;

			double totalFPS = 0.0;
			double totalUPS = 0.0;

			for(int i = 0; i < NUM_FPS; i++) {
				totalFPS += this.fpsHistory[i];
				totalUPS += this.upsHistory[i];
			}

			if(this.statsCount < NUM_FPS) {
				this.averageFPS = totalFPS / this.statsCount;
				this.averageUPS = totalUPS / this.statsCount;

			} else {
				this.averageFPS = totalFPS / NUM_FPS;
				this.averageUPS = totalFPS / NUM_FPS;
			}

			this.framesSkipped = 0;
			this.prevStatsTime = currentGameTime;
			this.statsInterval = 0L;

		}

	}

	/**
	 * Display the win screen. This can be any number of things so it's design
	 * is left up to the implementor.
	 *
	 * @param g - Reference to a graphics object containing screen elements
	 */
	protected abstract void win(Graphics2D g);

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
		resized = true;
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

	/**
	 * An override of the mouseClicked method of the MouseListener. Its
	 * actions are up to the implementor. This is invoked when the mouse is pressed
	 * and released.
	 *
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
	}

	/**
	 * An override of the mouseEntered method of the MouseListener. Its
	 * actions are up to the implementor. This is invoked when the mouse enters
	 * an object.
	 *
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * An override of the mousePressed method of the MouseListener. Its
	 * actions are up to the implementor. This is invoked when the mouse is
	 * pressed on an object.
	 *
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * An override of the mouseExited method of the MouseListener. Its
	 * actions are up to the implementor. This is invoked when the mouse leaves
	 * an object.
	 *
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * An override of the mouseReleased method of the MouseListener. Its
	 * actions are up to the implementor. This is invoked when the mouse is released
	 * an object.
	 *
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * An override of the mouseDragged method of the MouseMotionListener. Its
	 * actions are up to the implementor. This is invoked when the mouse is pressed
	 * and moved.
	 *
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent arg0) {
	}

	/**
	 * An override of the mouseMoved method of the MouseMotionListener. Its
	 * actions are up to the implementor. This is invoked when the mouse is moved.
	 *
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
	}


}