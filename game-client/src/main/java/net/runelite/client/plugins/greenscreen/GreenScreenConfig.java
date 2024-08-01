package net.runelite.client.plugins.greenscreen;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;

import java.awt.Color;

@ConfigGroup("greenscreen")
public interface GreenScreenConfig extends Config
{
	@ConfigItem(
		keyName = "color",
		name = "Color",
		description = "The color of the greenscreen",
		position = 0
	)
	default Color greenscreenColor()
	{
		return new Color(41, 244, 24);
	}

	@ConfigItem(
			keyName = "toggleKey",
			name= "Toggle Key",
			description = "Key to press to toggle greenscreen",
			position = 1
	)
	default Keybind hotkey()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			keyName = "defaultState",
			name = "Should Default On",
			description = "What state should the greenscreen default to",
			position = 2
	)
	default boolean defaultState()
	{
		return true;
	}
}
