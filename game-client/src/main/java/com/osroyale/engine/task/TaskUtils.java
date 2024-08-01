package com.osroyale.engine.task;

public class TaskUtils {

    public static void sleep(long wait) {
        if (wait > 0L) {
            if (0L == wait % 10L) {

                try {
                    Thread.sleep(1L);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }

        }
    }

}
