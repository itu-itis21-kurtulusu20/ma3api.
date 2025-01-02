package tr.gov.tubitak.uekae.esya.cmpapi.esya;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.ECertResponse;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.ECertifiedKeyPair;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIFailureInfo;
import tr.gov.tubitak.uekae.esya.api.asn.crmf.ECertReqMsg;
import tr.gov.tubitak.uekae.esya.api.asn.crmf.EEncryptedValue;
import tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8.EPrivateKeyInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu.GNUCryptoProvider;
import tr.gov.tubitak.uekae.esya.asn.cmp.CMPCertificate;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertOrEncCert;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;
import tr.gov.tubitak.uekae.esya.asn.x509.SubjectPublicKeyInfo;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ExceptionInfo;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.util.Utilcrmf;

import java.security.PrivateKey;
import java.security.PublicKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Nov 11, 2010
 * Time: 2:58:40 PM
 * To change this template use File | Settings | File Templates.
 */

public class KeyGenerationOnServer extends BaseCertificationParam {

    protected static Logger logger = LoggerFactory.getLogger(KeyGenerationOnServer.class);
    private PublicKey proEncKey;
    private BaseCipher cipher;
    private PrivateKey privateKey;

    public KeyGenerationOnServer(EName sender,
                                 PublicKey proEncKey,
                                 Long sertifikaTalepNo,
                                 String cardNo,
                                 Long cardManufacturerNo,
                                 BaseCipher cipher
    ) {
        super(sender, sertifikaTalepNo, cardNo, cardManufacturerNo);
        this.proEncKey = proEncKey;
        this.cipher = cipher;
    }

    @Override
    public void addSpecificOperations(ECertReqMsg certReqMsg) {
        super.addSpecificOperations(certReqMsg);
        if (!proEncKey.getAlgorithm().equalsIgnoreCase(AsymmetricAlg.RSA.getName())) {//private keyimi şifreleyecek anahtar RSA olmalı
            logger.error("Protocol Encrytion key must be RSA, it is:" + proEncKey.getAlgorithm());
            throw new ESYARuntimeException("Protocol Encrytion key must be RSA, it is:" + proEncKey.getAlgorithm());
        }
        SubjectPublicKeyInfo proEncKeyInfo = new SubjectPublicKeyInfo();
        try {
            proEncKeyInfo.decode(new Asn1DerDecodeBuffer(proEncKey.getEncoded()));
        } catch (Exception aEx) {
            throw new ESYARuntimeException("Error whilde encoding SubjectPublicKeyInfo:" + aEx.getMessage(), aEx);
        }
        try {
            Utilcrmf.istegeProtocolEncKeyEkle(certReqMsg.getCertRequest().getObject(), proEncKeyInfo);
        } catch (Asn1Exception e) {
            throw new ESYARuntimeException("Error while creating CertReqMsg-ProEncrKey" + e.getMessage(), e);
        }
    }

    @Override
    public void extractResponse(ECertResponse certResponse) throws CMPProtocolException {
        super.extractResponse(certResponse);
        if(isWaiting())
            return;
        if (certOrEncCert.getChoiceID() != CertOrEncCert._CERTIFICATE)
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData,
                    "choice must be CERTIFICATE for incoming CertOrEncCert, its:" + certOrEncCert.getElemName());

        if (!(certOrEncCert.getElement() instanceof CMPCertificate))
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData,
                    "CertOrEncCert must have CMPCertificate, but its:" + certOrEncCert.getElement().getClass());

        try {
            CMPCertificate cc = (CMPCertificate) certOrEncCert.getElement();
            certificate = new ECertificate((Certificate) cc.getElement());
        } catch (Exception ex) {
            throw new CMPProtocolException(ex,
                    new ExceptionInfo(EPKIFailureInfo.badDataFormat, "Error while encode-decoding Certificate"));
        }

        EEncryptedValue encryptedPrivateKey = certResponse.getCertifiedKeyPair().getPrivateKey();
        if (cipher == null) {
            //throw new RuntimeException("No Cipher presented to decrypt incoming PrivateKey");
            logger.warn("No Cipher presented to decrypt incoming PrivateKey");
        } else {
            byte[] privKeyBytes = null;
            try {
                privKeyBytes = Utilcrmf.encryptedValuedakiSifreliyiAl(cipher, encryptedPrivateKey.getObject());
                AsymmetricAlg asymmetricAlg = AsymmetricAlg.fromOID(encryptedPrivateKey.getObject().intendedAlg.algorithm.value);
                try {
                    privateKey = decodePrivateKey(asymmetricAlg, privKeyBytes);
                } catch (Exception e) {
                    logger.warn("Warning in KeyGenerationOnServer", e);
                    privateKey = decodePrivateKey(asymmetricAlg, new EPrivateKeyInfo(privKeyBytes).getEncoded());
                }
            } catch (Exception ex) {
                throw new CMPProtocolException("Error while decrypting PrivateKey:" + ex.getMessage(),ex,
                    new ExceptionInfo(EPKIFailureInfo.badDataFormat, "Error while encode-decoding Certificate"));
            }
        }
    }


    private PrivateKey decodePrivateKey(AsymmetricAlg asymmetricAlg, byte[] privateKeyBytes) throws CryptoException {
        return new GNUCryptoProvider().getKeyFactory().decodePrivateKey(asymmetricAlg,privateKeyBytes);
    }

    public ECertifiedKeyPair getCertifiedKeyPair(){
        return certResponse.getCertifiedKeyPair();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}
