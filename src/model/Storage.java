package model;

import model.characters.BearPlayer;
import model.characters.TigerPlayer;
import model.monsters.Boss;

import java.io.IOException;

public class Storage {
    private static Storage instance;
    private BearPlayer bearPlayer;
    private TigerPlayer tigerPlayer;
    private Boss boss;
    private int sharedHp; // 공유 HP

    // private 생성자를 통해 외부에서 객체 생성 방지
    private Storage() {
        try {
            this.bearPlayer = new BearPlayer(0, 6);
            this.tigerPlayer = new TigerPlayer(0, 0);
            this.boss = new Boss(500, 300); // 보스 객체 초기화
            this.sharedHp = 3;
        } catch (IOException e) {
            System.out.println("파일을 읽어들이는 데에 실패했습니다. 프로그램을 다시 실행해 주세요.");
        }
    }

    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    public BearPlayer getBear() {
        return bearPlayer;
    }

    public TigerPlayer getTiger() {
        return tigerPlayer;
    }

    public Boss getBoss() {
        return boss;
    }

    public int getSharedHp() {
        return sharedHp;
    }

    public void decreaseSharedHp() {
        if (sharedHp > 0) {
            sharedHp--;
        }
    }

    public void resetSharedHp() {
        sharedHp = 3;
    }
}
