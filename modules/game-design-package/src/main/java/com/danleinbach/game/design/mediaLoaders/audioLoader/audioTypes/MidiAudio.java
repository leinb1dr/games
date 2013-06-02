package com.danleinbach.game.design.mediaLoaders.audioLoader.audioTypes;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import java.io.IOException;
import java.util.StringTokenizer;

public class MidiAudio extends AudioType {

	private Sequence sequence;
	private Sequencer sequencer;
	private SequenceWatcher watcher;
	private String name;

	MidiAudio(Sequencer sqr) {
		sequencer = sqr;
		watcher = SequencerHandler.getInstance();
	}

	@Override
	protected String loadAudio(String line) {
		StringTokenizer tokenizer = new StringTokenizer(line);

		// First token is identifier
		tokenizer.nextToken();
		// Second token is name
		name = tokenizer.nextToken();
		// Third token is filename
		String fileName = tokenizer.nextToken();

		try {
			sequence = MidiSystem.getSequence(getClass().getClassLoader().getResource(AUDIO_DIRECTORY + fileName));
		} catch(InvalidMidiDataException e) {

		} catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return name;

	}

	@Override
	public synchronized void play() {
		try {
			watcher.setSongName(name);
			sequencer.setSequence(sequence);
			sequencer.start();
		} catch(InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void pause() {
//		clip.stop();

	}

	@Override
	public void stop() {
		sequencer.stop();
		sequencer.setTickPosition(0);

	}

	@Override
	public void close() {
//		clip.close();

	}

}
