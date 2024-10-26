package controller;

import view.Stage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class MoveInteraction {
    private Stage tmpStage;
    private Timer timer;

    public MoveInteraction(Stage tmpStage) {
        this.tmpStage = tmpStage;

        // 60 FPS로 캐릭터 위치 업데이트
        timer = new Timer(1000 / 60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tmpStage.updateCharacterPositions();
            }
        });
        timer.start();
    }

    public void stop() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }
}
