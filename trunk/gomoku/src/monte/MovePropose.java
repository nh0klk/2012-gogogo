package monte;

import gomoku.ChessBoardCheckArea;
import gomoku.ChessBoardChecker;
import gomoku.ChessBoardConstant;
import gomoku.ChessBoardHelper;
import gomoku.ChessBoardTimer;
import gomoku.Minimax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

//import java.util.*;
public class MovePropose {
   // compare the score of the Node 
   // and choose the first 5 moves. 
   // finally, play the move
     Board board = new Board(); 
	 private int side = 0;
	 boolean flag = false;
	 int temp;
	 private int[] bestfiveMoves = null;
	 private int bestmove = 0;
	 private Long timeCountRandom;
	 private Long timeCountSimulate;
	 private ChessBoardChecker chessBoardChecker = new ChessBoardChecker();

public MovePropose(){

}
public int play(int[]game,int playside) throws Exception{
	timeCountRandom = (long) 0;
	timeCountSimulate = (long) 0;
	if(bestfiveMoves == null){
		System.out.println("Random Function: " + timeCountRandom.toString());
		System.out.println("Simulate Function: " + timeCountSimulate.toString());
		return firstmove(game, playside);
	}else
	{
		System.out.println("Random Function: " + timeCountRandom.toString());
		System.out.println("Simulate Function: " + timeCountSimulate.toString());
		return playmove(game, playside);
	}
	
}
public int playmove(int[]game,int playside) throws Exception{
	
	board.boardone = game.clone();
	board.boardcopy = game.clone();
	side = playside;
	
	for(int i = 0;i < board.size;i++)
		for(int j = 0;j < board.size;j++)
			board.boardtwo[i][j] = board.boardone[i + j * 15];

	for(int i = 0;i < 20; i++)
	 {
		if(board.boardone[bestfiveMoves[i]] == 0){
			SmartSimulate(bestfiveMoves[i],10000);
//			(bestfiveMoves[i],side); // get the new winRateList
			board.clearboard();
		}
		else
			continue;
	 }
	    movesearch(); // update the new bestmove and bestfiveMoves
	    System.out.println("hi, here is 2 " + bestmove);
	    return bestmove; 
	    
	
	 }

	public int firstmove(int[]game,int playside) throws Exception{
		
		Boolean isChessBoardEmpty = ChessBoardHelper.emptyChessBoard(game);
		ArrayList<Integer> blankList = new ArrayList<Integer>();
		ChessBoardCheckArea chessBoardCheckArea;
		int[] seedBoard = game.clone();
		
		board.boardone = game.clone();
		board.boardcopy = game.clone();
		bestfiveMoves = new int[25];
		side = playside;
	
		//随机选取n个子做为种子
		if(isChessBoardEmpty){
			seedBoard[112]=ChessBoardConstant.PlayerBlack;
		}
		chessBoardCheckArea = new ChessBoardCheckArea(seedBoard,2);
		
		//初始化 种子区域
		for (int row = chessBoardCheckArea.getTopEdge(); row <= chessBoardCheckArea.getBottomEdge(); row++) {
			for(int column = chessBoardCheckArea.getLeftEdge() ; column<=chessBoardCheckArea.getRightEdge(); column++){
				int index = ChessBoardHelper.getListIndex(row, column);
				if(game[index]==ChessBoardConstant.Blank){
					blankList.add(index);
				}
			}
		}
		
		//在区域内随机选取种子
		for(int i =0; i<25; i++){
			if(blankList.size()==0)
				break;
        	int random =(new Random()).nextInt(blankList.size());
        	int randomIndex = blankList.get(random);
        	blankList.remove(random);
        	bestfiveMoves[i] = randomIndex;
		}
		
		System.out.println("hi, this is" + bestmove);
		
		for(int i = 0;i < 25; i++){	 
			SmartSimulate(bestfiveMoves[i],10000);
		 }
		
	     movesearch(); // update the new bestmove and bestfiveMoves
	     System.out.println("hi, here is " + bestmove);
	     
	     if(isChessBoardEmpty)
	    	 return 112;
	     return bestmove;
		    
	}
// do the movesearch, return the best
	public void movesearch(){
	//initial the helper structures	
		if(flag == false){
		LinkedList<Wintime> wintimelist = new LinkedList<Wintime>();
		for(int i = 0; i < board.winRate.length; i++){
			if(board.boardone[i] == 0){
				Wintime w = new Wintime();
				w.index = i;
				w.winrate = board.winRate[i];
				wintimelist.add(w);
			}
			else continue;
		}
		Collections.sort(wintimelist, new WintimeComparator());
	//	int[] tem = new int[6];
	
		bestmove = wintimelist.get(wintimelist.size() - 1).index;
			for(int i = 0 ;i < 20;i++)
			{
				bestfiveMoves[i] = wintimelist.get(wintimelist.size() - 2 - i).index;
			}
		
		}else{ 
			bestmove = temp;
		}
	}
     


	private void simulateplay(int x, int playside, ArrayList<Integer> blankList) throws Exception {
		
       //play the move
        board.boardone[x] = playside;       
        int emptyChessCount = ChessBoardHelper.emptyChessCount(board.boardone);
 		if(blankList.size()!=emptyChessCount){
 			int a =5;
 		}
    	//add to the trace of side 
     	if(playside == side){
    		Move playmove = new Move(x);
    		board.winRateList.add(playmove); 
    	}
   
        if(board.isfull()) return;
         
        if(chessBoardChecker.isWin(playside, board.boardone, x)){
        	 if(playside == side){
            	 board.updatetotalrate(board.winRateList);
            	return;
        	 }
        	 else{
            	 board.reducetotalrate(board.winRateList);
            	return;
        	 }
     		
     	}
     	else{
     		int random = (new Random()).nextInt(blankList.size());
     		int randomIndex = blankList.get(random);
     		blankList.remove(random);
     		simulateplay( randomIndex , 0 - playside, blankList);
	}
       

    

	}
	
	public void SmartSimulate(int index, int playTimes) throws Exception{
		
		flag = false;
		for(int i = 0; i < board.size * board.size;i++){
			if(board.boardone[i] == 0){
				board.boardone[i] = side;
		    	if(board.isWin(side)){
		    		flag = true;
		    		temp = i;
		    		return;
		    	}
		    	board.boardone[i] = 0;
			}
		}
		
		
		
		//play this move;
		board.boardone[index] = side; 
    	Move playmove = new Move(index);
    	board.winRateList.add(playmove);
		
    	//judge win;
        if(board.isWin(side)){
     //       a.updateWinRate(a.winRateList,side,true); 
            board.updatetotalrate(board.winRateList);
            return;
        }
      //oppose play in minmax
        Minimax mn = new Minimax(board.boardone);
    	int m = mn.getBestMove(0 - side, 2);
    	board.boardone[m] = 0 - side;
    	
	    if(board.isWin(0 - side)){
		 	 board.reducetotalrate(board.winRateList);
		 	 flag = true;
		 	 temp = m;
			 return;
		}
	    
    	if(board.isfull())return;
    	
    	ArrayList<Integer> blankList = new ArrayList<Integer>();
		for(int i = 0; i < board.size * board.size;i++)
		{
			if(board.boardone[i] == 0){
				blankList.add(i);
			}
		}
		
        for(int i = 0; i < playTimes;i++) {
        	ArrayList<Integer> blankListTemp = (ArrayList<Integer>) blankList.clone();
        	int random =(new Random()).nextInt(blankListTemp.size());
        	int randomIndex = blankListTemp.get(random);
        	blankListTemp.remove(random);
         	simulateplay(randomIndex,side,blankListTemp);

        	board.clearboard();
        	
        	board.boardone[index] = side; 	     
        	board.winRateList.add(playmove);   		    	
        	
        	//judge win;
            if(board.isWin(side)){
          //      a.updateWinRate(a.winRateList,side,true); 
                board.updatetotalrate(board.winRateList);
                return;
            }
            //oppose play in minmax
        	board.boardone[m] = 0 - side;
        	if(board.isWin(0 - side)){
				board.reducetotalrate(board.winRateList);
				return;
			}
        	if(board.isfull())
        		return; 
       	}
    }
	private class WintimeComparator implements  Comparator<Wintime>{
		@Override
		public int compare(Wintime o1, Wintime o2) {
			return  o1.winrate.compareTo(o2.winrate);
		}
	}
	public class Wintime {
		
		Integer winrate;
		int index;
	}
}
