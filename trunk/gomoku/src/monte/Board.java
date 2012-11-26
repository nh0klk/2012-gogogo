package monte;

/*A board for playing Gomoku.  Conceptually, a board is a square composed of
 * points.  Each point may be empty or hold a black or a white stone.  The
 * points can be addressed either by pairs of x/y coordinates, ranging from
 * 1 to the width of the board, or by a single z index, ranging from 0 to one
 * less than the number of points on the board. 
   */
import java.util.LinkedList;

import java.util.*;

public class Board {

		  private int winner;

		  private int point;
		  private boolean isGameOver;
		  int[] boardcopy;
		  int[] boardone;
		  int[][] boardtwo;
		  public int size = 15;
          LinkedList<Move> winRateList;
          int[] winRate = new int[size*size];
          
          

		public Board(){
		 
		  int row , column , i;
		  boardone = new int[size*size];
		  boardcopy = new int[size*size];
		  boardtwo = new int[size][size];
		  
		  for(int t = 0; t < size*size;t++)
		  {
			  winRate[t] = 0;
		  }
		 
		  for(i=0;i<=size; i++){
			  boardone[i] =0;
		  }
		  boardcopy = boardone.clone();
		  
		  for(row=0; row< size; row++)
			for(column =0; column<size; column++)
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
        public boolean checkPlay (int move)
        {   

        	if(boardone[move] == 0)
        	return true;
            else 
            return false;
        }
        
        public void updateWinRate(LinkedList<Move> list, int x, boolean y)
        {
        	if (y == true) // win 
        	{   Iterator<Move> itr = list.iterator();
        		Move thismove = null;
				while(itr.hasNext())
        			thismove = (Move) itr.next();
				   thismove.wintime++;
					
				
        	}
        	else if (y == false)
        	{
        		Iterator<Move> itr = list.iterator();
        		Move thismove = null;
				while(itr.hasNext())
        			thismove = (Move) itr.next();
				   thismove.wintime--;
					
        	}
        	
        }
        
        
        public void clearAfterX (LinkedList<Move> list, int x)
        {
        	Iterator<Move> itr = list.iterator();
        	Move thismove = new Move(x);
        	Move previousmove = null;
        	for(; itr.next() != thismove; itr.hasNext())
        		previousmove = (Move) itr.next();
		        previousmove.numPoints = 0;
        }
        
		public boolean isWin(int side){
		    boolean isOver = false;
		    if(side != -1){
		        if(checkHorizontalWinner(1)){
		            isOver=true;
		        }
		        if(!isOver && checkVerticalWinner(1)){
		            isOver=true;
		        }
		        if(!isOver && checkMainDiagonalWinner(1)){
		            isOver=true;
		        }
		        if(!isOver && checkSecondaryDiagonalWinner(1)){
		            isOver=true;
		        }
		        if(isOver){
		            winner=1;
		            isGameOver=true;
		            return true;
		        }
		    }
		    if(side != 1){
		        if(checkHorizontalWinner(-1)){
		            isOver=true;
		        }
		        if(!isOver && checkVerticalWinner(-1)){
		            isOver=true;
		        }
		        if(!isOver && checkMainDiagonalWinner(-1)){
		            isOver=true;
		        }
		        if(!isOver && checkSecondaryDiagonalWinner(-1)){
		            isOver=true;
		        }
		        if(isOver){
		            winner= -1 ;
		            isGameOver=true;
		            return true;
		        }
		    }
		    return false;
		}



		private boolean checkHorizontalWinner(int side){
		    int row , column;
		    int length=0;

		    for(row=0 ; row<size ; row++){
		        for(column=0 ; column<size ; column++){
		            if(boardtwo[row][column] == side){
		                length++;
		            }
		            else{
		                length=0;
		            }
		            if(length == 5){
		                return true;
		            }
		        }
		        length=0;
		    }
		    
		    return false;
		}



		private boolean checkVerticalWinner(int side){
		    int row , column;
		    int length=0;

		    for(column=0 ; column<size ; column++){
		        for(row=0 ; row<size ; row++){
		            if(boardtwo[row][column] == side){
		                length++;
		            }
		            else{
		                length=0;
		            }
		            if(length == 5){
		                return true;
		            }
		        }
		        length=0;
		    }
		    
		    return false;
		}

		private boolean checkMainDiagonalWinner(int side){
		    int row , column;
		    int length=0;
		    int aux;

		    //diagonals above , parallel to and including the main board matrice diagonal
		    for(aux=0 ; aux<size ; aux++){
		        for(column=size-1-aux , row=0 ; column<size ; column++ , row++){
		            if(boardtwo[row][column] == side){
		                length++;
		            }
		            else{
		                length=0;
		            }
		            if(length == 5){
		                return true;
		            }
		        }
		        length=0;
		    }

		    //diagonals below and parallel to the main board matrice diagonal
		    for(aux=0 ; aux<size-1 ; aux++){
		        for(row=size-1-aux , column=0 ; row<=size-1 ; row++ , column++){
		            if(boardtwo[row][column] == side){
		                length++;
		            }
		            else{
		                length=0;
		            }
		            if(length == 5){
		                return true;
		            }
		        }
		        length=0;
		    }

		    return false;
		}

		private boolean checkSecondaryDiagonalWinner(int side){
		    int row , column;
		    int length=0;
		    int aux;

		    //diagonals above , parallel to and including the secondary board matrice diagonal
		    for(aux=0 ; aux<size ; aux++){
		        for(row=aux , column=0 ; row>=0 ; row-- , column++){
		            if(boardtwo[row][column] == side){
		                length++;
		            }
		            else{
		                length=0;
		            }
		            if(length == 5){
		                return true;
		            }
		        }
		        length=0;
		    }

		    //diagonals below and parallel to the secondary board matrice diagonal
		    for(aux=0 ; aux<size-1 ; aux++){
		        for(column=size-1-aux , row=size-1 ; column<=size-1 ; column++ , row--){
		            if(boardtwo[row][column] == side){
		                length++;
		            }
		            else{
		                length=0;
		            }
		            if(length == 5){
		                return true;
		            }
		        }
		        length=0;
		    }
		    
		    return false;
		}

		   public int getnextSide(int side)
		   {
			  return side == 1 ? -1:1;
		   }


		   public boolean whiteWin(){
			  if(winner == -1){
				  return true;
			  }
			  return false;
		   }

		 

		  

		    public boolean blackWin(){
		        if(winner == 1){
		            return true;
		        }
		        return false;
		    }
		    
		    public boolean isfull(){
		    	for(int i = 0; i < size*size ;i++){
		    			if(boardone[i] == 0){
		    				return false;
		    			}
		    	}
		    	return true;
		    	}
		    public void updatetotalrate(LinkedList<Move> winRateList)
		    {
		    	for(int i = 0;i < winRateList.size();i++){
		    		int t = winRateList.get(i).numPoints;
		    		winRate[t]++;
		    	}
		    }
		    
		    public void clearboard()
		    {
		    	boardone = boardcopy.clone();
		    	winRateList.clear();
		    	for(int a = 0; a < size;a++){
		    		for(int j = 0; j < size;j++){
		    			boardtwo[a][j] = boardone[a + j * size];
		    		}
		    	}
		    	isGameOver = false;
		    }
		    	
		    }

		  



