package tr.gov.tubitak.uekae.esya.api.asn.ocsp;

import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1GeneralizedTime;
import com.objsys.asn1j.runtime.Asn1OctetString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtensions;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.ocsp.ResponseData;
import tr.gov.tubitak.uekae.esya.asn.ocsp.Version;
import tr.gov.tubitak.uekae.esya.asn.ocsp._SeqOfSingleResponse;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;

import java.util.Calendar;

/**
 * @author ayetgin
 */
public class EResponseData extends BaseASNWrapper<ResponseData> {

    protected static Logger logger = LoggerFactory.getLogger(EResponseData.class);

    public EResponseData(ResponseData aObject)
    {
        super(aObject);
    }

    public EResponseData(byte[] aBytes) throws ESYAException {
        super(aBytes, new ResponseData());
    }

    public EResponseData(      Version version,
                               EResponderID responderID,
                               Asn1GeneralizedTime producedAt,
                               ESingleResponse[] responses,
                               EExtensions extensions)  {
        super(new ResponseData(version,
                responderID.getObject(),
                producedAt,
                new _SeqOfSingleResponse(unwrapArray(responses)) ,
                extensions.getObject()));
    }

    /**
     *  _BYNAME = 1
     *  _BYKEY = 2;
     * @return responder option type
     */
    public int getResponderIDType(){
        return mObject.responderID.getChoiceID();
    }

    public EName getResponderIdByName(){
        return new EName((Name)mObject.responderID.getElement());
    }

    public byte[] getResponderIdByKey(){
        return ((Asn1OctetString)mObject.responderID.getElement()).value;
    }

    public EResponderID getResponderID(){
        return new EResponderID(mObject.responderID);
    }


    public Calendar getProducetAt(){
        try {
            return mObject.producedAt.getTime();
        } catch (Asn1Exception x) {
            logger.warn("Warning in EResponseData", x);
            return null;
        }
    }

    public long getVersion() {
        Version ver = mObject.version;
        if (ver == null)
            return Version.v1;

        return ver.value;
    }

    public int getSingleResponseCount(){
        return mObject.responses.getLength();
    }

    public ESingleResponse getSingleResponse(int aIndex){
        return new ESingleResponse(mObject.responses.elements[aIndex]);
    }

    public ESingleResponse[] getResponses(){
        return wrapArray(mObject.responses.elements, ESingleResponse.class);
    }
}
