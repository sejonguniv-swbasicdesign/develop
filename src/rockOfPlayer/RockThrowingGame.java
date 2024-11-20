package rockOfPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class RockThrowingGame extends JPanel implements ActionListener, KeyListener {
	int playerHP = 3;
	// --stage1---------------------------------------------------
	private Image backgroundImage;
	private boolean isBridgeBroken = true; // 다리 초기 상태

	// MovingMonster-----------------------------------------------
	private ArrayList<MovingMonster> movingMonstersOnRight;
	private ArrayList<MovingMonster> movingMonstersOnLeft1;
	private int spawnCounterR = 0;
	private int spawnCounterL = 0;
	private Timer timer1;
	// rockMonster------------------------------------------------

	private ArrayList<RockMonster> rockMonsters;
	private ArrayList<RocksOfMonsters> rocksOfMonsters;
	private Timer timer2;
	// ------------------------------------------------------------------------------

	private ArrayList<ThrowingMonster> monsters;
	private ArrayList<RockForAttack> rocksOfPlayer;
	private Player bearPlayer;
	private Player tigerPlayer;
	private RockForAttack launchedRock = null;
	private boolean hasRock = false;

	private double rockDirectionX = 0, rockDirectionY = 0;
	private Timer timer3;

	public void restart() {
		bearPlayer.setX(50);
		bearPlayer.setY(750);
		tigerPlayer.setX(550);
		tigerPlayer.setY(750);

	}

	public void checkGameover() {
		if (bearPlayer.getHp() <= 0 || tigerPlayer.getHp() <= 0) {
			System.exit(0); // End the game
		}

	}

	public RockThrowingGame() {
		setPreferredSize(new Dimension(600, 800));

		try {
			backgroundImage = ImageIO.read(new File("./src/assets/image/background.png"));
			// backgroundImage=backgroundImage.getScaledInstance(600, 800,
			// Image.SCALE_SMOOTH);

		} catch (IOException e) {
			e.printStackTrace();
		}
//stage1-------------------------------------
		monsters = new ArrayList<>();
		rocksOfPlayer = new ArrayList<>();
		bearPlayer = new Player(50, 750, 0);
		tigerPlayer = new Player(550, 750, 1);
		// MovingMonster---------------------------

		movingMonstersOnRight = new ArrayList<>();
		movingMonstersOnLeft1 = new ArrayList<>();

		timer1 = new Timer(20, this);
		timer1.start();
		// RockMonster--------------------------------------
		rockMonsters = new ArrayList<>();
		rockMonsters.add(new RockMonster(100, 80));
		rockMonsters.add(new RockMonster(300, 80));
		rockMonsters.add(new RockMonster(500, 80));
		rocksOfMonsters = new ArrayList<>();
		timer2 = new Timer(60, this);
		timer2.start();
		// RockThrowingGame--------------------------------

		generateRocksOfPlayer();

		timer3 = new Timer(120, this);
		timer3.start();
		addKeyListener(this);
		setFocusable(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// 배경 이미지 그리기
		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
		}

		drawStageElements(g);
		// ----------rocksOfPlayer-----------------------------------------------------
		for (ThrowingMonster monster : monsters) {
			monster.draw(g);
		}
		for (RockForAttack rock : rocksOfPlayer) {
			rock.draw(g);
		}
		if (launchedRock != null) {
			launchedRock.draw(g);
		}
		// -------------------------------------------------------------
		for (MovingMonster monster : movingMonstersOnRight) {
			monster.draw(g);
		}

		for (MovingMonster monster : movingMonstersOnLeft1) {
			monster.draw(g);
		}
		// RockMonster--------------------------------------------------
		for (RockMonster monster : rockMonsters) {
			monster.draw(g);
		}

		for (RocksOfMonsters rock : rocksOfMonsters) {
			rock.draw(g);
		}


		bearPlayer.draw(g);
		tigerPlayer.draw(g);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// MovingMonster---------------------------------
		spawnMonstersOnRightSide();
		spawnMonstersOnLeftSide();
		updateRightMonsters();
		updateLeftMonsters();
		checkCollisionsMovingMonster();

		// RockMonster--------------------------------
		for (RockMonster monster : rockMonsters) {
			monster.throwRocks(rocksOfMonsters);
		}
		updateRocks();
		updateRockMonster();
		checkCollisionsRockAttacked();
//RockThrowing-------------------------------------------
		updateMonsters();
		updateLaunchedRock();
		checkCollisions();
		bearPlayer.move();
		tigerPlayer.move();
		checkGameover();
		repaint();
	}

	// Stage1--------------------------------
	private void drawStageElements(Graphics g) {

		if (isBridgeBroken) {
			g.setColor(Color.RED);
			g.drawString("Broken Bridge", getWidth() / 2 - 30, getHeight() / 2);
		} else {
			g.setColor(Color.GREEN);
			g.drawString("Bridge", getWidth() / 2 - 20, getHeight() / 2);
		}
		g.setColor(Color.BLUE);
		g.fillOval(350,455,50,10);
		// 충돌 감지
		checkBearPosition();
	}

	private void checkBearPosition() {

		if (bearPlayer.getX() > 350 && bearPlayer.getX() < 400 && bearPlayer.getY() > 800 / 2 + 55
				&& bearPlayer.getY() < 800 / 2 + 65) {
			isBridgeBroken = false;
		} else {
			isBridgeBroken = true;
		}
	}

//MovingMonster------------------------------------------------------------
	private void spawnMonstersOnRightSide() {
		spawnCounterR++;
		if (spawnCounterR >= 50) {
			movingMonstersOnRight.add(new MovingMonster(800, 150));
			spawnCounterR = 0;
		}
	}

	private void spawnMonstersOnLeftSide() {
		spawnCounterL++;
		if (spawnCounterL >= 50) {
			movingMonstersOnLeft1.add(new MovingMonster(0, 250));
			spawnCounterL = 0;
		}
	}

	private void updateRightMonsters() {
		for (int i = movingMonstersOnRight.size() - 1; i >= 0; i--) {
			MovingMonster monster = movingMonstersOnRight.get(i);
			monster.moveLeft();
			if (monster.isOffScreen()) {
				movingMonstersOnRight.remove(i);
			}
		}
	}

	private void updateLeftMonsters() {
		for (int i = movingMonstersOnLeft1.size() - 1; i >= 0; i--) {
			MovingMonster monster = movingMonstersOnLeft1.get(i);
			monster.moveRight();
			if (monster.isOffScreen()) {
				movingMonstersOnLeft1.remove(i);
			}
		}
	}

	private void checkCollisionsMovingMonster() {
		int check = 0;
		int who = 2;
		for (MovingMonster monster : movingMonstersOnRight) {
			if (monster.collidesWith(bearPlayer)) {
				check = 1;
				who = 0;
			}
			if (monster.collidesWith(tigerPlayer)) {
				check = 1;
				who = 1;
			}
		}
		for (MovingMonster monster : movingMonstersOnLeft1) {
			if (monster.collidesWith(bearPlayer)) {
				check = 1;
				who = 0;
			}
			if (monster.collidesWith(tigerPlayer)) {
				check = 1;
				who = 1;
			}
		}
		if (check == 1 && who == 0) {
			bearPlayer.setHp(-1);
			// System.exit(0); // End the game
		} else if (check == 1 && who == 1) {
			tigerPlayer.setHp(-1);
			restart();
			// System.exit(0); // End the game
		}

	}

	// RockMonster------------------------------------------------------
	private void updateRocks() {
		for (int i = rocksOfMonsters.size() - 1; i >= 0; i--) {
			RocksOfMonsters rock = rocksOfMonsters.get(i);
			rock.move();
			if (rock.isDestroyed()) {
				rocksOfMonsters.remove(i);
			}
		}
	}
	private void updateRockMonster() {
		for (int i = rockMonsters.size() - 1; i >= 0; i--) {
			if(launchedRock!=null) {
				if(launchedRock.collidesWithMonster(rockMonsters.get(i))) {
					launchedRock=null;
					rockMonsters.get(i).setHp(-1);
					if(rockMonsters.get(i).getHp()<=0) {
						rockMonsters.remove(i);
					}
				
				}
			}
		}
	}

	private void checkCollisionsRockAttacked() {
		for (RocksOfMonsters rock : rocksOfMonsters) {
			if (rock.collidesWith(bearPlayer) || rock.collidesWith(tigerPlayer)) {
				if (rock.collidesWith(bearPlayer))
					bearPlayer.setHp(-1);
				else if (rock.collidesWith(tigerPlayer))
					tigerPlayer.setHp(-1);
				restart();

			}
		}
	}

//-------------------------------------RockThrowingGame--------------------
	private void generateRocksOfPlayer() {
		Random rand = new Random();
		for (int i = 0; i < 6; i++) { // Generate 5 rocks
			int x =  rand.nextInt(600);
			int y = 100 + rand.nextInt(280);
			rocksOfPlayer.add(new RockForAttack(x, y));
		}
	}

	private void updateMonsters() {
		for (int i = monsters.size() - 1; i >= 0; i--) {
			ThrowingMonster monster = monsters.get(i);
			monster.move();
			if (monster.isOffScreen()) {
				monsters.remove(i);
			}
		}
	}

	private void updateLaunchedRock() {
		if (launchedRock != null) {
			launchedRock.move(rockDirectionX, rockDirectionY);
			if (launchedRock.isOffScreen()) {
				launchedRock = null; // Remove the rock if it goes off screen
			}
		}
	}

	private void checkCollisions() {
		for (int i = monsters.size() - 1; i >= 0; i--) {
			ThrowingMonster monster = monsters.get(i);
			if (monster.collidesWith(bearPlayer) || monster.collidesWith(tigerPlayer)) {
				if (monster.collidesWith(bearPlayer))
					bearPlayer.setHp(-1);
				else if (monster.collidesWith(tigerPlayer))
					tigerPlayer.setHp(-1);
				restart();

			}
		}

		for (int i = rocksOfPlayer.size() - 1; i >= 0; i--) {
			RockForAttack rock = rocksOfPlayer.get(i);
			if (rock.collidesWith(bearPlayer)) {
				if (hasRock == true)
					continue;
				else {
					hasRock = true;
					rocksOfPlayer.remove(i);
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE && hasRock) {
			launchRock();
		}

			if (e.getKeyCode() == KeyEvent.VK_UP&&checkBoundaryForBearPlayer() ) {
				bearPlayer.setMovingUp(true);
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN&&checkBoundaryForBearPlayer()) {
				bearPlayer.setMovingDown(true);
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT&&checkBoundaryForBearPlayer()) {
				bearPlayer.setMovingLeft(true);
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT&&checkBoundaryForBearPlayer()) {
				bearPlayer.setMovingRight(true);
			}
		

			if (e.getKeyCode() == KeyEvent.VK_W&&checkBoundaryForTigerPlayer()) {
				tigerPlayer.setMovingUp(true);
			} else if (e.getKeyCode() == KeyEvent.VK_S&&checkBoundaryForTigerPlayer()) {
				tigerPlayer.setMovingDown(true);
			} else if (e.getKeyCode() == KeyEvent.VK_A&&checkBoundaryForTigerPlayer()) {
				tigerPlayer.setMovingLeft(true);
			} else if (e.getKeyCode() == KeyEvent.VK_D&&checkBoundaryForTigerPlayer()) {
				tigerPlayer.setMovingRight(true);
			}
		

	}//-----------------------넣어라??--------------------
	public boolean checkBoundaryForBearPlayer() {
		if(bearPlayer.getY()<385||bearPlayer.getY()>455)return true;
		else return false;
	}
	public boolean checkBoundaryForTigerPlayer() {
		if(tigerPlayer.getY()<385||tigerPlayer.getY()>455)return true;
		else return false;
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			bearPlayer.setMovingUp(false);
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			bearPlayer.setMovingDown(false);
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			bearPlayer.setMovingLeft(false);
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			bearPlayer.setMovingRight(false);
		} else if (e.getKeyCode() == KeyEvent.VK_W) {
			tigerPlayer.setMovingUp(false);
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			tigerPlayer.setMovingDown(false);
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			tigerPlayer.setMovingLeft(false);
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			tigerPlayer.setMovingRight(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	private void launchRock() {
		double angle = Math.atan2(bearPlayer.getDirectionY(), bearPlayer.getDirectionX());
		rockDirectionX = Math.cos(angle) * 5;
		rockDirectionY = Math.sin(angle) * 5;
		launchedRock = new RockForAttack(bearPlayer.getX(), bearPlayer.getY());
		hasRock = false;
	}

}

//-------------------------------------------------------------------------------------------------------------------------
