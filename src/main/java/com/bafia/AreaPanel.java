package com.bafia;

import com.bafia.state.Healthy;
import com.bafia.state.Infected;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class AreaPanel extends JPanel {
    private static Random random = new Random();
    private List<Unit> list;
    private int width, height, congestion;
    private int margin = 10;
    private double panelWidth, panelHeight;
    private int frameNumber;

    public AreaPanel(int congestion, int width, int height) {

        this.frameNumber = 0;
        this.width = width;
        this.height = height;
        this.congestion = congestion;
        this.list = new ArrayList<>();

        for (int i = 0; i < congestion; ++i) {
            list.add(new Unit(width * random.nextDouble(), height * random.nextDouble()));
        }
    }

    public void reset(int congestion, int width, int height) {
        this.frameNumber = 0;
        this.width = width;
        this.height = height;
        this.congestion = congestion;
        this.random = new Random();
        this.list.removeAll(list);
        this.list = new ArrayList<>();

        for (int i = 0; i < congestion; ++i) {
            list.add(new Unit(width * random.nextDouble(), height * random.nextDouble()));
        }
    }

    public void goToNthFrame(int n) {
        frameNumber = n;
        for (Unit u : list) {
            u.getFrame(frameNumber);
        }
    }

    public int getNumberOfCurrentFrame() {
        return frameNumber;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        panelWidth = this.getSize().width;
        panelHeight = this.getSize().height;

        goToNthFrame(frameNumber);
        detectBorderCollision();

        paintMargin(g);
        paintUnits(g);
        frameNumber++;
    }

    private void paintUnits(Graphics g) {
        for (Unit u : list) {
            //rysowanie punktu
            Vector positionOnMap = convertPosition(u.position);
            int x = (int) positionOnMap.getX();
            int y = (int) positionOnMap.getY();
            g.setColor(u.state.getColor());
            g.fillRect(x - 4, y - 4, 8, 8);
        }

    }

    private void paintMargin(Graphics g) {

        int left, right, top, bottom;

        double scalar = Math.min((panelWidth - 2 * margin) / (double) width, (panelHeight - 2 * margin) / (double) height);

        if ((panelWidth - 2 * margin) / (double) width < (panelHeight - 2 * margin) / (double) height) {
            left = margin;
            top = (int) (panelHeight - 2 * margin - height * scalar) / 2 + margin;
        } else {
            left = (int) (panelWidth - 2 * margin - width * scalar) / 2 + margin;
            top = margin;
        }
        right = (int) (left + width * scalar);
        bottom = (int) (top + height * scalar);
        g.drawLine(left, top, right, top);
        g.drawLine(left, bottom, right, bottom);
        g.drawLine(left, top, left, bottom);
        g.drawLine(right, top, right, bottom);
    }

    private Vector convertPosition(Vector p) {
        double x, y;
        double panelWidth = this.getSize().width;
        double panelHeight = this.getSize().height;
        double scalar = Math.min((panelWidth - 2 * margin) / (double) width, (panelHeight - 2 * margin) / (double) height);
        if ((panelWidth - 2 * margin) / (double) width < (panelHeight - 2 * margin) / (double) height) {
            x = margin + p.x * scalar;
            y = ((panelHeight - 2 * margin - height * scalar) / 2 + margin) + (p.y * scalar);
        } else {
            x = ((panelWidth - 2 * margin - width * scalar) / 2 + margin) + (p.x * scalar);
            y = margin + p.y * scalar;
        }
        return new Vector(x, y);
    }

    private void detectBorderCollision() {

        for (Unit u : list) {
            if (u.position.x < 0) {
                u.motion = new Vector(2.5/25, 0);
            } else if (u.position.x > width) {
                u.motion = new Vector(-2.5/25, 0);
            }

            if (u.position.y < 0) {
                u.motion = new Vector(0, 2.5/25);
            } else if (u.position.y > height) {
                u.motion = new Vector(0, -2.5/25);
            }
        }

        List<Unit> infected = new ArrayList<>();
        List<Unit> healthy = new ArrayList<>();

        for (Unit u : list) {
            if (u.state instanceof Healthy) {
                healthy.add(u);
            } else if (u.state instanceof Infected) {
                infected.add(u);
            }
        }

        for (int i = 0; i < infected.size() - 1; ++i) {
            List<Unit> healthyCloseInfected = new ArrayList<>();
            Unit infected_u = infected.get(i);
            for (int j = 0; j < healthy.size() - 1; ++j) {
                Unit healthy_u = healthy.get(j);
                if (healthy_u.position.getDistance(infected_u.position) < 2.) {
                    healthyCloseInfected.add(healthy_u);
                }
            }
            Infected inf = (Infected) infected_u.getState();
            inf.infect(healthyCloseInfected);
        }
    }
}
