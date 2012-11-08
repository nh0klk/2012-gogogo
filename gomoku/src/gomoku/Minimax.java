package gomoku;

public class Minimax {
	private int[] pieces = new int[Game.n * Game.n + 1];
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
	public int bestscore;
	public int[] bestmove=new int[6];
	public int depth;
	public int color;
	public Minimax() {
		pieces = (int[]) Game.pieces.clone();
	}
	public Minimax(int[] p) {
		pieces = (int[]) p.clone();
	}

	public int getScore(int color) {
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

		pieceVisits = new int[Game.n * Game.n + 1];
		int index = 0;
		while (index < Game.n * Game.n) {
			int pc = pieces[index];
			if (pc == 0) {
				index++;
				continue;
			}
			int step = checkFive(index);
			index += step;
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
		int turn = Game.getTurn(color);

		if (Count4_2[1 - turn] > 0 || Count4_1[1 - turn] > 0)
			return 970000 * color;
		if (Count4_2[turn] > 0)
			return -950000 * color;
		if (Count4_1[turn] > 1)
			return -900000;
		if (Count4_1[turn] + Count3_2[turn] > 1)
			return -930000 * color;
		if (Count3_2[1 - turn] > 0)
			return 850000 * color;
		if (Count3_2[turn] + Count3_1_2[turn] > 1)
			return -800000;
		result = (Count4_1[0] * (2 + color) - Count4_1[1] * (2 - color))
				* 10000
				+ (Count4_2[0] * (2 + color) - Count4_2[1] * (2 - color))
				* 40000
				+ (Count3_1[0] * (2 + color) - Count3_1[1] * (2 - color))
				* 1000
				+ (Count3_1_2[0] * (2 + color) - Count3_1_2[1] * (2 - color))
				* 2000
				+ (Count3_2[0] * (2 + color) - Count3_2[1] * (2 - color))
				* 4000
				+ (Count2_1[0] * (2 + color) - Count2_1[1] * (2 - color)) * 100
				+ (Count2_2[0] * (2 + color) - Count2_2[1] * (2 - color)) * 400
				+ (Count1_1[0] * (2 + color) - Count1_1[1] * (2 - color)) * 10
				+ (Count1_2[0] * (2 + color) - Count1_2[1] * (2 - color)) * 40;
		return result;

	}
	private int checkFive(int index) {
		int step = 1;
		int i = Game.getI(index);
		int j = Game.getJ(index);
		int pc = pieces[index];
		checkByDir(pc, index, i, j, 'H');
		checkByDir(pc, index, i, j, 'V');
		checkByDir(pc, index, i, j, 'R');
		checkByDir(pc, index, i, j, 'L');
		return step;
	}
	private void checkByDir(int color, int index, int i, int j, char dir) {
		int pv = pieceVisits[index];
		if (Game.isTested(pv, dir) == 0) {
			int c = 0;
			setTested(index, dir);
			int tmpIndex;
			tmpIndex = index;
			while (pieces[tmpIndex] == color) {
				setTested(tmpIndex, dir);
				c++;
				tmpIndex = Game.getNewIndex(i, j, dir, c);
			}
			test(i, j, dir, c, color);
		}
	}
	private void test(int i, int j, char dir, int c, int color) {
		int head = getPiece(i, j, dir, -1);
		int head1 = getPiece(i, j, dir, -2);
		int head2 = getPiece(i, j, dir, -3);
		int head3 = getPiece(i, j, dir, -4);
		int rear = getPiece(i, j, dir, c);
		int rear1 = getPiece(i, j, dir, c + 1);
		int rear2 = getPiece(i, j, dir, c + 2);
		int rear3 = getPiece(i, j, dir, c + 3);
		int rear4 = getPiece(i, j, dir, c + 4);
		int turn = Game.getTurn(color);
		if (c >= 6)
			Count6[turn]++;
		if (c == 5)
			Count5[turn]++;
		if (c == 4) {
			if (empty(head) && empty(rear))
				Count4_2[turn]++;
			if (xor(empty(head), empty(rear)))
				Count4_1[turn]++;
		}
		if (c == 3) {
			if (empty(head) && empty(rear))
				Count3_2[turn]++;
			if (xor(empty(head), empty(rear)))
				Count3_1[turn]++;
			if (empty(rear) && rear1 == color && rear2 != color)
				Count4_1[turn]++;

		}
		if (c == 2) {
			if (empty(head) && empty(rear) && rear1 == color && empty(rear2))
				Count3_1_2[turn]++;
			if (empty(rear) && rear1 == color && rear2 == color
					&& rear3 != color && (empty(head) || empty(rear3)))
				Count4_1[turn]++;
			if (empty(rear) && rear1 == color && rear2 != color
					&& xor(empty(head), empty(rear2)))
				Count3_1[turn]++;
			if (empty(rear) && empty(rear1) && rear2 == color)
				Count3_1[turn]++;
			if (avail(head, color) && avail(head1, color)
					&& avail(head2, color) && avail(rear, color)
					&& avail(rear1, color) && avail(rear2, color))
				Count2_2[turn]++;
			else if (!((!avail(head, color) || !avail(head1, color) || avail(
					head2, color)) && (!avail(rear, color)
					&& !avail(rear1, color) && !avail(rear2, color))))
				Count2_1[turn]++;
		}
		if (c == 1) {
			if (empty(head) && empty(rear) && rear1 == color && rear2 == color
					&& empty(rear3))
				Count3_2[turn]++;
			if (empty(rear) && rear1 == color && rear2 == color
					&& rear3 == color && rear4 != color)
				Count4_1[turn]++;
			if (empty(rear) && empty(rear1) && rear2 == color && rear3 == color)
				Count3_1[turn]++;
			if (empty(rear) && rear1 == color && empty(rear2) && rear3 == color)
				Count3_1[turn]++;
			if (empty(rear) && rear1 == color) {
				if (xor(!(avail(head, color) && avail(head1, color)),
						!(avail(rear2, color) && avail(rear3, color))))
					Count2_1[turn]++;
			}
			if (avail(rear, color) && avail(rear1, color)
					&& avail(rear2, color) && avail(rear3, color)
					&& avail(head, color) && avail(head1, color)
					&& avail(head2, color) && avail(head3, color))
				Count1_2[turn]++;
			else if (xor(
					!(avail(rear, color) && avail(rear1, color)
							&& avail(rear2, color) && avail(rear3, color)),
					!(avail(head, color) && avail(head1, color)
							&& avail(head2, color) && avail(head3, color))))
				Count1_1[turn]++;

		}
	}
	private void setTested(int index, char dir) {
		pieceVisits[index] |= Game.getDirCode(dir);
	}
	private int getPiece(int i, int j, char dir, int step) {
		switch (dir) {
			case 'H' :
				return pieces[Game.getIndex(i, j + step)];
			case 'V' :
				return pieces[Game.getIndex(i + step, j)];
			case 'R' :
				return pieces[Game.getIndex(i + step, j + step)];
			case 'L' :
				return pieces[Game.getIndex(i + step, j - step)];
		}
		return 2;
	}

	private boolean xor(boolean a, boolean b) {
		return (a && !b) || (b && !a);
	}
	private boolean avail(int pc, int color) {
		return pc != -color && pc != 2;
	}
	private boolean empty(int pc) {
		return pc == 0;
	}
	public int getBestMove(int color, int d) {
		if (color == -1) {
			max(-1000001, 1000001, d);
			return bestmove[d-1];
		} else {
			min(-1000001, 1000001, d);
			return bestmove[d-1];
		}
	}
	private int max(int alpha, int beta, int d) {
		if (d == 0)
			return getScore(1);
		int a = alpha;
		int b = beta;
		for (int i = 0; i < Game.n * Game.n; i++) {
			if (a >= b)
				return a;
			if (pieces[i]!=0)
				continue;
			pieces[i] = -1;
			if(i==114)
				i+=0;
			int a1 = min(a, b, d - 1);
			if (a1==970000)
				a1+=0;
			if (a1 > a) {
				a = a1;
				bestmove[d-1] = i;
			}
			pieces[i] = 0;
		}
		return a;
	}
	private int min(int alpha, int beta, int d) {
		if (d == 0)
			return getScore(-1);
		int a = alpha;
		int b = beta;
		for (int i = 0; i < Game.n * Game.n; i++) {
			if (a >= b)
				return b;
			if (pieces[i]!=0)
				continue;
			pieces[i] = 1;
			if (i==110)
				i+=0;
			int b1 = max(a, b, d - 1);
			if (b1 < b) {
				b = b1;
				
				bestmove[d-1] = i;
			}
			pieces[i] = 0;
		}
		return b;
	}

}
