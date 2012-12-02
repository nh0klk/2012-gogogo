package gomoku;

import monte.MovePropose;

public class ChessBoardTest {
	
	public ChessBoardTest(){
		
	}
	
	//黑子Monte
	//白字MiniMax
	public void TestMonteMiniMax() throws Exception{
		//初始化
		int blackWin = 0;
		int whiteWin = 0;
		for(int i =0; i< 100 ;i++){
		    boolean isGameOver = false;
		    int[] chessBoard = new int[ChessBoardConstant.ChessBoardWidth * ChessBoardConstant.ChessBoardWidth];
		    Minimax minimaxPlayer = new Minimax(chessBoard);
		    MovePropose montePlayer = new MovePropose();
		    ChessBoardChecker chessBoardChecker = new ChessBoardChecker();
		    int currentPlayer = ChessBoardConstant.PlayerBlack;
		    int newMove = 0;
			while(! isGameOver){
				if(currentPlayer==ChessBoardConstant.PlayerBlack){
					//模拟monte
					newMove = montePlayer.play(chessBoard, currentPlayer);
				}
				else{
					//模拟minimax
					newMove = minimaxPlayer.getBestMove(currentPlayer, 4, chessBoard);
				}
				
				//检查结果
				chessBoard[newMove] = currentPlayer;
				if(chessBoardChecker.isWin(currentPlayer, chessBoard,newMove)){
					isGameOver = true;
				}
				else{
					currentPlayer = ChessBoardHelper.getNextPlayer(currentPlayer);
				}
			}
			
			if(currentPlayer==ChessBoardConstant.PlayerBlack){
				blackWin++;
				System.out.println("第: "+ String.valueOf(i) +"局黑胜");
			}
			else{
				whiteWin++;
				System.out.println("第: "+ String.valueOf(i) +"局白胜");
			}
			
		}
		System.out.println("黑棋Monte胜: "+ String.valueOf(blackWin) +"局");
		System.out.println("白棋Minimax胜: "+ String.valueOf(whiteWin) + "局");
	}
}
