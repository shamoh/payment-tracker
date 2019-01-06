package cz.kramolis.exercises.paymenttracker;

import java.util.Optional;

/**
 * Service to convert specified currency amount to target currency.
 */
public interface ExchangeRateService {

    /**
     * Returns target currency of the service.
     *
     * @return service target currency
     */
    String getTargetCurrency();

    /**
     * Converts specified {@code currency} {@code amount} to {@link #getTargetCurrency() target currency}.
     *
     * @param currency source currency
     * @param amount   source amount to be converted
     *
     * @return converted value as an {@link Optional} or {@link Optional#empty()} in case there is no exchange rate
     * available.
     */
    Optional<Double> convert(String currency, double amount);

}
