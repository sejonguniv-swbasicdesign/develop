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
public class HpConrtol {
	private Timer timer;
	private Storage storage;

	public void getChatacterHp() {
		try {
			storage = Storage.getInstance();
		} catch (IOException e) {
			System.out.println("IOException 발생: " + e.getMessage());
		}

	}

	private TigerPlayer tigerPlayer = storage.getTiger();
	private BearPlayer bearPlayer = storage.getBear(); // 곰,호랑이 정보 받아오기

	private MonsterMovementController monsterMovementControl; // 몬스터 정보 받아오기
	private JLabel monsterLabel = monsterMovementControl.getMonster();
	private Rectangle monsterRect = new Rectangle(monsterLabel.getX(), monsterLabel.getY(), monsterLabel.getWidth(),
			monsterLabel.getHeight());

	// ----------------------------------------------------------

	public void detectTigerCollision() {// 호랑이가 몬스터와 충돌
		Rectangle tigerPlayerRect = new Rectangle(tigerPlayer.x, tigerPlayer.y, 64, 64);

		if (tigerPlayerRect.intersects(monsterRect)) {
			if (tigerPlayer.hp > 0) {
				tigerPlayer.hp--;

			}
		}

	}

	public void detectBearCollision() {// 곰이 몬스터와 충돌
		Rectangle bearPlayerRect = new Rectangle(bearPlayer.x, bearPlayer.y, 64, 64);

		if (bearPlayerRect.intersects(monsterRect)) {
			if (bearPlayer.hp > 0) {
				bearPlayer.hp--;

			}
		}

	}

	HpConrtol() {
		getChatacterHp();

		timer = new Timer(100, e -> { // 충돌감지지속검사
			detectTigerCollision();
			detectBearCollision();
			if (tigerPlayer.hp == 0 && bearPlayer.hp == 0) {
				// game over 시
			}
		});
		timer.start();
	}
}