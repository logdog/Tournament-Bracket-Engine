package logdog;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class TournamentOptionsChooser extends JPanel {

	private static final long serialVersionUID = 1L;

	private JRadioButton singleElimination, doubleElimination, roundRobin;
	private ButtonGroup eliminationTypeGroup;
	private JPanel radioPanel, spinPanel;

	private SpinnerNumberModel numPlayersSpinner;
	protected JSpinner spinner;
	
	private JButton go, advanced;

	public TournamentOptionsChooser() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		numPlayersSpinner = new SpinnerNumberModel(8, 4, 64, 1);
		spinPanel = new JPanel(new FlowLayout());
		
		spinner = addLabeledSpinner(spinPanel, "Number of Teams: ", numPlayersSpinner);
		add(spinPanel);

		singleElimination = new JRadioButton("Single Elimination");
		singleElimination.setMnemonic(KeyEvent.VK_S);
		singleElimination.setSelected(true);

		doubleElimination = new JRadioButton("Double Elimination");
		doubleElimination.setMnemonic(KeyEvent.VK_D);

		roundRobin = new JRadioButton("Round Robin");
		roundRobin.setMnemonic(KeyEvent.VK_R);

		eliminationTypeGroup = new ButtonGroup();
		eliminationTypeGroup.add(singleElimination);
		eliminationTypeGroup.add(doubleElimination);
		eliminationTypeGroup.add(roundRobin);

		radioPanel = new JPanel(new GridLayout(0, 1));
		radioPanel.add(singleElimination);
		radioPanel.add(doubleElimination);
		radioPanel.add(roundRobin);

		add(radioPanel);
		
		add(Box.createVerticalGlue());
		
		JPanel buttons  = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		
		advanced = new JButton("Advanced");
		advanced.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			//	RunMe.changeMenu("as");
			}
		});
		
		buttons.add(advanced);
		
		go = new JButton("Make Me A Bracket!");
		go.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Number of Players: " + numPlayersSpinner.getValue());
				if(singleElimination.isSelected()) {
					ArrayList<String> s = new ArrayList<String>();
					for(int i = 0; i < (int)spinner.getValue(); i++) {
						s.add("");
					}
				//	Single.main(s);
				}
				if(doubleElimination.isSelected())
					System.out.println("Type of Tourament: " + "DOUBLE-ELIMINATION");
				if(roundRobin.isSelected())
					System.out.println("Type of Tourament: " + "ROUND-ROBIN");
			}
		});
		buttons.add(go);
		
		add(buttons);
	}

	private static JSpinner addLabeledSpinner(Container c, String label, SpinnerModel model) {
		JLabel l = new JLabel(label);
		c.add(l);

		JSpinner spinner = new JSpinner(model);
		l.setLabelFor(spinner);
		c.add(spinner);

		return spinner;
	}
}
