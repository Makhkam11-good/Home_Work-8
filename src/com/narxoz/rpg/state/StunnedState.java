package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class StunnedState implements HeroState {

    private int turnsRemaining;

    public StunnedState(int turnsRemaining) {
        this.turnsRemaining = Math.max(1, turnsRemaining);
    }

    public StunnedState() {
        this(1);
    }

    @Override
    public String getName() {
        return "Stunned";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return basePower;
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return Math.max(0, (int) Math.ceil(rawDamage * 1.2));
    }

    @Override
    public void onTurnStart(Hero hero) {
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
        return false;
    }
}
