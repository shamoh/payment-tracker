package cz.kramolis.exercises.paymenttracker;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;
import org.junit.rules.TestRule;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Tests {@link SystemBackedExchangeRateServiceImpl}.
 */
public class SystemBackedExchangeRateServiceImplTest {

    @Rule
    public final TestRule restoreSystemProperties = new RestoreSystemProperties();

    @Test
    public void testGretTargetCurrency() {
        // given
        SystemBackedExchangeRateServiceImpl target = new SystemBackedExchangeRateServiceImpl("AAA");

        // when
        String targetCurrency = target.getTargetCurrency();

        // then
        assertThat(targetCurrency, equalTo("AAA"));
    }

    @Test
    public void testConvert_noRateProperty() {
        // given
        SystemBackedExchangeRateServiceImpl target = new SystemBackedExchangeRateServiceImpl("AAA");

        // when
        Optional<Double> converted = target.convert("AAA", 42);

        // then
        assertThat(converted, equalTo(Optional.empty()));
    }

    @Test
    public void testConvert_withRateProperty() {
        // given
        System.setProperty("paymentTracker.exchangeRate.AAA.AAA", "2");
        SystemBackedExchangeRateServiceImpl target = new SystemBackedExchangeRateServiceImpl("AAA");

        // when
        Optional<Double> converted = target.convert("AAA", 42);

        // then
        assertThat(converted.get(), equalTo(84.0));
    }

    @Test
    public void testConvert_twoRateProperties() {
        // given
        System.setProperty("paymentTracker.exchangeRate.AAA.BBB", "2");
        System.setProperty("paymentTracker.exchangeRate.AAA.CCC", "3");
        SystemBackedExchangeRateServiceImpl target = new SystemBackedExchangeRateServiceImpl("AAA");

        // when
        Optional<Double> convertedBBB = target.convert("BBB", 42);
        Optional<Double> convertedCCC = target.convert("CCC", 23);

        // then
        assertThat(convertedBBB.get(), equalTo(84.0));
        assertThat(convertedCCC.get(), equalTo(69.0));
    }

}
