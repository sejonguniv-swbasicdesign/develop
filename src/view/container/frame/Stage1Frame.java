package view.container.frame;

import rockOfPlayer.RockThrowingGame;

import javax.swing.*;
import java.awt.*;

public class Stage1Frame {

    RockThrowingGame stage1;
    public Stage1Frame(){
        stage1 = new RockThrowingGame();
    }

    public void setStage1Frame(){
        JFrame frame = new JFrame();
        frame.setSize(600,800);
        frame.setPreferredSize(new Dimension(600,800));
        frame.setLocationRelativeTo(null);
        frame.add(stage1);

        frame.pack();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
