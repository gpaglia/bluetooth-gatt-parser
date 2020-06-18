package org.sputnikdev.bluetooth.gattparser.spec;

import org.sputnikdev.bluetooth.gattparser.num.RealNumberFormatter;

import java.util.List;
import java.util.Set;

public interface IFlagUtils {
  Set<String> getReadFlags(List<Field> fields, byte[] data, RealNumberFormatter formatter);

  // Field getFlags(List<Field> fields);
  // Field getOpCodes(List<Field> fields);

  static boolean isFlagsField(Field field) {
    return "flags".equalsIgnoreCase(field.getName()) && field.getBitField() != null;
  }

  static boolean isOpCodesField(Field field) {
    String name = field.getName();
    if ("op code".equalsIgnoreCase(name) || "op codes".equalsIgnoreCase(name)) {
      Enumerations enms = field.getEnumerations();
      if (enms != null) {
        List<Enumeration> list = enms.getEnumerations();
        if (! list.isEmpty()) {
          return true;
        } else {
          return false;
        }
      } else {
        return false;
      }
    } else {
      return false;
    }
    /*
    return ("op code".equalsIgnoreCase(name) || "op codes".equalsIgnoreCase(name))
        && field.getEnumerations() != null && !field.getEnumerations().getEnumerations().isEmpty();

     */
  }
}
