package view.container.panel;

import view.component.DesignedButton;

import javax.swing.*;
import java.awt.*;

public class StageClearPanel extends JPanel {

    private ImagePanel imagePanel;

    public StageClearPanel(){
    }

    public void setPanel(){
        new JPanel();
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
    }

    private void setLabel(){
        JLabel label = new JLabel("Stage Clear");
        label.setBounds(170,20,100,50);
        add(label);
    }

    private void setButton(){
        DesignedButton designedButton = new DesignedButton();
        designedButton.setText("Next Stage");
        designedButton.setBounds(170,100,100,50);
        add(designedButton);

    }
}
