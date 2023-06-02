package com.bafia.state;

import java.awt.*;

public class Immune implements State {
    @Override
    public Color getColor() {
        return new Color(0,150,0);
    }
}
