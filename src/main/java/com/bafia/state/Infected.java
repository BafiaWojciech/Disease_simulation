package com.bafia.state;

import com.bafia.Unit;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Infected implements State {
    private static final Random random = new Random();
    private final boolean hasSymptoms;
    private Unit owner;
    private Map<Unit, Integer> closeUnits;
    private List<Unit> closeUnitsLastTime;
    private Integer timeToHeal;


    public Infected(Unit owner) {
        this.owner = owner;
        closeUnits = new HashMap<>();
        closeUnitsLastTime = new ArrayList<>();
        timeToHeal = random.nextInt(20 * 25, 30 * 25);
        if (random.nextDouble() < .3) {      //30% na to ze nie bedzie miał objawów
            hasSymptoms = false;
        } else {
            hasSymptoms = true;
        }
    }

    public void infect(List<Unit> close) {
        //przeiteruj wszystkie closeUnits i sprawdź czy zawierają klucz
        for(Unit u : close) {
            if(closeUnits.containsKey(u)) {
                closeUnits.put(u, closeUnits.get(u) + 1);
                if(closeUnits.get(u) > 75) {
                    u.setState(new Infected(u));
                }
            } else {
                closeUnits.put(u, 1);
            }
        }
        // jeżeli lista dostarczona w tej iteracji nie zawiera obiektu z poprzedniej iteracji,
        // to znaczy, że obiekty przestały być blisko siebie
        // W takim przypadku, należy usunąć obiekt z hashmapy
        for(Unit u : closeUnitsLastTime) {
            if(!close.contains(u)) {
                closeUnits.remove(u);
            }
        }

        if(--timeToHeal == 0) {
            owner.setState(new Immune());
        }

        // Kopiujemy dostarczoną listę do closeUnitsLastTime, przyda się w następnej iteracji
        closeUnitsLastTime = new ArrayList<>(close);

    }

    @Override
    public Color getColor() {
        return hasSymptoms ? new Color(240, 0, 0) : new Color(255, 89, 0);
    }
}
