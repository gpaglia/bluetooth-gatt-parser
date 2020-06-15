package org.sputnikdev.bluetooth.gattparser;

import org.sputnikdev.bluetooth.gattparser.num.FloatingPointNumberFormatter;
import org.sputnikdev.bluetooth.gattparser.num.RealNumberFormatter;
import org.sputnikdev.bluetooth.gattparser.spec.BluetoothGattSpecificationReader;
import org.sputnikdev.bluetooth.gattparser.spec.IFlagUtils;

public final class GattParserConfiguration implements IGattParserConfiguration {
  private final RealNumberFormatter twosComplementNumberFormatter;
  private final FloatingPointNumberFormatter IEEE754FloatingPointNumberFormatter;
  private final FloatingPointNumberFormatter IEEE11073FloatingPointNumberFormatter;
  private final BluetoothGattSpecificationReader gattSpecificationReader;
  private final IFlagUtils flagUtils;
  private volatile BluetoothGattParser gattParser;

  GattParserConfiguration(
      RealNumberFormatter twosComplementNumberFormatter,
      FloatingPointNumberFormatter IEEE754FloatingPointNumberFormatter,
      FloatingPointNumberFormatter IEEE11073FloatingPointNumberFormatter,
      IFlagUtils flagUtils
  ) {
    this.twosComplementNumberFormatter = twosComplementNumberFormatter;
    this.IEEE754FloatingPointNumberFormatter = IEEE754FloatingPointNumberFormatter;
    this.IEEE11073FloatingPointNumberFormatter = IEEE11073FloatingPointNumberFormatter;
    this.gattSpecificationReader = new BluetoothGattSpecificationReader();
    this.flagUtils = flagUtils;
    // gatt parser is lazily initialized in the getter to avoid leaking 'this' references from constructor
    // in creation of GenericCharacteristicParser
  }

  @Override
  public BluetoothGattSpecificationReader getGattSpecificationReader() {
    return gattSpecificationReader;
  }

  @Override
  public BluetoothGattParser getGattParser() {
    if (gattParser == null) {
      synchronized (this) {
        if (gattParser == null) {
          // initialize it
          gattParser = new BluetoothGattParser(gattSpecificationReader, new GenericCharacteristicParser(this));
        }
      }
    }
    return gattParser;
  }

  @Override
  public RealNumberFormatter getTwosComplementNumberFormatter() {
    return twosComplementNumberFormatter;
  }

  @Override
  public FloatingPointNumberFormatter getIEEE754FloatingPointNumberFormatter() {
    return IEEE754FloatingPointNumberFormatter;
  }

  @Override
  public FloatingPointNumberFormatter getIEEE11073FloatingPointNumberFormatter() {
    return IEEE11073FloatingPointNumberFormatter;
  }

  @Override
  public IFlagUtils getFlagUtils() { return flagUtils; }
}
