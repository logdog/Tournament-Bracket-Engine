package logdog;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class RoundRobin extends JPanel implements Printable {

	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Week> weeks;

	private BufferedImage[] images;
	
	private ArrayList<String> textLines = new ArrayList<String>();
	
	public RoundRobin(ArrayList<Player> ps) {
		images = new BufferedImage[ps.size() / 30 + 1];
		players = ps;
		
		weeks = new ArrayList<Week>();
		
		textLines.add("Round Robin Tournament");
		textLines.add("A Log Dog Creation");
		textLines.add("Questions? Comments? Concerns?");
		textLines.add("Contact us at makebracketsgreatagain@gmail.com");
		textLines.add("");
		
		for (int week = 1; week <= players.size(); week++) {
			weeks.add(new Week(week));
			textLines.add("");
			textLines.add("** Week " + week + " **");
			for (int i = 0; i < players.size(); i += 2) {
				try {
					weeks.get(week - 1).addGame(new Game(players.get(i), players.get(i+1)));
					textLines.add(players.get(i).getName() + " vs. " + players.get(i+1).getName());
				} catch (Exception e) {
					textLines.add(players.get(i).getName() + " bye");
				}
			}
			Player temp = players.get(0);
			players.remove(0);
			players.add(temp);
		}
		
		for(Week w : weeks)
			add(w);
	}

	public void printTable() {
		for (int week = 1; week <= players.size(); week++) {
			System.out.println("Week " + week + ":");
			for (int i = 0; i < players.size(); i += 2) {
				try {
					System.out.println(players.get(i).getName() + " vs. " + players.get(i + 1).getName());
				} catch (Exception e) {
					System.out.println(players.get(i).getName() + " bye");
				}
			}
			Player temp = players.get(0);
			players.remove(0);
			players.add(temp);
			System.out.println();
		}
	}
	
//	public void makePopup() {
//		JFrame frame = new JFrame("Round Robin");
//		frame.setBackground(Color.yellow);
//		frame.setMinimumSize(new Dimension(100, 100));
//		frame.setResizable(true);
//		frame.setLocationRelativeTo(null);
//		
//		
//		for(Week w : weeks) {
//			add(w);
//		}
//		
//		frame.getContentPane().add(this);
//		
//		frame.pack();
//		frame.setVisible(true);
//	}
	
	public void paint(Graphics g) {
		for(Week w : weeks)
			w.paint(g);
	}
	
	public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
		// TODO Auto-generated method stub
		
		Font font = new Font("Serif", Font.PLAIN, 10);
        FontMetrics metrics = g.getFontMetrics(font);
        int lineHeight = metrics.getHeight();
 
            
        int linesPerPage = (int)(pf.getImageableHeight()/lineHeight);
        int numBreaks = (textLines.size()-1)/linesPerPage;
          
        int[] pageBreaks;
        pageBreaks = new int[numBreaks];
        for (int b=0; b<numBreaks; b++) {
            pageBreaks[b] = (b+1)*linesPerPage; 
        }
        
        if (pageIndex > numBreaks) {
            return NO_SUCH_PAGE;
        }
 
        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         * Since we are drawing text we
         */
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
 
        /* Draw each line that is on this page.
         * Increment 'y' position by lineHeight for each line.
         */
        int y = 0; 
        int start = (pageIndex == 0) ? 0 : pageBreaks[pageIndex-1];
        int end   = (pageIndex == pageBreaks.length)
                         ? textLines.size() : pageBreaks[pageIndex];
        for (int line=start; line<end; line++) {
            y += lineHeight;
            g.drawString(textLines.get(line), 0, y);
        }
 
        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
	}


	private class Week extends JComponent{
		
		private static final long serialVersionUID = 1L;
		int weekNumber;
		ArrayList<Game> games = new ArrayList<Game>();
		
		Week(int n) {
			weekNumber = n;
		}
		
		private int getWeek() {
			  return weekNumber;
		}
		
		private void addGame(Game g) {
			games.add(g);
		}
		
		public void paint(Graphics g) {
			for(Game gg : games)
				gg.paint(g);
		}
		
	}

	private class Game extends JComponent{
		
		private static final long serialVersionUID = 1L;
		Player p1, p2;

		Game(Player p1, Player p2) {
			this.p1 = p1;
			this.p2 = p2;
		}
		
		public void paint(Graphics g) {
			g.drawRect(0, 0, 60, 30);
			g.drawString(p1.getName() + " vs " + p2.getName(), 5, 5);
			g.drawLine(0, 10, 60, 10);
		}

	}
}
