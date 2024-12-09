package view.component;

import view.container.frame.GameFrame;

import javax.swing.*;
import java.awt.*;


public class GameOverDialog extends JDialog {

    public GameOverDialog(JFrame parent, int stage){
        super(parent, "Game Over",true);
        setTitle("Game Over");
        setLayout(new FlowLayout());
        setSize(200,100);
        setLocationRelativeTo(null);

        JButton ok = new JButton("Try Again");
        ok.addActionListener(e -> {
            setVisible(false);
            parent.dispose();
            restartStage(stage);
        });

        JButton cancel = new JButton("Exit");
        cancel.addActionListener(e->{
            setVisible(false);
            System.exit(0);
        });

        add(ok);
        add(cancel);
        setVisible(true);
    }

    private void restartStage(int stage){
        switch (stage){
            case 1: break;
            case 2:
                GameFrame gameFrame = new GameFrame();
                gameFrame.setVisible(true);
                break;
            case 3: break;
            default: break;
        }
    }
}
