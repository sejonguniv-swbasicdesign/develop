package model;

public enum PlayerPosition {
    BEAR_START(300, 400),
    TIGER_START(800, 400);

    private final int x;
    private final int y;

    PlayerPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
