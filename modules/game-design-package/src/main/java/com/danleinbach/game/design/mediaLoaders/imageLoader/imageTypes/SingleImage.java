package com.danleinbach.game.design.mediaLoaders.imageLoader.imageTypes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.StringTokenizer;

public class SingleImage extends ImageType<Void> {

	private BufferedImage image;

	SingleImage() {
		super();
	}

	@Override
	public BufferedImage getImage(Void v) {
		return this.image;
	}

	@Override
	public String loadImages(String s) throws IOException {
		StringTokenizer tokenizer = new StringTokenizer(s);
		// First token is identifier
		tokenizer.nextToken();
		// Second token is name that will be returned
		String name = tokenizer.nextToken();
		// Third token is the name of the file that will be loaded
		String fileName = tokenizer.nextToken();

		this.image = ImageIO.read(
				getClass().getClassLoader().getResource(imageDirectory + fileName));

		return name;


	}

}
