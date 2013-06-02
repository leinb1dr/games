package com.danleinbach.game.tetris;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class RecordProvider {
	private static RecordProvider instance;
	private boolean changed;
	private ArrayList<Record> records;
	private Comparator<Record> recordComparator = new Comparator<Record>() {
		public int compare(Record o1, Record o2) {
			return o1.compareTo(o2);
		}
	};

	public static RecordProvider getInstance() {
		if(instance == null) {
			instance = new RecordProvider();
		}
		return instance;
	}

	private RecordProvider() {
		this.changed = false;
		loadRecords();
	}

	public ArrayList<Record> getRecords() {
		if(this.changed) {
			sortList();
		}
		return this.records;
	}

	private void sortList() {
		Collections.sort(this.records, this.recordComparator);
		for(int x = 10; x < this.records.size(); x++) {
			this.records.remove(x);
		}
	}

	public void addRecord(Record r) {
		this.changed = true;
		this.records.add(r);
	}

	private void loadRecords() {
		try {
			this.records = ReadWriteScores.getRecords();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isScoreRecord(int score) {
		if((this.records == null) || (this.records.isEmpty()) || (this.records.size() < 10)) {
			return true;
		}
		Record max = (Record) Collections.max(this.records, this.recordComparator);
		return score > max.getScore().intValue();
	}

	public void saveRecords() {
		try {
			if(this.changed) {
				sortList();
			}
			ReadWriteScores.saveRecords(this.records);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public String toString() {
		String string = "[";
		for(Record r : this.records) {
			string = string + r.toString() + ", ";
		}
		string = string.substring(0, string.length() - 2);
		string = string + "]";
		return string;
	}
}
