package view.container.frame;

import view.container.panel.GamePanel;

import javax.swing.*;
import java.awt.*;

//게임 화면
public class GameFrame extends JFrame{

    private GamePanel gamePanel;
    public GameFrame(){
        gamePanel = new GamePanel();
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

        gamePanel.setGamePanel(this.getContentPane());
    }
}
