package com.bafia;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Simulation {
    private JPanel mainPanel;
    private JButton startButton;
    private JButton stopButton;
    private JButton resetButton;
    private JTextField heightTextField;
    private JTextField widthTextField;
    private JTextField congestionTextField;
    private JPanel textFieldsPanel;
    private JPanel mapPanel;
    private JPanel controlPanel;
    private JSlider slider;
    private JLabel counter;
    private AreaPanel areaPanel;
    private boolean run = false;
    private int numberOfFrame = 1;

    Simulation() {
        Timer timer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (run) {
                    int currentFrame = areaPanel.getNumberOfCurrentFrame()+1;
                    if(numberOfFrame < currentFrame)
                        numberOfFrame = currentFrame;
                    slider.setMaximum(numberOfFrame);
                    slider.setValue(currentFrame);
                    counter.setText(String.valueOf(slider.getValue()));
                    areaPanel.repaint();
                }
            }
        });
        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.start();

        startButton.addActionListener(new StartButtonListener());
        stopButton.addActionListener(new StopButtonListener());
        resetButton.addActionListener(new ResetButtonListener());
        slider.addChangeListener(new SliderListener());
        slider.setMinimum(1);
        slider.setMaximum(1);
        slider.setValue(1);
        widthTextField.setText("30");
        heightTextField.setText("30");
        congestionTextField.setText("200");
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }


    private class StartButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (areaPanel == null)
                resetButton.doClick();
            run = true;
        }
    }

    private class StopButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            run = false;
            int nr = areaPanel.getNumberOfCurrentFrame();
            if(numberOfFrame < nr) {
                numberOfFrame = nr;
            }
            slider.setMaximum(numberOfFrame);
            slider.setValue(nr);
        }
    }

    private class ResetButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            run = false;
            int width, height, congestion;
            try {
                width = parseFromTextField(widthTextField);
                height = parseFromTextField(heightTextField);
                congestion = parseFromTextField(congestionTextField);
            } catch (NumberFormatException _e) {
                return;
            }
            if(areaPanel == null) {
                areaPanel = new AreaPanel(congestion, width, height);
                mapPanel.add(areaPanel);
            } else {
                areaPanel.reset(congestion, width, height);
            }
            numberOfFrame = 1;
            slider.setMinimum(1);
            slider.setValue(1);
            areaPanel.repaint();
            areaPanel.revalidate();
        }

        private int parseFromTextField(JTextField textField) throws NumberFormatException {
            try {
                int value = Integer.parseInt(textField.getText());
                if (value > 0 && value < 10000)
                    return value;
                throw new NumberFormatException();
            } catch (NumberFormatException e) {
                textField.setText("ERROR");
                throw new NumberFormatException();
            }
        }
    }

    private class SliderListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            if (!run && areaPanel != null) {
                counter.setText(String.valueOf(slider.getValue()));
                areaPanel.goToNthFrame(slider.getValue());
                areaPanel.repaint();
            }
        }
    }
}
