package com.danleinbach.game.tetris;

import java.awt.*;
import java.util.ArrayList;

public enum Shapes {
	LINE(new int[][] {
			{1, 1, 1, 1}}),

	R_ELBOW(new int[][] {
			{2, 2, 2},
			{0, 0, 2}}),

	L_ELBOW(new int[][] {
			{0, 0, 3},
			{3, 3, 3}}),

	T(new int[][] {
			{0, 4},
			{4, 4, 4}}),

	S(new int[][] {
			{0, 5, 5},
			{5, 5}}),

	REVERS_S(new int[][] {
			{6, 6},
			{0, 6, 6}}),

	BOX(new int[][] {
			{7, 7},
			{7, 7}});

	public static final int size = 40;
	int[][] orientation;
	boolean rotated;

	public int[][] rotateRight(int[][] orientation) {
		int[][] newOrientation = new int[orientation[0].length][orientation.length];

		for(int x = 0; x < newOrientation.length; x++) {
			for(int y = 0; y < newOrientation[x].length; y++) {
				newOrientation[x][y] = orientation[(orientation.length - 1 - y)][x];
			}
		}
		return newOrientation;
	}

	public int[][] rotateLeft(int[][] orientation) {
		int[][] newOrientation = new int[orientation[0].length][orientation.length];

		for(int x = 0; x < newOrientation.length; x++) {
			for(int y = 0; y < newOrientation[x].length; y++) {
				newOrientation[(newOrientation.length - 1 - x)][(newOrientation[x].length - 1 - y)] =
						orientation[(orientation.length - 1 - y)][x];
			}
		}
		return newOrientation;
	}

	public ArrayList<Rectangle> getParts() {
		ArrayList r = new ArrayList();
		for(int x = 0; x < this.orientation.length; x++) {
			for(int y = 0; y < this.orientation[x].length; y++) {
				if(this.orientation[x][y] == 1) {
					r.add(new Rectangle(x * 40, y * 40, 40, 40));
				}
			}
		}
		return r;
	}

	public void draw(Graphics2D g, int locX, int locY) {
		g.setColor(Color.black);
		for(int x = 0; x < this.orientation.length; x++) {
			for(int y = 0; y < this.orientation[x].length; y++) {
				if(this.orientation[x][y] == 1) {
					g.fillRect(locX + x * 40, locY + y * 40, 40, 40);
				}
			}
		}
	}

	private Shapes(int[][] orientation) {
		this.orientation = orientation;
	}

	public int[][] getGrid() {
		return this.orientation;
	}
}

/* Location:           C:\Users\Daniel\Desktop\Tetris.jar
 * Qualified Name:     Shapes
 * JD-Core Version:    0.6.2
 */