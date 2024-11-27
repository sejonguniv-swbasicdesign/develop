package model;

import model.characters.BearPlayer;
import model.characters.TigerPlayer;

import java.io.IOException;

public class Storage {
    private static Storage instance;
    private BearPlayer bearPlayer;
    private TigerPlayer tigerPlayer;

    // private 생성자를 통해 외부에서 객체 생성 방지
    private Storage() throws IOException {
        this.bearPlayer = new BearPlayer(0, 6);
        this.tigerPlayer = new TigerPlayer(0, 0);
    }

    public static Storage getInstance() throws IOException {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    private Storage(int x, int y) throws IOException {
        this.bearPlayer = new BearPlayer(x, y);
        this.tigerPlayer = new TigerPlayer(x+70, y);
    }

    public static Storage getInstance(int x ,int y) throws IOException {
        if (instance == null) {
            instance = new Storage(x,y);
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
