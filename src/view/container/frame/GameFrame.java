package view.container.frame;

import view.container.panel.Stage2Panel;


import javax.swing.*;
import java.awt.*;

//게임 화면
public class GameFrame extends JFrame{

    private Stage2Panel stage2Panel;
    public GameFrame(){
        stage2Panel = new Stage2Panel();
        setFrame();

    }

    private void setFrame(){
        setTitle("Start Game");
        setSize(1500, 1000);
        setPreferredSize(new Dimension(1500, 1000));
        setMinimumSize(new Dimension(1500, 1000));
        setLocationRelativeTo(null);

        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        stage2Panel.setStage2Panel(this.getContentPane());

    }
}
