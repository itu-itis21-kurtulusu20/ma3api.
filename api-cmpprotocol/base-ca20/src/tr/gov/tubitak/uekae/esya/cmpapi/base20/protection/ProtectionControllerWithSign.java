package tr.gov.tubitak.uekae.esya.cmpapi.base20.protection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIMessage;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.IConfigType;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.util.UtilCmp;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Dec 3, 2010
 * Time: 4:30:05 PM
 * To change this template use File | Settings | File Templates.
 */

public class ProtectionControllerWithSign<T extends IConfigType> implements IProtectionController<T> {
    private static final Logger logger = LoggerFactory.getLogger(ProtectionControllerWithSign.class);
    private ITrustedCertificateFinder trustedCertificateFinder;

    public void setAcceptedSignAlgs(List<SignatureAlg> acceptedSignAlgs) {
        this.acceptedSignAlgs = acceptedSignAlgs;
    }

    private List<SignatureAlg> acceptedSignAlgs = Arrays.asList(SignatureAlg.RSA_SHA1, SignatureAlg.RSA_SHA256,SignatureAlg.RSA_PSS,
                                                                SignatureAlg.ECDSA_SHA1,SignatureAlg.ECDSA_SHA224,SignatureAlg.ECDSA_SHA256,SignatureAlg.ECDSA_SHA384,SignatureAlg.ECDSA_SHA512);

    public ProtectionControllerWithSign(ITrustedCertificateFinder trustedCertificateFinder) {
        this.trustedCertificateFinder = trustedCertificateFinder;
    }

    public boolean verifyProtection(EPKIMessage message) {
        try {
            verifyProtectionImpl(message.getObject());
        } catch (Exception e) {
            logger.error("Protection Failed:" + e.getMessage(), e);
            return false;
        }
        return true;
    }

    private void verifyProtectionImpl(PKIMessage message) throws Exception {
        logger.debug("Protection kontrolu yapiliyor");

        if ((message == null)
                || (message.header == null)
                || (message.header.protectionAlg == null)
                || (message.header.protectionAlg.algorithm == null)
                ) {
            throw new Exception("Protection Algorithm is empty");
        }
        //SignatureAlg signatureAlg = SignatureAlg.fromAlgorithmIdentifier(new EAlgorithmIdentifier(message.header.protectionAlg));
        Pair<SignatureAlg, AlgorithmParams> pair = SignatureAlg.fromAlgorithmIdentifier(new EAlgorithmIdentifier(message.header.protectionAlg));
        if (pair.first() == null) {
            throw new Exception("Uknown Signature Algorithm:" + AsnIO.getFormattedAsn(message.header.protectionAlg));
        }
        if (!acceptedSignAlgs.contains(pair.first())) {
            throw new Exception("Unaccepted Signature Algorithm:" + pair.first());
        }

        byte[] imzalanan = UtilCmp.getSigningData(message.header, message.body);

        List<ECertificate> trustedCertificates = trustedCertificateFinder.findTrustedCertificates(new EPKIMessage(message ) );
        for (ECertificate trustedCertificate : trustedCertificates) {
            if (verifySignatureWithCertificate(imzalanan,
                    message.protection.value,
                    pair,
                    trustedCertificate)) {
                logger.info("Protection Kontrolü Başarılı. Sertifika:" + trustedCertificate.toString());
                trustedCertificateFinder.setProtectionCertificate(trustedCertificate);
                return;
            }
        }

        throw new Exception("Message Protection failed: Message altered.");
    }


    private boolean verifySignatureWithCertificate(byte[] imzalanan, byte[] protection, Pair<SignatureAlg, AlgorithmParams> pair, ECertificate protectionCertificate) {
        try {
            return SignUtil.verify(pair.first(),pair.second(), imzalanan, protection, KeyUtil.decodePublicKey(protectionCertificate.getSubjectPublicKeyInfo()));
        } catch (CryptoException e) {
            logger.error("Error while Protection Check: " + e.getMessage(), e);
            return false;
        }
    }


}
