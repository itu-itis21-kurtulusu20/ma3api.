package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1InvalidEnumException;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.x509.CRLReason;

/**
 * User: zeldal.ozdemir
 * Date: 2/2/11
 * Time: 9:03 AM
 */
public class ECRLReason extends BaseASNWrapper<CRLReason> implements ExtensionType{
   public static final ECRLReason UNSPECIFIED = new ECRLReason(CRLReason.unspecified());
   public static final ECRLReason KEYCOMPROMISE = new ECRLReason(CRLReason.keyCompromise());
   public static final ECRLReason CACOMPROMISE = new ECRLReason(CRLReason.cACompromise());
   public static final ECRLReason AFFILIATIONCHANGED = new ECRLReason(CRLReason.affiliationChanged());
   public static final ECRLReason SUPERSEDED = new ECRLReason(CRLReason.superseded());
   public static final ECRLReason CESSATIONOFOPERATION = new ECRLReason(CRLReason.cessationOfOperation());
   public static final ECRLReason CERTIFICATEHOLD = new ECRLReason(CRLReason.certificateHold());
   public static final ECRLReason REMOVEFROMCRL = new ECRLReason(CRLReason.removeFromCRL());
   public static final ECRLReason PRIVILEGEWITHDRAWN = new ECRLReason(CRLReason.privilegeWithdrawn());
   public static final ECRLReason AACOMPROMISE = new ECRLReason(CRLReason.aACompromise());

    public ECRLReason(CRLReason aObject) {
        super(aObject);
    }

    public ECRLReason(byte[] aBytes) throws ESYAException {
        super(aBytes, CRLReason.unspecified()); // no public constructor of CRLReason
    }

    public ECRLReason(int crlReason) throws Asn1InvalidEnumException {
        super(CRLReason.valueOf(crlReason));
    }

    public int getValue(){
        return mObject.getValue();
    }

    public EExtension toExtension(boolean aCritic) throws ESYAException {
        return new EExtension(EExtensions.oid_ce_cRLReasons, aCritic, this);
    }
}
