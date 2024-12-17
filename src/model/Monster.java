package model;

//몬스터 객체
public abstract class Monster {

    //체력, x,y좌표
    public int hp;
    public int x,y;

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
