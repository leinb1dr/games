package com.danleinbach.game.tetris;

import com.danleinbach.game.design.mediaLoaders.audioLoader.GameAudioLoader;
import com.danleinbach.game.design.mediaLoaders.imageLoader.GameImageLoader;
import com.danleinbach.game.design.windowElements.GameWindow;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.FileNotFoundException;


public class TetrisWindow extends GameWindow {
	private static final int panelWidth = 1024;
	private static final int panelHeight = 768;
	private BufferStrategy strategy;

	public TetrisWindow(int period) {
		super(period, "Tetris", 1024, 768);
		setIgnoreRepaint(true);
		setDefaultCloseOperation(3);
	}

	public void setFullScreen() {
	}

	protected void constructUserInterface(int sleepTime) {
		pack();
		Insets frameInsets = getInsets();
		try {
			GameImageLoader.getInstance().loadImages("imageFile.txt");
			GameAudioLoader.getInstance().loadAudio("audioFile.txt");
			GameAudioLoader.getInstance().getAudio("beat1").setLooping(true);
			GameAudioLoader.getInstance().getAudio("beat2").setLooping(true);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}

		int pWidth = 1024 + frameInsets.left;
		int pHeight = 768 + frameInsets.top;
		setMinimumSize(new Dimension(pWidth, pHeight));
		this.gp = new TetrisPanel(sleepTime, 1024, 768, this.strategy);
		addKeyListener(((TetrisPanel) this.gp).getGameKeyListener());
		setContentPane(this.gp);
		validate();
		setResizable(false);
	}

	public void windowClosing(WindowEvent e) {
		super.windowClosing(e);
		RecordProvider.getInstance().saveRecords();
	}

	protected void instantiateGameWindow() {
	}

	public static void main(String[] args) {
		args = new String[] {"80"};
		mainBootStrap(args);

		new TetrisWindow(sleepTime);
	}
}

/* Location:           C:\Users\Daniel\Desktop\Tetris.jar
 * Qualified Name:     TetrisWindow
 * JD-Core Version:    0.6.2
 */