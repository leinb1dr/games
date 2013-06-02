package com.danleinbach.game.design.mediaLoaders.audioLoader.audioTypes;

import com.danleinbach.game.design.mediaLoaders.audioLoader.GameAudioLoader;

import javax.sound.midi.*;

public class SequencerHandler implements MetaEventListener, SequenceWatcher {

	private static final int END_OF_TRACK = 47;
	private static SequencerHandler handler;
	private String sequenceName;

	public static SequencerHandler getInstance() {
		if(handler == null) {
			handler = new SequencerHandler();
		}
		return handler;
	}

	private Sequencer sequencer;

	private SequencerHandler() {
		initSequencer();
	}

	public Sequencer getSequencer() {
		return sequencer;
	}

	private void initSequencer() 
	/* Set up the MIDI sequencer, and the sequencer's meta-event
		 listener. No synthesizer is used here. */ {
		try {
			sequencer = MidiSystem.getSequencer();
			if(sequencer == null) {
				System.out.println("Cannot get a sequencer");
				return;
			}

			sequencer.open();
			sequencer.addMetaEventListener(this);

			// maybe the sequencer is not the same as the synthesizer
			// so link sequencer --> synth (this is required in J2SE 1.5)
			if(! (sequencer instanceof Synthesizer)) {
				System.out.println("Linking the MIDI sequencer and synthesizer");
				Synthesizer synthesizer = MidiSystem.getSynthesizer();
				synthesizer.open();  // new
				Receiver synthReceiver = synthesizer.getReceiver();
				Transmitter seqTransmitter = sequencer.getTransmitter();
				seqTransmitter.setReceiver(synthReceiver);
			}
		} catch(MidiUnavailableException e) {
			System.out.println("No sequencer available");
			sequencer = null;
		}
	} // end of initSequencer()

	public void meta(MetaMessage meta) {
		if(meta.getType() == END_OF_TRACK) {
			sequencer.stop();
			sequencer.setTickPosition(0);
			if(GameAudioLoader.getInstance().getAudio(sequenceName).looping) {
				sequencer.start();
			}


		}
	}

	public void setSongName(String name) {
		sequenceName = name;
	}

}


