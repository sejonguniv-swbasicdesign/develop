package view.container.frame;


import view.container.panel.AnimationPanel;

import javax.swing.*;
import java.awt.*;

public class AnimationFrame extends JFrame {

    private AnimationPanel animationPanel;

    public AnimationFrame() {
        setTitle("스토리");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,630);
        setPreferredSize(new Dimension(800,630));
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        pack();
        Container c = getContentPane();
        this.animationPanel = new AnimationPanel(c);
        setContentPane(animationPanel);
    }

    public void setStage(int stage){
        animationPanel.setStage(stage);
    }
}
