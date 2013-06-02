package com.danleinbach.game.minesweeper.mineSweeper;


import com.danleinbach.game.design.entities.GameEntity;
import com.danleinbach.game.design.mediaLoaders.audioLoader.GameAudioLoader;
import com.danleinbach.game.design.mediaLoaders.imageLoader.GameImageLoader;
import com.danleinbach.game.design.mediaLoaders.imageLoader.imageTypes.ReelImage;
import com.danleinbach.game.design.mediaLoaders.imageLoader.imageTypes.StateImage;

import java.awt.*;

public class MineTile extends GameEntity<Integer> {

	public static final int BOMB = - 1;

	private int value;
	private boolean hover;
	private boolean hidden;
	private Rectangle shape;
	private boolean flagged;

	private GameImageLoader images;

	private int explosionAnimation = 0;

	private int wait = 0;

	private GameAudioLoader audio;

	public MineTile(Rectangle shape) {
		super(0, 0);
		this.shape = shape;
		this.value = 0;
		this.hover = false;
		this.hidden = true;
		this.flagged = false;

		audio = GameAudioLoader.getInstance();
		images = GameImageLoader.getInstance();

	}


	public boolean isFlagged() {
		return flagged;
	}

	public void setAsBomb() {
		value = MineTile.BOMB;
	}

	public void updateValue() {
		if(value != MineTile.BOMB) {
			value++;
		}
	}

	public boolean isHover() {
		return hover;
	}

	public void setHover(int x, int y) {
		this.hover = shape.contains(x, y);
	}

	public int getValue() {
		return value;
	}

	public boolean isHidden() {
		return hidden;
	}

	public Rectangle getShape() {
		return shape;
	}

	@Override
	public Integer update(int x, int y) {
		if(! flagged) {
			if(hover) {
				hidden = false;
				return value;
			}
		}
		return - 2;
	}

	@Override
	public void draw(Graphics2D g) {
		if(hidden) {
			StateImage si = (StateImage) images.getImage("tile");
			if(! hover && ! flagged) {
				g.drawImage(si.getImage("normal"), shape.x, shape.y, shape.width, shape.height, null);
			} else {
				if(flagged) {
					g.drawImage(si.getImage("flagged"), shape.x, shape.y, shape.width, shape.height, null);
				} else if(hover) {
					g.drawImage(si.getImage("hover"), shape.x, shape.y, shape.width, shape.height, null);
				}
			}

		} else {
			if(value == BOMB) {
				ReelImage ri = (ReelImage) images.getImage("boom");
				if(wait == 0) {
					audio.getAudio("boom").play();
				}
				g.drawImage(images.getImage("bombTile").getImage(null), shape.x, shape.y, shape.width, shape.height,
						null);
				g.drawImage(ri.getImage(explosionAnimation), shape.x, shape.y, shape.width, shape.height, null);
				if(wait++ % 8 == 0 && explosionAnimation < ri.getSize() - 1) {
					explosionAnimation++;
				}


			} else {
				ReelImage ri = (ReelImage) images.getImage("number");
				g.drawImage(ri.getImage(value), shape.x, shape.y, shape.width, shape.height, null);
			}

		}
	}


//	Thread playSound = new Thread(new Runnable() {
//		
//		private LineListener ll = new LineListener() {
//			
//			@Override
//			public void update(LineEvent event) {
//				if(LineEvent.Type.STOP == event.getType())
//					boom.close();
//				
//			}
//		};
//		
//		@Override
//		public void run() {
//			boom.addLineListener(ll);
//			boom.start();
//		}
//	});

	@Override
	public Integer update() {
		if(! flagged) {
			if(hidden) {
				hidden = false;
				return value;
			}
		}
		return - 2;

	}

	public void setFlagged() {
		this.flagged = ! this.flagged;
	}

	public void setFlagged(int x, int y) {
		if(shape.contains(x, y)) {
			this.flagged = ! this.flagged;
		}

	}


}
