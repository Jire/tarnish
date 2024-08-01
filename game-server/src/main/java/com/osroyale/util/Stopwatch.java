package com.osroyale.util;

import java.util.concurrent.TimeUnit;

public final class Stopwatch {

	private long cachedTime;

	public Stopwatch() {
		cachedTime = System.nanoTime();
	}

	public static Stopwatch start() {
		return new Stopwatch();
	}

    @Override
    public String toString() {
        return String.format("STOPWATCH[elapsed=%d]", elapsedTime());
    }

	public Stopwatch reset() {
		cachedTime = System.nanoTime();
		return this;
	}

	public Stopwatch reset(int delay, TimeUnit unit) {
		cachedTime = System.nanoTime() + TimeUnit.NANOSECONDS.convert(delay, unit);
		return this;
	}

	public Stopwatch reset(long millis) {
		cachedTime = System.nanoTime() + TimeUnit.NANOSECONDS.convert(millis, TimeUnit.MILLISECONDS);
		return this;
	}

	public long elapsedTime(TimeUnit unit) {
		return unit.convert(System.nanoTime() - cachedTime, TimeUnit.NANOSECONDS);
	}

	public boolean finished() {
		return cachedTime - System.nanoTime() <= 0;
	}

	public long elapsedTime() {
		return elapsedTime(TimeUnit.MILLISECONDS);
	}

	public boolean elapsed(long time, TimeUnit unit) {
		return elapsedTime(unit) >= time;
	}

	public boolean elapsed(long time) {
		return elapsed(time, TimeUnit.MILLISECONDS);
	}

	public long getCachedTime() {
		return cachedTime;
	}

	public void setCachedTime(long cachedTime) {
		this.cachedTime = cachedTime;
	}

}