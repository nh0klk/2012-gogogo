package gomoku;

import java.util.ArrayList;
import java.util.Collections;

public class ChessBoardCheckArea {
	private int[] chessBoardStatus;
	private int rightEdge=-1;
	private int leftEdge=ChessBoardConstant.ChessBoardWidth+1;
	private int topEdge= -1;
	private int radius = 3;
	private int bottomEdge = ChessBoardConstant.ChessBoardWidth+1;
	
	public ChessBoardCheckArea(int[] chessBoardStatus ,int areaRadius){
		radius = areaRadius;
		init(chessBoardStatus);
	}	
	
	private void init(int[] chessBoardStatus){
		ArrayList<Integer> rowList = new ArrayList<Integer>();
		ArrayList<Integer> columnList = new ArrayList<Integer>();
		for(int i= 0; i<ChessBoardConstant.ChessBoardWidth*ChessBoardConstant.ChessBoardWidth;i++){
			if(chessBoardStatus[i]==ChessBoardConstant.Blank)
				continue;
			rowList.add(ChessBoardHelper.getRowIndex(i));
			columnList.add(ChessBoardHelper.getColumnIndex(i));
		}
		bottomEdge = Collections.max(rowList);
		topEdge = Collections.min(rowList);
		leftEdge = Collections.min(columnList);
		rightEdge = Collections.max(columnList);
	}
	
	public ChessBoardCheckArea(int[] chessBoardStatus){
		init(chessBoardStatus);
	}
	
	public ChessBoardCheckArea(int leftEdge, int rightEdge, int topEdge, int bottomEdge){
		this.rightEdge = rightEdge;
		this.bottomEdge = bottomEdge;
		this.leftEdge = leftEdge;
		this.topEdge = topEdge;
	}
	
	public int getRightEdge(){
		return Math.min(rightEdge+radius, ChessBoardConstant.ChessBoardWidth-1);
	}
	public int getLeftEdge(){
		return Math.max(leftEdge-radius, 0);
	}
	public int getTopEdge(){
		return Math.max(topEdge-radius, 0);
	}
	public int getBottomEdge(){
		return Math.min(bottomEdge+radius, ChessBoardConstant.ChessBoardWidth-1);
	}
	
	public void UpdateCheckArea(int index){
		int row = ChessBoardHelper.getRowIndex(index);
		int column = ChessBoardHelper.getColumnIndex(index);
		rightEdge = Math.max(rightEdge,column);
		leftEdge = Math.min(leftEdge,column);
		topEdge = Math.min(topEdge,row);
		bottomEdge = Math.max(bottomEdge,row);
	}
	
	public ChessBoardCheckArea ExpandCheckArea(int index){
		int row = ChessBoardHelper.getRowIndex(index);
		int column = ChessBoardHelper.getColumnIndex(index);
		return new ChessBoardCheckArea(Math.min(leftEdge,column),
		                                                      Math.max(rightEdge,column),
		                                                      Math.min(topEdge,row),
		                                                      Math.max(bottomEdge,row));
	}
}
