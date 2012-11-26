package monte;

import gomoku.Minimax;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Main {


public static void main(String[] args) throws Exception{
	  MovePropose movepropose = new MovePropose();
	  
	//  movepropose.firstmove();
	  int[] pieces = new int[15 * 15 + 1];
		String s = "112 64 97 ";
		String[] k = s.split(" ");
		int color = -1;
		for (String t : k) {
			int i = Integer.parseInt(t);
			pieces[i] = color;
			color = -color;
		}
		Minimax mn = new Minimax(pieces);
		int m=mn.getBestMove(1, 2);
		System.out.println(m);
	  /* 
  //  System.out.println(movepropose.firstmove());
	  int[] p = new int[15 * 15];
	  int[]pieces = new int[15 * 15 + 1];
	  pieces[0] = 0;
  	for(int j =  0; j < 15 * 15; j++)
  	{
  		pieces[j + 1] = 0; //a.boardone[j];
  	}
	Minimax mn = new Minimax(pieces);
	int m = mn.getBestMove(1, 4);
	System.out.println("hi!"+ m);
	  
	 
	  
//	  movepropose.firstmove();
	  
	 
	  p[movepropose.firstmove()] = 1;
	  
//	  movepropose.playmove(p, -1);
	  for(int i = 0;i < 20;i++){
		  BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		  String a = in.readLine();
		  int b = Integer.parseInt(a);
	  p[b] = -1;
	  p[movepropose.playmove(p, 1)] = 1;
	  }
	  */ 
	  
}

}