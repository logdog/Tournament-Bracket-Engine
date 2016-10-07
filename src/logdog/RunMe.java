package logdog;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class RunMe {

	static JFrame frame;
	static AdvancedSettings as;
	static TournamentOptionsChooser tc = new TournamentOptionsChooser();
	
	static Toc2 tc2 = new Toc2();

	static Component current = tc2;

	public static void main(String[] args) {
		frame = new JFrame("Tournament Engine");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setMinimumSize(new Dimension(300, 250));
		frame.setLocationRelativeTo(null);
		frame.getContentPane().add(tc2);
		frame.pack();
		frame.setVisible(true);
	}

//	public static void changeMenu(String menu) {
//		if (menu.equals("as")) {
//			if (as != null)
//				frame.add(as);
//			else {
//				as = new AdvancedSettings((int)tc.spinner.getValue());
//				frame.add(as);
//			}
//			frame.remove(current);
//			current = as;
//			frame.setSize(480, 400);
//		} else if (menu.equals("toc")) {
//			frame.add(tc);
//			frame.remove(current);
//			current = tc;
//			frame.setSize(300, 250);
//		}
//
//		SwingUtilities.updateComponentTreeUI(frame);
//
//	}
}
