package main;

import view.container.frame.third.InitBossStage;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(InitBossStage::new);
    }
}