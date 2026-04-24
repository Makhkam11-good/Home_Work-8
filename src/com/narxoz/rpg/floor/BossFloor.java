package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.state.StunnedState;
import java.util.List;

public class BossFloor extends TowerFloor {

    private Monster boss;

    @Override
    protected String getFloorName() {
        return "Abyss Throne";
    }

    @Override
    protected void announce() {
        System.out.println("\n=== BOSS FLOOR: The Abyss Warden awakens ===");
    }

    @Override
    protected void setup(List<Hero> party) {
        boss = new Monster("Abyss Warden", 120, 20);
        System.out.println("Setup: " + boss.getName() + " towers over the party.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("Challenge: final battle begins.");
        int hpBefore = totalHp(party);
        int round = 1;

        while (boss.isAlive() && hasLivingHero(party) && round <= 10) {
            System.out.println("Boss Round " + round + ":");

            for (Hero hero : party) {
                if (!hero.isAlive()) {
                    continue;
                }

                hero.onTurnStart();
                if (!hero.isAlive()) {
                    System.out.println(hero.getName() + " falls before acting.");
                    continue;
                }

                if (!hero.canAct()) {
                    System.out.println(hero.getName() + " is unable to act (" + hero.getStateName() + ").");
                    hero.onTurnEnd();
                    continue;
                }

                int hit = hero.getEffectiveAttackPower();
                boss.takeDamage(hit);
                System.out.println(hero.getName() + " strikes for " + hit + " (boss hp: " + boss.getHp() + ")");
                hero.onTurnEnd();

                if (!boss.isAlive()) {
                    break;
                }
            }

            if (!boss.isAlive()) {
                break;
            }

            Hero target = lowestHpHero(party);
            if (target == null) {
                break;
            }

            int targetBefore = target.getHp();
            boss.attack(target);
            int dealt = Math.max(0, targetBefore - target.getHp());
            System.out.println(boss.getName() + " crushes " + target.getName() + " for " + dealt
                    + " (hp: " + target.getHp() + ")");

            if (target.isAlive() && round % 2 == 0) {
                target.setState(new StunnedState(1));
            } else if (target.isAlive()) {
                target.setState(new PoisonedState(2, 3));
            }

            Hero secondTarget = nextLivingHero(party, target);
            if (secondTarget != null) {
                int before = secondTarget.getHp();
                secondTarget.takeDamage(8);
                int aoe = Math.max(0, before - secondTarget.getHp());
                System.out.println("Shadow burst hits " + secondTarget.getName() + " for " + aoe
                    + " (hp: " + secondTarget.getHp() + ")");
            }

            round++;
        }

        int hpAfter = totalHp(party);
        int damageTaken = Math.max(0, hpBefore - hpAfter);
        boolean cleared = !boss.isAlive();
        String summary = cleared
                ? "The boss falls. The top of the tower is within reach."
                : "The boss remains undefeated.";
        System.out.println("Challenge result: " + summary);

        return new FloorResult(cleared, damageTaken, summary);
    }

    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        return result.isCleared();
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("Loot: victorious heroes recover 10 HP.");
        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(10);
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

    private Hero lowestHpHero(List<Hero> party) {
        Hero candidate = null;
        for (Hero hero : party) {
            if (!hero.isAlive()) {
                continue;
            }
            if (candidate == null || hero.getHp() < candidate.getHp()) {
                candidate = hero;
            }
        }
        return candidate;
    }

    private Hero nextLivingHero(List<Hero> party, Hero excluded) {
        for (Hero hero : party) {
            if (hero.isAlive() && hero != excluded) {
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
