package com.danleinbach.game.design.mediaLoaders.imageLoader.imageTypes;

import java.awt.image.BufferedImage;
import java.io.IOException;


public abstract class ImageType<E> {

	public static final String imageDirectory = "images/";

	public abstract BufferedImage getImage(E arg0);

	public abstract String loadImages(String s) throws IOException;

}
