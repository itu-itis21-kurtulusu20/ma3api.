package tr.gov.tubitak.uekae.esya.api.asn.cmp;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIFailureInfo;

/**
 * User: zeldal.ozdemir
 * Date: 2/2/11
 * Time: 10:00 AM
 */
public class EPKIFailureInfo extends BaseASNWrapper<PKIFailureInfo> {
    public final static EPKIFailureInfo badAlg = new EPKIFailureInfo(PKIFailureInfo.badAlg);
    public final static EPKIFailureInfo badMessageCheck = new EPKIFailureInfo(PKIFailureInfo.badMessageCheck);
    public final static EPKIFailureInfo badRequest = new EPKIFailureInfo(PKIFailureInfo.badRequest);
    public final static EPKIFailureInfo badTime = new EPKIFailureInfo(PKIFailureInfo.badTime);
    public final static EPKIFailureInfo badCertId = new EPKIFailureInfo(PKIFailureInfo.badCertId);
    public final static EPKIFailureInfo badDataFormat = new EPKIFailureInfo(PKIFailureInfo.badDataFormat);
    public final static EPKIFailureInfo wrongAuthority = new EPKIFailureInfo(PKIFailureInfo.wrongAuthority);
    public final static EPKIFailureInfo incorrectData = new EPKIFailureInfo(PKIFailureInfo.incorrectData);
    public final static EPKIFailureInfo missingTimeStamp = new EPKIFailureInfo(PKIFailureInfo.missingTimeStamp);
    public final static EPKIFailureInfo badPOP = new EPKIFailureInfo(PKIFailureInfo.badPOP);
    public final static EPKIFailureInfo certRevoked = new EPKIFailureInfo(PKIFailureInfo.certRevoked);
    public final static EPKIFailureInfo certConfirmed = new EPKIFailureInfo(PKIFailureInfo.certConfirmed);
    public final static EPKIFailureInfo wrongIntegrity = new EPKIFailureInfo(PKIFailureInfo.wrongIntegrity);
    public final static EPKIFailureInfo badRecipientNonce = new EPKIFailureInfo(PKIFailureInfo.badRecipientNonce);
    public final static EPKIFailureInfo timeNotAvailable = new EPKIFailureInfo(PKIFailureInfo.timeNotAvailable);
    public final static EPKIFailureInfo unacceptedPolicy = new EPKIFailureInfo(PKIFailureInfo.unacceptedPolicy);
    public final static EPKIFailureInfo unacceptedExtension = new EPKIFailureInfo(PKIFailureInfo.unacceptedExtension);
    public final static EPKIFailureInfo addInfoNotAvailable = new EPKIFailureInfo(PKIFailureInfo.addInfoNotAvailable);
    public final static EPKIFailureInfo badSenderNonce = new EPKIFailureInfo(PKIFailureInfo.badSenderNonce);
    public final static EPKIFailureInfo badCertTemplate = new EPKIFailureInfo(PKIFailureInfo.badCertTemplate);
    public final static EPKIFailureInfo signerNotTrusted = new EPKIFailureInfo(PKIFailureInfo.signerNotTrusted);
    public final static EPKIFailureInfo transactionIdInUse = new EPKIFailureInfo(PKIFailureInfo.transactionIdInUse);
    public final static EPKIFailureInfo unsupportedVersion = new EPKIFailureInfo(PKIFailureInfo.unsupportedVersion);
    public final static EPKIFailureInfo notAuthorized = new EPKIFailureInfo(PKIFailureInfo.notAuthorized);
    public final static EPKIFailureInfo systemUnavail = new EPKIFailureInfo(PKIFailureInfo.systemUnavail);
    public final static EPKIFailureInfo systemFailure = new EPKIFailureInfo(PKIFailureInfo.systemFailure);
    public final static EPKIFailureInfo duplicateCertReq = new EPKIFailureInfo(PKIFailureInfo.duplicateCertReq);

    public EPKIFailureInfo(PKIFailureInfo aObject) {
        super(aObject);
    }

    public EPKIFailureInfo(int failInfo) {
        super(new PKIFailureInfo());
        mObject.set(failInfo);
    }

    public EPKIFailureInfo(byte[] aBytes) throws ESYAException {
        super(aBytes, new PKIFailureInfo());
    }
}
