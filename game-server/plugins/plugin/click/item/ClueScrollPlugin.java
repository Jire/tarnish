package plugin.click.item;

import com.osroyale.content.ActivityLog;
import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.discord.DiscordPlugin;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.util.Utility;
import com.osroyale.util.chance.Chance;

/**
 * Handles opening the clue scroll
 *
 * @author Daniel
 */
public class ClueScrollPlugin extends PluginContext {

    private static final String[] ITEMS_ANNOUNCED = {
            "3rd", "ranger", "gilded", "robin", "sandals", "wizard boots", "monk", "golden", "wooden", "team cape", "ornamnet", "royal"
    };

    private enum ClueLevel {
        EASY, MEDIUM, HARD, ELITE
    }

    private static final Item EASY_CLUE = new Item(2677);
    private static final Item MEDIUM_CLUE = new Item(2801);
    private static final Item HARD_CLUE = new Item(2722);
    private static final Item ELITE_CLUE = new Item(12073);
    private static final Chance<Item> EASY = new Chance<>();
    private static final Chance<Item> MEDIUM = new Chance<>();
    private static final Chance<Item> HARD = new Chance<>();
    private static final Chance<Item> ELITE = new Chance<>();

    private void handle(Player player, Item item, ClueLevel level) {
        if (player.inventory.getFreeSlots() < 6) {
            player.message("You need at least 6 free inventory space!");
            return;
        }

        Chance<Item> clue_rewards = getReward(level);

        if (clue_rewards == null)
            return;

        int length = 3 + Utility.random(2);
        Item[] rewards = new Item[length];


        for (int index = 0, count = 0; index < length; index++, count++) {
            Item clue_item = clue_rewards.next();

            if (!clue_item.isStackable() && clue_item.getAmount() > 1) {
                clue_item.setId(clue_item.getNotedId());
            }

            String name = clue_item.getName().toLowerCase();
            rewards[count] = clue_item;

            for (String announcement : ITEMS_ANNOUNCED) {
                if (name.contains(announcement)) {
                    World.sendMessage("<icon=12><col=5739B3> Tarnish: <col=" + player.right.getColor() + ">" + player.getName() + " </col>has received " + Utility.getAOrAn(name) + " <col=5739B3>" + name + " </col>from <col=5739B3>" + item.getName().toLowerCase() + "</col>.");
                    DiscordPlugin.sendSimpleMessage(player.getName() + " has just received " + Utility.getAOrAn(name) + " " + name + " from " + item.getName().toLowerCase() + "!");
                }
            }
        }

        player.inventory.remove(item);
        player.inventory.addAll(rewards);
        player.send(new SendItemOnInterface(6963, rewards));
        player.message("<col=EB2A3D>Your " + item.getName().toLowerCase() + " reward value is " + Utility.formatDigits(getValue(rewards)) + " coins!");
        AchievementHandler.activate(player, AchievementKey.CLUE_SCROLLS);
        player.interfaceManager.open(6960);

        if (level == ClueLevel.EASY) {
            player.activityLogger.add(ActivityLog.EASY_CLUE);
        } else if (level == ClueLevel.MEDIUM) {
            player.activityLogger.add(ActivityLog.MEDIUM_CLUE);
        } else if (level == ClueLevel.HARD) {
            player.activityLogger.add(ActivityLog.HARD_CLUE);
        } else if (level == ClueLevel.ELITE) {
            player.activityLogger.add(ActivityLog.ELITE_CLUE);
        }
    }

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        Item item = event.getItem();

        if (item.getId() == EASY_CLUE.getId()) {
            handle(player, item, ClueLevel.EASY);
            return true;
        }
        if (item.getId() == MEDIUM_CLUE.getId()) {
            handle(player, item, ClueLevel.MEDIUM);
            return true;
        }
        if (item.getId() == HARD_CLUE.getId()) {
            handle(player, item, ClueLevel.HARD);
            return true;
        }
        if (item.getId() == ELITE_CLUE.getId()) {
            handle(player, item, ClueLevel.ELITE);
            return true;
        }

        return false;
    }

    private int getValue(Item... items) {
        int value = 0;
        for (Item item : items) {
            value += item.getValue();
        }
        return value;
    }

    private Chance<Item> getReward(ClueLevel level) {
        switch (level) {
            case EASY:
                return EASY;
            case MEDIUM:
                return MEDIUM;
            case HARD:
                return HARD;
            case ELITE:
                return ELITE;
        }
        return null;
    }

    public static void declare() {
        EASY.add(200, new Item(1077)); // Black platelegs
        EASY.add(200, new Item(1089)); // Black plateskirt
        EASY.add(200, new Item(1107)); // Black chainbody
        EASY.add(200, new Item(1125)); // Black platebody
        EASY.add(200, new Item(1151)); // Black med helm
        EASY.add(200, new Item(1165)); // Black full helm
        EASY.add(200, new Item(1179)); // Black sq shield
        EASY.add(200, new Item(1195)); // Black kiteshield
        EASY.add(200, new Item(1217)); // Black dagger
        EASY.add(200, new Item(1283)); // Black sword
        EASY.add(200, new Item(1297)); // Black longsword
        EASY.add(200, new Item(1313)); // Black 2h sword
        EASY.add(200, new Item(1327)); // Black scimitar
        EASY.add(200, new Item(1341)); // Black warhammer
        EASY.add(200, new Item(1361)); // Black axe
        EASY.add(200, new Item(1367)); // Black battleaxe
        EASY.add(200, new Item(1426)); // Black mace
        EASY.add(200, new Item(8778)); // Oak plank
        EASY.add(200, new Item(849)); // Willow shortbow
        EASY.add(200, new Item(1169)); // Coif
        EASY.add(200, new Item(1095)); // Leather chaps
        EASY.add(200, new Item(1129)); // Leather body
        EASY.add(200, new Item(1131)); // Hardleather body
        EASY.add(200, new Item(1063)); // Leather vambraces
        EASY.add(200, new Item(1061)); // Leather boots
        EASY.add(200, new Item(1059)); // Leather gloves
        EASY.add(200, new Item(1167)); // Leather cowl
        EASY.add(200, new Item(329, 100)); // Salmon
        EASY.add(200, new Item(333, 100)); // Trout
        EASY.add(200, new Item(1438)); // Air talisman
        EASY.add(200, new Item(1440)); // Earth talisman
        EASY.add(200, new Item(1442)); // Fire talisman
        EASY.add(200, new Item(1444)); // Water talisman
        EASY.add(200, new Item(1446)); // Body talisman
        EASY.add(200, new Item(1448)); // Mind talisman
        EASY.add(200, new Item(1269)); // Steel pickaxe
        EASY.add(200, new Item(1452)); // Chaos talisman
        EASY.add(200, new Item(1454)); // Cosmic talisman
        EASY.add(200, new Item(1456)); // Death talisman
        EASY.add(200, new Item(1458)); // Law talisman
        EASY.add(200, new Item(1462)); // Nature talisman
        EASY.add(15, new Item(12205)); // Bronze platebody (g)
        EASY.add(15, new Item(12207)); // Bronze platelegs (g)
        EASY.add(15, new Item(12209)); // Bronze plateskirt (g)
        EASY.add(15, new Item(12211)); // Bronze full helm (g)
        EASY.add(15, new Item(12213)); // Bronze kiteshield (g)
        EASY.add(15, new Item(12215)); // Bronze platebody (t)
        EASY.add(15, new Item(12217)); // Bronze platelegs (t)
        EASY.add(15, new Item(12219)); // Bronze plateskirt (t)
        EASY.add(15, new Item(12221)); // Bronze full helm (t)
        EASY.add(15, new Item(12223)); // Bronze kiteshield (t)
        EASY.add(15, new Item(12225)); // Iron platebody (t)
        EASY.add(15, new Item(12227)); // Iron platelegs (t)
        EASY.add(15, new Item(12229)); // Iron plateskirt (t)
        EASY.add(15, new Item(12231)); // Iron full helm (t)
        EASY.add(15, new Item(12233)); // Iron kiteshield (t)
        EASY.add(15, new Item(12235)); // Iron platebody (g)
        EASY.add(15, new Item(12237)); // Iron platelegs (g)
        EASY.add(15, new Item(12239)); // Iron plateskirt (g)
        EASY.add(15, new Item(12241)); // Iron full helm (g)
        EASY.add(15, new Item(12243)); // Iron kiteshield (g)
        EASY.add(15, new Item(2583)); // Black platebody (t)
        EASY.add(15, new Item(2585)); // Black platelegs (t)
        EASY.add(15, new Item(2587)); // Black full helm (t)
        EASY.add(15, new Item(2589)); // Black kiteshield (t)
        EASY.add(15, new Item(2591)); // Black platebody (g)
        EASY.add(15, new Item(2593)); // Black platelegs (g)
        EASY.add(15, new Item(2595)); // Black full helm (g)
        EASY.add(15, new Item(2597)); // Black kiteshield (g)
        EASY.add(15, new Item(3472)); // Black plateskirt (t)
        EASY.add(15, new Item(3473)); // Black plateskirt (g)
        EASY.add(45, new Item(2635)); // Black beret
        EASY.add(45, new Item(2637)); // White beret
        EASY.add(45, new Item(12247)); // Red beret
        EASY.add(45, new Item(2633)); // Blue beret
        EASY.add(200, new Item(2631)); // Highwayman mask
        EASY.add(25, new Item(12245)); // Beanie
        EASY.add(15, new Item(7386)); // Blue skirt (g)
        EASY.add(15, new Item(7390)); // Blue wizard robe (g)
        EASY.add(15, new Item(7394)); // Blue wizard hat (g)
        EASY.add(15, new Item(7396)); // Blue wizard hat (t)
        EASY.add(15, new Item(7388)); // Blue skirt (t)
        EASY.add(15, new Item(7392)); // Blue wizard robe (t)
        EASY.add(15, new Item(12449)); // Black wizard robe (g)
        EASY.add(15, new Item(12453)); // Black wizard hat (g)
        EASY.add(15, new Item(12445)); // Black skirt (g)
        EASY.add(15, new Item(12447)); // Black skirt (t)
        EASY.add(15, new Item(12451)); // Black wizard robe (t)
        EASY.add(15, new Item(12455)); // Black wizard hat (t)
        EASY.add(15, new Item(7364)); // Studded body (t)
        EASY.add(15, new Item(7368)); // Studded chaps (t)
        EASY.add(15, new Item(7362)); // Studded body (g)
        EASY.add(15, new Item(7366)); // Studded chaps (g)
        EASY.add(175, new Item(7332)); // Black shield (h1)
        EASY.add(175, new Item(7338)); // Black shield (h2)
        EASY.add(175, new Item(7344)); // Black shield (h3)
        EASY.add(175, new Item(7350)); // Black shield (h4)
        EASY.add(175, new Item(7356)); // Black shield (h5)
        EASY.add(175, new Item(10306)); // Black helm (h1)
        EASY.add(175, new Item(10308)); // Black helm (h2)
        EASY.add(175, new Item(10310)); // Black helm (h3)
        EASY.add(175, new Item(10312)); // Black helm (h4)
        EASY.add(175, new Item(10314)); // Black helm (h5)
        EASY.add(25, new Item(10404)); // Red elegant shirt
        EASY.add(25, new Item(10406)); // Red elegant legs
        EASY.add(25, new Item(10424)); // Red elegant blouse
        EASY.add(25, new Item(10426)); // Red elegant skirt
        EASY.add(25, new Item(10412)); // Green elegant shirt
        EASY.add(25, new Item(10414)); // Green elegant legs
        EASY.add(25, new Item(10432)); // Green elegant blouse
        EASY.add(25, new Item(10434)); // Green elegant skirt
        EASY.add(25, new Item(10408)); // Blue elegant shirt
        EASY.add(25, new Item(10410)); // Blue elegant legs
        EASY.add(25, new Item(10428)); // Blue elegant blouse
        EASY.add(25, new Item(10430)); // Blue elegant skirt
        EASY.add(25, new Item(10316)); // Bob's red shirt
        EASY.add(25, new Item(10318)); // Bob's blue shirt
        EASY.add(25, new Item(10320)); // Bob's green shirt
        EASY.add(25, new Item(10322)); // Bob's black shirt
        EASY.add(25, new Item(10324)); // Bob's purple shirt
        EASY.add(27, new Item(10392)); // A powdered wig
        EASY.add(27, new Item(10394)); // Flared trousers
        EASY.add(27, new Item(10396)); // Pantaloons
        EASY.add(27, new Item(10398)); // Sleeping cap
        EASY.add(17, new Item(10366)); // Amulet of magic (t)
        EASY.add(10, new Item(12375)); // Black cane
        EASY.add(10, new Item(12297)); // Black pickaxe
        EASY.add(20, new Item(10458)); // Saradomin robe top
        EASY.add(20, new Item(10464)); // Saradomin robe legs
        EASY.add(20, new Item(10462)); // Guthix robe top
        EASY.add(20, new Item(10466)); // Guthix robe legs
        EASY.add(20, new Item(10460)); // Zamorak robe top
        EASY.add(20, new Item(10468)); // Zamorak robe legs
        EASY.add(20, new Item(12193)); // Ancient robe top
        EASY.add(20, new Item(12195)); // Ancient robe legs
        EASY.add(20, new Item(12253)); // Armadyl robe top
        EASY.add(20, new Item(12255)); // Armadyl robe legs
        EASY.add(20, new Item(12265)); // Bandos robe top
        EASY.add(20, new Item(12267)); // Bandos robe legs
        EASY.add(.99, new Item(20217)); // Team Cape i
        EASY.add(.99, new Item(20211)); // Team Cape zero
        EASY.add(.99, new Item(20214)); // Team Cape x
        EASY.add(5, new Item(12249)); // Imp Mask
        EASY.add(5, new Item(12251)); // Goblin Mask
        EASY.add(.99, new Item(20166)); // Wooden Shield (g)
        EASY.add(.99, new Item(20205)); // Golden Chef's Hat
        EASY.add(.99, new Item(20208)); // Golden Apron
        EASY.add(.99, new Item(20199)); // Monk's Robe Top (g)
        EASY.add(.99, new Item(20202)); // Monk's Robe (g
        EASY.add(15, new Item(20164)); // Large Spade

        MEDIUM.add(200, new Item(1073)); // Adamant platelegs
        MEDIUM.add(200, new Item(1091)); // Adamant plateskirt
        MEDIUM.add(200, new Item(1111)); // Adamant chainbody
        MEDIUM.add(200, new Item(1123)); // Adamant platebody
        MEDIUM.add(200, new Item(1145)); // Adamant med helm
        MEDIUM.add(200, new Item(1161)); // Adamant full helm
        MEDIUM.add(200, new Item(1183)); // Adamant sq shield
        MEDIUM.add(200, new Item(1199)); // Adamant kiteshield
        MEDIUM.add(200, new Item(1211)); // Adamant dagger
        MEDIUM.add(200, new Item(1287)); // Adamant sword
        MEDIUM.add(200, new Item(1301)); // Adamant longsword
        MEDIUM.add(200, new Item(1317)); // Adamant 2h sword
        MEDIUM.add(200, new Item(1331)); // Adamant scimitar
        MEDIUM.add(200, new Item(1345)); // Adamant warhammer
        MEDIUM.add(200, new Item(1357)); // Adamant axe
        MEDIUM.add(200, new Item(1371)); // Adamant battleaxe
        MEDIUM.add(200, new Item(1430)); // Adamant mace
        MEDIUM.add(200, new Item(1271)); // Adamant pickaxe
        MEDIUM.add(200, new Item(9183)); // Adamant crossbow
        MEDIUM.add(200, new Item(4823, 15)); // Adamantite nails
        MEDIUM.add(200, new Item(1393)); // Fire battlestaff
        MEDIUM.add(200, new Item(857)); // Yew shortbow
        MEDIUM.add(200, new Item(8780, 100)); // Teak plank
        MEDIUM.add(200, new Item(373, 100)); // Swordfish
        MEDIUM.add(200, new Item(379, 100)); // Lobster
        MEDIUM.add(200, new Item(1099)); // Green d'hide chaps
        MEDIUM.add(200, new Item(1135)); // Green d'hide body
        MEDIUM.add(15, new Item(12293)); // Mithril full helm (t)
        MEDIUM.add(15, new Item(12287)); // Mithril platebody (t)
        MEDIUM.add(15, new Item(12289)); // Mithril platelegs (t)
        MEDIUM.add(15, new Item(12291)); // Mithril kiteshield (t)
        MEDIUM.add(15, new Item(12295)); // Mithril plateskirt (t)
        MEDIUM.add(15, new Item(12283)); // Mithril full helm (g)
        MEDIUM.add(15, new Item(12277)); // Mithril platebody (g)
        MEDIUM.add(15, new Item(12285)); // Mithril plateskirt (g)
        MEDIUM.add(15, new Item(12279)); // Mithril platelegs (g)
        MEDIUM.add(15, new Item(12281)); // Mithril kiteshield (g)
        MEDIUM.add(15, new Item(2605)); // Adamant full helm (t)
        MEDIUM.add(15, new Item(3474)); // Adamant plateskirt (t)
        MEDIUM.add(15, new Item(2603)); // Adamant kiteshield (t)
        MEDIUM.add(15, new Item(2599)); // Adamant platebody (t)
        MEDIUM.add(15, new Item(2601)); // Adamant platelegs (t)
        MEDIUM.add(15, new Item(2607)); // Adamant platebody (g)
        MEDIUM.add(15, new Item(2609)); // Adamant platelegs (g)
        MEDIUM.add(15, new Item(2611)); // Adamant kiteshield (g)
        MEDIUM.add(15, new Item(2613)); // Adamant full helm (g)
        MEDIUM.add(15, new Item(3475)); // Adamant plateskirt (g)
        MEDIUM.add(.99, new Item(2577)); // Ranger boots
        MEDIUM.add(.99, new Item(12598)); // Holy sandals
        MEDIUM.add(.99, new Item(2579)); // Wizard boots
        MEDIUM.add(100, new Item(2647)); // Black headband
        MEDIUM.add(100, new Item(2645)); // Red headband
        MEDIUM.add(100, new Item(2649)); // Brown headband
        MEDIUM.add(100, new Item(12305)); // Pink headband
        MEDIUM.add(100, new Item(12307)); // Green headband
        MEDIUM.add(100, new Item(12301)); // Blue headband
        MEDIUM.add(100, new Item(12299)); // White headband
        MEDIUM.add(100, new Item(12303)); // Gold headband
        MEDIUM.add(35, new Item(7319)); // Red boater
        MEDIUM.add(35, new Item(7321)); // Orange boater
        MEDIUM.add(35, new Item(7323)); // Green boater
        MEDIUM.add(35, new Item(7325)); // Blue boater
        MEDIUM.add(35, new Item(7327)); // Black boater
        MEDIUM.add(35, new Item(12309)); // Pink boater
        MEDIUM.add(35, new Item(12311)); // Purple boater
        MEDIUM.add(35, new Item(12313)); // White boater
        MEDIUM.add(15, new Item(7380)); // Green d'hide chaps (t)
        MEDIUM.add(15, new Item(7372)); // Green d'hide body (t)
        MEDIUM.add(15, new Item(7370)); // Green d'hide body (g)
        MEDIUM.add(15, new Item(7378)); // Green d'hide chaps (g)
        MEDIUM.add(100, new Item(7334)); // Adamant shield (h1)
        MEDIUM.add(100, new Item(7340)); // Adamant shield (h2)
        MEDIUM.add(100, new Item(7346)); // Adamant shield (h3)
        MEDIUM.add(100, new Item(7352)); // Adamant shield (h4)
        MEDIUM.add(100, new Item(7358)); // Adamant shield (h5)
        MEDIUM.add(100, new Item(10296)); // Adamant helm (h1)
        MEDIUM.add(100, new Item(10298)); // Adamant helm (h2)
        MEDIUM.add(100, new Item(10300)); // Adamant helm (h3)
        MEDIUM.add(100, new Item(10302)); // Adamant helm (h4)
        MEDIUM.add(100, new Item(10304)); // Adamant helm (h5)
        MEDIUM.add(25, new Item(10400)); // Black elegant shirt
        MEDIUM.add(25, new Item(10402)); // Black elegant legs
        MEDIUM.add(25, new Item(10416)); // Purple elegant shirt
        MEDIUM.add(25, new Item(10418)); // Purple elegant legs
        MEDIUM.add(25, new Item(12315)); // Pink elegant shirt
        MEDIUM.add(25, new Item(12317)); // Pink elegant legs
        MEDIUM.add(25, new Item(12339)); // Pink elegant blouse
        MEDIUM.add(25, new Item(12341)); // Pink elegant skirt
        MEDIUM.add(25, new Item(12343)); // Gold elegant blouse
        MEDIUM.add(25, new Item(12345)); // Gold elegant skirt
        MEDIUM.add(25, new Item(12347)); // Gold elegant shirt
        MEDIUM.add(25, new Item(12349)); // Gold elegant legs
        MEDIUM.add(25, new Item(10436)); // Purple elegant blouse
        MEDIUM.add(25, new Item(10438)); // Purple elegant skirt
        MEDIUM.add(25, new Item(10420)); // White elegant blouse
        MEDIUM.add(25, new Item(10422)); // White elegant skirt
        MEDIUM.add(10, new Item(12377)); // Adamant cane
        MEDIUM.add(35, new Item(10364)); // Strength amulet (t)
        MEDIUM.add(20, new Item(10446)); // Saradomin cloak
        MEDIUM.add(20, new Item(10448)); // Guthix cloak
        MEDIUM.add(20, new Item(10450)); // Zamorak cloak
        MEDIUM.add(20, new Item(12197)); // Ancient cloak
        MEDIUM.add(20, new Item(12261)); // Armadyl cloak
        MEDIUM.add(20, new Item(12273)); // Bandos cloak
        MEDIUM.add(20, new Item(10452)); // Saradomin mitre
        MEDIUM.add(20, new Item(10454)); // Guthix mitre
        MEDIUM.add(20, new Item(10456)); // Zamorak mitre
        MEDIUM.add(20, new Item(12203)); // Ancient mitre
        MEDIUM.add(20, new Item(12259)); // Armadyl mitre
        MEDIUM.add(20, new Item(12271)); // Bandos mitre
        MEDIUM.add(35, new Item(12361)); // Cat mask
        MEDIUM.add(35, new Item(12428)); // Penguin mask
        MEDIUM.add(35, new Item(12359)); // Leprechaun hat
        MEDIUM.add(35, new Item(12319)); // Crier hat
        MEDIUM.add(20, new Item(12269)); // Bandos stole
        MEDIUM.add(20, new Item(12257)); // Armadyl stole
        MEDIUM.add(20, new Item(12201)); // Ancient stole
        MEDIUM.add(20, new Item(12275)); // Bandos crozier
        MEDIUM.add(20, new Item(12263)); // Armadyl crozier
        MEDIUM.add(20, new Item(12199)); // Ancient crozier
        MEDIUM.add(75, new Item(20260)); // Piscarilius Banner
        MEDIUM.add(75, new Item(20257)); // Lovakengj Banner
        MEDIUM.add(75, new Item(20251)); // Arceuus Banner
        MEDIUM.add(75, new Item(20263)); // Shayzien Banner
        MEDIUM.add(75, new Item(20254)); // Hosidius Banner
        MEDIUM.add(5, new Item(20269)); // White Unicorn Mask
        MEDIUM.add(5, new Item(20266)); // Black Unicorn Mask
        MEDIUM.add(5, new Item(20246)); // Black Leprechaun hat
        MEDIUM.add(5, new Item(20240)); // Crier's Bell
        MEDIUM.add(5, new Item(20243)); // Crier's Coat
        MEDIUM.add(5, new Item(20275)); // Gnomish Firelighter
        MEDIUM.add(5, new Item(20249)); // Clueless Scroll
        MEDIUM.add(1, new Item(20272)); // Cabbage Round Shield

        HARD.add(200, new Item(1079)); // Rune platelegs
        HARD.add(200, new Item(1093)); // Rune plateskirt
        HARD.add(200, new Item(1113)); // Rune chainbody
        HARD.add(200, new Item(1127)); // Rune platebody
        HARD.add(200, new Item(1147)); // Rune med helm
        HARD.add(200, new Item(1163)); // Rune full helm
        HARD.add(200, new Item(1185)); // Rune sq shield
        HARD.add(200, new Item(1201)); // Rune kiteshield
        HARD.add(200, new Item(1213)); // Rune dagger
        HARD.add(200, new Item(1289)); // Rune sword
        HARD.add(200, new Item(1303)); // Rune longsword
        HARD.add(200, new Item(1319)); // Rune 2h sword
        HARD.add(200, new Item(1333)); // Rune scimitar
        HARD.add(200, new Item(1347)); // Rune warhammer
        HARD.add(200, new Item(1359)); // Rune axe
        HARD.add(200, new Item(1373)); // Rune battleaxe
        HARD.add(200, new Item(1432)); // Rune mace
        HARD.add(200, new Item(859)); // Magic longbow
        HARD.add(200, new Item(861)); // Magic shortbow
        HARD.add(200, new Item(2497)); // Black d'hide chaps
        HARD.add(200, new Item(2503)); // Black d'hide body
        HARD.add(200, new Item(2491)); // Black d'hide vamb
        HARD.add(200, new Item(385, 100)); // Shark
        HARD.add(200, new Item(379, 100)); // Lobster
        HARD.add(5, new Item(12526)); // Fury ornament kit
        HARD.add(5, new Item(12532)); // Dragon sq shield ornament kit
        HARD.add(5, new Item(12534)); // Dragon chainbody ornament kit
        HARD.add(5, new Item(12536)); // Dragon plate/skirt ornament kit
        HARD.add(5, new Item(12538)); // Dragon full helm ornament kit
        HARD.add(5, new Item(12528)); // Dark infinity colour kit
        HARD.add(5, new Item(12530)); // Light infinity colour kit
        HARD.add(15, new Item(12381)); // Black d'hide body (g)
        HARD.add(15, new Item(12383)); // Black d'hide chaps (g)
        HARD.add(15, new Item(12385)); // Black d'hide body (t)
        HARD.add(15, new Item(12387)); // Black d'hide chaps (t)
        HARD.add(15, new Item(2615)); // Rune platebody (g)
        HARD.add(15, new Item(2617)); // Rune platelegs (g)
        HARD.add(15, new Item(2619)); // Rune full helm (g)
        HARD.add(15, new Item(2621)); // Rune kiteshield (g)
        HARD.add(15, new Item(2623)); // Rune platebody (t)
        HARD.add(15, new Item(2625)); // Rune platelegs (t)
        HARD.add(15, new Item(2627)); // Rune full helm (t)
        HARD.add(15, new Item(2629)); // Rune kiteshield (t)
        HARD.add(15, new Item(3476)); // Rune plateskirt (g)
        HARD.add(15, new Item(3477)); // Rune plateskirt (t)
        HARD.add(65, new Item(2669)); // Guthix platebody
        HARD.add(65, new Item(2671)); // Guthix platelegs
        HARD.add(65, new Item(2673)); // Guthix full helm
        HARD.add(65, new Item(2675)); // Guthix kiteshield
        HARD.add(65, new Item(3480)); // Guthix plateskirt
        HARD.add(65, new Item(2653)); // Zamorak platebody
        HARD.add(65, new Item(2655)); // Zamorak platelegs
        HARD.add(65, new Item(2657)); // Zamorak full helm
        HARD.add(65, new Item(2659)); // Zamorak kiteshield
        HARD.add(65, new Item(3478)); // Zamorak plateskirt
        HARD.add(65, new Item(2661)); // Saradomin platebody
        HARD.add(65, new Item(2663)); // Saradomin platelegs
        HARD.add(65, new Item(2665)); // Saradomin full helm
        HARD.add(65, new Item(2667)); // Saradomin kiteshield
        HARD.add(65, new Item(3479)); // Saradomin plateskirt
        HARD.add(.99, new Item(3481)); // Gilded platebody
        HARD.add(.99, new Item(3483)); // Gilded platelegs
        HARD.add(.99, new Item(3485)); // Gilded plateskirt
        HARD.add(.99, new Item(3486)); // Gilded full helm
        HARD.add(.99, new Item(3488)); // Gilded kiteshield
        HARD.add(.99, new Item(12389)); // Gilded scimitar
        HARD.add(.99, new Item(12391)); // Gilded boots
        HARD.add(100, new Item(7336)); // Rune shield (h1)
        HARD.add(100, new Item(7342)); // Rune shield (h2)
        HARD.add(100, new Item(7348)); // Rune shield (h3)
        HARD.add(100, new Item(7354)); // Rune shield (h4)
        HARD.add(100, new Item(7360)); // Rune shield (h5)
        HARD.add(100, new Item(10286)); // Rune helm (h1)
        HARD.add(100, new Item(10288)); // Rune helm (h2)
        HARD.add(100, new Item(10290)); // Rune helm (h3)
        HARD.add(100, new Item(10292)); // Rune helm (h4)
        HARD.add(100, new Item(10294)); // Rune helm (h5)
        HARD.add(65, new Item(7374)); // Blue d'hide body (g)
        HARD.add(65, new Item(7376)); // Blue d'hide body (t)
        HARD.add(65, new Item(7382)); // Blue d'hide chaps (g)
        HARD.add(65, new Item(7384)); // Blue d'hide chaps (t)
        HARD.add(65, new Item(7398)); // Enchanted robe
        HARD.add(65, new Item(7399)); // Enchanted top
        HARD.add(65, new Item(7400)); // Enchanted hat
        HARD.add(1, new Item(2581)); // Robin hood hat
        HARD.add(35, new Item(2639)); // Tan cavalier
        HARD.add(35, new Item(2641)); // Dark cavalier
        HARD.add(35, new Item(2643)); // Black cavalier
        HARD.add(35, new Item(12321)); // White cavalier
        HARD.add(35, new Item(12323)); // Red cavalier
        HARD.add(35, new Item(12325)); // Navy cavalier
        HARD.add(45, new Item(2651)); // Pirate's hat
        HARD.add(.1, new Item(10346)); // 3rd age platelegs
        HARD.add(.1, new Item(10348)); // 3rd age platebody
        HARD.add(.1, new Item(10350)); // 3rd age full helmet
        HARD.add(.1, new Item(10352)); // 3rd age kiteshield
        HARD.add(.1, new Item(10330)); // 3rd age range top
        HARD.add(.1, new Item(10332)); // 3rd age range legs
        HARD.add(.1, new Item(10334)); // 3rd age range coif
        HARD.add(.1, new Item(10336)); // 3rd age vambraces
        HARD.add(.1, new Item(10338)); // 3rd age robe top
        HARD.add(.1, new Item(10340)); // 3rd age robe
        HARD.add(.1, new Item(10342)); // 3rd age mage hat
        HARD.add(.1, new Item(10344)); // 3rd age amulet
        HARD.add(25, new Item(10382)); // Guthix coif
        HARD.add(25, new Item(10380)); // Guthix chaps
        HARD.add(25, new Item(10378)); // Guthix dragonhide
        HARD.add(25, new Item(10376)); // Guthix bracers
        HARD.add(25, new Item(10390)); // Saradomin coif
        HARD.add(25, new Item(10388)); // Saradomin chaps
        HARD.add(25, new Item(10386)); // Saradomin d'hide
        HARD.add(25, new Item(10384)); // Saradomin bracers
        HARD.add(25, new Item(10374)); // Zamorak coif
        HARD.add(25, new Item(10372)); // Zamorak chaps
        HARD.add(25, new Item(10370)); // Zamorak d'hide
        HARD.add(25, new Item(10368)); // Zamorak bracers
        HARD.add(25, new Item(12512)); // Armadyl coif
        HARD.add(25, new Item(12510)); // Armadyl chaps
        HARD.add(25, new Item(12508)); // Armadyl d'hide
        HARD.add(25, new Item(12506)); // Armadyl bracers
        HARD.add(25, new Item(12496)); // Ancient coif
        HARD.add(25, new Item(12494)); // Ancient chaps
        HARD.add(25, new Item(12492)); // Ancient d'hide
        HARD.add(25, new Item(12490)); // Ancient bracers
        HARD.add(25, new Item(12504)); // Bandos coif
        HARD.add(25, new Item(12502)); // Bandos chaps
        HARD.add(25, new Item(12500)); // Bandos d'hide
        HARD.add(25, new Item(12498)); // Bandos bracers
        HARD.add(47, new Item(12518)); // Green dragon mask
        HARD.add(47, new Item(12520)); // Blue dragon mask
        HARD.add(47, new Item(12522)); // Red dragon mask
        HARD.add(47, new Item(12524)); // Black dragon mask
        HARD.add(47, new Item(12516)); // Pith helmet
        HARD.add(20, new Item(10474)); // Zamorak stole
        HARD.add(20, new Item(10472)); // Guthix stole
        HARD.add(20, new Item(10470)); // Saradomin stole
        HARD.add(20, new Item(10444)); // Zamorak crozier
        HARD.add(20, new Item(10442)); // Guthix crozier
        HARD.add(20, new Item(10440)); // Saradomin crozier
        HARD.add(10, new Item(12379)); // Rune cane
        HARD.add(25, new Item(19924)); // Bandos D'Hide Boots
        HARD.add(25, new Item(19930)); // Armadyl D'Hide Boots
        HARD.add(25, new Item(19933)); // Saradomin D'Hide Boots
        HARD.add(25, new Item(19936)); // Zamorak D'Hide Boots
        HARD.add(25, new Item(19927)); // Guthix D'Hide Boots
        HARD.add(15, new Item(12514)); // Explorer Backpack
        HARD.add(75, new Item(19912)); // Zombie Head
        HARD.add(75, new Item(19915)); // Cyclops Head
        HARD.add(1, new Item(19918)); // Nunchaku

        ELITE.add(175, new Item(1163)); // Rune Full Helm
        ELITE.add(175, new Item(1147)); // Rune Med Helm
        ELITE.add(175, new Item(1127)); // Rune Platebody
        ELITE.add(175, new Item(1113)); // Rune Chainbody
        ELITE.add(175, new Item(1093)); // Rune Plateskirt
        ELITE.add(175, new Item(1079)); // Rune Platelegs
        ELITE.add(175, new Item(1201)); // Rune Kiteshield
        ELITE.add(175, new Item(1185)); // Rune Sq Shield
        ELITE.add(175, new Item(4131)); // Rune Boots
        ELITE.add(175, new Item(1319)); // Rune 2h Sword
        ELITE.add(175, new Item(1303)); // Rune Long Sword
        ELITE.add(175, new Item(1333)); // Rune Scimitar
        ELITE.add(175, new Item(1347)); // Rune Warhammer
        ELITE.add(175, new Item(1373)); // Rune Battleaxe
        ELITE.add(175, new Item(1432)); // Rune Mace
        ELITE.add(200, new Item(3025, 25)); // Super Restore (4)
        ELITE.add(200, new Item(2445, 25)); // Ranging Potion (4)
        ELITE.add(200, new Item(6686, 25)); // Saradomin Brew (4)
        ELITE.add(200, new Item(11952, 25)); // Extended Antifire (4)
        ELITE.add(200, new Item(8783, 100)); // Mahogany Planks
        ELITE.add(200, new Item(8781, 100)); // Teak Planks
        ELITE.add(200, new Item(1392, 25)); // Battlestaves
        ELITE.add(200, new Item(7219, 15)); // Summer Pies
        ELITE.add(200, new Item(7061, 50)); // Tuna Potato
        ELITE.add(200, new Item(985)); // Tooth Half of Key
        ELITE.add(200, new Item(987)); // Loop Half of Key
        ELITE.add(200, new Item(565, 250)); // Blood Rune
        ELITE.add(200, new Item(566, 250)); // Soul Rune
        ELITE.add(200, new Item(560, 250)); // Death Rune
        ELITE.add(200, new Item(563, 250)); // Law Rune
        ELITE.add(75, new Item(1305)); // Dragon Longsword
        ELITE.add(75, new Item(1215)); // Dragon Dagger
        ELITE.add(75, new Item(1434)); // Dragon Mace
        ELITE.add(75, new Item(1377)); // Dragon Battleaxe
        ELITE.add(75, new Item(4587)); // Dragon Scimitar
        ELITE.add(50, new Item(9194, 50)); // Onyx Bolt Tips
        ELITE.add(125, new Item(5317)); // Spirit Seed
        ELITE.add(125, new Item(5316)); // Magic Seed
        ELITE.add(125, new Item(5315)); // Yew Seed
        ELITE.add(25, new Item(12526)); // Fury ornament kit
        ELITE.add(25, new Item(12528)); // Dark infinity colour kit
        ELITE.add(25, new Item(12530)); // Light infinity colour kit
        ELITE.add(25, new Item(12532)); // Dragon sq shield ornament kit
        ELITE.add(25, new Item(12534)); // Dragon chainbody ornament kit
        ELITE.add(25, new Item(12536)); // Dragon legs/skirt ornament kit
        ELITE.add(25, new Item(12538)); // Dragon full helm ornament kit
        ELITE.add(25, new Item(12335)); // Briefcase
        ELITE.add(25, new Item(12337)); // Sagacious spectacles
        ELITE.add(25, new Item(12339)); // Pink elegant blouse
        ELITE.add(25, new Item(12341)); // Pink elegant skirt
        ELITE.add(25, new Item(12343)); // Gold elegant blouse
        ELITE.add(25, new Item(12345)); // Gold elegant skirt
        ELITE.add(25, new Item(12347)); // Gold elegant shirt
        ELITE.add(25, new Item(12349)); // Gold elegant legs
        ELITE.add(25, new Item(12351)); // Musketeer hat
        ELITE.add(25, new Item(12353)); // Monocle
        ELITE.add(25, new Item(12355)); // Big pirate hat
        ELITE.add(10, new Item(12357)); // Katana
        ELITE.add(25, new Item(12359)); // Leprechaun hat
        ELITE.add(25, new Item(12361)); // Cat mask
        ELITE.add(25, new Item(12363)); // Bronze dragon mask
        ELITE.add(25, new Item(12365)); // Iron dragon mask
        ELITE.add(25, new Item(12367)); // Steel dragon mask
        ELITE.add(25, new Item(12369)); // Mithril dragon mask
        ELITE.add(25, new Item(12371)); // Lava dragon mask
        ELITE.add(25, new Item(12373)); // Dragon cane
        ELITE.add(25, new Item(12540)); // Deerstalker
        ELITE.add(.99, new Item(12391)); // Gilded boots
        ELITE.add(.99, new Item(12389)); // Glided scimitar
        ELITE.add(25, new Item(12381)); // Black d'hide body (g)
        ELITE.add(25, new Item(12383)); // Black d'hide chaps (g)
        ELITE.add(25, new Item(12385)); // Black d'hide body (t)
        ELITE.add(25, new Item(12387)); // Black d'hide chaps (t)
        ELITE.add(4, new Item(12393)); // Royal gown top
        ELITE.add(4, new Item(12395)); // Royal gown bottom
        ELITE.add(4, new Item(12397)); // Royal crown
        ELITE.add(1, new Item(12389)); // Ranger's tunic
        ELITE.add(3, new Item(19943)); // Arceuus house scarf
        ELITE.add(3, new Item(19946)); // Hosidius house scarf
        ELITE.add(3, new Item(19949)); // Lovakengj house scarf
        ELITE.add(3, new Item(19952)); // Piscarilius house scarf
        ELITE.add(3, new Item(19955)); // Shayzien house scarf
        ELITE.add(8, new Item(19958)); // Dark tuxedo jacket
        ELITE.add(8, new Item(19961)); // Dark tuxedo cuffs
        ELITE.add(8, new Item(19964)); // Dark trousers
        ELITE.add(8, new Item(19967)); // Dark tuxedo shoes
        ELITE.add(8, new Item(19970)); // Dark bow tie
        ELITE.add(8, new Item(19973)); // Light tuxedo jacket
        ELITE.add(8, new Item(19976)); // Light tuxedo cuffs
        ELITE.add(8, new Item(19979)); // Light trousers
        ELITE.add(8, new Item(19982)); // Light tuxedo shoes
        ELITE.add(8, new Item(19985)); // Light bow tie
        ELITE.add(5, new Item(19988)); // Blacksmith's helm
        ELITE.add(5, new Item(19991)); // Bucket helm
        ELITE.add(5, new Item(19994)); // Ranger gloves
        ELITE.add(5, new Item(19997)); // Holy wraps
        ELITE.add(5, new Item(19941)); // Heavy casket
        ELITE.add(.01, new Item(12422)); // 3rd age wand
        ELITE.add(.01, new Item(12424)); // 3rd age bow
        ELITE.add(.01, new Item(12426)); // 3rd age longsword
        ELITE.add(.01, new Item(12437)); // 3rd age cloak
        ELITE.add(.001, new Item(20011)); // 3rd age axe
        ELITE.add(.001, new Item(20014)); // 3rd age pickaxe
    }
}
