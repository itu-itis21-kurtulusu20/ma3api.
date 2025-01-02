package tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;

import com.objsys.asn1j.runtime.Asn1UTF8String;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.pkixtsp.TimeStampResp;

import java.io.InputStream;
import java.math.BigInteger;

/**
 * @author ayetgin
 */
public class ETimeStampResponse extends BaseASNWrapper<TimeStampResp>
{
    public ETimeStampResponse(TimeStampResp aObject)
    {
        super(aObject);
    }

    public ETimeStampResponse(byte[] aBytes) throws ESYAException
    {
        super(aBytes, new TimeStampResp());
    }

    public ETimeStampResponse(InputStream aIS) throws ESYAException
    {
        super(aIS, new TimeStampResp());
    }
    
    public EContentInfo getContentInfo(){
        return new EContentInfo(mObject.timeStampToken);
    }

    /**
     * get failure information as PKIFailureInfo defined codes, -1 for no info
     *
     * @see tr.gov.tubitak.uekae.esya.asn.pkixtsp.PKIFailureInfo
     * @return failure info as code
     */
    public int getFailInfo() {

        if (mObject.status.failInfo != null)
            return new BigInteger(mObject.status.failInfo.value).intValue();
        return -1;
    }

    /**
     * @return error explanation
     */
    public String getStatusString(){

        String freeText = "";

        if (mObject.status.statusString != null){
            for(Asn1UTF8String element : mObject.status.statusString.elements){
               freeText += element.value +"\n";
            }
        }

        return freeText;
    }

    /**
     * @return Status of the response message.
     */
    public long getStatus() {
        return mObject.status.status.value;
    }

}
