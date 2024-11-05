package controller;

import controller.MonsterMovementController;
import model.Storage;
import model.characters.BearPlayer;
import model.characters.TigerPlayer;

import java.io.IOException;
import java.awt.Rectangle;
import javax.swing.JLabel;
import javax.swing.Timer;

//화면에 하트 띄우기
public class HpControl {
	private Timer timer;
	private Storage storage;
	private TigerPlayer tigerPlayer;
	private BearPlayer bearPlayer; // 곰,호랑이 정보 받아오기
	private MonsterMovementController monsterMovementControl; // 몬스터 정보 받아오기
	private JLabel monsterLabel;
	private Rectangle monsterRect;

	public void getChatacterInfo() { // 캐릭터 정보 받아오는 매소드
		try {
			storage = Storage.getInstance();

			tigerPlayer = storage.getTiger();
			bearPlayer = storage.getBear(); // 곰,호랑이 정보 받아오기
			monsterLabel = monsterMovementControl.getMonster();

		} catch (IOException e) {
			System.out.println("IOException 발생: " + e.getMessage());
		}

	}

	public void detectTigerCollision() {// 호랑이가 몬스터와 충돌확인 매소드
		Rectangle tigerPlayerRect = new Rectangle(tigerPlayer.x, tigerPlayer.y, 64, 64); // 캐릭터 있는 부분 rectangle 정의
		monsterRect.setBounds(monsterLabel.getX(), monsterLabel.getY(), monsterLabel.getWidth(),
				monsterLabel.getHeight());
		if (tigerPlayerRect.intersects(monsterRect)) { // 충돌 확인
			if (tigerPlayer.hp > 0) {
				tigerPlayer.hp--;

			}
		}

	}

	public void detectBearCollision() {// 곰이 몬스터와 충돌확인 매소드
		Rectangle bearPlayerRect = new Rectangle(bearPlayer.x, bearPlayer.y, 64, 64); // 캐릭터 있는 부분 rectangle 정의
		monsterRect.setBounds(monsterLabel.getX(), monsterLabel.getY(), monsterLabel.getWidth(),
				monsterLabel.getHeight());
		if (bearPlayerRect.intersects(monsterRect)) { // 충돌 확인
			if (bearPlayer.hp > 0) {
				bearPlayer.hp--;

			}
		}

	}

	public HpControl() { // 생성자

		timer = new Timer(100, e -> { // 충돌감지지속검사
			getChatacterInfo();

			detectTigerCollision();
			detectBearCollision();
			if (tigerPlayer.hp == 0 && bearPlayer.hp == 0) {
				// game over 시
			}
		});
		timer.start();
	}
}
