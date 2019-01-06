# Payment Tracker

If the user enters invalid input, it prints an error message to console error output.

Exchange conversion rates are configurable via system properties in format:
`paymentTracker.exchangeRate.TO.FROM=RATE`. Where `TO` and `FROM` are currency codes and `RATE` is the conversion rate in `double`,
e.g. `paymentTracker.exchangeRate.USD.RMB=0.14557`.

# How to build it

It uses Gradle to build, run and create distribution pack of an application.
You can use Gradle Wrapper, command line `gradlew` in the application root directory.

```bash
gradlew clean
gradlew build
```

# How to run it

## Using Gradle

No input file:

```bash
gradlew run --console=plain
```

With input file specified:

```bash
gradlew run --console=plain --args src/test/resources/input1.txt
```

See `build.gradle.kts` for details about exchange rates configuration.

## Using Distribution

Build:

```bash
gradlew assemble
```

Unpack distribution:

```bash
unzip build/distributions/payment-tracker.zip -d build/distributions/
```

Run the application, specify exchange rates using `PAYMENT_TRACKER_OPTS` env variable and input file:

```bash
PAYMENT_TRACKER_OPTS="-DpaymentTracker.exchangeRate.USD.RMB=0.14557 -DpaymentTracker.exchangeRate.USD.HKD=0.12764" \
    build/distributions/payment-tracker/bin/payment-tracker src/test/resources/input1.txt
```
