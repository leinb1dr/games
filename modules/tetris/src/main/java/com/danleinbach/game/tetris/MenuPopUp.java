package com.danleinbach.game.tetris;


import com.danleinbach.game.design.entities.GameEntity;
import com.danleinbach.game.design.mediaLoaders.imageLoader.GameImageLoader;
import com.danleinbach.game.design.mediaLoaders.imageLoader.imageTypes.SingleImage;
import com.danleinbach.game.design.mediaLoaders.imageLoader.imageTypes.StateImage;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class MenuPopUp extends GameEntity<Void> {
	private final int width = 400;
	private final int height = 400;
	private Rectangle background;
	private Ellipse2D.Float startButton;
	private boolean hoverStartButton;
	private Ellipse2D.Float enterButton;
	private boolean hideEnterButton;
	private boolean hoverEnterButton;
	private int score;
	private StringBuffer name = new StringBuffer(14);
	private int blink = 0;

	public MenuPopUp(int pWidth, int pHeight, int score) {
		super(pWidth, pHeight);
		this.score = score;
		int x = (int) ((pWidth - 400) / 2.0D);
		int y = (int) ((pHeight - 400) / 2.0D);
		this.background = new Rectangle(x, y, 400, 400);
		this.startButton = new Ellipse2D.Float(125 + x, y + 300, 150.0F, 50.0F);
		this.enterButton = new Ellipse2D.Float(40 + x, y + 300, 150.0F, 50.0F);
		this.hideEnterButton = (score <= 0);
	}

	public Void update() {
		return null;
	}

	public void draw(Graphics2D g) {
		SingleImage img = (SingleImage) GameImageLoader.getInstance().getImage("title");

		g.setColor(Color.white);
		g.drawImage(img.getImage(null), this.background.x, this.background.y, this.background.width,
				this.background.height, null);
		StateImage si = (StateImage) GameImageLoader.getInstance().getImage("button");
		this.startButton.x = ((this.drawWidth - 400) / 2.0F + 125.0F);
		g.setFont(new Font("Time New Roman", 1, 25));

		if((this.score >= 0) && (! this.hideEnterButton)) {
			FontMetrics fm = g.getFontMetrics();
			String msg = "Score " + this.score;
			int width = fm.stringWidth(msg);
			int height = fm.getHeight();
			g.drawString("Score: " + this.score,
					this.background.x + (this.background.width - width) / 2.0F,
					this.background.y + (this.background.height - height) / 2.0F);

			float recordName = this.background.y + (this.background.height - height) / 2.0F + height * 1.5F;
			width = fm.stringWidth("New Record: " + this.name);
			height = fm.getHeight();
			g.drawString("New Record: " + this.name,
					this.background.x + (this.background.width - width) / 2.0F,
					recordName);
			if(this.blink++ < 60) {
				g.fillRect((int) (this.background.x + (this.background.width - width) / 2.0F + width),
						(int) (recordName - fm.getHeight()), fm.stringWidth("M"), fm.getHeight());
			} else if(this.blink++ > 80) {
				this.blink = 0;
			}

			g.setColor(Color.black);
			if(! this.hideEnterButton) {
				BufferedImage bi;
				if(this.hoverEnterButton) {
					bi = si.getImage("hover");
				} else {
					bi = si.getImage("normal");
				}
				g.drawImage(bi, (int) this.enterButton.x, (int) this.enterButton.y, null);
				g.setColor(Color.black);
				g.setFont(new Font("Time New Roman", 1, 25));
				g.drawString("Save", this.enterButton.x + 50.0F, this.enterButton.y + 30.0F);

				this.startButton.x += 85.0F;
			}

		}

		g.setColor(Color.black);
		BufferedImage bi;
		if(this.hoverStartButton) {
			bi = si.getImage("hover");
		} else {
			bi = si.getImage("normal");
		}
		g.drawImage(bi, (int) this.startButton.x, (int) this.startButton.y, null);

		g.drawString("Start", this.startButton.x + 50.0F, this.startButton.y + 30.0F);
	}

	public Void update(int x, int y) {
		return null;
	}

	public void update(int x, int y, int mouseButton) {
		if(this.startButton.contains(new Point2D.Float(x, y))) {
			this.hoverStartButton = true;
			if(mouseButton == 1) {
				setChanged();
				notifyObservers("START_GAME");
			}
		} else {
			this.hoverStartButton = false;
		}
		if(! this.hideEnterButton) {
			if(this.enterButton.contains(new Point2D.Float(x, y))) {
				this.hoverEnterButton = true;
				if(mouseButton == 1) {
					RecordProvider rp = RecordProvider.getInstance();
					if((this.score > 0) && (rp.isScoreRecord(this.score))) {
						rp.addRecord(new Record(this.name.toString(), this.score));
					}
					setChanged();
					notifyObservers("UPDATE_RECORDS");
					this.hideEnterButton = true;
				}
			} else {
				this.hoverEnterButton = false;
			}
		}
	}

	public void appendName(char keyChar) {
		if(this.name.length() < this.name.capacity()) {
			this.name.append(keyChar);
		}
	}

	public void backSpace() {
		if(this.name.length() > 0) {
			this.name.deleteCharAt(this.name.length() - 1);
		}
	}
}