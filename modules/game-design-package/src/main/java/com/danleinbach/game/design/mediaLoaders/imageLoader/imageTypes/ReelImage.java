package com.danleinbach.game.design.mediaLoaders.imageLoader.imageTypes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.StringTokenizer;

public class ReelImage extends ImageType<Integer> {

	private BufferedImage[] imageList;

	ReelImage() {
		super();
	}

	@Override
	public BufferedImage getImage(Integer index) {
		return imageList[index];
	}

	public int getSize() {
		return imageList.length;
	}

	@Override
	public String loadImages(String s) throws IOException {
		StringTokenizer tokenizer = new StringTokenizer(s);
		// First token is identifier
		tokenizer.nextToken();
		// Second token is name that will be returned
		String name = tokenizer.nextToken();
		// Third token is the name of the file
		String fileName = tokenizer.nextToken();
		// Fourth token is the number of files in the series
		Integer numOfFiles = Integer.parseInt(tokenizer.nextToken());
		imageList = new BufferedImage[numOfFiles];
		// Now the tokens come in stateName fileName pairs
		for(int x = 0; x < numOfFiles; x++) {
			String tmpFile = imageDirectory + fileName.replace("*", Integer.toString(x));

			BufferedImage tmp = ImageIO.read(getClass().getClassLoader().getResource(tmpFile));

			imageList[x] = tmp;
		}

		return name;

	}

}
