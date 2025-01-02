package tr.gov.tubitak.uekae.esya.api.asn.cms;

import com.objsys.asn1j.runtime.Asn1OctetString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues;
import tr.gov.tubitak.uekae.esya.asn.cms.CrlValidatedID;
import tr.gov.tubitak.uekae.esya.asn.cms.OtherHash;
import tr.gov.tubitak.uekae.esya.asn.cms.OtherHashAlgAndValue;

import java.math.BigInteger;
import java.util.Calendar;

/**
 * @author ayetgin
 */
public class ECrlValidatedID extends BaseASNWrapper<CrlValidatedID> {

    protected static Logger logger = LoggerFactory.getLogger(ECrlValidatedID.class);

    public ECrlValidatedID(CrlValidatedID aObject)
    {
        super(aObject);
    }

    public EName getCrlissuer(){
        return new EName(mObject.crlIdentifier.crlissuer);
    }

    public Calendar getCrlIssuedTime(){
        try {
        return mObject.crlIdentifier.crlIssuedTime.getTime();
        }catch (Exception x){
            logger.error("Error in ECrlValidatedID", x);
        }
        return null;
    }
    public BigInteger getCrlNumber(){
        if (mObject.crlIdentifier.crlNumber!=null)
            return mObject.crlIdentifier.crlNumber.value;
        return null;
    }

    public byte[] getDigestValue(){
        OtherHash hash = mObject.crlHash;
        byte[] value = null;
        if(hash.getChoiceID()==OtherHash._SHA1HASH) {
            value = ((Asn1OctetString) hash.getElement()).value;
        } else {
            value = ((OtherHashAlgAndValue) hash.getElement()).hashValue.value;
        }
        return value;
    }

    public int[] getDigestAlg(){
        OtherHash hash = mObject.crlHash;
        if(hash.getChoiceID()==OtherHash._SHA1HASH) {
            return _algorithmsValues.sha_1;
        } else {
        	return ((OtherHashAlgAndValue) hash.getElement()).hashAlgorithm.algorithm.value;
        }
    }
}
