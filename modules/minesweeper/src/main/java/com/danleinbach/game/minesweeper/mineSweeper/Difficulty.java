package com.danleinbach.game.minesweeper.mineSweeper;

public enum Difficulty {
	EASY, MEDIUM, HARD, CUSTOM;

	static {
		currentDifficulty = EASY;
	}

	private static Difficulty currentDifficulty;

	public static Difficulty getCurrentDifficulty() {
		return currentDifficulty;
	}

	public static void setCurrentDifficulty(Difficulty currentDifficulty) {
		Difficulty.currentDifficulty = currentDifficulty;
	}
}
