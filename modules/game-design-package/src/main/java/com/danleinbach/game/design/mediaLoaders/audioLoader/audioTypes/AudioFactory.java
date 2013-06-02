package com.danleinbach.game.design.mediaLoaders.audioLoader.audioTypes;

import java.util.Map.Entry;

public abstract class AudioFactory {

	public static Entry<String, AudioType> getAudio(String line) {
		AudioType audio = null;
		String name = "";
		char loadType = line.charAt(0);

		switch(loadType) {
			case 'S':
				audio = new SampledAudio();
				break;
			case 'M':
				audio = new MidiAudio(SequencerHandler.getInstance().getSequencer());
				break;
		}
		if(audio != null) {
			name = audio.loadAudio(line);
		}

		return new AudioEntry(name, audio);
	}


	private static class AudioEntry implements Entry<String, AudioType> {

		private String key;
		private AudioType value;

		public AudioEntry(String key, AudioType value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return this.key;
		}

		public AudioType getValue() {
			return this.value;
		}

		public AudioType setValue(AudioType value) {
			return this.value = value;
		}

	}
}
