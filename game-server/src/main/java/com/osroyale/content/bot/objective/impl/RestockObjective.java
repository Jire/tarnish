package com.osroyale.content.bot.objective.impl;

import com.osroyale.content.bot.BotUtility;
import com.osroyale.content.bot.PlayerBot;
import com.osroyale.content.bot.botclass.BotClass;
import com.osroyale.content.bot.botclass.impl.*;
import com.osroyale.content.bot.objective.BotObjective;
import com.osroyale.content.bot.objective.BotObjectiveListener;
import com.osroyale.content.consume.FoodData;
import com.osroyale.content.consume.PotionData;
import com.osroyale.game.world.entity.combat.strategy.player.special.CombatSpecial;
import com.osroyale.game.world.items.Item;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;

import java.util.Optional;

public class RestockObjective implements BotObjectiveListener {

    /** The positions of all the bank locations for the bot to access. */
    private static final BotClass[] TYPES = {
        new WelfareRuneMelee(),
        new AGSRuneMelee(),
        new PureMelee(),
        new PureRangeMelee(),
        new ZerkerMelee()
    };

    @Override
    public void init(PlayerBot bot) {
        if (bot.botClass == null)
            bot.botClass = RandomUtils.random(TYPES);

        Item[] inventory = bot.botClass.inventory();
        bot.inventory.set(inventory);
        bot.equipment.manualWearAll(bot.botClass.equipment());

        for (Item item : inventory) {
            if (item == null) continue;
            if (FoodData.forId(item.getId()).isPresent()) {
                bot.foodRemaining++;
            }
            Optional<PotionData> potion = PotionData.forId(item.getId());
            if (!potion.isPresent() || potion.get() == PotionData.SUPER_RESTORE_POTIONS || potion.get() == PotionData.SARADOMIN_BREW) {
                continue;
            }
            bot.statBoostersRemaining++;
        }

        int[] skills = bot.botClass.skills();
        for (int skill = 0; skill < skills.length; skill++) {
            bot.skills.setMaxLevel(skill, skills[skill]);
        }
        bot.skills.setCombatLevel();

        CombatSpecial.restore(bot, 100);
        bot.schedule(2, () -> finish(bot));
    }

    @Override
    public void finish(PlayerBot bot) {
        bot.speak(Utility.randomElement(BotUtility.GEAR_UP_MESSAGES));
        BotObjective.WALK_TO_DITCH.init(bot);
    }

}
