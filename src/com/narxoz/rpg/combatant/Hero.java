package com.narxoz.rpg.combatant;

import com.narxoz.rpg.state.HeroState;

/**
 * Represents a player-controlled hero participating in the tower climb.
 *
 * Students: you may extend this class as needed for your implementation.
 * You will need to add a HeroState field and related methods.
 */
public class Hero {

    private static final HeroState DEFAULT_STATE = new HeroState() {
        @Override
        public String getName() {
            return "Normal";
        }

        @Override
        public int modifyOutgoingDamage(int basePower) {
            return basePower;
        }

        @Override
        public int modifyIncomingDamage(int rawDamage) {
            return rawDamage;
        }

        @Override
        public void onTurnStart(Hero hero) {
        }

        @Override
        public void onTurnEnd(Hero hero) {
        }

        @Override
        public boolean canAct() {
            return true;
        }
    };

    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;
    private HeroState state;

    public Hero(String name, int hp, int attackPower, int defense) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.state = DEFAULT_STATE;
    }

    public String getName()        { return name; }
    public int getHp()             { return hp; }
    public int getMaxHp()          { return maxHp; }
    public int getAttackPower()    { return attackPower; }
    public int getDefense()        { return defense; }
    public boolean isAlive()       { return hp > 0; }
    public HeroState getState()    { return state; }
    public String getStateName()   { return state.getName(); }

    public void setState(HeroState state) {
        if (state == null) {
            throw new IllegalArgumentException("state cannot be null");
        }
        this.state = state;
    }

    public void onTurnStart() {
        state.onTurnStart(this);
    }

    public void onTurnEnd() {
        state.onTurnEnd(this);
    }

    public boolean canAct() {
        return state.canAct();
    }

    public int getEffectiveAttackPower() {
        return Math.max(0, state.modifyOutgoingDamage(attackPower));
    }

    /**
     * Reduces this hero's HP by the given amount, clamped to zero.
     *
     * @param amount the damage to apply; must be non-negative
     */
    public void takeDamage(int amount) {
        int reduced = Math.max(0, amount - defense);
        int modified = Math.max(0, state.modifyIncomingDamage(reduced));
        hp = Math.max(0, hp - modified);
    }

    /**
     * Restores this hero's HP by the given amount, clamped to maxHp.
     *
     * @param amount the HP to restore; must be non-negative
     */
    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }
}
