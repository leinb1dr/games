package com.danleinbach.game.minesweeper.mineSweeper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public abstract class ReadWriteScores {

	private static final String file = "score.txt";

	public static ArrayList<Record> getRecords() throws IOException {

		ArrayList<Record> records = new ArrayList<Record>(5);
		File f = new File(file);
		if(! f.exists()) {
			f.createNewFile();
		}
		Scanner scan = new Scanner(f);
		while(scan.hasNext()) {
			records.add(Record.fromString(scan.nextLine()));
		}
		scan.close();
		return records;
	}

	public static void saveRecords(ArrayList<Record> records) throws IOException {
		File f = new File(file);
		if(! f.exists()) {
			f.createNewFile();
		}
		FileWriter fw = new FileWriter(f);
		int count = 0;
		while(count < records.size()) {
			fw.write(records.get(count).toString() + '\n');
			count++;
		}
		fw.close();

	}

}
