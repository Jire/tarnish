package com.osroyale.net;

import com.osroyale.net.codec.login.LoginDetailsPacket;
import com.osroyale.net.session.LoginSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class LoginExecutorService implements Closeable {

    private static final Logger logger = LoggerFactory.getLogger(LoginExecutorService.class);

    private static final long TERMINATION_TIMEOUT = 60;
    private static final TimeUnit TERMINATION_TIMEOUT_UNIT = TimeUnit.SECONDS;

    private final ExecutorService executorService;

    public LoginExecutorService(final int threads) {
        this.executorService = Executors.newFixedThreadPool(threads);
    }

    @Override
    public void close() throws IOException {
        final ExecutorService executorService = this.executorService;
        executorService.shutdown();
        try {
            // Wait a while for existing tasks to terminate
            if (!executorService.awaitTermination(TERMINATION_TIMEOUT, TERMINATION_TIMEOUT_UNIT)) {
                executorService.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!executorService.awaitTermination(TERMINATION_TIMEOUT, TERMINATION_TIMEOUT_UNIT)) {
                    logger.warn("Pool did not terminate in {} {}", TERMINATION_TIMEOUT, TERMINATION_TIMEOUT_UNIT);
                }
            }
        } catch (final InterruptedException ex) {
            // (Re-)Cancel if current thread also interrupted
            executorService.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

    public void execute(final LoginSession loginSession,
                        final LoginDetailsPacket loginDetailsPacket) {
        executorService.execute(() -> loginSession.handleUserLoginDetails(loginDetailsPacket));
    }

}
