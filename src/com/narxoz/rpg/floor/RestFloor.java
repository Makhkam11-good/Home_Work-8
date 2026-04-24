package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.NormalState;
import java.util.List;

public class RestFloor extends TowerFloor {

    @Override
    protected String getFloorName() {
        return "Sanctuary Chamber";
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("Setup: a quiet shrine offers a recovery ritual.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("Challenge: maintain focus during a short ritual.");
        int hpBefore = totalHp(party);

        int focused = 0;
        for (Hero hero : party) {
            if (!hero.isAlive()) {
                continue;
            }
            hero.heal(6);
            hero.setState(new NormalState());
            focused++;
            System.out.println(hero.getName() + " completes the ritual (hp: " + hero.getHp() + ").");
        }

        int hpAfter = totalHp(party);
        int damageTaken = Math.max(0, hpBefore - hpAfter);
        boolean cleared = focused > 0;
        String summary = cleared
                ? "The party recovers and prepares for the next ascent."
                : "No one remains to complete the ritual.";
        System.out.println("Challenge result: " + summary);

        return new FloorResult(cleared, damageTaken, summary);
    }

    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        return false;
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("Loot: none.");
    }

    private int totalHp(List<Hero> party) {
        int sum = 0;
        for (Hero hero : party) {
            sum += Math.max(0, hero.getHp());
        }
        return sum;
    }
}
