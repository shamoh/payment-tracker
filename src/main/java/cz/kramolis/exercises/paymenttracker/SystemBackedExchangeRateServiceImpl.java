package cz.kramolis.exercises.paymenttracker;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * Service to convert specified currency amount to target currency.
 */
class SystemBackedExchangeRateServiceImpl implements ExchangeRateService {

    private static final Logger logger = Logger.getLogger(SystemBackedExchangeRateServiceImpl.class.getName());
    private final String targetCurrency;

    public SystemBackedExchangeRateServiceImpl(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    @Override
    public String getTargetCurrency() {
        return targetCurrency;
    }

    @Override
    public Optional<Double> convert(String currency, double amount) {
        String property = "paymentTracker.exchangeRate." + targetCurrency + "." + currency;
        String rate = System.getProperty(property);

        logger.fine(() -> "property=" + property + " value=" + rate);

        if (rate == null) {
            return Optional.empty();
        } else {
            return Optional.of(amount * Double.parseDouble(rate));
        }
    }

}
