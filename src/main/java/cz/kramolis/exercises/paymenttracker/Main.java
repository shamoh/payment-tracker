package cz.kramolis.exercises.paymenttracker;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main executable class.
 */
public class Main {

    private static final String USD_CURRENCY = "USD";

    public static void main(String[] args) {
        if (args.length > 1) {
            System.err.println("Wrong number of arguments [" + args.length + "].");
            System.exit(1);
        }

        App app = new App(loadUsdExchangeRateService(), new Tracker());
        if (args.length == 1) {
            app.processInputFile(findInputFile(args[0]));
        }
        app.run();
    }

    private static ExchangeRateService loadUsdExchangeRateService() {
        return new SystemBackedExchangeRateServiceImpl(USD_CURRENCY);
    }

    static Path findInputFile(String filename) {
        Path path = Paths.get(filename);
        if (Files.notExists(path)) {
            throw new IllegalArgumentException("File " + filename + " does not exists.");
        }
        return path;
    }

}
