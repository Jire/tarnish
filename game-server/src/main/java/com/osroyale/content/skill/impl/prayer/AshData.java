package com.osroyale.content.skill.impl.prayer;

import java.util.Arrays;
import java.util.Optional;

public enum AshData {
    FIENDISH_ASHES(25766, 25767, 10.0D),
    VILE_ASHES(25769, 25770, 25.0D),
    MALICIOUS_ASHES(25772, 25773, 65.0D),
    ABYSSAL_ASHES(25775, 25776,85.0D),
    INFERNAL_ASHES(25778, 25779, 110.0D);

    private final int id;
    private final int id2;
    private final double experience;

    AshData(int id, int noted, double experience) {
        this.id = id;
        this.id2 = noted;
        this.experience = experience;
    }

    public int getId() {
        return id;
    }
    public int getId2() {
        return id2;
    }
    public double getExperience() {
        return experience;
    }
    public static Optional<AshData> forId(int id) {
        return Arrays.stream(values()).filter(a -> a.id == id).findAny();
    }
}