package com.osroyale.login.impl;

import com.osroyale.Client;

public class LoginRenderThread extends Thread {
    private volatile boolean stillRunning;
    private volatile Client client;

    public LoginRenderThread(Client client) {
        this.client = client;
        stillRunning = true;
    }

    @Override
    public void run() {
        while (stillRunning)
        {
            try {
                long start = System.currentTimeMillis();
                client.loginRenderer.display();
                long end = System.currentTimeMillis();
                long elapsed = end - start;
                if (elapsed < 20) {
                    Thread.sleep(20 - elapsed);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //Fuck'em
            }
            //client.loginRenderer.click(); //NO - BAD. DO NOT IMPLEMENT THIS LINE.
        }
    }

    public void stopRendering() {
        stillRunning = false;
    }
}
