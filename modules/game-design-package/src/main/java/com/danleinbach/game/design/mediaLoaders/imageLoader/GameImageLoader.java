package com.danleinbach.game.design.mediaLoaders.imageLoader;

import com.danleinbach.game.design.mediaLoaders.imageLoader.imageTypes.ImageFactory;
import com.danleinbach.game.design.mediaLoaders.imageLoader.imageTypes.ImageType;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class GameImageLoader {

	private final String REG_EX = "R [a-zA-Z]+ [a-zA-Z]+\\*\\.(png|jpg) [0-9]" +
			"|I [a-zA-Z]+ [a-zA-Z]+\\.(png|jpg)" +
			"|S [a-zA-Z]+ [[a-zA-Z]+ [a-zA-Z]+\\.(png|jpg)]+";

	private static GameImageLoader loadImages;
	private ExecutorService animatorService;
	private boolean loaded = false;

	public static GameImageLoader getInstance() {
		if(loadImages == null) {
			loadImages = new GameImageLoader();
		}
		return loadImages;
	}

	private HashMap<String, ImageType<?>> images;

	private GameImageLoader() {
		images = new HashMap<String, ImageType<?>>();
	}

	public ImageType<?> getImage(String name) {
		return images.get(name);
	}

	public void loadImages(String fileName) throws FileNotFoundException {
		if(! loaded) {
			animatorService = Executors.newFixedThreadPool(20);
			Scanner reader =
					new Scanner(getClass().getClassLoader().getResourceAsStream(ImageType.imageDirectory + fileName));
			while(reader.hasNext()) {
				String line = reader.nextLine();
				if(line.matches(REG_EX)) {
					animatorService.execute(new ImageLoadThread(line));
				}

			}
			reader.close();
			animatorService.shutdown();
			try {
				System.out.println(animatorService.awaitTermination(10, TimeUnit.MINUTES));
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public HashMap<String, ImageType<?>> getImages() {
		return images;
	}

	private class ImageLoadThread implements Runnable {

		private String line;

		public ImageLoadThread(String line) {
			this.line = line;
		}

		public void run() {
			Entry<String, ImageType<?>> tmp = ImageFactory.getImage(line);
			synchronized(images) {
				images.put(tmp.getKey(), tmp.getValue());
			}
		}

	}
}
