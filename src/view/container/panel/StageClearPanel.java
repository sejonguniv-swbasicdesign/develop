package view.container.panel;

import view.component.DesignedButton;
import view.container.frame.GameFrame;
import view.container.frame.third.InitBossStage;

import javax.swing.*;
import java.awt.*;


public class StageClearPanel extends JPanel {

    private ImagePanel imagePanel;
    private Container container;
    private String nextStage;
    private Container gameFrame;

    public StageClearPanel(){
    }

    public void setContainer(Container container){
        this.container = container;
    }
    public void setNextStage(String nextStage){
        this.nextStage = nextStage;
    }
    public void setGameFrame(Container gameFrame){
        this.gameFrame = gameFrame;
    }

    public void setPanel(){
        new JPanel();
        setLayout(null);
        setSize(new Dimension(300,200));
        setPreferredSize(new Dimension(300,200));
        setPreferredSize(new Dimension(300,200));
        imagePanel = new ImagePanel();
        imagePanel.setImage("src/assets/image/background/stage_clear_background.png");
        imagePanel.setSize(new Dimension(300, 200));
        imagePanel.setPreferredSize(new Dimension(300, 200));
        imagePanel.setMinimumSize(new Dimension(300, 200));
        add(imagePanel, JLayeredPane.DEFAULT_LAYER);
        setLabel();
        setButton();
        setVisible(true);
        container.add(this);
        container.revalidate();
        container.repaint();
    }

    private void setLabel(){
        JLabel label = new JLabel("Stage Clear");
        label.setFont(new Font("plain", Font.PLAIN,25));
        label.setBounds(75,20,150,50);
        add(label);
        setComponentZOrder(label, 0);
    }

    private void setButton(){
        DesignedButton designedButton = new DesignedButton();
        designedButton.setTextColor(Color.WHITE, Color.RED);
        designedButton.setText("Next Stage");
        designedButton.setFont(new Font("plain", Font.BOLD,20));
        designedButton.setBounds(75,100,150,50);

        designedButton.addActionListener(e -> {
            JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(container);
            currentFrame.dispose();
            JFrame currentGameFrame = (JFrame) SwingUtilities.getWindowAncestor(gameFrame);
            currentGameFrame.dispose();
            if(nextStage.equals("stage2")){
                GameFrame gameFrame = new GameFrame();
                gameFrame.setVisible(true);
            }
            else if(nextStage.equals("stage3")){
                new InitBossStage();
            }
        });

        add(designedButton);
        setComponentZOrder(designedButton, 0);
    }
}
