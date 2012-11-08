package gomoku;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.border.BevelBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

public class MainWindow {

	private JFrame frame;

	/**
	 * Launch the application.
	 */

	private final CheckerBoard board = new CheckerBoard();
	private final JLabel label = new JLabel("Black");
	private final JButton btnReplay = new JButton("Replay");
	private final JButton btnMenu = new JButton("Menu");
	private final JButton btnStats = new JButton("Stats");
	private final JCheckBox isBoardDisplayed = new JCheckBox("Show board");

	int top = 100;
	int left = 100;
	int width = 700;
	int height = 750;
	int margin = 20;

	private final JPanel panel = new JPanel();
	private final JTextField textField = new JTextField();;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void newGame(int player1, int player2, long runs) {
		Game.initGame(player1, player2, runs);
		clean();
	}
	private void newGame(int player1, int player2) {
		Game.initGame(player1, player2);
		clean();
	}
	private void newGame() {
		Game.initGame();
		clean();
	}
	private void clean() {
		board.cleanBoard();
		board.getGraphics().clearRect(0, 0, width - margin * 2,
				width - margin * 2);
		board.repaint();
		label.setText("Black");
	}
	private void initialize() {

		frame = new JFrame();
		frame.setTitle("Gomoku");
		frame.setResizable(false);
		frame.setBounds(left, top, width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		btnReplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newGame();
			}
		});
		btnReplay.setBounds(width / 4 - 117 / 2, width - 2, 117, 29);
		frame.getContentPane().add(btnReplay);
		btnStats.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showStats();
			}
		});
		btnStats.setBounds(width / 4 * 2 - 117 / 2, width - 2, 117, 29);
		frame.getContentPane().add(btnStats);
		btnMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showMenu();
				hideBoard();
			}
		});
		btnMenu.setBounds(width / 4 * 3 - 117 / 2, width - 2, 117, 29);
		frame.getContentPane().add(btnMenu);

		label.setBounds(30, width - 2, 150, 16);
		frame.getContentPane().add(label);

		panel.setBounds(0, 0, width, height);
		frame.getContentPane().add(panel);
		panel.setLayout(new FormLayout(new ColumnSpec[]{
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("145px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("151px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("131px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("19px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("109px:grow"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("111px"),}, new RowSpec[]{
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("29px"),
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("29px"),
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,}));

		textField.setText("1000");
		panel.add(textField, "6, 26, fill, default");
		textField.setColumns(10);

		JButton btnMonteVsWhite = new JButton("MonteVsWhite");
		btnMonteVsWhite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newGame(2, 0);
				hideMenu();
				showBoard();
			}
		});
		panel.add(btnMonteVsWhite, "6, 16, left, top");

		JButton btnBlackVsMonte = new JButton("BlackVsMonte");
		btnBlackVsMonte.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newGame(0, 2);
				hideMenu();
				showBoard();
			}
		});
		panel.add(btnBlackVsMonte, "6, 14, left, top");

		JButton btnBlackVsMinimax = new JButton("BlackVsMiniMax");
		btnBlackVsMinimax.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newGame(0, 1);
				hideMenu();
				showBoard();
			}
		});
		panel.add(btnBlackVsMinimax, "6, 10, left, top");

		JButton btnTwoPlayer = new JButton("Two Player");
		btnTwoPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newGame(0, 0);
				hideMenu();
				showBoard();
			}
		});
		panel.add(btnTwoPlayer, "6, 6, fill, top");

		JButton btnMinimaxVsWhite = new JButton("MiniMaxVsWhite");
		btnMinimaxVsWhite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newGame(1, 0);
				hideMenu();
				showBoard();
			}
		});
		panel.add(btnMinimaxVsWhite, "6, 12, left, top");

		JButton btnMonteVsMinimax = new JButton("MonteVsMiniMax");
		btnMonteVsMinimax.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long times = Long.parseLong(textField.getText());
				newGame(2, 1, times);
				if (isBoardDisplayed.getSelectedObjects() != null) {
					hideMenu();
					showBoard();
				}

			}
		});
		panel.add(btnMonteVsMinimax, "6, 20, right, top");

		JButton btnMinimaxVsMonte = new JButton("MiniMaxVsMonte");
		btnMinimaxVsMonte.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long times = Long.parseLong(textField.getText());
				newGame(1, 2, times);
				if (isBoardDisplayed.getSelectedObjects() != null) {
					hideMenu();
					showBoard();
				}
			}
		});
		panel.add(btnMinimaxVsMonte, "6, 22, left, top");

		JLabel lblTimes = new JLabel("Number of games:");
		panel.add(lblTimes, "6, 24");

		JButton btnStats_1 = new JButton("Stats");
		btnStats_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showStats();
			}
		});
		panel.add(btnStats_1, "6, 36, fill, top");

		panel.add(isBoardDisplayed, "6, 28");
		board.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null,
				null));
		board.setBounds(margin, margin, width - margin * 2, width - margin * 2);
		board.initBoard(Game.n);
		board.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				if (Game.game) {
					JOptionPane.showMessageDialog(frame, "Game Over!");
					return;
				}
				if (Game.player[Game.Turn] != 0) {
					JOptionPane.showMessageDialog(frame, "Not your turn!");
					return;
				}
				int x = arg0.getX();
				int y = arg0.getY();
				int index = Game.transPoint(x, y);
				CrossPoint ocp = board.points.get(index);
				if (!Game.putTest(ocp))
					return;
				Game.displayNewPiece(index);
				int result = Game.getResult(index);
				System.out.print(index+" ");
				Game.updateLabel(result);
				Thread t = new Thread() {
					public void run() {
						Game.switchPlayer();
					}
				};
				t.start();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});
		frame.getContentPane().add(board);
		showMenu();
		Game.board = board;
		Game.label = label;
	}
	private void hideMenu() {
		panel.setVisible(false);
	}
	private void showMenu() {
		panel.setVisible(true);
	}
	private void hideBoard() {
		board.setVisible(false);
		label.setVisible(false);
		btnMenu.setVisible(false);
		btnReplay.setVisible(false);
		btnStats.setVisible(false);
	}
	private void showBoard() {
		board.setVisible(true);
		label.setVisible(true);
		btnMenu.setVisible(true);
		btnReplay.setVisible(true);
		btnStats.setVisible(true);
	}
	private void showStats() {
		Object[] options = {"Got it", "Save"};
		String s = Game.statsContent();
		int n = JOptionPane.showOptionDialog(frame, s, "Statistics",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[0]);
		switch (n) {
			case 0 :
				break;
			case 1 :
				Game.saveStats(s);
				break;
		}
	}

}
