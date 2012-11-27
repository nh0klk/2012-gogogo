
import java.util.Random;

//import java.util.*;
public class MovePropose {
   // compare the score of the Node 
   // and choose the first 5 moves. 

   // finally, play the move
     Board a = new Board(); 
	 private int side = 0;
     private int[] wintime = null;
	 private int[] posMove = null;
	 private int[] bestfiveMoves = null;
	 private int bestmove = 0;
//	 private boolean[] win = new boolean[2];


public MovePropose(){

}

public int firstmove() throws Exception{
	bestfiveMoves = new int[5];

	// initiate the winRateList first
/*	bestfiveMoves[0] = a.winRateList.size()/2 - 1;
	bestfiveMoves[1] = a.winRateList.size()/2 + 1;
	bestfiveMoves[2] = a.winRateList.size()/2 + 2;
	bestfiveMoves[3] = (int) (a.winRateList.size()/2 + Math.sqrt(a.winRateList.size()));
	bestfiveMoves[4] = (int) (a.winRateList.size()/2 - Math.sqrt(a.winRateList.size()));

	bestfiveMoves[0] = 114;
	bestfiveMoves[1] = 111;
	bestfiveMoves[2] = 113;
	bestfiveMoves[3] = 97;
	bestfiveMoves[4] = 127;
	bestmove = 112; // a.winRateList.size()/2;
	*/	
	bestfiveMoves[0] = a.winRate.length / 2;
	bestfiveMoves[1] = a.winRate.length / 2 + 1;
	bestfiveMoves[2] = a.winRate.length / 2 - 1;
	bestfiveMoves[3] = a.winRate.length / 2 - a.size;
	bestfiveMoves[4] = a.winRate.length / 2 + a.size;
//	bestmove = a.winRate.length / 2;
	side = 1;
	
	wintime = new int[a.boardone.length];
	posMove = new int[a.boardone.length];
	
	System.out.println("hi, this is" + bestmove);
	for(int i=0;i < 5; i++)
	 {	 // use the initial five positions.
	    simulate(bestfiveMoves[i],side); // simulate to get the new wintime
	 }
	     
//	simulate(bestfiveMoves[0],side);
//	System.out.println("hi, second is" + bestmove);
	     movesearch(); // update the new bestmove and bestfiveMoves
	     System.out.println("hi, here is " + bestmove);
	     return bestmove;
	    
}
// do the movesearch, return the best
public void movesearch(){
	
for(int i = 0; i < a.winRate.length; i++){
	
	posMove[i] = i;
//	side = a.winRateList.get(i).side;
	wintime[i] = a.winRate[i];
//	System.out.println("posmove" + i + ":" + posMove[i]+"wintime:"+ wintime[i]+"\n");

	}
	int[] tempwin = new int[6];
	int[] tempposmove = new int[6];
	for(int i = 0; i < 6;i++){
		tempwin[i] = 0;
		tempposmove[i] = 0;
//		System.out.print(" ");
	}
//	System.out.println("hi!\n");
	//find the best six moves;
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
//	System.out.println("hi2!\n");
	//new bestmove and bestfivemoves for next step;
	bestmove = tempposmove[5];
//	System.out.println("bestmove"+ tempposmove[5] +"\n");
	for(int i = 0; i < 5;i++){
		bestfiveMoves[i] = tempposmove[i + 1];
	
	}
    // based on the wintime to get the bestmove and bestfiveMoves
	}
  


public int playmove(int[]game,int playside) throws Exception{
	
	a.boardone = game.clone();
	a.boardcopy = game.clone();
	side = playside;
	
	for(int i = 0;i < a.size;i++)
		for(int j = 0;j < a.size;j++)
			a.boardtwo[i][j] = a.boardone[i + j * 15];

	for(int i=0;i < 5; i++)
	 {	
	    simulate(bestfiveMoves[i],side); // get the new winRateList
	    a.clearboard();
	 }
	    movesearch(); // update the new bestmove and bestfiveMoves
	    System.out.println("hi, here is  " + bestmove);
	    return bestmove; 
	    
	
	 }


		 
		 
	
	
	protected int playRandomLegalMove() throws Exception{
		   final Random rnd = new Random();
	
		   int movepos ;
	// generate a random move	    
		   movepos = rnd.nextInt(225); 
    // if the move has not been played on the board
    // return the move. otherwise, generate a new random move.
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
    //add the move to the winRateList for further calculation.
    	
    	if(playside == side){
    		Move playmove = new Move(x);
    		a.winRateList.add(playmove); 
    	}
    	
    // if the game is end with the playing move.
    	
       if(a.isWin(playside)){
        	 if(playside == side){
  //      		 win[0] = true;
  //      		 win[1] = false;
            	 a.updateWinRate(a.winRateList,playside,true); // 
            	 a.updatetotalrate(a.winRateList);
        	 }else{
   //     		 win[0] = false;
   //     		 win[1] = true;
        		 a.updateWinRate(a.winRateList,playside,false); // 
            	 a.reducetotalrate(a.winRateList);
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
        //	int xCoord1= x % 15;
        //	int yCoord1 = x / 15;
        	a.boardtwo[xCoord][yCoord] = playside;
        	if(playside == side){
        		Move playmove1 = new Move(x);
        		a.winRateList.add(playmove1); 
        	}// the new winRateList
        	
        	}
         }
	
      }

	private void simulateplay(int x, int playside) throws Exception {


        a.boardone[x] = playside;
        
    	int xCoord= x % 15;
    	int yCoord = x / 15;
    	a.boardtwo[xCoord][yCoord] = playside;
    	 
     	if(playside == side){
    		Move playmove = new Move(x);
    		a.winRateList.add(playmove); 
    	}
   
        if(a.isfull()) return;
         
     	if(a.isWin(playside)) {
     		
     		if(playside == side){
    // 			win[0] = true;
    // 			win[1] = false;
    // 			System.out.println("hi5!\n");
     			a.updateWinRate(a.winRateList,playside,true);
     			a.updatetotalrate(a.winRateList);
     			return;
     		}else{
  //   			win[0] = false;
  //   			win[1] = true;
  //   			 a.updateWinRate(a.winRateList,playside,false); // 
            	 a.reducetotalrate(a.winRateList);
            	 return;
       	 }
     		
     	//	a.updateWinRate(a.winRateList,side,true);
     	}
     	else{
     		int y = playRandomLegalMove();
     		simulateplay( y , 0 - playside);
	}
       

    // totally 5*1000 5 roots * 1000 loops

	}

}
