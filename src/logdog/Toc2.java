package logdog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

public class Toc2 extends JPanel {

	private static final long serialVersionUID = 1L;

	private CenterOptions co;

	private JButton addAnother, removeOne, go, printStats;
	private JScrollPane scroll;
	private JPanel southern, middle, northern;
	private JCheckBox randomized;
	
	private JTextField manualInput;

	private JRadioButton sing, doub, round;
	private ButtonGroup group;

	private boolean random = false;

	public static int boxes = 0;

	public Toc2() {
		super();

		setLayout(new BorderLayout());

		// center scroll and stuff
		middle = new JPanel();
		middle.setLayout(new BoxLayout(middle, BoxLayout.Y_AXIS));

		northern = new JPanel();

		sing = new JRadioButton("Single Elimination");
		doub = new JRadioButton("Double Elimination");
		round = new JRadioButton("Round Robin");

		sing.setSelected(true);

		group = new ButtonGroup();

		group.add(sing);
		group.add(doub);
		group.add(round);

		northern.add(sing);
		northern.add(doub);
		northern.add(round);

		add(northern, BorderLayout.NORTH);

		co = new CenterOptions();
		scroll = new JScrollPane(co);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setSize(new Dimension(300, 300));
		middle.add(scroll);

		add(middle);

		// south
		southern = new JPanel();

		removeOne = new JButton("Remove A Team");
		southern.add(removeOne);

		addAnother = new JButton("Add Another Team");
		southern.add(addAnother);
		
		manualInput = new JTextField("  ");
		southern.add(manualInput);
		
		Toc2 thiss = this;
		
		manualInput.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int teams = 0;
				try {
					teams = Integer.parseInt(manualInput.getText().trim());
				} catch (Exception ex) {
					return;
				}
				while(co.getTotalTeams() < teams) {
					co.addTeam();
				}
				while(co.getTotalTeams() > teams) {
					co.removeTeam();
				}
				SwingUtilities.updateComponentTreeUI(thiss);
			}
			
		});

		addAnother.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(co.getTotalTeams() >= Single.MAX_PLAYERS)
					return;
				
				co.addTeam();
				SwingUtilities.updateComponentTreeUI(RunMe.frame);
			}
		});

		removeOne.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				co.removeTeam();
				SwingUtilities.updateComponentTreeUI(RunMe.frame);
			}
		});

		for (int i = 0; i < 6; i++)
			co.addTeam();

		go = new JButton("Go");
		southern.add(go);

		go.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (sing.isSelected()) {
					if(co.getTotalTeams() <= 64) {
//						int pages = 3;
//						if(co.getTotalTeams() > 256)
//							pages = 9;
//						else if(co.getTotalTeams() > 128)
//							pages = 5;
//						
//						int choice = JOptionPane.showConfirmDialog(null, "A brakcet with " + co.getTotalTeams() + " teams requires "
//								+ pages + " pages. Is this ok?");
//						
//						if(choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.NO_OPTION)
//							return;
//						//Choice must be yes now
//						JOptionPane.showMessageDialog(null, "You will be displayed 2 windows.\nThe first is a template for the larger brackets,\nand the second for the final small bracket");
//						
//						if(pages == 3) {
//							
//							BufferedImage[] images = new BufferedImage[pages];
//							//do stuff with these images
//							
//							Single parent = Single.makeBracket(co.getTotalTeams(), false);
//							parent.setTeamNames(co.getPlayers());
//							parent.setTeamColors(co.getPlayers());
//							
//							parent.getTop().printable = false;
//							parent.getTop().specialSeed = "A";
//							parent.getTop().makePopup();
//							
//							parent.getBottom().setSettings(parent.getTop().getSettings());
//							parent.getBottom().specialSeed = "B";
//							
//							PrinterJob job = PrinterJob.getPrinterJob();
//							job.setPrintable(parent.getTop());
//							job.setPrintable(parent.getBottom());
//							boolean ok = job.printDialog();
//							if (ok) {
//								try {
//									job.print();
//								} catch (PrinterException ex) {
//									/* The job did not successfully complete */
//								}
//							}
//							
////							job.setPrintable(parent.getBottom());
////							boolean ok1 = job.printDialog();
////							if (ok1) {
////								try {
////									job.print();
////								} catch (PrinterException ex) {
////									/* The job did not successfully complete */
////								}
////							}
//							
//							
//							
//						}
//						
//						return;
//					}
//					else if(co.getTotalTeams() > 512) {
//						
//					}
//					else {

						Single s = Single.makeBracket(co.getPlayers().size(), false);

						if (!random)
							s.makePopup(co.getPlayers());
						else {
							ArrayList<Player> a = new ArrayList<Player>();
							a = co.getPlayers();
							Collections.shuffle(a);
							s.makePopup(a);
						}
					}
					else {
						JOptionPane.showMessageDialog(null, "ERROR: Max Teams = 64");
					}
				}
				else if(doub.isSelected()) {
					
					if(co.getTotalTeams() > 32) {
						JOptionPane.showMessageDialog(null, "ERROR: Max Teams = 32");
						//System.out.println(num);
						return;
					}
					
					ArrayList<Player> a = new ArrayList<Player>();
					a = co.getPlayers();
					
					if(random){
						Collections.shuffle(a);
					}
					
					DoubleSingleJoiner ds = new DoubleSingleJoiner(a);
					ds.makePopup();
				}
				else if(round.isSelected()) {
					RoundRobin r = new RoundRobin(co.getPlayers());
					 PrinterJob job = PrinterJob.getPrinterJob();
			         job.setPrintable(r);
			         boolean ok = job.printDialog();
			         if (ok) {
			             try {
			                  job.print();
			             } catch (PrinterException ex) {
			              /* The job did not successfully complete */
			             }
			         }
				}
			}

		});
		
		printStats = new JButton("Print Stats");
		
		printStats.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//creates popup, then prints the stats window
				new Stats(co.getPlayers());
			}
		});

		southern.add(printStats);
		
		randomized = new JCheckBox("Randomized");

		randomized.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				random = randomized.isSelected();

			}
		});

		southern.add(randomized);

		add(southern, BorderLayout.SOUTH);
	}

	private class TeamChooser extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected JLabel label;
		protected JTextField field;
		protected JButton button;
		protected Color color = Color.black;

		public TeamChooser(int seed) {
			super();

			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

			label = new JLabel("Team " + seed + ":\t");
			field = new JTextField();
			button = new JButton("Color");
			button.setOpaque(false);
			button.setForeground(color);

			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					color = JColorChooser.showDialog(button, "Choose a Color", color);
					button.setForeground(color);
					field.setForeground(color);
				}
			});

			add(label);
			add(field);
			add(button);
		}

		public String getName() {
			return field.getText();
		}

		public Color getColor() {
			return color;
		}

	}

	private class CenterOptions extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private ArrayList<TeamChooser> tcs = new ArrayList<TeamChooser>();

		public CenterOptions() {
			super();
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		}

		public void addTeam() {
			tcs.add(new TeamChooser(boxes + 1));
			//tcs.get(tcs.size()-1).field.setText("" + tcs.size());
			add(tcs.get(boxes));
			boxes++;
		}

		public void removeTeam() {
			if (boxes <= 2 || boxes > 64)
				return;

			boxes--;
			remove(tcs.get(boxes));
			tcs.remove(boxes);
		}

		public ArrayList<Player> getPlayers() {
			ArrayList<Player> players = new ArrayList<Player>();
			for (int i = 0; i < tcs.size(); i++) {
				players.add(new Player(tcs.get(i).getName(), tcs.get(i).getColor()));
			}
			return players;
		}

		public int getTotalTeams() {
			return tcs.size();
		}
	}

}
