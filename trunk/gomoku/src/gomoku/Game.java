package gomoku;

import java.io.FileWriter;
import java.io.IOException;
import java.util.GregorianCalendar;

import javax.swing.JLabel;

import monte.MovePropose;

public class Game {

	public static int buffer = 3;
	public static int margin = 20;
	public static int dist;
	public static int n = 15;
	public static int Turn = 0;
	public static boolean game = false;
	public static int WidthV = 20;
	public static int WidthO = 6;
	public static int[] pieces = new int[n * n];
	public static ChessBoardDrawer board;
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
	public static MovePropose monte = new MovePropose();
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
	
	public static int getColor() {
		return Turn * 2 - 1;
	}
	public static int getResult(int index) {
		putPiece(index);

		ChessBoardChecker chessBoardChecker = new ChessBoardChecker();
		if(chessBoardChecker.isWin(ChessBoardConstant.PlayerBlack, pieces)){
			game = true;
			return ChessBoardConstant.BlackWin;
		}
		if(chessBoardChecker.isWin(ChessBoardConstant.PlayerWhite, pieces)){
			game = true;
			return ChessBoardConstant.WhiteWin;
		}
		Draws++;
		return ChessBoardConstant.Continue;
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
		pieces = new int[n * n];
		//pieces[n * n] = 2;
		Turn = 0;
		game = false;
		Count6 = 0;
		Count5 = 0;
		Count4 = 0;
		Count3 = 0;
		lastmove = -1;
		monte = new MovePropose();
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
		Minimax mm = new Minimax(Game.pieces);
		ChessBoardTimer timer = new ChessBoardTimer();
		timer.start();
		int index = mm.getBestMove(getColor(), 3);
		timer.end();
		timer.printDuration(System.out);
		timer.reset();
		timer.start();
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
			case ChessBoardConstant.BlackWin :
				label.setText("Black Wins");
				return;
			case ChessBoardConstant.WhiteWin :
				label.setText("White Wins");
				return;
			case ChessBoardConstant.Continue :
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

		int nextMove = 0;
		if(ChessBoardHelper.emptyChessBoard(Game.pieces))
			try {
				nextMove = monte.firstmove();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		else{
			try {
				nextMove = monte.playmove(Game.pieces, getColor());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		pieces[nextMove] = getColor();
		displayNewPiece(nextMove);
		int result = getResult(nextMove);
		updateLabel(result);
		switchPlayer();
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
			return n * n-1;
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
