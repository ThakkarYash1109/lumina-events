package com.lumina.concurrency;

import java.util.concurrent.*;
import java.time.LocalDateTime;

/**
 * Multithreading Background Task:
 * Asynchronously processes booking notifications, dispatching alerts to hosts and vendors
 * without blocking API response times, using a bounded thread pool and blocking queue.
 */
public class NotificationDispatcher {
    private static final BlockingQueue<NotificationTask> queue = new LinkedBlockingQueue<>(100);
    private static final ExecutorService workerPool = Executors.newFixedThreadPool(2, new ThreadFactory() {
        private int count = 1;
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "Lumina-NotificationWorker-" + count++);
            t.setDaemon(true);
            return t;
        }
    });

    static {
        // Start background worker consumer thread
        workerPool.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    NotificationTask task = queue.take();
                    task.execute();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception ex) {
                    System.err.println("[NotificationDispatcher] Error executing task: " + ex.getMessage());
                }
            }
        });
    }

    public static void dispatchAsync(String recipient, String message, String type) {
        NotificationTask task = new NotificationTask(recipient, message, type);
        boolean offered = queue.offer(task);
        if (!offered) {
            System.err.println("[NotificationDispatcher] Queue capacity reached, executing inline fallback.");
            workerPool.submit(task::execute);
        }
    }

    public static int getPendingQueueSize() {
        return queue.size();
    }

    public static class NotificationTask {
        private final String recipient;
        private final String message;
        private final String type;
        private final LocalDateTime enqueuedAt;

        public NotificationTask(String recipient, String message, String type) {
            this.recipient = recipient;
            this.message = message;
            this.type = type;
            this.enqueuedAt = LocalDateTime.now();
        }

        public void execute() {
            try {
                // Simulate asynchronous notification delivery (e.g., SMTP email / SMS gateway)
                Thread.sleep(80);
                System.out.printf("[ASYNC NOTIFICATION] [%s] -> %s: %s (queued at %s)%n",
                        type, recipient, message, enqueuedAt.toLocalTime());
            } catch (InterruptedException ignored) {}
        }
    }
}

