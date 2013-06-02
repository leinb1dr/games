package com.danleinbach.game.minesweeper.mineSweeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class RecordProvider {

	private static RecordProvider instance;

	public static RecordProvider getInstance() {
		if(instance == null) {
			instance = new RecordProvider();
		}
		return instance;
	}

	private boolean changed;
	private ArrayList<Record> records;

	private RecordProvider() {
		changed = false;
		loadRecords();
	}

	public ArrayList<Record> getRecordsForDifficulty(Difficulty d) {
		Difficulty currentDifficulty = Difficulty.getCurrentDifficulty();
		Difficulty.setCurrentDifficulty(d);
		ArrayList<Record> tmp = getCurrentLevelRecords();
		Difficulty.setCurrentDifficulty(currentDifficulty);
		return tmp;
	}

	public ArrayList<Record> getCurrentLevelRecords() {
		if(changed) {
			sortList();
		}

		ArrayList<Record> tmp = new ArrayList<Record>(records);

		for(Record r : records) {
			if(! r.getDifficulty().equals(Difficulty.getCurrentDifficulty())) {
				tmp.remove(r);
			}
		}

		return tmp;

	}

	private void sortList() {
		Collections.sort(records, recordComparator);
		Difficulty d = null;
		int count = 1;
		for(int x = 0; x < records.size(); x++) {
			if(d == null || ! d.equals(records.get(x).getDifficulty())) {
				d = records.get(x).getDifficulty();
				count = 1;
			}
			if(count > 5) {
				records.remove(x);
			}
			count++;

		}

	}

	private Comparator<Record> recordComparator = new Comparator<Record>() {
		@Override
		public int compare(Record o1, Record o2) {
			return o1.compareTo(o2);
		}
	};

	public void addRecord(Record r) {
		changed = true;
		records.add(r);
	}

	private void loadRecords() {
		try {
			records = ReadWriteScores.getRecords();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isScoreRecord(int score) {
		ArrayList<Record> tmp = getCurrentLevelRecords();
		if(tmp == null || tmp.isEmpty() || tmp.size() < 5) {
			return true;
		}
		Record max = Collections.max(tmp, recordComparator);
		return score < max.getScore();
	}

	public void saveRecords() {
		try {
			if(changed) {
				sortList();
			}
			ReadWriteScores.saveRecords(records);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		RecordProvider rp = new RecordProvider();
		rp.loadRecords();
		System.out.println(rp);
		rp.addRecord(new Record(Difficulty.MEDIUM, "Jim", 5));
		rp.addRecord(new Record(Difficulty.HARD, "Jim", 5));
		rp.addRecord(new Record(Difficulty.HARD, "Jim", 2));
		rp.addRecord(new Record(Difficulty.EASY, "TOM", 65));
		rp.addRecord(new Record(Difficulty.MEDIUM, "Jim", 2));
		rp.sortList();
		System.out.println(rp);
		Difficulty.setCurrentDifficulty(Difficulty.EASY);
		System.out.println(rp.isScoreRecord(5));
		System.out.println(rp.isScoreRecord(8));
		ArrayList<Record> tmp = rp.getCurrentLevelRecords();

		String string = "[";
		for(Record r : tmp) {
			string += r.toString() + ", ";
		}
		string = string.substring(0, string.length() - 2);
		string += "]";
		System.out.println(string);
	}

	@Override
	public String toString() {
		String string = "[";
		for(Record r : records) {
			string += r.toString() + ", ";
		}
		string = string.substring(0, string.length() - 2);
		string += "]";
		return string;
	}

}
