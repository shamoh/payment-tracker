package cz.kramolis.exercises.paymenttracker;

import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * Tests {@link Tracker}.
 */
public class TrackerTest {

    @Test
    public void testGetNetAmounts() {
        // given
        Tracker target = new Tracker();

        // when
        Map<String, Integer> netAmounts = target.getNetAmounts();

        // then
        assertThat(netAmounts.entrySet(), hasSize(0));
    }

    @Test
    public void testGetNetAmounts_plusMinusPayment() {
        // given
        Tracker target = new Tracker();

        // when
        target.payment("AAA", 23);
        target.payment("AAA", -23);
        Map<String, Integer> netAmounts = target.getNetAmounts();

        // then
        assertThat(netAmounts.entrySet(), hasSize(0));
    }

    @Test
    public void testGetNetAmounts_singlePayment() {
        // given
        Tracker target = new Tracker();

        // when
        target.payment("AAA", 23);
        Map<String, Integer> netAmounts = target.getNetAmounts();

        // then
        assertThat(netAmounts.entrySet(), hasSize(1));
        assertThat(netAmounts.get("AAA"), equalTo(23));
    }

    @Test
    public void testGetNetAmounts_sumPayment() {
        // given
        Tracker target = new Tracker();

        // when
        target.payment("AAA", 23);
        target.payment("AAA", 42);
        Map<String, Integer> netAmounts = target.getNetAmounts();

        // then
        assertThat(netAmounts.entrySet(), hasSize(1));
        assertThat(netAmounts.get("AAA"), equalTo(65));
    }

    @Test
    public void testGetNetAmounts_complex() {
        // given
        Tracker target = new Tracker();

        // when
        target.payment("AAA", 23);
        target.payment("AAA", 42);
        target.payment("BBB", 23);
        target.payment("BBB", -23);
        target.payment("CCC", 42);
        Map<String, Integer> netAmounts = target.getNetAmounts();

        // then
        assertThat(netAmounts.entrySet(), hasSize(2));
        assertThat(netAmounts.get("AAA"), equalTo(65));
        assertThat(netAmounts.get("CCC"), equalTo(42));
    }

}
