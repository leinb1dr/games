package com.danleinbach.game.design.mediaLoaders.audioLoader;

import com.danleinbach.game.design.mediaLoaders.audioLoader.audioTypes.AudioFactory;
import com.danleinbach.game.design.mediaLoaders.audioLoader.audioTypes.AudioType;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameAudioLoader {

	private final String REG_EX = "S [a-zA-Z0-9]+ [a-zA-Z0-9]+\\.(wav) [0-9]+" +
			"|M [a-zA-Z0-9]+ [a-zA-Z0-9]+\\.(mid|midi)";

	private static GameAudioLoader loadAudio;
	private boolean loaded;

	public static GameAudioLoader getInstance() {
		if(loadAudio == null) {
			loadAudio = new GameAudioLoader();
		}
		return loadAudio;
	}

	private HashMap<String, AudioType> audio;

	private ExecutorService audioLoaderService;

	private GameAudioLoader() {
		audio = new HashMap<String, AudioType>();
	}

	public AudioType getAudio(String name) {
		return audio.get(name);
	}

	public void loadAudio(String fileName) throws FileNotFoundException {
		if(! loaded) {
			audioLoaderService = Executors.newFixedThreadPool(20);
			Scanner reader =
					new Scanner(getClass().getClassLoader().getResourceAsStream(AudioType.AUDIO_DIRECTORY + fileName));
			while(reader.hasNext()) {
				String line = reader.nextLine();
				if(line.matches(REG_EX)) {
					audioLoaderService.execute(new ImageLoadThread(line));
				}

			}
			reader.close();
			audioLoaderService.shutdown();
			try {
				System.out.println(audioLoaderService.awaitTermination(10, TimeUnit.MINUTES));
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public HashMap<String, AudioType> getImages() {
		return audio;
	}

	private class ImageLoadThread implements Runnable {

		private String line;

		public ImageLoadThread(String line) {
			this.line = line;
		}

		public void run() {
			Entry<String, AudioType> tmp = AudioFactory.getAudio(line);
			synchronized(audio) {
				audio.put(tmp.getKey(), tmp.getValue());
			}
		}

	}


}
