package tr.gov.tubitak.uekae.esya.api.signature.certval.match;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EResponderID;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EResponseData;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.signature.certval.OCSPSearchCriteria;

import java.util.Arrays;

/**
 * @author ayetgin
 */

public class OCSPResponseCriteriaMatcher
{
    private static Logger logger = LoggerFactory.getLogger(OCSPResponseCriteriaMatcher.class);

    public boolean match(OCSPSearchCriteria aCriteria, EOCSPResponse aResponse)
    {
        /*
        a) Check that the content of ResponderID element matches the content of
        the responderID field within the OCSP response. If the content of this
        field is the byName choice, check if its string format is the same. If
        the content of this field is the byKey choice, the ResponderID should
        contain the base-64 encoded key digest. The verifier should check if
        this value matches the byKey choice.

        */
        EBasicOCSPResponse response = aResponse.getBasicOCSPResponse();
        EResponseData rd = response.getTbsResponseData();

        // check responder id by key
        //  _BYNAME = 1
        //  _BYKEY = 2;
        if(aCriteria.getOCSPResponderIDByKey() != null && rd.getResponderIDType()==2) {
            if(!Arrays.equals(aCriteria.getOCSPResponderIDByKey(),rd.getResponderIdByKey())){
                logger.warn("Criteria mismatch with responder id by key");
                return false;
            }
        }

        // check responder id by name
        if(aCriteria.getOCSPResponderIDByName() != null && rd.getResponderIDType()==1) {
            if(!aCriteria.getOCSPResponderIDByName().equals(rd.getResponderIdByName().stringValue())){
                logger.warn("Criteria mismatch with responder id by name");
                return false;
            }
        }

        /** burdan asagi kasimpasa
        if (rd.getResponderIDType()== ResponderID._BYKEY){
            if (aCriteria.getOCSPResponderIDByKey()==null){
                logger.warn("Criteria not contains response id by key");
                return false;
            }
            byte[] byKey = rd.getResponderIdByKey();
            if (!Arrays.equals(byKey, aCriteria.getOCSPResponderIDByKey())){
                logger.warn("Criteria mismatch with response id by key");
                return false;
            }

        }
        else {
            String name = aCriteria.getOCSPResponderIDByName();
            if (name==null){
                logger.warn("Criteria not contains response id by name");
                return false;
            }
            name = LDAPDNUtil.normalize(name);
            EName byName = rd.getResponderIdByName();
            if (! name.equals(byName.stringValue())){
                logger.warn("Criteria mismatch with response id by name");
                    return false;
            }
        }//*/

        /*
        b) Check that the time indicated by the thisUpdate field in the OCSP
        response is the same as the time indicated by the ProducedAt element.

        c) If the aforementioned checks are successful, compute the digest of
        the OCSP response according to the algorithm indicated in the
        ds:DigestMethod element, base-64 encode the result and check if this is
        the same as the contents of the ds:DigestValue element.
        */

        // check produced at
        if(aCriteria.getProducedAt() != null) {
            if(!response.getProducedAt().getTime().equals(aCriteria.getProducedAt())) {
                logger.warn("Criteria mismatch with response produced at "+response.getProducedAt().getTime()+" - "+aCriteria.getProducedAt());
            }
        }

        /** burdan asagi mahmutpasa
        Date producedAt = response.getProducedAt().getTime();
        if (!producedAt.equals(aCriteria.getProducedAt())) {
            logger.warn("Criteria mismatch with response produced at "+producedAt+" - "+aCriteria.getProducedAt());
            return false;
        }//*/

        /*
        c) If the aforementioned checks are successful, compute the digest of
        the OCSP response according to the algorithm indicated in the
        ds:DigestMethod element, base-64 encode the result and check if this is
        the same as the contents of the ds:DigestValue element.
        */
      //TODO ocsp ref hash null olabilir
        try {
            byte[] digestResponse = DigestUtil.digest(aCriteria.getDigestAlg(), aResponse.getEncoded());
            byte[] digestBasicResponse = DigestUtil.digest(aCriteria.getDigestAlg(), response.getEncoded());
            if (!Arrays.equals(digestResponse, aCriteria.getDigestValue())&& !Arrays.equals(digestBasicResponse, aCriteria.getDigestValue())){
                logger.warn("Criteria digest mismatch with response digest! ");
                return false;
            }
        }
        catch (Exception x){
            x.printStackTrace();
            return false;
        }

        return true;

    }

}
