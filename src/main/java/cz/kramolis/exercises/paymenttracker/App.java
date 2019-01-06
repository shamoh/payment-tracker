package cz.kramolis.exercises.paymenttracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Application functionality. Processes input file, reads console input and prints current net amounts every minute.
 */
public class App {

    private static final Logger logger = Logger.getLogger(App.class.getName());
    private static final String QUIT_INPUT = "quit";

    private final ExchangeRateService usdExchangeRateService;
    private final ScheduledExecutorService consoleOutputScheduler;
    private final Tracker tracker;
    private final DecimalFormat decimalFormat;

    /**
     * Creates new instance of the application.
     */
    public App(ExchangeRateService usdExchangeRateService, Tracker tracker) {
        this.usdExchangeRateService = usdExchangeRateService;
        this.tracker = tracker;

        consoleOutputScheduler = Executors.newSingleThreadScheduledExecutor(this::createDaemonThread);
        decimalFormat = new DecimalFormat("#0.00");
    }

    Thread createDaemonThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        return thread;
    }

    /**
     * Processes input file.
     *
     * @param inputFile path to file to be processed.
     */
    public void processInputFile(Path inputFile) {
        logger.info(() -> "Loading file " + inputFile);
        try (BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8)) {
            reader.lines().forEach(this::processLine);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex,
                    () -> "IO ERROR: Reading input file '" + inputFile.toString() + "'. Detail: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Runs the application. Reads console input and prints current net amounts every minute.
     */
    public void run() {
        logger.info("Type 'quit' to exit the program:");
        consoleOutputScheduler.scheduleAtFixedRate(this::printNetAmountsToConsole, 1, 1, TimeUnit.MINUTES);
        readConsoleInput();
    }

    private void printNetAmountsToConsole() {
        logger.fine("action=printNetAmountsToConsole");
        tracker.getNetAmounts().forEach(this::printNetAmount);
    }

    String formatNetAmount(String currency, int amount) {
        StringBuilder sb = new StringBuilder();
        sb.append(currency).append(" ").append(amount);

        usdExchangeRateService
                .convert(currency, amount)
                .ifPresent(usd -> sb
                        .append(" (")
                        .append(usdExchangeRateService.getTargetCurrency())
                        .append(" ")
                        .append(decimalFormat.format(usd))
                        .append(")")
                );

        return sb.toString();
    }

    private void printNetAmount(String currency, int amount) {
        System.out.println(formatNetAmount(currency, amount));
    }

    private void readConsoleInput() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equals(QUIT_INPUT)) {
                break;
            }
            processLine(line);
        }
        shutdown();
    }

    void shutdown() {
        consoleOutputScheduler.shutdownNow();
        logger.info("Goodbye blue sky...");
    }

    void processLine(String line) {
        if (line.isEmpty()) {
            return;
        }

        logger.fine(() -> "Input: '" + line + "'");
        try {
            String currency = line.substring(0, 3);
            int amount = Integer.parseInt(line.substring(3).trim());
            logger.fine(() -> "currency=" + currency + " amount=" + amount);
            tracker.payment(currency, amount);
        } catch (StringIndexOutOfBoundsException | NumberFormatException ex) {
            logger.log(Level.WARNING, ex, () -> "Wrong format of line '" + line + "'.");
        }
    }

}
