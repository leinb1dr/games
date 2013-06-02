package com.danleinbach.game.design.mediaLoaders.imageLoader.imageTypes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

public class StateImage extends ImageType<String> {

	private HashMap<String, BufferedImage> imageList;

	StateImage() {
		imageList = new HashMap<String, BufferedImage>();
	}

	public BufferedImage getImage(String name) {
		return imageList.get(name);
	}

	@Override
	public String loadImages(String s) throws IOException {

		StringTokenizer tokenizer = new StringTokenizer(s);
		// First token is identifier
		tokenizer.nextToken();
		// Second token is name that will be returned
		String name = tokenizer.nextToken();

		// Now the tokens come in stateName fileName pairs
		while(tokenizer.hasMoreTokens()) {
			String stateName = tokenizer.nextToken();
			String fileName = tokenizer.nextToken();

			BufferedImage tmp = ImageIO.read(
					getClass().getClassLoader().getResource(imageDirectory + fileName));

			imageList.put(stateName, tmp);
		}

		return name;

	}


}
