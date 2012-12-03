package gomoku;

public class Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	
	public static void main(String[] args) throws Exception {
/*		// TODO Auto-generated method stub
		int[] pieces = new int[15 * 15 + 1];
		//String s = "112 64 97 127 110 0 113 2 109";
		String s = "112";
		String[] k = s.split(" ");
		int color = -1;
		for (String t : k) {
			int i = Integer.parseInt(t);
			pieces[i] = color;
			color = -color;
		}
		Minimax mn = new Minimax(pieces);
		int m=mn.getBestMove(-1, 3);
		System.out.println(m);*/
		ChessBoardTest chessBoardTest = new ChessBoardTest();
	//	chessBoardTest.TestMonteMiniMax(1000,30,1);
	//	chessBoardTest.TestMonteMiniMax(5000,30,1);
		chessBoardTest.TestMonteMiniMax(10000,10,1);
	//	chessBoardTest.TestMonteMiniMax(5000,10,3);
	//	chessBoardTest.TestMonteMiniMax(10000,10,3);
	}
	
	
	
}
