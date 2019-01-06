package cz.kramolis.exercises.paymenttracker;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Payment tracker. Holds current net amounts.
 */
public class Tracker {

    private final Map<String, Integer> netAmountsPerCurrency;

    public Tracker() {
        netAmountsPerCurrency = new ConcurrentHashMap<>();
    }

    /**
     * Returns immutable copy of current not zero net amounts.
     *
     * @return current not zero net amounts
     */
    public Map<String, Integer> getNetAmounts() {
        ImmutableMap.Builder<String, Integer> resultBuilder = ImmutableMap.builderWithExpectedSize(netAmountsPerCurrency.size());
        netAmountsPerCurrency.forEach((k, v) -> {
            if (v != 0) {
                resultBuilder.put(k, v);
            }
        });
        return resultBuilder.build();
    }

    /**
     * Track another payment.
     *
     * @param currency payment currency
     * @param amount   payment amount
     */
    public void payment(String currency, int amount) {
        netAmountsPerCurrency.merge(currency, amount, Tracker::sum);
    }

    private static Integer sum(Integer a, Integer b) {
        return a + b;
    }

}
