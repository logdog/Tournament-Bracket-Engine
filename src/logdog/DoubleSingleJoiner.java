package logdog;

import java.awt.BasicStroke;
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

public class DoubleSingleJoiner extends JPanel implements Printable {

	private static final long serialVersionUID = 1L;
	protected Single single;
	protected DoubleNode doubleNode;
	private int teams, SHIFT = 30;
	private int x = WIDTH - 100, y = HEIGHT / 3, sw = 100, dw = 100, h = 100, dh = 100;
	private Color lineColor, seedColor, gameColor;
	private boolean showGame = true, showSeed = true;

	private BufferedImage backgroundImage;

	private Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);

	private static final int WIDTH = 576, HEIGHT = 820;

	public DoubleSingleJoiner(ArrayList<Player> players) {
		this.teams = players.size();
		single = Single.makeBracket(players.size(), true);
		single.setTeamNames(players);
		single.setTeamColors(players);
		doubleNode = DoubleNode.makeDoubleElim(players.size());
	}

	public DoubleSingleJoiner(int teams) {
		single = Single.makeBracket(teams, true);
		doubleNode = DoubleNode.makeDoubleElim(teams);
		this.teams = teams;
	}

	public void setLineColor(Color c) {
		lineColor = c;
		single.setLineColor(c);
		doubleNode.setLineColor(c);
	}

	public void setSeedColor(Color c) {
		seedColor = c;
		single.setSeedColor(c);
		doubleNode.setSeedColor(c);
	}

	public void setGameColor(Color c) {
		gameColor = c;
		single.setGameColor(c);
	}

	public void setShowGame(boolean b) {
		showGame = b;
		single.setShowGame(b);
		doubleNode.setShowGame(b);
	}

	public void setShowSeeds(boolean b) {
		showSeed = b;
		single.setShowSeeds(b);
	}

	public void setFontSize(int n) {
		font = new Font(Font.SANS_SERIF, Font.PLAIN, n);
	}

	@Override
	public void paint(Graphics g) {
		g.setFont(font);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 576, 820);

		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, 576, 820, null);
		}

		g.setColor(lineColor);

		single.drawFractal(g, x - sw, y - SHIFT, sw, h);
		doubleNode.drawFractal(g, x - dw, HEIGHT - y - SHIFT, dw, dh);

		g.drawLine(x, HEIGHT / 2 - SHIFT, x + 45, HEIGHT / 2 - SHIFT);
		g.drawLine(x, y - SHIFT, x, HEIGHT - y - SHIFT);

		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 10.0f },
				0.0f));

		g2.drawLine(x + 45, HEIGHT / 2 - SHIFT, x + 45, HEIGHT - y - SHIFT + 100);
		g2.drawLine(x, HEIGHT - y - SHIFT + 100, x + 45, HEIGHT - y - SHIFT + 100);
		g2.drawLine(x + 45, HEIGHT - y - SHIFT, x + 90, HEIGHT - y - SHIFT);

		g.drawString("If W" + (teams - 1) + " is L" + teams, x, HEIGHT - y - SHIFT + 120);

		g.setColor(gameColor);
		if (showGame)
			g.drawString((teams) + ")", x - 20, HEIGHT / 2 + 5 - SHIFT);

		// g.setColor(Color.blue);
		// g.drawLine(0, HEIGHT/2, WIDTH, HEIGHT/2);
	}

	public void makePopup() {

		JFrame frame = new JFrame();
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		frame.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setBackground(Color.white);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Double Elimination");
		frame.getContentPane().add(this);

		DoubleSingleJoiner thiss = this;

		JButton button = new JButton("Print Meh Bracket!");
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

		JPanel south = new JPanel();
		JSlider wSlider = new JSlider(JSlider.HORIZONTAL, 20, 150, sw);
		wSlider.setMajorTickSpacing(20);
		wSlider.setMinorTickSpacing(10);
		wSlider.setPaintTicks(true);
		wSlider.setPaintLabels(true);
		wSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				sw = wSlider.getValue();
				repaint();
			}

		});

		JSlider dwSlider = new JSlider(JSlider.HORIZONTAL, 20, 150, dw);
		dwSlider.setMajorTickSpacing(20);
		dwSlider.setMinorTickSpacing(10);
		dwSlider.setPaintTicks(true);
		dwSlider.setPaintLabels(true);
		dwSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				dw = dwSlider.getValue();
				repaint();
			}

		});

		JSlider hSlider = new JSlider(JSlider.HORIZONTAL, 50, 200, h);
		hSlider.setMajorTickSpacing(50);
		hSlider.setMinorTickSpacing(25);
		hSlider.setPaintTicks(true);
		hSlider.setPaintLabels(true);
		hSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				h = hSlider.getValue();
				repaint();
			}

		});

		JSlider dhSlider = new JSlider(JSlider.HORIZONTAL, 50, 200, h);
		dhSlider.setMajorTickSpacing(50);
		dhSlider.setMinorTickSpacing(25);
		dhSlider.setPaintTicks(true);
		dhSlider.setPaintLabels(true);
		dhSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				dh = dhSlider.getValue();
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
				// if(file.getName().substring(file.getName().length() -
				// 4).equals(".png")
				// || file.getName().substring(file.getName().length() -
				// 4).equals(".jpg")) {
				//
				// }

				if (file != null) {
					try {
						backgroundImage = ImageIO.read(file);
					} catch (IOException e1) {
						backgroundImage = null;
					}
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

		JSlider udSlider = new JSlider(JSlider.VERTICAL, -100, 100, 0);
		udSlider.setToolTipText("Adjust Up/Down");
		udSlider.setMajorTickSpacing(25);
		udSlider.setMinorTickSpacing(5);
		udSlider.setPaintTicks(true);
		udSlider.setPaintLabels(true);
		udSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				SHIFT = udSlider.getValue();
				repaint();
			}

		});

		JPanel north = new JPanel();
		north.add(button);
		north.add(more);

		south.add(wSlider);
		south.add(hSlider);
		south.add(dwSlider);
		south.add(dhSlider);

		frame.add(north, BorderLayout.NORTH);

		frame.add(south, BorderLayout.SOUTH);
		frame.add(udSlider, BorderLayout.EAST);
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
		g.setColor(Color.BLACK);
		paint(g);
		/* Now we perform our rendering */
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		g.drawString("A LogDog Creation", (int) pageFormat.getImageableWidth() - 6 * "A LogDog Creation".length() - 25,
				(int) pageFormat.getImageableHeight() - 50);
		g.drawString("Questions? Comments? Concerns?",
				(int) pageFormat.getImageableWidth() - 6 * "Questions? Comments? Concerns?".length() - 25,
				(int) pageFormat.getImageableHeight() - 30);
		g.drawString("makebracketsgreatagain@gmail.com",
				(int) pageFormat.getImageableWidth() - 6 * "makebracketsgreatagain@gmail.com".length() - 25,
				(int) pageFormat.getImageableHeight() - 10);

		// System.out.println(pageFormat.getImageableWidth()); //576
		// System.out.println(pageFormat.getImageableHeight()); //734
		/* tell the caller that this page is part of the printed document */
		return PAGE_EXISTS;
	}

}
