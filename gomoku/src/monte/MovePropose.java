package monte;

import gomoku.ChessBoardChecker;
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
 //    private int[] wintime = null;
//	 private int[] posMove = null;
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
	/*
	for(int i = 0; i < a.size * a.size; i++)
	{
		a.winRate[i] = 0;
	}
	*/
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
		SmartSimulate(bestfiveMoves[i]);
//		simulate(bestfiveMoves[i],side); // get the new winRateList
	    board.clearboard();
	 }
	    movesearch(); // update the new bestmove and bestfiveMoves
	    System.out.println("hi, here is 2 " + bestmove);
	    /*
	    if(bestmove == 0)
	    {
	    	System.out.println("bestfivemove");
	    	for(int i = 0 ; i < 225; i++)
	    	{
	    		System.out.println(a.winRate[i]);
	    	}
	    }
	    */
	    return bestmove; 
	    
	
	 }
public int firstmove(int[]game,int playside) throws Exception{
	
	board.boardone = game.clone();
	board.boardcopy = game.clone();
	
	bestfiveMoves = new int[20];
	//initial as the bestmoves in the center.
	bestfiveMoves[0] = board.winRate.length / 2;
	bestfiveMoves[1] = board.winRate.length / 2 + 1;
	bestfiveMoves[2] = board.winRate.length / 2 - 1;
	bestfiveMoves[3] = board.winRate.length / 2 - board.size;
	bestfiveMoves[4] = board.winRate.length / 2 + board.size;
	side = playside;
	
//	wintime = new int[a.boardone.length];
//	posMove = new int[a.boardone.length];
	
	System.out.println("hi, this is" + bestmove);
	for(int i = 0;i < 5; i++)
	 {	 
		SmartSimulate(bestfiveMoves[i]);
//		simulate(bestfiveMoves[i],side); // get the new winRateList
	 }
	     
	     movesearch(); // update the new bestmove and bestfiveMoves
	     System.out.println("hi, here is " + bestmove);
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
		}else continue;
		}
	Collections.sort(wintimelist, new WintimeComparator());
//	int[] tem = new int[6];

	bestmove = wintimelist.get(wintimelist.size() - 1).index;
	for(int i = 0 ;i < 20;i++)
	{
		bestfiveMoves[i] = wintimelist.get(wintimelist.size() - 2 - i).index;
	}
	
	}else
	{ 
		bestmove = temp;
	}

/*
	int[] tempwin = new int[6];
	int[] tempposmove = new int[6];
	for(int i = 0; i < 6;i++){
		tempwin[i] = -1000000000;
		tempposmove[i] = 0;
	}
	//find the best six moves
	for(int i = 0 ; i < a.boardone.length;i++){
		if(wintime[i] > tempwin[0] && a.boardone[i] == 0){	
			//greater than the tempwin[0] and not been occupied.
			tempwin[0] = wintime[i];
			tempposmove[0] = posMove[i];
			//sort the temp arrays, upper order;
			 for(int a = 5;a > 0;a--)
				 for(int b = 0;b < a;b++)
					 if(tempwin[b] > tempwin[b + 1]){
						 int t = tempwin[b];
						 tempwin[b] = tempwin[b + 1];
						 tempwin[b + 1] = t;
						 int t1 = tempposmove[b];
						 tempposmove[b] = tempposmove[b + 1];
						 tempposmove[b + 1] = t1;						 
			   }
			}	
	}
	
	//new bestmove and bestfivemoves for next step;
	bestmove = tempposmove[5];
	for(int i = 0; i < 5;i++){
		bestfiveMoves[i] = tempposmove[i + 1];
	}
	*/
	}
 	  
/*	public void simulate(int x, int playside) throws Exception //  simulate play till the very end from the root(one of the best moves)
    { 
 	//play move;
 	board.boardone[x] = playside; 
 	int xCoord= x % 15;
 	int yCoord = x / 15;
 	board.boardtwo[xCoord][yCoord] = playside;
 // if this is my side, generate a move.
 // a move has position and wintime of the position.
 // add the move to the winRateList for further calculation.
 	
 	if(playside == side){
 		Move playmove = new Move(x);
 		board.winRateList.add(playmove); 
 	}
 	
 // if the game is end with the playing move.
 	if(board.isfull())return;
    if(board.isWin(playside)){
     	 if(playside == side){
    //     	 a.updateWinRate(a.winRateList,playside,true); 
         	 board.updatetotalrate(board.winRateList);
         	return;
     	 }else{
     //		 a.updateWinRate(a.winRateList,playside,false); 
         	 board.reducetotalrate(board.winRateList);
         	return;
     	 }     	
      }
      else 
      {    	   
     for(int i = 0; i < 10000;i++)
      {

         int y = playRandomLegalMove();
      	 simulateplay(y,0 - playside);
      	 
      	board.clearboard();
     // clear the board, and 
     // after clearing the board, should replay the move x.
     // and we will have a new winRateList.
      	board.boardone[x] = playside; 
     	board.boardtwo[xCoord][yCoord] = playside;
     	if(playside == side){
     		Move playmove1 = new Move(x);
     		board.winRateList.add(playmove1); 
     	}// the new winRateList
     	
     	}
      }
    }*/
     


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
	
	public void SmartSimulate(int x) throws Exception{
		
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
		board.boardone[x] = side; 
    	Move playmove = new Move(x);
    	board.winRateList.add(playmove);
		
    	//judge win;
        if(board.isWin(side)){
     //       a.updateWinRate(a.winRateList,side,true); 
            board.updatetotalrate(board.winRateList);
            return;
        }
        
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
		
        for(int i = 0; i < 10000;i++) {
        	ArrayList<Integer> blankListTemp = (ArrayList<Integer>) blankList.clone();
        	int random =(new Random()).nextInt(blankListTemp.size());
        	int randomIndex = blankListTemp.get(random);
        	blankListTemp.remove(random);
         	simulateplay(randomIndex,side,blankListTemp);

        	board.clearboard();
        	
        	board.boardone[x] = side; 	     
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
        	if(board.isfull())return; 
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
