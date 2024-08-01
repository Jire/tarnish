/*
 * Copyright (c) 2016-2017, Adam <Adam@sigterm.info>
 * Copyright (c) 2018, Tomas Slusny <slusnucky@gmail.com>
 * Copyright (c) 2019 Abex
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.rs;

import com.osroyale.Configuration;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.FatalErrorDialog;
import net.runelite.client.ui.SplashScreen;

import javax.swing.*;
import java.applet.Applet;
import java.io.IOException;
import java.util.function.Supplier;

@Slf4j
@SuppressWarnings({"deprecation", "removal"})
public class ClientLoader implements Supplier<Applet>
{
	private Object client;

	@Override
	public synchronized Applet get()
	{
		if (client == null)
		{
			try {
				client = doLoad();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
		}

		if (client instanceof Throwable)
		{
			throw new RuntimeException((Throwable) client);
		}
		return (Applet) client;
	}

	private static Applet loadVanilla() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class<com.osroyale.Client> clientClass = com.osroyale.Client.class;
		return ClientLoader.loadFromClass(clientClass);
	}

	private static Applet loadFromClass(Class<?> clientClass) throws IllegalAccessException, InstantiationException {
		return (Applet)clientClass.newInstance();
	}

	private Object doLoad() throws InstantiationException
	{
		try
		{
			SplashScreen.stage(.465, "Starting", "Starting " + Configuration.NAME);

			Applet rs = loadVanilla();

			SplashScreen.stage(.5, null, "Starting core classes");

			return rs;
		}
		catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | SecurityException e)
		{
			//log.error("Error loading RS!", e);

			SwingUtilities.invokeLater(() -> FatalErrorDialog.showNetErrorWindow("loading the client", e));
			return e;
		}
	}
}
