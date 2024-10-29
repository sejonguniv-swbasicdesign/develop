package view.container.panel;

import javax.swing.*;
import java.awt.*;

public class SettingPanel {

    private JPanel settingPanel;
    private Container container;
    public SettingPanel(){

    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public void addSettingPanel() {
        settingPanel = setSettingPanel();
        container.add(settingPanel);
        container.revalidate();
        container.repaint();
    }

    private JPanel setSettingPanel(){
        JPanel settingPanel = new JPanel();
        settingPanel.setSize(new Dimension(800,630));
        settingPanel.setPreferredSize(new Dimension(800, 630));
        settingPanel.setMinimumSize(new Dimension(800, 630));

        settingPanel.setBackground(Color.LIGHT_GRAY);

        return  settingPanel;
    }
}
