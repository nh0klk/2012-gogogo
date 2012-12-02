package monte;

/*A board for playing Gomoku.  Conceptually, a board is a square composed of
 * points.  Each point may be empty or hold a black or a white stone.  The
 * points can be addressed either by pairs of x/y coordinates, ranging from
 * 1 to the width of the board, or by a single z index, ranging from 0 to one
 * less than the number of points on the board. 
   */
import gomoku.ChessBoardChecker;
import gomoku.ChessBoardConstant;

import java.util.LinkedList;

import java.util.*;

public class Board {

		  private int winner;

		  private int point;
		  private boolean isGameOver;
		  int[] boardcopy;
		  int[] boardone;
		  int[][] boardtwo;
          LinkedList<Move> winRateList;
          public  double[] winRate = new double[ChessBoardConstant.ChessBoardWidth*ChessBoardConstant.ChessBoardWidth];
          
          

		public Board(){
		 
		  int row , column , i;
		  boardone = new int[ChessBoardConstant.ChessBoardWidth*ChessBoardConstant.ChessBoardWidth];
		  boardcopy = new int[ChessBoardConstant.ChessBoardWidth*ChessBoardConstant.ChessBoardWidth];
		  boardtwo = new int[ChessBoardConstant.ChessBoardWidth][ChessBoardConstant.ChessBoardWidth];
		  
		  for(int t = 0; t < ChessBoardConstant.ChessBoardWidth*ChessBoardConstant.ChessBoardWidth;t++)
		  {
			  winRate[t] = 0;
		  }
		 
		  for(i=0;i<=ChessBoardConstant.ChessBoardWidth; i++){
			  boardone[i] =0;
		  }
		  boardcopy = boardone.clone();
		  
		  for(row=0; row< ChessBoardConstant.ChessBoardWidth; row++)
			for(column =0; column<ChessBoardConstant.ChessBoardWidth; column++)
				boardtwo[row][column] = 0;
		  
		  winRateList = new LinkedList<Move>();
		  isGameOver = false;
		}
        public int getPoint(){return point;}
        public boolean isGameOver(){return isGameOver;}
		public boolean canMove(int point){
			if(boardone[point] == 0){
			   boardone[point] = 1;
			   return true;
			}
			return false;
		}
	    public void reducetotalrate(LinkedList<Move> winRateList)
	    {
	    	for(int i = 0;i < winRateList.size();i++){
	    		int t = winRateList.get(i).numPoints;
	    		winRate[t]--;
	    	}
	    }

	    
	    public void updatetotalrate(LinkedList<Move> winRateList)
	    {
	    	for(int i = 0;i < winRateList.size();i++){
	    		int t = winRateList.get(i).numPoints;
	    		winRate[t]++;
	    	}
	    }
        
		public boolean isWin(int player){
			ChessBoardChecker chessBoardChecker = new ChessBoardChecker();
			return chessBoardChecker.isWin(player, boardone);
		}
		    
		    public void clearboard()
		    {
		    	boardone = boardcopy.clone();
		    	winRateList.clear();
		    	for(int a = 0; a < ChessBoardConstant.ChessBoardWidth;a++){
		    		for(int j = 0; j < ChessBoardConstant.ChessBoardWidth;j++){
		    			boardtwo[a][j] = boardone[a + j * ChessBoardConstant.ChessBoardWidth];
		    		}
		    	}
		    	isGameOver = false;
		    }
		    	
		    }

		  



