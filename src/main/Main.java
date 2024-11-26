package main;



import javax.swing.JFrame;

import rockOfPlayer.RockThrowingGame;

public class Main {
	public static void main(String[] args) {

		JFrame frame = new JFrame();
		RockThrowingGame stage1 = new RockThrowingGame();
		
		frame.add(stage1);

		frame.pack();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
}
