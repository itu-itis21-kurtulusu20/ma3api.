
package org.etsi.uri.ts102204.v1_1.turktelekom;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessagingModeType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MessagingModeType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="synch"/&gt;
 *     &lt;enumeration value="asynchClientServer"/&gt;
 *     &lt;enumeration value="asynchServerServer"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "MessagingModeType")
@XmlEnum
public enum MessagingModeType {

    @XmlEnumValue("synch")
    SYNCH("synch"),
    @XmlEnumValue("asynchClientServer")
    ASYNCH_CLIENT_SERVER("asynchClientServer"),
    @XmlEnumValue("asynchServerServer")
    ASYNCH_SERVER_SERVER("asynchServerServer");
    private final String value;

    MessagingModeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MessagingModeType fromValue(String v) {
        for (MessagingModeType c: MessagingModeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
