package cafemanagement.exception;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GlobalExceptionHandler {

    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());
    private static final int MAX_REQUESTS_PER_MINUTE = 200;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static volatile int requestCount = 0;
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final long RETRY_DELAY_MS = 2000;
    private static final String ERROR_LOG_FILE = "errors.log";

    private static final Map<Class<? extends Exception>, ExceptionHandler> exceptionHandlers = new HashMap<>();

    static {
        exceptionHandlers.put(GracefulRecoveryException.class, e -> {
            logger.log(Level.INFO, "Graceful recovery: " + e.getMessage());
            recoverGracefully((GracefulRecoveryException) e);
        });
        exceptionHandlers.put(LoadSheddingException.class, e -> {
            logger.log(Level.SEVERE, "Load shedding error: " + e.getMessage());
            throttleRequests();
        });
        exceptionHandlers.put(AuthenticationException.class, e -> {
            logger.log(Level.SEVERE, "Authentication error: " + e.getMessage());
        });
        exceptionHandlers.put(ClientHandlerException.class, e -> {
            logger.log(Level.SEVERE, "Client handler error: " + e.getMessage());
        });
        exceptionHandlers.put(ServerException.class, e -> {
            logger.log(Level.SEVERE, "Server error: " + e.getMessage());
        });
        exceptionHandlers.put(RetryException.class, e -> {
            logger.log(Level.WARNING, "Retry error: " + e.getMessage());
            attemptRetry((RetryException) e);
        });
        exceptionHandlers.put(FallbackException.class, e -> {
            logger.log(Level.SEVERE, "Fallback error: " + e.getMessage());
            handleFallback((FallbackException) e);
        });
        exceptionHandlers.put(ErrorReportingException.class, e -> {
            logger.log(Level.SEVERE, "Error reporting: " + e.getMessage());
            reportError((ErrorReportingException) e);
        });
    }

    public static void handle(Exception e) {
        ExceptionHandler handler = exceptionHandlers.get(e.getClass());
        if (handler != null) {
            handler.handle(e);
        } else {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage());
        }
    }

    private static void throttleRequests() {
        scheduler.scheduleAtFixedRate(() -> requestCount = 0, 1, 1, TimeUnit.MINUTES);
        if (requestCount >= MAX_REQUESTS_PER_MINUTE) {
            System.out.println("Throttling requests: Rate limit exceeded.");
        } else {
            requestCount++;
        }
    }

    private static void recoverGracefully(GracefulRecoveryException exception) {
        System.out.println("Attempting graceful recovery...");
        int attempts = 0;
        boolean recovered = false;
        
        while (attempts < MAX_RETRY_ATTEMPTS && !recovered) {
            try {
                retryOperation(exception);
                recovered = true;
                System.out.println("Recovery successful.");
            } catch (Exception e) {
                attempts++;
                System.out.println("Recovery attempt " + attempts + " failed.");
                if (attempts < MAX_RETRY_ATTEMPTS) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        System.err.println("Recovery retry interrupted: " + ie.getMessage());
                    }
                }
            }
        }
        
        if (!recovered) {
            System.err.println("Failed to recover gracefully after " + MAX_RETRY_ATTEMPTS + " attempts.");
        }
    }

    private static void retryOperation(GracefulRecoveryException exception) throws Exception {
        System.out.println("Retrying operation.");
    }

    private static void attemptRetry(RetryException exception) {
        int attempts = 0;
        boolean success = false;
        
        while (attempts < MAX_RETRY_ATTEMPTS && !success) {
            attempts++;
            System.out.println("Retry attempt " + attempts + ".");
            
            try {
                success = true;
                System.out.println("Retry attempt " + attempts + " succeeded.");
            } catch (Exception e) {
                System.out.println("Retry attempt " + attempts + " failed.");
                if (attempts < MAX_RETRY_ATTEMPTS) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        System.err.println("Retry delay interrupted: " + ie.getMessage());
                    }
                } else {
                    System.err.println("Failed to complete operation after " + MAX_RETRY_ATTEMPTS + " attempts.");
                }
            }
        }
    }

    private static void handleFallback(FallbackException exception) {
        System.out.println("Fallback mechanism triggered.");
    }
    
    private static void reportError(ErrorReportingException exception) {
        try {
            logErrorToFile(exception);
            System.out.println("Error reported successfully.");
        } catch (Exception e) {
            System.err.println("Failed to report error: " + e.getMessage());
        }
    }

    private static void logErrorToFile(ErrorReportingException exception) throws IOException {
        try (FileWriter fileWriter = new FileWriter(ERROR_LOG_FILE, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("Error reported: " + exception.getMessage());
            exception.printStackTrace(printWriter);
        }
    }
    
    @FunctionalInterface
    private interface ExceptionHandler {
        void handle(Exception e);
    }
}
