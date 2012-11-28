package monte;

import gomoku.Minimax;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

//import java.util.*;
public class MovePropose {
   // compare the score of the Node 
   // and choose the first 5 moves. 

   // finally, play the move
     Board a = new Board(); 
	 private int side = 0;
 //    private int[] wintime = null;
//	 private int[] posMove = null;
	 boolean flag = false;
	 int temp;
	 private int[] bestfiveMoves = null;
	 private int bestmove = 0;


public MovePropose(){

}
public int play(int[]game,int playside) throws Exception{
	if(bestfiveMoves == null){
		return firstmove(game, playside);
	}else
	{
		return playmove(game, playside);
	}
	
}
public int playmove(int[]game,int playside) throws Exception{
	
	a.boardone = game.clone();
	a.boardcopy = game.clone();
	side = playside;
	
	for(int i = 0;i < a.size;i++)
		for(int j = 0;j < a.size;j++)
			a.boardtwo[i][j] = a.boardone[i + j * 15];

	for(int i = 0;i < 5; i++)
	 {
		SmartSimulate(bestfiveMoves[i]);
//		simulate(bestfiveMoves[i],side); // get the new winRateList
	    a.clearboard();
	 }
	    movesearch(); // update the new bestmove and bestfiveMoves
	    System.out.println("hi, here is 2 " + bestmove);
	    if(bestmove == 0)
	    {
	    	System.out.println("bestfivemove");
	    	for(int i = 0 ; i < 225; i++)
	    	{
	    		System.out.println(a.winRate[i]);
	    	}
	    }
	    return bestmove; 
	    
	
	 }
public int firstmove(int[]game,int playside) throws Exception{
	
	bestfiveMoves = new int[5];
	//initial as the bestmoves in the center.
	bestfiveMoves[0] = a.winRate.length / 2;
	bestfiveMoves[1] = a.winRate.length / 2 + 1;
	bestfiveMoves[2] = a.winRate.length / 2 - 1;
	bestfiveMoves[3] = a.winRate.length / 2 - a.size;
	bestfiveMoves[4] = a.winRate.length / 2 + a.size;
	side = playside;
	
//	wintime = new int[a.boardone.length];
//	posMove = new int[a.boardone.length];
	
	System.out.println("hi, this is" + bestmove);
	for(int i=0;i < 5; i++)
	 {	 
//		SmartSimulate(bestfiveMoves[i]);
		simulate(bestfiveMoves[i],side); // get the new winRateList
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
	for(int i = 0; i < a.winRate.length; i++){
		if(a.boardone[i] == 0){
			Wintime w = new Wintime();
			w.index = i;
			w.winrate = a.winRate[i];
			wintimelist.add(w);
		}else continue;
		}
	Collections.sort(wintimelist, new WintimeComparator());
	bestmove = wintimelist.get(wintimelist.size() - 1).index;
	for(int i = 0 ;i < 5;i++)
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
  
	 

	protected int playRandomLegalMove() throws Exception{//return a legal move
		   final Random rnd = new Random();
	
		   int movepos ;
		    
		   movepos = rnd.nextInt(225); 
		   while(!a.checkPlay(movepos)) 
		   {  
			   movepos = rnd.nextInt(225); 
           }
		   
		   
		   return movepos;
		   
		    
		}
	
		  
	public void simulate(int x, int playside) throws Exception //  simulate play till the very end from the root(one of the best moves)
    { 
 	//play move;
 	a.boardone[x] = playside; 
 	int xCoord= x % 15;
 	int yCoord = x / 15;
 	a.boardtwo[xCoord][yCoord] = playside;
 // if this is my side, generate a move.
 // a move has position and wintime of the position.
 // add the move to the winRateList for further calculation.
 	
 	if(playside == side){
 		Move playmove = new Move(x);
 		a.winRateList.add(playmove); 
 	}
 	
 // if the game is end with the playing move.
 	if(a.isfull())return;
    if(a.isWin(playside)){
     	 if(playside == side){
         	 a.updateWinRate(a.winRateList,playside,true); 
         	 a.updatetotalrate(a.winRateList);
         	return;
     	 }else{
     		 a.updateWinRate(a.winRateList,playside,false); 
         	 a.reducetotalrate(a.winRateList);
         	return;
     	 }     	
      }
      else 
      {    	   
     for(int i = 0; i < 1000;i++)
      {

         int y = playRandomLegalMove();
      	 simulateplay(y,0 - playside);
      	 
      	a.clearboard();
     // clear the board, and 
     // after clearing the board, should replay the move x.
     // and we will have a new winRateList.
      	a.boardone[x] = playside; 
     	a.boardtwo[xCoord][yCoord] = playside;
     	if(playside == side){
     		Move playmove1 = new Move(x);
     		a.winRateList.add(playmove1); 
     	}// the new winRateList
     	
     	}
      }
    }
     


	private void simulateplay(int x, int playside) throws Exception {

       //play the move
        a.boardone[x] = playside;       
    	int xCoord= x % 15;
    	int yCoord = x / 15;
    	a.boardtwo[xCoord][yCoord] = playside;
    	//add to the trace of side 
     	if(playside == side){
    		Move playmove = new Move(x);
    		a.winRateList.add(playmove); 
    	}
   
        if(a.isfull()) return;
         
        if(a.isWin(playside)){
        	 if(playside == side){
            	 a.updateWinRate(a.winRateList,playside,true); 
            	 a.updatetotalrate(a.winRateList);
            	return;
        	 }else{
        		 a.updateWinRate(a.winRateList,playside,false); 
            	 a.reducetotalrate(a.winRateList);
            	return;
        	 }
     		
     	}
     	else{
     		int y = playRandomLegalMove();
     		simulateplay( y , 0 - playside);
	}
       

    

	}
	
	public void SmartSimulate(int x) throws Exception{
		
		for(int i = 0; i < a.size * a.size;i++)
		{
			a.boardone[i] = side;
			int xCoord= x % 15;
	    	int yCoord = x / 15;
	    	a.boardtwo[xCoord][yCoord] = side;
	    	if(a.isWin(side))
	    	{
	    		flag = true;
	    		temp = i;
	    		return;
	    	}
	    	a.boardone[i] = 0;
	    	a.boardtwo[xCoord][yCoord] = 0;
		}
		
		
		//play this move;
		a.boardone[x] = side; 
    	int xCoord= x % 15;
    	int yCoord = x / 15;
    	a.boardtwo[xCoord][yCoord] = side;    	
 
    	Move playmove = new Move(x);
    	a.winRateList.add(playmove);   		    	
    	
    	//judge win;
        if(a.isWin(side)){
            a.updateWinRate(a.winRateList,side,true); 
            a.updatetotalrate(a.winRateList);
            return;
        }
        
        
 //     System.out.println("hi1!\n");
        //oppose play in minmax
        Minimax mn = new Minimax(a.boardone);
    	int m = mn.getBestMove(0 - side, 2);
//    	System.out.println("hi!"+ m +"\n");
    	a.boardone[m] = 0 - side;
    	int xCoord1 = m % 15;
    	int yCoord1 = m / 15;
    	a.boardtwo[xCoord1][yCoord1] = 0 - side;
    	if(a.isWin(0 - side))
    		{
    	//	System.out.println("hiwin1!\n");
    		 a.updateWinRate(a.winRateList,side,false); // 
         	 a.reducetotalrate(a.winRateList);
         	 flag = true;
         	 temp = m;
    		return;
    		}
    	if(a.isfull())return;
    	//simulate
        for(int i = 0; i < 1000;i++)       	 
        {
        	int y = playRandomLegalMove();
         	 simulateplay(y,side);

        	a.clearboard();
        	
        	a.boardone[x] = side; 
        	a.boardtwo[xCoord][yCoord] = side;    	     
        	a.winRateList.add(playmove);   		    	
        	
        	//judge win;
            if(a.isWin(side)){
                a.updateWinRate(a.winRateList,side,true); 
                a.updatetotalrate(a.winRateList);
                return;
            }
            //oppose play in minmax
        	a.boardone[m] = 0 - side;
        	a.boardtwo[xCoord1][yCoord1] = 0 - side;
        	if(a.isWin(0 - side))
        		{
        	//	System.out.println("hiwin1!\n");
        		 a.updateWinRate(a.winRateList,side,false); // 
             	 a.reducetotalrate(a.winRateList);
        		return;
        		}
        	if(a.isfull())return; 
       	
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
