package gomoku;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultHandler {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File in = new File("stat_5000_3_12_.txt");
		FileInputStream fis = new FileInputStream(in);
		Pattern p = Pattern.compile("Step:(\\d+)");
		BufferedReader bf = new BufferedReader(new InputStreamReader(fis,
				"UTF-8"));
		String line = "";
		int step = 0;
		int game = 0;
		while ((line = bf.readLine()) != null) {

			Matcher m = p.matcher(line);
			if (m.find()) {
				step += Integer.parseInt(m.group(1));
				game++;
			}
		}
		float average = ((float) step) / game;
		System.out.println(average);
	}

}
