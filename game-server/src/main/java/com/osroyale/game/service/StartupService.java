package com.osroyale.game.service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * An <code>ExecutorService</code> that waits for all its events to finish
 * executing.
 * 
 * @author Graham Edgecombe
 */
public class StartupService implements ExecutorService {

	/** The service backing this service. */
	private ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	/** A list of pending tasks. */
	private BlockingQueue<Future<?>> pendingTasks = new LinkedBlockingQueue<>();

	/**
	 * Waits for pending tasks to complete.
	 * 
	 * @throws ExecutionException
	 *             if an error in a task occurred.
	 */
	public void waitForPendingTasks() throws ExecutionException {
		while (pendingTasks.size() > 0) {
			if (isShutdown()) {
				return;
			}
			try {
				pendingTasks.take().get();
			} catch (InterruptedException e) {
				continue;
			}
		}
	}

	public void awaitUntilFinished(long timeout, TimeUnit timeUnit) {
		try {
			service.awaitTermination(timeout, timeUnit);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the number of pending tasks.
	 * 
	 * @return The number of pending tasks.
	 */
	public int getPendingTaskAmount() {
		return pendingTasks.size();
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return service.awaitTermination(timeout, unit);
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		List<Future<T>> futures = service.invokeAll(tasks);
		for (Future<?> future : futures) {
			pendingTasks.add(future);
		}
		return futures;
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException {
		List<Future<T>> futures = service.invokeAll(tasks, timeout, unit);
		for (Future<?> future : futures) {
			pendingTasks.add(future);
		}
		return futures;
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		return service.invokeAny(tasks);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		return service.invokeAny(tasks, timeout, unit);
	}

	@Override
	public boolean isShutdown() {
		return service.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return service.isTerminated();
	}

	@Override
	public void shutdown() {
		service.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		return service.shutdownNow();
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		Future<T> future = service.submit(task);
		pendingTasks.add(future);
		return future;
	}

	@Override
	public Future<?> submit(Runnable task) {
		Future<?> future = service.submit(task);
		pendingTasks.add(future);
		return future;
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		Future<T> future = service.submit(task, result);
		pendingTasks.add(future);
		return future;
	}

	@Override
	public void execute(Runnable command) {
		service.execute(command);
	}

}
