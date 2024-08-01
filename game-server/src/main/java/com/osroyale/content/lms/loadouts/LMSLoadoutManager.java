package com.osroyale.content.lms.loadouts;

import com.osroyale.content.lms.lobby.LMSLobby;
import com.osroyale.util.Utility;
import io.github.classgraph.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;


public class LMSLoadoutManager {
    private static final Logger logger = LogManager.getLogger();

    private static List<LMSLoadout> lmsLoadouts = new ArrayList<LMSLoadout>();

    public static List<LMSLoadout> getLmsLoadouts() {
        return lmsLoadouts;
    }

    public static void load() {
        try (final ScanResult scanResult = new ClassGraph().enableClassInfo().enableMethodInfo().scan()) {
            for (final ClassInfo classInfo : scanResult.getSubclasses(LMSLoadout.class)) {
                try {
                    if (!classInfo.isAbstract()) {
                        final MethodInfoList constructorInfos = classInfo.getDeclaredConstructorInfo();
                        final MethodInfo constructorInfo = constructorInfos.get(0);
                        final Constructor<?> constructor = constructorInfo.loadClassAndGetConstructor();

                        final Object instance = constructor.newInstance();
                        final LMSLoadout loadout = (LMSLoadout) instance;

                        loadout.setup();
                        lmsLoadouts.add(loadout);
                    }
                } catch (Exception ex) {
                    logger.error(String.format("Error loading lms loadout=%s", classInfo.getSimpleName()), ex);
                }
            }
        }
        logger.info(String.format("Loaded: %d lms loadouts.", lmsLoadouts.size()));

        LMSLobby.currentGameType = LMSLoadoutManager.getLmsLoadouts().get(Utility.random(LMSLoadoutManager.getLmsLoadouts().size()));
    }

}
