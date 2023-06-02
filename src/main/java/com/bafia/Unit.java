package com.bafia;

import com.bafia.state.Healthy;
import com.bafia.state.Infected;
import com.bafia.state.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Unit {
    static double maxVelocityPerStep = 2.5/25;
    static Random random = new Random();
    Vector position;
    Vector motion;
    State state;
    List<UnitMemento> mementoList;

    public Unit(double x, double y) {
        this.mementoList = new ArrayList<>();
        this.position = new Vector(x, y);
        this.motion = new Vector(maxVelocityPerStep);
        if(random.nextDouble()<.05) {
            this.state = new Infected(this);
        } else {
            this.state = new Healthy();
        }
        this.mementoList.add(new UnitMemento(this));
    }

    public void setState(State s) {
        state = s;
    }
    public State getState() {
        return state;
    }


    public void getFrame(int nthFrame) {
        if(nthFrame >= mementoList.size()) {
            this.position.add(motion);
            if(random.nextDouble() < .25)
                this.motion = new Vector(maxVelocityPerStep);
            this.mementoList.add(new UnitMemento(this));
        }  else {
            UnitMemento tmpMemento = mementoList.get(nthFrame);
            position.x = tmpMemento._position.x;
            position.y = tmpMemento._position.y;
            state = tmpMemento._state;
        }
    }

    private class UnitMemento {
        Vector _position;
        State _state;

        public UnitMemento(Unit u) {
            this._position = new Vector(u.position.x, u.position.y);
            this._state = u.state;
        }
    }
}
