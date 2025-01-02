package tr.gov.tubitak.uekae.esya.api.asn.ocsp;

import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1GeneralizedTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtensions;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.ocsp.CertStatus;
import tr.gov.tubitak.uekae.esya.asn.ocsp.RevokedInfo;
import tr.gov.tubitak.uekae.esya.asn.ocsp.SingleResponse;

import java.util.Calendar;
import java.util.Date;

/**
 * @author ahmety
 *         date: Feb 8, 2010
 */
public class ESingleResponse extends BaseASNWrapper<SingleResponse> {

    protected static Logger logger = LoggerFactory.getLogger(ESingleResponse.class);
	public static final byte CERTSTATUS_GOOD = CertStatus._GOOD;
	public static final byte CERTSTATUS_REVOKED = CertStatus._REVOKED;
	public static final byte CERTSTATUS_UNKOWN = CertStatus._UNKNOWN;
	
    public ESingleResponse(SingleResponse aObject) {
        super(aObject);
    }

    public ESingleResponse(byte[] aBytes) throws ESYAException {
        super(aBytes, new SingleResponse());
    }

    public ESingleResponse(
            ECertID certID,
            CertStatus certStatus,
            Asn1GeneralizedTime thisUpdate,
            Asn1GeneralizedTime nextUpdate,
            EExtensions singleExtensions )  {
        super(new SingleResponse(
                certID.getObject(),
                certStatus,
                thisUpdate,
                nextUpdate,
                singleExtensions.getObject()
        ));
    }

    public ESingleResponse(
            ECertID certID,
            CertStatus certStatus,
            Asn1GeneralizedTime thisUpdate )  {
        super(new SingleResponse(
                certID.getObject(),
                certStatus,
                thisUpdate
        ));
    }

    public ECertID getCertID(){
        if(mObject == null)
            return null;
        return new ECertID(mObject.certID);
    }


    public int getCertificateStatus()
    {
        return mObject.certStatus.getChoiceID();
    }

    public int getRevokationReason() {
        if(mObject.certStatus.getElement() instanceof RevokedInfo) {
            RevokedInfo revoke = (RevokedInfo) mObject.certStatus.getElement();
            if(revoke.revocationReason == null)
            	return -1;
            return revoke.revocationReason.getValue();
        }else {
            return -1;
        }
    }

    public Calendar getRevocationTime() {
        if(mObject.certStatus.getElement() instanceof RevokedInfo) {
            RevokedInfo revoke = (RevokedInfo) mObject.certStatus.getElement();
            try {
                return revoke.revocationTime.getTime();
            } catch (Asn1Exception aEx) {
                logger.warn("Warning in ESingleResponse", aEx);
                return null;
            }
        }else {
            return null;
        }
    }

    public Calendar getThisUpdate(){
        try {
            return mObject.thisUpdate.getTime();
        } catch (Exception x){
            logger.warn("Warning in ESingleResponse", x);
            return null;
        }
    }

    public Date getNextUpdate(){
        try {
            return mObject.nextUpdate.getTime().getTime();
        } catch (Exception x){
            logger.warn("Warning in ESingleResponse", x);
            return null;
        }
    }

    public String toString(){
        CertStatus status = new CertStatus((byte)getCertificateStatus(), null);
        return "Cert: " + getCertID().toString() + " Status: " + status.getElemName();
    }

    public EExtensions getSingleExtensions(){
        return new EExtensions(mObject.singleExtensions);
    }
}
