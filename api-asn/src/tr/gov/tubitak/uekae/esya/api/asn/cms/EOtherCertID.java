package tr.gov.tubitak.uekae.esya.api.asn.cms;

import com.objsys.asn1j.runtime.Asn1OctetString;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EGeneralNames;
import tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues;
import tr.gov.tubitak.uekae.esya.asn.cms.OtherCertID;
import tr.gov.tubitak.uekae.esya.asn.cms.OtherHash;
import tr.gov.tubitak.uekae.esya.asn.cms.OtherHashAlgAndValue;

import java.math.BigInteger;

/**
 * @author ayetgin
 */
public class EOtherCertID extends BaseASNWrapper<OtherCertID>
{
    public EOtherCertID(OtherCertID aObject)
    {
        super(aObject);
    }

    public EGeneralNames getIssuerName(){

        return new EGeneralNames(mObject.issuerSerial.issuer);
    }

    public BigInteger getIssuerSerial(){
        return mObject.issuerSerial.serialNumber.value;
    }

    public byte[] getDigestValue(){
        OtherHash hash = mObject.otherCertHash;
        byte[] value = null;
        if(hash.getChoiceID()==OtherHash._SHA1HASH)
        {
            value = ((Asn1OctetString) hash.getElement()).value;
        }
        else
        {
            value = ((OtherHashAlgAndValue) hash.getElement()).hashValue.value;
        }
        return value;
    }
    
    public int[] getDigestAlg(){
        OtherHash hash = mObject.otherCertHash;
        if(hash.getChoiceID()==OtherHash._SHA1HASH) {
            return _algorithmsValues.sha_1;
        }
        else {
        	return ((OtherHashAlgAndValue) hash.getElement()).hashAlgorithm.algorithm.value;
        }
    }    
}
