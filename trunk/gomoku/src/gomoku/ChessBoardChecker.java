package gomoku;

public class ChessBoardChecker {
	
	private int[] chessBoardStatus;
	private int[] pieceVisits;
	public int[] Count6 = new int[2];
	public int[] Count5 = new int[2];
	public int[] Count4_1 = new int[2];
	public int[] Count4_2 = new int[2];
	public int[] Count3_1 = new int[2];
	public int[] Count3_1_2 = new int[2];
	public int[] Count3_2 = new int[2];
	public int[] Count2_1 = new int[2];
	public int[] Count2_2 = new int[2];
	public int[] Count1_1 = new int[2];
	public int[] Count1_2 = new int[2];
	
	
	public ChessBoardChecker(int[] chessBoardStatus){
		this.chessBoardStatus = chessBoardStatus;
	}
	
	public ChessBoardChecker(){
	}
	
	//估值函数
	public int evaluateValue(int player, int[] chessBoardStatus) {
		this.chessBoardStatus = chessBoardStatus.clone();
		Count6 = new int[2];
		Count5 = new int[2];
		Count4_1 = new int[2];
		Count4_2 = new int[2];
		Count3_1 = new int[2];
		Count3_1_2 = new int[2];
		Count3_2 = new int[2];
		Count2_1 = new int[2];
		Count2_2 = new int[2];
		Count1_1 = new int[2];
		Count1_2 = new int[2];

		pieceVisits = new int[ChessBoardConstant.ChessBoardWidth* ChessBoardConstant.ChessBoardWidth];
		
		for (int i=0 ;i < ChessBoardConstant.ChessBoardWidth* ChessBoardConstant.ChessBoardWidth;i++) {;
			if (chessBoardStatus[i] == ChessBoardConstant.Blank) {
				continue;
			}
			checkChessInOneRow(i);
		}
		
		if (Count6[0] > 0)
			return -1000000;
		if (Count6[1] > 0)
			return -1000000;
		if (Count5[0] > 0)
			return 1000000;
		if (Count5[1] > 0)
			return -1000000;
		if (Count4_1[0] + Count4_2[0] > 1)
			return -1000000;
		if (Count3_2[0] + Count3_1_2[0] > 1)
			return -1000000;
		int result = 0;
		int turn = Game.getTurn(player);

		if (Count4_2[1 - turn] > 0 || Count4_1[1 - turn] > 0)
			return 970000 * player;
		if (Count4_2[turn] > 0)
			return -950000 * player;
		if (Count4_1[turn] > 1)
			return -900000;
		if (Count4_1[turn] + Count3_2[turn] > 1)
			return -930000 * player;
		if (Count3_2[1 - turn] > 0)
			return 850000 * player;
		if (Count3_2[turn] + Count3_1_2[turn] > 1)
			return -800000;
		result = (Count4_1[0] * (2 + player) - Count4_1[1] * (2 - player))
				* 10000
				+ (Count4_2[0] * (2 + player) - Count4_2[1] * (2 - player))
				* 40000
				+ (Count3_1[0] * (2 + player) - Count3_1[1] * (2 - player))
				* 1000
				+ (Count3_1_2[0] * (2 + player) - Count3_1_2[1] * (2 - player))
				* 2000
				+ (Count3_2[0] * (2 + player) - Count3_2[1] * (2 - player))
				* 4000
				+ (Count2_1[0] * (2 + player) - Count2_1[1] * (2 - player)) * 100
				+ (Count2_2[0] * (2 + player) - Count2_2[1] * (2 - player)) * 400
				+ (Count1_1[0] * (2 + player) - Count1_1[1] * (2 - player)) * 10
				+ (Count1_2[0] * (2 + player) - Count1_2[1] * (2 - player)) * 40;
		return result;

	}
	
	//检查给定子周围的情况（五连，四连，三连，二连）
	private void checkChessInOneRow(int index) {
		//检查上下左右和对角线
		checkChessByDirection(index,'H');//垂直
		checkChessByDirection(index,'V');//水平
		checkChessByDirection(index,'R');//左上-右下
		checkChessByDirection(index,'L');//右上-左下
	}
	
	private void checkChessByDirection(int index, char dir) {
		int i = ChessBoardHelper.GetRowIndex(index);
		int j = ChessBoardHelper.GetColumnIndex(index);
		int player = chessBoardStatus[index];
		int pv = pieceVisits[index];
		if (isTested(pv, dir) == 0) {
			
			//逐个按方向检察是否是同一颜色子，遇到边界，对手子，空白时停止检查
			int chessInOneRowCount = 0;
			setTested(index, dir);
			int tmpIndex;
			tmpIndex = index;
			
			//获取当前方向上最大有几个子相连
			while (tmpIndex!=ChessBoardConstant.BoarderIndex
					&&chessBoardStatus[tmpIndex] == player) {
				setTested(tmpIndex, dir);
				chessInOneRowCount++;
				tmpIndex = getChessIndexByDirection(i, j, dir, chessInOneRowCount);
			}
			
			//查看具体有几个子在一行上
			checkDetail(i, j, dir, chessInOneRowCount, player);
		}
	}
	
	//获取dir方向上，距离distance个空格的子
	public int getChessIndexByDirection(int i, int j, char direction, int distance) {
		switch (direction) {
			case 'H' :
				return ChessBoardHelper.GetListIndex(i, j + distance);
			case 'V' :
				return ChessBoardHelper.GetListIndex(i + distance, j);
			case 'R' :
				return ChessBoardHelper.GetListIndex(i + distance, j + distance);
			case 'L' :
				return ChessBoardHelper.GetListIndex(i + distance, j - distance);
		}
		return ChessBoardConstant.BoarderIndex;

	}
	
	private void checkDetail(int i, int j, char dir, int c, int player) {
		int head = getChess(i, j, dir, -1);
		int head1 = getChess(i, j, dir, -2);
		int head2 = getChess(i, j, dir, -3);
		int head3 = getChess(i, j, dir, -4);
		int rear = getChess(i, j, dir, c);
		int rear1 = getChess(i, j, dir, c + 1);
		int rear2 = getChess(i, j, dir, c + 2);
		int rear3 = getChess(i, j, dir, c + 3);
		int rear4 = getChess(i, j, dir, c + 4);
		int turn = Game.getTurn(player);
		if (c >= 6)
			Count6[turn]++;
		if (c == 5)
			Count5[turn]++;
		if (c == 4) {
			if (blankPlayer(head) && blankPlayer(rear))
				Count4_2[turn]++;
			if (xor(blankPlayer(head), blankPlayer(rear)))
				Count4_1[turn]++;
		}
		if (c == 3) {
			if (blankPlayer(head) && blankPlayer(rear))
				Count3_2[turn]++;
			if (xor(blankPlayer(head), blankPlayer(rear)))
				Count3_1[turn]++;
			if (blankPlayer(rear) && rear1 == player && rear2 != player)
				Count4_1[turn]++;

		}
		if (c == 2) {
			if (blankPlayer(head) && blankPlayer(rear) && rear1 == player && blankPlayer(rear2))
				Count3_1_2[turn]++;
			if (blankPlayer(rear) && rear1 == player && rear2 == player
					&& rear3 != player && (blankPlayer(head) || blankPlayer(rear3)))
				Count4_1[turn]++;
			if (blankPlayer(rear) && rear1 == player && rear2 != player
					&& xor(blankPlayer(head), blankPlayer(rear2)))
				Count3_1[turn]++;
			if (blankPlayer(rear) && blankPlayer(rear1) && rear2 == player)
				Count3_1[turn]++;
			if (avail(head, player) && avail(head1, player)
					&& avail(head2, player) && avail(rear, player)
					&& avail(rear1, player) && avail(rear2, player))
				Count2_2[turn]++;
			else if (!((!avail(head, player) || !avail(head1, player) || avail(
					head2, player)) && (!avail(rear, player)
					&& !avail(rear1, player) && !avail(rear2, player))))
				Count2_1[turn]++;
		}
		if (c == 1) {
			if (blankPlayer(head) && blankPlayer(rear) && rear1 == player && rear2 == player
					&& blankPlayer(rear3))
				Count3_2[turn]++;
			if (blankPlayer(rear) && rear1 == player && rear2 == player
					&& rear3 == player && rear4 != player)
				Count4_1[turn]++;
			if (blankPlayer(rear) && blankPlayer(rear1) && rear2 == player && rear3 == player)
				Count3_1[turn]++;
			if (blankPlayer(rear) && rear1 == player && blankPlayer(rear2) && rear3 == player)
				Count3_1[turn]++;
			if (blankPlayer(rear) && rear1 == player) {
				if (xor(!(avail(head, player) && avail(head1, player)),
						!(avail(rear2, player) && avail(rear3, player))))
					Count2_1[turn]++;
			}
			if (avail(rear, player) && avail(rear1, player)
					&& avail(rear2, player) && avail(rear3, player)
					&& avail(head, player) && avail(head1, player)
					&& avail(head2, player) && avail(head3, player))
				Count1_2[turn]++;
			else if (xor(
					!(avail(rear, player) && avail(rear1, player)
							&& avail(rear2, player) && avail(rear3, player)),
					!(avail(head, player) && avail(head1, player)
							&& avail(head2, player) && avail(head3, player))))
				Count1_1[turn]++;

		}
	}
	
	private void setTested(int index, char dir) {
		pieceVisits[index] |= Game.getDirCode(dir);
	}
	
	private int getChess(int i, int j, char dir, int step) {
		
		int index = getChessIndexByDirection(i, j, dir, step);
		if(index==ChessBoardConstant.BoarderIndex)
			return 2;
		else
			return chessBoardStatus[index];
	}

	private boolean xor(boolean a, boolean b) {
		return (a && !b) || (b && !a);
	}
	private boolean avail(int pc, int color) {
		return pc != -color && pc != 2;
	}
	
	private boolean blankPlayer(int pc) {
		return pc == ChessBoardConstant.Blank;
	}
	
	public int isTested(int pv, char c) {
		int k = getDirCode(c);
		return pv & k;
	}
	public int getDirCode(char c) {
		switch (c) {
			case 'H' :
				return 8;
			case 'V' :
				return 4;
			case 'R' :
				return 2;
			case 'L' :
				return 1;
		}
		return 0;
	}
	
	
	//检查是否有人赢了，根据传入的玩家（黑，白）和棋局
	public boolean isWin(int player, int[] chessBoardStatus){
		
        if(checkHorizontalWinner(player,chessBoardStatus)){
            return true;
        }
        
        if(checkVerticalWinner(player,chessBoardStatus)){
        	return true;	        
        }
        
        if(checkMainDiagonalWinner(player,chessBoardStatus)){
        	return true;
        }
        
        if(checkSecondaryDiagonalWinner(player,chessBoardStatus)){
        	return true;
        }
        return false;
	}



	private boolean checkHorizontalWinner(int player,int[] chessBoardStatus){
	    int length=0;

	    for(int row=0 ; row<ChessBoardConstant.ChessBoardWidth ; row++){
	        for(int column=0 ; column<ChessBoardConstant.ChessBoardWidth ; column++){
	            if(chessBoardStatus[ChessBoardHelper.GetListIndex(row,column)] == player){
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



	private boolean checkVerticalWinner(int player,int[] chessBoardStatus){
	    int length=0;

	    for(int column=0 ; column<ChessBoardConstant.ChessBoardWidth ; column++){
	        for(int row=0 ; row<ChessBoardConstant.ChessBoardWidth ; row++){
	            if(chessBoardStatus[ChessBoardHelper.GetListIndex(row,column)] == player){
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

	private boolean checkMainDiagonalWinner(int player,int[] chessBoardStatus){
	    int length=0;

	    //diagonals above , parallel to and including the main board matrice diagonal
	    for(int aux=0 ; aux<ChessBoardConstant.ChessBoardWidth ; aux++){
	        for(int column=ChessBoardConstant.ChessBoardWidth-1-aux , row=0 ; 
	        		column<ChessBoardConstant.ChessBoardWidth ; 
	        		column++ , row++){
	            if(chessBoardStatus[ChessBoardHelper.GetListIndex(row,column)] == player){
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
	    for(int aux=0 ; aux<ChessBoardConstant.ChessBoardWidth-1 ; aux++){
	        for(int row=ChessBoardConstant.ChessBoardWidth-1-aux , column=0 ; 
	        		row<=ChessBoardConstant.ChessBoardWidth-1 ; row++ , column++){
	            if(chessBoardStatus[ChessBoardHelper.GetListIndex(row,column)] == player){
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

	private boolean checkSecondaryDiagonalWinner(int player,int[] chessBoardStatus){
	    int length=0;

	    //diagonals above , parallel to and including the secondary board matrice diagonal
	    for(int aux=0 ; aux<ChessBoardConstant.ChessBoardWidth ; aux++){
	        for(int row=aux , column=0 ; row>=0 ; row-- , column++){
	            if(chessBoardStatus[ChessBoardHelper.GetListIndex(row,column)]== player){
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
	    for(int aux=0 ; aux<ChessBoardConstant.ChessBoardWidth-1 ; aux++){
	        for(int column=ChessBoardConstant.ChessBoardWidth-1-aux , row=ChessBoardConstant.ChessBoardWidth-1 ;
	        		column<=ChessBoardConstant.ChessBoardWidth-1 ; column++ , row--){
	            if(chessBoardStatus[ChessBoardHelper.GetListIndex(row,column)] == player){
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
}
