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

	// --stage1---------------------------------------------------
	private Image backgroundImage;
	private Image bridgeImage;
	private Image brokenBridgeImage;
	private Image hp;
	private boolean isBridgeBroken; // 다리 초기 상태
	private boolean checkSuccess;
	// MovingMonster-----------------------------------------------
	private ArrayList<MovingMonster> movingMonstersOnRight;
	private ArrayList<MovingMonster> movingMonstersOnLeft1;
	private int spawnCounterR = 0;
	private int spawnCounterL = 0;
	private Timer timer1;
	// rockMonster------------------------------------------------

	private ArrayList<RockMonster> rockMonster;
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
	// ------------------------------------------------------------------------------------
	private Image doorToNextStage;
	private Image bridgeButton;
	// 바위 몬스터--------------------------------------------------------------------
	private ArrayList<FollowingMonster> followingMonsters;

	public void gameSuccess(Graphics g) {
		if (checkSuccess == true) {
			try {
				doorToNextStage = ImageIO.read(new File("./src/assets/image/탈출구.png"));
				doorToNextStage = doorToNextStage.getScaledInstance(60, 60, Image.SCALE_SMOOTH);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (doorToNextStage != null) {
				g.drawImage(doorToNextStage, 600 / 2 - 30, 20, null);
			}
		}
	}

	public void restart() {
		bearPlayer.setX(50);
		bearPlayer.setY(750);
		tigerPlayer.setX(550);
		tigerPlayer.setY(750);

		Timer timer=new Timer(2000,null);
		timer.start();

	}

	public void checkGameover() {
		if (bearPlayer.getHp() <= 0 || tigerPlayer.getHp() <= 0) {
			System.exit(0); // End the game
		}

	}

	public RockThrowingGame() {
		super(true); // 더블 버퍼링 활성화
		setPreferredSize(new Dimension(600, 800));

		try {
			backgroundImage = ImageIO.read(new File("./src/assets/image/background.png"));
			backgroundImage = backgroundImage.getScaledInstance(600, 800, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			// 다리 장치
			// 이미지--------------------------------------------------------------------------------------
			bridgeButton = ImageIO.read(new File("./src/assets/image/발판.png"));
			bridgeButton = bridgeButton.getScaledInstance(50, 20, Image.SCALE_SMOOTH);

		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			hp = ImageIO.read(new File("./src/assets/image/하트.png"));
			hp = hp.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {

			bridgeImage = ImageIO.read(new File("./src/assets/image/다리.png"));
			bridgeImage = bridgeImage.getScaledInstance(120, 80, Image.SCALE_SMOOTH);

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {

			brokenBridgeImage = ImageIO.read(new File("./src/assets/image/부서진다리.png"));
			brokenBridgeImage = brokenBridgeImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);

		} catch (IOException e) {
			e.printStackTrace();
		}

//stage1-------------------------------------

		monsters = new ArrayList<>();
		rocksOfPlayer = new ArrayList<>();

		bearPlayer = new Player(50, 770, 0);
		tigerPlayer = new Player(550, 770, 1);
		// MovingMonster---------------------------

		movingMonstersOnRight = new ArrayList<>();
		movingMonstersOnLeft1 = new ArrayList<>();

		timer1 = new Timer(20, this);
		timer1.start();
		// RockMonster--------------------------------------
		rockMonster = new ArrayList<>();

		rockMonster.add(new RockMonster(300, 80));

		rocksOfMonsters = new ArrayList<>();
		timer2 = new Timer(20, this);
		timer2.start();
		// RockThrowingGame--------------------------------

		generateRocksOfPlayer();

		timer3 = new Timer(20, this);
		timer3.start();
		addKeyListener(this);
		setFocusable(true);
		// 바위산신 ----------
		followingMonsters = new ArrayList<>();
		followingMonsters.add(new FollowingMonster(200, 550));
		followingMonsters.add(new FollowingMonster(400, 550));
	}

	@Override
	protected void paintComponent(Graphics g) {
		// super.paintComponent(g);

		// 배경 이미지 그리기
		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, 600, 800, this);
		}
		drawBridgeElements(g);
		bearPlayer.draw(g);
		tigerPlayer.draw(g);



		drawHpStatus(g);

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
		for (RockMonster monster : rockMonster) {
			monster.draw(g);
		}

		for (RocksOfMonsters rock : rocksOfMonsters) {
			rock.draw(g);
		}

		// Stage
		// Status------------------------------------------------------------------------------

		gameSuccess(g);

		// 바위몬스터-----------------------------------------------------------------------------

		for (FollowingMonster monster : followingMonsters) {
			monster.draw(g);
		}

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

		for (RockMonster monster : rockMonster) {
			monster.throwRocks(rocksOfMonsters);
		}

		updateRocks();
		updateRockMonster();
		checkCollisionsRockAttacked();
		// RockThrowing-------------------------------------------
		updateMonsters();
		updateLaunchedRock();
		checkCollisions();
		// 바위몬스터-----------------------------------------------
		updateFollowingMonsters();
		checkCollisionsFollowingMonster();

		bearPlayer.move();
		tigerPlayer.move();
		checkGameover();
		repaint();

	}

	// Stage1--------------------------------
	private void drawBridgeElements(Graphics g) {

		if (isBridgeBroken) {
			g.drawImage(brokenBridgeImage, 300, 380, null);

		} else {
			g.drawImage(bridgeImage, 279, 378, null);

		}

		g.drawImage(bridgeButton, 330, 470, null);

		checkBearFixPosition();
	}

	private void checkBearFixPosition() {

		if (bearPlayer.getX() > 335 && bearPlayer.getX() < 375 && bearPlayer.getY() > 800 / 2 + 60
				&& bearPlayer.getY() < 800 / 2 + 75) {
			isBridgeBroken = false;

		} else {

			isBridgeBroken = true;
		}
	}

	private void drawHpStatus(Graphics g) {

		for (int i = 0; i < bearPlayer.getHp(); i++) {
			g.drawImage(hp, 7 + 42 * i, 20, null);

		}

		for (int i = 0; i < tigerPlayer.getHp(); i++) {
			g.drawImage(hp, 460 + 42 * i, 20, null);
		}

		Color brown = new Color(165, 42, 42);

		Font customFont = new Font("Serif", Font.BOLD, 15);
		g.setColor(brown);
		g.setFont(customFont);
		g.drawString("곰", 5, 15);
		g.setColor(Color.ORANGE);
		g.drawString("호랑이", 450, 15);

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
		for (int i = rockMonster.size() - 1; i >= 0; i--) {
			if (launchedRock != null) {
				if (launchedRock.collidesWithMonster(rockMonster.get(i))) {
					launchedRock = null;
					rockMonster.get(i).setHp(-1);
					if (rockMonster.get(i).getHp() <= 0) {

						rockMonster.remove(i);

						checkSuccess = true;
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
		for (int i = 0; i < 9; i++) {
			int x = 30 + rand.nextInt(560);
			int y = 460 + rand.nextInt(290);
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

	// 바위
	// 몬스터--------------------------------------------------------------------------
	private void updateFollowingMonsters() {
		for (int i = followingMonsters.size() - 1; i >= 0; i--) {
			FollowingMonster monster = followingMonsters.get(i);

			monster.followPlayer(bearPlayer.getX(), bearPlayer.getY());
			monster.followPlayer(tigerPlayer.getX(), tigerPlayer.getY());

		}

	}

	private void checkCollisionsFollowingMonster() {
		int check = 0;
		int who = 2;
		for (FollowingMonster monster : followingMonsters) {
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
			// System.exit(0); // End the game
		}
		if (check == 1)
			restart();

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE && hasRock) {
			launchRock();
		}
		if (e.getKeyCode() == KeyEvent.VK_Q) {
			killFollowingMonster();
		}
		int key = e.getKeyCode();

		// 임시로 새로운 위치 계산
		int newX = bearPlayer.getX();
		int newY = bearPlayer.getY();

		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (checkBoundaryForBearPlayer()) {
				bearPlayer.setMovingUp(true);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN && checkBoundaryForBearPlayer()) {
			if (checkBoundaryForBearPlayer()) {
				bearPlayer.setMovingDown(true);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT && checkBoundaryForBearPlayer()) {
			if (checkBoundaryForBearPlayer()) {
				bearPlayer.setMovingLeft(true);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT && checkBoundaryForBearPlayer()) {
			if (checkBoundaryForBearPlayer()) {
				bearPlayer.setMovingRight(true);
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_W && checkBoundaryForTigerPlayer()) {
			tigerPlayer.setMovingUp(true);
		} else if (e.getKeyCode() == KeyEvent.VK_S && checkBoundaryForTigerPlayer()) {
			tigerPlayer.setMovingDown(true);
		} else if (e.getKeyCode() == KeyEvent.VK_A && checkBoundaryForTigerPlayer()) {
			tigerPlayer.setMovingLeft(true);
		} else if (e.getKeyCode() == KeyEvent.VK_D && checkBoundaryForTigerPlayer()) {
			tigerPlayer.setMovingRight(true);
		}

	}// -----------------------넣어라??--------------------

	public boolean checkBoundaryForBearPlayer() {
		if (bearPlayer.getY() < 385 || bearPlayer.getY() > 465)
			return true;
		else
			return false;
	}

	public boolean checkBoundaryForTigerPlayer() {
		if (tigerPlayer.getY() < 385 || tigerPlayer.getY() > 465)
			return true;
		else
			return false;
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

	private double calculateDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	private int checkIftoKillFollowingMonster;

	private void killFollowingMonster() {
		FollowingMonster targetMonster = null;

		for (FollowingMonster monster : followingMonsters) {
			double distance = calculateDistance(tigerPlayer.getX(), tigerPlayer.getY(), monster.getX(), monster.getY());
			if (distance <= 80) {
				targetMonster = monster; // 제거 대상 설정
				break;
			}
		}

		// 몬스터 제거
		if (targetMonster != null) {
			monsters.remove(targetMonster);

		}
	}

}

//-------------------------------------------------------------------------------------------------------------------------
