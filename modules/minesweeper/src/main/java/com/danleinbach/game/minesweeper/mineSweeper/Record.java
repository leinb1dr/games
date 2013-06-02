package com.danleinbach.game.minesweeper.mineSweeper;

public class Record implements Comparable<Record> {

	private Difficulty difficulty;
	private String name;
	private Integer score;

	public Record() {
		this.name = "";
		this.score = 0;
	}

	public Record(Difficulty difficulty, String name, int score) {
		this.setDifficulty(difficulty);
		this.name = name;
		this.score = score;
	}


	@Override
	public String toString() {
		return getDifficulty().toString() + ":" + name + ":" + score;

	}

	public static Record fromString(String record) {
		String[] fields = record.split(":");
		return new Record(Difficulty.valueOf(fields[0]), fields[1], Integer.parseInt(fields[2]));
	}

	public String getName() {
		return name;
	}

	public Integer getScore() {
		return score;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	@Override
	public int compareTo(Record o) {
		if(difficulty.equals(o.getDifficulty())) {
			return score.compareTo(o.getScore());
		} else {
			return difficulty.compareTo(o.getDifficulty());
		}
	}
}
