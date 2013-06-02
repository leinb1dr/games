package com.danleinbach.game.design.mediaLoaders.audioLoader.audioTypes;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.StringTokenizer;

public class SampledAudio extends AudioType implements LineListener {

	private Clip[] clip;
	private boolean forceStop = false;


	@Override
	protected String loadAudio(String line) {
		StringTokenizer tokenizer = new StringTokenizer(line);

		// First token is identifier
		tokenizer.nextToken();
		// Second token is name
		String name = tokenizer.nextToken();
		// Third token is filename
		String fileName = tokenizer.nextToken();
		// Fourth token is how many instances should be loaded
		String cnt = tokenizer.nextToken();
		System.out.println(cnt);
		int count = Integer.parseInt(cnt);
		clip = new Clip[count];
		try {
			for(int x = 0; x < count; x++) {
				// link an audio stream to the sound clip's file
				AudioInputStream stream = AudioSystem.getAudioInputStream(
						getClass().getClassLoader().getResource(AUDIO_DIRECTORY + fileName));

				AudioFormat format = stream.getFormat();

				// convert ULAW/ALAW formats to PCM format
				if((format.getEncoding() == AudioFormat.Encoding.ULAW) ||
						(format.getEncoding() == AudioFormat.Encoding.ALAW)) {
					AudioFormat newFormat =
							new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
									format.getSampleRate(),
									format.getSampleSizeInBits() * 2,
									format.getChannels(),
									format.getFrameSize() * 2,
									format.getFrameRate(), true);  // big endian
					// update stream and format details
					stream = AudioSystem.getAudioInputStream(newFormat, stream);
					System.out.println("Converted Audio format: " + newFormat);
					format = newFormat;
				}

				DataLine.Info info = new DataLine.Info(Clip.class, format);

				// make sure sound system supports data line
				if(! AudioSystem.isLineSupported(info)) {
					System.out.println("Unsupported Clip File: " + fileName);
					return "";
				}


				// get clip line resource
				clip[x] = (Clip) AudioSystem.getLine(info);

				// listen to clip for events
				clip[x].addLineListener(this);

				clip[x].open(stream);    // open the sound file as a clip
				stream.close(); // we're done with the input stream
			}

		} catch(LineUnavailableException e) {
		} catch(IOException e) {
		} catch(UnsupportedAudioFileException e) {
			e.printStackTrace();
		}

		return name;
	}


	@Override
	public synchronized void play() {
		for(int x = 0; x < clip.length; x++) {
			if(! clip[x].isActive()) {
				clip[x].start();
				System.out.println("Played audio: " + x +
						" Is active: " + clip[x].isActive());
				break;
			}
		}

	}

	@Override
	public void pause() {

	}

	@Override
	public void stop() {
		for(int x = 0; x < clip.length; x++) {
			if(clip[x].isActive()) {
				forceStop = true;
				clip[x].stop();
				clip[x].setFramePosition(0);
			}
		}

	}

	@Override
	public void close() {
		//		clip.close();

	}

	public void update(LineEvent arg0) {
		if(LineEvent.Type.STOP.equals(arg0.getType())) {

			((Clip) (arg0.getSource())).stop();
			((Clip) (arg0.getSource())).setFramePosition(0);
			if(looping && ! forceStop) {
				((Clip) (arg0.getSource())).start();
			}

			forceStop = false;
		}

	}

}
