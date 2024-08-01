package plugin.click.item;

import com.osroyale.content.activity.Activity;
import com.osroyale.content.consume.PotionData;
import com.osroyale.game.Animation;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;

import java.util.Optional;

public class DrinkPotionPlugin extends PluginContext {

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        Optional<PotionData> potion = PotionData.forId(event.getItem().getId());

        if (!potion.isPresent() || player.isDead() || !player.potionDelay.elapsed(1600)) {
            return false;
        }

        if (Activity.evaluate(player, it -> !it.canDrinkPotions(player))) {
            return true;
        }

        if (!potion.get().canDrink(player)) {
            return true;
        }

        if (!player.interfaceManager.isClear()) {
            player.interfaceManager.close(false);
        }

        player.animate(new Animation(829, UpdatePriority.LOW));
        player.potionDelay.reset();

        Item replace = PotionData.getReplacementItem(event.getItem());

        if (replace.getId() == 229) {
            player.inventory.remove(event.getItem());
        } else {
            player.inventory.replace(event.getItem().getId(), replace.getId(), event.getSlot(), true);
        }
        potion.get().onEffect(player);
        return true;
    }


}
