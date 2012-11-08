package gomoku;

import java.io.FileWriter;
import java.io.IOException;
import java.util.GregorianCalendar;

import javax.swing.JLabel;

public class Game {

	public static int buffer = 3;
	public static int margin = 20;
	public static int dist;
	public static int n = 15;
	public static int Turn = 0;
	public static boolean game = false;
	public static int WidthV = 20;
	public static int WidthO = 6;
	public static int[] pieces = new int[n * n + 1];
	public static CheckerBoard board;
	public static JLabel label;
	public static int lastmove;
	/*
	 * H = 1000 = 8 V = 100 = 4 R = 10 = 2 L = 1
	 */
	public static int[] pieceVisits;
	public static int Count6 = 0;
	public static int Count5 = 0;
	public static int Count4 = 0;
	public static int Count3 = 0;
	public static int BlackWins = 0;
	public static int WhiteWins = 0;
	public static int Draws = 0;
	public static int[] player = new int[2];
	public static long runs = 0;
	public static boolean putTest(CrossPoint cp) {
		return cp.getPiece() == 0;
	}
	public static boolean putTest(int index) {
		return pieces[index] == 0;
	}
	public static CrossPoint putPiece(CrossPoint cp) {
		cp.setPiece(getColor());
		return cp;
	}
	public static void putPiece(int index) {
		pieces[index] = getColor();
	}
	public static int transPoint(int x, int y) {
		int j = (x - 20 + dist / 2) / dist;
		int i = (y - 20 + dist / 2) / dist;
		return i * n + j;
	}
	public static boolean isTerminated() {
		for (int pc : pieces) {
			if (pc == 0)
				return false;
		}
		return true;
	}
	public static long score() {
		return 0;
	}
	public static int stateCheck() {
		Count6 = 0;
		Count5 = 0;
		Count4 = 0;
		Count3 = 0;
		pieceVisits = new int[n * n + 1];
		int color = getColor();
		int count = 0;
		while (count < n * n) {
			int pc = pieces[count];
			if (pc != color) {
				count++;
				continue;
			}
			int step = checkFive(count);
			count += step;
		}
		if (Turn == 1) {
			if (Count5 + Count6 > 0)
				return -1;
		} else {
			if (Count5 > 0)
				return 1;
			if (Count4 >= 2)
				return -1;
			if (Count3 >= 2)
				return -1;
			if (Count6 >= 1)
				return -1;
		}
		return 0;
	}
	public static int checkFive(int index) {
		int step = 1;
		int i = getI(index);
		int j = getJ(index);
		int pc = pieces[index];

		checkByDir(pc, index, i, j, 'H', step);
		checkByDir(pc, index, i, j, 'V', 0);
		checkByDir(pc, index, i, j, 'R', 0);
		checkByDir(pc, index, i, j, 'L', 0);
		return step;
	}
	public static int checkByDir(int color, int index, int i, int j, char dir,
			int step) {
		int pv = pieceVisits[index];
		if (isTested(pv, dir) == 0) {
			int c = 0;
			setTested(index, dir);
			int tmpIndex;
			do {
				c++;
				tmpIndex = getNewIndex(i, j, dir, c);
				setTested(tmpIndex, dir);
			} while (pieces[tmpIndex] == color);
			test(i, j, dir, c, color);
			return c;
		}
		return step;
	}
	public static int getNewIndex(int i, int j, char dir, int step) {
		switch (dir) {
			case 'H' :
				return getIndex(i, j + step);
			case 'V' :
				return getIndex(i + step, j);
			case 'R' :
				return getIndex(i + step, j + step);
			case 'L' :
				return getIndex(i + step, j - step);
		}
		return -1;

	}
	public static int getPiece(int i, int j, char dir, int step) {
		switch (dir) {
			case 'H' :
				return pieces[getIndex(i, j + step)];
			case 'V' :
				return pieces[getIndex(i + step, j)];
			case 'R' :
				return pieces[getIndex(i + step, j + step)];
			case 'L' :
				return pieces[getIndex(i + step, j - step)];
		}
		return 2;
	}
	public static void test(int i, int j, char dir, int c, int color) {
		int head = getPiece(i, j, dir, -1);
		int rear = getPiece(i, j, dir, c);
		int rear1 = getPiece(i, j, dir, c + 1);
		int rear2 = getPiece(i, j, dir, c + 2);
		int rear3 = getPiece(i, j, dir, c + 3);
		int rear4 = getPiece(i, j, dir, c + 4);
		if (c >= 6)
			Count6++;
		if (c == 5)
			Count5++;
		if (Turn == 0) {
			if (c == 4) {
				if (head == 0 || rear == 0)
					Count4++;
			}
			if (c == 3) {
				if (head == 0 && rear == 0)
					Count3++;
				if (rear == 0 && rear1 == color && rear2 != color)
					Count4++;
			}
			if (c == 2) {
				if (head == 0 && rear == 0 && rear1 == color && rear2 == 0)
					Count3++;
				if (rear == 0 && rear1 == color && rear2 == color
						&& rear3 != color)
					Count4++;
			}
			if (c == 1) {
				if (head == 0 && rear == 0 && rear1 == color && rear2 == color
						&& rear3 == 0)
					Count3++;
				if (rear == 0 && rear1 == color && rear2 == color
						&& rear3 == color && rear4 != color)
					Count4++;
			}
		}
	}
	public static int getColor() {
		return Turn * 2 - 1;
	}
	public static int getResult(int index) {
		putPiece(index);
		int result = stateCheck();
		if (result != 0)
			game = true;
		if (result == 1)
			BlackWins++;
		if (result == -1)
			WhiteWins++;
		if (isTerminated() && result == 0) {
			game = true;
			result = 2;
			Draws++;
		}

		return result;
	}
	public static void initGame(int player1, int player2, long times) {
		initGame(player1, player2);
		runs = times;
	}
	public static void initGame(int player1, int player2) {
		player[0] = player1;
		player[1] = player2;
		BlackWins = 0;
		WhiteWins = 0;
		Draws = 0;
		initGame();
	}
	public static void initGame() {
		pieces = new int[n * n + 1];
		pieces[n * n] = 2;
		Turn = 0;
		game = false;
		Count6 = 0;
		Count5 = 0;
		Count4 = 0;
		Count3 = 0;
		lastmove = -1;
		Thread t = new Thread() {
			public void run() {
				callPlayer(player[Turn]);
			}
		};
		t.start();
	}
	public static void switchPlayer() {
		if (game) {
			return;
		}
		Turn = (Turn + 1) % 2;
		label.setText(Game.Turn == 1 ? "White" : "Black");
		Thread t = new Thread() {
			public void run() {
				callPlayer(player[Turn]);
			}
		};
		t.start();
	}
	public static void callPlayer(int player) {
		switch (player) {
			case 0 :
				break;
			case 1 :
				minimax();
				break;
			case 2 :
				monte();
				break;
		}
	}
	public static void minimax() {
		Minimax mm = new Minimax();
		int index = mm.getBestMove(getColor(), 3);
		System.out.print(index + " ");

		pieces[index] = getColor();
		displayNewPiece(index);
		int result = getResult(index);
		updateLabel(result);
		switchPlayer();
	}
	public static void displayNewPiece(int index) {
		CrossPoint cp = board.points.get(index);
		cp.setPiece(getColor());
		cp.setCurrent(true);
		if (lastmove != -1) {
			CrossPoint cl = board.points.get(lastmove);
			cl.setCurrent(false);
		}
		lastmove = index;
		board.repaint();
	}
	public static void updateLabel(int result) {
		switch (result) {
			case 1 :
				label.setText("Black Wins");
				return;
			case -1 :
				label.setText("White Wins");
				return;
			case 2 :
				label.setText("Draw");
				return;
		}
	}

	public static String statsContent() {
		return String.format(
				"%d Games\n\tBlack\tWhite\nName\t%s\t%s\nWins\t%d\t%d\n",
				BlackWins + WhiteWins + Draws, playerName(player[0]),
				playerName(player[1]), BlackWins, WhiteWins);
	}
	public static String playerName(int player) {
		switch (player) {
			case 0 :
				return "Human";
			case 1 :
				return "Minimax";
			case 2 :
				return "Monte Carlo";
		}
		return "Unknown";
	}
	public static void monte() {

	}
	public static void saveStats(String s) {
		GregorianCalendar c = new GregorianCalendar();
		int year = c.get(GregorianCalendar.YEAR);
		int month = c.get(GregorianCalendar.MONTH);
		int date = c.get(GregorianCalendar.DATE);
		int hour = c.get(GregorianCalendar.HOUR_OF_DAY);
		int minute = c.get(GregorianCalendar.MINUTE);
		int second = c.get(GregorianCalendar.SECOND);
		String recordname = String.format("record %d-%d-%d %d:%d:%d\n", year,
				month, date, hour, minute, second);
		try {
			FileWriter fw = new FileWriter("./stats.txt", true);
			fw.append(recordname + s);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static int getIndex(int i, int j) {
		if (i < 0 || i >= n || j < 0 || j >= n)
			return n * n;
		return i * n + j;
	}
	public static int getI(int index) {
		return index / n;
	}
	public static int getJ(int index) {
		return index % n;
	}
	public static int getDirCode(char c) {
		switch (c) {
			case 'H' :
				return 8;
			case 'V' :
				return 4;
			case 'R' :
				return 2;
			case 'L' :
				return 1;
		}
		return 0;
	}
	public static void setTested(int i, int j, char dir, int step) {
		setTested(getNewIndex(i, j, dir, step), dir);
	}
	public static void setTested(int index, char dir) {
		pieceVisits[index] |= getDirCode(dir);
	}
	public static int isTested(int pv, char c) {
		int k = getDirCode(c);
		return pv & k;
	}
	public static int getTurn(int color) {
		return (color + 1) / 2;
	}
}
