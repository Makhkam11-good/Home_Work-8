package com.narxoz.rpg.tower;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.FloorResult;
import com.narxoz.rpg.floor.TowerFloor;
import java.util.List;

public class TowerRunner {

    private final List<TowerFloor> floors;

    public TowerRunner(List<TowerFloor> floors) {
        this.floors = floors;
    }

    public TowerRunResult run(List<Hero> party) {
        int floorsCleared = 0;

        for (TowerFloor floor : floors) {
            if (!hasLivingHero(party)) {
                break;
            }

            FloorResult result = floor.explore(party);
            System.out.println("Floor summary: " + result.getSummary());

            if (!result.isCleared()) {
                break;
            }

            floorsCleared++;
        }

        int heroesSurviving = countLivingHeroes(party);
        boolean reachedTop = floorsCleared == floors.size() && heroesSurviving > 0;
        return new TowerRunResult(floorsCleared, heroesSurviving, reachedTop);
    }

    private boolean hasLivingHero(List<Hero> party) {
        for (Hero hero : party) {
            if (hero.isAlive()) {
                return true;
            }
        }
        return false;
    }

    private int countLivingHeroes(List<Hero> party) {
        int count = 0;
        for (Hero hero : party) {
            if (hero.isAlive()) {
                count++;
            }
        }
        return count;
    }
}
