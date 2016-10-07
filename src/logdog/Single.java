package logdog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Single extends JPanel implements Printable {

	private static final long serialVersionUID = 1L;
	private Single top, bottom;
	private int seed;
	private int highest = 2;
	public static final int MAX_PLAYERS = 512;
	private Player player;
	public String specialSeed = "";

	public boolean printable = true;

	public int nodeWidth = 75, nodeHeight = 165;
	private Color lineColor = Color.BLACK, seedColor = Color.BLACK, gameColor = Color.black;
	private boolean showSeeds = true, showGame = true;

	private Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
	private BufferedImage backgroundImage;

	public Single() {
		this(-1);
	}

	private Single(int seed) {
		this.seed = seed;
		player = new Player();
		if (seed == -1) {
			top = new Single(1);
			bottom = new Single(2);
		}
	}

	public void setFontSize(int n) {
		font = new Font(Font.SANS_SERIF, Font.PLAIN, n);
	}

	public void setTeamNames(ArrayList<Player> p) {
		for (int i = 1; i <= p.size(); i++) {
			findCellThatHasSeed(i).player.setName(p.get(i - 1).getName());
		}
	}

	public void setTeamColors(ArrayList<Player> colors) {
		for (int i = 1; i <= colors.size(); i++) {
			findCellThatHasSeed(i).player.setColor(colors.get(i - 1).getColor());
		}
	}

	public void setShowSeeds(boolean b) {
		showSeeds = b;
		if (top != null)
			top.setShowSeeds(b);
		if (bottom != null)
			bottom.setShowSeeds(b);
	}

	public void setShowGame(boolean b) {
		showGame = b;
		if (top != null)
			top.setShowGame(b);
		if (bottom != null)
			bottom.setShowGame(b);
	}

	public void drawFractal(Graphics g, int x, int y, int w, int h) {
		if (backgroundImage != null)
			g.drawImage(backgroundImage, 0, 0, 576, 820, null);

		g.setColor(lineColor);
		g.drawLine(x, y, x + w, y);
		g.setColor(player.getColor());
		g.drawString(player.getName(), x, y - 2);
		g.setColor(seedColor);

		if (showSeeds && seed > 0)
			g.drawString("" + seed, x - 20, y + 5);
		else if (showGame && seed <= 0) {
			g.setColor(gameColor);
			g.drawString("" + (-seed) + ")", x - 20, y + 5);
		}

		g.setColor(lineColor);
		if (top != null && bottom != null) {
			g.drawLine(x, y, x, y - h);
			g.drawLine(x, y, x, y + h);
			g.drawString(specialSeed,  x + 5, y - 5);
			top.drawFractal(g, x - w, y - h, w, h / 2);
			bottom.drawFractal(g, x - w, y + h, w, h / 2);
		}
	}

	public void setLineColor(Color c) {
		lineColor = c;
		if (top != null) {
			top.setLineColor(c);
		}
		if (bottom != null) {
			bottom.setLineColor(c);
		}
	}

	public void setSeedColor(Color c) {
		seedColor = c;
		if (top != null) {
			top.setSeedColor(c);
		}
		if (bottom != null) {
			bottom.setSeedColor(c);
		}
	}

	public void setGameColor(Color c) {
		gameColor = c;
		if (top != null) {
			top.setGameColor(c);
		}
		if (bottom != null) {
			bottom.setGameColor(c);
		}
	}

	public void removeNodes() {
		top = bottom = null;
	}

	public void addNode(int s, int order) {
		Single father = findCellThatHasSeed(s);

		father.top = new Single(s);
		father.bottom = new Single(highest + 1);
		highest++;
		father.seed = (MAX_PLAYERS + 1) * (-order - 1);
		// father.seed = -(int)(Math.log(DoubleNode.getNextTwo(total)) /
		// Math.log(2));

		// reduceAllSeedsBy(1, this);
	}

	public int getHighestSeed() {
		return (getHighestSeed(top) > getHighestSeed(bottom) ? getHighestSeed(top) : getHighestSeed(bottom));
	}

	public int getLowestSeed() {
		return (getLowestSeed(top) < getLowestSeed(bottom) ? getLowestSeed(top) : getLowestSeed(bottom));
	}

	// returns biggest seed on the outskirts
	public int getHighestSeed(Single s) {
		if (s.top != null && s.bottom != null) {
			return (getHighestSeed(s.top) > getHighestSeed(s.bottom) ? getHighestSeed(s.top)
					: getHighestSeed(s.bottom));
		} else if (s.top != null) {
			return getHighestSeed(s.top);
		} else if (s.bottom != null) {
			return getHighestSeed(s.bottom);
		}
		return s.seed;
	}

	public int getLowestSeed(Single s) {
		if (s.top.top != null && s.bottom.bottom != null) {
			return (getLowestSeed(s.top) < getLowestSeed(s.bottom) ? getLowestSeed(s.top) : getLowestSeed(s.bottom));
		} else if (s.top.top != null) {
			return getLowestSeed(s.top);
		} else if (s.bottom.bottom != null) {
			return getLowestSeed(s.bottom);
		}
		return s.seed;
	}

	public Single findCellThatHasSeed(final int SEED) {
		if (top != null && bottom != null) {
			return findCellThatHasSeed(SEED, this);
		}
		return null;
	}

	public Single findCellThatHasSeed(final int SEED, Single s) {
		if (s.seed == SEED) {
			return s;
		}

		Single sTOP, sBOT;

		if (s.top != null) {
			sTOP = findCellThatHasSeed(SEED, s.top);
			if (sTOP != null)
				return sTOP;
		}

		if (s.bottom != null) {
			sBOT = findCellThatHasSeed(SEED, s.bottom);
			if (sBOT != null)
				return sBOT;
		}
		return null;
	}

	public Single findFatherCell(Single daughter) {
		return findFatherCell(daughter, this);
	}

	public Single findFatherCell(Single daughter, Single s) {
		if (s.top != null) {
			if (s.top == daughter)
				return s;
			Single sTOP = findFatherCell(daughter, s.top);
			if (sTOP != null)
				return sTOP;
		}
		if (s.bottom != null) {
			if (s.bottom == daughter)
				return s;
			Single sBOT = findFatherCell(daughter, s.bottom);
			if (sBOT != null)
				return sBOT;
		}
		return null;
	}

	public Single findSisterCell(Single sister, Single father) {
		if (father.top != null) {
			if (father.top.seed != sister.seed)
				return father.top;
		}
		if (father.bottom != null) {
			if (father.bottom.seed != sister.seed)
				return father.bottom;
		}

		return null;
	}

	public void reduceAllSeedsBy(int n, Single s) {
		if (s.top.top != null) {
			s.top.reduceAllSeedsBy(n, s.top);
		}
		if (s.bottom.bottom != null) {
			s.bottom.reduceAllSeedsBy(n, s.bottom);
		}
		s.seed -= n;
	}

	public void removePlayer() {
		Single ex = findCellThatHasSeed(getHighestSeed());

		Single father = findFatherCell(ex);
		Single sis = findSisterCell(ex, father);

		father.seed = sis.seed;
		father.removeNodes();
	}

	public void setSettings(Object[] settings) {
		nodeWidth = (int) settings[0];
		nodeHeight = (int) settings[1];
		lineColor = (Color) settings[2];
		seedColor = (Color) settings[3];
		gameColor = (Color) settings[4];
		showSeeds = (boolean) settings[5];
		showGame = (boolean) settings[6];
		font = (Font) settings[7];
		backgroundImage = (BufferedImage) settings[8];
	}

	public Object[] getSettings() {
		Object[] settings = new Object[9];

		settings[0] = nodeWidth;
		settings[1] = nodeHeight;
		settings[2] = lineColor;
		settings[3] = seedColor;
		settings[4] = gameColor;
		settings[5] = showSeeds;
		settings[6] = showGame;
		settings[7] = font;
		settings[8] = backgroundImage;

		return settings;
	}

	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, 576, 734);
		g.setColor(Color.black);

		g.drawString("A LogDog Creation", (int) 576 - 6 * "A LogDog Creation".length() - 25, (int) 734 - 30);
		g.drawString("Make Brackets Great Again!", (int) 576 - 6 * "Make Brackets Great Again!".length() - 25,
				(int) 734 - 10);
		g.drawRect(0, 0, 576, 734);

		g.setFont(font);

		drawFractal(g, 576 - nodeWidth - 10, 734 / 2, nodeWidth, nodeHeight);
	}

	@SuppressWarnings("unused")
	private static int getSpots(int n) {
		for (int i = 1; i < 6; i++) // 2 --> 32
			if ((int) (Math.pow(2, i)) == n)
				return n;
		return getSpots(n + 1);
	}

	public Single getTop() {
		return top;
	}

	public Single getBottom() {
		return bottom;
	}

	public static Single makeBracket(int players, boolean doub) {
		if (players < 1)
			throw new IllegalArgumentException("Invalid number of players");
		Single node = new Single();

		int order = 1;
		int num = (int) (Math.pow(2, order));

		for (int i = 2; i < players; i++) {
			node.addNode(num, order);
			num--;
			if (num == 0) {
				order++;
				num = (int) (Math.pow(2, order));
			}
		}

		int low = node.getLowestSeed();
		int count = -1;
		int alt = 0;

		// System.out.println(order);

		while (low < -1) {
			Single s = node.findCellThatHasSeed(low);
			if (s == null) {
				low += (MAX_PLAYERS + 1);
				if (doub) {
					// count -= (int)(Math.pow(2, order - 2)) +
					// (DoubleNode.getNextTwo(players) - players);
					alt++;
					if (alt % 2 == 0) {
						order--;
					}
				}
				continue;
			} else {
				s.seed = count;
				count--;
			}
		}

		node.seed = count;
		return node;
	}

	public void makePopup(ArrayList<Player> p) {
		setTeamNames(p);
		setTeamColors(p);
		makePopup();
	}

	public void makePopup() {

		JFrame frame = new JFrame("Single Elimination");
		frame.setResizable(false);
		frame.setMinimumSize(new Dimension(576, 820));
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		frame.add(this);

		Single thiss = this;

		JButton button = (printable ? new JButton("Print Meh Bracket!") : new JButton("Next"));
		if (printable) {
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					PrinterJob job = PrinterJob.getPrinterJob();
					job.setPrintable(thiss);
					boolean ok = job.printDialog();
					if (ok) {
						try {
							job.print();
						} catch (PrinterException ex) {
							/* The job did not successfully complete */
						}
					}
				}
			});
		}
		else {
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
				}
			});
		}

		JPanel south = new JPanel();
		JSlider wSlider = new JSlider(JSlider.HORIZONTAL, 20, 150, nodeWidth);
		wSlider.setMajorTickSpacing(20);
		wSlider.setMinorTickSpacing(10);
		wSlider.setPaintTicks(true);
		wSlider.setPaintLabels(true);
		wSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				nodeWidth = wSlider.getValue();
				repaint();
			}

		});
		JSlider hSlider = new JSlider(JSlider.HORIZONTAL, 50, 200, nodeHeight);
		hSlider.setMajorTickSpacing(50);
		hSlider.setMinorTickSpacing(25);
		hSlider.setPaintTicks(true);
		hSlider.setPaintLabels(true);
		hSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				nodeHeight = hSlider.getValue();
				repaint();
			}

		});

		JButton lineColorButton = new JButton("Line Color");
		lineColorButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setLineColor(JColorChooser.showDialog(button, "Choose a Color", lineColor));
				lineColorButton.setForeground(lineColor);
				repaint();
			}

		});

		JButton seedColorButton = new JButton("Seed Color");
		seedColorButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setSeedColor(JColorChooser.showDialog(button, "Choose a Color", lineColor));
				seedColorButton.setForeground(seedColor);
				repaint();
			}

		});

		JButton gameColorButton = new JButton("Game Color");
		gameColorButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setGameColor(JColorChooser.showDialog(button, "Choose a Color", lineColor));
				gameColorButton.setForeground(gameColor);
				repaint();
			}

		});

		JCheckBox seedCheck = new JCheckBox();
		seedCheck.setText("Seed Numbers On?");
		seedCheck.setSelected(true);
		seedCheck.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setShowSeeds(seedCheck.isSelected());
				repaint();
			}

		});

		JCheckBox gameCheck = new JCheckBox();
		gameCheck.setText("Game Numbers On?");
		gameCheck.setSelected(true);
		gameCheck.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setShowGame(gameCheck.isSelected());
				repaint();
			}

		});

		JSlider fsSlider = new JSlider(JSlider.HORIZONTAL, 6, 24, 12);
		fsSlider.setToolTipText("Set the Font Size");
		fsSlider.setMajorTickSpacing(6);
		fsSlider.setMinorTickSpacing(2);
		fsSlider.setPaintTicks(true);
		fsSlider.setPaintLabels(true);
		fsSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				setFontSize(fsSlider.getValue());
				repaint();
			}

		});

		JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
				setLineColor(Color.black);
				setGameColor(Color.black);
				setSeedColor(Color.black);
				setShowGame(true);
				setShowSeeds(true);
				backgroundImage = null;

				seedCheck.setSelected(true);
				gameCheck.setSelected(true);

				lineColorButton.setForeground(lineColor);
				seedColorButton.setForeground(seedColor);
				gameColorButton.setForeground(gameColor);
				fsSlider.setValue(12);
				repaint();

			}
		});

		JButton backImgButton = new JButton("Set Background Image");
		backImgButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);

				File file = chooser.getSelectedFile();

				try {
					backgroundImage = ImageIO.read(file);
				} catch (IOException e1) {
					backgroundImage = null;
				}

				repaint();
			}

		});

		JButton more = new JButton("Advanced");
		more.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFrame advanced = new JFrame("Advanced Settings");
				advanced.setResizable(false);
				advanced.setBackground(Color.white);

				JPanel options = new JPanel();
				options.setLayout(new GridLayout(5, 2));

				options.add(lineColorButton);
				options.add(seedColorButton);

				options.add(gameColorButton);
				options.add(backImgButton);

				options.add(seedCheck);
				options.add(gameCheck);

				// options.add(new JLabel("Font Size:"));
				options.add(fsSlider);
				options.add(reset);

				advanced.getContentPane().add(options);
				advanced.pack();
				advanced.setVisible(true);

			}

		});

		south.add(wSlider);
		south.add(hSlider);

		south.add(button);
		south.add(more);

		frame.add(south, BorderLayout.SOUTH);
		frame.pack();

		frame.setVisible(true);
	}
	

	public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
		if (pageIndex > 0) { /*
								 * We have only one page, and 'page' is
								 * zero-based
								 */
			return NO_SUCH_PAGE;
		}

		/*
		 * User (0,0) is typically outside the imageable area, so we must
		 * translate by the X and Y values in the PageFormat to avoid clipping
		 */

		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

		drawFractal(g, (int) pageFormat.getImageableWidth() - nodeWidth - 10, (int) pageFormat.getImageableHeight() / 2,
				nodeWidth, nodeHeight);

		/* Now we perform our rendering */
		g.drawString("A LogDog Creation", (int) pageFormat.getImageableWidth() - 6 * "A LogDog Creation".length() - 25,
				(int) pageFormat.getImageableHeight() - 50);
		g.drawString("Questions? Comments? Concerns?",
				(int) pageFormat.getImageableWidth() - 6 * "Questions? Comments? Concerns?".length() - 25,
				(int) pageFormat.getImageableHeight() - 30);
		g.drawString("makebracketsgreatagain@gmail.com",
				(int) pageFormat.getImageableWidth() - 6 * "makebracketsgreatagain@gmail.com".length() - 25,
				(int) pageFormat.getImageableHeight() - 10);

		g.setFont(font);

		// System.out.println(pageFormat.getImageableWidth()); //576
		// System.out.println(pageFormat.getImageableHeight()); //734
		/* tell the caller that this page is part of the printed document */
		return PAGE_EXISTS;
	}
}
