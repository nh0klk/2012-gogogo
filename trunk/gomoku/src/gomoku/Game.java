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
	public int getResult(int index) {
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
	public void initGame(int player1, int player2, long times) {
		initGame(player1, player2);
		runs = times;
	}
	public void initGame(int player1, int player2) {
		player[0] = player1;
		player[1] = player2;
		BlackWins = 0;
		WhiteWins = 0;
		Draws = 0;
		initGame();
	}
	public void initGame() {
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
		AIThread t = new AIThread ();
		t.start();
	}
	public void switchPlayer() {
		if (game) {
			return;
		}
		Turn = (Turn + 1) % 2;
		label.setText(Game.Turn == 1 ? "White" : "Black");
		AIThread t = new AIThread ();
		t.start();
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
	
	public class AIThread extends Thread{
		
		public AIThread() {
			
		}
		public void run() {
			callPlayer(player[Turn]);
		}
		
		public void callPlayer(int player) {
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
		public void minimax() {
			Minimax mm = new Minimax(Game.pieces);
			ChessBoardTimer timer = new ChessBoardTimer();
			int index = mm.getBestMove(getColor(), 3);

			pieces[index] = getColor();
			displayNewPiece(index);
			int result = getResult(index);
			updateLabel(result);
			switchPlayer();
		}
		public void monte() {

			int nextMove = 0;
			try {
				nextMove = monte.play(Game.pieces, getColor());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pieces[nextMove] = getColor();
			displayNewPiece(nextMove);
			int result = getResult(nextMove);
			updateLabel(result);
			switchPlayer();
		}
	}
}
