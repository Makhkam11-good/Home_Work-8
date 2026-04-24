package com.narxoz.rpg;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.BossFloor;
import com.narxoz.rpg.floor.CombatFloor;
import com.narxoz.rpg.floor.RestFloor;
import com.narxoz.rpg.floor.TowerFloor;
import com.narxoz.rpg.floor.TrapFloor;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.state.StunnedState;
import com.narxoz.rpg.tower.TowerRunResult;
import com.narxoz.rpg.tower.TowerRunner;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Hero arin = new Hero("Arin", 120, 22, 4);
        Hero liora = new Hero("Liora", 105, 18, 5);

        arin.setState(new PoisonedState(2, 2));
        liora.setState(new StunnedState(1));

        List<Hero> party = Arrays.asList(arin, liora);

        List<TowerFloor> floors = Arrays.asList(
                new CombatFloor(),
                new TrapFloor(),
                new RestFloor(),
                new CombatFloor(),
                new BossFloor()
        );

        TowerRunner runner = new TowerRunner(floors);

        System.out.println("=== Tower Run Begins ===");
        TowerRunResult result = runner.run(party);
        System.out.println("=== Tower Run Ends ===");

        System.out.println("Floors cleared: " + result.getFloorsCleared());
        System.out.println("Heroes surviving: " + result.getHeroesSurviving());
        System.out.println("Reached top: " + result.isReachedTop());

        System.out.println("Final party status:");
        for (Hero hero : party) {
            System.out.println("- " + hero.getName() + " | hp=" + hero.getHp() + " | state=" + hero.getStateName());
        }
    }
}
