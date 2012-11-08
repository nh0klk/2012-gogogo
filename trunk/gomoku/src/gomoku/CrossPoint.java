package gomoku;

import java.awt.Color;
import java.awt.Graphics;

public class CrossPoint {
	private int piece = 0;
	private int X;
	private int Y;
	private boolean isCurrent;
	public CrossPoint(int x, int y) {
		this.setX(x);
		this.setY(y);
		this.setPiece(0);
	}
	public int getPiece() {
		return piece;
	}
	public void setPiece(int piece) {
		this.piece = piece;
	}
	public int getX() {
		return X;
	}
	public void setX(int x) {
		X = x;
	}
	public int getY() {
		return Y;
	}
	public void setY(int y) {
		Y = y;
	}
	public void paint(Graphics g) {
		int width;
		int r;
		Color color;
		switch (piece) {
			case -1 :
				width = Game.WidthV;
				color = Color.BLACK;
				break;
			case 1 :
				width = Game.WidthV;
				color = Color.WHITE;
				break;
			default :
				width = Game.WidthO;
				color = Color.BLACK;
				break;
		}
		r = width / 2;
		g.setColor(color);
		g.fillOval(X - r, Y - r, width, width);
		if (isCurrent)
			g.setColor(Color.RED);
		else
			g.setColor(Color.BLACK);
		g.drawOval(X - r, Y - r, width, width);
	}
	public boolean isCurrent() {
		return isCurrent;
	}
	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}
}