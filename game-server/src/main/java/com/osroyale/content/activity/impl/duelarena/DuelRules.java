package com.osroyale.content.activity.impl.duelarena;

import java.util.EnumSet;


public final class DuelRules {

    private final EnumSet<DuelRule> flags = EnumSet.noneOf(DuelRule.class);

    private int value;

    private void incrementValue(int value) {
        this.value += value;
    }

    private void decrementValue(int value) {
        this.value -= value;
        if (this.value < 0) {
            this.value = 0;
        }
    }

    public void flag(DuelRule rule) {
        if (flags.contains(rule)) {
            return;
        }
        flags.add(rule);
        if (DuelRule.EQUIPMENT_RULES.contains(rule)) {
            incrementValue(rule.getValue());
        }
    }

    public void unflag(DuelRule rule) {
        if (!flags.contains(rule)) {
            return;
        }
        flags.remove(rule);
        if (DuelRule.EQUIPMENT_RULES.contains(rule)) {
            decrementValue(rule.getValue());
        }
    }

    public void alternate(DuelRule flag) {
        if (flags.contains(flag)) {
            flags.remove(flag);
            if (DuelRule.EQUIPMENT_RULES.contains(flag)) {
                decrementValue(flag.getValue());
            }
        } else {
            flags.add(flag);
            if (DuelRule.EQUIPMENT_RULES.contains(flag)) {
                incrementValue(flag.getValue());
            }
        }
    }

    public int getValue() {
        return value;
    }

    public boolean contains(DuelRule rule) {
        return flags.contains(rule);
    }

    public void reset() {
        flags.clear();
        value = 0;
    }

    public EnumSet<DuelRule> getFlags() {
        return flags.clone();
    }

}
