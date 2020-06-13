package org.sputnikdev.bluetooth.gattparser;

import org.sputnikdev.bluetooth.gattparser.num.*;
import org.sputnikdev.bluetooth.gattparser.spec.FlagUtils;
import org.sputnikdev.bluetooth.gattparser.spec.IFlagUtils;

import java.util.Objects;
import java.util.function.Supplier;

public class GattParserConfigurationBuilder {
  public static final Supplier<RealNumberFormatter> DEFAULT_TWOS_COMPLEMENT_NUMBER_FORMATTER =
      TwosComplementNumberFormatter::new;
  public static final Supplier<FloatingPointNumberFormatter> DEFAULT_IEEE_754_FLOATING_POINT_NUMBER_FORMATTER =
      IEEE754FloatingPointNumberFormatter::new;
  public static final Supplier<FloatingPointNumberFormatter> DEFAULT_IEEE_11073_FLOATING_POINT_NUMBER_FORMATTER =
      IEEE11073FloatingPointNumberFormatter::new;
  public static final Supplier<IFlagUtils> DEFAULT_FLAG_UTILS = () -> new FlagUtils();

  private RealNumberFormatter twosComplementNumberFormatter;
  private FloatingPointNumberFormatter IEEE754FloatingPointNumberFormatter;
  private FloatingPointNumberFormatter IEEE11073FloatingPointNumberFormatter;
  private IFlagUtils flagUtils;

  public static GattParserConfigurationBuilder builder() { return new GattParserConfigurationBuilder(); }

  private GattParserConfigurationBuilder() {}

  public GattParserConfigurationBuilder setDefaults() {
    twosComplementNumberFormatter = null;
    IEEE754FloatingPointNumberFormatter = null;
    IEEE11073FloatingPointNumberFormatter = null;
    flagUtils = null;

    return this;
  }

  public GattParserConfigurationBuilder withTwosComplementNumberFormatter(RealNumberFormatter formatter) {
    Objects.requireNonNull(formatter, "Formatter, if specified, cannot be null");
    this.twosComplementNumberFormatter = formatter;
    return this;
  }

  public GattParserConfigurationBuilder withIEEE754FloatingPointNumberFormatter(FloatingPointNumberFormatter formatter) {
    Objects.requireNonNull(formatter, "Formatter, if specified, cannot be null");
    this.IEEE754FloatingPointNumberFormatter = formatter;
    return this;
  }

  public GattParserConfigurationBuilder withIEEE11073FloatingPointNumberFormatter(FloatingPointNumberFormatter formatter) {
    Objects.requireNonNull(formatter, "Formatter, if specified, cannot be null");
    this.IEEE11073FloatingPointNumberFormatter = formatter;
    return this;
  }

  public GattParserConfigurationBuilder withFlagUtils(IFlagUtils utils) {
    Objects.requireNonNull(utils, "FlagUtils instance, if specified, cannot be null");
    this.flagUtils = utils;
    return this;
  }

  public GattParserConfiguration toConfiguration() {
    return new GattParserConfiguration(
        Objects.requireNonNullElse(twosComplementNumberFormatter, DEFAULT_TWOS_COMPLEMENT_NUMBER_FORMATTER.get()),
        Objects.requireNonNullElse(IEEE754FloatingPointNumberFormatter, DEFAULT_IEEE_754_FLOATING_POINT_NUMBER_FORMATTER.get()),
        Objects.requireNonNullElse(IEEE11073FloatingPointNumberFormatter, DEFAULT_IEEE_11073_FLOATING_POINT_NUMBER_FORMATTER.get()),
        Objects.requireNonNullElse(flagUtils, DEFAULT_FLAG_UTILS.get())
    );
  }
}
