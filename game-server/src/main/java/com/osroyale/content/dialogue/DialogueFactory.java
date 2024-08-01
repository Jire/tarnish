package com.osroyale.content.dialogue;

import com.osroyale.game.world.entity.mob.npc.definition.NpcDefinition;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.*;
import com.osroyale.util.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Represents a factory class that contains important functions for building
 * dialogues.
 *
 * @author nshusa
 */
public final class DialogueFactory {

    private static final Logger logger = LogManager.getLogger();

    /** The queue of dialogues in this factory. */
    private final Queue<Chainable> CHAIN = new ArrayDeque<>();

    /** The maximum length of a single line of dialogue. */
    private static final int MAXIMUM_LENGTH = 100;

    /** The player who owns this factory. */
    private final Player player;

    /** The flag that denotes dialogue is active. */
    private boolean active;

    /** The next action in the dialogue chain. */
    private Optional<Runnable> nextAction = Optional.empty();
    private boolean locked;

    /**
     * Creates a new {@link DialogueFactory}.
     *
     * @param player The player who owns this factory.
     */
    public DialogueFactory(Player player) {
        this.player = player;
    }

    /**
     * Sends a player a dialogue.
     *
     * @param dialogue The dialogue to sent.
     */
    public final DialogueFactory sendDialogue(Dialogue dialogue) {
        player.dialogue = Optional.of(dialogue);
        dialogue.sendDialogues(this);
        return this;
    }

    /**
     * Sets an {@code action} so this action can be executed after dialogues are
     * done.
     *
     * @param action The action to set.
     * @return The instance of this factory.
     */
    public final DialogueFactory onAction(Runnable action) {
        setNextAction(Optional.of(action));
        return this;
    }

    public void lock(boolean lock) {
        this.locked = lock;
    }

    /**
     * Accepts the next dialogue in the chain.
     *
     * @return The instance of this factory.
     */
    public DialogueFactory onNext() {
        if (getChain().peek() != null) {
            Chainable chain = getChain().poll();
            chain.accept(this);
        } else {
            player.interfaceManager.close();
        }
        return this;
    }

    /**
     * Executes an {@code result} for a {@code player}.
     *
     * @param type   The type of option.
     * @param result The option to execute.
     */
    public final DialogueFactory executeOption(int type, Optional<OptionDialogue> result) {
        if (result.isPresent()) {
            OptionDialogue optionDialogue = result.get();

            if (type < 0 || type >= optionDialogue.getActions().size()) {
                return this;
            }

            optionDialogue.getActions().get(type).run();
        }
        execute();
        return this;
    }

    /**
     * Clears the current dialogue {@code chain}.
     *
     * @return The instance of this factory.
     */
    public void clear() {
        CHAIN.clear();
        nextAction = Optional.empty();
        player.dialogue = Optional.empty();
        player.optionDialogue = Optional.empty();
        setActive(false);
        player.interfaceManager.setDialogue(-1);
        player.send(new SendRemoveInterface(false));
    }

    /**
     * Appends a {@code chain} to this factory.
     *
     * @return The instance of this factory.
     */
    private final DialogueFactory append(Chainable chain) {
        this.CHAIN.add(chain);
        return this;
    }

    /**
     * Gets the current chain.
     *
     * @return The queue of dialogues.
     */
    public final Queue<Chainable> getChain() {
        return CHAIN;
    }

    /**
     * Gets if the dialogue is option.
     *
     * @return The option dialogue.
     */
    public boolean isOption() {
        Chainable next = getChain().peek();
        return next instanceof OptionDialogue;
    }

    /**
     * Retrieves the next dialogue in the chain and executes it.
     *
     * @return The instance of this factory.
     */
    public final DialogueFactory execute() {
        if (locked)
            return this;
        if (player.movement.isMoving()) {
            player.movement.reset();
        }
        // check to see if there are anymore dialogues.
        if (getChain().peek() != null) {

            // there is so, grab the next dialogue.
            Chainable entry = getChain().poll();

            // is this an option dialogue?
            if (entry instanceof OptionDialogue) {
                OptionDialogue option = (OptionDialogue) entry;
                player.optionDialogue = Optional.of(option);
            } else {
                player.optionDialogue = Optional.empty();
            }

            player.interfaceManager.setDialogue(1);
            setActive(true);
            // whatever dialogue it is, accept it.
            entry.accept(this);
        } else {
            // there are no dialogues in this chain.
            // is there an action?
            if (getNextAction().isPresent()) {
                // there is so, execute it.
                getNextAction().ifPresent($it -> $it.run());
                // we just used this action so empty it so it can't be used
                // again.
                setNextAction(Optional.empty());
                player.optionDialogue = Optional.empty();
                return this;
            }
            // there are no more dialogues, so clear the screen.
            player.dialogueFactory.clear();
        }
        return this;
    }

    /**
     * Appends keywords to an existing dialogue text.
     *
     * @param line The line to check for a keyword.
     */
    private final String appendKeywords(String line) {
        if (line.contains("#username")) {
            line = line.replace("#username", Utility.formatName((player.getName())));
        }
        if (line.contains("#name")) {
            line = line.replace("#name", Utility.formatName((player.getName())));
        }
        return line;
    }

    /**
     * Appends a {@link PlayerDialogue} to the current dialogue chain.
     *
     * @param lines The dialogue of the player talking.
     * @return The instance of this factory.
     */
    public final DialogueFactory sendPlayerChat(String... lines) {
        return append(new PlayerDialogue(lines));
    }

    /**
     * Appends a {@link PlayerDialogue} to the current dialogue chain.
     *
     * @param lines      The dialogue of the player talking.
     * @param expression The expression of this dialogue.
     * @return The instance of this factory.
     */
    public final DialogueFactory sendPlayerChat(Expression expression, String... lines) {
        return append(new PlayerDialogue(expression, lines));
    }

    /**
     * Sends a dialogue with a player talking.
     *
     * @param dialogue The player dialogue.
     * @return The instance of this factory.
     */
    final DialogueFactory sendPlayerChat(PlayerDialogue dialogue) {
        Expression expression = dialogue.getExpression();
        String[] lines = dialogue.getLines();
        validateLength(lines);
        switch (lines.length) {
            case 1:
                player.send(new SendInterfaceAnimation(969, expression.getId()));
                player.send(new SendString(Utility.formatName(player.getName()), 970));
                player.send(new SendString(appendKeywords(lines[0]), 971));
                player.send(new SendPlayerDialogueHead(969));
                player.send(new SendChatBoxInterface(968));
                break;
            case 2:
                player.send(new SendInterfaceAnimation(974, expression.getId()));
                player.send(new SendString(Utility.formatName(player.getName()), 975));
                player.send(new SendString(appendKeywords(lines[0]), 976));
                player.send(new SendString(appendKeywords(lines[1]), 977));
                player.send(new SendPlayerDialogueHead(974));
                player.send(new SendChatBoxInterface(973));
                break;
            case 3:
                player.send(new SendInterfaceAnimation(980, expression.getId()));
                player.send(new SendString(Utility.formatName(player.getName()), 981));
                player.send(new SendString(appendKeywords(lines[0]), 982));
                player.send(new SendString(appendKeywords(lines[1]), 983));
                player.send(new SendString(appendKeywords(lines[2]), 984));
                player.send(new SendPlayerDialogueHead(980));
                player.send(new SendChatBoxInterface(979));
                break;
            case 4:
                player.send(new SendInterfaceAnimation(987, expression.getId()));
                player.send(new SendString(Utility.formatName(player.getName()), 988));
                player.send(new SendString(appendKeywords(lines[0]), 989));
                player.send(new SendString(appendKeywords(lines[1]), 990));
                player.send(new SendString(appendKeywords(lines[2]), 991));
                player.send(new SendString(appendKeywords(lines[3]), 992));
                player.send(new SendPlayerDialogueHead(987));
                player.send(new SendChatBoxInterface(986));
                break;
            default:
                logger.error(String.format("Invalid player dialogue line length: %s", lines.length));
                break;
        }
        return this;
    }

    /**
     * Appends an {@link NpcDialogue} to the current dialogue chain.
     *
     * @param id    The id of this npc.
     * @param lines The text of this dialogue.
     * @return The instance of this factory.
     */
    public final DialogueFactory sendNpcChat(int id, String... lines) {
        return append(new NpcDialogue(id, Expression.DEFAULT, lines));
    }

    /**
     * Appends an {@link NpcDialogue} to the current dialogue chain.
     *
     * @param id         The id of this npc.
     * @param expression The expression of this npc.
     * @param lines      The text of this dialogue.
     * @return The instance of this factory.
     */
    public final DialogueFactory sendNpcChat(int id, Expression expression, String... lines) {
        return append(new NpcDialogue(id, expression, lines));
    }

    /**
     * Sends a dialogue with a npc talking.
     *
     * @param dialogue The dialogue.
     * @return The instance of this factory.
     */
    final DialogueFactory sendNpcChat(NpcDialogue dialogue) {
        Expression expression = dialogue.getExpression();
        String[] lines = dialogue.getLines();
        validateLength(lines);
        int id = dialogue.getId();
        final NpcDefinition npcDef = NpcDefinition.get(id);

        if (npcDef == null) return this;
        switch (lines.length) {
            case 1:
                player.send(new SendInterfaceAnimation(4883, expression.getId()));
                player.send(new SendString(npcDef.getName(), 4884));
                player.send(new SendString(appendKeywords(lines[0]), 4885));
                player.send(new SendNpcHead(npcDef.getId(), 4883));
                player.send(new SendChatBoxInterface(4882));
                break;
            case 2:
                player.send(new SendInterfaceAnimation(4888, expression.getId()));
                player.send(new SendString(npcDef.getName(), 4889));
                player.send(new SendString(appendKeywords(lines[0]), 4890));
                player.send(new SendString(appendKeywords(lines[1]), 4891));
                player.send(new SendNpcHead(npcDef.getId(), 4888));
                player.send(new SendChatBoxInterface(4887));
                break;
            case 3:
                player.send(new SendInterfaceAnimation(4894, expression.getId()));
                player.send(new SendString(npcDef.getName(), 4895));
                player.send(new SendString(appendKeywords(lines[0]), 4896));
                player.send(new SendString(appendKeywords(lines[1]), 4897));
                player.send(new SendString(appendKeywords(lines[2]), 4898));
                player.send(new SendNpcHead(npcDef.getId(), 4894));
                player.send(new SendChatBoxInterface(4893));
                break;
            case 4:
                player.send(new SendInterfaceAnimation(4901, expression.getId()));
                player.send(new SendString(npcDef.getName(), 4902));
                player.send(new SendString(appendKeywords(lines[0]), 4903));
                player.send(new SendString(appendKeywords(lines[1]), 4904));
                player.send(new SendString(appendKeywords(lines[2]), 4905));
                player.send(new SendString(appendKeywords(lines[3]), 4906));
                player.send(new SendNpcHead(npcDef.getId(), 4901));
                player.send(new SendChatBoxInterface(4900));
                break;
            default:
                logger.error(String.format("Invalid npc dialogue line length: %s", lines.length));
                break;
        }
        return this;
    }

    /**
     * Appends the {@link OptionDialogue} onto the current dialogue chain.
     *
     * @param option1 The text for the first option.
     * @param action1 The action for the first action.
     * @param option2 The text for the second option.
     * @param action2 The action for the second action.
     */
    public final DialogueFactory sendOption(String option1, Runnable action1, String option2, Runnable action2) {
        return append(new OptionDialogue(option1, action1, option2, action2));
    }

    /**
     * Appends the {@link OptionDialogue} onto the current dialogue chain.
     *
     * @param option1 The text for the first option.
     * @param action1 The action for the first action.
     * @param option2 The text for the second option.
     * @param action2 The action for the second action.
     * @param option3 The text for the third option.
     * @param action3 The action for the third action.
     */
    public final DialogueFactory sendOption(String option1, Runnable action1, String option2, Runnable action2, String option3, Runnable action3) {
        return append(new OptionDialogue(option1, action1, option2, action2, option3, action3));
    }

    /**
     * Appends the {@link OptionDialogue} onto the current dialogue chain.
     *
     * @param option1 The text for the first option.
     * @param action1 The action for the first action.
     * @param option2 The text for the second option.
     * @param action2 The action for the second action.
     * @param option3 The text for the third option.
     * @param action3 The action for the third action.
     * @param option4 The text for the four option.
     * @param action4 The action for the four action.
     */
    public final DialogueFactory sendOption(String option1, Runnable action1, String option2, Runnable action2, String option3, Runnable action3, String option4, Runnable action4) {
        return append(new OptionDialogue(option1, action1, option2, action2, option3, action3, option4, action4));
    }

    /**
     * Appends the {@link OptionDialogue} onto the current dialogue chain.
     *
     * @param option1 The text for the first option.
     * @param action1 The action for the first action.
     * @param option2 The text for the second option.
     * @param action2 The action for the second action.
     * @param option3 The text for the third option.
     * @param action3 The action for the third action.
     * @param option4 The text for the four option.
     * @param action4 The action for the four action.
     * @param option5 The text for the fifth option.
     * @param action5 The action for the fifth action.
     */
    public final DialogueFactory sendOption(String option1, Runnable action1, String option2, Runnable action2, String option3, Runnable action3, String option4, Runnable action4, String option5, Runnable action5) {
        return append(new OptionDialogue(option1, action1, option2, action2, option3, action3, option4, action4, option5, action5));
    }

    /**
     * Sends a dialogue with options.
     *
     * @param dialogue The dialogue.
     * @return The instance of this factory.
     */
    final DialogueFactory sendOption(OptionDialogue dialogue) {
        String[] options = dialogue.getLines();
        validateLength(options);
        switch (options.length) {
            case 2:
                player.send(new SendString("Select an Option", 2460));
                player.send(new SendString(options[0], 2461));
                player.send(new SendTooltip("</col>Select <col=A89590>" + options[0], 2461));
                player.send(new SendString(options[1], 2462));
                player.send(new SendTooltip("</col>Select <col=A89590>" + options[1], 2461));
                player.send(new SendChatBoxInterface(2459));
                return this;
            case 3:
                player.send(new SendString("Select an Option", 2470));
                player.send(new SendString(options[0], 2471));
                player.send(new SendTooltip("</col>Select <col=A89590>" + options[0], 2471));
                player.send(new SendString(options[1], 2472));
                player.send(new SendTooltip("</col>Select <col=A89590>" + options[1], 2472));
                player.send(new SendString(options[2], 2473));
                player.send(new SendTooltip("</col>Select <col=A89590>" + options[2], 2473));
                player.send(new SendChatBoxInterface(2469));
                return this;
            case 4:
                player.send(new SendString("Select an Option", 2481));
                player.send(new SendString(options[0], 2482));
                player.send(new SendTooltip("</col>Select <col=A89590>" + options[0], 2482));
                player.send(new SendString(options[1], 2483));
                player.send(new SendTooltip("</col>Select <col=A89590>" + options[1], 2483));
                player.send(new SendString(options[2], 2484));
                player.send(new SendTooltip("</col>Select <col=A89590>" + options[2], 2484));
                player.send(new SendString(options[3], 2485));
                player.send(new SendTooltip("</col>Select <col=A89590>" + options[3], 2485));
                player.send(new SendChatBoxInterface(2480));
                return this;
            case 5:
                player.send(new SendString("Select an Option", 2493));
                player.send(new SendString(options[0], 2494));
                player.send(new SendTooltip("</col>Select <col=A89590>" + options[0], 2494));
                player.send(new SendString(options[1], 2495));
                player.send(new SendTooltip("</col>Select <col=A89590>" + options[1], 2495));
                player.send(new SendString(options[2], 2496));
                player.send(new SendTooltip("</col>Select <col=A89590>" + options[2], 2496));
                player.send(new SendString(options[3], 2497));
                player.send(new SendTooltip("</col>Select <col=A89590>" + options[3], 2497));
                player.send(new SendString(options[4], 2498));
                player.send(new SendTooltip("</col>Select <col=A89590>" + options[4], 2498));
                player.send(new SendChatBoxInterface(2492));
                return this;
        }
        return this;
    }

    /**
     * Appends a {@link StatementDialogue} to the current dialogue chain.
     *
     * @param lines The text for this statement.
     * @return The instance of this factory.
     */
    public final DialogueFactory sendStatement(String... lines) {
        validateLength(lines);
        append(new StatementDialogue(lines));
        return this;
    }

    /**
     * Sends a player a statement dialogue.
     *
     * @param dialogue The statement dialogue.
     */
    final DialogueFactory sendStatement(StatementDialogue dialogue) {
        validateLength(dialogue.getLines());
        switch (dialogue.getLines().length) {
            case 1:
                player.send(new SendString(dialogue.getLines()[0], 357));
                player.send(new SendChatBoxInterface(356));
                break;
            case 2:
                player.send(new SendString(dialogue.getLines()[0], 360));
                player.send(new SendString(dialogue.getLines()[1], 361));
                player.send(new SendChatBoxInterface(359));
                break;
            case 3:
                player.send(new SendString(dialogue.getLines()[0], 364));
                player.send(new SendString(dialogue.getLines()[1], 365));
                player.send(new SendString(dialogue.getLines()[2], 366));
                player.send(new SendChatBoxInterface(363));
                break;
            case 4:
                player.send(new SendString(dialogue.getLines()[0], 369));
                player.send(new SendString(dialogue.getLines()[1], 370));
                player.send(new SendString(dialogue.getLines()[2], 371));
                player.send(new SendString(dialogue.getLines()[3], 372));
                player.send(new SendChatBoxInterface(368));
                break;
            case 5:
                player.send(new SendString(dialogue.getLines()[0], 375));
                player.send(new SendString(dialogue.getLines()[1], 376));
                player.send(new SendString(dialogue.getLines()[2], 377));
                player.send(new SendString(dialogue.getLines()[3], 378));
                player.send(new SendString(dialogue.getLines()[4], 379));
                player.send(new SendChatBoxInterface(374));
                break;
            default:
                logger.error(String.format("Invalid statement dialogue line length: %s", dialogue.getLines().length));
                break;
        }
        return this;
    }

    public final DialogueFactory sendItem(String title, String text, Item item) {
        validateLength(text);
        append(new ItemDialogue(title, text, item.getId()));
        return this;
    }

    public final DialogueFactory sendItem(String title, String text, int item) {
        validateLength(text);
        append(new ItemDialogue(title, text, item));
        return this;
    }

    final DialogueFactory sendItem(ItemDialogue dialogue) {
        validateLength(dialogue.getContext());
        player.send(new SendInterfaceAnimation(4883, 591));
        player.send(new SendString(dialogue.getTitle(), 4884));
        player.send(new SendString(dialogue.getContext(), 4885));
        player.send(new SendString("Click here to continue.", 4886));
        player.send(new SendItemModelOnInterface(4883, 250, dialogue.getItem()));
        player.send(new SendChatBoxInterface(4882));
        return this;
    }

    public final DialogueFactory sendInformationBox(String title, String...lines) {
        validateLength(lines);
        append(new InformationDialogue(title, lines));
        return this;
    }

    final DialogueFactory sendInformationBox(InformationDialogue dialogue) {
        validateLength(dialogue.getLines());
        switch (dialogue.getLines().length) {
            case 1:
                player.send(new SendString(dialogue.getTitle(), 6180));
                player.send(new SendString("", 6181));
                player.send(new SendString(dialogue.getLines()[0], 6182));
                player.send(new SendString("", 6183));
                player.send(new SendString("", 6184));
                player.send(new SendChatBoxInterface(6179));
                break;
            case 2:
                player.send(new SendString(dialogue.getTitle(), 6180));
                player.send(new SendString("", 6181));
                player.send(new SendString(dialogue.getLines()[0], 6182));
                player.send(new SendString(dialogue.getLines()[1], 6183));
                player.send(new SendString("", 6184));
                player.send(new SendChatBoxInterface(6179));
                break;
            case 3:
                player.send(new SendString(dialogue.getTitle(), 6180));
                player.send(new SendString("", 6181));
                player.send(new SendString(dialogue.getLines()[0], 6182));
                player.send(new SendString(dialogue.getLines()[1], 6183));
                player.send(new SendString(dialogue.getLines()[2], 6184));
                player.send(new SendChatBoxInterface(6179));
                break;
            case 4:
                player.send(new SendString(dialogue.getTitle(), 6180));
                player.send(new SendString(dialogue.getLines()[0], 6181));
                player.send(new SendString(dialogue.getLines()[1], 6182));
                player.send(new SendString(dialogue.getLines()[2], 6183));
                player.send(new SendString(dialogue.getLines()[3], 6184));
                player.send(new SendChatBoxInterface(6179));
                break;
            default:
                logger.error(String.format("Invalid information dialogue line length: %s", dialogue.getLines().length));
                break;
        }
        return this;
    }

    /**
     * The method that validates the length of {@code text}.
     *
     * @param text the text that will be validated.
     * @throws IllegalStateException if any lines of the text exceed a certain length.
     */
    private final void validateLength(String... text) {
        if (Arrays.stream(text).filter(Objects::nonNull).anyMatch(s -> s.length() > MAXIMUM_LENGTH)) {
            throw new IllegalStateException("Dialogue length too long, maximum length is: " + MAXIMUM_LENGTH);
        }
    }

    /**
     * The player that owns this factory.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the {@link Optional} describing the next action in the dialogue
     * chain.
     *
     * @return The optional describing the next action.
     */
    public Optional<Runnable> getNextAction() {
        return nextAction;
    }

    /**
     * Sets the next action in the dialogue chain.
     *
     * @param nextAction The action to set.
     */
    public void setNextAction(Optional<Runnable> nextAction) {
        this.nextAction = nextAction;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
