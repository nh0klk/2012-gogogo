package src;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Main {
 

public static void main(String[] args) throws Exception{
	  MovePropose movepropose = new MovePropose();

  //  System.out.println(movepropose.firstmove());
	  int[] p = new int[15 * 15];
	  p[movepropose.firstmove()] = 1;
	  
//	  movepropose.playmove(p, -1);
	  for(int i = 0;i < 20;i++){
		  BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		  String a = in.readLine();
		  int b = Integer.parseInt(a);
	  p[b] = -1;
	  p[movepropose.playmove(p, 1)] = 1;
	  }
}
}