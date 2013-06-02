package com.danleinbach.game.tetris;


import com.danleinbach.game.design.entities.GameEntity;
import com.danleinbach.game.design.mediaLoaders.audioLoader.audioTypes.AudioType;
import com.danleinbach.game.design.mediaLoaders.imageLoader.GameImageLoader;
import com.danleinbach.game.design.mediaLoaders.imageLoader.imageTypes.StateImage;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpeakerButton extends GameEntity<Void> {
	private int x;
	private int y;
	private BufferedImage speaker;
	private BufferedImage playing;
	private boolean isPlaying;
	private Rectangle hitBox;
	private AudioType audio;

	public SpeakerButton(int x, int y, int pWidth, int pHeight, AudioType audio) {
		super(pWidth, pHeight);

		this.audio = audio;
		this.x = x;
		this.y = y;
		StateImage map = (StateImage) GameImageLoader.getInstance().getImages().get("speaker");
		this.speaker = map.getImage("notPlaying");
		this.playing = map.getImage("playing");
		this.hitBox = new Rectangle(x, y, this.playing.getWidth(), this.playing.getHeight());
	}

	public Void update() {
		this.audio.stop();
		this.isPlaying = false;
		return null;
	}

	public void draw(Graphics2D g) {
		if(this.isPlaying) {
			g.drawImage(this.playing, this.x, this.y, null);
		}
		g.drawImage(this.speaker, this.x, this.y, null);
	}

	public Void update(int x, int y) {
		return null;
	}

	public AudioType getAudio() {
		return this.audio;
	}

	public boolean update(int x, int y, int mouseButton) {
		if(this.hitBox.contains(x, y)) {
			if(mouseButton == 1) {
				this.audio.play();
				return this.isPlaying = true;
			}
			update();
		}
		return false;
	}
}

/* Location:           C:\Users\Daniel\Desktop\Tetris.jar
 * Qualified Name:     SpeakerButton
 * JD-Core Version:    0.6.2
 */