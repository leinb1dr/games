package com.danleinbach.game.tetris;


import com.danleinbach.game.design.entities.GameEntity;
import com.danleinbach.game.design.mediaLoaders.imageLoader.GameImageLoader;

import java.awt.*;
import java.util.ArrayList;

public class DisplayRecords extends GameEntity {
	private Rectangle background;
	private ArrayList<Record> records;
	private String listOfScores;
	private int rows;

	public DisplayRecords(int pWidth, int pHeight) {
		super(pWidth, pHeight);

		this.records = RecordProvider.getInstance().getRecords();
	}

	public Object update() {
		this.records = RecordProvider.getInstance().getRecords();
		return null;
	}

	public void draw(Graphics2D g) {
		g.setFont(new Font("Times New Roman", 1, 24));
		String title = String.format("RANK %14s\t%12s", new Object[] {centerText("NAME", 14), centerText("SCORE", 12)});

		int width = g.getFontMetrics().stringWidth(title);
		int height = g.getFontMetrics().getHeight();
		float startingY = 48.0F;
		float startingX = (this.drawWidth / 2.0F - width) / 2.0F;

		g.setColor(Color.gray);
		int recWidth = (int) (width * 1.1F);
		int recHeight = (int) (height * (this.records.size() + 2) * 1.1F);
		g.drawImage(GameImageLoader.getInstance().getImage("scores").getImage(null),
				(int) ((this.drawWidth / 2.0F - recWidth) / 2.0F), 24, recWidth, recHeight, null);

		g.setColor(Color.white);
		g.drawString(title,
				startingX,
				startingY);
		int count = 0;
		synchronized(this.records) {
			for(Record r : this.records) {
				String scores = String.format("%4s %14s\t%12s", new Object[] {centerText(Integer.toString(++ count), 4),
						centerText(r.getName(), 14), centerText(Integer.toString(r.getScore().intValue()), 12)});

				g.drawString(scores,
						startingX,
						startingY + height * (count + 1));
			}
		}
	}

	public Object update(int x, int y) {
		return null;
	}

	private String centerText(String text, int i) {
		char[] word = new char[i];

		int offset = (int) Math.floor((i - text.length()) / 2.0D);
		for(int x = 0; x < i; x++) {
			if((x >= offset) && (x < text.length() + offset)) {
				word[x] = text.charAt(x - offset);
			} else {
				word[x] = ' ';
			}
		}
		return String.valueOf(word);
	}
}

/* Location:           C:\Users\Daniel\Desktop\Tetris.jar
 * Qualified Name:     DisplayRecords
 * JD-Core Version:    0.6.2
 */