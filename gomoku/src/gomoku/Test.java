package gomoku;

public class Test {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] pieces = new int[15 * 15 + 1];
		String s = "112 64 97 127 110 0 113 2 109";
		String[] k = s.split(" ");
		int color = -1;
		for (String t : k) {
			int i = Integer.parseInt(t);
			pieces[i] = color;
			color = -color;
		}
		Minimax mn = new Minimax(pieces);
		int m=mn.getBestMove(-1, 3);
		System.out.println(m);
	}
	
}
