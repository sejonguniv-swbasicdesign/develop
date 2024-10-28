package controller;
import model.HpStatus;
import view.HpOnScreen;


public class HpConrtol {
	private int hpOfTigerPlayer=3;
	private int hpOfBearPlayer=3;
	
	public int detectCollision() {
		if() {
			return 1;
		}
		else return 0;
	}
	
    private void hitObstacle() {
    	if(detectCollision()==1) {
    		
    		if (hpOfTigerPlayer > 0) {
            hpOfTigerPlayer--;  
 
    		}
        
    		if (hpOfBearPlayer > 0) {
            hpOfBearPlayer--;  
          
    		}
        
        
    		if (hpOfTigerPlayer == 0 && hpOfBearPlayer == 0) {
         
    		}
    	}

    }
	
}
