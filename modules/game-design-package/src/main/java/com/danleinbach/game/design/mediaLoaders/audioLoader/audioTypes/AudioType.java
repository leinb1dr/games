package com.danleinbach.game.design.mediaLoaders.audioLoader.audioTypes;

public abstract class AudioType {

	public static String AUDIO_DIRECTORY = "audio/";
	protected boolean looping = false;

	protected abstract String loadAudio(String line);

	public abstract void play();

	public abstract void pause();

	public abstract void stop();

	public abstract void close();

	public void setLooping(boolean looping) {
		this.looping = looping;
	}

}
