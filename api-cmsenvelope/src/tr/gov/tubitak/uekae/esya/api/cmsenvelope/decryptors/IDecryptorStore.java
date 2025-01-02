package tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.params.IDecryptorParams;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;

import javax.crypto.SecretKey;


public interface IDecryptorStore {

    SecretKey decrypt(ECertificate aCert, IDecryptorParams aParams) throws CryptoException;

    ECertificate[] getEncryptionCertificates() throws CryptoException;

    byte[] decrypt(CipherAlg simAlg, AlgorithmParams simAlgParams, byte[] encryptedContent, SecretKey anahtar) throws CryptoException;
}
