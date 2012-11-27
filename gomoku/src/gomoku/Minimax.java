package gomoku;


public class Minimax {
	private int[] pieces;
	private ChessBoardChecker chessBoardChecker = new ChessBoardChecker();
	public int bestscore;
	public int[] bestmove=new int[6];
	public int depth;
	public int color;
	public int nodenumber;
	public Minimax() {
		pieces = (int[]) Game.pieces.clone();
	}
	public Minimax(int[] p) {
		pieces = (int[]) p.clone();
	}
	
	public int getBestMove(int player, int searchDepth) {
		
		nodenumber = 0;
		
		if (player == ChessBoardConstant.PlayerBlack) {
			max(-1000001, 1000001, searchDepth);
		} else {
			min(-1000001, 1000001, searchDepth);
		}
		
		System.out.println(nodenumber);
		return bestmove[searchDepth-1];
	}
	public  int getBestMove(int player, int searchDepth, int[] chessBoardStatus) {
		pieces = chessBoardStatus.clone();
		return getBestMove(player,searchDepth);
	}
	private int max(int alpha, int beta, int searchDepth) {
		nodenumber++;
		if (searchDepth == 0)
			return chessBoardChecker.evaluateValue(ChessBoardConstant.PlayerWhite,pieces);
		int a = alpha;
		int b = beta;
		for (int i = 0; i < ChessBoardConstant.ChessBoardWidth* ChessBoardConstant.ChessBoardWidth; i++) {
			if (a >= b)
				return a;
			if (pieces[i]!=0)
				continue;
			pieces[i] = -1;
			int a1 = min(a, b, searchDepth - 1);
			if (a1==970000)
				a1+=0;
			if (a1 > a) {
				a = a1;
				bestmove[searchDepth-1] = i;
			}
			pieces[i] = 0;
		}
		return a;
	}
	
	private int min(int alpha, int beta, int searchDepth) {
		nodenumber++;
		if (searchDepth == 0)
			return chessBoardChecker.evaluateValue(ChessBoardConstant.PlayerBlack,pieces);
		int a = alpha;
		int b = beta;
		for (int i = 0; i < ChessBoardConstant.ChessBoardWidth* ChessBoardConstant.ChessBoardWidth; i++) {
			if (a >= b)
				return b;
			if (pieces[i]!=0)
				continue;
			pieces[i] = 1;
			int b1 = max(a, b, searchDepth - 1);
			if (b1 < b) {
				b = b1;
				
				bestmove[searchDepth-1] = i;
			}
			pieces[i] = 0;
		}
		return b;
	}

}
