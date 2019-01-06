package cz.kramolis.exercises.paymenttracker;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Tests {@link App}.
 */
public class AppTest {

    @Test
    public void testCreateDaemonThread() {
        // given
        App app = new App(mock(ExchangeRateService.class), mock(Tracker.class));

        // when
        Thread thread = app.createDaemonThread(() -> {});

        // then
        assertThat(thread.isDaemon(), equalTo(true));
    }

    @Test
    public void testProcessLine_emptyLine() {
        // given
        Tracker tracker = mock(Tracker.class);
        App app = new App(mock(ExchangeRateService.class), tracker);

        // when
        app.processLine("");

        // then
        verifyZeroInteractions(tracker);
    }

    @Test
    public void testProcessLine_positiveNumber() {
        // given
        Tracker tracker = mock(Tracker.class);
        App app = new App(mock(ExchangeRateService.class), tracker);

        // when
        app.processLine("AAA 23");

        // then
        verify(tracker).payment("AAA", 23);
    }

    @Test
    public void testProcessLine_negativeNumber() {
        // given
        Tracker tracker = mock(Tracker.class);
        App app = new App(mock(ExchangeRateService.class), tracker);

        // when
        app.processLine("AAA -23");

        // then
        verify(tracker).payment("AAA", -23);
    }

    @Test
    public void testProcessLine_StringIndexOutOfBoundsException() {
        // given
        Tracker tracker = mock(Tracker.class);
        App app = new App(mock(ExchangeRateService.class), tracker);

        // when
        app.processLine("A");

        // then
        verifyZeroInteractions(tracker);
    }

    @Test
    public void testProcessLine_NumberFormatException() {
        // given
        Tracker tracker = mock(Tracker.class);
        App app = new App(mock(ExchangeRateService.class), tracker);

        // when
        app.processLine("AAA n/a");

        // then
        verifyZeroInteractions(tracker);
    }

    @Test
    public void testProcessInputFile() {
        // given
        App app = spy(new App(mock(ExchangeRateService.class), mock(Tracker.class)));
        Path path = Paths.get(this.getClass().getClassLoader().getResource("input1.txt").getFile());

        // when
        app.processInputFile(path);

        // then
        verify(app).processLine("USD 1000");
        verify(app).processLine("HKD 100");
        verify(app).processLine("USD -100");
        verify(app).processLine("RMB 2000");
        verify(app).processLine("HKD 200");
    }

    @Test
    public void testFormatNetAmount() {
        // given
        ExchangeRateService exchangeRateService = mock(ExchangeRateService.class);
        when(exchangeRateService.convert(any(), anyDouble())).thenReturn(Optional.empty());
        App app = new App(exchangeRateService, mock(Tracker.class));

        // when
        String format = app.formatNetAmount("AAA", 123);

        // then
        assertThat(format, equalTo("AAA 123"));
    }

    @Test
    public void testFormatNetAmount_withUsdConversion() {
        // given
        ExchangeRateService exchangeRateService = mock(ExchangeRateService.class);
        when(exchangeRateService.convert(any(), anyDouble())).thenReturn(Optional.of(987.654321));
        when(exchangeRateService.getTargetCurrency()).thenReturn("USD");
        App app = new App(exchangeRateService, mock(Tracker.class));

        // when
        String format = app.formatNetAmount("AAA", 123);

        // then
        assertThat(format, equalTo("AAA 123 (USD 987.65)"));
    }

}
