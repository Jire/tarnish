package com.osroyale.game.world.cronjobs;

import com.osroyale.game.world.World;
import org.quartz.JobExecutionContext;

public abstract class Job implements org.quartz.Job {

    private final String name;

    protected Job(String name) {
        this.name = name;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        System.out.println("Firing " + name + " job");

        final Job job = this;
        World.schedule(1, () -> {
            try {
                job.execute();
            } catch (Exception e) {
                System.err.println("An error occurred in " + job.name + " job");
            }
        });
    }

    public abstract void execute();
}
