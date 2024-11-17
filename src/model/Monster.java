package model;

public abstract class Monster {

    public int hp;
    
    public Monster(int hp) {
        this.hp = hp;
    }

    public void move(){}

    protected void resetHp(int hp)  {
        this.hp = hp;
    }
}
