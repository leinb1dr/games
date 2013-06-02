package com.danleinbach.game.minesweeper.mineSweeper;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;


public class DisplayScores extends JFrame {


	private static final long serialVersionUID = 1L;

	public DisplayScores() {

		try {
			addComponents();
			setDefaultCloseOperation(HIDE_ON_CLOSE);
			pack();
			setVisible(true);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private JTextArea getScores(Difficulty d) {
		JTextArea scores = new JTextArea(10, 25);
		scores.setBackground(getBackground());
		scores.setEditable(false);
		scores.setFont(new Font("Courier New", Font.PLAIN, 16));

		ArrayList<Record> records = RecordProvider.getInstance().getRecordsForDifficulty(d);
		String nameTitle = "Names";
		String scoreTitle = "Scores";
		String listOfScores = String.format("# %10s\t%6s\n", centerText(nameTitle, 10), centerText(scoreTitle, 6));
		int count = 0;
		for(Record r : records) {
			listOfScores += ++ count + " " + String.format("%10s\t%6s", centerText(r.getName(), 10),
					centerText(Integer.toString(r.getScore()), 6)) + "\n";
		}

		scores.setText(listOfScores);

		return scores;

	}

	private void addComponents() throws IOException {

		JTabbedPane scoresTabbedPane = new JTabbedPane();
		scoresTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		for(Difficulty d : Difficulty.values()) {
			scoresTabbedPane.addTab(d.name(), getScores(d));
		}

		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				DisplayScores.this.dispose();
			}
		});

		Container c = getContentPane();
		GroupLayout layout = new GroupLayout(c);
		c.setLayout(layout);

		SequentialGroup hGroup = layout.createSequentialGroup();
		ParallelGroup components = layout.createParallelGroup();

		hGroup.addContainerGap();
		components.addComponent(scoresTabbedPane, Alignment.CENTER);
		components.addComponent(ok, Alignment.TRAILING);
		hGroup.addGroup(components);
		hGroup.addContainerGap();

		SequentialGroup vGroup = layout.createSequentialGroup();
		vGroup.addContainerGap();
		vGroup.addComponent(scoresTabbedPane);
		vGroup.addPreferredGap(ComponentPlacement.UNRELATED);
		vGroup.addComponent(ok);
		vGroup.addContainerGap();

		layout.setHorizontalGroup(hGroup);
		layout.setVerticalGroup(vGroup);
	}

	private String centerText(String text, int i) {
		char[] word = new char[i];

		int offset = (int) Math.floor((((double) i - text.length()) / 2));
		for(int x = 0; x < i; x++) {
			if(x >= offset && x < text.length() + offset) {
				word[x] = text.charAt(x - offset);
			} else {
				word[x] = ' ';
			}
		}
		return String.valueOf(word);
	}

}
