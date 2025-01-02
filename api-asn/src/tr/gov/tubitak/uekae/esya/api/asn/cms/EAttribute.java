package tr.gov.tubitak.uekae.esya.api.asn.cms;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OpenType;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.x509.Attribute;
import tr.gov.tubitak.uekae.esya.asn.x509._SetOfAttributeValue;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class EAttribute extends BaseASNWrapper<Attribute> {

	public EAttribute(Attribute aObject)
	{
		super(aObject);
	}
	
	public EAttribute(byte[] aBytes) throws ESYAException {
		super(aBytes,new Attribute());
	}

    public EAttribute( Asn1ObjectIdentifier type, Asn1OpenType[] elements ) {
        super(new Attribute(type, new _SetOfAttributeValue(elements)));
    }

    public Asn1ObjectIdentifier getType()
	{
		return mObject.type;
	}
	
	public void setType(Asn1ObjectIdentifier aType)
	{
		mObject.type = aType;
	}
	
	public int getValueCount() {
		if(mObject.values==null || mObject.values.elements==null)
			return 0;
		return mObject.values.elements.length;
	}
	
	public byte[] getValue(int aIndex) {
		if(mObject.values==null || mObject.values.elements==null)
			return null;
		return mObject.values.elements[aIndex].value;
	}
	
	public void setValue(int aIndex,byte[] aValue) {
		if(mObject.values==null || mObject.values.elements==null)
			return;
		mObject.values.elements[aIndex].value = aValue;
	}
	
	public void addValue(byte[] aValue) {
		if(mObject.values==null) {
			mObject.values = new _SetOfAttributeValue();
			mObject.values.elements = new Asn1OpenType[0];
		}
		mObject.values.elements = extendArray(mObject.values.elements, new Asn1OpenType(aValue));
	}

    public <T extends BaseASNWrapper> List<T> getValuesAsList(Class<T> tClass) throws ESYAException {
        List<T> list = new ArrayList<T>();
        try {
            for (int i = 0; i < mObject.values.elements.length; i++) {
                /* Beware it assumes T has constructor with byte[] parameter*/
                Constructor<T> constructor = tClass.getConstructor(byte[].class);
                list.add( constructor.newInstance(mObject.values.elements[i].value));
            }
        } catch (Exception e) {
            throw new ESYAException("Error while creating List for:" + tClass , e);
        }
        return list;
	}
}
