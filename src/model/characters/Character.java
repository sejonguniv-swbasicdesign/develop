package model.characters;

public abstract class Character {

    public int hp;
    public int x, y;
    boolean isFacingRight;

    public Character() {
        hp = 3;
        x = 0;
        y = 0;
        isFacingRight = true;
    }

    public Character(int x, int y) {
        this.hp = 3;
        this.x = x;
        this.y = y;
        this.isFacingRight = true;
    }

    public int getHp() {
        return this.hp;
    }

    protected void resetHp() {
        hp = 3;
    }

    public abstract void move(int x, int y);

}
