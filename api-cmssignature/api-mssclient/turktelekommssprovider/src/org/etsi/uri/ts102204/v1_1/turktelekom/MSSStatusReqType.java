
package org.etsi.uri.ts102204.v1_1.turktelekom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for MSS_StatusReqType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="MSS_StatusReqType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://uri.etsi.org/TS102204/v1.1.2#}MessageAbstractType"&gt;
 *       &lt;sequence&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="MSSP_TransID" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MSS_StatusReqType")
public class MSSStatusReqType
    extends MessageAbstractType
{

    @XmlAttribute(name = "MSSP_TransID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String msspTransID;

    /**
     * Gets the value of the msspTransID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMSSPTransID() {
        return msspTransID;
    }

    /**
     * Sets the value of the msspTransID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMSSPTransID(String value) {
        this.msspTransID = value;
    }

}
