package org.sputnikdev.bluetooth.gattparser.problem;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.basic.*;
import com.thoughtworks.xstream.converters.collections.*;
import com.thoughtworks.xstream.converters.extended.*;
import com.thoughtworks.xstream.converters.reflection.ExternalizableConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.SerializableConverter;
import com.thoughtworks.xstream.core.util.SelfStreamingInstanceChecker;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.WildcardTypePermission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sputnikdev.bluetooth.gattparser.spec.*;

import java.io.Writer;
import java.util.Comparator;
import java.util.TreeMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ReflectiveConversionTest {
  private XStream xstream;

  @BeforeEach
  public void setup() {
    System.out.println("Starting setup ...");
     xstream = new XStream(new DomDriver() {
      @Override
      public HierarchicalStreamWriter createWriter(Writer out) {
        return new PrettyPrintWriter(out, "    ");
      }
    }) {

      // only register the converters we need; other converters generate a private access warning in the console on Java9+...
      @Override
      protected void setupConverters() {
        System.out.println("... in xstream setup ...");
                    /*
                    registerConverter(new NullConverter(), PRIORITY_VERY_HIGH);
                    registerConverter(new IntConverter(), PRIORITY_NORMAL);
                    registerConverter(new FloatConverter(), PRIORITY_NORMAL);
                    registerConverter(new DoubleConverter(), PRIORITY_NORMAL);
                    registerConverter(new LongConverter(), PRIORITY_NORMAL);
                    registerConverter(new ShortConverter(), PRIORITY_NORMAL);
                    registerConverter(new BooleanConverter(), PRIORITY_NORMAL);
                    registerConverter(new ByteConverter(), PRIORITY_NORMAL);
                    registerConverter(new StringConverter(), PRIORITY_NORMAL);
                    registerConverter(new DateConverter(), PRIORITY_NORMAL);
                    registerConverter(new CollectionConverter(getMapper()), PRIORITY_NORMAL);
                    registerConverter(new ReflectionConverter(getMapper(), getReflectionProvider()), PRIORITY_VERY_LOW);
                    */
        registerConverter(new ReflectionConverter(getMapper(), getReflectionProvider()), PRIORITY_VERY_LOW);

        registerConverter(new SerializableConverter(getMapper(), getReflectionProvider(), getClassLoaderReference()), PRIORITY_LOW);
        registerConverter(new ExternalizableConverter(getMapper(), getClassLoaderReference()), PRIORITY_LOW);

        registerConverter(new NullConverter(), PRIORITY_VERY_HIGH);
        registerConverter(new IntConverter(), PRIORITY_NORMAL);
        registerConverter(new FloatConverter(), PRIORITY_NORMAL);
        registerConverter(new DoubleConverter(), PRIORITY_NORMAL);
        registerConverter(new LongConverter(), PRIORITY_NORMAL);
        registerConverter(new ShortConverter(), PRIORITY_NORMAL);
        registerConverter((Converter)new CharConverter(), PRIORITY_NORMAL);
        registerConverter(new BooleanConverter(), PRIORITY_NORMAL);
        registerConverter(new ByteConverter(), PRIORITY_NORMAL);

        registerConverter(new StringConverter(), PRIORITY_NORMAL);
        registerConverter(new StringBufferConverter(), PRIORITY_NORMAL);
        registerConverter(new DateConverter(), PRIORITY_NORMAL);
        registerConverter(new BitSetConverter(), PRIORITY_NORMAL);
        registerConverter(new URIConverter(), PRIORITY_NORMAL);
        registerConverter(new URLConverter(), PRIORITY_NORMAL);
        registerConverter(new BigIntegerConverter(), PRIORITY_NORMAL);
        registerConverter(new BigDecimalConverter(), PRIORITY_NORMAL);

        registerConverter(new ArrayConverter(getMapper()), PRIORITY_NORMAL);
        registerConverter(new CharArrayConverter(), PRIORITY_NORMAL);
        registerConverter(new CollectionConverter(getMapper()), PRIORITY_NORMAL);
        registerConverter(new MapConverter(getMapper()), PRIORITY_NORMAL);
        // registerConverter(new TreeMapConverter(getMapper()), PRIORITY_NORMAL);
        // registerConverter(new TreeSetConverter(getMapper()), PRIORITY_NORMAL);
        registerConverter(new SingletonCollectionConverter(getMapper()), PRIORITY_NORMAL);
        registerConverter(new SingletonMapConverter(getMapper()), PRIORITY_NORMAL);
        // registerConverter(new PropertiesConverter(), PRIORITY_NORMAL);
        registerConverter((Converter)new EncodedByteArrayConverter(), PRIORITY_NORMAL);

        registerConverter(new FileConverter(), PRIORITY_NORMAL);
                    /*
                    if (JVM.isSQLAvailable()) {
                        registerConverter(new SqlTimestampConverter(), PRIORITY_NORMAL);
                        registerConverter(new SqlTimeConverter(), PRIORITY_NORMAL);
                        registerConverter(new SqlDateConverter(), PRIORITY_NORMAL);
                    }
                    */

        registerConverter(new JavaClassConverter(getClassLoaderReference()), PRIORITY_NORMAL);
        registerConverter(new JavaMethodConverter(getClassLoaderReference()), PRIORITY_NORMAL);
        registerConverter(new JavaFieldConverter(getClassLoaderReference()), PRIORITY_NORMAL);

                    /*
                    if (JVM.isAWTAvailable()) {
                        registerConverter(new ColorConverter(), PRIORITY_NORMAL);
                    }
                    if (JVM.isSwingAvailable()) {
                        registerConverter(new LookAndFeelConverter(getMapper(), getReflectionProvider()), PRIORITY_NORMAL);
                    }
                    */
        registerConverter(new LocaleConverter(), PRIORITY_NORMAL);
        registerConverter(new GregorianCalendarConverter(), PRIORITY_NORMAL);

        registerConverter(new SelfStreamingInstanceChecker(getConverterLookup(), this), PRIORITY_NORMAL);

        System.out.println(" ... out of xstream setup ...");
      }

    };
    // setup proper security by limiting which classes can be loaded by XStream
    xstream.addPermission(NoTypePermission.NONE);
    xstream.addPermission(
        new WildcardTypePermission(
            new String[] {this.getClass().getPackageName() + ".**", "java.util.**", "java.lang.**" }
        )
    );
    // ***
    xstream.autodetectAnnotations(true);
    xstream.processAnnotations(Bit.class);
    xstream.processAnnotations(BitField.class);
    xstream.processAnnotations(Characteristic.class);
    xstream.processAnnotations(Enumeration.class);
    xstream.processAnnotations(Enumerations.class);
    xstream.processAnnotations(Field.class);
    xstream.processAnnotations(InformativeText.class);
    xstream.processAnnotations(Service.class);
    xstream.processAnnotations(Value.class);
    xstream.processAnnotations(Reserved.class);
    xstream.processAnnotations(Examples.class);
    xstream.processAnnotations(CharacteristicAccess.class);
    xstream.processAnnotations(Characteristics.class);
    xstream.processAnnotations(Properties.class);
    xstream.ignoreUnknownElements();
    xstream.setClassLoader(Characteristic.class.getClassLoader());

    System.out.println("Finished setup ...");

  }

  @Test
  public void test() {

    final TreeMap<String, String> mapNoC  = new TreeMap<>();
    final TreeMap<String, String> mapWithC = new TreeMap<>(Comparator.reverseOrder());

    for (int i = 1; i < 5; i++) {
      mapNoC.put("key" + i, "value" + i);
      mapWithC.put("key" + i, "value" + i);
    }

    System.out.println("Objects initialized ...");
    // final String xml1 = xstream.toXML("CiaoCiao");

    /*
    final String xmlNoC = xstream.toXML(mapNoC);
    System.out.println("mapNoC marshalled  ...");


    final String xmlWithC = xstream.toXML(mapWithC);
    System.out.println("mapWithC marshalled  ...");

    System.out.println("xmlNoC = " + xmlNoC);
    System.out.println("xmlWithC = " + xmlWithC);

    @SuppressWarnings("unchecked")
    final TreeMap<String, String> newMapNoC = (TreeMap<String, String>) xstream.fromXML(xmlNoC);

    @SuppressWarnings("unchecked")
    final TreeMap<String, String> newMapWithC = (TreeMap<String, String>) xstream.fromXML(xmlWithC);
    */

    // System.out.println("xml1: " + xml1);
    System.out.println("Objects unmarshalled back ...");

    // assertThat(mapNoC, is(newMapNoC));
    // assertThat(mapWithC, is(newMapWithC));

  }
}
