package model;

public class HpStatus {
    private int hpOfTigerPlayer ;
    
    private int hpOfBearPlayer ;
    
    
    HpStatus(){
    	hpOfTigerPlayer=3;
    	hpOfTigerPlayer=3;
    }
   public int getHpOfTigerPlayer() {  //hp 받아오기
    	return hpOfTigerPlayer;
    }
   public int getHpOfBearPlayer() {
    	return hpOfBearPlayer;
    } 
   
   public void changeHpOfTigerPlayer(int n) {		//hp 상태변경
    	hpOfTigerPlayer=n;
    	
    }
   public void changeHpOfBearPlayer(int n) {
    	hpOfBearPlayer=n;
    	
    }
}
