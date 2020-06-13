package org.sputnikdev.bluetooth.gattparser.spec;

import org.sputnikdev.bluetooth.gattparser.num.RealNumberFormatter;

import java.util.List;
import java.util.Set;

public interface IFlagUtils {
  Set<String> getReadFlags(List<Field> fields, byte[] data, RealNumberFormatter formatter);
  // Field getFlags(List<Field> fields);
  // Field getOpCodes(List<Field> fields);
}
