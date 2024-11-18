package model;

import model.characters.BearPlayer;
import model.characters.TigerPlayer;

import java.io.IOException;

public class Storage {
    private static Storage instance;
    private BearPlayer bearPlayer;
    private TigerPlayer tigerPlayer;

    // private 생성자를 통해 외부에서 객체 생성 방지
    private Storage() {
        try {
            this.bearPlayer = new BearPlayer(0, 6);
            this.tigerPlayer = new TigerPlayer(0, 0);
        } catch (IOException e){
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

}
