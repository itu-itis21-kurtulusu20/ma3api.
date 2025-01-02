package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.SignatureParser;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;
import tr.gov.tubitak.uekae.esya.asn.cms.ContentInfo;
import tr.gov.tubitak.uekae.esya.asn.cms._cmsValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.SUCCESS;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.UNSUCCESS;

/**
 * Checks signature of timestamp response
 * It uses checkers CryptoChecker,MessageDigestAttrChecker and SigningCertificateAttrChecker to check
 *
 * @author aslihan.kubilay
 */
public class TimeStampSignatureChecker extends BaseChecker
{
    protected static Logger logger = LoggerFactory.getLogger(TimeStampSignatureChecker.class);
    private ESignedData mSignedData = null;

    public TimeStampSignatureChecker(ESignedData aSD)
    {
        mSignedData = aSD;
    }

    protected boolean _check(Signer aSigner, CheckerResult aCheckerResult)
    {
        aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.TIMESTAMP_SIGNATURE_CHECKER), TimeStampSignatureChecker.class);

        CryptoChecker sigChecker = new CryptoChecker();
        MessageDigestAttrChecker mdAttrChecker = new MessageDigestAttrChecker();
        SigningCertificateAttrChecker scChecker = new SigningCertificateAttrChecker();


        List<Checker> checkers = new ArrayList<Checker>();
        checkers.add(sigChecker);
        checkers.add(mdAttrChecker);
        checkers.add(scChecker);


        ESignerInfo signerInfo = mSignedData.getSignerInfo(0);


        Signer signer = null;
        try {
            ESignatureType type = SignatureParser.parse(signerInfo, false);
            EContentInfo ci = new EContentInfo(new ContentInfo());
            ci.setContentType(new Asn1ObjectIdentifier(_cmsValues.id_signedData));
            ci.setContent(mSignedData.getEncoded());

            signer = ESignatureType.createSigner(type, new BaseSignedData(ci), signerInfo);
        }
        catch (Exception e) {
            logger.warn("Warning in TimeStampSignatureChecker", e);
            aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TS_SIGNATURE_CHECKER_UNSUCCESSFUL)));
            aCheckerResult.setResultStatus(UNSUCCESS);
            return false;
        }


        Map<String, Object> params = new HashMap<String, Object>();
        params.put(AllEParameters.P_SIGNED_DATA, mSignedData);

        ECertificate signerCertificate = signer.getSignerCertificate();
        if (signerCertificate != null)
            params.put(AllEParameters.P_SIGNING_CERTIFICATE, signerCertificate);
        else
            params.put(AllEParameters.P_SIGNING_CERTIFICATE, mSignedData.getCertificates().get(0));


        boolean allResult = true;
        for (Checker checker : checkers) {
            checker.setParameters(params);
            CheckerResult cresult = new CheckerResult();
            boolean result = checker.check(signer, cresult);
            allResult = allResult && result;
            aCheckerResult.addCheckerResult(cresult);
        }

        if (!allResult) {        	
            aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TS_SIGNATURE_CHECKER_UNSUCCESSFUL)));
            aCheckerResult.setResultStatus(UNSUCCESS);
        }
        else {
            aCheckerResult.setResultStatus(SUCCESS);
            aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TS_SIGNATURE_CHECKER_SUCCESSFUL)));
        }

        return allResult;
    }
}
