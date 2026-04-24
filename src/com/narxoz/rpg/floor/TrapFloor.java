package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.state.StunnedState;
import java.util.List;

public class TrapFloor extends TowerFloor {

    @Override
    protected String getFloorName() {
        return "Venom Hallway";
    }

    @Override
    protected void announce() {
        System.out.println("\n--- Entering Venom Hallway: pressure plates and toxic darts ---");
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("Setup: narrow tiles, hidden needles, collapsing sections.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("Challenge: each hero crosses the trapped corridor.");
        int hpBefore = totalHp(party);

        for (Hero hero : party) {
            if (!hero.isAlive()) {
                continue;
            }

            int hpStart = hero.getHp();
            hero.takeDamage(10);
            int dealt = Math.max(0, hpStart - hero.getHp());
            System.out.println(hero.getName() + " suffers trap damage: " + dealt + " (hp: " + hero.getHp() + ")");

            if (!hero.isAlive()) {
                continue;
            }

            if (hero.getHp() <= Math.max(1, hero.getMaxHp() / 4)) {
                hero.setState(new StunnedState(1));
            } else {
                hero.setState(new PoisonedState(2, 2));
            }
        }

        int hpAfter = totalHp(party);
        int damageTaken = Math.max(0, hpBefore - hpAfter);
        boolean cleared = hasLivingHero(party);
        String summary = cleared
                ? "The party survives the corridor and finds the exit lever."
                : "All heroes fall to the trap corridor.";
        System.out.println("Challenge result: " + summary);

        return new FloorResult(cleared, damageTaken, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("Loot: antidote fragments restore 2 HP to survivors.");
        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(2);
            }
        }
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("Cleanup: the corridor seals behind the party.");
    }

    private boolean hasLivingHero(List<Hero> party) {
        for (Hero hero : party) {
            if (hero.isAlive()) {
                return true;
            }
        }
        return false;
    }

    private int totalHp(List<Hero> party) {
        int sum = 0;
        for (Hero hero : party) {
            sum += Math.max(0, hero.getHp());
        }
        return sum;
    }
}
