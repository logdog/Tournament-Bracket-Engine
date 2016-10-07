package logdog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

public class Stats extends JPanel {

	JTable table;

	public Stats(ArrayList<Player> players) {

		JFrame frame = new JFrame("Table");
		frame.setMinimumSize(new Dimension(500, 500));

		setLayout(new BorderLayout());

		String[] headings = { "Team", "Wins", "Loses", "Draws", "PF", "PA" };
		Object[][] data = new Object[players.size() + 1][headings.length];

		data[0][0] = "";

		for (int i = 1; i <= players.size(); i++) {
			data[i][0] = players.get(i - 1).getName();
		}

		table = new JTable(data, headings);
		table.setGridColor(Color.BLACK);

		frame.add(table.getTableHeader(), BorderLayout.PAGE_START);
		frame.add(table, BorderLayout.CENTER);
		
		frame.pack();
		frame.setVisible(true);
		//
		try {
			table.print();
		} catch (PrinterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
