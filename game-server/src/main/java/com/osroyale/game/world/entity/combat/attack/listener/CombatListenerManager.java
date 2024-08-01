package com.osroyale.game.world.entity.combat.attack.listener;

import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import io.github.classgraph.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * @author StanTheWoman
 */
public final class CombatListenerManager {

    private static final Logger logger = LogManager.getLogger();

    private static final Map<Integer, Set<CombatListenerSet>> ITEM_LISTENERS = new HashMap<>();
    public static final Map<Integer, CombatListener<Npc>> NPC_LISTENERS = new HashMap<>();
    public static final Map<Integer, CombatStrategy<Npc>> NPC_STRATEGIES = new HashMap<>();

    public static void load() {
        loadItems("com.osroyale.game.world.entity.combat.attack.listener.item");
        loadNpcs("com.osroyale.game.world.entity.combat.attack.listener.npc");
    }

    private static void loadItems(final String pkg) {
        try (final ScanResult scanResult =
                     new ClassGraph()
                             .enableClassInfo()
                             .enableMethodInfo()
                             .enableAnnotationInfo()
                             .acceptPackages(pkg)
                             .scan()) {
            for (final ClassInfo classInfo : scanResult.getClassesWithAnnotation(ItemCombatListenerSignature.class)) {
                try {
                    final MethodInfoList constructorInfos = classInfo.getDeclaredConstructorInfo();
                    final MethodInfo constructorInfo = constructorInfos.get(0);
                    final Constructor<?> constructor = constructorInfo.loadClassAndGetConstructor();

                    final Object instance = constructor.newInstance();
                    @SuppressWarnings("unchecked") final CombatListener<Player> listener = (CombatListener<Player>) instance;

                    final AnnotationInfo annotationInfo = classInfo.getAnnotationInfo(ItemCombatListenerSignature.class);
                    final Annotation annotation = annotationInfo.loadClassAndInstantiate();
                    final ItemCombatListenerSignature meta = (ItemCombatListenerSignature) annotation;

                    for (int item : meta.items()) {
                        Set<CombatListenerSet> set = ITEM_LISTENERS.getOrDefault(item, new HashSet<>());
                        set.add(new CombatListenerSet(meta.items(), meta.requireAll(), listener));
                        ITEM_LISTENERS.put(item, set);
                    }
                } catch (Exception ex) {
                    logger.error(String.format("Error loading item set combat listener=%s", classInfo.getSimpleName()), ex);
                }
            }
        }
        logger.info(String.format("Loaded: %d item set combat listeners.", ITEM_LISTENERS.size()));
    }

    private static void loadNpcs(final String pkg) {
        try (final ScanResult scanResult =
                     new ClassGraph()
                             .enableClassInfo()
                             .enableMethodInfo()
                             .enableAnnotationInfo()
                             .acceptPackages(pkg)
                             .scan()) {
            for (final ClassInfo classInfo : scanResult.getClassesWithAnnotation(NpcCombatListenerSignature.class)) {
                try {
                    final MethodInfoList constructorInfos = classInfo.getDeclaredConstructorInfo();
                    final MethodInfo constructorInfo = constructorInfos.get(0);
                    final Constructor<?> constructor = constructorInfo.loadClassAndGetConstructor();

                    final Object instance = constructor.newInstance();
                    @SuppressWarnings("unchecked") final CombatListener<Npc> listener = (CombatListener<Npc>) instance;

                    final AnnotationInfo annotationInfo = classInfo.getAnnotationInfo(NpcCombatListenerSignature.class);
                    final Annotation annotation = annotationInfo.loadClassAndInstantiate();
                    final NpcCombatListenerSignature meta = (NpcCombatListenerSignature) annotation;

                    for (int npc : meta.npcs()) {
                        NPC_LISTENERS.put(npc, listener);
                    }
                } catch (Exception ex) {
                    logger.error(String.format("Error loading npc combat listener=%s", classInfo.getSimpleName()), ex);
                }
            }
        }
        logger.info(String.format("Loaded: %d npc combat listeners.", NPC_LISTENERS.size()));
    }

    public static void addListener(Player player, int id) {
        Set<CombatListenerSet> sets = ITEM_LISTENERS.get(id);

        if (sets == null) {
            return;
        }

        for (CombatListenerSet set : sets) {
            if (set.requireAll && !player.equipment.containsAll(set.set)) {
                continue;
            }

            if (!set.requireAll && !player.equipment.containsAny(set.set)) {
                continue;
            }

            player.getCombat().addListener(set.listener);
        }
    }

    public static void removeListener(Player player, int id) {
        Set<CombatListenerSet> sets = ITEM_LISTENERS.get(id);

        if (sets == null) {
            return;
        }

        for (CombatListenerSet set : sets) {
            if (set.requireAll && player.equipment.containsAll(set.set)) {
                continue;
            }

            if (!set.requireAll && player.equipment.containsAny(set.set)) {
                continue;
            }

            player.getCombat().removeListener(set.listener);
        }
    }

    public static final class CombatListenerSet {
        private final int[] set;
        private final boolean requireAll;
        private final CombatListener<Player> listener;

        CombatListenerSet(int[] set, boolean requireAll, CombatListener<Player> listener) {
            this.set = set;
            this.requireAll = requireAll;
            this.listener = listener;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if (obj instanceof CombatListenerSet) {
                CombatListenerSet other = (CombatListenerSet) obj;
                return Arrays.equals(other.set, set) && other.requireAll == requireAll;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(set, requireAll);
        }

        @Override
        public String toString() {
            return "ItemSet[set=" + Arrays.toString(set) + ", requireAll=" + requireAll + "]";
        }
    }
}
