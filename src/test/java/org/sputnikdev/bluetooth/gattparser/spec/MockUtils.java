package org.sputnikdev.bluetooth.gattparser.spec;

/*-
 * #%L
 * org.sputnikdev:bluetooth-gatt-parser
 * %%
 * Copyright (C) 2017 Sputnik Dev
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.*;

public class MockUtils {

    public static Field mockControlField(String name, boolean isMandatory, String... enumerations) {
        Field fieldMock = mock(
            Field.class,
            withSettings()
                .lenient()
                .defaultAnswer(CALLS_REAL_METHODS)
        );

        Enumerations enumMock = mock(
            Enumerations.class,
            withSettings()
                .lenient()
        );

        FieldFormat formatMock = mock(
            FieldFormat.class,
            withSettings()
                .lenient()
        );

        // field
        when(fieldMock.getName()).thenReturn(name);
        when(fieldMock.getEnumerations()).thenReturn(enumMock);
        when(fieldMock.getMultiplier()).thenReturn(null);
        when(fieldMock.getDecimalExponent()).thenReturn(null);
        when(fieldMock.getBinaryExponent()).thenReturn(null);
        when(fieldMock.getMinimum()).thenReturn(null);
        when(fieldMock.getMaximum()).thenReturn(null);
        when(fieldMock.getFormat()).thenReturn(formatMock);

        // format
        when(formatMock.getType()).thenReturn(FieldType.SINT);

        // enumerations
        List<Enumeration> enums = new ArrayList<>();
        when(enumMock.getEnumerations()).thenReturn(enums);
        if (isMandatory) {
            when(fieldMock.getRequirements()).thenReturn(singletonList("Mandatory"));
        }
        int i = 1;
        for (String enumeration : enumerations) {
            Enumeration en = mock(Enumeration.class, withSettings().lenient());
            when(en.getKey()).thenReturn(BigInteger.valueOf(i++));
            when(en.getRequires()).thenReturn(enumeration);
            enums.add(en);
        }

        return fieldMock;
    }

    public static Field mockField(String name, FieldType fieldType, int size, String... requirements) {
        Field fieldMock = mock(
            Field.class,
            withSettings()
                .lenient()
                .defaultAnswer(CALLS_REAL_METHODS)
        );

        FieldFormat formatMock = mock(
            FieldFormat.class,
            withSettings()
                .lenient()
        );

        // field
        when(fieldMock.getName()).thenReturn(name);
        when(fieldMock.getRequirements()).thenReturn(Arrays.asList(requirements));
        when(fieldMock.getMinimum()).thenReturn(null);
        when(fieldMock.getMaximum()).thenReturn(null);
        when(fieldMock.getFormat()).thenReturn(formatMock);

        // format
        when(formatMock.getType()).thenReturn(fieldType);
        when(formatMock.getSize()).thenReturn(size);

        return fieldMock;
    }

    public static Field mockFieldFormat(String name, String format) {
        Field fieldMock = mock(
            Field.class,
            withSettings()
                .lenient()
                .defaultAnswer(CALLS_REAL_METHODS)
        );

        when(fieldMock.getFormat()).thenReturn(FieldFormat.valueOf(format));
        when(fieldMock.getName()).thenReturn(name);
        when(fieldMock.getMultiplier()).thenReturn(null);
        when(fieldMock.getDecimalExponent()).thenReturn(null);
        when(fieldMock.getBinaryExponent()).thenReturn(null);
        when(fieldMock.getMinimum()).thenReturn(null);
        when(fieldMock.getMaximum()).thenReturn(null);

        return fieldMock;
    }

    public static Field mockFieldFormat(String name, String format, String... requirements) {
        Field fieldMock = mockFieldFormat(name, format);
        when(fieldMock.getRequirements()).thenReturn(Arrays.asList(requirements));
        return fieldMock;
    }


    public static Field mockField(String name, String... requirements) {
        return mockField(name, FieldType.SINT, 32, requirements);
    }

    public static Bit mockBit(int index, int size, String flagPrefix) {
        Bit bit = mock(Bit.class, withSettings().lenient());
        when(bit.getIndex()).thenReturn(index);
        when(bit.getSize()).thenReturn(size);
        for (int i = 0; i <= Math.pow(2, size); i++) {
            when(bit.getFlag((byte) i)).thenReturn(flagPrefix + i);
        }
        return bit;
    }

    public static Bit mockBit(int index, String flag) {
        Bit bit = mock(Bit.class, withSettings().lenient());
        when(bit.getIndex()).thenReturn(index);
        when(bit.getSize()).thenReturn(1);
        when(bit.getFlag((byte) 1)).thenReturn(flag);
        return bit;
    }


    public static Enumeration mockEnumeration(Integer key, String flag) {
        Enumeration enumeration = mock(Enumeration.class, withSettings().lenient());
        when(enumeration.getKey()).thenReturn(BigInteger.valueOf(key));
        when(enumeration.getRequires()).thenReturn(flag);
        return enumeration;
    }

    public static Enumeration mockEnumeration(Integer key, String flag, String value) {
        Enumeration enumeration = mock(Enumeration.class, withSettings().lenient());
        when(enumeration.getKey()).thenReturn(BigInteger.valueOf(key));
        when(enumeration.getRequires()).thenReturn(flag);
        when(enumeration.getValue()).thenReturn(value);
        return enumeration;
    }


}
