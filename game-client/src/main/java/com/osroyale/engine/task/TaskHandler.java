package com.osroyale.engine.task;

import net.runelite.rs.api.RSTaskHandler;

import java.io.DataInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

public class TaskHandler implements Runnable, RSTaskHandler {

    public static String javaVersion;
    Task current;
    Task task;
    Thread thread;
    boolean isClosed;

    public TaskHandler() {
        this.current = null;
        this.task = null;
        javaVersion = "1.6";
        this.isClosed = false;
        this.thread = new Thread(this);
        this.thread.setPriority(10);
        this.thread.setDaemon(true);
        this.thread.start();
    }

    public final void close() {
        synchronized (this) {
            this.isClosed = true;
            this.notifyAll();
        }

        try {
            this.thread.join();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }

    }

    final Task newTask(int var1, int var2, int var3, Object var4) {
        Task task = new Task();
        task.type = var1;
        task.intArgument = var2;
        task.objectArgument = var4;
        synchronized (this) {
            if (this.task != null) {
                this.task.next = task;
                this.task = task;
            } else {
                this.task = this.current = task;
            }

            this.notify();
            return task;
        }
    }

    public final Task newSocketTask(String name, int priority) {
        return this.newTask(1, priority, 0, name);
    }

    public final Task newThreadTask(Runnable runnable, int priority) {
        return this.newTask(2, priority, 0, runnable);
    }

    public final void run() {
        while (true) {
            Task task;
            synchronized (this) {
                while (true) {
                    if (this.isClosed) {
                        return;
                    }

                    if (this.current != null) {
                        task = this.current;
                        this.current = this.current.next;
                        if (this.current == null) {
                            this.task = null;
                        }
                        break;
                    }

                    try {
                        this.wait();
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                }
            }

            try {
                int var5 = task.type;
                if (var5 == 1) {
                    task.result = new Socket(InetAddress.getByName((String) task.objectArgument), task.intArgument);
                } else if (var5 == 2) {
                    Thread var3 = new Thread((Runnable) task.objectArgument);
                    var3.setDaemon(true);
                    var3.start();
                    var3.setPriority(task.intArgument);
                    task.result = var3;
                } else if (var5 == 4) {
                    task.result = new DataInputStream(((URL) task.objectArgument).openStream());
                }

                task.status = 1;
            } catch (ThreadDeath threadDeath) {
                threadDeath.printStackTrace();
            } catch (Throwable throwable) {
                task.status = 2;
                throwable.printStackTrace();
            }
        }
    }


}