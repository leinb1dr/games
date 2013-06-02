package com.danleinbach.game.design.mediaLoaders.imageLoader.imageTypes;

import java.io.IOException;
import java.util.Map.Entry;

public abstract class ImageFactory {

	public static Entry<String, ImageType<?>> getImage(String s) {
		ImageType<?> image = null;
		String name = "";
		char loadType = s.charAt(0);

		switch(loadType) {
			case 'I':
				image = new SingleImage();
				break;
			case 'S':
				image = new StateImage();
				break;
			case 'R':
				image = new ReelImage();
				break;
		}
		if(image != null) {
			try {
				name = image.loadImages(s);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}

		return new ImageEntry(name, image);
	}

	private static class ImageEntry implements Entry<String, ImageType<?>> {

		private String key;
		private ImageType<?> value;

		private ImageEntry(String key, ImageType<?> value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public ImageType<?> getValue() {
			return value;
		}

		public ImageType<?> setValue(ImageType<?> value) {
			return this.value = value;
		}

	}

}
