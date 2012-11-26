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
	
	public int getBestMove(int Player, int SearchDepth) {
		
		nodenumber = 0;
		
		if (Player == ChessBoardConstant.PlayerBlack) {
			max(-1000001, 1000001, SearchDepth);
		} else {
			min(-1000001, 1000001, SearchDepth);
		}
		
		System.out.println(nodenumber);
		return bestmove[SearchDepth-1];
	}
	private int max(int alpha, int beta, int SearchDepth) {
		nodenumber++;
		if (SearchDepth == 0)
			return chessBoardChecker.getScore(ChessBoardConstant.PlayerWhite,pieces);
		int a = alpha;
		int b = beta;
		for (int i = 0; i < Game.n * Game.n; i++) {
			if (a >= b)
				return a;
			if (pieces[i]!=0)
				continue;
			pieces[i] = -1;
			int a1 = min(a, b, SearchDepth - 1);
			if (a1==970000)
				a1+=0;
			if (a1 > a) {
				a = a1;
				bestmove[SearchDepth-1] = i;
			}
			pieces[i] = 0;
		}
		return a;
	}
	
	private int min(int alpha, int beta, int SearchDepth) {
		nodenumber++;
		if (SearchDepth == 0)
			return chessBoardChecker.getScore(ChessBoardConstant.PlayerBlack,pieces);
		int a = alpha;
		int b = beta;
		for (int i = 0; i < Game.n * Game.n; i++) {
			if (a >= b)
				return b;
			if (pieces[i]!=0)
				continue;
			pieces[i] = 1;
			int b1 = max(a, b, SearchDepth - 1);
			if (b1 < b) {
				b = b1;
				
				bestmove[SearchDepth-1] = i;
			}
			pieces[i] = 0;
		}
		return b;
	}

}
