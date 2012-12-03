package gomoku;


public class Minimax {
	private int[] pieces;
	private ChessBoardChecker chessBoardChecker = new ChessBoardChecker();
	private ChessBoardCheckArea chessBoardCheckArea =null;
	public int bestscore;
	public int[] bestmove=new int[6];
	public int depth;
	public int color;
	public int nodenumber;
	public int avgnumber =0;
	public int stepcount =0;
	public Minimax(int[] p) {
		pieces = (int[]) p.clone();
	}
	
	public int getBestMove(int player, int searchDepth) {
		nodenumber = 0;
		stepcount++;
		if(ChessBoardHelper.emptyChessBoard(pieces))
			return 112;
		chessBoardCheckArea = new ChessBoardCheckArea(pieces);
		if (player == ChessBoardConstant.PlayerBlack) {
			max(-1000001, 1000001, searchDepth,chessBoardCheckArea);
		} else {
			min(-1000001, 1000001, searchDepth,chessBoardCheckArea);
		}
		avgnumber+=nodenumber;
		//System.out.println((double)avgnumber/(double)stepcount);
		return bestmove[searchDepth-1];
	}
	
	public  int getBestMove(int player, int searchDepth, int[] chessBoardStatus) {
		pieces = chessBoardStatus.clone();
		return getBestMove(player,searchDepth);
	}
	
	private int max(int alpha, int beta, int searchDepth, ChessBoardCheckArea chessBoardCheckArea) {
		int rightEdge = chessBoardCheckArea.getRightEdge();
		int leftEdge = chessBoardCheckArea.getLeftEdge();
		int topEdge = chessBoardCheckArea.getTopEdge();
		int bottomEdge = chessBoardCheckArea.getBottomEdge();
		nodenumber++;
		if (searchDepth == 0)
			return chessBoardChecker.evaluateValue(ChessBoardConstant.PlayerWhite,pieces);
		int a = alpha;
		int b = beta;
		for (int row = topEdge; row <=bottomEdge; row++) {
			for(int column = leftEdge ; column<=rightEdge; column++){
				int index = ChessBoardHelper.getListIndex(row, column);
				if (a >= b)
					return a;
				//非空白点
				if (pieces[index]!=ChessBoardConstant.Blank)
					continue;
				//下一个黑子
				pieces[index] = ChessBoardConstant.PlayerBlack;
				if(chessBoardChecker.isWin(ChessBoardConstant.PlayerBlack,pieces, index)){
					pieces[index]  = ChessBoardConstant.Blank;
					bestmove[searchDepth-1] = index;
					return 1000000;
				}
				int a1 = min(a, b, searchDepth - 1, chessBoardCheckArea.expandCheckArea(index));
				if (a1 > a) {
					a = a1;
					bestmove[searchDepth-1] = index;
				}
				pieces[index] =ChessBoardConstant.Blank;	
			}
		}
		return a;
	}
	
	private int min(int alpha, int beta, int searchDepth,ChessBoardCheckArea chessBoardCheckArea) {
		nodenumber++;
		int rightEdge = chessBoardCheckArea.getRightEdge();
		int leftEdge = chessBoardCheckArea.getLeftEdge();
		int topEdge = chessBoardCheckArea.getTopEdge();
		int bottomEdge = chessBoardCheckArea.getBottomEdge();
		if (searchDepth == 0)
			return chessBoardChecker.evaluateValue(ChessBoardConstant.PlayerBlack,pieces);
		int a = alpha;
		int b = beta;
		for (int row = topEdge; row <=bottomEdge; row++) {
			for(int column = leftEdge ; column<=rightEdge; column++){
				int index = ChessBoardHelper.getListIndex(row, column);
				if (a >= b)
					return b;
				if (pieces[index]!=ChessBoardConstant.Blank)
					continue;
				pieces[index] = ChessBoardConstant.PlayerWhite;
				if(chessBoardChecker.isWin(ChessBoardConstant.PlayerWhite,pieces, index)){
					pieces[index]  = ChessBoardConstant.Blank;
					bestmove[searchDepth-1] = index;
					return -1000000;
				}
				int b1 = max(a, b, searchDepth - 1,  chessBoardCheckArea.expandCheckArea(index));
				if (b1 < b) {
					b = b1;
					bestmove[searchDepth-1] = index;
				}
				pieces[index] = ChessBoardConstant.Blank;
			}
		}
		return b;
	}

}
