package com.osroyale.content.dialogue.impl;

import com.osroyale.content.dialogue.Dialogue;
import com.osroyale.content.dialogue.DialogueFactory;

/**
 * Handles the lootshare dialogue.
 *
 * @author Daniel
 */
public class LootshareDialogue extends Dialogue {

    @Override
    public void sendDialogues(DialogueFactory factory) {
        factory.sendStatement("").sendStatement(
                "When active, all clan gameMembers within a 32 tile radius will receive",
                "an equal split of any item dropped by a npc that has a value of",
                "2,500,000 or more. Untradeable items will not be split or given to each.",
                "Clan member. Be advised that the item value will not always be 100%").sendStatement(
                "correct and 25% of the item value will be removed due to the item",
                "automatically converting into coins. Only gameMembers with a certain clan",
                "rank may toggle the lootshare.")
                .execute();
    }
}
