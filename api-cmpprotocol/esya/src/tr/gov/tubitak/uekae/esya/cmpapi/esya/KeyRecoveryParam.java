package tr.gov.tubitak.uekae.esya.cmpapi.esya;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.asn.cmp.CMPCertificate;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertOrEncCert;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertifiedKeyPair;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIFailureInfo;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertReqMsg;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;
import tr.gov.tubitak.uekae.esya.asn.x509.SubjectPublicKeyInfo;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ExceptionInfo;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.IKeyRecoveryParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base.util.Utilcrmf;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Dec 9, 2010
 * Time: 8:41:00 AM
 * To change this template use File | Settings | File Templates.
 */

public class KeyRecoveryParam implements IKeyRecoveryParam {
    protected static final Logger logger = LoggerFactory.getLogger(KeyRecoveryParam.class);

    private List<Long> sertTalepNos;
    private CertReqMsg certReqMsg;
    private List<Pair<ECertificate, PrivateKey>> certAndPrivKeys;
    private KeyPair keyPair;


    public KeyRecoveryParam(List<Long> sertTalepNos, KeyPair keyPair) {
        this.sertTalepNos = sertTalepNos;
        this.keyPair = keyPair;
    }


    public void addSpecificOperations(CertReqMsg certReqMsg) {
        if (!keyPair.getPublic().getAlgorithm().equalsIgnoreCase(AsymmetricAlg.RSA.getName())) {//private keyimi şifreleyecek anahtar RSA olmalı
            logger.error("Protocol Encrytion key must be RSA, it is:" + keyPair.getPublic().getAlgorithm());
            throw new RuntimeException("Protocol Encrytion key must be RSA, it is:" + keyPair.getPublic().getAlgorithm());
        }
        SubjectPublicKeyInfo proEncKeyInfo = new SubjectPublicKeyInfo();
        try {
            proEncKeyInfo.decode(new Asn1DerDecodeBuffer(keyPair.getPublic().getEncoded()));
        } catch (Exception aEx) {
            throw new RuntimeException("Error whilde encoding SubjectPublicKeyInfo:" + aEx.getMessage(), aEx);
        }
        try {
            Utilcrmf.istegeProtocolEncKeyEkle(certReqMsg.certReq, proEncKeyInfo);
        } catch (Asn1Exception e) {
            throw new RuntimeException("Error while creating CertReqMsg-ProEncrKey" + e.getMessage(), e);
        }
        for (Long sertTalepNo : sertTalepNos)
            try {
                Utilcrmf.istegeSertTalepNoEkle(certReqMsg.certReq, sertTalepNo);
            } catch (Asn1Exception e) {
                throw new RuntimeException("Error while adding SertifikaTalepNo to CertReq:" + e.getMessage(), e);
            }

    }

    public void setCertReq(CertReqMsg certReqMsg) {
        this.certReqMsg = certReqMsg;
    }

    public CertReqMsg getCertReqMsg() {
        return certReqMsg;
    }

    public List<Pair<ECertificate, PrivateKey>> extractResponse(CertifiedKeyPair[] certifiedKeyPairs) throws CMPProtocolException {
        if (sertTalepNos == null || sertTalepNos.size() == 0) {
            logger.info("SertTalepNos are 0, Response has" + certifiedKeyPairs.length + " KeyPair.");
        } else if (certifiedKeyPairs.length != sertTalepNos.size())
            throw new CMPProtocolException(PKIFailureInfo.incorrectData,
                    "Unexpected CertifiedKeyPairs size, expected:" + sertTalepNos.size() + " But it is:" + certifiedKeyPairs.length);

        certAndPrivKeys = new ArrayList<Pair<ECertificate, PrivateKey>>();

        for (int i = 0; i < certifiedKeyPairs.length; i++) {
            if (certifiedKeyPairs[i].certOrEncCert == null) {
                throw new CMPProtocolException(PKIFailureInfo.incorrectData, i + "th CertOrEncCert is empty");
            } else if (certifiedKeyPairs[i].privateKey == null) {
                throw new CMPProtocolException(PKIFailureInfo.incorrectData, i + "th PrivateKey(EncryptedValue) is empty");
            }
            CertOrEncCert certOrEncCert = certifiedKeyPairs[i].certOrEncCert;
            if (certOrEncCert.getChoiceID() != CertOrEncCert._CERTIFICATE)
                throw new CMPProtocolException(PKIFailureInfo.incorrectData,
                        "choice must be CERTIFICATE for incoming CertOrEncCert, its:" + certOrEncCert.getElemName());

            if (!(certOrEncCert.getElement() instanceof CMPCertificate))
                throw new CMPProtocolException(PKIFailureInfo.incorrectData,
                        "CertOrEncCert must have CMPCertificate, but its:" + certOrEncCert.getElement().getClass());

            CMPCertificate cc = (CMPCertificate) certOrEncCert.getElement();
            Pair<ECertificate, PrivateKey> certAndPrivKey = new Pair<ECertificate, PrivateKey>();
            try {
                certAndPrivKey.setObject1(new ECertificate((Certificate) cc.getElement()));
            } catch (Exception ex) {
                throw new CMPProtocolException(ex,
                        new ExceptionInfo(PKIFailureInfo.badDataFormat, "Error while encode-decoding Certificate"));
            }

            BufferedCipher decryptor;
            try {
                decryptor = Crypto.getDecryptor(CipherAlg.RSA_PKCS1);
                decryptor.init(keyPair.getPrivate(), null);
            } catch (CryptoException e) {
                throw new RuntimeException("Error while creating KeyRecoveryParam:" + e.getMessage(), e);
            }
            byte[] privKeyBytes;
            try {

                privKeyBytes = Utilcrmf.encryptedValuedakiSifreliyiAl(decryptor, certifiedKeyPairs[i].privateKey);
            } catch (Exception e) {
                logger.error("Error while Decrypting Private Key:" + e.getMessage(), e);
                throw new CMPProtocolException(PKIFailureInfo.incorrectData, "Error while Decrypting Private Key");
            }
            try {
                AsymmetricAlg pair =  AsymmetricAlg.fromOID(certifiedKeyPairs[i].privateKey.intendedAlg.algorithm.value);
                PrivateKey privKey = KeyUtil.decodePrivateKey(pair, privKeyBytes);
                certAndPrivKey.setObject2(privKey);
            } catch (Exception e) {
                logger.error("Error while Decoding Private Key:" + e.getMessage(), e);
                throw new CMPProtocolException(PKIFailureInfo.incorrectData, "Error while Decrypting Private Key");
            }
            certAndPrivKeys.add(certAndPrivKey);

        }
        return certAndPrivKeys;
    }

    public List<Pair<ECertificate, PrivateKey>> getCertAndPrivKeys() {
        return certAndPrivKeys;
    }
}
