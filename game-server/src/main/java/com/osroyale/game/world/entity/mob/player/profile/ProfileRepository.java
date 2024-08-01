package com.osroyale.game.world.entity.mob.player.profile;

import com.google.gson.reflect.TypeToken;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.util.GsonUtils;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles the profile repository, used for gathering important information for
 * all created profiles.
 *
 * @author Daniel
 */
public final class ProfileRepository {
    private static final Path FILE_PATH = Path.of("data", "profile", "world_profile_list.json");

    /** The hash map of all the profiles. */
    private static Map<String, Profile> profiles = new HashMap<>();

    /** Checks if a profile is registered to the parameter. */
    public static boolean exist(String name) {
        return profiles.containsKey(name);
    }

    /** Checks if the other player is a friend. */
    public static boolean isFriend(Player player, String other) {
        return player.relations.isFriendWith(other);
    }

    /** Gets all the registered accounts to a specific host. */
    public static List<String> getRegistry(String host) {
        List<String> list = new ArrayList<>();
        for (Profile profile : profiles.values()) {
            for (String host_list : profile.getHost()) {
                if (host_list != null && host_list.equalsIgnoreCase(host)) {
                    list.add(profile.getName());
                }
            }
        }
        return list;
    }

    /** Puts a profile into the hash map. */
    public static void put(Profile profile) {
        final String name = profile.getName();
        if (profiles.containsKey(name)) {
            profiles.replace(name, profile);
        } else {
            profiles.put(name, profile);
        }
        save();
    }

    /** Loads all the profiles. */
    public static void load() {
        if (!Files.exists(FILE_PATH)) return;

        final Type type = new TypeToken<Map<String, Profile>>() {
        }.getType();

        try (final FileReader reader = new FileReader(FILE_PATH.toFile())) {
            profiles = GsonUtils.json().fromJson(reader, type);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**  Saves all the profiles. */
    public static void save() {
        Thread.startVirtualThread(() -> {
            try {
                final Path path = FILE_PATH;
                Path parent = path.getParent();
                if (parent == null) {
                    throw new UnsupportedOperationException("Path must have a parent " + path);
                }
                if (!Files.exists(parent)) {
                    parent = Files.createDirectories(parent);
                }

                final Path tempFile = Files.createTempFile(parent, path.getFileName().toString(), ".tmp");
                Files.write(tempFile, GsonUtils.json().toJson(profiles).getBytes());

                Files.move(tempFile, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });
    }

}
