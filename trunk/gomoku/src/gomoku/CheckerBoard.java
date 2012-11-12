package gomoku;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class CheckerBoard extends JPanel {
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private final ArrayList<Line> lines = new ArrayList<Line>();
	public ArrayList<CrossPoint> points = new ArrayList<CrossPoint>();

	public void initBoard(int n) {
		Game.dist = (this.getWidth() - Game.margin * 2) / (n - 1);
		int x0 = Game.margin;
		int y0 = Game.margin;
		int x1 = x0;
		int y1 = y0;
		int x2 = this.getWidth() - Game.margin;
		int y2 = y0;
		for (int i = 0; i < n; i++) {
			addLine(x1, y1, x2, y2);
			y1 += Game.dist;
			y2 += Game.dist;
		}
		x1 = x0;
		y1 = y0;
		x2 = x0;
		y2 = this.getWidth() - Game.margin;
		for (int i = 0; i < n; i++) {
			addLine(x1, y1, x2, y2);
			x1 += Game.dist;
			x2 += Game.dist;
		}
		x1 = x0;
		for (int i = 0; i < n; i++) {
			x1 = x0;
			for (int j = 0; j < n; j++) {
				addPoint(x1, y1);
				x1 += Game.dist;
			}
			y1 += Game.dist;
		}
	}

	public void cleanBoard() {
		for (CrossPoint cp : points) {
			cp.setPiece(0);
		}
	}

	private void addPoint(int x, int y) {
		this.points.add(new CrossPoint(x, y));
	}

	private void addLine(int x1, int y1, int x2, int y2) {
		this.lines.add(new Line(x1, y1, x2, y2));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (final Line r : lines) {
			r.paint(g);
		}
		for (final CrossPoint cp : points) {
			cp.paint(g);
		}
	}
}

class Line {
	public final int x1;
	public final int x2;
	public final int y1;
	public final int y2;
	public Line(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}
	public void paint(Graphics g) {
		g.drawLine(this.x1, this.y1, this.x2, this.y2);
	}
}
