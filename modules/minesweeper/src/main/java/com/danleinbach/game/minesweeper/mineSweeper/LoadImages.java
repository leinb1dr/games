package com.danleinbach.game.minesweeper.mineSweeper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;


public class LoadImages {

	private static LoadImages loadImages;

	public static LoadImages getInstance() {
		if(loadImages == null) {
			loadImages = new LoadImages();
		}
		return loadImages;
	}


	HashMap<String, BufferedImage[]> images;
	private GraphicsConfiguration gc;

	private LoadImages() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
		images = new HashMap<String, BufferedImage[]>();
	}

	public BufferedImage[] getImageList(String name) {
		return images.get(name);
	}

	public void loadSingleImage(String name, String ext) {
		String string = name + ext;
		BufferedImage[] tmp = new BufferedImage[1];

		tmp[0] = loadImage(string);

		images.put(name, tmp);
	}

	public void loadImageList(String name, String ext, int i) {
		String string = name + ext;
		BufferedImage[] tmp = new BufferedImage[i];

		for(int x = 0; x < i; x++) {
			tmp[x] = loadImage(string.replace('*', Integer.toString(x).charAt(0)));
		}

		images.put(name, tmp);
	}

	private BufferedImage loadImage(String string) {
		try {
			BufferedImage im = ImageIO.read(
					getClass().getResource("images/" + string));
			// An image returned from ImageIO in J2SE <= 1.4.2 is 
			// _not_ a managed image, but is after copying!

			int transparency = im.getColorModel().getTransparency();
			BufferedImage copy = gc.createCompatibleImage(
					im.getWidth(), im.getHeight(),
					transparency);
			// create a graphics context
			Graphics2D g2d = copy.createGraphics();
			// g2d.setComposite(AlphaComposite.Src);

			// reportTransparency(IMAGE_DIR + fnm, transparency);

			// copy image
			g2d.drawImage(im, 0, 0, null);
			g2d.dispose();
			return copy;
		} catch(IOException e) {
			return null;
		}

	}

}
