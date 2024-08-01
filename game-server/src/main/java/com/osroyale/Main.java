package com.osroyale;

/**
 * @author Jire
 */
public enum Main {

    ;

    public static void main(String[] args) {
        new Thread(new Starter(), Starter.class.getSimpleName()).start();
    }

}
