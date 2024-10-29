package model;

public class HpStatus {
    private int hpOfTigerPlayer ;
    
    private int hpOfBearPlayer ;
    
    
    HpStatus(){
    	hpOfTigerPlayer=3;
    	hpOfTigerPlayer=3;
    }
   public int getHpOfTigerPlayer() {
    	return hpOfTigerPlayer;
    }
   public int getHpOfBearPlayer() {
    	return hpOfBearPlayer;
    } 
   
   public void changeHpOfTigerPlayer(int n) {
    	hpOfTigerPlayer=n;
    	
    }
   public void changeHpOfBearPlayer(int n) {
    	hpOfBearPlayer=n;
    	
    }
}
