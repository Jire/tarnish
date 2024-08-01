package com.osroyale.game.world.entity.mob.player.persist;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.osroyale.content.ActivityLog;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.content.activity.impl.barrows.BrotherData;
import com.osroyale.content.activity.impl.duelarena.DuelRule;
import com.osroyale.content.clanchannel.ClanRepository;
import com.osroyale.content.clanchannel.channel.ClanChannel;
import com.osroyale.content.clanchannel.content.ClanMemberComporator;
import com.osroyale.content.dailyeffect.impl.DailySlayerTaskSkip;
import com.osroyale.content.dailyeffect.impl.DailySlayerTaskTeleport;
import com.osroyale.content.dailyeffect.impl.DailySpellBookSwap;
import com.osroyale.content.emote.EmoteUnlockable;
import com.osroyale.content.overrides.Overrides;
import com.osroyale.content.pet.PetData;
import com.osroyale.content.preset.Preset;
import com.osroyale.content.prestige.PrestigePerk;
import com.osroyale.content.skill.impl.hunter.birdhouse.PlayerBirdHouseData;
import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.content.skill.impl.slayer.SlayerTask;
import com.osroyale.content.skill.impl.slayer.SlayerUnlockable;
import com.osroyale.content.teleport.Teleport;
import com.osroyale.content.tittle.PlayerTitle;
import com.osroyale.content.tradingpost.ItemHistory;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.magic.CombatSpell;
import com.osroyale.game.world.entity.combat.weapon.WeaponInterface;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.mob.player.appearance.Appearance;
import com.osroyale.game.world.entity.mob.player.relations.PrivacyChatMode;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.game.world.entity.mob.prayer.PrayerBook;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.codec.login.LoginResponse;
import com.osroyale.util.Utility;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jire.tarnishps.Argon2;

import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static com.osroyale.util.GsonUtils.JSON_PRETTY_ALLOW_NULL;

public final class PlayerPersistFile implements PlayerPersistable {

    private static final Logger logger = LogManager.getLogger();

    private static final Path FILE_DIR = Paths.get("data", "profile", "save");

    private static Gson gson() {
        return JSON_PRETTY_ALLOW_NULL.get();
    }

    @Override
    public void save(Player player) {
        if (player.isBot) {
            return;
        }

        try {
            final Gson gson = gson();

            JsonObject properties = new JsonObject();

            for (PlayerJSONProperty property : PROPERTIES) {
                properties.add(property.label, gson.toJsonTree(property.write(player)));
            }

            final Path path = FILE_DIR.resolve(player.getName() + ".json");
            Path parent = path.getParent();
            if (parent == null) {
                throw new UnsupportedOperationException("Path must have a parent " + path);
            }
            if (!Files.exists(parent)) {
                parent = Files.createDirectories(parent);
            }

            final Path tempFile = Files.createTempFile(parent, path.getFileName().toString(), ".tmp");
            Files.write(tempFile, gson.toJson(properties).getBytes());

            Files.move(tempFile, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            logger.error(String.format("Error while saving player=%s", player.getName()), ex);
        }

        player.saved.set(true);
    }

    @Override
    public LoginResponse load(Player player, String expectedPassword) {
        try {
            final Path path = FILE_DIR.resolve(player.getName() + ".json");

            if (!Files.exists(path)) {
                player.newPlayer = true;
                player.needsStarter = true;
                return LoginResponse.NORMAL;
            }

            final JsonElement jsonElement;
            try (final Reader reader = new FileReader(path.toFile())) {
                jsonElement = JsonParser.parseReader(reader);
            }
            final JsonObject jsonObject = jsonElement.getAsJsonObject();

            for (PlayerJSONProperty property : PROPERTIES) {
                final String label = property.label;
                final JsonElement propertyElement = jsonObject.get(label);
                if (propertyElement == null || propertyElement.isJsonNull()) continue;
                property.read(player, propertyElement);
            }

            final String password = player.getPassword();
            if (expectedPassword.equals(password)) {
                return LoginResponse.NORMAL; // fallback to support old unhashed passwords
            }

            final Argon2Types type = Argon2.argon2Type(password);
            if (type == null) {
                return LoginResponse.INVALID_CREDENTIALS;
            }

            final var argon2 = Argon2.forType(type);
            if (!argon2.verify(password, expectedPassword)) {
                return LoginResponse.INVALID_CREDENTIALS;
            }

            return LoginResponse.NORMAL;
        } catch (final Exception ex) {
            ex.printStackTrace();
        }

        return LoginResponse.COULD_NOT_COMPLETE_LOGIN;
    }

    private static final PlayerJSONProperty[] PROPERTIES = {

            new PlayerJSONProperty("username") {
                @Override
                void read(Player player, JsonElement property) {
                    player.setUsername(property.getAsString());
                }

                @Override
                Object write(Player player) {
                    return player.getUsername();
                }
            },

            new PlayerJSONProperty("password") {
                @Override
                void read(Player player, JsonElement property) {
                    player.setPassword(property.getAsString());
                }

                @Override
                Object write(Player player) {
                    return player.getPassword();
                }
            },
            new PlayerJSONProperty("weapon-styles") {
                @Override
                void read(Player player, JsonElement property) {
                    HashMap<WeaponInterface, FightType> styles = gson().fromJson(property, new TypeToken<HashMap<WeaponInterface, FightType>>() {
                    }.getType());
                    styles.forEach((key, val) -> player.fightStyles.put(key, val));
                }

                @Override
                Object write(Player player) {
                    return gson().toJsonTree(player.fightStyles, new TypeToken<HashMap<WeaponInterface, FightType>>() {
                    }.getType());
                }
            },
            new PlayerJSONProperty("player-rights") {
                @Override
                void read(Player player, JsonElement property) {
                    player.right = gson().fromJson(property, PlayerRight.class);
                }

                @Override
                Object write(Player player) {
                    return player.right;
                }
            },

            new PlayerJSONProperty("last-host") {
                @Override
                void read(Player player, JsonElement property) {
                    player.lastHost = property.getAsString();
                }

                @Override
                Object write(Player player) {
                    return player.lastHost;
                }
            },

            new PlayerJSONProperty("host-list") {
                @Override
                void read(Player player, JsonElement property) {
                    player.hostList.addAll(gson().fromJson(property, new TypeToken<Set<String>>() {
                    }.getType()));
                }

                @Override
                Object write(Player player) {
                    return player.hostList;
                }
            },

            new PlayerJSONProperty("position") {
                @Override
                void read(Player player, JsonElement property) {
                    final Position position = gson().fromJson(property, Position.class);
                    final int height = position.getHeight() < 0 ? 0 : position.getHeight() % 4;
                    player.setPosition(Position.create(position.getX(), position.getY(), height));
                }

                @Override
                Object write(Player player) {
                    return player.getPosition();
                }
            },

            new PlayerJSONProperty("created") {
                @Override
                void read(Player player, JsonElement property) {
                    player.created = property.getAsString();
                }

                @Override
                Object write(Player player) {
                    if (player.created == null)
                        return Utility.getDate();
                    return player.created;
                }
            },

            new PlayerJSONProperty("play-time") {
                @Override
                void read(Player player, JsonElement property) {
                    player.playTime = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.playTime;
                }
            },

            new PlayerJSONProperty("money-spent") {
                @Override
                void read(Player player, JsonElement property) {
                    player.donation.setSpent(property.getAsInt());
                }

                @Override
                Object write(Player player) {
                    return player.donation.getSpent();
                }
            },

            new PlayerJSONProperty("donation-credits") {
                @Override
                void read(Player player, JsonElement property) {
                    player.donation.setCredits(property.getAsInt());
                }

                @Override
                Object write(Player player) {
                    return player.donation.getCredits();
                }
            },

            new PlayerJSONProperty("skilling-points") {
                @Override
                void read(Player player, JsonElement property) {
                    player.skillingPoints = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.skillingPoints;
                }
            },

            new PlayerJSONProperty("lms-kills") {
                @Override
                void read(Player player, JsonElement property) {
                    player.lmsTotalKills = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.lmsTotalKills;
                }
            },

            new PlayerJSONProperty("lms-deaths") {
                @Override
                void read(Player player, JsonElement property) {
                    player.lmsTotalDeaths = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.lmsTotalDeaths;
                }
            },

            new PlayerJSONProperty("lms-points") {
                @Override
                void read(Player player, JsonElement property) {
                    player.lmsPoints = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.lmsPoints;
                }
            },

            new PlayerJSONProperty("pest-points") {
                @Override
                void read(Player player, JsonElement property) {
                    player.pestPoints = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.pestPoints;
                }
            },

            new PlayerJSONProperty("mute-start") {
                @Override
                void read(Player player, JsonElement property) {
                    player.punishment.muteStart = property.getAsLong();
                }

                @Override
                Object write(Player player) {
                    return player.punishment.muteStart;
                }
            },

            new PlayerJSONProperty("mute-duration") {
                @Override
                void read(Player player, JsonElement property) {
                    player.punishment.muteDuration = property.getAsLong();
                }

                @Override
                Object write(Player player) {
                    return player.punishment.muteDuration;
                }
            },

            new PlayerJSONProperty("jail-start") {
                @Override
                void read(Player player, JsonElement property) {
                    player.punishment.jailStart = property.getAsLong();
                }

                @Override
                Object write(Player player) {
                    return player.punishment.jailStart;
                }
            },

            new PlayerJSONProperty("jail-duration") {
                @Override
                void read(Player player, JsonElement property) {
                    player.punishment.jailDuration = property.getAsLong();
                }

                @Override
                Object write(Player player) {
                    return player.punishment.jailDuration;
                }
            },

            new PlayerJSONProperty("player-title") {
                @Override
                void read(Player player, JsonElement property) {
                    player.playerTitle = gson().fromJson(property, PlayerTitle.class);
                }

                @Override
                Object write(Player player) {
                    return player.playerTitle;
                }
            },

            new PlayerJSONProperty("clan") {
                @Override
                void read(Player player, JsonElement property) {
                    ClanChannel channel = ClanRepository.getChannel(property.getAsString());
                    if (channel != null) {
                        player.clanChannel = channel;
                    }
                }

                @Override
                Object write(Player player) {
                    return player.clanChannel == null ? "" : player.clanChannel.getOwner();
                }
            },

            new PlayerJSONProperty("last-clan") {
                @Override
                void read(Player player, JsonElement property) {
                    player.lastClan = property.getAsString();
                }

                @Override
                Object write(Player player) {
                    return player.lastClan;
                }
            },

            new PlayerJSONProperty("clan-tag") {
                @Override
                void read(Player player, JsonElement property) {
                    player.clanTag = property.getAsString();
                }

                @Override
                Object write(Player player) {
                    return player.clanTag;
                }
            },

            new PlayerJSONProperty("clan-tag-color") {
                @Override
                void read(Player player, JsonElement property) {
                    player.clanTagColor = property.getAsString();
                }

                @Override
                Object write(Player player) {
                    return player.clanTagColor;
                }
            },

            new PlayerJSONProperty("clan-sort-type") {
                @Override
                void read(Player player, JsonElement property) {
                    player.settings.clanMemberComporator = ClanMemberComporator.valueOf(property.getAsString());
                }

                @Override
                Object write(Player player) {
                    return player.settings.clanMemberComporator.name();
                }
            },

            new PlayerJSONProperty("bank-placeholder") {
                @Override
                void read(Player player, JsonElement property) {
                    player.bank.placeHolder = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.bank.placeHolder;
                }
            },

            new PlayerJSONProperty("bank-pin") {
                @Override
                void read(Player player, JsonElement property) {
                    player.bankPin.pin = property.getAsString();
                }

                @Override
                Object write(Player player) {
                    return player.bankPin.pin;
                }
            },

            new PlayerJSONProperty("bank-vault") {
                @Override
                void read(Player player, JsonElement property) {
                    player.bankVault.coinsContainer = property.getAsLong();
                }

                @Override
                Object write(Player player) {
                    return player.bankVault.coinsContainer;
                }
            },

            new PlayerJSONProperty("bank-vault-blood-money") {
                @Override
                void read(Player player, JsonElement property) {
                    player.bankVault.bloodMoneyContainer = property.getAsLong();
                }

                @Override
                Object write(Player player) {
                    return player.bankVault.bloodMoneyContainer;
                }
            },

            new PlayerJSONProperty("pet") {
                @Override
                void read(Player player, JsonElement property) {
                    int pet = property.getAsInt();
                    if (pet != -1) {
                        player.pet = new Npc(pet, player.getPosition());
                    }
                }

                @Override
                Object write(Player player) {
                    return player.pet == null ? -1 : player.pet.id;
                }
            },

            new PlayerJSONProperty("pet-insurnce") {
                @Override
                void read(Player player, JsonElement property) {
                    player.petInsurance = gson().fromJson(property, new TypeToken<HashSet<PetData>>() {
                    }.getType());
                }

                @Override
                Object write(Player player) {
                    return player.petInsurance;
                }
            },

            new PlayerJSONProperty("lost-pets") {
                @Override
                void read(Player player, JsonElement property) {
                    player.lostPets = gson().fromJson(property, new TypeToken<HashSet<PetData>>() {
                    }.getType());
                }

                @Override
                Object write(Player player) {
                    return player.lostPets;
                }
            },

            new PlayerJSONProperty("new-player") {
                @Override
                void read(Player player, JsonElement property) {
                    player.newPlayer = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.newPlayer;
                }
            },

            new PlayerJSONProperty("needs-starter") {
                @Override
                void read(Player player, JsonElement property) {
                    player.needsStarter = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.needsStarter;
                }
            },

            new PlayerJSONProperty("kills") {
                @Override
                void read(Player player, JsonElement property) {
                    player.kill = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.kill;
                }
            },

            new PlayerJSONProperty("deaths") {
                @Override
                void read(Player player, JsonElement property) {
                    player.death = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.death;
                }
            },

            new PlayerJSONProperty("kill-streak") {
                @Override
                void read(Player player, JsonElement property) {
                    player.killstreak.streak = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.killstreak.streak;
                }
            },
            new PlayerJSONProperty("answered-trivias") {
                @Override
                void read(Player player, JsonElement property) {
                    player.answeredTrivias = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.answeredTrivias;
                }
            },
            new PlayerJSONProperty("small-pouch") {
                @Override
                void read(Player player, JsonElement property) {
                    player.smallPouch = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.smallPouch;
                }
            },

            new PlayerJSONProperty("medium-pouch") {
                @Override
                void read(Player player, JsonElement property) {
                    player.mediumPouch = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.mediumPouch;
                }
            },

            new PlayerJSONProperty("large-pouch") {
                @Override
                void read(Player player, JsonElement property) {
                    player.largePouch = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.largePouch;
                }
            },

            new PlayerJSONProperty("giant-pouch") {
                @Override
                void read(Player player, JsonElement property) {
                    player.giantPouch = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.giantPouch;
                }
            },

            new PlayerJSONProperty("rune-pouch") {
                @Override
                void read(Player player, JsonElement property) {
                    player.runePouch.runes = gson().fromJson(property, new TypeToken<LinkedList<Item>>() {
                    }.getType());
                }

                @Override
                Object write(Player player) {
                    return player.runePouch.runes;
                }
            },

            new PlayerJSONProperty("rune-pouch-copy") {
                @Override
                void read(Player player, JsonElement property) {
                    player.runePouch_copy.runes = gson().fromJson(property, new TypeToken<LinkedList<Item>>() {
                    }.getType());
                }

                @Override
                Object write(Player player) {
                    return player.runePouch_copy.runes;
                }
            },

            new PlayerJSONProperty("run-toggled") {
                @Override
                void read(Player player, JsonElement property) {
                    player.movement.setRunningToggled(property.getAsBoolean());
                }

                @Override
                Object write(Player player) {
                    return player.movement.isRunningToggled();
                }
            },

            new PlayerJSONProperty("run-energy") {
                @Override
                void read(Player player, JsonElement property) {
                    player.runEnergy = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.runEnergy;
                }
            },

            new PlayerJSONProperty("energy-rate") {
                @Override
                void read(Player player, JsonElement property) {
                    player.energyRate = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.energyRate;
                }
            },

            new PlayerJSONProperty("special-amount") {
                @Override
                void read(Player player, JsonElement property) {
                    player.getSpecialPercentage().set(property.getAsInt());
                }

                @Override
                Object write(Player player) {
                    return player.getSpecialPercentage().get();
                }
            },

            new PlayerJSONProperty("recoil-charge") {
                @Override
                void read(Player player, JsonElement property) {
                    player.ringOfRecoil = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.ringOfRecoil;
                }
            },

            new PlayerJSONProperty("whip-charge") {
                @Override
                void read(Player player, JsonElement property) {
                    player.whipCharges = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.whipCharges;
                }
            },
            new PlayerJSONProperty("ags-charge") {
                @Override
                void read(Player player, JsonElement property) {
                    player.agsCharges = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.agsCharges;
                }
            },

            new PlayerJSONProperty("dragonfire-shield-charge") {
                @Override
                void read(Player player, JsonElement property) {
                    player.dragonfireCharges = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.dragonfireCharges;
                }
            },

            new PlayerJSONProperty("dragonfire-shield-cooldown") {
                @Override
                void read(Player player, JsonElement property) {
                    player.dragonfireUsed = property.getAsLong();
                }

                @Override
                Object write(Player player) {
                    return player.dragonfireUsed;
                }
            },

            new PlayerJSONProperty("blowpipe-darts") {
                @Override
                void read(Player player, JsonElement property) {
                    player.blowpipeDarts = gson().fromJson(property, Item.class);
                }

                @Override
                Object write(Player player) {
                    return player.blowpipeDarts;
                }
            },

            new PlayerJSONProperty("blowpipe-scales") {
                @Override
                void read(Player player, JsonElement property) {
                    player.blowpipeScales = property.getAsFloat();
                }

                @Override
                Object write(Player player) {
                    return player.blowpipeScales;
                }
            },

            new PlayerJSONProperty("serpentine-helm") {
                @Override
                void read(Player player, JsonElement property) {
                    player.serpentineHelmCharges = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.serpentineHelmCharges;
                }
            },

            new PlayerJSONProperty("tanzanite-helm") {
                @Override
                void read(Player player, JsonElement property) {
                    player.tanzaniteHelmCharges = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.tanzaniteHelmCharges;
                }
            },

            new PlayerJSONProperty("magma-helm") {
                @Override
                void read(Player player, JsonElement property) {
                    player.magmaHelmCharges = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.magmaHelmCharges;
                }
            },


            new PlayerJSONProperty("craws-bow-charges") {
                @Override
                void read(Player player, JsonElement property) {
                    player.crawsBowCharges = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.crawsBowCharges;
                }
            },
            new PlayerJSONProperty("viggoras-chainmace-charges") {
                @Override
                void read(Player player, JsonElement property) {
                    player.viggorasChainmaceCharges = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.viggorasChainmaceCharges;
                }
            },
            new PlayerJSONProperty("thammaron-sceptre-charges") {
                @Override
                void read(Player player, JsonElement property) {
                    player.thammoranSceptreCharges = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.thammoranSceptreCharges;
                }
            },
            new PlayerJSONProperty("trident-seas-charges") {
                @Override
                void read(Player player, JsonElement property) {
                    player.tridentSeasCharges = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.tridentSeasCharges;
                }
            },

            new PlayerJSONProperty("trident-swamp-charges") {
                @Override
                void read(Player player, JsonElement property) {
                    player.tridentSwampCharges = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.tridentSwampCharges;
                }
            },

            new PlayerJSONProperty("celestial-ring-charges") {
                @Override
                void read(Player player, JsonElement property) {
                    player.celestialRingCharges = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.celestialRingCharges;
                }
            },

            new PlayerJSONProperty("fight-type") {
                @Override
                void read(Player player, JsonElement property) {
                    player.getCombat().setFightType(gson().fromJson(property, FightType.class));
                }

                @Override
                Object write(Player player) {
                    return player.getCombat().getFightType();
                }
            },

            new PlayerJSONProperty("client-width") {
                @Override
                void read(Player player, JsonElement property) {
                    player.settings.clientWidth = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.settings.clientWidth;
                }
            },

            new PlayerJSONProperty("client-height") {
                @Override
                void read(Player player, JsonElement property) {
                    player.settings.clientHeight = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.settings.clientHeight;
                }
            },

            new PlayerJSONProperty("spell-book") {
                @Override
                void read(Player player, JsonElement property) {
                    player.spellbook = gson().fromJson(property, Spellbook.class);
                }

                @Override
                Object write(Player player) {
                    return player.spellbook;
                }
            },

            new PlayerJSONProperty("spell-book-copy") {
                @Override
                void read(Player player, JsonElement property) {
                    player.spellbook_copy = gson().fromJson(property, Spellbook.class);
                }

                @Override
                Object write(Player player) {
                    return player.spellbook_copy;
                }
            },

            new PlayerJSONProperty("auto-cast") {
                @Override
                void read(Player player, JsonElement property) {
                    player.setAutocast(gson().fromJson(property, CombatSpell.class));
                }

                @Override
                Object write(Player player) {
                    return player.autocast;
                }
            },

            new PlayerJSONProperty("brightness") {
                @Override
                void read(Player player, JsonElement property) {
                    player.settings.setBrightness(property.getAsInt());
                }

                @Override
                Object write(Player player) {
                    return player.settings.brightness;
                }
            },

            new PlayerJSONProperty("zoom") {
                @Override
                void read(Player player, JsonElement property) {
                    player.settings.setZoom(property.getAsInt(), true);
                }

                @Override
                Object write(Player player) {
                    return player.settings.zoom;
                }
            },

            new PlayerJSONProperty("private-chat") {
                @Override
                void read(Player player, JsonElement property) {
                    player.settings.privateChat = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.settings.privateChat;
                }
            },

            new PlayerJSONProperty("chat-effects") {
                @Override
                void read(Player player, JsonElement property) {
                    player.settings.chatEffects = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.settings.chatEffects;
                }
            },

            new PlayerJSONProperty("accept-aid") {
                @Override
                void read(Player player, JsonElement property) {
                    player.settings.acceptAid = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.settings.acceptAid;
                }
            },

            new PlayerJSONProperty("mouse-clicks") {
                @Override
                void read(Player player, JsonElement property) {
                    player.settings.mouse = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.settings.mouse;
                }
            },

            new PlayerJSONProperty("auto-retaliate") {
                @Override
                void read(Player player, JsonElement property) {
                    player.settings.autoRetaliate = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.settings.autoRetaliate;
                }
            },

            new PlayerJSONProperty("profanity-filter") {
                @Override
                void read(Player player, JsonElement property) {
                    player.settings.profanityFilter = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.settings.profanityFilter;
                }
            },

            new PlayerJSONProperty("camera-movement") {
                @Override
                void read(Player player, JsonElement property) {
                    player.settings.cameraMovement = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.settings.cameraMovement;
                }
            },

            new PlayerJSONProperty("experience-lock") {
                @Override
                void read(Player player, JsonElement property) {
                    player.settings.lockExperience = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.settings.lockExperience;
                }
            },

            new PlayerJSONProperty("welcome-screen") {
                @Override
                void read(Player player, JsonElement property) {
                    player.settings.welcomeScreen = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.settings.welcomeScreen;
                }
            },

            new PlayerJSONProperty("screenshot-kill") {
                @Override
                void read(Player player, JsonElement property) {
                    player.settings.screenshotKill = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.settings.screenshotKill;
                }
            },

            new PlayerJSONProperty("trivia-bot") {
                @Override
                void read(Player player, JsonElement property) {
                    player.settings.triviaBot = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.settings.triviaBot;
                }
            },

            new PlayerJSONProperty("drop-notification") {
                @Override
                void read(Player player, JsonElement property) {
                    player.settings.dropNotification = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.settings.dropNotification;
                }
            },

            new PlayerJSONProperty("yell-notification") {
                @Override
                void read(Player player, JsonElement property) {
                    player.settings.yell = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.settings.yell;
                }
            },

            new PlayerJSONProperty("untradeable-notification") {
                @Override
                void read(Player player, JsonElement property) {
                    player.settings.untradeableNotification = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.settings.untradeableNotification;
                }
            },

            new PlayerJSONProperty("esc-close") {
                @Override
                void read(Player player, JsonElement property) {
                    player.settings.ESC_CLOSE = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.settings.ESC_CLOSE;
                }
            },

            new PlayerJSONProperty("gloves-tier") {
                @Override
                void read(Player player, JsonElement property) {
                    player.glovesTier = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.glovesTier;
                }
            },

            new PlayerJSONProperty("experience-rate") {
                @Override
                void read(Player player, JsonElement property) {
                    player.experienceRate = property.getAsDouble();
                }

                @Override
                Object write(Player player) {
                    return player.experienceRate;
                }
            },

            new PlayerJSONProperty("experience-counter") {
                @Override
                void read(Player player, JsonElement property) {
                    player.skills.experienceCounter = property.getAsDouble();
                }

                @Override
                Object write(Player player) {
                    return player.skills.experienceCounter;
                }
            },

            new PlayerJSONProperty("experience-counter-copy") {
                @Override
                void read(Player player, JsonElement property) {
                    player.skills_copy.experienceCounter = property.getAsDouble();
                }

                @Override
                Object write(Player player) {
                    return player.skills_copy.experienceCounter;
                }
            },

            new PlayerJSONProperty("preset-death-open") {
                @Override
                void read(Player player, JsonElement property) {
                    player.presetManager.deathOpen = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.presetManager.deathOpen;
                }
            },

            new PlayerJSONProperty("preset-death-open") {
                @Override
                void read(Player player, JsonElement property) {
                    player.presetManager.deathOpen = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.presetManager.deathOpen;
                }
            },

            new PlayerJSONProperty("preset-automatic-deposit") {
                @Override
                void read(Player player, JsonElement property) {
                    player.presetManager.autoDeposit = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.presetManager.autoDeposit;
                }
            },

            new PlayerJSONProperty("hidden-brother") {
                @Override
                void read(Player player, JsonElement property) {
                    player.hiddenBrother = gson().fromJson(property, BrotherData.class);
                }

                @Override
                Object write(Player player) {
                    return player.hiddenBrother;
                }
            },

            new PlayerJSONProperty("brothers-kill-count") {
                @Override
                void read(Player player, JsonElement property) {
                    player.barrowsKillCount = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.barrowsKillCount;
                }
            },

            new PlayerJSONProperty("brothers-dead") {
                @Override
                void read(Player player, JsonElement property) {
                    player.barrowKills = gson().fromJson(property, boolean[].class);
                }

                @Override
                Object write(Player player) {
                    return player.barrowKills;
                }
            },

            new PlayerJSONProperty("vote-points") {
                @Override
                void read(Player player, JsonElement property) {
                    player.votePoints = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.votePoints;
                }
            },

            new PlayerJSONProperty("total-votes") {
                @Override
                void read(Player player, JsonElement property) {
                    player.totalVotes = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.totalVotes;
                }
            },

            new PlayerJSONProperty("completed-magearena") {
                @Override
                void read(Player player, JsonElement property) {
                    player.completedMageArena = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.completedMageArena;
                }
            },

            new PlayerJSONProperty("magearena-points") {
                @Override
                void read(Player player, JsonElement property) {
                    player.mageArenaPoints = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.mageArenaPoints;
                }
            },

            new PlayerJSONProperty("poison-immunity") {
                @Override
                void read(Player player, JsonElement property) {
                    player.getPoisonImmunity().set(property.getAsInt());
                }

                @Override
                Object write(Player player) {
                    return player.getPoisonImmunity().get();
                }
            },

            new PlayerJSONProperty("venom-immunity") {
                @Override
                void read(Player player, JsonElement property) {
                    player.getVenomImmunity().set(property.getAsInt());
                }

                @Override
                Object write(Player player) {
                    return player.getVenomImmunity().get();
                }
            },

            new PlayerJSONProperty("skull-timer") {
                @Override
                void read(Player player, JsonElement property) {
                    player.skulling.getSkullRemoveTask().setSkullTime(property.getAsInt());
                }

                @Override
                Object write(Player player) {
                    return player.skulling.getSkullRemoveTask().getSkullTime();
                }
            },

            new PlayerJSONProperty("teleblock-timer") {
                @Override
                void read(Player player, JsonElement property) {
                    player.teleblockTimer.set(property.getAsInt());
                }

                @Override
                Object write(Player player) {
                    return player.teleblockTimer.get();
                }
            },

            new PlayerJSONProperty("prestige-points") {
                @Override
                void read(Player player, JsonElement property) {
                    player.prestige.setPrestigePoint(property.getAsInt());
                }

                @Override
                Object write(Player player) {
                    return player.prestige.getPrestigePoint();
                }
            },

            new PlayerJSONProperty("prestige-total") {
                @Override
                void read(Player player, JsonElement property) {
                    player.prestige.totalPrestige = property.getAsInt();
                }

                @Override
                Object write(Player player) {
                    return player.prestige.totalPrestige;
                }
            },

            new PlayerJSONProperty("prestige-color") {
                @Override
                void read(Player player, JsonElement property) {
                    player.settings.prestigeColors = property.getAsBoolean();
                }

                @Override
                Object write(Player player) {
                    return player.settings.prestigeColors;
                }
            },

            new PlayerJSONProperty("prestiges") {
                @Override
                void read(Player player, JsonElement property) {
                    player.prestige.prestige = gson().fromJson(property, int[].class);
                }

                @Override
                Object write(Player player) {
                    return player.prestige.prestige;
                }
            },

            new PlayerJSONProperty("active-perks") {
                @Override
                void read(Player player, JsonElement property) {
                    player.prestige.activePerks = gson().fromJson(property, new TypeToken<Set<PrestigePerk>>() {
                    }.getType());
                }

                @Override
                Object write(Player player) {
                    return player.prestige.activePerks;
                }
            },

            new PlayerJSONProperty("achievements") {
                @Override
                void read(Player player, JsonElement property) {
                    HashMap<AchievementKey, Integer> achievements = gson().fromJson(property, new TypeToken<HashMap<AchievementKey, Integer>>() {
                    }.getType());
                    achievements.forEach((key, val) -> player.playerAchievements.put(key, val));
                }

                @Override
                Object write(Player player) {
                    return gson().toJsonTree(player.playerAchievements, new TypeToken<HashMap<AchievementKey, Integer>>() {
                    }.getType());
                }
            },

            new PlayerJSONProperty("last-killed") {
                @Override
                void read(Player player, JsonElement property) {
                    player.lastKilled = gson().fromJson(property, new TypeToken<Deque<String>>() {
                    }.getType());
                }

                @Override
                Object write(Player player) {
                    return player.lastKilled;
                }
            },

            new PlayerJSONProperty("favorite-teleport") {
                @Override
                void read(Player player, JsonElement property) {
                    player.favoriteTeleport = gson().fromJson(property, new TypeToken<List<Teleport>>() {
                    }.getType());
                }

                @Override
                Object write(Player player) {
                    return player.favoriteTeleport;
                }
            },

            new PlayerJSONProperty("unlocked-emotes") {
                @Override
                void read(Player player, JsonElement property) {
                    player.emoteUnlockable = gson().fromJson(property, new TypeToken<List<EmoteUnlockable>>() {
                    }.getType());
                }

                @Override
                Object write(Player player) {
                    return player.emoteUnlockable;
                }
            },

            new PlayerJSONProperty("public-chat-mode") {
                @Override
                void read(Player player, JsonElement property) {
                    player.relations.setPublicChatMode(PrivacyChatMode.valueOf(gson().fromJson(property, String.class).toUpperCase()), false);
                }

                @Override
                Object write(Player player) {
                    return player.relations.getPublicChatMode().name();
                }
            },

            new PlayerJSONProperty("private-chat-mode") {
                @Override
                void read(Player player, JsonElement property) {
                    player.relations.setPrivateChatMode(PrivacyChatMode.valueOf(gson().fromJson(property, String.class).toUpperCase()), true);
                }

                @Override
                Object write(Player player) {
                    return player.relations.getPrivateChatMode().name();
                }
            },

            new PlayerJSONProperty("clan-chat-mode") {
                @Override
                void read(Player player, JsonElement property) {
                    player.relations.setClanChatMode(PrivacyChatMode.valueOf(gson().fromJson(property, String.class).toUpperCase()), false);
                }

                @Override
                Object write(Player player) {
                    return player.relations.getClanChatMode().name();
                }
            },

            new PlayerJSONProperty("trade-chat-mode") {
                @Override
                void read(Player player, JsonElement property) {
                    player.relations.setTradeChatMode(PrivacyChatMode.valueOf(gson().fromJson(property, String.class).toUpperCase()), false);
                }

                @Override
                Object write(Player player) {
                    return player.relations.getTradeChatMode().name();
                }
            },

            new PlayerJSONProperty("friend-list") {
                @Override
                void read(Player player, JsonElement property) {
                    player.relations.getFriendList().addAll(gson().fromJson(property, new TypeToken<List<Long>>() {
                    }.getType()));
                }

                @Override
                Object write(Player player) {
                    return player.relations.getFriendList();
                }
            },

            new PlayerJSONProperty("ignore-list") {
                @Override
                void read(Player player, JsonElement property) {
                    player.relations.getIgnoreList().addAll(gson().fromJson(property, new TypeToken<List<Long>>() {
                    }.getType()));
                }

                @Override
                Object write(Player player) {
                    return player.relations.getIgnoreList();
                }
            },

            new PlayerJSONProperty("appearance") {
                @Override
                void read(Player player, JsonElement property) {
                    player.appearance = gson().fromJson(property, Appearance.class);
                }

                @Override
                Object write(Player player) {
                    return player.appearance;
                }
            },

            new PlayerJSONProperty("preset") {
                @Override
                void read(Player player, JsonElement property) {
                    Preset[] loaded = gson().fromJson(property, Preset[].class);
                    System.arraycopy(loaded, 0, player.presetManager.preset, 0, loaded.length);
                }

                @Override
                Object write(Player player) {
                    return player.presetManager.preset;
                }
            },

            new PlayerJSONProperty("activity-logger") {
                @Override
                void read(Player player, JsonElement property) {
                    player.loggedActivities = gson().fromJson(property, new TypeToken<HashMap<ActivityLog, Integer>>() {
                    }.getType());
                }

                @Override
                Object write(Player player) {
                    return gson().toJsonTree(player.loggedActivities, new TypeToken<HashMap<ActivityLog, Integer>>() {
                    }.getType());
                }
            },

            new PlayerJSONProperty("quick-prayers") {
                @Override
                void read(Player player, JsonElement property) {
                    player.quickPrayers = gson().fromJson(property, PrayerBook.class);
                }

                @Override
                Object write(Player player) {
                    return player.quickPrayers;
                }
            },

            new PlayerJSONProperty("locked-prayers") {
                @Override
                void read(Player player, JsonElement property) {
                    player.unlockedPrayers = gson().fromJson(property, new TypeToken<Set<Prayer>>() {
                    }.getType());
                }

                @Override
                Object write(Player player) {
                    return player.unlockedPrayers;
                }
            },

            new PlayerJSONProperty("slayer-points") {
                @Override
                void read(Player player, JsonElement property) {
                    player.slayer.setPoints(property.getAsInt());
                }

                @Override
                Object write(Player player) {
                    return player.slayer.getPoints();
                }
            },

            new PlayerJSONProperty("slayer-task") {
                @Override
                void read(Player player, JsonElement property) {
                    player.slayer.setTask(gson().fromJson(property, SlayerTask.class));
                }

                @Override
                Object write(Player player) {
                    return player.slayer.getTask();
                }
            },

            new PlayerJSONProperty("slayer-assigned") {
                @Override
                void read(Player player, JsonElement property) {
                    player.slayer.setAssigned(property.getAsInt());
                }

                @Override
                Object write(Player player) {
                    return player.slayer.getAssigned();
                }
            },

            new PlayerJSONProperty("slayer-amount") {
                @Override
                void read(Player player, JsonElement property) {
                    player.slayer.setAmount(property.getAsInt());
                }

                @Override
                Object write(Player player) {
                    return player.slayer.getAmount();
                }
            },

            new PlayerJSONProperty("slayer-total-assigned") {
                @Override
                void read(Player player, JsonElement property) {
                    player.slayer.setTotalAssigned(property.getAsInt());
                }

                @Override
                Object write(Player player) {
                    return player.slayer.getTotalAssigned();
                }
            },

            new PlayerJSONProperty("slayer-total-completed") {
                @Override
                void read(Player player, JsonElement property) {
                    player.slayer.setTotalCompleted(property.getAsInt());
                }

                @Override
                Object write(Player player) {
                    return player.slayer.getTotalCompleted();
                }
            },

            new PlayerJSONProperty("slayer-total-cancelled") {
                @Override
                void read(Player player, JsonElement property) {
                    player.slayer.setTotalCancelled(property.getAsInt());
                }

                @Override
                Object write(Player player) {
                    return player.slayer.getTotalCancelled();
                }
            },

            new PlayerJSONProperty("slayer-total-points") {
                @Override
                void read(Player player, JsonElement property) {
                    player.slayer.setTotalPoints(property.getAsInt());
                }

                @Override
                Object write(Player player) {
                    return player.slayer.getTotalPoints();
                }
            },

            new PlayerJSONProperty("slayer-blocked") {
                @Override
                void read(Player player, JsonElement property) {
                    player.slayer.setBlocked(gson().fromJson(property, new TypeToken<List<SlayerTask>>() {
                    }.getType()));
                }

                @Override
                Object write(Player player) {
                    return player.slayer.getBlocked();
                }
            },

            new PlayerJSONProperty("slayer-unlocked") {
                @Override
                void read(Player player, JsonElement property) {
                    player.slayer.setUnlocked(gson().fromJson(property, new TypeToken<Set<SlayerUnlockable>>() {
                    }.getType()));
                }

                @Override
                Object write(Player player) {
                    return player.slayer.getUnlocked();
                }
            },
            new PlayerJSONProperty("waypoints") {
                @Override
                void read(Player player, JsonElement property) {
                    player.waypoints = gson().fromJson(property, new TypeToken<HashMap<String, Position>>() {}.getType());
                }

                @Override
                Object write(Player player) {
                    return player.waypoints;
                }
            },
            new PlayerJSONProperty("presetOverrides") {
                @Override
                void read(Player player, JsonElement property) {
                    player.overrides.presets = gson().fromJson(property, new TypeToken<HashMap<String, Overrides.OverridePreset>>() {}.getType());
                }

                @Override
                Object write(Player player) {
                    return player.overrides.presets;
                }
            },
            new PlayerJSONProperty("currentOverrides") {
                @Override
                void read(Player player, JsonElement property) {
                    player.overrides.currentOverrides = gson().fromJson(property, new TypeToken<HashMap<Integer, Item>>() {}.getType());
                }

                @Override
                Object write(Player player) {
                    return player.overrides.currentOverrides;
                }
            },
            new PlayerJSONProperty("allOverrides") {
                @Override
                void read(Player player, JsonElement property) {
                    player.overrides.allOverrides = gson().fromJson(property, new TypeToken<List<Integer>>() {}.getType());
                }

                @Override
                Object write(Player player) {
                    return player.overrides.allOverrides;
                }
            },
            new PlayerJSONProperty("skills") {
                @Override
                void read(Player player, JsonElement property) {
                    player.skills.setSkills(gson().fromJson(property, Skill[].class));
                }

                @Override
                Object write(Player player) {
                    return player.skills.getSkills();
                }
            },

            new PlayerJSONProperty("skills-copy") {
                @Override
                void read(Player player, JsonElement property) {
                    player.skills_copy.setSkills(gson().fromJson(property, Skill[].class));
                }

                @Override
                Object write(Player player) {
                    return player.skills_copy.getSkills();
                }
            },

            new PlayerJSONProperty("inventory") {
                @Override
                void read(Player player, JsonElement property) {
                    player.inventory.set(gson().fromJson(property, Item[].class));
                }

                @Override
                Object write(Player player) {
                    return player.inventory.getItems();
                }
            },

            new PlayerJSONProperty("equipment") {
                @Override
                void read(Player player, JsonElement property) {
                    player.equipment.set(gson().fromJson(property, Item[].class));
                }

                @Override
                Object write(Player player) {
                    return player.equipment.getItems();
                }
            },

            new PlayerJSONProperty("inventory-copy") {
                @Override
                void read(Player player, JsonElement property) {
                    player.inventory_copy.set(gson().fromJson(property, Item[].class));
                }

                @Override
                Object write(Player player) {
                    return player.inventory_copy.getItems();
                }
            },

            new PlayerJSONProperty("equipment-copy") {
                @Override
                void read(Player player, JsonElement property) {
                    player.equipment_copy.set(gson().fromJson(property, Item[].class));
                }

                @Override
                Object write(Player player) {
                    return player.equipment_copy.getItems();
                }
            },

            new PlayerJSONProperty("tab-amounts") {
                @Override
                void read(Player player, JsonElement property) {
                    player.bank.tabAmounts = gson().fromJson(property, int[].class);
                }

                @Override
                Object write(Player player) {
                    return player.bank.tabAmounts;
                }
            },

            new PlayerJSONProperty("bank") {
                @Override
                void read(Player player, JsonElement property) {
                    player.bank.set(gson().fromJson(property, Item[].class));
                }

                @Override
                Object write(Player player) {
                    return player.bank.getItems();
                }
            },

            new PlayerJSONProperty("looting-bag") {
                @Override
                void read(Player player, JsonElement property) {
                    player.lootingBag.set(gson().fromJson(property, Item[].class));
                }

                @Override
                Object write(Player player) {
                    return player.lootingBag.getItems();
                }
            },

            new PlayerJSONProperty("lost-items") {
                @Override
                void read(Player player, JsonElement property) {
                    player.lostItems = (gson().fromJson(property, new TypeToken<LinkedList<Item>>() {
                    }.getType()));
                }

                @Override
                Object write(Player player) {
                    return player.lostItems;
                }
            },

            new PlayerJSONProperty("lost-untradables") {
                @Override
                void read(Player player, JsonElement property) {
                    player.lostUntradeables.set(gson().fromJson(property, Item[].class));
                }

                @Override
                Object write(Player player) {
                    return player.lostUntradeables.getItems();
                }
            },

            new PlayerJSONProperty("duel-rules") {
                @Override
                void read(Player player, JsonElement property) {
                    if (property.isJsonNull()) {
                        return;
                    }
                    Type ruleSet = new TypeToken<EnumSet<DuelRule>>() {
                    }.getType();


                    player.attributes.put("duel_rules", gson().fromJson(property, ruleSet));
                }

                @Override
                Object write(Player player) {
                    if (player.attributes.has("duel_rules")) {
                        return player.attributes.<String, EnumSet<DuelRule>>get("duel_rules");
                    }
                    return null;
                }
            },

            new PlayerJSONProperty("farming-patches") {
                @Override
                void read(Player player, JsonElement property) {
                    player.farming.fromJson(property.getAsJsonObject());
                }

                @Override
                Object write(Player player) {
                    return player.farming.toJson();
                }
            },

            new PlayerJSONProperty("daily-spell-book-swap") {
                @Override
                void read(Player player, JsonElement property) {
                    player.dailySpellBookSwap = gson().fromJson(property, DailySpellBookSwap.class);
                }

                @Override
                Object write(Player player) {
                    return player.dailySpellBookSwap;
                }
            },

            new PlayerJSONProperty("daily-slayer-task-skip") {
                @Override
                void read(Player player, JsonElement property) {
                    player.dailySlayerTaskSkip = gson().fromJson(property, DailySlayerTaskSkip.class);
                }

                @Override
                Object write(Player player) {
                    return player.dailySlayerTaskSkip;
                }
            },

            new PlayerJSONProperty("daily-slayer-task-teleport") {
                @Override
                void read(Player player, JsonElement property) {
                    player.dailySlayerTaskTeleport = gson().fromJson(property, DailySlayerTaskTeleport.class);
                }

                @Override
                Object write(Player player) {
                    return player.dailySlayerTaskTeleport;
                }
            },

            new PlayerJSONProperty("gem-bag") {
                @Override
                void read(Player player, JsonElement property) {
                    player.gemBag.container.set(gson().fromJson(property, Item[].class));
                }

                @Override
                Object write(Player player) {
                    return player.gemBag.container.getItems();
                }
            },

            new PlayerJSONProperty("coal-bag") {
                @Override
                void read(Player player, JsonElement property) {
                    player.coalBag.container.set(gson().fromJson(property, Item[].class));
                }

                @Override
                Object write(Player player) {
                    return player.coalBag.container.getItems();
                }
            },

            new PlayerJSONProperty("birdhouse-data") {
                @Override
                void read(Player player, JsonElement property) {
                    player.birdHouseData = gson().fromJson(property, new TypeToken<List<PlayerBirdHouseData>>() {}.getType());
                }

                @Override
                Object write(Player player) {
                    return player.birdHouseData;
                }
            },

            new PlayerJSONProperty("player-trading-post-history") {
                @Override
                void read(Player player, JsonElement property) {
                    Type type = new TypeToken<List<ItemHistory>>() {
                    }.getType();
                    player.tradingPost.getPlayersItemHistory().addAll(gson().fromJson(property, type));
                }

                @Override
                Object write(Player player) {
                    return player.tradingPost.getPlayersItemHistory();
                }
            }

    };

}
