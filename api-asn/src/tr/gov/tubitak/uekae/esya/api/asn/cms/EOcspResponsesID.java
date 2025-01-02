package tr.gov.tubitak.uekae.esya.api.asn.cms;

import com.objsys.asn1j.runtime.Asn1OctetString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EResponderID;
import tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues;
import tr.gov.tubitak.uekae.esya.asn.cms.OcspResponsesID;
import tr.gov.tubitak.uekae.esya.asn.cms.OtherHash;
import tr.gov.tubitak.uekae.esya.asn.cms.OtherHashAlgAndValue;

import java.util.Calendar;

/**
 * @author ayetgin
 */
public class EOcspResponsesID extends BaseASNWrapper<OcspResponsesID> {

    protected static Logger logger = LoggerFactory.getLogger(EOcspResponsesID.class);

    public EOcspResponsesID(OcspResponsesID aObject)
    {
        super(aObject);
    }

    public EResponderID getOcspResponderID(){
        return new EResponderID(mObject.ocspIdentifier.ocspResponderID);
    }

    public Calendar getProducedAt(){
        try {
            return mObject.ocspIdentifier.producedAt.getTime();
        } catch (Exception x){
            logger.error("Error in EOcspResponsesID", x);
        }
        return null;
    }

    public byte[] getDigestValue(){
        OtherHash hash = mObject.ocspRepHash;
        byte[] value = null;
        if(hash.getChoiceID()==OtherHash._SHA1HASH) {
            value = ((Asn1OctetString) hash.getElement()).value;
        } else {
            value = ((OtherHashAlgAndValue) hash.getElement()).hashValue.value;
        }
        return value;
    }

    public int[] getDigestAlg(){
        OtherHash hash = mObject.ocspRepHash;
        if(hash.getChoiceID()==OtherHash._SHA1HASH) {
            return _algorithmsValues.sha_1;
        } else {
        	return ((OtherHashAlgAndValue) hash.getElement()).hashAlgorithm.algorithm.value;
        }
    }
}
