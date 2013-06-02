package com.danleinbach.game.minesweeper.mineSweeper;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class NewGameDialog extends JFrame implements NewMineSweeper, ActionListener {

	private static final long serialVersionUID = 1L;
	private final String EASY = "EASY";
	private final String MEDIUM = "MEDIUM";
	private final String HARD = "HARD";
	private final String OK = "OK";

	private int rows;
	private int cols;
	private int bombs;
	private boolean confirm;

	public NewGameDialog(String title) {
		super(title);

		initComponents();
		pack();
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
	}

	private void initComponents() {

		JRadioButton easy = new JRadioButton("Easy: 9x9 10 bombs ");
		easy.setName(EASY);
		easy.addActionListener(this);

		JRadioButton medium = new JRadioButton("Medium: 16x16 40 bombs");
		medium.setName(MEDIUM);
		medium.addActionListener(this);

		JRadioButton hard = new JRadioButton("Hard: 30x16 99 bombs");
		hard.setName(HARD);
		hard.addActionListener(this);

		ButtonGroup bg = new ButtonGroup();
		bg.add(easy);
		bg.add(medium);
		bg.add(hard);

		JButton okButton = new JButton("Ok");
		okButton.setName(OK);
		okButton.addActionListener(this);

		Container c = getContentPane();
		GroupLayout layout = new GroupLayout(c);

		SequentialGroup hGroup = layout.createSequentialGroup();
		ParallelGroup options = layout.createParallelGroup();

		options.addComponent(easy, Alignment.LEADING);
		options.addComponent(medium, Alignment.LEADING);
		options.addComponent(hard, Alignment.LEADING);
		options.addComponent(okButton, Alignment.TRAILING);

		hGroup.addContainerGap();
		hGroup.addGroup(options);
		hGroup.addContainerGap();

		SequentialGroup vGroup = layout.createSequentialGroup();
		vGroup.addContainerGap();
		vGroup.addComponent(easy);
		vGroup.addPreferredGap(ComponentPlacement.RELATED);
		vGroup.addComponent(medium);
		vGroup.addPreferredGap(ComponentPlacement.RELATED);
		vGroup.addComponent(hard);
		vGroup.addPreferredGap(ComponentPlacement.UNRELATED);
		vGroup.addComponent(okButton);
		vGroup.addContainerGap();

		layout.setHorizontalGroup(hGroup);
		layout.setVerticalGroup(vGroup);

		c.setLayout(layout);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch(((Component) arg0.getSource()).getName()) {
			case EASY:
				Difficulty.setCurrentDifficulty(Difficulty.EASY);
				rows = cols = 9;
				bombs = 10;
				break;
			case MEDIUM:
				Difficulty.setCurrentDifficulty(Difficulty.MEDIUM);
				rows = cols = 16;
				bombs = 40;
				break;
			case HARD:
				Difficulty.setCurrentDifficulty(Difficulty.HARD);
				rows = 16;
				cols = 30;
				bombs = 99;
				break;
			case OK:
				setConfirm(true);
				this.dispose();
				break;

		}

	}

	public boolean isConfirm() {
		return confirm;
	}

	public void setConfirm(boolean confirm) {
		this.confirm = confirm;
	}

	@Override
	public int getRows() {
		return rows;
	}

	@Override
	public int getCols() {
		return cols;
	}

	@Override
	public int getBombs() {
		return bombs;
	}

}
