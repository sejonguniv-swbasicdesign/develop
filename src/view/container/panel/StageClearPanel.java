package view.container.panel;

import view.component.DesignedButton;

import javax.swing.*;
import java.awt.*;

public class StageClearPanel extends JPanel {


    public StageClearPanel(){
    }

    public void setPanel(){
        new JPanel();
        setSize(new Dimension(500,400));
        setPreferredSize(new Dimension(500,400));
        setBackground(Color.WHITE);
        setLabel();
        setButton();
        setVisible(true);
    }

    private void setLabel(){
        JLabel label = new JLabel("Stage Clear");
        label.setBounds(200,100,100,50);
        add(label);
    }

    private void setButton(){
        DesignedButton designedButton = new DesignedButton();
        designedButton.setText("Next Stage");
        designedButton.setBounds(200,300,100,50);
        add(designedButton);

    }
}
