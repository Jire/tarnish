package plugin.click.button;

import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.magic.Autocast;
import com.osroyale.game.world.entity.combat.weapon.WeaponInterface;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Area;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendSpecialEnabled;

import java.util.Arrays;
import java.util.Optional;

public class CombatButtonPlugin extends PluginContext {

    /** Array of all the auto retaliate button identifications. */
    private static final int[] AUTO_RETALIATE_BUTTON = {24010, 24017, 24025, 24033, 24041, 24048, 24068, 24115, 22845};

    @Override
    protected boolean onClick(Player player, int button) {

        if(button == 777) {
            if (Area.inWilderness(player)) {
                player.message("You cannot use this within the wilderness.");
                return true;
            }


            if (!player.isSpecialActivated()) {
                if (player.getSpecialPercentage().intValue() < player.getCombatSpecial().getAmount()) {
                    player.send(new SendMessage("You do not have enough special energy left!"));
                    return true;
                }

                player.send(new SendSpecialEnabled(1));
                player.getCombatSpecial().enable(player);
                return true;
            } else {
                player.send(new SendSpecialEnabled(0));
                player.getCombatSpecial().disable(player, true);
                return true;
            }
        }

        /* Auto retaliate. */
        if (Arrays.stream(AUTO_RETALIATE_BUTTON).anyMatch(b -> button == b)) {
            player.settings.autoRetaliate = !player.settings.autoRetaliate;
            return true;
        }

        if (Autocast.clickButton(player, button)) {
            return true;
        }

        Optional<FightType> fightButton = player.getWeapon().forFightButton(button);

        if (fightButton.isPresent()) {
            FightType fightType = fightButton.get();
            player.getCombat().setFightType(fightType);
            return true;
        }

        if (player.getCombatSpecial() != null && WeaponInterface.isSpecialButton(button)) {
            if (!player.isSpecialActivated()) {
                player.send(new SendSpecialEnabled(1));
                player.getCombatSpecial().enable(player);
            } else {
                player.send(new SendSpecialEnabled(0));
                player.getCombatSpecial().disable(player, true);
            }
            return true;
        }
        return false;
    }

}
