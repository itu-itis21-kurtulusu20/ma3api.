package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;

import java.io.Serializable;
import java.security.Key;
import java.util.*;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br>
 * <b>Date</b>: 10/10/12 - 11:45 PM <p>
 * <b>Description</b>:<br>
 * Herein lies our most basic Key Template Definition to inspire all key template definitions
 */
public abstract class KeyTemplate implements Serializable, Key {
    protected static Logger logger = LoggerFactory.getLogger(KeyTemplate.class);

    protected String label;
    protected long keyId = -1;
    /**
     * Attribute list to manage key with PKCS11 interface
     */
    public Map<Long,CK_ATTRIBUTE> attributes  = new HashMap<Long, CK_ATTRIBUTE>();

    protected KeyTemplate(String label) {
        this.label = label;
        this.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, getLabel()));
    }

    public String getLabel() {
        return label;
    }

    public long getKeyId() {
        return keyId;
    }

    public void setKeyId(long keyId) {
        this.keyId = keyId;
    }

    public String getFormat() {
        throw new ESYARuntimeException("KeyTemplate Has No Format");
    }

    public byte[] getEncoded() {
        throw new ESYARuntimeException(this.getClass().getName() + " does NOT support encoding!");
    }

    /**
     * Adds attribute for this key, overrides existing  CK_ATTRIBUTE.type is unique
     * @param attribute
     * @return
     */
    public KeyTemplate add(CK_ATTRIBUTE attribute){
        if(attributes.containsKey(attribute.type))
            logger.debug("Overriding existing Attribute:" + attributes.get(attribute.type) + " With: " + attribute.type);
        attributes.put(attribute.type, attribute);
        return this;
    }

    public KeyTemplate add(CK_ATTRIBUTE[] attributes) {
        for (CK_ATTRIBUTE attribute : attributes)
            add(attribute);
        return this;
    }

    public void remove(long attributeType){
        if(attributes.containsKey(attributeType)){
            attributes.remove(attributeType);
        }
    }

    public Object getAttribute(long attributeType)
    {
        return attributes.get(attributeType).pValue;
    }

    public boolean containsAttribute(long attributeType)
    {
        return attributes.containsKey(attributeType);
    }

    /**
     * gets Copy of attributes
     * @return
     */
    public List<CK_ATTRIBUTE> getAttributes(){
        return new ArrayList<CK_ATTRIBUTE>(attributes.values());
    }

    /**
     * gets attribute as array
     * @return
     */
    public CK_ATTRIBUTE[] getAttributesAsArr(){
        Collection<CK_ATTRIBUTE> values = attributes.values();
        return values.toArray(new CK_ATTRIBUTE[values.size()]);
    }
}
