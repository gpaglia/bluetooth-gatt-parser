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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.sputnikdev.bluetooth.gattparser.num.RealNumberFormatter;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Vlad Kolotov
 */
@XStreamAlias("Field")
public class Field {

    @XStreamAsAttribute
    private String name;
    @XStreamAlias("InformativeText")
    private String informativeText;
    @XStreamImplicit(itemFieldName = "Requirement")
    private List<String> requirements;
    @XStreamAlias("Reference")
    private String reference;
    @XStreamAlias("Format")
    private String format;
    @XStreamAlias("BitField")
    private BitField bitField;
    @XStreamAlias("DecimalExponent")
    private Integer decimalExponent;
    @XStreamAlias("BinaryExponent")
    private Integer binaryExponent;
    @XStreamAlias("Multiplier")
    private Integer multiplier;
    @XStreamAlias("Unit")
    private String unit;
    @XStreamAlias("Minimum")
    private Double minimum;
    @XStreamAlias("Maximum")
    private Double maximum;
    @XStreamAlias("Offset")
    private Double offset;
    @XStreamAlias("Enumerations")
    private Enumerations enumerations;

    // extensions
    @XStreamAsAttribute
    private boolean unknown;
    @XStreamAsAttribute
    private boolean system;

    public String getName() {
        return name != null ? name.trim() : null;
    }

    public String getInformativeText() {
        return informativeText;
    }

    public List<String> getRequirements() {
        return requirements;
    }

    public FieldFormat getFormat() {
        return FieldFormat.valueOf(format);
    }

    public BitField getBitField() {
        return bitField;
    }

    public Integer getDecimalExponent() {
        return decimalExponent;
    }

    public Integer getBinaryExponent() {
        return binaryExponent;
    }

    public Integer getMultiplier() {
        return multiplier;
    }

    public String getUnit() {
        return unit;
    }

    public Double getMinimum() {
        return minimum;
    }

    public Double getMaximum() {
        return maximum;
    }

    public Double getOffset() {
        return offset;
    }

    public Enumerations getEnumerations() {
        return enumerations;
    }

    public String getReference() {
        return reference;
    }

    public boolean isUnknown() {
        return unknown;
    }

    public boolean isSystem() {
        return system;
    }

    public boolean isFlagField() {
        return isFlagsFieldLocal();
    }

    public boolean isOpCodesField() {
        return isOpCodesFieldLocal();
    }

    public boolean hasEnumerations() {
        return enumerations != null && enumerations.getEnumerations() != null
                && !enumerations.getEnumerations().isEmpty();
    }

    // Added by GP

    public String getRequires(BigInteger key) {
        return getEnumeration(key).map(Enumeration::getRequires).orElse(null);
    }

    public Optional<Enumeration> getEnumeration(BigInteger key) {
        if (key == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(getEnumerations()).map(Enumerations::getEnumerations)
            .map(Collection::stream).orElse(Stream.empty())
            .filter(e -> key.equals(e.getKey())).findAny();
    }

    public List<Enumeration> getEnumerations(String value) {
        if (value == null) {
            return Collections.emptyList();
        }

        return Optional.ofNullable(getEnumerations()).map(Enumerations::getEnumerations)
            .map(Collection::stream).orElse(Stream.empty())
            .filter(e -> value.equals(e.getValue()))
            .collect(Collectors.toList());
    }

    public Set<String> getAllFlags() {
        Set<String> result = new HashSet<>();
        if (getBitField() != null) {
            for (Bit bit : getBitField().getBits()) {
                for (Enumeration enumeration : bit.getEnumerations().getEnumerations()) {
                    if (enumeration.getRequires() != null) {
                        result.add(enumeration.getRequires());
                    }
                }
            }
        }
        return result;
    }

    public Set<String> getAllOpCodes() {
        Set<String> result = new HashSet<>();
        if (getEnumerations() == null || getEnumerations().getEnumerations() == null) {
            return Collections.emptySet();
        }
        for (Enumeration enumeration : getEnumerations().getEnumerations()) {
            result.add(enumeration.getRequires());
        }
        return result;
    }

    int[] parseReadFlags(byte[] raw, int index, RealNumberFormatter formatter) {
        BitSet bitSet = BitSet.valueOf(raw).get(index, index + getFormat().getSize());
        List<Bit> bits = getBitField().getBits();
        int[] flags = new int[bits.size()];
        int offset = 0;
        for (int i = 0; i < bits.size(); i++) {
            int size = bits.get(i).getSize();
            flags[i] = formatter.deserializeInteger(bitSet.get(offset, offset + size), size, false);
            offset += size;
        }
        return flags;
    }
    // private helper methods

    private boolean isFlagsFieldLocal() {
        return "flags".equalsIgnoreCase(getName()) && getBitField() != null;
    }

    private boolean isOpCodesFieldLocal() {
        String name = getName();
        return ("op code".equalsIgnoreCase(name) || "op codes".equalsIgnoreCase(name))
            && getEnumerations() != null && !getEnumerations().getEnumerations().isEmpty();
    }

}
