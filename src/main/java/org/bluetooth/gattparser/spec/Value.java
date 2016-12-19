package org.bluetooth.gattparser.spec;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Value")
public class Value {

    @XStreamImplicit
    private List<Field> fields;

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public Field getFlags() {
        for (Field field : getFields()) {
            if ("flags".equalsIgnoreCase(field.getName()) && field.getBitField() != null) {
                return field;
            }
        }
        return null;
    }
}