package gomoku;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

import monte.MovePropose;

public class ChessBoardTest {
	
	private int miniMaxDepth = 3;
	private ChessBoardTimer chessBoardTimer = new ChessBoardTimer();
	
	public ChessBoardTest(){	
	}
	
	//黑子Monte
	//白字MiniMax
	public void TestMonteMiniMax() throws Exception{
		//初始化
		int blackWin = 0;
		int whiteWin = 0;
		long monteTotalTime = 0;
		int monteTotalStep = 0;
		long miniMaxTotalTime = 0;
		int miniMaxTotalStep = 0;
		int gameCount = 30;
		int threadCount = 12;
		int trainingTimes = 3;
		int simulateTimes = 20000;
		File file = new File("stat.txt_"+String.valueOf(simulateTimes)+"_"+String.valueOf(trainingTimes)+"_"+String.valueOf(threadCount)+"_");
		BufferedWriter out=new BufferedWriter(
		          new FileWriter(file,false));
		out.write("Test " + gameCount + " Games Black:Monte White:MiniMax");
		out.newLine();
		out.write("MiniMax Search Depth:"+ miniMaxDepth);
		out.newLine();
		out.write("Thread:"+ threadCount);
		out.newLine();
		out.write("Training Times:"+ trainingTimes);
		out.newLine();
		out.write("For Each Seed Simulate Count:"+ simulateTimes);
		out.newLine();
		out.write("=================================");
		for(int i =0; i< gameCount ;i++){
		    boolean isGameOver = false;
		    int[] chessBoard = new int[ChessBoardConstant.ChessBoardWidth * ChessBoardConstant.ChessBoardWidth];
		    Minimax minimaxPlayer = new Minimax(chessBoard);
		    MovePropose montePlayer = new MovePropose();
		    ChessBoardChecker chessBoardChecker = new ChessBoardChecker();
		    int currentPlayer = ChessBoardConstant.PlayerBlack;
		    int newMove = 0;
			long monteTime = 0;
			int monteStep = 0;
			long miniMaxTime = 0;
			int miniMaxStep = 0;
			int remainChess =225;
			int result = ChessBoardConstant.Blank;
		    ArrayList<StepStat> stepStatList = new ArrayList<StepStat>();
			while(! isGameOver){
				remainChess--;
				chessBoardTimer.reset();
				if(currentPlayer==ChessBoardConstant.PlayerBlack){
					//模拟monte
					chessBoardTimer.start();
					newMove = montePlayer.play(chessBoard, currentPlayer);
					chessBoardTimer.end();
				    System.out.println("Monte: " + newMove);
				}
				else{
					//模拟minimax
					chessBoardTimer.start();
					newMove = minimaxPlayer.getBestMove(currentPlayer, miniMaxDepth, chessBoard);
					chessBoardTimer.end();
					System.out.println("MiniMax: " + newMove);
				}
				
				stepStatList.add(new StepStat(currentPlayer,chessBoardTimer.duration()));
				System.out.println((float)chessBoardTimer.duration()/(float)1000);
				//检查结果
				chessBoard[newMove] = currentPlayer;
				if(chessBoardChecker.isWin(currentPlayer, chessBoard,newMove)){
					isGameOver = true;
					result = currentPlayer;
				}
				else if(remainChess==0){
					isGameOver = true;
					return;	
				}
				else{
					currentPlayer = ChessBoardHelper.getNextPlayer(currentPlayer);
				}
			}
			
			out.newLine();
			out.write("Game"+ String.valueOf(i+1) + ":");
			out.newLine();
			
			for(int j = 0 ;j<stepStatList.size(); j++){
				StepStat stepStat = stepStatList.get(j);
				if(stepStat.player == ChessBoardConstant.PlayerBlack){
					monteTotalTime += stepStat.executeTime;
					monteTotalStep++;
					monteTime += stepStat.executeTime;
					monteStep++;
				}
				else{
					miniMaxTotalTime += stepStat.executeTime;
					miniMaxTotalStep++;
				    miniMaxTime += stepStat.executeTime;
					miniMaxStep++;
				}
			}
			
			System.out.println("Game:"+i + "end==========================");
			System.out.println("Step:"+String.valueOf(monteStep+miniMaxStep)+ "============================");
			if(result==ChessBoardConstant.PlayerBlack){
				System.out.println("Black Win==========================");
				blackWin++;
				out.write("Black win");
			}
			else if(result==ChessBoardConstant.PlayerWhite){
				System.out.println("White Win==========================");
				whiteWin++;
				out.write("White win");
			}
			else{
				System.out.println("No Winner==========================");
				out.write("No Winner");
			}
			out.newLine();
			out.write("Step:"+ String.valueOf(monteStep+miniMaxStep));
			out.newLine();
			out.write("Average time For Monte 1 step:"+ String.valueOf((float)monteTime/(float)monteStep/1000)  +"seconds");
			out.newLine();
			out.write("Average time For Minimax 1 step:"+ String.valueOf((float)miniMaxTime/(float)miniMaxStep/1000) +"seconds");
		}
		out.newLine();
		out.newLine();
		out.newLine();
		out.write("=================================");
		out.newLine();
		out.write("Total Game:" + gameCount);
		out.newLine();
		out.write("Black Win Game: " + blackWin);
		out.newLine();
		out.write("White Win Game: " + whiteWin);
		out.newLine();
		out.write("Total Average time For Monte 1 step:"+ String.valueOf((float)monteTotalTime/(float)monteTotalStep/1000)  +"seconds");
		out.newLine();
		out.write("Total Average time For Minimax 1 step:"+ String.valueOf((float)miniMaxTotalTime/(float)miniMaxTotalStep/1000)  +"seconds");
		out.flush();
		out.close();
	}
	
	public class StepStat{
		public long executeTime;
		public int player;
		
		public StepStat(int player, long executeTime){	
			this.executeTime = executeTime;
			this.player = player;
		}
	}
}
