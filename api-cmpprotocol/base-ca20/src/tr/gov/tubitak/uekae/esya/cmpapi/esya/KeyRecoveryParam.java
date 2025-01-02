package tr.gov.tubitak.uekae.esya.cmpapi.esya;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.ECertifiedKeyPair;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIFailureInfo;
import tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8.EPrivateKeyInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseCipher;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu.GNUCryptoProvider;
import tr.gov.tubitak.uekae.esya.asn.cmp.CMPCertificate;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertOrEncCert;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertifiedKeyPair;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertReqMsg;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;
import tr.gov.tubitak.uekae.esya.asn.x509.SubjectPublicKeyInfo;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ExceptionInfo;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.IKeyRecoveryParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.util.Utilcrmf;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
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
    private List<ECertifiedKeyPair> certifiedKeyPairList;


    private PublicKey protocolEncKey;
    private BufferedCipher decryptor=null;

    ECertificate eCertificate;

    public KeyRecoveryParam(List<Long> sertTalepNos, KeyPair keyPair) {
        this.sertTalepNos = sertTalepNos;
        this.protocolEncKey = keyPair.getPublic();
        generateCipher(keyPair);
    }

    public KeyRecoveryParam(List<Long> sertTalepNos, PublicKey protocolEncKey) {
        this.sertTalepNos = sertTalepNos;
        this.protocolEncKey = protocolEncKey;
    }

    public List<ECertifiedKeyPair> getCertifiedKeyPairList() {
        return certifiedKeyPairList;
    }

    public void addSpecificOperations(CertReqMsg certReqMsg) {
        addProtocolEncKeytoRequest(certReqMsg);
        for (Long sertTalepNo : sertTalepNos) {
            try {
                Utilcrmf.istegeSertTalepNoEkle(certReqMsg.certReq, sertTalepNo);
            } catch (Asn1Exception e) {
                throw new ESYARuntimeException("Error while adding SertifikaTalepNo to CertReq:" + e.getMessage(), e);
            }
        }
    }

    private void addProtocolEncKeytoRequest(CertReqMsg certReqMsg) {
        if (!protocolEncKey.getAlgorithm().equalsIgnoreCase(AsymmetricAlg.RSA.getName())) {//private keyimi şifreleyecek anahtar RSA olmalı
            logger.error("Protocol Encrytion key must be RSA, it is:" + protocolEncKey.getAlgorithm());
            throw new ESYARuntimeException("Protocol Encrytion key must be RSA, it is:" + protocolEncKey.getAlgorithm());
        }
        SubjectPublicKeyInfo proEncKeyInfo = new SubjectPublicKeyInfo();
        try {
            proEncKeyInfo.decode(new Asn1DerDecodeBuffer(protocolEncKey.getEncoded()));
        } catch (Exception aEx) {
            throw new ESYARuntimeException("Error whilde encoding SubjectPublicKeyInfo:" + aEx.getMessage(), aEx);
        }
        try {
            Utilcrmf.istegeProtocolEncKeyEkle(certReqMsg.certReq, proEncKeyInfo);
        } catch (Asn1Exception e) {
            throw new ESYARuntimeException("Error while creating CertReqMsg-ProEncrKey" + e.getMessage(), e);
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
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData,
                    "Unexpected CertifiedKeyPairs size, expected:" + sertTalepNos.size() + " But it is:" + certifiedKeyPairs.length);

        certAndPrivKeys = new ArrayList<Pair<ECertificate, PrivateKey>>();
        certifiedKeyPairList = new ArrayList<ECertifiedKeyPair>();

        for (int i = 0; i < certifiedKeyPairs.length; i++) {

            certifiedKeyPairList.add(new ECertifiedKeyPair(certifiedKeyPairs[i]));

            if (certifiedKeyPairs[i].certOrEncCert == null) {
                throw new CMPProtocolException(EPKIFailureInfo.incorrectData, i + "th CertOrEncCert is empty");
            } else if (certifiedKeyPairs[i].privateKey == null) {
                throw new CMPProtocolException(EPKIFailureInfo.incorrectData, i + "th PrivateKey(EncryptedValue) is empty");
            }
            CertOrEncCert certOrEncCert = certifiedKeyPairs[i].certOrEncCert;
            if (certOrEncCert.getChoiceID() != CertOrEncCert._CERTIFICATE)
                throw new CMPProtocolException(EPKIFailureInfo.incorrectData,
                        "choice must be CERTIFICATE for incoming CertOrEncCert, its:" + certOrEncCert.getElemName());

            if (!(certOrEncCert.getElement() instanceof CMPCertificate))
                throw new CMPProtocolException(EPKIFailureInfo.incorrectData,
                        "CertOrEncCert must have CMPCertificate, but its:" + certOrEncCert.getElement().getClass());

            CMPCertificate cc = (CMPCertificate) certOrEncCert.getElement();
            Pair<ECertificate, PrivateKey> certAndPrivKey = new Pair<ECertificate, PrivateKey>();
            try {
                eCertificate = new ECertificate((Certificate) cc.getElement());
                certAndPrivKey.setObject1(eCertificate);
            } catch (Exception ex) {
                throw new CMPProtocolException(ex,
                        new ExceptionInfo(EPKIFailureInfo.badDataFormat, "Error while encode-decoding Certificate"));
            }
            if(decryptor!=null)
                decryptPrivateKey(certifiedKeyPairs[i], certAndPrivKey);
            certAndPrivKeys.add(certAndPrivKey);
        }
        return certAndPrivKeys;
    }

    private void decryptPrivateKey(CertifiedKeyPair certifiedKeyPair, Pair<ECertificate, PrivateKey> certAndPrivKey) throws CMPProtocolException {
        byte[] privKeyBytes;
        try {
            privKeyBytes = Utilcrmf.encryptedValuedakiSifreliyiAl(decryptor, certifiedKeyPair.privateKey);
        } catch (Exception e) {
            logger.error("Error while Decrypting Private Key:" + e.getMessage(), e);
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData, "Error while Decrypting Private Key");
        }

        GNUCryptoProvider gnuCryptoProvider = new GNUCryptoProvider();

        try {
            AsymmetricAlg asymAlg =  AsymmetricAlg.fromOID(certifiedKeyPair.privateKey.intendedAlg.algorithm.value);
            try {
                PrivateKey privKey = gnuCryptoProvider.getKeyFactory().decodePrivateKey(asymAlg,privKeyBytes);
                certAndPrivKey.setObject2(privKey);
            } catch (Exception e) {
                PrivateKey privKey = gnuCryptoProvider.getKeyFactory().decodePrivateKey(asymAlg,new EPrivateKeyInfo(privKeyBytes).getEncoded());
                certAndPrivKey.setObject2(privKey);
                logger.warn("Warning in KeyRecoveryParam", e);
            }
        } catch (Exception e) {
            logger.error("Error while Decoding Private Key:" + e.getMessage(), e);
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData, "Error while Decrypting Private Key");
        }
    }

    private BufferedCipher generateCipher(KeyPair keyPair) {
        GNUCryptoProvider gnuCryptoProvider = new GNUCryptoProvider();
        try {
            decryptor = new BufferedCipher(gnuCryptoProvider.getDecryptor(CipherAlg.RSA_PKCS1));
            decryptor.init(keyPair.getPrivate(), null);
        } catch (CryptoException e) {
            throw new ESYARuntimeException("Error while creating KeyRecoveryParam:" + e.getMessage(), e);
        }
        return decryptor;
    }

    public List<Pair<ECertificate, PrivateKey>> getCertAndPrivKeys() {
        return certAndPrivKeys;
    }
}
