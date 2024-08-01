package plugin.click.button;

import com.osroyale.content.skill.guides.GuideData;
import com.osroyale.content.skill.guides.SkillGuides;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendRemoveInterface;

public class SkillGuideButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(final Player player, final int button) {
        boolean open = player.interfaceManager.isInterfaceOpen(SkillGuides.INTERFACE_ID)  ? true : false;

        switch (button) { //openInterface(player, player.menuOpened, index, open);

            case -22982:
                player.menuOpened = -1;
                player.optionOpened = -1;
                player.send(new SendRemoveInterface());
                return true;
            /*case 8654:
                SkillGuides.openInterface(player, GuideData.ATTACK.ordinal(), 0, open);
                return true;
            case 8655:
                SkillGuides.openInterface(player, GuideData.HITPOINTS.ordinal(), 0, open);
                return true;*/
            case 8656:
                SkillGuides.openInterface(player, GuideData.MINING.ordinal(), 0, open);
                return true;
        /*    case 8657:
                SkillGuides.openInterface(player, GuideData.STRENGTH.ordinal(), 0, open);
                return true;*/
            case 8658:
                SkillGuides.openInterface(player, GuideData.AGILITY.ordinal(), 0, open);
                return true;
            case 8659:
                SkillGuides.openInterface(player, GuideData.SMITHING.ordinal(), 0, open);
                return true;
         /*   case 8660:
                SkillGuides.openInterface(player, GuideData.DEFENCE.ordinal(), 0, open);
                return true;*/
            case 8661:
                SkillGuides.openInterface(player, GuideData.HERBLORE.ordinal(), 0, open);
                return true;
            case 8662:
                SkillGuides.openInterface(player, GuideData.FISHING.ordinal(), 0, open);
                return true;
           /* case 8663:
                SkillGuides.openInterface(player, GuideData.RANGED.ordinal(), 0, open);
                return true;*/
            case 8664:
                SkillGuides.openInterface(player, GuideData.THIEVING.ordinal(), 0, open);
                return true;
            case 8665:
                SkillGuides.openInterface(player, GuideData.COOKING.ordinal(), 0, open);
                return true;
          /*  case 8666:
                SkillGuides.openInterface(player, GuideData.PRAYER.ordinal(), 0, open);
                return true;*/
            case 8667:
                SkillGuides.openInterface(player, GuideData.CRAFTING.ordinal(), 0, open);
                return true;
            case 8668:
                SkillGuides.openInterface(player, GuideData.FIREMAKING.ordinal(), 0, open);
                return true;
           /* case 8669:
                SkillGuides.openInterface(player, GuideData.MAGIC.ordinal(), 0, open);
                return true;*/
            case 8670:
                SkillGuides.openInterface(player, GuideData.FLETCHING.ordinal(), 0, open);
                return true;
            case 8671:
                SkillGuides.openInterface(player, GuideData.WOODCUTTING.ordinal(), 0, open);
                return true;
            case 8672:
                SkillGuides.openInterface(player, GuideData.RUNECRAFTING.ordinal(), 0, open);
                return true;
            case 12162:
                SkillGuides.openInterface(player, GuideData.SLAYER.ordinal(), 0, open);
                return true;
            case 13928:
                SkillGuides.openInterface(player, GuideData.FARMING.ordinal(), 0, open);
                return true;
            case 18801:
                SkillGuides.openInterface(player, GuideData.CONSTRUCTION.ordinal(), 0, open);
                return true;
            case 18829:
                SkillGuides.openInterface(player, GuideData.HUNTER.ordinal(), 0, open);
                return true;

            case -22977: //Option 0
                SkillGuides.openInterface(player, player.menuOpened, 0, open);
                return true;
            case -22976: //Option 1
                SkillGuides.openInterface(player, player.menuOpened, 1, open);
                return true;
            case -22975: //Option 2
                SkillGuides.openInterface(player, player.menuOpened, 2, open);
                return true;
            case -22974: //Option 3
                SkillGuides.openInterface(player, player.menuOpened, 3, open);
                return true;
            case -22973: //Option 4
                SkillGuides.openInterface(player, player.menuOpened, 4, open);
                return true;
            case -22972: //Option 5
                SkillGuides.openInterface(player, player.menuOpened, 5, open);
                return true;
            case -22971: //Option 6
                SkillGuides.openInterface(player, player.menuOpened, 6, open);
                return true;
            case -22970: //Option 7
                SkillGuides.openInterface(player, player.menuOpened, 7, open);
                return true;
            case -22969: //Option 8
                SkillGuides.openInterface(player, player.menuOpened, 8, open);
                return true;
            case -22968: //Option 9
                SkillGuides.openInterface(player, player.menuOpened, 9, open);
                return true;
            case -22967: //Option 10
                SkillGuides.openInterface(player, player.menuOpened, 10, open);
                return true;
            case -22966: //Option 11
                SkillGuides.openInterface(player, player.menuOpened, 11, open);
                return true;
            case -22965: //Option 12
                SkillGuides.openInterface(player, player.menuOpened, 12, open);
                return true;
            case -22964: //Option 13
                SkillGuides.openInterface(player, player.menuOpened, 13, open);
                return true;
        }
        return false;
    }

}
