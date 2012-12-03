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
	 private ArrayList<Integer> bestfiveMoves =  new ArrayList<Integer>();;
	 private int bestmove = 0;
	 private Long timeCountRandom;
	 private Long timeCountSimulate;
	 private ChessBoardChecker chessBoardChecker = new ChessBoardChecker();
	 public int simulateTimes = 20000;
	 public int threadCount = 12;
	 public int miniMaxStep = 2;
	 public int trainingTimes = 2;

	public MovePropose(){
	
	}
	public int play(int[]game,int playside) throws Exception{
		timeCountRandom = (long) 0;
		timeCountSimulate = (long) 0;
		int result = 0 ;
		if(bestfiveMoves.size()==0){
			result = firstmove(game, playside);
		}
		else{
			result = playmove(game, playside);
		}
	    return result;
	}
	
	public int playmove(int[]game,int playside) throws Exception{
		board.boardone = game.clone();
		board.boardcopy = game.clone();
		side = playside;
		//先进行进攻
		int attackIndex = attackTry();
		if(attackIndex!=-1)
			return attackIndex;
		//后进行防守
		int defendIndex = defendTry();
		if(defendIndex!=-1)
			return defendIndex;
		//模拟多步
		//board.winRate = new long[ChessBoardConstant.ChessBoardWidth*ChessBoardConstant.ChessBoardWidth];
		
    	for(int j = 0; j<ChessBoardConstant.ChessBoardWidth*ChessBoardConstant.ChessBoardWidth;j++){
    		board.winRate[j] = 0.4*board.winRate[j];
        } 
    	
		for(int k=0; k<trainingTimes;k++){
				for(int i = 0;i < 20&&i<bestfiveMoves.size(); i++){
					if(board.boardone[bestfiveMoves.get(i)] == 0){
						SmartSimulate(bestfiveMoves.get(i),simulateTimes);
			//			(bestfiveMoves[i],side); // get the new winRateList
						board.clearboard();
					}
					else
						continue;
					movesearch(); // update the new bestmove and bestfiveMoves
			    }
		}
		
	    bestmove = bestfiveMoves.get(0);
	    bestfiveMoves.remove(0);
	    return bestmove; 	
	
	 }
	
	public int defendTry(){
		for(int i = 0; i < ChessBoardConstant.ChessBoardWidth*ChessBoardConstant.ChessBoardWidth;i++){
			if(board.boardone[i] == 0){
				board.boardone[i] = 0-side;
		    	if(chessBoardChecker.isWin(0-side, board.boardone,i)){
		    		return i;
		    	}
		    	board.boardone[i] = 0;
			}
		}
		return -1;
	}
	
	public int attackTry(){
		for(int i = 0; i < ChessBoardConstant.ChessBoardWidth*ChessBoardConstant.ChessBoardWidth;i++){
			if(board.boardone[i] == 0){
				board.boardone[i] = side;
		    	if(chessBoardChecker.isWin(side, board.boardone,i)){
		    		return i;
		    	}
		    	board.boardone[i] = 0;
			}
		}
		return -1;
	}
	
	public int firstmove(int[]game,int playside) throws Exception{
		
		Boolean isChessBoardEmpty = ChessBoardHelper.emptyChessBoard(game);
		ArrayList<Integer> blankList = new ArrayList<Integer>();
		ChessBoardCheckArea chessBoardCheckArea;
		int[] seedBoard = game.clone();
		int seedCount = 24;
		board.boardone = game.clone();
		board.boardcopy = game.clone();
		side = playside;

		
		//随机选取n个子做为种子
		if(isChessBoardEmpty){
			seedBoard[112]=ChessBoardConstant.PlayerBlack;
			seedCount=25;
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
		for(int i =0; i<seedCount; i++){
			if(blankList.size()==0)
				break;
        	int random =(new Random()).nextInt(blankList.size());
        	int randomIndex = blankList.get(random);
        	blankList.remove(random);
        	bestfiveMoves.add(randomIndex);
		}
		
		for(int i = 0;i < seedCount; i++){	 
			SmartSimulate(bestfiveMoves.get(i),simulateTimes);
		 }
		
	     movesearch(25); // update the new bestmove and bestfiveMoves
	     
	     if(isChessBoardEmpty)
	    	 return 112;
	     else{
			 ArrayList<Integer> whiteMoveList = new ArrayList<Integer>();
			 ChessBoardCheckArea chessBoardCheckAreaWhite =  new ChessBoardCheckArea(seedBoard,1); 
			 for (int row = chessBoardCheckAreaWhite.getTopEdge(); row <= chessBoardCheckAreaWhite.getBottomEdge(); row++) {
				for(int column = chessBoardCheckAreaWhite.getLeftEdge() ; column<=chessBoardCheckAreaWhite.getRightEdge(); column++){
					int index = ChessBoardHelper.getListIndex(row, column);
					if(game[index]==ChessBoardConstant.Blank){
						whiteMoveList.add(index);
					}
				}
			 }
			 int random =(new Random()).nextInt(whiteMoveList.size());
			 return whiteMoveList.get(random);
	     }
	}
	
// do the movesearch, return the best
	public void movesearch(){
		movesearch(20);
	}
	public void movesearch(int bestCount){
	//initial the helper structures	
		LinkedList<Wintime> wintimelist = new LinkedList<Wintime>();
		bestfiveMoves =  new ArrayList<Integer>();
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
	
		for(int i = 0 ;i < bestCount&&i<wintimelist.size();i++)
		{
			bestfiveMoves.add(wintimelist.get(wintimelist.size() - 1 - i).index); 
		}
	}
	
	public void SmartSimulate(int index, int playTimes) throws Exception{
		
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
    	int m = mn.getBestMove(0 - side, miniMaxStep);
    	board.boardone[m] = 0 - side;
    	
	    if(board.isWin(0 - side)){
		 	 board.reducetotalrate(board.winRateList);
		 	 flag = true;
		 	 temp = m;
			 return;
		}
	    
    	if(ChessBoardHelper.isfull(board.boardone))return;
    	
    	ArrayList<Integer> blankList = new ArrayList<Integer>();
		for(int i = 0; i < ChessBoardConstant.ChessBoardWidth * ChessBoardConstant.ChessBoardWidth;i++)
		{
			if(board.boardone[i] == 0){
				blankList.add(i);
			}
		}
		/////////////////////////////////
/*    	int random =(new Random()).nextInt(blankList.size());
    	int randomIndex = blankList.get(random);
    	blankList.remove(random);
    	board.boardone[randomIndex] = side;
    	  if(board.isWin(side)){
    		     //       a.updateWinRate(a.winRateList,side,true); 
    		            board.updatetotalrate(board.winRateList);
    		            return;
    		        }
    	  
        mn = new Minimax(board.boardone);
      	m = mn.getBestMove(0 - side, miniMaxStep);
      	board.boardone[m] = 0 - side;
      	
  	    if(board.isWin(0 - side)){
  		 	 board.reducetotalrate(board.winRateList);
  		 	 flag = true;
  		 	 temp = m;
  			 return;
  	    }*/
  		///////////////////////////////////
    	  
		SimulatePlayThread[] threadList = new SimulatePlayThread[threadCount];
        for(int i=0; i<threadList.length; i++) {  
        	threadList[i] = new SimulatePlayThread(playTimes/threadCount,board.boardone,blankList);  
        	threadList[i].start();  
        }  
        
        for(Thread th : threadList) {  
            try {  
                th.join();//join方式  
            } catch (InterruptedException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }
        
        for(int i=0; i<threadList.length; i++) {
        	SimulatePlayThread simulatePlayThread = threadList[i];
        	for(int j = 0; j<ChessBoardConstant.ChessBoardWidth*ChessBoardConstant.ChessBoardWidth;j++){
        		board.winRate[j]+=0.6*simulatePlayThread.winRate[j];
        	}
        } 
    }
	private class WintimeComparator implements  Comparator<Wintime>{
		@Override
		public int compare(Wintime o1, Wintime o2) {
			return  o1.winrate.compareTo(o2.winrate);
		}
	}
	public class Wintime {
		
		Double winrate;
		int index;
	}
	
	public class SimulatePlayThread extends Thread{
		private int[] chessBoard;
		private int[] initChessBoard;
		public double[] winRate = new double[ChessBoardConstant.ChessBoardWidth*ChessBoardConstant.ChessBoardWidth];
		private ArrayList<Integer> gameMoveList;
		private int simulateCount =0;
		private ArrayList<Integer> blankList;
		private ChessBoardChecker chessBoardChecker = new ChessBoardChecker();
		@SuppressWarnings("unchecked")
		public SimulatePlayThread(int simulateCount , int[] initChessBoard, ArrayList<Integer> blankList){
			 this.initChessBoard = initChessBoard.clone();
			 this.simulateCount = simulateCount;
			 this.blankList = (ArrayList<Integer>) blankList.clone();
		}
		
		@SuppressWarnings("unchecked")
		public void run(){
			for(int i = 0; i<simulateCount; i++){
				gameMoveList = new ArrayList<Integer>();
				chessBoard = initChessBoard.clone();
	        	ArrayList<Integer> blankListTemp = (ArrayList<Integer>) blankList.clone();
	        	int random =(new Random()).nextInt(blankListTemp.size());
	        	int randomIndex = blankListTemp.get(random);
	        	blankListTemp.remove(random);
	         	simulateplay(randomIndex,side,blankListTemp);
			}
		}
		
		private void simulateplay(int index, int playside, ArrayList<Integer> blankList){
			//play the move
		    chessBoard[index] = playside;       
	        int emptyChessCount = ChessBoardHelper.emptyChessCount(chessBoard);
	    	//add to the trace of side 
	     	if(playside == side){   
	        	gameMoveList.add(index);   
	    	}
	   
	        if(ChessBoardHelper.isfull(chessBoard)) return;
	         
	        if(chessBoardChecker.isWin(playside, chessBoard, index)){
	        	 if(playside == side){
	            	updatetotalrate();
	            	return;
	        	 }
	        	 else{
	            	 reducetotalrate();
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
		
	    public void reducetotalrate(){
	    	for(int i = 0;i < gameMoveList.size();i++){
	    		winRate[gameMoveList.get(i)] -= (double)2;
	    	}
	    }

	    
	    public void updatetotalrate(){
	    	for(int i = 0;i < gameMoveList.size();i++){
	    		winRate[gameMoveList.get(i)]+= (double)1;
	    	}
	    }
	}
}
