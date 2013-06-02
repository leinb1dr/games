package com.danleinbach.game.minesweeper.mineSweeper;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class PropertiesWindow extends JFrame implements NewMineSweeper {

	private static final long serialVersionUID = 1L;
	private int rows;
	private int cols;
	private int bombs;
	private boolean confirm;

	/* (non-Javadoc)
	 * @see NewMineSweeper#getRows()
	 */
	@Override
	public int getRows() {
		return rows;
	}

	/* (non-Javadoc)
	 * @see NewMineSweeper#getCols()
	 */
	@Override
	public int getCols() {
		return cols;
	}

	/* (non-Javadoc)
	 * @see NewMineSweeper#getBombs()
	 */
	@Override
	public int getBombs() {
		return bombs;
	}

	/* (non-Javadoc)
	 * @see NewMineSweeper#isConfirm()
	 */
	@Override
	public boolean isConfirm() {
		return confirm;
	}

	public PropertiesWindow() {
		addComponents();
		pack();
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setVisible(true);
	}

	private void addComponents() {
		JLabel rowLabel = new JLabel("Rows:");
		JLabel colLabel = new JLabel("Columns:");
		JLabel bombsLabel = new JLabel("Bombs:");
		final JTextField rowText = new JTextField();
		final JTextField colText = new JTextField();
		final JTextField bombsText = new JTextField();

		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					PropertiesWindow.this.rows = Integer.parseInt(rowText.getText());
					PropertiesWindow.this.cols = Integer.parseInt(colText.getText());
					PropertiesWindow.this.bombs = Integer.parseInt(bombsText.getText());
					PropertiesWindow.this.confirm = true;
					PropertiesWindow.this.dispose();
				} catch(NumberFormatException e) {

				}
			}
		});
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PropertiesWindow.this.confirm = false;
				PropertiesWindow.this.dispose();

			}
		});

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

		buttonPanel.add(ok);
		buttonPanel.add(cancel);

		JPanel panel = new JPanel();
		GroupLayout layout = new GroupLayout(panel);

		SequentialGroup hGroup = layout.createSequentialGroup();
		ParallelGroup hLabels = layout.createParallelGroup(Alignment.LEADING);
		ParallelGroup hText = layout.createParallelGroup(Alignment.LEADING);

		hLabels.addComponent(rowLabel);
		hLabels.addComponent(colLabel);
		hLabels.addComponent(bombsLabel);

		hText.addComponent(rowText);
		hText.addComponent(colText);
		hText.addComponent(bombsText);

		hGroup.addContainerGap(10, 10);
		hGroup.addGroup(hLabels);
		hGroup.addPreferredGap(ComponentPlacement.RELATED);
		hGroup.addGroup(hText);
		hGroup.addContainerGap(10, 10);

		SequentialGroup vGroup = layout.createSequentialGroup();
		ParallelGroup vRow = layout.createParallelGroup(Alignment.CENTER);
		ParallelGroup vCol = layout.createParallelGroup(Alignment.CENTER);
		ParallelGroup vBomb = layout.createParallelGroup(Alignment.CENTER);

		vRow.addComponent(rowLabel);
		vRow.addComponent(rowText);

		vCol.addComponent(colLabel);
		vCol.addComponent(colText);

		vBomb.addComponent(bombsLabel);
		vBomb.addComponent(bombsText);

		vGroup.addContainerGap(10, 10);
		vGroup.addGroup(vRow);
		vGroup.addPreferredGap(ComponentPlacement.UNRELATED);
		vGroup.addGroup(vCol);
		vGroup.addPreferredGap(ComponentPlacement.UNRELATED);
		vGroup.addGroup(vBomb);
		vGroup.addContainerGap(10, 10);

		layout.setHorizontalGroup(hGroup);
		layout.setVerticalGroup(vGroup);

		panel.setLayout(layout);

		Container contentPane = getContentPane();

		contentPane.setLayout(new BorderLayout());

		contentPane.add(panel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);


	}

}
