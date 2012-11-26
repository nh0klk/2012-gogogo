package src;

// A Move consists of a numPoints and a side. (color and Vertex)
// Move also has wintime
public class Move {
   public int row;
   public int column;
   public int side;
   public int numPoints ;
   public int wintime = 0;
   
   
   Move(int numPoints){this.numPoints =numPoints; this.wintime =0;}
   
   Move(int row, int column, int side){
	   this.row = row;
	   this.column = column;
	   this.side = side;
   }
   
   
   Move(int numPoints, int side){
	   this.numPoints = numPoints;
	   this.side = side;
   }
}
