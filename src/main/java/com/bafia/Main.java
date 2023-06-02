package com.bafia;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Symulacja");
        Simulation simulation = new Simulation();
        jFrame.setContentPane(simulation.getMainPanel());
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(700, 600);
        jFrame.setResizable(false);
        jFrame.setVisible(true);
    }
}
