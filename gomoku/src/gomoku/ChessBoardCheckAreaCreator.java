package gomoku;

import java.util.ArrayList;
import java.util.Collections;

public class ChessBoardCheckAreaCreator {
	private int[] chessBoardStatus;
	private int rightEdge=-1;
	private int leftEdge=ChessBoardConstant.ChessBoardWidth+1;
	private int topEdge= -1;
	private int bottomEdge = ChessBoardConstant.ChessBoardWidth+1;
	
	public ChessBoardCheckAreaCreator(int[] chessBoardStatus){
		ArrayList<Integer> rowList = new ArrayList<Integer>();
		ArrayList<Integer> columnList = new ArrayList<Integer>();
		for(int i= 0; i<ChessBoardConstant.ChessBoardWidth*ChessBoardConstant.ChessBoardWidth;i++){
			rowList.add(ChessBoardHelper.getRowIndex(i));
			columnList.add(ChessBoardHelper.getColumnIndex(i));
		}
		rightEdge = Collections.max(rowList);
		bottomEdge = Collections.max(columnList);
		leftEdge = Collections.min(rowList);
		topEdge = Collections.min(columnList);
	}	
	
	public int getRightEdge(){
		return Math.min(rightEdge+3, ChessBoardConstant.ChessBoardWidth);
	}
	public int getLeftEdge(){
		return Math.max(leftEdge-3, 0);
	}
	public int getTopEdge(){
		return Math.max(topEdge-3, 0);
	}
	public int getBottomEdge(){
		return Math.max(bottomEdge+3, ChessBoardConstant.ChessBoardWidth);
	}
}
