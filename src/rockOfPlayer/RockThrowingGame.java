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
	private int hp;
	private Image backgroundImage;
	private Image bridgeImage;
	private Image brokenBridgeImage;
	private Image hpImage;
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
	private ArrayList<RockForAttack> launchedRocks;

	private double rockDirectionX = 0, rockDirectionY = 0;
	private Timer timer3;
	// ------------------------------------------------------------------------------------
	private Image doorToNextStage;
	private Image bridgeButton;

	// 바위 몬스터--------------------------------------------------------------------
	private ArrayList<FollowingMonster> followingMonsters;
	private boolean isVisible = true;

	public void toTheNextStage() {
		if (checkSuccess == true) {
			if (290 <= tigerPlayer.getX() && tigerPlayer.getX() <= 310 && 10 <= tigerPlayer.getY()
					&& tigerPlayer.getY() <= 50) {
				tigerPlayer.setShowImage(false);
				System.exit(0); // 게임 클리어 시 다음 스테이지로 넘어감
				// ...----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

			}


		}

	}

	public void doorOpen(Graphics g) {
		if (checkSuccess == true) {
			try {
				doorToNextStage = ImageIO.read(new File("./src/assets/image/component/탈출구.png"));
				doorToNextStage = doorToNextStage.getScaledInstance(60, 60, Image.SCALE_SMOOTH);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (doorToNextStage != null) {
				g.drawImage(doorToNextStage, 600 / 2 - 30, 50 - 30, null);
	
			}

		}
	}

	public void restart() {
		bearPlayer.setHasRock(false);
		tigerPlayer.setHasRock(false);
		bearPlayer.setX(50);
		bearPlayer.setY(650);
		tigerPlayer.setX(550);
		tigerPlayer.setY(650);
		followingMonsters.get(0).setX(100);
		followingMonsters.get(0).setY(550);
		followingMonsters.get(1).setX(300);
		followingMonsters.get(1).setY(550);
		followingMonsters.get(2).setX(500);
		followingMonsters.get(2).setY(550);
		// 깜빡임 효과 추가

		// Timer timer = new Timer(2000, null);
		// timer.start();

	}

	public void checkGameover() {
		if (hp <= 0) {
			System.exit(0); // End the game
		}

	}

	public RockThrowingGame() {
		super(true); // 더블 버퍼링 활성화

		try {
			backgroundImage = ImageIO.read(new File("src/assets/image/stage1/background2.png"));
			backgroundImage = backgroundImage.getScaledInstance(600, 800, Image.SCALE_SMOOTH);

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			// 다리 장치
			bridgeButton = ImageIO.read(new File("./src/assets/image/stage1/발판.png"));
			bridgeButton = bridgeButton.getScaledInstance(50, 20, Image.SCALE_SMOOTH);

		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			hpImage = ImageIO.read(new File("./src/assets/image/component/하트.png"));
			hpImage = hpImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {

			bridgeImage = ImageIO.read(new File("./src/assets/image/stage1/다리.png"));
			bridgeImage = bridgeImage.getScaledInstance(120, 80, Image.SCALE_SMOOTH);

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {

			brokenBridgeImage = ImageIO.read(new File("./src/assets/image/stage1/부서진다리.png"));
			brokenBridgeImage = brokenBridgeImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);

		} catch (IOException e) {
			e.printStackTrace();
		}

//stage1-------------------------------------
		hp = 3;
		monsters = new ArrayList<>();
		rocksOfPlayer = new ArrayList<>();

		bearPlayer = new Player(50, 650, 0);
		tigerPlayer = new Player(550, 650, 1);
// MovingMonster---------------------------

		movingMonstersOnRight = new ArrayList<>();
		movingMonstersOnLeft1 = new ArrayList<>();

		timer1 = new Timer(1000 / 60, this);
		// timer1.start();
// RockMonster--------------------------------------
		rockMonster = new ArrayList<>();

		rockMonster.add(new RockMonster(300, 80));

		rocksOfMonsters = new ArrayList<>();
		// timer2 = new Timer(1000/60, this);
		// timer2.start();
		// RockThrowingGame--------------------------------

		generateRocksOfPlayer();
		launchedRocks = new ArrayList<>();

		// timer3 = new Timer(1000/60, this);
		timer1.start();

		addKeyListener(this);
		setFocusable(true);

		// 바위산신 ----------
		followingMonsters = new ArrayList<>();
		followingMonsters.add(new FollowingMonster(100, 550));
		followingMonsters.add(new FollowingMonster(300, 550));
		followingMonsters.add(new FollowingMonster(500, 550));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// 배경 이미지 그리기
		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, 600, 800, this);

		}

		drawBridgeElements(g);

		// ----------rocksOfPlayer-----------------------------------------------------
		for (ThrowingMonster monster : monsters) {
			monster.draw(g);
		}
		for (RockForAttack rock : rocksOfPlayer) {
			rock.draw(g);
		}
		for (RockForAttack rock : launchedRocks) {
			rock.draw(g);
		}

		// -------------------------------------------------------------
		for (MovingMonster monster : movingMonstersOnRight) {
			monster.draw(g);
		}

		for (MovingMonster monster : movingMonstersOnLeft1) {
			monster.draw(g);
		}
		// RockMonster--------------------------------------------------
		if (isVisible) {
			for (RockMonster monster : rockMonster) {
				monster.draw(g);
			}
		}

		for (RocksOfMonsters rock : rocksOfMonsters) {
			rock.draw(g);
		}

		// Stage
		// Status------------------------------------------------------------------------------

		doorOpen(g);

		// 바위몬스터-----------------------------------------------------------------------------

		for (FollowingMonster monster : followingMonsters) {
			monster.draw(g);
		}
		bearPlayer.draw(g);

		tigerPlayer.draw(g);

		drawHpStatus(g);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// MovingMonster---------------------------------
		spawnMonstersOnRightSide();
		spawnMonstersOnLeftSide();
		updateRightMonsters();
		updateLeftMonsters();
		checkCollisionsMovingMonster();
		checkFallFromBridge();
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
		checkDeliverRock();
		// 바위몬스터-----------------------------------------------
		updateFollowingMonsters();
		checkCollisionsFollowingMonster();

		bearPlayer.move();
		tigerPlayer.move();
		checkGameover();
		toTheNextStage();
		repaint();

	}

	// Stage1--------------------------------
	private void drawBridgeElements(Graphics g) {

		if (isBridgeBroken) {
			g.drawImage(brokenBridgeImage, 300, 366, null);
			tigerPlayer.setIfOnBridge(false);

		} else {
			g.drawImage(bridgeImage, 279, 364, null);
			tigerPlayer.setIfOnBridge(true);
		}

		g.drawImage(bridgeButton, 330, 470, null);

		checkBearFixPosition();
	}

	private void checkBearFixPosition() {

		if (bearPlayer.getX() > 335 && bearPlayer.getX() < 375 && bearPlayer.getY() > 800 / 2 + 56
				&& bearPlayer.getY() < 800 / 2 + 72) {
			isBridgeBroken = false;

		} else {

			isBridgeBroken = true;
		}
	}

	private void checkFallFromBridge() {
		if (isBridgeBroken) {
			if (340 < tigerPlayer.getX() && tigerPlayer.getX() < 346 && 340 < tigerPlayer.getY()
					&& tigerPlayer.getY() < 434) {
				hp--;
				restart();
			}
		}
	}

	private void drawHpStatus(Graphics g) {

		for (int i = 0; i < hp; i++) {
			g.drawImage(hpImage, 460 + 42 * i, 20, null);
		}

	}

	// MovingMonster------------------------------------------------------------
	private void spawnMonstersOnRightSide() {
		spawnCounterR++;
		if (spawnCounterR >= 80) {
			movingMonstersOnRight.add(new MovingMonster(800, 150));
			spawnCounterR = 0;
		}
	}

	private void spawnMonstersOnLeftSide() {
		spawnCounterL++;
		if (spawnCounterL >= 80) {
			movingMonstersOnLeft1.add(new MovingMonster(0, 270));
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
			hp--;
			restart();
			// System.exit(0); // End the game
		} else if (check == 1 && who == 1) {
			hp--;
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

		if (rockMonster.get(0).getSPEED() > 0) {
			rockMonster.get(0).moveRight();
			if (rockMonster.get(0).getX() == 380) {
				rockMonster.get(0).setSPEED(-2);
			}
		} else if (rockMonster.get(0).getSPEED() < 0) {
			rockMonster.get(0).moveLeft();
			if (rockMonster.get(0).getX() == 220) {
				rockMonster.get(0).setSPEED(2);
			}
		}

		for (int i = rockMonster.size() - 1; i >= 0; i--) {
			for (int j = launchedRocks.size() - 1; j >= 0; j--) {
				if (launchedRocks.get(j).collidesWithMonster(rockMonster.get(i))) {
					launchedRocks.remove(j);
					rockMonster.get(i).setHp(-1);
					Timer timer = new Timer(100, e -> {
						isVisible = !isVisible;
						repaint();
					});

					timer.start();

					// 1초 후 타이머 중지
					new Timer(1000, e -> {
						timer.stop();
						isVisible = true;
						repaint();
					}).start();
				}
				if (rockMonster.get(i).getHp() <= 0) {

					rockMonster.remove(i);

					checkSuccess = true;
				}

			}
		}
	}

	private void checkCollisionsRockAttacked() {
		for (int i = rocksOfMonsters.size() - 1; i >= 0; i--) {

			if (rocksOfMonsters.get(i).collidesWith(bearPlayer) || rocksOfMonsters.get(i).collidesWith(tigerPlayer)) {
				rocksOfMonsters.remove(i);
				hp--;

				restart();

			}
		}
	}

	// -------------------------------------RockThrowingGame--------------------
	private void generateRocksOfPlayer() {
		Random rand = new Random();
		for (int i = 0; i < 9; i++) {
			int x = 30 + rand.nextInt(560);
			int y = 125 + rand.nextInt(140);
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
		for (int i = launchedRocks.size() - 1; i >= 0; i--) {
			launchedRocks.get(i).move(launchedRocks.get(i).getDx(), launchedRocks.get(i).getDy());
			if (launchedRocks.get(i).isOffScreen()) {
				launchedRocks.remove(i);
			}
		}
	}

	private void checkCollisions() {
		for (int i = monsters.size() - 1; i >= 0; i--) {
			ThrowingMonster monster = monsters.get(i);
			if (monster.collidesWith(bearPlayer) || monster.collidesWith(tigerPlayer)) {
				if (monster.collidesWith(bearPlayer))
					hp--;
				else if (monster.collidesWith(tigerPlayer))
					hp--;
				restart();

			}
		}

		for (int i = rocksOfPlayer.size() - 1; i >= 0; i--) {
			RockForAttack rock = rocksOfPlayer.get(i);
			if (rock.collidesWith(tigerPlayer)) {
				if (tigerPlayer.getHasRock() == true)
					continue;
				else {
					tigerPlayer.setHasRock(true);
					rocksOfPlayer.remove(i);
				}
			}
		}

	}

	private void checkDeliverRock() {
		if (tigerPlayer.deliverRock(bearPlayer.getX(), bearPlayer.getY()) && bearPlayer.getHasRock() == false
				&& tigerPlayer.getHasRock() == true) {
			bearPlayer.setHasRock(true);
			tigerPlayer.setHasRock(false);

		}
	}

	// 바위
	// 몬스터--------------------------------------------------------------------------
	private void updateFollowingMonsters() {
		for (int i = followingMonsters.size() - 1; i >= 0; i--) {
			FollowingMonster monster = followingMonsters.get(i);
			if (monster.calculateDistance(bearPlayer.getX(), bearPlayer.getY())<=
					monster.calculateDistance(tigerPlayer.getX(), tigerPlayer.getY()) ) {
				monster.followPlayer(bearPlayer.getX(), bearPlayer.getY());
			} else {
				monster.followPlayer(tigerPlayer.getX(), tigerPlayer.getY());
			}
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
			hp--;

		} else if (check == 1 && who == 1) {
			hp--;

		}
		if (check == 1)
			restart();

	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_Q) {
			killFollowingMonster();
		}
		int key = e.getKeyCode();

		// 임시로 새로운 위치 계산
		int newX = bearPlayer.getX();
		int newY = bearPlayer.getY();

		if (bearPlayer.getHasRock() == true) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE && bearPlayer.getHasRock() == true) {
				if (bearPlayer.getHasRock() == true)
					launchRock();
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {

			bearPlayer.setMovingUp(true);

		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {

			bearPlayer.setMovingDown(true);

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			bearPlayer.setCheckDirection(0);
			bearPlayer.setMovingLeft(true);

		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			bearPlayer.setCheckDirection(1);
			bearPlayer.setMovingRight(true);

		}

		if (e.getKeyCode() == KeyEvent.VK_W) {
			tigerPlayer.setMovingUp(true);
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			tigerPlayer.setMovingDown(true);
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			tigerPlayer.setCheckDirection(0);
			tigerPlayer.setMovingLeft(true);
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			tigerPlayer.setCheckDirection(1);
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
		if (bearPlayer.getDirectionX() == 0 && bearPlayer.getDirectionY() == 0) {
			if (bearPlayer.getCheckDirection() == 0)
				angle = 135;
			else
				angle = 0;
		}

		if (bearPlayer.getHasRock() == true) {
			launchedRocks.add(new RockForAttack(bearPlayer.getX(), bearPlayer.getY()));
			launchedRocks.get(launchedRocks.size() - 1).setDx(Math.cos(angle) * 5);
			launchedRocks.get(launchedRocks.size() - 1).setDy(Math.sin(angle) * 5);
			bearPlayer.setHasRock(false);
		}
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