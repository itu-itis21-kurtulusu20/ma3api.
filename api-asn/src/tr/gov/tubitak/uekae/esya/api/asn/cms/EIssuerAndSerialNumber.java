package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.asn.cms.IssuerAndSerialNumber;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author ahmety
 * date: Feb 1, 2010
 */
public class EIssuerAndSerialNumber extends BaseASNWrapper<IssuerAndSerialNumber>
{

    public EIssuerAndSerialNumber(IssuerAndSerialNumber aObject)
    {
        super(aObject);
    }

    public EIssuerAndSerialNumber(ECertificate aCertificate)
    {
        super(new IssuerAndSerialNumber(aCertificate.getIssuer().getObject(), aCertificate.getObject().tbsCertificate.serialNumber));
    }

    public EName getIssuer(){
        return new EName(mObject.issuer);
    }

    public BigInteger getSerialNumber(){
        return mObject.serialNumber.value;
    }
    
    public String toString()
    {
    	return getIssuer().stringValue() + " Serial Number: " + getSerialNumber().toString(16);
    }
    
    @Override
    public boolean equals(Object obj) 
    {
        if (obj instanceof EIssuerAndSerialNumber)
        {
        	EIssuerAndSerialNumber objEIssuerAndSerial = (EIssuerAndSerialNumber)obj;
            if(getIssuer().equals(objEIssuerAndSerial.getIssuer()) == true && 
            		getSerialNumber().equals(objEIssuerAndSerial.getSerialNumber()) == true)
            	return true;
            else
            	return false;
        }
        return super.equals(obj);    //To change body of overridden methods use File | Settings | File Templates.
    }
    
    @Override
    public int hashCode() 
    {
    	return Arrays.hashCode(getEncoded());
    }
}
