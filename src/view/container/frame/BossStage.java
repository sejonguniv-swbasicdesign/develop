package view.container.frame;

import utils.constants.ComponentSize;
import view.container.panel.third.Background;

import javax.swing.*;
import java.awt.*;

public class BossStage extends JFrame {
    private Background background;

    public BossStage() {
        background = new Background();
        setFrame();
    }

    private void setFrame() {
        setTitle("Boss Stage");
        setSize(ComponentSize.BOSSSTAGEWIDTH.getBossDimension());
        setPreferredSize(ComponentSize.BOSSSTAGEWIDTH.getBossDimension());
        setMinimumSize(ComponentSize.BOSSSTAGEWIDTH.getBossDimension());
        setResizable(false);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true);
        setVisible(true);
        pack();

        background.setContainer(this.getContentPane());
        background.setBackgroundPanel();
    }
}
