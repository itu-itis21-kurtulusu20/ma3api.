
package org.etsi.uri.ts102204.v1_1.turktelekom;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * <p>Java class for AdditionalServiceType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="AdditionalServiceType"&gt;
 *   &lt; complexContent &gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Description" type="{http://uri.etsi.org/TS102204/v1.1.2#}mssURIType"/&gt;
 *         &lt;element name="Entity" type="{http://uri.etsi.org/TS102204/v1.1.2#}MeshMemberType" minOccurs="0"/&gt;
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdditionalServiceType", propOrder = {
    "description",
    "entity",
    "any"
})
public class AdditionalServiceType {

    @XmlElement(name = "Description", required = true)
    protected MssURIType description;
    @XmlElement(name = "Entity")
    protected MeshMemberType entity;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the description property.
     *
     * @return
     *     possible object is
     *     {@link MssURIType }
     *
     */
    public MssURIType getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value
     *     allowed object is
     *     {@link MssURIType }
     *
     */
    public void setDescription(MssURIType value) {
        this.description = value;
    }

    /**
     * Gets the value of the entity property.
     *
     * @return
     *     possible object is
     *     {@link MeshMemberType }
     *
     */
    public MeshMemberType getEntity() {
        return entity;
    }

    /**
     * Sets the value of the entity property.
     *
     * @param value
     *     allowed object is
     *     {@link MeshMemberType }
     *
     */
    public void setEntity(MeshMemberType value) {
        this.entity = value;
    }

    /**
     * Gets the value of the any property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Element }
     * {@link Object }
     *
     *
     */
    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }

}
