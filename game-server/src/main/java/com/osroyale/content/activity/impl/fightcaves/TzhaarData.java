package com.osroyale.content.activity.impl.fightcaves;

public final class TzhaarData {

//    public static ImmutableList<Integer> TZHAAR_NPCS = ImmutableList.of(2189, 2191, 2191_MINI, 2193, 3123, 3125, TZTOK_JAD, YT_HURKOT);

    public enum WaveData {
        WAVE_1(2189, 2191),
        WAVE_2(2193, 2189),
        WAVE_3(2193, 2191, 2189, 2189),
		WAVE_4(3123, 2189),
		WAVE_5(3123, 2191, 2189, 2189),
		WAVE_6(3123, 2193, 2189, 2189),
		WAVE_7(3123, 2193, 2191, 2191),
		WAVE_8(3125, 2189),
		WAVE_9(3125, 2191, 2189, 2189),
		WAVE_10(3125, 2193, 2189, 2189),
		WAVE_11(3125, 2193, 2191, 2191),
		WAVE_12(3125, 3123, 2189, 2189),
		WAVE_13(3125, 3123, 2193, 2189),
		WAVE_14(3125, 3125),
		WAVE_15(3127, 3128, 3128, 3128, 3128, 3128);

        private final int[] monster;

        WaveData(int... monster) {
            this.monster = monster;
        }

        public int[] getMonster() {
            return monster;
        }

        public static WaveData getOrdinal(int ordinal) {
            for (WaveData wave : values()) {
                if (wave.ordinal() == ordinal)
                    return wave;
            }
            return null;
        }

        public static WaveData getNext(int current) {
            return getOrdinal(current + 1);
        }
    }
}
