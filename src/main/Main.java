package main;

import javax.swing.JFrame;

import rockOfPlayer.RockThrowingGame;

public class Main {
	public static void main(String[] args) {

		JFrame frame = new JFrame();
		RockThrowingGame game = new RockThrowingGame();
		frame.add(game);

		frame.pack();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
