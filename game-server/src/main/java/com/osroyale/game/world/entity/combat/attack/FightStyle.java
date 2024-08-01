package com.osroyale.game.world.entity.combat.attack;

/**
 * The enumerated type whose elements represent the fighting styles.
 *
 * @author lare96 <http://github.com/lare96>
 * @author Michael | Chex
 */
public enum FightStyle {
    ACCURATE(3, 0, 0),
    AGGRESSIVE(0, 3, 0),
    DEFENSIVE(0, 0, 3),
    CONTROLLED(1, 1, 1);

    /** The increase to accuracy. */
    private int accuracyIncrease;

    /** The increase to strength. */
    private int strengthIncrease;

    /** The increase to defense. */
    private int defensiveIncrease;

    /** Constructs a new {@link FightStyle} element. */
    FightStyle(int accuracyIncrease, int strengthIncrease, int defensiveIncrease) {
        this.accuracyIncrease = accuracyIncrease;
        this.strengthIncrease = strengthIncrease;
        this.defensiveIncrease = defensiveIncrease;
    }

    /** @return the accuracy increase */
    public int getAccuracyIncrease() {
        return accuracyIncrease;
    }

    /** @return the strength increase */
    public int getStrengthIncrease() {
        return strengthIncrease;
    }

    /** @return the defense increase */
    public int getDefensiveIncrease() {
        return defensiveIncrease;
    }

}