package com.osroyale.game.world.cronjobs;

import com.osroyale.game.world.cronjobs.impl.DoubleExperienceJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Jobs {

    private static final SchedulerFactory factory = new StdSchedulerFactory();

    public static void load() throws SchedulerException {
        final Scheduler scheduler = factory.getScheduler();

        // Double Experience Job - Fired every hour
        final JobDetail doubleExpJob = JobBuilder.newJob(DoubleExperienceJob.class).build();
        final CronTrigger doubleExpTrigger = newTrigger().withSchedule(cronSchedule("0 0 * ? * *")).build();
        scheduler.scheduleJob(doubleExpJob, doubleExpTrigger);
        System.out.println("Double Experience Job: " + doubleExpTrigger.getNextFireTime());

        scheduler.start();
    }
}
