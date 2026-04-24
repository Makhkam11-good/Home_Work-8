package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class PoisonedState implements HeroState {

    private int turnsRemaining;
    private final int poisonDamage;

    public PoisonedState(int turnsRemaining, int poisonDamage) {
        this.turnsRemaining = Math.max(1, turnsRemaining);
        this.poisonDamage = Math.max(1, poisonDamage);
    }

    public PoisonedState() {
        this(3, 3);
    }

    @Override
    public String getName() {
        return "Poisoned";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return Math.max(0, (int) Math.floor(basePower * 0.8));
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return Math.max(0, (int) Math.ceil(rawDamage * 1.15));
    }

    @Override
    public void onTurnStart(Hero hero) {
        if (!hero.isAlive()) {
            return;
        }
        hero.takeDamage(poisonDamage);
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsRemaining--;
        if (turnsRemaining <= 0 && hero.isAlive()) {
            hero.setState(new NormalState());
        }
    }

    @Override
    public boolean canAct() {
        return true;
    }
}
