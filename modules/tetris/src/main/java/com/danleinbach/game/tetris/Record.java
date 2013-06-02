package com.danleinbach.game.tetris;

public class Record
		implements Comparable<Record> {
	private String name;
	private Integer score;

	public Record() {
		this.name = "";
		this.score = 0;
	}

	public Record(String name, int score) {
		this.name = name;
		this.score = score;
	}

	public String toString() {
		return this.name + ":" + this.score;
	}

	public static Record fromString(String record) {
		String[] fields = record.split(":");
		return new Record(fields[0], Integer.parseInt(fields[1]));
	}

	public String getName() {
		return this.name;
	}

	public Integer getScore() {
		return this.score;
	}

	public int compareTo(Record o) {
		return o.getScore().compareTo(this.score);
	}
}

/* Location:           C:\Users\Daniel\Desktop\Tetris.jar
 * Qualified Name:     Record
 * JD-Core Version:    0.6.2
 */