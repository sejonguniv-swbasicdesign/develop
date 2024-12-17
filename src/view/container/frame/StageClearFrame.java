package view.container.frame;

import view.container.panel.StageClearPanel;

import javax.swing.*;
import java.awt.*;

public class StageClearFrame extends JFrame {

    private StageClearPanel stageClearPanel;
    private String nextStage;
    private Container container;

    public StageClearFrame(String nextStage,Container container) {
        this.nextStage = nextStage;
        this.container = container;
        stageClearPanel = new StageClearPanel();
        setStageClearFrame();
    }

    public void setStageClearFrame(){
        setTitle("Stage Clear");
        setSize(310, 230);
        setPreferredSize(new Dimension(310, 230));
        setMinimumSize(new Dimension(310, 230));
        setLocationRelativeTo(null);

        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        stageClearPanel.setContainer(this.getContentPane());
        stageClearPanel.setGameFrame(container);
        stageClearPanel.setPanel();
        stageClearPanel.setNextStage(nextStage);

    }
}
