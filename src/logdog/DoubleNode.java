package logdog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DoubleNode extends JPanel {

	private static final long serialVersionUID = 1L;
	private DoubleNode top, bottom, parent;
	private boolean female, cbr = true, showGame = true;
	private int seed;
	private Color lineColor = Color.black, seedColor = Color.black;

	public DoubleNode(int seed) {
		seed = getNextTwo(seed);
		this.seed = seed;
		// System.out.println("woox " + seed);
		female = true;
		parent = null;
		top = new DoubleNode(false, seed, this, true);
		bottom = new DoubleNode(true, seed, this, false);

		this.seed = -seed;
	}

	private DoubleNode(boolean female, int seed, DoubleNode parent, boolean istop) {
		// System.out.println("woo " + seed);
		this.female = female;
		this.seed = seed;
		this.parent = parent;
		if (seed <= 2)
			return;

		// males have no children
		if (!female) {
			return;
		}

		// if top is female have 2 daughters
		if ((istop && female)) {
			top = new DoubleNode(false, this.seed, this, true);
			bottom = new DoubleNode(true, this.seed, this, false);
			this.seed = -seed;
			return;
		}

		// if has brother
		if (!parent.top.female) {
			top = new DoubleNode(true, this.seed / 2, this, true);
			bottom = new DoubleNode(true, this.seed / 2, this, false);
			this.seed = -seed;
			return;
		}

		// if(is female and has sister), then have top son and bottom daughter
		if (parent.top.female && !istop) {
			top = new DoubleNode(false, this.seed, this, true);
			bottom = new DoubleNode(true, this.seed, this, false);
			this.seed = -seed;
			return;
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

	public void setShowGame(boolean b) {
		showGame = b;
		if (top != null) {
			top.setShowGame(b);
		}
		if (bottom != null) {
			bottom.setShowGame(b);
		}
	}

	// from DoubleNode.java
	public int getHighestSeed() {
		return (getHighestSeed(top) > getHighestSeed(bottom) ? getHighestSeed(top) : getHighestSeed(bottom));
	}

	// returns biggest seed on the outskirts, musn't have children
	public int getHighestSeed(DoubleNode s) {
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

	public DoubleNode findCellThatHasSeed(final int SEED) {
		if (top != null && bottom != null) {
			return findCellThatHasSeed(SEED, this);
		}
		return null;
	}

	public DoubleNode findCellThatHasSeed(final int SEED, DoubleNode s) {
		if (s.seed == SEED && s.cbr) {
			return s;
		}

		DoubleNode sTOP, sBOT;

		if (s.bottom != null) {
			sBOT = findCellThatHasSeed(SEED, s.bottom);
			if (sBOT != null)
				return sBOT;
		}

		if (s.top != null) {
			sTOP = findCellThatHasSeed(SEED, s.top);
			if (sTOP != null)
				return sTOP;
		}

		return null;
	}

	public void reset() {
		cbr = true;
		if (top != null)
			top.reset();
		if (bottom != null)
			bottom.reset();
	}
	// end copy pasta

	private void removeChildren() {
		top = null;
		bottom = null;
	}

	public static int getNextTwo(int n) {
		int i = 0;
		while ((Math.log(n) / Math.log(2)) > i)
			i++;
		return (int) Math.pow(2, i);
	}

	/*
	 * PRECONDITION: teams >= 3
	 * 
	 * @param teams number of teams playing
	 * 
	 * @return the head of the bracket
	 */
	public static DoubleNode makeDoubleElim(int teams) {
		DoubleNode dn = new DoubleNode(teams);
		int tot = getNextTwo(teams);
		boolean cont = false;
		while (tot > teams) {
			// System.out.println('r');
			DoubleNode p, pb;
			try {
				p = dn.findCellThatHasSeed(-4);
				pb = p.bottom;
			} catch (Exception e) {
				dn.reset();
				cont = true;
				continue;
			}

			if (!cont) {
				pb.seed = -p.seed;
				pb.removeChildren();
				pb.cbr = false;
				p.cbr = false;
			} else {
				p.seed = -p.seed;
				p.removeChildren();
				p.cbr = false;
			}
			tot--;
		}

		dn.reset();

		int seedToAdd = 1 - (teams);
		int high = dn.getHighestSeed();
		while (seedToAdd < 0) {
			// System.out.println(high);
			dn.findCellThatHasSeed(high).seed = seedToAdd;
			high = dn.getHighestSeed();
			seedToAdd++;
		}
		//
		return dn;
	}

	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		drawFractal(g, 500, 200, 40, 250);
	}

	public void drawFractal(Graphics g, int x, int y, int w, int h) {
		g.setColor(Color.black);
		g.drawRect(0, 0, WIDTH, HEIGHT);
		g.setColor(seedColor);
		if ((!female || top == null || bottom == null) && showGame)
			g.drawString("L" + (-seed), x - 25, y + 5);

		g.setColor(lineColor);
		g.drawLine(x, y, x + w, y);

		if (top != null) {
//			if (top.top == null) {
//				g.drawLine(x, y, x, y - h);
//				top.drawFractal(g, x - w, y - h, w, h);
//			} else {
				g.drawLine(x, y, x, y - 2*h/3);
				top.drawFractal(g, x - w, y - (2*h/3), w, 2*h/3);
			//}
		}

		if (bottom != null) {
			g.drawLine(x, y, x, y + 2 * h / 3);
			bottom.drawFractal(g, x - w, y + (2 * h / 3), w, 2* h / 3);
		}
	}

}
