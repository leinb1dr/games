package com.danleinbach.game.design.windowElements.bufferTypes;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class BufferFactory {

	public static BufferProvider getBuffer(BufferStrategy strategy, Container panel, int width, int height) {
		if(strategy != null) {
			return new FullScreenBuffer(strategy);
		}
		return new WindowedBuffer(panel, width, height);
	}


	private static class FullScreenBuffer implements BufferProvider {

		private BufferStrategy strategy;

		public FullScreenBuffer(BufferStrategy strategy) {
			this.strategy = strategy;
		}

		public Graphics2D getGraphics() {
			return (Graphics2D) strategy.getDrawGraphics();

		}

		public void draw() {
			if(! strategy.contentsLost()) {
				strategy.show();
			}

		}

		public void resize(int width, int height) {
		}
	}

	private static class WindowedBuffer implements BufferProvider {

		private Container panel;
		private BufferedImage image;

		public WindowedBuffer(Container panel, int width, int height) {
			this.panel = panel;
			this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		}

		public Graphics2D getGraphics() {
			return (Graphics2D) image.getGraphics();

		}

		public void draw() {

			Graphics2D g = (Graphics2D) panel.getGraphics();
			if(g == null) {
				try {
					Thread.sleep(500);
				} catch(InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				g = (Graphics2D) panel.getGraphics();
			}
			g.drawImage(image, 0, 0, null);
			Toolkit.getDefaultToolkit().sync();
			g.dispose();

		}

		public void resize(int width, int height) {
			this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		}
	}


}
