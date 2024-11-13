package controller;
import model.HpStatus;
import view.HpOnScreen;
import javax.swing.Timer;



public class HpConrtol {
	private int  hpOfTigerPlayer;
	private int  hpOfBearPlayer;		//hp 상태 저장
	private HpStatus hpStatus;
	
	
	HpConrtol() {//hp 상태 받아오기
		hpOfTigerPlayer=hpStatus.getHpOfTigerPlayer();
		hpOfBearPlayer=hpStatus.getHpOfBearPlayer();
		
		
	}
	void updateHpStatus() {//hp 상태 업데이트
		hpStatus.changeHpOfTigerPlayer(hpOfTigerPlayer);
		hpStatus.changeHpOfBearPlayer(hpOfBearPlayer);
		
	}
	
	//----------------------------------------------------------
	
	
	public boolean detectCollision( /*Tiger of Bear*/) {//곰따로 호랑이따로 해야됨....
		if( true/*tiger or bear get hurt*/ ) {//***************************************again
			return true;
		}
		else return false;
	}
	
    private void hitObstacle() {		//hp 깍이면...
    	if(detectCollision(/*Tiger*/)) {
    		
    		if (hpOfTigerPlayer > 0) {
            hpOfTigerPlayer--;  
 
    		}
    	}
        
    	if(detectCollision(/*Bear*/)) {
    		
    		if (hpOfBearPlayer > 0) {
            hpOfBearPlayer--;  
 
    		}
    	}
        
    		if (hpOfTigerPlayer == 0 && hpOfBearPlayer == 0) {
    				//game over
    		}
    	}

    }
	

