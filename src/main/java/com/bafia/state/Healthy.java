package com.bafia.state;

import com.bafia.Unit;

import java.awt.*;

public class Healthy implements State {
    @Override
    public Color getColor() {
        return new Color(0,110,230);
    }
}
