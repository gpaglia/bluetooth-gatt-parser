package org.sputnikdev.bluetooth.gattparser;

import org.sputnikdev.bluetooth.gattparser.num.FloatingPointNumberFormatter;
import org.sputnikdev.bluetooth.gattparser.num.RealNumberFormatter;
import org.sputnikdev.bluetooth.gattparser.spec.BluetoothGattSpecificationReader;
import org.sputnikdev.bluetooth.gattparser.spec.IFlagUtils;

public interface IGattParserConfiguration {
  BluetoothGattSpecificationReader getGattSpecificationReader();
  BluetoothGattParser getGattParser();
  RealNumberFormatter getTwosComplementNumberFormatter();
  FloatingPointNumberFormatter getIEEE754FloatingPointNumberFormatter();
  FloatingPointNumberFormatter getIEEE11073FloatingPointNumberFormatter();
  IFlagUtils getFlagUtils();
}
