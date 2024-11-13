package model.monsters;

public abstract class Monster {

    public int hp;
    public int x;
    public int y;

    public Monster(int hp, int x, int y) {
        this.hp = hp;
        this.x = x;
        this.y = y;
    }



    public void move(){}

    protected void resetHp(int hp)  {
        this.hp = hp;
    }

}
