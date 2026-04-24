package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.state.StunnedState;
import java.util.List;

public class CombatFloor extends TowerFloor {

    private Monster monster;

    @Override
    protected String getFloorName() {
        return "Forgotten Arena";
    }

    @Override
    protected void setup(List<Hero> party) {
        monster = new Monster("Ghoul Knight", 60, 14);
        System.out.println("Setup: a " + monster.getName() + " blocks the path.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("Challenge: combat begins.");
        int hpBefore = totalHp(party);
        int round = 1;

        while (monster.isAlive() && hasLivingHero(party) && round <= 8) {
            System.out.println("Round " + round + ":");

            for (Hero hero : party) {
                if (!hero.isAlive()) {
                    continue;
                }

                hero.onTurnStart();
                if (!hero.isAlive()) {
                    System.out.println(hero.getName() + " falls from turn effects.");
                    continue;
                }

                if (!hero.canAct()) {
                    System.out.println(hero.getName() + " cannot act due to " + hero.getStateName() + ".");
                    hero.onTurnEnd();
                    continue;
                }

                int hit = hero.getEffectiveAttackPower();
                monster.takeDamage(hit);
                System.out.println(hero.getName() + " hits " + monster.getName() + " for " + hit
                    + " (monster hp: " + monster.getHp() + ")");
                hero.onTurnEnd();

                if (!monster.isAlive()) {
                    break;
                }
            }

            if (!monster.isAlive()) {
                break;
            }

            Hero target = firstLivingHero(party);
            if (target == null) {
                break;
            }

            int hpTargetBefore = target.getHp();
            monster.attack(target);
            int dealt = Math.max(0, hpTargetBefore - target.getHp());
            System.out.println(monster.getName() + " strikes " + target.getName() + " for " + dealt
                    + " (hp: " + target.getHp() + ")");

            if (target.isAlive() && round == 1) {
                target.setState(new PoisonedState(2, 2));
            }
            if (target.isAlive() && round == 2) {
                target.setState(new StunnedState(1));
            }

            round++;
        }

        int hpAfter = totalHp(party);
        int damageTaken = Math.max(0, hpBefore - hpAfter);
        boolean cleared = !monster.isAlive();
        String summary = cleared ? "The party wins the combat floor." : "The party fails to clear the floor.";
        System.out.println("Challenge result: " + summary);

        return new FloorResult(cleared, damageTaken, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("Loot: survivors recover 4 HP.");
        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(4);
            }
        }
    }

    private boolean hasLivingHero(List<Hero> party) {
        for (Hero hero : party) {
            if (hero.isAlive()) {
                return true;
            }
        }
        return false;
    }

    private Hero firstLivingHero(List<Hero> party) {
        for (Hero hero : party) {
            if (hero.isAlive()) {
                return hero;
            }
        }
        return null;
    }

    private int totalHp(List<Hero> party) {
        int sum = 0;
        for (Hero hero : party) {
            sum += Math.max(0, hero.getHp());
        }
        return sum;
    }
}
