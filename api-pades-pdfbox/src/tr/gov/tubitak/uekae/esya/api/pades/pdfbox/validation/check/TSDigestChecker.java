package tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation.check;

import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETSTInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.common.util.StreamUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESSignature;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation.PadesChecker;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation.ValidationResultDetailImpl;
import tr.gov.tubitak.uekae.esya.api.signature.Signable;
import tr.gov.tubitak.uekae.esya.api.signature.Signature;
import tr.gov.tubitak.uekae.esya.api.signature.ValidationResultDetail;
import tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author ayetgin
 */
public class TSDigestChecker implements PadesChecker {

    public ValidationResultDetail check(PAdESSignature signature) {
        if (!signature.isTimestamp())
            return null;

        boolean fail = false;
        String message = CMSSignatureI18n.getMsg(E_KEYS.TS_MESSAGE_DIGEST_CHECKER_SUCCESSFUL);

        try {
            List<Signable> signableList = ((Signature)signature.getUnderlyingObject()).getContents();
            Signable signable= signableList.get(0);

            BaseSignedData bsd = (BaseSignedData)((Signature)signature.getUnderlyingObject()).getUnderlyingObject();

            ETSTInfo tstInfo = new ETSTInfo(bsd.getSignedData().getEncapsulatedContentInfo().getContent());
            AlgorithmIdentifier tsHashAlg = tstInfo.getHashAlgorithm().getObject();
            DigestAlg digestAlg=DigestAlg.fromOID(tsHashAlg.algorithm.value);

            InputStream content = signable.getContent();
            byte[] bytes = StreamUtil.readAll(content);
            byte[] hash = DigestUtil.digest(digestAlg, bytes);

            if (!Arrays.equals(hash, tstInfo.getHashedMessage())){
                fail = true;
                message = CMSSignatureI18n.getMsg(E_KEYS.TS_MESSAGE_DIGEST_CHECKER_UNSUCCESSFUL);
            }
        }
        catch (Exception x){
            fail = true;
            message  = CMSSignatureI18n.getMsg(E_KEYS.TS_MESSAGE_DIGEST_CHECKER_DIGEST_CALCULATION_ERROR);
            x.printStackTrace();
        }

        String checkerName = CMSSignatureI18n.getMsg(E_KEYS.TIMESTAMP_MESSAGE_DIGEST_CHECKER);

        return new ValidationResultDetailImpl(getClass(), checkerName, message, fail ? ValidationResultType.INVALID : ValidationResultType.VALID);
    }
}
