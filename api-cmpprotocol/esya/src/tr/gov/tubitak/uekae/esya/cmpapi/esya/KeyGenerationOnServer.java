package tr.gov.tubitak.uekae.esya.cmpapi.esya;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.asn.cmp.CMPCertificate;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertOrEncCert;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertResponse;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIFailureInfo;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertReqMsg;
import tr.gov.tubitak.uekae.esya.asn.crmf.EncryptedValue;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;
import tr.gov.tubitak.uekae.esya.asn.x509.SubjectPublicKeyInfo;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ExceptionInfo;
import tr.gov.tubitak.uekae.esya.cmpapi.base.util.Utilcrmf;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Nov 11, 2010
 * Time: 2:58:40 PM
 * To change this template use File | Settings | File Templates.
 */

public class KeyGenerationOnServer extends BaseCertificationParam {
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
    public void addSpecificOperations(CertReqMsg certReqMsg) {
        super.addSpecificOperations(certReqMsg);
        if (!proEncKey.getAlgorithm().equalsIgnoreCase(AsymmetricAlg.RSA.getName())) {//private keyimi şifreleyecek anahtar RSA olmalı
            logger.error("Protocol Encrytion key must be RSA, it is:" + proEncKey.getAlgorithm());
            throw new RuntimeException("Protocol Encrytion key must be RSA, it is:" + proEncKey.getAlgorithm());
        }
        SubjectPublicKeyInfo proEncKeyInfo = new SubjectPublicKeyInfo();
        try {
            proEncKeyInfo.decode(new Asn1DerDecodeBuffer(proEncKey.getEncoded()));
        } catch (Exception aEx) {
            throw new RuntimeException("Error whilde encoding SubjectPublicKeyInfo:" + aEx.getMessage(), aEx);
        }
        try {
            Utilcrmf.istegeProtocolEncKeyEkle(certReqMsg.certReq, proEncKeyInfo);
        } catch (Asn1Exception e) {
            throw new RuntimeException("Error while creating CertReqMsg-ProEncrKey" + e.getMessage(), e);
        }
    }

    @Override
    public void extractResponse(CertResponse certResponse) throws CMPProtocolException {
        super.extractResponse(certResponse);
        if (certOrEncCert.getChoiceID() != CertOrEncCert._CERTIFICATE)
            throw new CMPProtocolException(PKIFailureInfo.incorrectData,
                    "choice must be CERTIFICATE for incoming CertOrEncCert, its:" + certOrEncCert.getElemName());

        if (!(certOrEncCert.getElement() instanceof CMPCertificate))
            throw new CMPProtocolException(PKIFailureInfo.incorrectData,
                    "CertOrEncCert must have CMPCertificate, but its:" + certOrEncCert.getElement().getClass());

        try {
            CMPCertificate cc = (CMPCertificate) certOrEncCert.getElement();
            certificate = new ECertificate((Certificate) cc.getElement());
        } catch (Exception ex) {
            throw new CMPProtocolException(ex,
                    new ExceptionInfo(PKIFailureInfo.badDataFormat, "Error while encode-decoding Certificate"));
        }

        EncryptedValue encryptedPrivateKey = certResponse.certifiedKeyPair.privateKey;
        if (cipher == null) {
            throw new RuntimeException("No Cipher presented to decrypt incoming PrivateKey");
        } else {
            byte[] privKeyBytes = null;
            try {
                privKeyBytes = Utilcrmf.encryptedValuedakiSifreliyiAl(cipher, encryptedPrivateKey);
                AsymmetricAlg asymmetricAlg = AsymmetricAlg.fromOID(encryptedPrivateKey.intendedAlg.algorithm.value);
                privateKey = KeyUtil.decodePrivateKey(asymmetricAlg, privKeyBytes);
            } catch (Exception ex) {
                throw new CMPProtocolException("Error while decrypting PrivateKey:" + ex.getMessage(),ex,
                    new ExceptionInfo(PKIFailureInfo.badDataFormat, "Error while encode-decoding Certificate"));
            }
        }
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}
