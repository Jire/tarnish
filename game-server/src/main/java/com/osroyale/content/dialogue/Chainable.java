package com.osroyale.content.dialogue;

import java.util.function.Consumer;

/**
 * The chain-able itemcontainer that allows implementing dialogue factories the ability to chain together.
 *
 * @author Seven
 */
public interface Chainable extends Consumer<DialogueFactory> { }