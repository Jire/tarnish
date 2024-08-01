package net.runelite.client.plugins.greenscreen;

import com.google.inject.Provides;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
	name = "Green Screen",
	enabledByDefault = false
)
public class GreenScreenPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private GreenScreenConfig config;

	@Inject
	private GreenScreenOverlay overlay;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private KeyManager keyManager;

	@Getter
	private boolean renderGreenscreen;

	private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.hotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			renderGreenscreen = !renderGreenscreen;
		}
	};

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
		renderGreenscreen = config.defaultState();
		keyManager.registerKeyListener(hotkeyListener);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		keyManager.unregisterKeyListener(hotkeyListener);
	}

	@Provides
	GreenScreenConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(GreenScreenConfig.class);
	}
}
