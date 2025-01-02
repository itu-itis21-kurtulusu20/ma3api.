package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1Boolean;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.x509.IssuingDistributionPoint;
import tr.gov.tubitak.uekae.esya.asn.x509.ReasonFlags;

/**
 * @author ahmety
 * date: Feb 17, 2010
 */
public class EIssuingDistributionPoint extends BaseASNWrapper<IssuingDistributionPoint>
{

    public EIssuingDistributionPoint(IssuingDistributionPoint aObject)
    {
        super(aObject);
    }

    public EIssuingDistributionPoint(byte[] aBytes) throws ESYAException {
        super(aBytes, new IssuingDistributionPoint());
    }

    public EIssuingDistributionPoint(   EDistributionPointName distributionPoint,
                                        boolean onlyContainsUserCerts,
                                        boolean onlyContainsCACerts,
                                        EReasonFlags onlySomeReasons,
                                        boolean indirectCRL,
                                        boolean onlyContainsAttributeCerts) {
        super(new IssuingDistributionPoint(
                    distributionPoint.getObject(),
                    onlyContainsUserCerts,
                    onlyContainsCACerts,
                    onlySomeReasons.getObject(),
                    indirectCRL,
                onlyContainsAttributeCerts));

    }

    public EIssuingDistributionPoint() {
        super(new IssuingDistributionPoint());
    }

    public EDistributionPointName getDistributionPoint(){
        if (mObject.distributionPoint==null)
            return null;
        return new EDistributionPointName(mObject.distributionPoint);
    }

    public void setDistributionPoint(EDistributionPointName aEDistributionPointName){
        mObject.distributionPoint = aEDistributionPointName.getObject();
    }

    public boolean isOnlyContainsCACerts(){
        return mObject.onlyContainsCACerts.value;
    }

    public void setOnlyContainsCACerts(boolean aFlag){
    	mObject.onlyContainsCACerts = new Asn1Boolean(aFlag);
    }

    public boolean isOnlyContainsUserCerts(){
        return mObject.onlyContainsUserCerts.value;
    }

    public void setOnlyContainsUserCerts(boolean aFlag){
    	mObject.onlyContainsUserCerts = new Asn1Boolean(aFlag);
    }

    public boolean isOnlyContainsAttributeCerts(){
        return (mObject.onlyContainsAttributeCerts!=null) && mObject.onlyContainsAttributeCerts.value;
    }

    public void setOnlyContainsAttributeCerts(boolean aFlag){
    	mObject.onlyContainsAttributeCerts = new Asn1Boolean(aFlag);
    }

    public boolean isIndirectCRL(){
        return mObject.indirectCRL.value;
    }

    public void setIndirectCRL(boolean aFlag){
    	mObject.indirectCRL = new Asn1Boolean(aFlag);
    }

    public ReasonFlags getOnlySomeReasons(){
        return mObject.onlySomeReasons;
    }

    public void setOnlySomeReasons(EReasonFlags aEReasonFlags){
    	mObject.onlySomeReasons = aEReasonFlags.getObject();
    }

}
