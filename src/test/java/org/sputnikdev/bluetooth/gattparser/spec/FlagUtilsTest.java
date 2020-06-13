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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.sputnikdev.bluetooth.gattparser.BluetoothGattParserFactory;
import org.sputnikdev.bluetooth.gattparser.GattParserConfigurationBuilder;
import org.sputnikdev.bluetooth.gattparser.MockUtils;
import org.sputnikdev.bluetooth.gattparser.num.RealNumberFormatter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class FlagUtilsTest {

    private FlagUtils flagUtils = new FlagUtils();

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Field flagField;

    @Mock
    private RealNumberFormatter twosComplementNumberFormatter;

    @BeforeEach
    public void setUp() {
        when(flagField.getName()).thenReturn("fLags");
    }

    @Test
    public void testGetReadFlags() throws Exception {
        final RealNumberFormatter formatter = GattParserConfigurationBuilder.DEFAULT_TWOS_COMPLEMENT_NUMBER_FORMATTER.get();
        List<Bit> bits = new ArrayList<>();
        when(flagField.getBitField().getBits()).thenReturn(bits);
        when(flagField.getFormat().getSize()).thenReturn(15);
        bits.add(MockUtils.mockBit(0, 1, "A"));
        bits.add(MockUtils.mockBit(1, 2, "B"));
        bits.add(MockUtils.mockBit(2, 1, "C"));
        bits.add(MockUtils.mockBit(3, 3, "D"));
        bits.add(MockUtils.mockBit(4, 2, "E"));
        bits.add(MockUtils.mockBit(5, 2, "F"));
        bits.add(MockUtils.mockBit(6, 4, "G"));

        byte[] raw = new byte[] { (byte) 0b10100101, (byte) 0b01010001 };
        int[] flagsValues = flagField.parseReadFlags(raw, 0, formatter);
        assertArrayEquals(new int[] {1, 2, 0, 2, 3, 0, 10}, flagsValues);

        Set<String> flags = flagUtils.getReadFlags(Arrays.asList(flagField), raw, formatter);
        assertEquals(7, flags.size());
        assertTrue(flags.containsAll(Arrays.asList("A1", "B2", "C0", "D2", "E3", "F0", "G10")));
    }

    @Test
    public void testGetRequires() throws Exception {
        List<Enumeration> enumerations = new ArrayList<>();
        enumerations.add(MockUtils.mockEnumeration(1, "C1"));
        enumerations.add(MockUtils.mockEnumeration(2, null));
        enumerations.add(MockUtils.mockEnumeration(3, "C2"));
        when(flagField.getEnumerations().getEnumerations()).thenReturn(enumerations);

        assertEquals("C1", flagField.getRequires(new BigInteger("1")));
        assertNull(flagField.getRequires(new BigInteger("2")));
        assertEquals("C2", flagField.getRequires(new BigInteger("3")));

        assertNull(flagField.getRequires(null));
        when(flagField.getEnumerations().getEnumerations()).thenReturn(null);
        assertNull(flagField.getRequires(new BigInteger("1")));
    }

    @Test
    public void testGetEnumerationValue() throws Exception {
        List<Enumeration> enumerations = new ArrayList<>();
        enumerations.add(MockUtils.mockEnumeration(1, "C1", "First"));
        enumerations.add(MockUtils.mockEnumeration(2, "C2", "Second"));
        enumerations.add(MockUtils.mockEnumeration(3, "C2", "Third"));
        when(flagField.getEnumerations().getEnumerations()).thenReturn(enumerations);

        assertEquals("C2", flagField.getEnumeration(new BigInteger("2")).get().getRequires());
        assertEquals("Second", flagField.getEnumeration(new BigInteger("2")).get().getValue());

        assertFalse(flagField.getEnumeration(new BigInteger("4")).isPresent());
    }

    @Test
    public void testGetAllOpCodes() {
        assertTrue(flagField.getAllOpCodes().isEmpty());

        List<Enumeration> enumerations = new ArrayList<>();
        enumerations.add(MockUtils.mockEnumeration(1, "C1"));
        enumerations.add(MockUtils.mockEnumeration(2, null));
        enumerations.add(MockUtils.mockEnumeration(3, "C2"));
        when(flagField.getEnumerations().getEnumerations()).thenReturn(enumerations);
        assertTrue(flagField.getAllOpCodes().containsAll(Arrays.asList("C1", "C2")));
    }

    @Test
    public void testGetReadFlagsComplex() {

        List<Bit> bits = new ArrayList<>();

        bits.add(MockUtils.mockBit(0, 1, "A"));
        bits.add(MockUtils.mockBit(1, 2, "B"));
        bits.add(MockUtils.mockBit(2, 1, "C"));
        bits.add(MockUtils.mockBit(3, 3, "D"));

        bits.add(MockUtils.mockBit(4, 2, "E"));
        bits.add(MockUtils.mockBit(5, 2, "F"));
        bits.add(MockUtils.mockBit(6, 4, "G"));

        when(flagField.getBitField().getBits()).thenReturn(bits);
        when(flagField.getFormat()).thenReturn(FieldFormat.valueOf("15bit"));
        when(twosComplementNumberFormatter.deserializeInteger(BitSet.valueOf(new byte[]{0b1}), 1, false)).thenReturn(1);
        when(twosComplementNumberFormatter.deserializeInteger(BitSet.valueOf(new byte[]{0b10}), 2, false)).thenReturn(2);
        when(twosComplementNumberFormatter.deserializeInteger(BitSet.valueOf(new byte[]{0b0}), 1, false)).thenReturn(0);
        when(twosComplementNumberFormatter.deserializeInteger(BitSet.valueOf(new byte[]{0b010}), 3, false)).thenReturn(2);
        when(twosComplementNumberFormatter.deserializeInteger(BitSet.valueOf(new byte[]{0b11}), 2, false)).thenReturn(3);
        when(twosComplementNumberFormatter.deserializeInteger(BitSet.valueOf(new byte[]{0b0}), 2, false)).thenReturn(0);
        when(twosComplementNumberFormatter.deserializeInteger(BitSet.valueOf(new byte[]{0b1010}), 4, false)).thenReturn(10);

        Set<String> flags = flagUtils.getReadFlags(
            Arrays.asList(flagField),
            new byte[] {(byte) 0b10100101, (byte) 0b01010001},
            twosComplementNumberFormatter);
        assertTrue(flags.contains("A1"));
        assertTrue(flags.contains("B2"));
        assertTrue(flags.contains("C0"));
        assertTrue(flags.contains("D2"));
        assertTrue(flags.contains("E3"));
        assertTrue(flags.contains("F0"));
        assertTrue(flags.contains("G10"));
    }

}
