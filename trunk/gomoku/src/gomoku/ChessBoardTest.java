package gomoku;

public class ChessBoardTest {
	
	//黑子Monte
	//白字MiniMax
	public void TestMonteMiniMax(){
		//初始化
	    boolean isGameOver = false;
	    int[] chessBoard = new int[ChessBoardConstant.ChessBoardWidth * ChessBoardConstant.ChessBoardWidth];
	    Minimax minimaxPlayer = new Minimax(chessBoard);
	    ChessBoardChecker chessBoardChecker = new ChessBoardChecker();
	    int currentPlayer = ChessBoardConstant.PlayerBlack;
	    int newMove = 0;
		while(! isGameOver){
			if(currentPlayer==ChessBoardConstant.PlayerBlack){
				//模拟monte
			}
			else{
				//模拟minimax
				newMove = minimaxPlayer.getBestMove(currentPlayer, 3, chessBoard);
			}
			
			//检查结果
			chessBoard[newMove] = currentPlayer;
			if(chessBoardChecker.isWin(currentPlayer, chessBoard)){
				isGameOver = true;
			}
			else{
				currentPlayer = ChessBoardHelper.GetNextPlayer(currentPlayer);
			}
		}
		System.out.println(ChessBoardConstant.PlayerBlack);
	}
}
