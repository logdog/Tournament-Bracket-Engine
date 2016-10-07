package logdog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class AdvancedSettings extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel buttons, center;
	private JButton cancel, randomize, go, linec, textc;
	private JTextArea textArea;
	private JLabel label;
	private JScrollPane scroll;
	
	Color line;

	public AdvancedSettings() {
		this(0);
	}

	public AdvancedSettings(int teams) {
		setLayout(new BorderLayout());

		// labels
		label = new JLabel();
		label.setText("<html>" + "Advanced Options:<br>" + "Here you can enter the team names for your bracket. "
				+ "Press CANCEL to go back, RANDOMIZE to scramble the teams, and GO to make your bracket! "
				+ "</html>");

		add(label, BorderLayout.NORTH);

		add(Box.createRigidArea(new Dimension(20, 0)), BorderLayout.EAST);
		add(Box.createRigidArea(new Dimension(20, 0)), BorderLayout.WEST);

		// text area
		center = new JPanel();
		center.setLayout(new BorderLayout());
		center.add(Box.createRigidArea(new Dimension(20, 20)), BorderLayout.NORTH);

		textArea = new JTextArea();
		textArea.setEditable(true);
		String example = "";
		for (int i = 1; i <= teams; i++) {
			example += "Team " + i + "\n";
		}
		textArea.setText(example);
		textArea.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				textArea.setBorder(BorderFactory.createLineBorder(Color.cyan, 3, true));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				textArea.setBorder(BorderFactory.createEmptyBorder());
			}

		});

		scroll = new JScrollPane(textArea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		center.add(scroll);

		add(center, BorderLayout.CENTER);

		// buttons
		buttons = new JPanel();

		cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		//		RunMe.changeMenu("toc");
			}
		});

		randomize = new JButton("Randomize");
		randomize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s[] = textArea.getText().split("\\r?\\n");
				ArrayList<String> arrList = new ArrayList<>(Arrays.asList(s));
				Collections.shuffle(arrList);

				textArea.setText("");
				for (String str : arrList) {
					//String str = arrList.get(i);
					if (!(str.equals("")) && !str.contains("\n"))
						while (str.charAt(0) == ' ') {
							str = str.substring(1);
							if (str.equals(""))
								continue;
						}
					textArea.append(str + "\n");
				}
			}
		});

		go = new JButton("GO");
		go.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s[] = textArea.getText().split("\\r?\\n");
				textArea.setText("");
				for (String str : s) {
					if (!(str.equals("")) && !str.contains("\n"))
						while (str.charAt(0) == ' ') {
							str = str.substring(1);
							if (str.equals(""))
								continue;
						}
					textArea.append(str + "\n");
				}

				s = textArea.getText().split("\\r?\\n");
				ArrayList<String> arrList = new ArrayList<>(Arrays.asList(s));
			///	Single.main(arrList);
		
			}
		});
		
		linec = new JButton("Edit Line Color");
		line = Color.black;
		
		linec.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				line = JColorChooser.showDialog(buttons, "Choose A Line Color", Color.BLACK);
				
			}
			
		});

		buttons.add(cancel);
		buttons.add(randomize);
		buttons.add(go);
		buttons.add(linec);

		add(buttons, BorderLayout.SOUTH);
	}
}
